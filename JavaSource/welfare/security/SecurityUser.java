package welfare.security;

import java.util.Date;
import java.util.HashMap;

import welfare.persistent.domain.security.Authorization;
import welfare.utils.Constants;

public class SecurityUser {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String systemName;
	private String subSystemName;
	private String warehouseCode;
	private String department;
	private Date loginDate;
	private Authorization[] authorizations = new Authorization[0];
	private HashMap<String, Boolean> pagemap;

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

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date aLoginDate) {
		loginDate = aLoginDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String aFirstName) {
		firstName = aFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String aLastName) {
		lastName = aLastName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSubSystemName() {
		return subSystemName;
	}

	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String aDepartment) {
		department = aDepartment;
	}

	public Authorization[] getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(Authorization[] aAuthorizations) {
		authorizations = aAuthorizations;
		pagemap = new HashMap<String, Boolean>();
		for (Authorization authorization : authorizations) {
			if (systemName.equals(Constants.SYS_MAIN_BUDGET)){
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_ADMIN)){
					pagemap.put("ROLE_BUDGET_ADMIN", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_MAIN)){
					pagemap.put("ROLE_BUDGET_MAIN", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_SUB)){
					pagemap.put("ROLE_BUDGET_SUB", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_TRANSFER_REQUEST)){
					pagemap.put("ROLE_BUDGET_TRANSFER_REQUEST", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_TRANSFER_APPROVE_MAIN)){
					pagemap.put("ROLE_BUDGET_TRANSFER_APPROVE_MAIN", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_TRANSFER_APPROVE_SUB)){
					pagemap.put("ROLE_BUDGET_TRANSFER_APPROVE_SUB", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_EXPENSE)){
					pagemap.put("ROLE_BUDGET_EXPENSE", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_BUDGET_CAPITAL)){
					pagemap.put("ROLE_BUDGET_CAPITAL", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_LOG)){
					pagemap.put("ROLE_LOG", true);
				}
			} else if(systemName.equals(Constants.SYS_MAIN_PURCHASING)){
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_ADMIN)){
					pagemap.put("ROLE_PURCHASING_ADMIN", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_VENDOR)){
					pagemap.put("ROLE_PURCHASING_VENDOR", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_MAT)){
					pagemap.put("ROLE_PURCHASING_MAT", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_PR)){
					pagemap.put("ROLE_PURCHASING_PR", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_PO)){
					pagemap.put("ROLE_PURCHASING_PO", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_GR)){
					pagemap.put("ROLE_PURCHASING_GR", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_GI)){
					pagemap.put("ROLE_PURCHASING_GI", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_RETURN)){
					pagemap.put("ROLE_PURCHASING_RETURN", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_PURCHASING_TRANSFER)){
					pagemap.put("ROLE_PURCHASING_TRANSFER", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_LOG)){
					pagemap.put("ROLE_LOG", true);
				}
			} else {
				if (authorization.getSystemRole().equals(Constants.ROLE_STOCK_ADMIN)){
					pagemap.put("ROLE_STOCK_ADMIN", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_STOCK_MATGROUP)){
					pagemap.put("ROLE_STOCK_MATGROUP", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_STOCK_MAT)){
					pagemap.put("ROLE_STOCK_MAT", true);
				}
				if (authorization.getSystemRole().equals(Constants.ROLE_LOG)){
					pagemap.put("ROLE_LOG", true);
				}
			}
		}
		System.out.println("PageAuth : "+pagemap);
	}
	
	public HashMap<String, Boolean> getPageAuthorizations(){
		return pagemap;
	}
	
	public String getFullName(){
		return firstName + " " + lastName;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

}
