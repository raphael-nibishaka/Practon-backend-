package com.unbeaten.Practon.models;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private PaymentDetails paymentDetails;

    // Many-to-One relationship with Person (the payer)
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person payer;

    // Many-to-One relationship with Skill (or Course)
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    // Default constructor
    public Payment() {
    }

    // Constructor with all fields
    public Payment(PaymentDetails paymentDetails, Person payer, Skill skill) {
        this.paymentDetails = paymentDetails;
        this.payer = payer;
        this.skill = skill;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Person getPayer() {
        return payer;
    }

    public void setPayer(Person payer) {
        this.payer = payer;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
