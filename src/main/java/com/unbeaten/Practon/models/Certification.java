package com.unbeaten.Practon.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String issuingOrganization;

    @Column(nullable = false)
    private String dateIssued;

    // Many-to-One relationship with Person (the owner of the certification)
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonIgnore
    private Person owner;

    // Many-to-One relationship with Skill (the skill related to the certification)
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    // Default constructor
    public Certification() {
    }

    // Constructor with all fields
    public Certification(String name, String issuingOrganization, String dateIssued, Person owner, Skill skill) {
        this.name = name;
        this.issuingOrganization = issuingOrganization;
        this.dateIssued = dateIssued;
        this.owner = owner;
        this.skill = skill;
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

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    // Add a method to get owner without certifications
    @Transient
    public Person getOwnerWithoutCertifications() {
        if (owner != null) {
            owner.setCertifications(null);
        }
        return owner;
    }
}
