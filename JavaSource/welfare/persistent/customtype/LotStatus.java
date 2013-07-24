package welfare.persistent.customtype;

public enum LotStatus {
	/*
	 * สถานะ Lot
	 * 				OPEN : ปิดการเบิกใช้เนื่องจากยังไม่ปิดใบรับ
	 * 				CLOSED : ปิดการเบิกใช้เนื่องจากยังไม่ปิดใบรับ
	 * 				FULL : ปิดการเบิกใช้เนื่องจากใช้ของหมดแล้ว
	 * 				CANCELED : ปิดการเบิกใช้เนื่องมีการยกเลิกใบรับ
	 */
	FULL("FULL","FULL"), 
	OPEN("OPEN","OPEN"), 
	CANCELED("CANCELED","CANCELED"), 
	CLOSED("CLOSED","CLOSED"), 
	;
	
	private String id;
	private String value;
	LotStatus(String aID, String aValue){
		id = aID;
		value = aValue;
	}
	
	public String getID(){
		return id;
	}
	
	public String getValue(){
		return value;
	}

	public String toString() {
		return value;
	}
	
	public static LotStatus find(String aID){
		for (LotStatus type : LotStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
