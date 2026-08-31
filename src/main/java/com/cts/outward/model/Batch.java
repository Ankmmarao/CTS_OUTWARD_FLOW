package com.cts.outward.model;

import java.time.LocalDateTime;

public class Batch {

    private String batchNumber;

    private String branchCode;

    private String branchName;

    private LocalDateTime captureDate;

    private Integer createdBy;

    private Integer totalCheque;

    private String status;

    public Batch() {
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public LocalDateTime getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(LocalDateTime captureDate) {
        this.captureDate = captureDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getTotalCheque() {
        return totalCheque;
    }

    public void setTotalCheque(Integer totalCheque) {
        this.totalCheque = totalCheque;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchNumber='" + batchNumber + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", branchName='" + branchName + '\'' +
                ", captureDate=" + captureDate +
                ", createdBy=" + createdBy +
                ", totalCheque=" + totalCheque +
                ", status='" + status + '\'' +
                '}';
    }
}