package welfare.persistent.domain.purchasing;

import java.math.BigDecimal;
import java.util.Date;

import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.stock.Material;

public class PurchaseRequisitionItem {
	public static final String STATUS_NORMAL = "N"; 
	public static final String STATUS_DELETED = "X"; 
	
	private Long id;
	private int itemNumber;	// เลขลำดับรายการ
	private String otherMaterial; // ชื่อพัสดุอื่นที่ไม่อยู่ในคลัง
	private BigDecimal quantity = new BigDecimal("0.00"); // จำนวน
	private BigDecimal unitPrice = new BigDecimal("0.00"); // ราคาต่อหน่วยซื้อ
	private String orderUnit; // หน่วยซื้อ
	private Date deliveryDate; // วันที่ต้องการใช้งาน
	private BigDecimal budgetReservedAmount = new BigDecimal("0.00"); // จำนวนเงินที่จองงบประมาณไว้
	private PurchaseRequisition purchaseRequisition; // ใบแจ้งจัดหา
	private Material material; // วัสดุ
	private BudgetItem budgetItem; // หมวดงบประมาณ
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

	public BigDecimal getBudgetReservedAmount() {
		return budgetReservedAmount;
	}

	public void setBudgetReservedAmount(BigDecimal budgetReservedAmount) {
		this.budgetReservedAmount = budgetReservedAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public PurchaseRequisition getPurchaseRequisition() {
		return purchaseRequisition;
	}

	public void setPurchaseRequisition(PurchaseRequisition purchaseRequisition) {
		this.purchaseRequisition = purchaseRequisition;
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
	
}
