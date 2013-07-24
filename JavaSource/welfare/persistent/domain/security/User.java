package welfare.persistent.domain.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sun.org.apache.regexp.internal.recompile;

import welfare.persistent.customtype.Status;

public class User {
	public static String STATUS_NORMAL = "N";
	public static String STATUS_DELETED = "X";
	public static String TYPE_EMPLOYEE = "E";
	public static String TYPE_NONEMPLOYEE = "N";
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String mainSystemName;
	private String subSystemName;
	private String warehouseCode;
	private String userType = TYPE_EMPLOYEE;
	private Status status = Status.NORMAL; // NORMAL, DELETED
	private List<Authorization> authorizations =  new ArrayList<Authorization>();
	private List<BudgetAuthorization> budgetAuthorizations = new ArrayList<BudgetAuthorization>();
	private List<Log> logs = new ArrayList<Log>();
	private HashMap<String, Boolean> pagemap;

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String aUsername) {
		username = aUsername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String aPassword) {
		password = aPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String aFisrtName) {
		firstName = aFisrtName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String aLastName) {
		lastName = aLastName;
	}

	public String getMainSystemName() {
		return mainSystemName;
	}

	public void setMainSystemName(String systemName) {
		this.mainSystemName = systemName;
	}

	public String getSubSystemName() {
		return subSystemName;
	}

	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status aStatus) {
		status = aStatus;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String aUserType) {
		userType = aUserType;
	}

	public List<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(List<Authorization> aAuthorizations) {
		authorizations = aAuthorizations;
	}
		
	public ArrayList<String> getAuthorizationsAsStringList() {
		ArrayList<String> list = new ArrayList<String>();
		for (Authorization authorization : authorizations) {
			list.add(authorization.getSystemRole());
		}
		return list;
	}
		
	public List<BudgetAuthorization> getBudgetAuthorizations() {
		return budgetAuthorizations;
	}

	public void setBudgetAuthorizations(List<BudgetAuthorization> budgetAuthorizations) {
		this.budgetAuthorizations = budgetAuthorizations;
	}
	
	public ArrayList<String> getBudgetAuthorizationsAsStringList() {
		ArrayList<String> list = new ArrayList<String>();
		for (BudgetAuthorization budgetAuthorization : budgetAuthorizations) {
			list.add(budgetAuthorization.getBudgetAuth());
		}
		return list;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
		
	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

}
