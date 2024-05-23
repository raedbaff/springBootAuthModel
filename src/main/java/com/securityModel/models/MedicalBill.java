package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity

public class MedicalBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount=0.0;
    @ManyToOne
    @JsonIncludeProperties({"id","username"})
    private Doctor doc;
    @ManyToOne
    @JsonIncludeProperties({"id","username"})
    private Patient patient;
    private String date;
    private String Description;
    private Double Tax=0.0;
    private Double Discount=0.0;
    private Double Total=amount+Tax-Discount;
    @OneToOne
    @JsonIncludeProperties("id")
    private RendezVous rendezvous;

    public Double getTax() {
        return Tax;
    }

    public RendezVous getRendezvous() {
        return rendezvous;
    }

    public void setRendezvous(RendezVous rendezvous) {
        this.rendezvous = rendezvous;
    }

    public void setTax(Double tax) {
        Tax = tax;
        calculateTotal();
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
        calculateTotal();
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public MedicalBill() {
    }

    public MedicalBill(Long id, Double amount, Doctor doc, Patient patient, String date, String description, Double tax, Double discount) {
        this.id = id;
        this.amount = amount;
        this.doc = doc;
        this.patient = patient;
        this.date = date;
        Description = description;
        Tax = tax;
        Discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
        calculateTotal();
    }

    public Doctor getDoc() {
        return doc;
    }

    public void setDoc(Doctor doc) {
        this.doc = doc;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    private void calculateTotal() {
        this.Total = this.amount + this.Tax - this.Discount;
    }
}