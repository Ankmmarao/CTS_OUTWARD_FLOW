package com.cts.outward.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cheque {
	private String chequeNumber;
    private String batchNumber;
    private String accountNumber;
    private String drawerName;
    private BigDecimal amount;
    private String micrCode;
    private String ifscCode;
    private String status;
    private LocalDate checkedDate;
    private String frontImagePath;
    private String backImagePath;
//	public Cheque(String chequeNumber, String batchNumber, String accountNumber, String drawerName, BigDecimal amount,
//			String micrCode, String ifscCode, String status, LocalDate checkedDate, String frontImagePath,
//			String backImagePath) {
//		super();
//		this.chequeNumber = chequeNumber;
//		this.batchNumber = batchNumber;
//		this.accountNumber = accountNumber;
//		this.drawerName = drawerName;
//		this.amount = amount;
//		this.micrCode = micrCode;
//		this.ifscCode = ifscCode;
//		this.status = status;
//		this.checkedDate = checkedDate;
//		this.frontImagePath = frontImagePath;
//		this.backImagePath = backImagePath;
//	}
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
	public String getDrawerName() {
		return drawerName;
	}
	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getCheckedDate() {
		return checkedDate;
	}
	public void setCheckedDate(LocalDate checkedDate) {
		this.checkedDate = checkedDate;
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
		return "Cheque [chequeNumber=" + chequeNumber + ", batchNumber=" + batchNumber + ", accountNumber="
				+ accountNumber + ", drawerName=" + drawerName + ", amount=" + amount + ", micrCode=" + micrCode
				+ ", ifscCode=" + ifscCode + ", status=" + status + ", checkedDate=" + checkedDate + ", frontImagePath="
				+ frontImagePath + ", backImagePath=" + backImagePath + "]";
	}
    
}