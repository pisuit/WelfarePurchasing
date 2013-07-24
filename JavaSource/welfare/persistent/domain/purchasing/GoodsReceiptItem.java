package welfare.persistent.domain.purchasing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import welfare.persistent.customtype.Status;
import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.StockMovement;

public class GoodsReceiptItem {
	private Long id;
	private int itemNumber;
	private String otherMaterial;
	private BigDecimal orderQty = new BigDecimal("0.00");
	private BigDecimal receivedQty = new BigDecimal("0.00");
	private BigDecimal oldReceivedQty = new BigDecimal("0.00");
	private BigDecimal oldUnitPrice = new BigDecimal("0.00");
	private BigDecimal unitPrice = new BigDecimal("0.00");
	private BigDecimal orderUnitPrice = new BigDecimal("0.00");
	private BigDecimal totalprice = new BigDecimal("0.00");
	private BigDecimal discountAmount = new BigDecimal("0.00");
	private BigDecimal netPrice = new BigDecimal("0.00");
	private BigDecimal avgPrice = new BigDecimal("0.00");
	private String receiveUnit;
	private Status status = Status.NORMAL;
	private BigDecimal budgetExpensedAmount = new BigDecimal("0.00");
	private StorageLocation storageLocation;
	private GoodsReceipt goodsReceipt;
	private Material material;
	private BudgetItem budgetItem;
	private List<StockMovement> stockMovements = new ArrayList<StockMovement>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getOtherMaterial() {
		return otherMaterial;
	}

	public void setOtherMaterial(String otherMaterial) {
		this.otherMaterial = otherMaterial;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(BigDecimal receivedQty) {
		this.receivedQty = receivedQty.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getOldReceivedQty() {
		return oldReceivedQty;
	}

	public void setOldReceivedQty(BigDecimal oldReceivedQty) {
		this.oldReceivedQty = oldReceivedQty.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public String getReceiveUnit() {
		return receiveUnit;
	}

	public void setReceiveUnit(String receiptUnit) {
		this.receiveUnit = receiptUnit;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getBudgetExpensedAmount() {
		return budgetExpensedAmount;
	}

	public void setBudgetExpensedAmount(BigDecimal budgetExpensedAmount) {
		this.budgetExpensedAmount = budgetExpensedAmount;
	}

	public StorageLocation getStorageLocation() {
		return storageLocation;
	}

	public BigDecimal getOrderUnitPrice() {
		return orderUnitPrice;
	}

	public void setOrderUnitPrice(BigDecimal orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice;
	}

	public void setStorageLocation(StorageLocation location) {
		this.storageLocation = location;
	}

	public GoodsReceipt getGoodsReceipt() {
		return goodsReceipt;
	}

	public void setGoodsReceipt(GoodsReceipt goodsReceipt) {
		this.goodsReceipt = goodsReceipt;
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

	public List<StockMovement> getStockMovements() {
		return stockMovements;
	}

	public void setStockMovements(List<StockMovement> stockMovements) {
		this.stockMovements = stockMovements;
	}
	
	public BigDecimal getOldUnitPrice() {
		return oldUnitPrice;
	}

	public void setOldUnitPrice(BigDecimal oldUnitPrice) {
		this.oldUnitPrice = oldUnitPrice;
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

}
