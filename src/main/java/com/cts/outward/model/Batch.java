package com.cts.outward.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Batch {

    // ==========================================
    // BATCH NUMBER
    // ==========================================

    private String batchNumber;

    // ==========================================
    // BRANCH CODE
    // ==========================================

    private String branchCode;

    // ==========================================
    // BRANCH NAME
    // ==========================================

    private String branchName;

    // ==========================================
    // CREATED BY
    // ==========================================

    private Integer createdBy;

    // ==========================================
    // SCHEDULE DATE
    // ==========================================

    private LocalDate scheduleDate;

    // ==========================================
    // SUBMITTED DATE
    // ==========================================

    private LocalDateTime submittedDate;

    // ==========================================
    // COMPLETED DATE
    // ==========================================

    private LocalDateTime completedDate;

    // ==========================================
    // TOTAL CHEQUES
    // ==========================================

    private Integer totalCheques;

    // ==========================================
    // BATCH STATUS
    // Database column: batch_status
    // ==========================================

    private String batchStatus;


    // ==========================================
    // GET BATCH NUMBER
    // ==========================================

    public String getBatchNumber() {

        return batchNumber;
    }


    // ==========================================
    // SET BATCH NUMBER
    // ==========================================

    public void setBatchNumber(String batchNumber) {

        this.batchNumber = batchNumber;
    }


    // ==========================================
    // GET BRANCH CODE
    // ==========================================

    public String getBranchCode() {

        return branchCode;
    }


    // ==========================================
    // SET BRANCH CODE
    // ==========================================

    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }


    // ==========================================
    // GET BRANCH NAME
    // ==========================================

    public String getBranchName() {

        return branchName;
    }


    // ==========================================
    // SET BRANCH NAME
    // ==========================================

    public void setBranchName(String branchName) {

        this.branchName = branchName;
    }


    // ==========================================
    // GET CREATED BY
    // ==========================================

    public Integer getCreatedBy() {

        return createdBy;
    }


    // ==========================================
    // SET CREATED BY
    // ==========================================

    public void setCreatedBy(Integer createdBy) {

        this.createdBy = createdBy;
    }


    // ==========================================
    // GET SCHEDULE DATE
    // ==========================================

    public LocalDate getScheduleDate() {

        return scheduleDate;
    }


    // ==========================================
    // SET SCHEDULE DATE
    // ==========================================

    public void setScheduleDate(LocalDate scheduleDate) {

        this.scheduleDate = scheduleDate;
    }


    // ==========================================
    // GET SUBMITTED DATE
    // ==========================================

    public LocalDateTime getSubmittedDate() {

        return submittedDate;
    }


    // ==========================================
    // SET SUBMITTED DATE
    // ==========================================

    public void setSubmittedDate(LocalDateTime submittedDate) {

        this.submittedDate = submittedDate;
    }


    // ==========================================
    // GET COMPLETED DATE
    // ==========================================

    public LocalDateTime getCompletedDate() {

        return completedDate;
    }


    // ==========================================
    // SET COMPLETED DATE
    // ==========================================

    public void setCompletedDate(LocalDateTime completedDate) {

        this.completedDate = completedDate;
    }


    // ==========================================
    // GET TOTAL CHEQUES
    // ==========================================

    public Integer getTotalCheques() {

        return totalCheques;
    }


    // ==========================================
    // SET TOTAL CHEQUES
    // ==========================================

    public void setTotalCheques(Integer totalCheques) {

        this.totalCheques = totalCheques;
    }


    // ==========================================
    // GET BATCH STATUS
    // ==========================================

    public String getBatchStatus() {

        return batchStatus;
    }


    // ==========================================
    // SET BATCH STATUS
    // ==========================================

    public void setBatchStatus(String batchStatus) {

        this.batchStatus = batchStatus;
    }


    // ==========================================
    // TO STRING
    // ==========================================

    @Override
    public String toString() {

        return "Batch ["
                + "batchNumber=" + batchNumber
                + ", branchCode=" + branchCode
                + ", branchName=" + branchName
                + ", createdBy=" + createdBy
                + ", scheduleDate=" + scheduleDate
                + ", submittedDate=" + submittedDate
                + ", completedDate=" + completedDate
                + ", totalCheques=" + totalCheques
                + ", batchStatus=" + batchStatus
                + "]";
    }
}