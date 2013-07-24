package welfare.persistent.customtype;

public enum PRType {
	PR_NORMAL("PR-NORMAL","·®Èß®—¥À“·∫∫ª°µ‘"),
	PR_NOPRICE("PR-NOPRICE","·®Èß®—¥À“·∫∫‰¡Ë„ Ë√“§“"),
	PR_MEDICAL("PR-MEDICAL","µ√«® ÿ¢¿“æ");
	
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
