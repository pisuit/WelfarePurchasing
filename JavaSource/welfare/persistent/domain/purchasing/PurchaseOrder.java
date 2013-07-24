package welfare.persistent.domain.purchasing;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import welfare.persistent.customtype.POStatus;
import welfare.persistent.customtype.POType;

public class PurchaseOrder {
	public static final DecimalFormat NF = new DecimalFormat("00000000");
	
	private Long id;
	private int poNumber; // เลขที่ใบสั่งซื้อ
	private Integer budgetYear;
	private POType poType; // ประเภทการจัดซื้อ ดู POType
	private String reason; // เหตุผล
	private BigDecimal totalPrice = new BigDecimal("0.00"); // จำนวนรวม
	private Date postingDate; // วันที่ทำรายการ
	private String buyerName; // ชื่อผู้ซื้อ
	private String receiverName; // ชื่อผู้รับ
	private String requisitionerName; // ชื่อผู้แจ้งจัดหา
	private String requisitionerPos; // ตำแหน่งผู้แจ้งจัดหา
	private String inspectorName; // ชื่อผู้ตรวจสอง
	private String inspectorPos; // ตำแหน่งผู้ตรวจสอบ
	private String approverName; // ชื่อผู้อนุมัติ
	private String approverPos; // ตำแหน่งผู้อนุมัติ
	private String warehouseCode;	// รหัสคลัง
	private POStatus status = POStatus.OPEN; // สถานะใบสั่งซื้อ
	private PurchaseRequisition purchaseRequisition; // ใบแจ้งจัดหา
	private Vendor vendor; // ร้านค้า
	private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<PurchaseOrderItem>(); // รายการย่อยในใบจัดซื้อ
	private List<GoodsReceipt> goodsReceipts = new ArrayList<GoodsReceipt>(); // ใบรับวัสดุ สามารถรับไม่เต็มจำนวนได้
	private String otherVendor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(int poNumber) {
		this.poNumber = poNumber;
	}

	public POType getPoType() {
		return poType;
	}

	public void setPoType(POType poType) {
		this.poType = poType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice.setScale(1, BigDecimal.ROUND_HALF_UP);
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public POStatus getStatus() {
		return status;
	}

	public void setStatus(POStatus status) {
		this.status = status;
	}

	public PurchaseRequisition getPurchaseRequisition() {
		return purchaseRequisition;
	}

	public void setPurchaseRequisition(PurchaseRequisition purchaseRequisition) {
		this.purchaseRequisition = purchaseRequisition;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<PurchaseOrderItem> getPurchaseOrderItems() {
		return purchaseOrderItems;
	}

	public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
		this.purchaseOrderItems = purchaseOrderItems;
	}

	public List<GoodsReceipt> getGoodsReceipts() {
		return goodsReceipts;
	}

	public void setGoodsReceipts(List<GoodsReceipt> goodReceipts) {
		this.goodsReceipts = goodReceipts;
	}

	public String getFormattedPoNumber() {
		return NF.format(Integer.valueOf(poNumber));
	}
	
	public BigDecimal totalQuantity(){
		BigDecimal totalQuantity = new BigDecimal("0.00");
		for (PurchaseOrderItem poItem : purchaseOrderItems) {
			totalQuantity = totalQuantity.add(poItem.getQuantity());
		}
		return totalQuantity;
	}
	
	public BigDecimal totalReceiptQuantity(){
		BigDecimal totalQuantity = new BigDecimal("0.00");
		for (PurchaseOrderItem poItem : purchaseOrderItems) {
			totalQuantity = totalQuantity.add(poItem.getReceivedQuantity());
		}
		return totalQuantity;
	}
	
	public boolean isReceiveAll(){
		boolean isAll = true;
		for (PurchaseOrderItem poItem : purchaseOrderItems) {
			if (poItem.getQuantity().compareTo(poItem.getReceivedQuantity()) == 1){
				isAll = false;
				break;
			}
		}
		return isAll;
	}	
	
	public BigDecimal getSumPrice(){
		BigDecimal totalPrice = new BigDecimal("0.00");
		for (PurchaseOrderItem poItem : purchaseOrderItems) {
			totalPrice = totalPrice.add(poItem.getTotalPrice());
		}
		return totalPrice;
	}

	public Integer getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}

	public String getOtherVendor() {
		return otherVendor;
	}

	public void setOtherVendor(String otherVendor) {
		this.otherVendor = otherVendor;
	}

}
