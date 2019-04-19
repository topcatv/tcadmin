package org.topcatv.devops;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.topcatv.devops.model.Authority;
import org.topcatv.devops.repository.AuthorityRepository;
import org.topcatv.devops.model.User;
import org.topcatv.devops.repository.UserRepository;
import org.topcatv.devops.support.PassUtil;

import javax.persistence.EntityManagerFactory;

/**
 * @Author liuyi
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
		AuthorityRepository authorityRepository = context.getBean(AuthorityRepository.class);

		Authority adminAuthority = getOrGreateAuthority("ROLE_ADMIN", authorityRepository);
		Authority basicAuthority = getOrGreateAuthority("ROLE_BASIC", authorityRepository);

		User admin = new User("admin", "123456");
		encodePassword(admin);
		admin.getAuthorities().add(adminAuthority);
		admin.getAuthorities().add(basicAuthority);

		User test = new User("test", "test");
		encodePassword(test);
		test.getAuthorities().add(basicAuthority);

		userRepository.save(admin);
		userRepository.save(test);
	}

	private static void encodePassword(User user) {
		user.setPassword(PassUtil.encode(user.getUsername(), user.getPassword()));
	}

	private static Authority getOrGreateAuthority(String authorityText, AuthorityRepository repository) {
		Authority authority = repository.findByAuthority(authorityText);
		if (authority == null) {
			authority = new Authority(authorityText);
			repository.save(authority);
		}
		return authority;
	}

	@Bean
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	public SessionFactory sessionFactory(EntityManagerFactory emf) {
		return emf.unwrap(SessionFactory.class);
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:org/springframework/security/messages");

		ReloadableResourceBundleMessageSource messageSourceLocal = new ReloadableResourceBundleMessageSource();
		messageSourceLocal.setBasename("classpath:messages");
		messageSourceLocal.setParentMessageSource(messageSource);

		return messageSourceLocal;
	}

}
