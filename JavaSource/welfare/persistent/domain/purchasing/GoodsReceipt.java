package welfare.persistent.domain.purchasing;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import welfare.persistent.customtype.GRStatus;
import welfare.persistent.customtype.GRType;
import welfare.persistent.customtype.Status;

public class GoodsReceipt {
	
	public static final DecimalFormat NF = new DecimalFormat("00000000");
	private Long id;
	private int grNumber;
	private Integer budgetYear;
	private String reason;
	private GRStatus status = GRStatus.OPEN;
	private GRType grType;
	private String invoiceNumber;
	private Date receivedDate;
	private Date postingDate;
	private String recipientName;
	private String recipientPos;
	private String entryName;
	private String entryPos;
	private BigDecimal totalPrice = new BigDecimal("0.00"); // จำนวนรวม
	private BigDecimal totalDiscountAmount = new BigDecimal("0.00");
	private String warehouseCode;	// รหัสคลัง
	private List<GoodsReceiptItem> goodsReceiptItems = new ArrayList<GoodsReceiptItem>();
	private PurchaseOrder purchaseOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getGrNumber() {
		return grNumber;
	}

	public void setGrNumber(int grNumber) {
		this.grNumber = grNumber;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public GRStatus getStatus() {
		return status;
	}

	public void setStatus(GRStatus status) {
		this.status = status;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientPos() {
		return recipientPos;
	}

	public void setRecipientPos(String recipientPos) {
		this.recipientPos = recipientPos;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public String getEntryPos() {
		return entryPos;
	}

	public void setEntryPos(String entryPos) {
		this.entryPos = entryPos;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public List<GoodsReceiptItem> getGoodsReceiptItems() {
		return goodsReceiptItems;
	}

	public void setGoodsReceiptItems(List<GoodsReceiptItem> goodsReceiptItems) {
		this.goodsReceiptItems = goodsReceiptItems;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public BigDecimal getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public boolean isEditable(){
		return !status.equals(GRStatus.CLOSED) && !status.equals(GRStatus.DELETED);
	}
	
	public BigDecimal totalReceivedQuantity(){
		BigDecimal totalQuantity = new BigDecimal("0.00");
		for (GoodsReceiptItem grItem : goodsReceiptItems) {
			if (grItem.getStatus().equals(Status.NORMAL)){
				totalQuantity = totalQuantity.add(grItem.getReceivedQty());
			}
		}
		return totalQuantity;
	}
	
	public BigDecimal calculateTotalPrice(){
		totalPrice = new BigDecimal("0.00");
		for (GoodsReceiptItem grItem : goodsReceiptItems) {
			if (grItem.getStatus().equals(Status.NORMAL)){
				totalPrice = totalPrice.add(grItem.getNetPrice());
			}
		}
		return totalPrice;
	}
	
	public String getFormattedGrNumber() {
		return NF.format(Integer.valueOf(grNumber));
	}

	public GRType getGrType() {
		return grType;
	}

	public void setGrType(GRType grType) {
		this.grType = grType;
	}

	public Integer getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}


}
