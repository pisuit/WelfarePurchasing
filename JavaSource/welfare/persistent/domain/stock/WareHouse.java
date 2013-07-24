package welfare.persistent.domain.stock;

import java.util.ArrayList;
import java.util.List;


public class WareHouse {
	private Long id;
	private String code; // รหัสคลัง
	private String description; // ชื่อคลัง
	private List<StorageLocation> storageLocations = new ArrayList<StorageLocation>(); // คลังย่อย
	private List<MaterialGroup> materialGroups = new ArrayList<MaterialGroup>(); // กลุ่มวัสดุที่อยู่ในคลัง

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String whCode) {
		this.code = whCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<StorageLocation> getStorageLocations() {
		return storageLocations;
	}

	public void setStorageLocations(List<StorageLocation> storageLocations) {
		this.storageLocations = storageLocations;
	}

	public List<MaterialGroup> getMaterialGroups() {
		return materialGroups;
	}

	public void setMaterialGroups(List<MaterialGroup> materialGroups) {
		this.materialGroups = materialGroups;
	}
}
