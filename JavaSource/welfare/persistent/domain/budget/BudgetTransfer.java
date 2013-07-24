package welfare.persistent.domain.budget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import welfare.persistent.customtype.BudgetTransferStatus;

public class BudgetTransfer {
	public static final DecimalFormat BTNUMBER_FORMATTER = new DecimalFormat("0000");
	
	private Long id;
	private int transferNumber;
	private String externalTransferNumber;
	private BigDecimal requestAmount = new BigDecimal("0.00");
	private BigDecimal approveAmount = new BigDecimal("0.00");
	private BigDecimal oldAmount = new BigDecimal("0.00");
	private String reason;
	private Date requestDate;
	private Date approveDate;
	private BudgetItem fromBudgetItem;
	private BudgetItem toBudgetItem;
	private BudgetItem oldFromBudgetItem;
	private BudgetItem oldToBudgetItem;
	private BudgetTransferStatus status = BudgetTransferStatus.REQUESTING; // R=REQUESTING A=APPROVED D=DISCARD

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTransferNumber() {
		return transferNumber;
	}

	public void setTransferNumber(int transferNumber) {
		this.transferNumber = transferNumber;
	}

	public String getExternalTransferNumber() {
		return externalTransferNumber;
	}

	public void setExternalTransferNumber(String externalTransferNumber) {
		this.externalTransferNumber = externalTransferNumber;
	}

	public BigDecimal getRequestAmount() {
		return requestAmount;
	}

	public void setRequestAmount(BigDecimal amount) {
		this.requestAmount = amount.setScale(2,BigDecimal.ROUND_HALF_UP);;
	}

	public BigDecimal getApproveAmount() {
		return approveAmount;
	}

	public void setApproveAmount(BigDecimal approveAmount) {
		this.approveAmount = approveAmount.setScale(2,BigDecimal.ROUND_HALF_UP);;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public BudgetItem getFromBudgetItem() {
		return fromBudgetItem;
	}

	public void setFromBudgetItem(BudgetItem fromBudgetItem) {
		this.fromBudgetItem = fromBudgetItem;
	}

	public BudgetItem getToBudgetItem() {
		return toBudgetItem;
	}

	public void setToBudgetItem(BudgetItem toBudgetItem) {
		this.toBudgetItem = toBudgetItem;
	}

	public BudgetItem getOldFromBudgetItem() {
		return oldFromBudgetItem;
	}

	public void setOldFromBudgetItem(BudgetItem oldFromBudgetItem) {
		this.oldFromBudgetItem = oldFromBudgetItem;
	}

	public BudgetItem getOldToBudgetItem() {
		return oldToBudgetItem;
	}

	public void setOldToBudgetItem(BudgetItem oldToBudgetItem) {
		this.oldToBudgetItem = oldToBudgetItem;
	}

	public BudgetTransferStatus getStatus() {
		return status;
	}

	public void setStatus(BudgetTransferStatus status) {
		this.status = status;
	}
	
	public boolean isApproved(){
		if (BudgetTransferStatus.APPROVED.equals(status)){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDiscarded(){
		if (BudgetTransferStatus.DISCARDED.equals(status)){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isRequesting(){
		if (BudgetTransferStatus.REQUESTING.equals(status)){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isApproveable(){
		return isRequesting();
	}
	
	public boolean isDiscardable(){
		return isRequesting();
	}
		
	public boolean isChangeFromBudgetItem(){
		return !fromBudgetItem.getId().equals(oldFromBudgetItem.getId());
	}
	
	public boolean isChangeToBudgetItem(){
		return !toBudgetItem.getId().equals(oldToBudgetItem.getId());
	}
	
	public String getFormattedTransferNumber(){
		return BTNUMBER_FORMATTER.format(transferNumber);
	}

	public BigDecimal getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(BigDecimal oldAmount) {
		this.oldAmount = oldAmount;
	}

}
