package welfare.persistent.domain.stock;

import java.math.BigDecimal;
import java.util.Date;

import welfare.persistent.customtype.MovementStatus;
import welfare.persistent.customtype.MovementType;
import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.purchasing.GoodsReceiptItem;

public class StockMovement {
	public static final int SIGN_PLUS = 1;
	public static final int SIGN_MINUS = -1;
	
	public static final BigDecimal minus = new BigDecimal(SIGN_MINUS);
	
	private Long id;
	private Material material;
	private Date movementDate;
	private BigDecimal totalQty  = new BigDecimal("0.00");
	private BigDecimal unitPrice  = new BigDecimal("0.00");
	private BigDecimal totalPrice  = new BigDecimal("0.00");
	private int movementSign = SIGN_PLUS; // Can be only -1 or 1
	private MovementType movementType; // GR, GR-CANCEL, GI, GI-CANCEL, RETURN, RETURN-CANCEL, STOCK-ADJUST 
	private String remark;
	private MovementStatus status = MovementStatus.TEMPORARY;
	private String warehouse;
	private StorageLocation storageLocation;
	private MaterialLot materialLot;
	private GoodsReceiptItem goodsReceiptItem;
	private GoodsIssueItem goodsIssueItem;

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material aMaterial) {
		material = aMaterial;
	}

	public Date getMovementDate() {
		return movementDate;
	}

	public void setMovementDate(Date aMovementDate) {
		movementDate = aMovementDate;
	}

	public BigDecimal getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(BigDecimal aQty) {
		totalQty = aQty;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal aUnitPrice) {
		unitPrice = aUnitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal aTotalPrice) {
		totalPrice = aTotalPrice;
	}

	public int getMovementSign() {
		return movementSign;
	}

	public void setMovementSign(int aMovementSign) {
		movementSign = aMovementSign;
	}

	public MovementType getMovementType() {
		return movementType;
	}

	public void setMovementType(MovementType aMovementType) {
		movementType = aMovementType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String aRemark) {
		remark = aRemark;
	}

	public MovementStatus getStatus() {
		return status;
	}

	public void setStatus(MovementStatus status) {
		this.status = status;
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

	public MaterialLot getMaterialLot() {
		return materialLot;
	}

	public void setMaterialLot(MaterialLot aMaterialLot) {
		materialLot = aMaterialLot;
	}

	public GoodsReceiptItem getGoodsReceiptItem() {
		return goodsReceiptItem;
	}

	public void setGoodsReceiptItem(GoodsReceiptItem aGoodsReceiptItem) {
		goodsReceiptItem = aGoodsReceiptItem;
	}

	
	public GoodsIssueItem getGoodsIssueItem() {
		return goodsIssueItem;
	}

	public void setGoodsIssueItem(GoodsIssueItem goodsIssueItem) {
		this.goodsIssueItem = goodsIssueItem;
	}

	public BigDecimal getTotalQtyWithSign() {
		if (movementSign == 1) {
			return totalQty;
		} else {
			return totalQty.multiply(minus);
		}
	}
	
	public BigDecimal getTotalPriceWithSign() {
		if (movementSign == 1) {
			return totalPrice;
		} else {
			return totalPrice.multiply(minus);
		}
	}
}
