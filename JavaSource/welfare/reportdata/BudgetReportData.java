package welfare.reportdata;

import java.math.BigDecimal;

public class BudgetReportData {
	private String accountCode = "";
	private String category = "";
	private BigDecimal initialAmount = new BigDecimal("0.00");
	private BigDecimal reservedAmount = new BigDecimal("0.00");
	private BigDecimal expensedAmount = new BigDecimal("0.00");
	private BigDecimal transferInAmount = new BigDecimal("0.00");
	private BigDecimal transferOutAmount = new BigDecimal("0.00");
	private BigDecimal availableAmount = new BigDecimal("0.00");
	
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getInitialAmount() {
		return initialAmount;
	}
	public void setInitialAmount(BigDecimal initialAmount) {
		this.initialAmount = initialAmount;
	}
	public BigDecimal getReservedAmount() {
		return reservedAmount;
	}
	public void setReservedAmount(BigDecimal reservedAmount) {
		this.reservedAmount = reservedAmount;
	}
	public BigDecimal getExpensedAmount() {
		return expensedAmount;
	}
	public void setExpensedAmount(BigDecimal expensedAmount) {
		this.expensedAmount = expensedAmount;
	}
	public BigDecimal getTransferInAmount() {
		return transferInAmount;
	}
	public void setTransferInAmount(BigDecimal transferInAmount) {
		this.transferInAmount = transferInAmount;
	}
	public BigDecimal getTransferOutAmount() {
		return transferOutAmount;
	}
	public void setTransferOutAmount(BigDecimal transferOutAmount) {
		this.transferOutAmount = transferOutAmount;
	}
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
	
}
