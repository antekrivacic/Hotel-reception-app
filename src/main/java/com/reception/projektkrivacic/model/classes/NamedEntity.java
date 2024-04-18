package com.reception.projektkrivacic.model.classes;

public abstract class NamedEntity {

    private Long id;
    private String name;

    public NamedEntity(Long id, String name) {
        this.name = name;
        this.id = id;
    }
    public NamedEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
