package com.unbeaten.Practon.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String password;
    private String fieldOfInterest;

    @Embedded
    private Address address; // Embedded Address field

    // One-to-Many relationship with Certification
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Certification> certifications;

    // Default constructor
    public Person() {
    }

    // Constructor with all fields
    public Person(String firstname, String lastname, String email, Address address) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
    }

    // Constructor without code (useful for inserting new records)
    public Person(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCode() {
        return id;
    }

    public void setCode(Integer code) {
        this.id = code;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public String getFieldOfInterest() {
        return fieldOfInterest;
    }

    public void setFieldOfInterest(String fieldOfInterest) {
        this.fieldOfInterest = fieldOfInterest;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    // Add a method to get certifications without owner reference
    @Transient
    public List<Certification> getCertificationsWithoutOwner() {
        if (certifications != null) {
            certifications.forEach(cert -> cert.setOwner(null));
        }
        return certifications;
    }
}
