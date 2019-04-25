package org.topcatv.devops.model;

import lombok.EqualsAndHashCode;
import org.topcatv.devops.support.BaseModel;

import javax.persistence.*;
import java.util.List;

/**
 * @author liuyi
 */
@Entity
public class Role extends BaseModel {

    private String name;

    private List<Permission> permissions;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Column(length = 20, nullable = false, unique = true)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "permission_id")})
    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
