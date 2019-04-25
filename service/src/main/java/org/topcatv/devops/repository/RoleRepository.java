package org.topcatv.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.topcatv.devops.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
