package org.topcatv.devops.model;

import org.topcatv.devops.support.BaseModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liuyi
 */
@Entity
public class User extends BaseModel {

    private Set<Role> roles = new HashSet<>();
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<Role> getRoles() {
        return this.roles;
    }

    @Column(length = 50, unique = true, nullable = false)
    public String getUsername() {
        return this.username;
    }

    @Column(length = 150, nullable = false)
    public String getPassword() {
        return this.password;
    }

    @Column(nullable = false)
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Column(nullable = false)
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Column(nullable = false)
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Column(nullable = false)
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
