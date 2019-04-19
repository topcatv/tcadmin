package org.topcatv.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.topcatv.devops.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
