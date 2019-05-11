package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="date")
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_date", updatable = false, nullable = false)
    private long id;
    @Column(name = "issue_date")
    private String issueDate;
    @Column(name = "sell_date")
    private String sellDate;
    @Column(name = "payment_date")
    private String paymentDate;
    @Column(name = "paid_date")
    private String paidDate;

    @OneToOne(mappedBy = "date",cascade = CascadeType.ALL)
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public long getId() {
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

