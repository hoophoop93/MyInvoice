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

    @OneToOne(mappedBy = "date",cascade = CascadeType.ALL)
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }
}

