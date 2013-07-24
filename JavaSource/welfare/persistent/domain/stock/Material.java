package welfare.persistent.domain.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import welfare.persistent.customtype.Status;
import welfare.persistent.domain.purchasing.GoodsReceiptItem;
import welfare.persistent.domain.purchasing.PurchaseOrderItem;
import welfare.persistent.domain.purchasing.PurchaseRequisitionItem;

public class Material {
	private Long id;
	private String code; 						// รหัสวัสดุ
	private String description; 				// ชื่อวัสดุ
	private String issueUnit;	 				// หน่วยเบิก
	private String orderUnit; 					// หน่วยจัดซื้อ
	private double unitConverter = 1;				// แปลงจากหน่วยจัดซื้อไปเป็นหน่วยจัดเก็บ
	private BigDecimal orderUnitPrice = new BigDecimal("0.00"); 			// ราคาจัดซื้อต่อหน่วย
	private double maxStock; 					// จำนวนสูงสุดที่สามารถเก็บได้ในคลัง
	private double minStock; 					// จำนวนต่ำสุดที่ต้องดำเนินการจัดซื้อ
	private String warehouseCode;				// รหัสคลัง
	private Status status = Status.NORMAL; 		// สถานะของวัสดุ
	private MaterialGroup materialGroup;		// กลุ่มวัสดุ 
	private List<PurchaseRequisitionItem> purchaseRequisitionItems = new ArrayList<PurchaseRequisitionItem>(); // รายการในใบแจ้งจัดหา
	private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<PurchaseOrderItem>(); // รายการในใบสั่งซื้อ
	private List<GoodsReceiptItem> goodsReceiptItems = new ArrayList<GoodsReceiptItem>(); // รายการในใบรับ
	private List<StockMovement> stockMovements = new ArrayList<StockMovement>(); // รายการเคลื่อนไหว
	private List<MaterialLot> materialLots = new ArrayList<MaterialLot>(); // Lot ของวัสดุ
	private List<GoodsIssueItem> goodsIssueItems = new ArrayList<GoodsIssueItem>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String matNo) {
		this.code = matNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIssueUnit() {
		return issueUnit;
	}

	public void setIssueUnit(String issueUnit) {
		this.issueUnit = issueUnit;
	}

	public String getOrderUnit() {
		return orderUnit;
	}

	public void setOrderUnit(String orderUnit) {
		this.orderUnit = orderUnit;
	}

	public double getUnitConverter() {
		return unitConverter;
	}

	public void setUnitConverter(double unitConverter) {
		this.unitConverter = unitConverter;
	}

	public BigDecimal getOrderUnitPrice() {
		return orderUnitPrice;
	}

	public void setOrderUnitPrice(BigDecimal orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public double getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(double maxStock) {
		this.maxStock = maxStock;
	}

	public double getMinStock() {
		return minStock;
	}

	public void setMinStock(double minStock) {
		this.minStock = minStock;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public MaterialGroup getMaterialGroup() {
		return materialGroup;
	}

	public void setMaterialGroup(MaterialGroup materialGroup) {
		this.materialGroup = materialGroup;
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

	public List<StockMovement> getStockMovements() {
		return stockMovements;
	}

	public void setStockMovements(List<StockMovement> stockMovements) {
		this.stockMovements = stockMovements;
	}

	public List<MaterialLot> getMaterialLots() {
		return materialLots;
	}

	public void setMaterialLots(List<MaterialLot> materialLots) {
		this.materialLots = materialLots;
	}

	public List<GoodsIssueItem> getGoodsIssueItems() {
		return goodsIssueItems;
	}

	public void setGoodsIssueItems(List<GoodsIssueItem> goodsIssueItems) {
		this.goodsIssueItems = goodsIssueItems;
	}

	public String toString(){
		return code + "-" +description;
	}
}
