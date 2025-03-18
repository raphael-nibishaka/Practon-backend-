package com.unbeaten.Practon.models;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    // Many-to-One relationship with Person (the owner of the skill)
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person owner;

    // Default constructor
    public Skill() {
    }

    // Constructor with all fields
    public Skill(String name, String description, Person owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
