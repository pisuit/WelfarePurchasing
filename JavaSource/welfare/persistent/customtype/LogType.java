package welfare.persistent.customtype;

public enum LogType {
	//CREATE("Create","Create"),
	//UPDATE("Update","Update"),
	//DELETE("Delete","Delete"),
	ACCESS("Access","Access"),
	PR("PR","PR"),
	PO("PO","PO"),
	GR("GR","GR"),
	ADMIN("Admin","Admin"),
	BUDGET("Budget","Budget"),
	VENDOR("Vendor","Vendor"),
	STOCK("Stock","Stock");
	
	private String id;
	private String value;
	
	LogType(String aID, String aValue){
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
	public static LogType find(String aID){
		for (LogType logType : LogType.values()) {
			if (logType.id.equals(aID)){
				return logType;
			}
		}
		return null;
	}
}
