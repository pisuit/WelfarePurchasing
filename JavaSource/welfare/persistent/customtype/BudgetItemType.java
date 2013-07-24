package welfare.persistent.customtype;

public enum BudgetItemType {
	M("M","หมวดหลัก"),
	S("S","หมวดย่อย");
	
	private String id;
	private String value;
	
	BudgetItemType(String aID, String aValue){
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
	
	public static BudgetItemType find(String aID){
		for (BudgetItemType poType : BudgetItemType.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
}
