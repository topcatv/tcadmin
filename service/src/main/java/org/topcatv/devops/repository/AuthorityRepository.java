package org.topcatv.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.topcatv.devops.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthority(String authority);

}
