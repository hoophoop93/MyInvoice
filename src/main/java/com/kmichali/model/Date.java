package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="date")
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idDate", updatable = false, nullable = false)
    private int id;
    private String issueDate;
    private String sellDate;
    private String paymentDate;
    private String paidDate;
}

