package welfare.persistent.customtype;

public enum MovementStatus {
	/*
	 * สถานะ Lot
	 * 				TEMPORARY : ยังไม่มีการปิดใบรับ
	 * 				CANCELED : มีการยกเลิกใบรับ หรือ ใบเบิก หรือ ใบคืน
	 * 				PERMANENT : มีการเคลื่อนไหวจริง
	 */
	CANCELED("CANCELED","CANCELED"), 
	TEMPORARY("TEMPORARY","TEMPORARY"), 
	PERMANENT("PERMANENT","PERMANENT"), 
	;
	
	private String id;
	private String value;
	MovementStatus(String aID, String aValue){
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
	
	public static MovementStatus find(String aID){
		for (MovementStatus type : MovementStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
