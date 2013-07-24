package welfare.persistent.domain.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import welfare.persistent.customtype.LotStatus;
import welfare.persistent.customtype.Status;
import welfare.persistent.customtype.StorageLocation;

public class MaterialLot {
	private Long id;
	private BigDecimal unitPrice  = new BigDecimal("0.00");
	private BigDecimal totalQty  = new BigDecimal("0.00");
	private BigDecimal totalPrice  = new BigDecimal("0.00");
	private BigDecimal availableQty  = new BigDecimal("0.00");
	private Date createdDate;
	private String warehouse;
	private StorageLocation storageLocation;
	private LotStatus status = LotStatus.CLOSED;
	private Material material;
	private List<StockMovement> stockMovements = new ArrayList<StockMovement>();

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal aUnitPrice) {
		unitPrice = aUnitPrice;
	}

	public BigDecimal getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(BigDecimal aTotalQty) {
		totalQty = aTotalQty;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal aTotalPrice) {
		totalPrice = aTotalPrice;
	}

	public BigDecimal getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigDecimal aAvailableQty) {
		availableQty = aAvailableQty;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date aCreatedDate) {
		createdDate = aCreatedDate;
	}
	
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public StorageLocation getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(StorageLocation storageLocation) {
		this.storageLocation = storageLocation;
	}

	public LotStatus getStatus() {
		return status;
	}

	public void setStatus(LotStatus aStatus) {
		status = aStatus;
	}
	
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material aMaterial) {
		material = aMaterial;
	}

	public List<StockMovement> getStockMovements() {
		return stockMovements;
	}

	public void setStockMovements(List<StockMovement> aStockMovements) {
		stockMovements = aStockMovements;
	}

	public String toString() {
		return "Lot ID: "+id+" Unit Price: "+unitPrice+" Total Qty: "+totalQty
				+" Total Price: "+totalPrice+ " Available Qty: "+availableQty
				+" Material :"+material;
	}
}
