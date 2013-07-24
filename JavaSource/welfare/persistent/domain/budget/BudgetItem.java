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
	private String accountCode;										// ���ʧ�����ҳ
	private String category="";										// ��Ǵ������ҳ
	private BigDecimal initialAmount = new BigDecimal("0.00");		// ǧ�Թ
	private BigDecimal reservedAmount = new BigDecimal("0.00");		// �ӹǹ�Թ�ѹ��
	private BigDecimal expensedAmount = new BigDecimal("0.00");		// �ӹǹ������ԧ 
	private BigDecimal transferInAmount = new BigDecimal("0.00");	// �ӹǹ�Թ����ա���͹���
	private BigDecimal transferOutAmount = new BigDecimal("0.00");	// �ӹǹ�Թ����ա���͹�͡
	private BudgetItemType budgetType = BudgetItemType.M;		// ������������ҳ (M=��Ǵ��ѡ, S=��Ǵ����)
	private Status status = Status.NORMAL;			// ʶҹЧ�����ҳ (X=ź, N=����)
	private String budgetLevel;
	private boolean isAvailable = true;							// ����ö�������������� (true=��, false=�����)
	private boolean isEditable = true;								// ����ö������ա? (true=��, false=�����)
	private boolean isControlled = false;							// �Ǻ���������ҳ
	private boolean isMonthlyBudget = false;						// �Ǻ����������͹
	private boolean isPurchasingBudget = true;						// ��Ѻ PR/PO
	private boolean isExpenseEntry = true;							// �ѡѺ��úѹ�֡��������������ҹ PR/PO
	private boolean isNurseryBudget = false;						// ������Ѻ��˹�����������
	private Budget budget;											// ������ҳ��Шӻ�
	private BudgetItem parentBudgetItem;								// ��Ǵ��ѡ
	private List<BudgetItem> subBudgetItems = new ArrayList<BudgetItem>(); // ��Ǵ����
	private List<BudgetTransfer> fromBudgetTransfers = new ArrayList<BudgetTransfer>(); // ��¡�â��͹���
	private List<BudgetTransfer> toBudgetTransfers = new ArrayList<BudgetTransfer>(); // ��¡�â��͹�͡
	private List<BudgetExpense> budgetExpenses = new ArrayList<BudgetExpense>(); // ��¡�úѹ�֡��������
	private List<PurchaseRequisitionItem> purchaseRequisitionItems = new ArrayList<PurchaseRequisitionItem>(); // ��¡����������
	private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<PurchaseOrderItem>(); // ��¡����������觫���
	private List<GoodsReceiptItem> goodsReceiptItems = new ArrayList<GoodsReceiptItem>(); // ��¡���������Ѻ��ʴ�
	
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
			return "�駺��";
		} else {
			return "�������ö�駺��";
		}
	}
	
	public BigDecimal getAvailableAmount(){
		// �ӹǹ�Թ�������ö���� = �ӹǹ�Թ��駵� + �ӹǹ�Թ�͹��� - �ӹǹ�Թ���ѹ�� - �ӹǹ�Թ����駺 - �ӹǹ�Թ����͹�͡
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
