package org.topcatv.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.topcatv.devops.model.Authority;
import org.topcatv.devops.model.User;
import org.topcatv.devops.repository.AuthorityRepository;
import org.topcatv.devops.repository.UserRepository;

/**
 * @Author liuyi
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DevopsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DevopsApplication.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
		AuthorityRepository authorityRepository = context.getBean(AuthorityRepository.class);
		BCryptPasswordEncoder encoder = context.getBean(BCryptPasswordEncoder.class);

		Authority adminAuthority = getOrCreateAuthority("ROLE_ADMIN", authorityRepository);
		Authority basicAuthority = getOrCreateAuthority("ROLE_BASIC", authorityRepository);

		User admin = new User("admin", "123456");
		encodePassword(admin, encoder);
		admin.getAuthorities().add(adminAuthority);
		admin.getAuthorities().add(basicAuthority);

		User test = new User("test", "test");
		encodePassword(test, encoder);
		test.getAuthorities().add(basicAuthority);

		userRepository.save(admin);
		userRepository.save(test);
	}

	private static void encodePassword(User user, BCryptPasswordEncoder encoder) {
		user.setPassword(encoder.encode(user.getPassword()));
	}

	private static Authority getOrCreateAuthority(String authorityText, AuthorityRepository repository) {
		Authority authority = repository.findByAuthority(authorityText);
		if (authority == null) {
			authority = new Authority(authorityText);
			repository.save(authority);
		}
		return authority;
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
