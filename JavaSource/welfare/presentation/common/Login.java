/**
 * 
 */
package welfare.presentation.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.AuthenticationException;

import welfare.persistent.controller.LogController;
import welfare.persistent.controller.SecurityController;
import welfare.persistent.customtype.LogType;
import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.domain.security.Log;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.security.AuthicationException;
import welfare.security.SecurityUser;
import welfare.security.ldap.InvalidLoginException;
import welfare.security.ldap.LDAPConnect;
import welfare.security.ldap.LDAPUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class Login {
	private String username;
	private String password;
	private String selectedItem;
	private ArrayList<SelectItem> selectItems;
	private SecurityController controller =  new SecurityController();
	
	public Login() {
		createSystemSelectItemList();
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<SelectItem> getSelectItems(){
		return selectItems;
	}
	
	private void createSystemSelectItemList(){
		selectItems = new ArrayList<SelectItem>();
		SelectItem selectItem;
		
//		selectItems.add(new SelectItem(Constants.SYS_BUDGET_BUDGET,"ระบบงบประมาณ"));
//		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_CANTEEN,"ระบบงานโภชการและสโมสร"));
//		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_SPORT,"ระบบงานกีฬา"));
//		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_NURSERY,"ระบบงานสถานรับเลี้ยงเด็ก"));
//		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_HOTEL,"ระบบงานห้องพักงามดูพลี"));
//		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_MEDICAL,"ระบบงานเวชภัณฑ์"));
//		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_SWIMMING,"ระบบงานสระว่ายน้ำ"));
		
		selectItems.add(new SelectItem(Constants.SYS_BUDGET_BUDGET, "ระบบงบประมาณ"));
		selectItems.add(new SelectItem(Constants.SYS_PURCHASING_PURCHASING, "ระบบจัดซื้อจัดหา"));
		selectItems.add(new SelectItem(Constants.SYS_STOCK_STOCK, "ระบบจัดการคลัง"));
	}
	
	public String login(){
		System.out.println("Logging in");
		LogManager log = new LogManager();
		try {
			SecurityUser user;
			boolean isForTest = false;
			if (isForTest) {
				Authorization[] authorizations;
				if (username.equalsIgnoreCase("admin")){
					user = new SecurityUser();
					user.setFirstName("admin");
					user.setUsername("admin");
					// add admin role
					authorizations = new Authorization[1];
					Authorization authorization;
					if (selectedItem.equals(Constants.SYS_BUDGET_BUDGET)){
						user.setSystemName(Constants.SYS_MAIN_BUDGET);
						user.setSubSystemName(Constants.SYS_BUDGET_BUDGET);
						// admin for budget
						authorization = new Authorization();
						authorization.setSystemRole(Constants.ROLE_BUDGET_ADMIN);
						authorizations[0] = authorization;
					} else if (selectedItem.equals(Constants.SYS_PURCHASING_PURCHASING)){
						user.setSystemName(Constants.SYS_MAIN_PURCHASING);
						user.setSubSystemName(Constants.SYS_PURCHASING_PURCHASING);
						// admin for purchasing
						authorization = new Authorization();
						authorization.setSystemRole(Constants.ROLE_PURCHASING_ADMIN);
						authorizations[0] = authorization;
					} else {
						user.setSystemName(Constants.SYS_MAIN_STOCK);
						user.setSubSystemName(Constants.SYS_STOCK_STOCK);
						// admin for stock
						authorization = new Authorization();
						authorization.setSystemRole(Constants.ROLE_STOCK_ADMIN);
						authorizations[0] = authorization;
					} 
					user.setAuthorizations(authorizations);
				} else {					
					user = new SecurityUser();
					user.setFirstName(username);
					user.setUsername(username);
					if (selectedItem.equals(Constants.SYS_BUDGET_BUDGET)){
						user.setSystemName(Constants.SYS_MAIN_BUDGET);
						user.setSubSystemName(Constants.SYS_BUDGET_BUDGET);
					} else if(selectedItem.equals(Constants.SYS_PURCHASING_PURCHASING)){
						user.setSystemName(Constants.SYS_MAIN_PURCHASING);
						user.setSubSystemName(Constants.SYS_PURCHASING_PURCHASING);
					} else {
						user.setSystemName(Constants.SYS_MAIN_STOCK);
						user.setSubSystemName(Constants.SYS_STOCK_STOCK);
					}
					SecurityController securityController = new SecurityController();
					try {
						authorizations = securityController.getAuthorizations(username, user.getSystemName(), user.getSubSystemName());
						if (authorizations.length == 0) {
							throw new AuthicationException("คุณไม่มีสิทธิในการใช้งานระบบ");
						}
						user.setAuthorizations(authorizations);
					} catch (ControllerException e) {
						throw new AuthicationException(e.getMessage());
					}
				}
				addUserToSession(user);
				//log.recordLogin();
				return Constants.SUCCESS_OUTCOME;
			}
//			//====== Start Log Auditor ======//
//			if (username.equalsIgnoreCase("auditor")){
//				if (password.equalsIgnoreCase("testing")){
//					user = new SecurityUser();
//					user.setFirstName(username);
//					user.setUsername(username);
//					user.setSystemName(Constants.SYS_MAIN_LOG);
//					user.setSubSystemName(Constants.SYS_LOG);
//					Authorization authorization = new Authorization();
//					Authorization[] authorizations = new Authorization[1];
//					authorization.setSystemRole(Constants.ROLE_LOG);	
//					authorizations[0] = authorization;
//					user.setAuthorizations(authorizations);					
//					addUserToSession(user);
//					return Constants.SUCCESS_OUTCOME;
//				} else {
//					throw new AuthicationException("รหัสผ่านไม่ถูกต้อง");
//				}
//			}
			//====== End Log Auditor ======//
			if (selectedItem.equals(Constants.SYS_BUDGET_BUDGET)) {
				if(username.equals("admin")){
					try {
						ArrayList<Authorization> existAdminRole = controller.getAuthorization(Constants.ROLE_BUDGET_ADMIN);
						if(existAdminRole.size() != 0){
							throw new AuthicationException("Role admin ถูกใส่ให้ user คนใดคนหนึ่งแล้ว");
						} else {
							user = new SecurityUser();
							Authorization[] authorizations = new Authorization[1];
							Authorization authorization = new Authorization();
							authorization.setSystemRole(Constants.ROLE_BUDGET_ADMIN);
							authorizations[0] = authorization;
							
							user.setUsername("admin");
							user.setFirstName("admin");
							user.setSystemName(Constants.SYS_MAIN_BUDGET);
							user.setSubSystemName(Constants.SYS_BUDGET_BUDGET);
							user.setAuthorizations(authorizations);
							
							addUserToSession(user);
						}
					} catch (ControllerException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} else {
					user = authen(username, password, Constants.SYS_MAIN_BUDGET, Constants.SYS_BUDGET_BUDGET);
					addUserToSession(user);
					log.recordLogin();
				}			
				return Constants.SUCCESS_OUTCOME;
			} else if(selectedItem.equals(Constants.SYS_PURCHASING_PURCHASING)){
				if(username.equals("admin")){
					try {
						ArrayList<Authorization> existAdminRole = controller.getAuthorization(Constants.ROLE_PURCHASING_ADMIN);
						if(existAdminRole.size() != 0){
							throw new AuthicationException("Role admin ถูกใส่ให้ user คนใดคนหนึ่งแล้ว");
						} else {
							user = new SecurityUser();
							Authorization[] authorizations = new Authorization[1];
							Authorization authorization = new Authorization();
							authorization.setSystemRole(Constants.ROLE_PURCHASING_ADMIN);
							authorizations[0] = authorization;
							
							user.setUsername("admin");
							user.setFirstName("admin");
							user.setSystemName(Constants.SYS_MAIN_PURCHASING);
							user.setSubSystemName(Constants.SYS_PURCHASING_PURCHASING);
							user.setAuthorizations(authorizations);
							
							addUserToSession(user);
						}
					} catch (ControllerException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} else {
					user = authen(username, password, Constants.SYS_MAIN_PURCHASING, Constants.SYS_PURCHASING_PURCHASING);
					addUserToSession(user);
					log.recordLogin();
				}
				return Constants.SUCCESS_OUTCOME;
			}   else {
				if(username.equals("admin")){
					try {
						ArrayList<Authorization> existAdminRole = controller.getAuthorization(Constants.ROLE_STOCK_ADMIN);
						if(existAdminRole.size() != 0){
							throw new AuthicationException("Role admin ถูกใส่ให้ user คนใดคนหนึ่งแล้ว");
						} else {
							user = new SecurityUser();
							Authorization[] authorizations = new Authorization[1];
							Authorization authorization = new Authorization();
							authorization.setSystemRole(Constants.ROLE_STOCK_ADMIN);
							authorizations[0] = authorization;
							
							user.setUsername("admin");
							user.setFirstName("admin");
							user.setSystemName(Constants.SYS_MAIN_STOCK);
							user.setSubSystemName(Constants.SYS_STOCK_STOCK);
							user.setAuthorizations(authorizations);
							
							addUserToSession(user);
						}
					} catch (ControllerException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} else {
					user = authen(username, password, Constants.SYS_MAIN_STOCK, Constants.SYS_STOCK_STOCK);
					addUserToSession(user);
					log.recordLogin();
				}
				return Constants.SUCCESS_OUTCOME;
			}

		} catch (AuthicationException e) {
			e.printStackTrace();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			//return Constants.FAILURE_OUTCOME;
			return null;
		}
	}
		
	public String logout() {
		LogManager log = new LogManager();
//		if (username.equalsIgnoreCase("auditor")){
//			removeUserFromSession();		
//			return "LOGOUT";
//		}
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		if(securityUser.getUsername().equals("admin")){
			removeUserFromSession();		
		} else {
			log.recordLogout();
			removeUserFromSession();
		}		
		return "LOGOUT";
	}
	
	public void addUserToSession(SecurityUser aUser) {
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		Application app = facesContext.getApplication();
//		ELContext elcontext = FacesContext.getCurrentInstance().getELContext();
//		ValueExpression userValueExpression = app.getExpressionFactory().createValueExpression(elcontext, "#{sessionScope."+Constants.USER_KEY+"}", SecurityUser.class);
//		userValueExpression.setValue(elcontext, aUser);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.USER_KEY, aUser);
	}
	
	public void removeUserFromSession() {
		removeBeanFromSession(Constants.USER_KEY);
		removeBeanFromSession("login");
		// budget
		removeBeanFromSession("budgetMain");
		removeBeanFromSession("budgetDetail");
		removeBeanFromSession("budgetAdmin");
		removeBeanFromSession("budgetTransferRequest");
		removeBeanFromSession("budgetTransferApprove");
		removeBeanFromSession("budgetExpense");
		// purchasing
		removeBeanFromSession("purchasingAdmin");
		removeBeanFromSession("purchasingMatGroup");
		removeBeanFromSession("purchasingMat");
		removeBeanFromSession("purchasingVendor");
		removeBeanFromSession("purchasingPR");
		removeBeanFromSession("purchasingPO");
		removeBeanFromSession("purchasingGR");
		removeBeanFromSession("purchasingGI");
		removeBeanFromSession("purchasingMovement");
		// log
		removeBeanFromSession("logManager");
	}
	
	private void removeBeanFromSession(String beanName){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.remove(beanName); // Removes the session scoped bean.
	}

	private SecurityUser authen(String aUsername, String aPassword, String aMainSystemName, String aSubSystemName) throws AuthicationException {
		try {
			// authen user
			LDAPConnect connect = new LDAPConnect();
			LDAPUser ldapUser = connect.login(aUsername, aPassword);
			SecurityUser user = new SecurityUser();
			User dbUser = controller.getUser(username, aMainSystemName, aSubSystemName);
			user.setLoginDate(Calendar.getInstance().getTime());
			user.setUsername(username);
			user.setLoginDate(CalendarUtils.getExpiredDate());
			if (dbUser == null) {
				user.setFirstName(ldapUser.getFirstName());
				user.setLastName(ldapUser.getLastName());
				user.setDepartment(ldapUser.getDepartment());
			} else {
				user.setFirstName(dbUser.getFirstName());
				user.setLastName(dbUser.getLastName());
				user.setSystemName(dbUser.getMainSystemName());
				user.setSubSystemName(dbUser.getSubSystemName());
				user.setWarehouseCode(dbUser.getWarehouseCode());
				user.setDepartment("");
			}
			connect.disconnect();
			// retrieve authorization
			Authorization[] authorizations = controller.getAuthorizations(user.getUsername(), aMainSystemName, aSubSystemName);
			if (authorizations.length == 0) {
				throw new AuthicationException("คุณไม่มีสิทธิในการใช้งานระบบ");
			}
			user.setAuthorizations(authorizations);
			return user;
		} catch (InvalidLoginException e) {
			throw new AuthicationException(e.getMessage());
		} catch (ControllerException e) {
			e.printStackTrace();
			throw new AuthicationException(e.getMessage());
		}
	}
}
