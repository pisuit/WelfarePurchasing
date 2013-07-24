package welfare.persistent.customtype;

import java.util.ArrayList;

public enum StorageLocation {
	SL_CANTEEN_CANTEEN("SL_CANTEEN_CANTEEN","อาคาสโมสร","WH_CANTEEN"),
	SL_CANTEEN_SWIMMING("SL_CANTEEN_SWIMMING","อาคารสระว่ายน้ำ","WH_CANTEEN");
	
	private String id;
	private String value;
	private String warehouseCode;
	
	StorageLocation(String aID, String aValue, String aWarehouseCode){
		id = aID;
		value = aValue;
		warehouseCode = aWarehouseCode;
	}
	
	public String getID(){
		return id;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getWarehouseCode(){
		return warehouseCode;
	}

	public String toString() {
		return value;
	}
	
	public static StorageLocation find(String aID){
		for (StorageLocation poType : StorageLocation.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
	
	public static ArrayList<StorageLocation> warehouse(String aWarehouseCode){
		ArrayList<StorageLocation> list = new ArrayList<StorageLocation>();
		for (StorageLocation poType : StorageLocation.values()) {
			if (poType.warehouseCode.equals(aWarehouseCode)){
				list.add(poType);
			}
		}
		return list;
	}

}
