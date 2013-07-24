package welfare.persistent.customtype;

public enum PRType {
	PR_NORMAL("PR-NORMAL","�駨Ѵ��Ẻ����"),
	PR_NOPRICE("PR-NOPRICE","�駨Ѵ��Ẻ�������Ҥ�"),
	PR_MEDICAL("PR-MEDICAL","��Ǩ�آ�Ҿ");
	
	private String id;
	private String value;
	
	PRType(String aID, String aValue){
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
	
	public static PRType find(String aID){
		for (PRType prType : PRType.values()) {
			if (prType.id.equals(aID)){
				return prType;
			}
		}
		return null;
	}
}
