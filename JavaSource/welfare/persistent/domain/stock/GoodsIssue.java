package welfare.persistent.domain.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import welfare.persistent.customtype.GIStatus;
import welfare.persistent.customtype.GIType;
import welfare.persistent.customtype.StorageLocation;

public class GoodsIssue {
	private Long id;
	private int giNumber;
	private String reason;
	private GIStatus status = GIStatus.ISSUED;
	private BigDecimal amount = new BigDecimal("0.00");
	private Date issuedDate;
	private String issuerName;
	private GIType issueType;
	private String warehouse;
	private StorageLocation storageLocation;
	private List<GoodsIssueItem> goodsIssueItems = new ArrayList<GoodsIssueItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getGiNumber() {
		return giNumber;
	}

	public void setGiNumber(int giNumber) {
		this.giNumber = giNumber;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public GIStatus getStatus() {
		return status;
	}

	public void setStatus(GIStatus status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public GIType getIssueType() {
		return issueType;
	}

	public void setIssueType(GIType issueType) {
		this.issueType = issueType;
	}

	public List<GoodsIssueItem> getGoodsIssueItems() {
		return goodsIssueItems;
	}

	public void setGoodsIssueItems(List<GoodsIssueItem> goodsIssueItems) {
		this.goodsIssueItems = goodsIssueItems;
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

}
