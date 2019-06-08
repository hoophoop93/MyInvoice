package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_settings", updatable = false, nullable = false)
    private long id;
    @Column(name = "path")
    private String path;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
