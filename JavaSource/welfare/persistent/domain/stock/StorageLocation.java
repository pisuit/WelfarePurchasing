package welfare.persistent.domain.stock;


public class StorageLocation {
	private Long id;
	private String code;			// ���ʤ�ѧ����
	private String description;		// ���ͤ�ѧ����
	private WareHouse wareHouse;	// ��ѧ��ѡ

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String stCode) {
		this.code = stCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WareHouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(WareHouse wareHouse) {
		this.wareHouse = wareHouse;
	}
}
