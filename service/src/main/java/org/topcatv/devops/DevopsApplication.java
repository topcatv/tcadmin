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
import org.topcatv.devops.model.Permission;
import org.topcatv.devops.model.Role;
import org.topcatv.devops.model.User;
import org.topcatv.devops.repository.PermissionRepository;
import org.topcatv.devops.repository.RoleRepository;
import org.topcatv.devops.repository.UserRepository;

/**
 * @Author liuyi
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DevopsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DevopsApplication.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
		RoleRepository roleRepository = context.getBean(RoleRepository.class);
		PermissionRepository permissionRepository = context.getBean(PermissionRepository.class);
		BCryptPasswordEncoder encoder = context.getBean(BCryptPasswordEncoder.class);

		Permission permission1 = getOrCreatePermission("a", "/a", permissionRepository);
		Permission permission2 = getOrCreatePermission("b", "/a/b", permissionRepository);
		permission2.setParent(permission1);

		Role roleAdmin = getOrCreateRole("ROLE_ADMIN", roleRepository);
		roleAdmin.getPermissions().add(permission1);
		roleAdmin.getPermissions().add(permission2);
		roleRepository.save(roleAdmin);
		Role roleBasic = getOrCreateRole("ROLE_BASIC", roleRepository);
		roleBasic.getPermissions().add(permission1);
		roleRepository.save(roleBasic);

		User admin = new User("admin", "123456");
		encodePassword(admin, encoder);
		admin.getRoles().add(roleAdmin);
		admin.getRoles().add(roleBasic);

		User test = new User("test", "test");
		encodePassword(test, encoder);
		test.getRoles().add(roleBasic);

		userRepository.save(admin);
		userRepository.save(test);
	}

	private static Permission getOrCreatePermission(String name, String url, PermissionRepository repository) {
		Permission permission = repository.findByName(name);
		if(permission == null) {
			permission = new Permission();
			permission.setName(name);
			permission.setUrl(url);
			repository.save(permission);
		}
		return permission;
	}

	private static void encodePassword(User user, BCryptPasswordEncoder encoder) {
		user.setPassword(encoder.encode(user.getPassword()));
	}

	private static Role getOrCreateRole(String roleText, RoleRepository repository) {
		Role role = repository.findByName(roleText);
		if (role == null) {
			role = new Role(roleText);
			repository.save(role);
		}
		return role;
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
