package welfare.persistent.domain.stock;

import java.util.ArrayList;
import java.util.List;

import welfare.persistent.customtype.Status;

public class MaterialGroup {
	
	private Long id;
	private String code;							// ���ʡ����
	private String description;							// ���͡����
	private String warehouseCode;								// ���ʤ�ѧ
	private Status status = Status.NORMAL;		// ʶҹТͧ��ʴ�
	private List<Material> materials = new ArrayList<Material>();	// ��ʴط���������㹡����
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String matGroupNo) {
		this.code = matGroupNo;
	}
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}
	
	public String toString(){
		return code + "-" +description;
	}
}
