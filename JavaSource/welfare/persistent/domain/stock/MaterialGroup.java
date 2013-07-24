package welfare.persistent.domain.stock;

import java.util.ArrayList;
import java.util.List;

import welfare.persistent.customtype.Status;

public class MaterialGroup {
	
	private Long id;
	private String code;							// รหัสกลุ่ม
	private String description;							// ชื่อกลุ่ม
	private String warehouseCode;								// รหัสคลัง
	private Status status = Status.NORMAL;		// สถานะของวัสดุ
	private List<Material> materials = new ArrayList<Material>();	// วัสดุที่อยู่ภายในกลุ่ม
	
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
