package org.topcatv.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.topcatv.devops.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);
}
