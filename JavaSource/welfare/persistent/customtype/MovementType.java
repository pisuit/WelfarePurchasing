package welfare.persistent.customtype;


public enum MovementType {
	GR("GR","GR"),
	GR_CANCEL("GR-CANCEL","GR-CANCEL"),
	GI("02","GI"),
	GI_CANCEL("GI-CANCEL","GI-CANCEL"),
	RET("RETURN","RETURN"),
	RET_CANCEL("RETURN-CANCEL","RETURN-CANCEL"),
	ST_ADJUST("STOCK-ADJUST","STOCK-ADJUST"),
	;
	
	private String id;
	private String value;
	MovementType(String aID, String aValue){
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
	
	public static MovementType find(String aID){
		for (MovementType poType : MovementType.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
	
}
