package org.topcatv.devops.model;

import org.topcatv.devops.support.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Permission extends BaseModel {

    private String name;
    private Long pid;
    private String url;
    private String description;

    @Column(length = 20, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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
}
