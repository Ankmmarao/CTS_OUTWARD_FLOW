package com.cts.outward.model;

import java.math.BigDecimal;

public class Cheque {

    private String chequeNumber;
    private String batchNumber;
    private String accountNumber;
    private String branchCode;
    private String payeeName;
    private BigDecimal amount;
    private String frontImagePath;
    private String backImagePath;

    public Cheque() {
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFrontImagePath() {
        return frontImagePath;
    }

    public void setFrontImagePath(String frontImagePath) {
        this.frontImagePath = frontImagePath;
    }

    public String getBackImagePath() {
        return backImagePath;
    }

    public void setBackImagePath(String backImagePath) {
        this.backImagePath = backImagePath;
    }

    @Override
    public String toString() {
        return "Cheque{" +
                "chequeNumber='" + chequeNumber + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", payeeName='" + payeeName + '\'' +
                ", amount=" + amount +
                ", frontImagePath='" + frontImagePath + '\'' +
                ", backImagePath='" + backImagePath + '\'' +
                '}';
    }
}