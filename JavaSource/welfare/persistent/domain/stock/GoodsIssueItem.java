package welfare.persistent.domain.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class GoodsIssueItem {
	// Persistence Attributes
	private Long id;
	private int itemNumber;
	private BigDecimal qty;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private String unit;
	private String warehouse;
	private welfare.persistent.customtype.StorageLocation storageLocation;
	private GoodsIssue goodsIssue;
	private Material material;
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

	public void setItemNumber(int itemNo) {
		this.itemNumber = itemNo;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public welfare.persistent.customtype.StorageLocation getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(welfare.persistent.customtype.StorageLocation storageLocation) {
		this.storageLocation = storageLocation;
	}

	public GoodsIssue getGoodsIssue() {
		return goodsIssue;
	}

	public void setGoodsIssue(GoodsIssue goodsIssue) {
		this.goodsIssue = goodsIssue;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public List<StockMovement> getStockMovements() {
		return stockMovements;
	}

	public void setStockMovements(List<StockMovement> stockMovements) {
		this.stockMovements = stockMovements;
	}

}
