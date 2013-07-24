package welfare.persistent.customtype;

public enum GIStatus {
	/*
	 * สถานะ Lot
	 * 				ISSUED : เบิกไปแล้ว
	 * 				CANCELED : ยกเลิกการเบิก
	 */
	ISSUED("ISSUED","ISSUED"), 
	CANCELED("CANCELED","CANCELED"), 
	;
	
	private String id;
	private String value;
	GIStatus(String aID, String aValue){
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
	
	public static GIStatus find(String aID){
		for (GIStatus type : GIStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
