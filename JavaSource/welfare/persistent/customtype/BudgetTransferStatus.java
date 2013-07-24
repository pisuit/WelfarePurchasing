package welfare.persistent.customtype;

public enum BudgetTransferStatus {
	REQUESTING("REQUESTING","Requesting"), 
	APPROVED("APPROVED","Approved"), 
	DISCARDED("DISCARDED","Discarded"), 
	;
	
	private String id;
	private String value;
	BudgetTransferStatus(String aID, String aValue){
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
	
	public static BudgetTransferStatus find(String aID){
		for (BudgetTransferStatus type : BudgetTransferStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
