package com.kmichali;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="date")
public class Date {
    private int idDate;
    private String issueDate;
    private String sellDate;
    private String paymentDate;
    private String paidDate;
}

