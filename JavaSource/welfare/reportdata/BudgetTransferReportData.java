package welfare.reportdata;

import java.math.BigDecimal;
import java.util.Date;

public class BudgetTransferReportData {
	private int transferNumber;
	private Date approveDate;
	private String categoryFrom;
	private String categoryTo;
	private BigDecimal transferInAmount = new BigDecimal("0.00");
	private BigDecimal transferOutAmount = new BigDecimal("0.00");
	private BigDecimal totalAmount = new BigDecimal("0.00");
	public int getTransferNumber() {
		return transferNumber;
	}
	public void setTransferNumber(int transferNumber) {
		this.transferNumber = transferNumber;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	public String getCategoryFrom() {
		return categoryFrom;
	}
	public void setCategoryFrom(String categoryFrom) {
		this.categoryFrom = categoryFrom;
	}
	public String getCategoryTo() {
		return categoryTo;
	}
	public void setCategoryTo(String categoryTo) {
		this.categoryTo = categoryTo;
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
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
}
