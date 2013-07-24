package welfare.persistent.domain.purchasing;

import java.math.BigDecimal;
import java.util.Date;

import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.stock.Material;

public class PurchaseOrderItem {
	private Long id;
	private int itemNumber;	// �Ţ�ӴѺ��¡��
	private String otherMaterial; // ���;�ʴ���蹷���������㹤�ѧ
	private BigDecimal quantity = new BigDecimal("0.00"); // �ӹǹ
	private BigDecimal unitPrice = new BigDecimal("0.00"); // �Ҥҵ��˹��«���
	private String orderUnit; // ˹��«���
	private Date deliveryDate; // �ѹ����ͧ�����ҹ
	private BigDecimal receivedQuantity =  new BigDecimal("0.00"); // �ӹǹ����Ѻ�����
	private BigDecimal budgetReservedAmount = new BigDecimal("0.00"); // �ӹǹ�Թ���ͧ������ҳ���
	private PurchaseOrder purchaseOrder; // ��駨Ѵ����
	private Material material; // ��ʴ�
	private BudgetItem budgetItem; // ��Ǵ������ҳ
	private Status status = Status.NORMAL ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNo) {
		this.itemNumber = itemNo;
	}

	public String getOtherMaterial() {
		return otherMaterial;
	}

	public void setOtherMaterial(String otherMaterial) {
		this.otherMaterial = otherMaterial;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal qty) {
		this.quantity = qty.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public String getOrderUnit() {
		return orderUnit;
	}

	public void setOrderUnit(String unit) {
		this.orderUnit = unit;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public BigDecimal getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(BigDecimal receivedQuantity) {
		this.receivedQuantity = receivedQuantity.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getBudgetReservedAmount() {
		return budgetReservedAmount;
	}

	public void setBudgetReservedAmount(BigDecimal budgetReservedAmount) {
		this.budgetReservedAmount = budgetReservedAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public BudgetItem getBudgetItem() {
		return budgetItem;
	}

	public void setBudgetItem(BudgetItem budgetItem) {
		this.budgetItem = budgetItem;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public BigDecimal getTotalPrice(){
		return unitPrice.multiply(quantity);
	}

	public BigDecimal getRemainQty(){
		return quantity.subtract(receivedQuantity);
	}
		
}
