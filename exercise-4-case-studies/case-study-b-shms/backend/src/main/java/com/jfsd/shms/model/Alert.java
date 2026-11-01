package com.jfsd.shms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

/**
 * Raised when a reading has a value outside the safe range.
 */
@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reading_id")
    private VitalReading reading;

    private String parameter;

    @Column(name = "reading_value")
    private String value;

    @Column(length = 300)
    private String message;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean acknowledged;

    public Alert() {
    }

    public Alert(Patient patient, VitalReading reading, String parameter, String value,
                 String message, Severity severity) {
        this.patient = patient;
        this.reading = reading;
        this.parameter = parameter;
        this.value = value;
        this.message = message;
        this.severity = severity;
    }

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public VitalReading getReading() {
        return reading;
    }

    public String getParameter() {
        return parameter;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
}
