package welfare.persistent.customtype;

public enum GRStatus {
	/*
	 * ʶҹ���Ѻ��ʴ�
	 * 				GR-FULL : ��Ѻ��ʴع����Ẻ�Ѻ�ͧ����ӹǹ
	 * 				GR-PAR : ��Ѻ��ʴع����Ẻ�Ѻ�ͧ�ҧ��ǹ
	 * 				DELETED : ��Ѻ��ʴع��١¡��ԡ
	 * 				RETURN-PAR : ��Ѻ��ʴع���ա�ä׹�ͧ�ҧ��ǹ
	 * 				RETURN-ALL : ��Ѻ��ʴع���ա�ä׹�ͧ������
	 * 				CLOSED : ��Ѻ��ʴض١�Դ �������ö�ӡ��¡��ԡ
	 */
	OPEN("OPEN","OPEN"), 
	GR_FULL("GR-FULL","GR-FULL"), 
	GR_PAR("GR-PAR","GR-PAR"), 
	DELETED("DELTED","DELTED"), 
	RET_ALL("RETURN-ALL","RETURN-ALL"), 
	RET_PAR("RETURN-PAR","RETURN-PAR"), 
	CLOSED("CLOSED","CLOSED"), 
	;
	
	private String id;
	private String value;
	GRStatus(String aID, String aValue){
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
	
	public static GRStatus find(String aID){
		for (GRStatus type : GRStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
