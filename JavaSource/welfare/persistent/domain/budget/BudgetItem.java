package welfare.persistent.domain.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import welfare.persistent.customtype.BudgetItemType;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.purchasing.GoodsReceiptItem;
import welfare.persistent.domain.purchasing.PurchaseOrder;
import welfare.persistent.domain.purchasing.PurchaseOrderItem;
import welfare.persistent.domain.purchasing.PurchaseRequisitionItem;

public class BudgetItem {
	private Long id;
	private String accountCode;										// รหัสงบประมาณ
	private String category="";										// หมวดงบประมาณ
	private BigDecimal initialAmount = new BigDecimal("0.00");		// วงเงิน
	private BigDecimal reservedAmount = new BigDecimal("0.00");		// จำนวนเงินกันงบ
	private BigDecimal expensedAmount = new BigDecimal("0.00");		// จำนวนที่ใช้จริง 
	private BigDecimal transferInAmount = new BigDecimal("0.00");	// จำนวนเงินที่มีการโอนเข้า
	private BigDecimal transferOutAmount = new BigDecimal("0.00");	// จำนวนเงินที่มีการโอนออก
	private BudgetItemType budgetType = BudgetItemType.M;		// ประเภทงบประมาณ (M=หมวดหลัก, S=หมวดย่อย)
	private Status status = Status.NORMAL;			// สถานะงบประมาณ (X=ลบ, N=ปกติ)
	private String budgetLevel;
	private boolean isAvailable = true;							// สามารถนำไปใช้ได้หรือไม่ (true=ได้, false=ไม่ได้)
	private boolean isEditable = true;								// สามารถแก้ไขได้อีก? (true=ได้, false=ไม่ได้)
	private boolean isControlled = false;							// ควบคุมงบประมาณ
	private boolean isMonthlyBudget = false;						// ควบคุมงบรายเดือน
	private boolean isPurchasingBudget = true;						// ใช้กับ PR/PO
	private boolean isExpenseEntry = true;							// ใชักับการบันทึกค่าใช้จ่ายโดยไม่ผ่าน PR/PO
	private boolean isNurseryBudget = false;						// ใช้สำหรับกำหนดค่าอาหารเด็ก
	private Budget budget;											// งบประมาณประจำปี
	private BudgetItem parentBudgetItem;								// หมวดหลัก
	private List<BudgetItem> subBudgetItems = new ArrayList<BudgetItem>(); // หมวดย่อย
	private List<BudgetTransfer> fromBudgetTransfers = new ArrayList<BudgetTransfer>(); // รายการขอโอนเข้า
	private List<BudgetTransfer> toBudgetTransfers = new ArrayList<BudgetTransfer>(); // รายการขอโอนออก
	private List<BudgetExpense> budgetExpenses = new ArrayList<BudgetExpense>(); // รายการบันทึกค่าใช้จ่าย
	private List<PurchaseRequisitionItem> purchaseRequisitionItems = new ArrayList<PurchaseRequisitionItem>(); // รายการไอเทมในใบแจ้ง
	private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<PurchaseOrderItem>(); // รายการไอเทมในใบสั่งซื้อ
	private List<GoodsReceiptItem> goodsReceiptItems = new ArrayList<GoodsReceiptItem>(); // รายการไอเทมในใบรับวัสดุ
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
		this.initialAmount = initialAmount.setScale(2,BigDecimal.ROUND_HALF_UP);;
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

