package org.topcatv.devops.model;

import org.springframework.security.core.GrantedAuthority;
import org.topcatv.devops.support.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author liuyi
 */
@Entity
public class Authority extends BaseModel implements GrantedAuthority {

    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    @Column(length = 20, nullable = false, unique = true)
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
