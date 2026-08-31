package com.cts.outward.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Batch {


    private String batchNumber;
    private String branchCode;
    private String branchName;
    private Integer createdBy;
    private LocalDate scheduleDate;
    private LocalDateTime submittedDate;
    private LocalDateTime completedDate;
    private Integer totalCheques;
//	public Batch(String batchNumber, String branchCode, String branchName, Integer createdBy, LocalDate scheduleDate,
//			LocalDateTime submittedDate, LocalDateTime completedDate, Integer totalCheques) {
//		super();
//		this.batchNumber = batchNumber;
//		this.branchCode = branchCode;
//		this.branchName = branchName;
//		this.createdBy = createdBy;
//		this.scheduleDate = scheduleDate;
//		this.submittedDate = submittedDate;
//		this.completedDate = completedDate;
//		this.totalCheques = totalCheques;
//	}
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
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDate getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(LocalDate scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public LocalDateTime getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(LocalDateTime submittedDate) {
		this.submittedDate = submittedDate;
	}
	public LocalDateTime getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(LocalDateTime completedDate) {
		this.completedDate = completedDate;
	}
	public Integer getTotalCheques() {
		return totalCheques;
	}
	public void setTotalCheques(Integer totalCheques) {
		this.totalCheques = totalCheques;
	}
	@Override
	public String toString() {
		return "Batch [batchNumber=" + batchNumber + ", branchCode=" + branchCode + ", branchName=" + branchName
				+ ", createdBy=" + createdBy + ", scheduleDate=" + scheduleDate + ", submittedDate=" + submittedDate
				+ ", completedDate=" + completedDate + ", totalCheques=" + totalCheques + "]";
	}
    
}