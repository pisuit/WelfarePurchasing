package welfare.persistent.domain.purchasing;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import welfare.persistent.customtype.PRStatus;
import welfare.persistent.customtype.PRType;

public class PurchaseRequisition {
	public static final DecimalFormat PRNUMBER_FORMATTER = new DecimalFormat("00000000");

	private Long id;
	private int prNumber; // เลขที่ใบแจ้งจัดหา
	private String reason; // เหตุผล
	private Integer budgetYear;
	private String referenceDocNumber; // เลขที่เอกสารอ้างอิง
	private String requisitionerName; // ชื่อผู้แจ้งจัดหา
	private String requisitionerPos; // ตำแหน่งผู้แจ้งจัดหา
	private String inspectorName; // ชื่อผู้ตรวจสอง
	private String inspectorPos; // ตำแหน่งผู้ตรวจสอบ
	private String approverName; // ชื่อผู้อนุมัติ
	private String approverPos; // ตำแหน่งผู้อนุมัติ
	private Date postingDate; // วันที่สร้างใบแจ้ง
	private BigDecimal totalPrice = new BigDecimal("0.00"); // จำนวนเงินรวม
	private String warehouseCode;	// รหัสคลัง
	private PRStatus status = PRStatus.OPEN; // สถานะใบแจ้ง
	private List<PurchaseRequisitionItem> purchaseRequisitionItems = new ArrayList<PurchaseRequisitionItem>();
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>(); // ใบจัดซื้อ
	private PRType prType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(int prNumber) {
		this.prNumber = prNumber;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReferenceDocNumber() {
		return referenceDocNumber;
	}

	public void setReferenceDocNumber(String referenceDocNumber) {
		this.referenceDocNumber = referenceDocNumber;
	}

	public String getRequisitionerName() {
		return requisitionerName;
	}

	public void setRequisitionerName(String requisitionerName) {
		this.requisitionerName = requisitionerName;
	}

	public String getRequisitionerPos() {
		return requisitionerPos;
	}

	public void setRequisitionerPos(String requisitionerPos) {
		this.requisitionerPos = requisitionerPos;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getInspectorPos() {
		return inspectorPos;
	}

	public void setInspectorPos(String inspectorPos) {
		this.inspectorPos = inspectorPos;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public String getApproverPos() {
		return approverPos;
	}

	public void setApproverPos(String approverPos) {
		this.approverPos = approverPos;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public PRStatus getStatus() {
		return status;
	}

	public void setStatus(PRStatus status) {
		this.status = status;
	}

	public List<PurchaseRequisitionItem> getPurchaseRequisitionItems() {
		return purchaseRequisitionItems;
	}

	public void setPurchaseRequisitionItems(List<PurchaseRequisitionItem> purchaseRequisitionItems) {
		this.purchaseRequisitionItems = purchaseRequisitionItems;
	}
	
	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public String getFormattedPrNumber(){
		return PRNUMBER_FORMATTER.format(prNumber);
	}
	
	
	public BigDecimal getSumPrice(){
		BigDecimal totalPrice = new BigDecimal("0.00");
		for (PurchaseRequisitionItem prItem : purchaseRequisitionItems) {
			totalPrice = totalPrice.add(prItem.getTotalPrice());
		}
		return totalPrice;
	}

	public Integer getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}

	public PRType getPrType() {
		return prType;
	}

	public void setPrType(PRType prType) {
		this.prType = prType;
	}
		
}
