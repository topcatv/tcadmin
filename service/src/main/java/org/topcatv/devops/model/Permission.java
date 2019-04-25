package org.topcatv.devops.model;

import org.topcatv.devops.support.BaseModel;

import javax.persistence.*;

@Entity
public class Permission extends BaseModel {

    private String name;
    private Permission parent;
    private String url;
    private String description;

    @Column(length = 20, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name="parent_id")
    public Permission getParent() {
        return parent;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
    }
}