	public BudgetItemType getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(BudgetItemType budgetType) {
		this.budgetType = budgetType;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setControlled(boolean isControlled) {
		this.isControlled = isControlled;
	}

	public boolean isControlled() {
		return isControlled;
	}

	public void setMonthlyBudget(boolean isMonthlyBudget) {
		this.isMonthlyBudget = isMonthlyBudget;
	}

	public boolean isMonthlyBudget() {
		return isMonthlyBudget;
	}

	public void setPurchasingBudget(boolean isPurchasingBudget) {
		this.isPurchasingBudget = isPurchasingBudget;
	}

	public boolean isPurchasingBudget() {
		return isPurchasingBudget;
	}

	public void setExpenseEntry(boolean isExpenseEntry) {
		this.isExpenseEntry = isExpenseEntry;
	}

	public boolean isExpenseEntry() {
		return isExpenseEntry;
	}

	public void setNurseryBudget(boolean isNurseryBudget) {
		this.isNurseryBudget = isNurseryBudget;
	}

	public boolean isNurseryBudget() {
		return isNurseryBudget;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BudgetItem getParentBudgetItem() {
		return parentBudgetItem;
	}

	public void setParentBudgetItem(BudgetItem parentCategory) {
		this.parentBudgetItem = parentCategory;
	}

	public List<BudgetItem> getSubBudgetItems() {
		return subBudgetItems;
	}

	public List<BudgetTransfer> getFromBudgetTransfers() {
		return fromBudgetTransfers;
	}

	public void setFromBudgetTransfers(List<BudgetTransfer> fromBudgetTransfers) {
		this.fromBudgetTransfers = fromBudgetTransfers;
	}

	public List<BudgetTransfer> getToBudgetTransfers() {
		return toBudgetTransfers;
	}

	public void setToBudgetTransfers(List<BudgetTransfer> toBudgetTransfers) {
		this.toBudgetTransfers = toBudgetTransfers;
	}

	public void setSubBudgetItems(List<BudgetItem> subCategorys) {
		this.subBudgetItems = subCategorys;
	}
	
	public List<BudgetExpense> getBudgetExpenses() {
		return budgetExpenses;
	}

	public void setBudgetExpenses(List<BudgetExpense> budgetExpenses) {
		this.budgetExpenses = budgetExpenses;
	}

	public List<PurchaseRequisitionItem> getPurchaseRequisitionItems() {
		return purchaseRequisitionItems;
	}

	public void setPurchaseRequisitionItems(List<PurchaseRequisitionItem> purchaseRequisitionItems) {
		this.purchaseRequisitionItems = purchaseRequisitionItems;
	}

	public List<PurchaseOrderItem> getPurchaseOrderItems() {
		return purchaseOrderItems;
	}

	public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
		this.purchaseOrderItems = purchaseOrderItems;
	}

	public List<GoodsReceiptItem> getGoodsReceiptItems() {
		return goodsReceiptItems;
	}

	public void setGoodsReceiptItems(List<GoodsReceiptItem> goodsReceiptItems) {
		this.goodsReceiptItems = goodsReceiptItems;
	}

	public void addSubcategory(BudgetItem aSubItem){
		subBudgetItems.add(aSubItem);
		aSubItem.setParentBudgetItem(this);
		aSubItem.setBudget(budget);
	}
	
	public String getFullAvaiableStr(){
		if (isAvailable) {
			return "ใช้งบได้";
		} else {
			return "ไม่สามารถใช้งบได้";
		}
	}
	
	public BigDecimal getAvailableAmount(){
		// จำนวนเงินที่สามารถใช้ได้ = จำนวนเงินตั้งต้น + จำนวนเงินโอนเข้า - จำนวนเงินที่กันงบ - จำนวนเงินที่ใช้งบ - จำนวนเงินที่โอนออก
		return initialAmount.add(transferInAmount).subtract(reservedAmount).subtract(expensedAmount).subtract(transferOutAmount);
	}
	
	public boolean isDeletable(){
		if (transferInAmount.doubleValue() > 0 || transferOutAmount.doubleValue() > 0 
				|| reservedAmount.doubleValue() > 0 || expensedAmount.doubleValue() > 0 ){
			return false;
		}
		return true;
	}
	
	public BigDecimal transferIn(BigDecimal aTransferInAmount){
		transferInAmount = transferInAmount.add(aTransferInAmount);
		return transferInAmount;
	}
	
	public BigDecimal transferOut(BigDecimal aTransferOutAmount){
		transferOutAmount = transferOutAmount.add(aTransferOutAmount);
		return transferOutAmount;
	}
	
	public BigDecimal reserve(BigDecimal aReserveAmount){
		reservedAmount = reservedAmount.add(aReserveAmount);
		return reservedAmount;
	}
	
	public BigDecimal expense(BigDecimal aExpenseAmount){
		expensedAmount = expensedAmount.add(aExpenseAmount);
		return expensedAmount;
	}
	
	public String toString(){
		return accountCode +" " + category;
	}

	public String getBudgetLevel() {
		return budgetLevel;
	}

	public void setBudgetLevel(String budgetLevel) {
		this.budgetLevel = budgetLevel;
	}

	/*
	@Override
	public Object clone() throws CloneNotSupportedException {
		BudgetItem cloned = (BudgetItem) super.clone();
		cloned.availableAmount = availableAmount;
		cloned.budgetType = budgetType;
		cloned.category = category;
		cloned.expensedAmount = expensedAmount;
		cloned.initialAmount = initialAmount;
		cloned.isAvailable = isAvailable;
		cloned.isControlled = isControlled;
		cloned.isEditable = isEditable;
		cloned.isExpenseEntry = isExpenseEntry;
		cloned.isMonthlyBudget = isMonthlyBudget;
		cloned.isNurseryBudget = isNurseryBudget;
		cloned.isPurchasingBudget = isPurchasingBudget;
		cloned.reservedAmount = reservedAmount;
		cloned.status = status;
		return super.clone();
	}
	*/		
	
}
