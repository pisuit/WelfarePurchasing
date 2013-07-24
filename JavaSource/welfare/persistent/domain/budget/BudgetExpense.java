package welfare.persistent.domain.budget;

import java.math.BigDecimal;
import java.util.Date;

import welfare.persistent.domain.purchasing.Vendor;

public class BudgetExpense {
	private Long id;
	private Date postingDate;
	private String invoiceNumber="";
	private BigDecimal amount =  new BigDecimal("0.00");
	private String detail="";
	private BudgetItem budgetItem;
	private Vendor vendor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(2,BigDecimal.ROUND_HALF_UP);;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public BudgetItem getBudgetItem() {
		return budgetItem;
	}

	public void setBudgetItem(BudgetItem budgetItem) {
		this.budgetItem = budgetItem;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

}
