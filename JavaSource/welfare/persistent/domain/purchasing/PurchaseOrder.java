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
	private int poNumber; // �Ţ������觫���
	private Integer budgetYear;
	private POType poType; // ��������èѴ���� �� POType
	private String reason; // �˵ؼ�
	private BigDecimal totalPrice = new BigDecimal("0.00"); // �ӹǹ���
	private Date postingDate; // �ѹ������¡��
	private String buyerName; // ���ͼ�����
	private String receiverName; // ���ͼ���Ѻ
	private String requisitionerName; // ���ͼ���駨Ѵ��
	private String requisitionerPos; // ���˹觼���駨Ѵ��
	private String inspectorName; // ���ͼ���Ǩ�ͧ
	private String inspectorPos; // ���˹觼���Ǩ�ͺ
	private String approverName; // ���ͼ��͹��ѵ�
	private String approverPos; // ���˹觼��͹��ѵ�
	private String warehouseCode;	// ���ʤ�ѧ
	private POStatus status = POStatus.OPEN; // ʶҹ����觫���
	private PurchaseRequisition purchaseRequisition; // ��駨Ѵ��
	private Vendor vendor; // ��ҹ���
	private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<PurchaseOrderItem>(); // ��¡�������㺨Ѵ����
	private List<GoodsReceipt> goodsReceipts = new ArrayList<GoodsReceipt>(); // ��Ѻ��ʴ� ����ö�Ѻ�������ӹǹ��
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
