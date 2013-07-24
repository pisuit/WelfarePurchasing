/**
 * 
 */
package welfare.presentation.budget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import sun.org.mozilla.javascript.internal.GeneratedClassLoader;
import sun.org.mozilla.javascript.internal.SecurityController;

import welfare.persistent.controller.BudgetAdminController;
import welfare.persistent.controller.BudgetController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.BudgetAuthorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class BudgetAdminManager {
	private BudgetAdminController budgetAdminController;	
	/* list panel */
	private ArrayList<User> userList = new ArrayList<User>();
	private ArrayList<User> purchasingUserList = new ArrayList<User>();
	/* edit panel */
	private User editUser;
	private ArrayList<String> selectedUserAuthorizationList = new ArrayList<String>();
	private ArrayList<SelectItem> authorizationSelectItemList =  new ArrayList<SelectItem>();
	private String selectedBudgetAuthorization;
	private ArrayList<String> selectedBudgetAuthorizationList = new ArrayList<String>();
	private ArrayList<SelectItem> budgetAuthorizationSelectItemList = new ArrayList<SelectItem>();
	private ArrayList<BudgetItem> budgetItemList;
	private String welcomeMsg = null;
	
	public BudgetAdminManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		budgetAdminController = new BudgetAdminController();
		listUser();	
		listPurchasingUser();
		createAuthorizationSelectItemList();
		createBudgetAuthorizationSelectItemList();
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public ArrayList<User> getUserList() {
		return userList;
	}

	public User getEditUser() {
		return editUser;
	}

	public void setEditUser(User editUser) {
		this.editUser = editUser;
	}
	
	public ArrayList<String> getSelectedUserAuthorizationList() {
		return selectedUserAuthorizationList;
	}

	public void setSelectedUserAuthorizationList(
			ArrayList<String> aSelectedUserAuthorizationList) {
		selectedUserAuthorizationList = aSelectedUserAuthorizationList;
	}

	public ArrayList<SelectItem> getAuthorizationSelectItemList() {
		return authorizationSelectItemList;
	}

	public ArrayList<User> getPurchasingUserList() {
		return purchasingUserList;
	}

	public void setPurchasingUserList(ArrayList<User> purchasingUserList) {
		this.purchasingUserList = purchasingUserList;
	}

	public ArrayList<String> getSelectedBudgetAuthorizationList() {
		return selectedBudgetAuthorizationList;
	}

	public void setSelectedBudgetAuthorizationList(
			ArrayList<String> selectedBudgetAuthorizationList) {
		this.selectedBudgetAuthorizationList = selectedBudgetAuthorizationList;
	}

	public ArrayList<SelectItem> getBudgetAuthorizationSelectItemList() {
		return budgetAuthorizationSelectItemList;
	}

	public void setBudgetAuthorizationSelectItemList(
			ArrayList<SelectItem> budgetAuthorizationSelectItemList) {
		this.budgetAuthorizationSelectItemList = budgetAuthorizationSelectItemList;
	}

	public ArrayList<BudgetItem> getBudgetItemList() {
		return budgetItemList;
	}

	public void setBudgetItemList(ArrayList<BudgetItem> budgetItemList) {
		this.budgetItemList = budgetItemList;
	}

	public String getSelectedBudgetAuthorization() {
		return selectedBudgetAuthorization;
	}

	public void setSelectedBudgetAuthorization(String selectedBudgetAuthorization) {
		this.selectedBudgetAuthorization = selectedBudgetAuthorization;
	}

	private void listUser(){
		try {
			if (userList != null) userList.clear();
			userList = budgetAdminController.getMainSystemUserList(Constants.SYS_MAIN_BUDGET,Constants.SYS_BUDGET_BUDGET);
			editUser = new User();
			selectedUserAuthorizationList.clear();
			System.out.println("Number of users ="+userList.size());
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public void userTableRowClicked(){
		selectedUserAuthorizationList.clear();
		selectedUserAuthorizationList = editUser.getAuthorizationsAsStringList();
	}
	
	public void saveUser(){	
		LogManager log = new LogManager();
		try {	
			User checkDuplicate = budgetAdminController.checkDuplicateUser(editUser.getUsername());
			if (checkDuplicate != null && editUser.getId() == null) 
				throw new ControllerException("มีชื่อผู้ใช้นี้ในระบบแล้ว");	
			else {
				System.out.println("Save");
				ArrayList<Authorization> authorizationList = new ArrayList<Authorization>();
				Authorization newAuthorization;
				for (String authorizationStr : selectedUserAuthorizationList) {
					newAuthorization = new Authorization();
					newAuthorization.setUser(editUser);
					newAuthorization.setSystemRole(authorizationStr);
					authorizationList.add(newAuthorization);
				}
				editUser.setMainSystemName(Constants.SYS_MAIN_BUDGET);
				editUser.setSubSystemName(Constants.SYS_BUDGET_BUDGET);
				if (editUser.getId() == null) {					
					budgetAdminController.saveUser(editUser, authorizationList);
					log.recordCreateBudgetUser(editUser.getUsername(), editUser.getMainSystemName());
				} else {					
					budgetAdminController.saveUser(editUser, authorizationList);
					log.recordUpdateBudgetUser(editUser.getUsername(), editUser.getMainSystemName());
				}
				listUser();
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void newUser(){
		System.out.println("new user");
		editUser = new User();
		selectedUserAuthorizationList.clear();
	}

	public void deleteUser(){
		LogManager log = new LogManager();
		// do not allow to delete in case of there is only admin user
		try {
			System.out.println("Delete");
			editUser.setStatus(Status.DELETED);
			budgetAdminController.deleteUser(editUser);
			log.recordDeleteBudgetUser(editUser.getUsername(), editUser.getMainSystemName());
			listUser();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void createAuthorizationSelectItemList() {
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_MAIN,"จัดการงบประมาณหมวดหลัก"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_SUB,"จัดการงบประมาณหมวดย่อย"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_TRANSFER_REQUEST,"ขอโอนงบประมาณ"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_TRANSFER_APPROVE_MAIN,"อนุมัติการโอนงบประมาณหมวดหลัก"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_TRANSFER_APPROVE_SUB,"อนุมัติการโอนงบประมาณหมวดย่อย"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_EXPENSE,"บันทึกค่าใช้จ่าย"));
		//authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_CAPITAL,"บันทึกเพิ่มงบประมาณ"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_BUDGET_ADMIN,"ผู้ดูแลระบบ"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_LOG,"ผู้ดูแลระบบ Log"));
	}
	
	private void createBudgetAuthorizationSelectItemList() {
		try {
			Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
			int year = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
			budgetItemList = budgetAdminController.getBudgetItemList(year);
			SelectItem selectItem;
			for (BudgetItem budgetItem : budgetItemList) {
				selectItem = new SelectItem();
				selectItem.setValue(budgetItem.getAccountCode());
				selectItem.setLabel(budgetItem.toString());
//				if (budgetItem.getBudgetLevel().equals("Level_1")) {
//					selectItem.setLabel("|-> "+budgetItem);
//				} else if (budgetItem.getBudgetLevel().equals("Level_2")) {
//					selectItem.setLabel("|->-> "+budgetItem);
//				} else if (budgetItem.getBudgetLevel().equals("Level_3")) {
//					selectItem.setLabel("|->->-> "+budgetItem);
//				} else if (budgetItem.getBudgetLevel().equals("Level_4")) {
//					selectItem.setLabel("|->->->-> "+budgetItem);
//				}
				budgetAuthorizationSelectItemList.add(selectItem);								
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void purchasingUserTableRowClicked() {
		selectedBudgetAuthorizationList.clear();
		selectedBudgetAuthorizationList = editUser.getBudgetAuthorizationsAsStringList();
	}
	
	public void saveBudgetAuthorization() {
		LogManager log = new LogManager();
		try {
			if (editUser.getId() == null) throw new ControllerException("กรุณาเลือกผู้ใช้ที่ต้องการเพิ่มสิทธิ");
			ArrayList<BudgetAuthorization> budgetAuthorizationList = new ArrayList<BudgetAuthorization>();
			BudgetAuthorization newBudgetAuthorization;;
			for (String budgetAuthorizationStr : selectedBudgetAuthorizationList) {
				newBudgetAuthorization = new BudgetAuthorization();
				newBudgetAuthorization.setUser(editUser);
				newBudgetAuthorization.setBudgetAuth(budgetAuthorizationStr);
				budgetAuthorizationList.add(newBudgetAuthorization);
			}		
			budgetAdminController.saveBudgetAuthorization(editUser, budgetAuthorizationList);
			log.recordUpdateBudgetAuthorization(editUser.getUsername(), editUser.getMainSystemName());
			listPurchasingUser();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void listPurchasingUser() {
		try {
			if (purchasingUserList != null) purchasingUserList.clear();
			ArrayList<User> userList = budgetAdminController.getPurchasingUserList(Constants.SYS_MAIN_PURCHASING);
			User tempUser;
			for (User user : userList) {
				tempUser = new User();
				tempUser.setUsername(user.getUsername());
				tempUser.setFirstName(user.getFirstName());
				tempUser.setLastName(user.getLastName());
				tempUser.setSubSystemName(user.getSubSystemName());
				tempUser.setMainSystemName(user.getMainSystemName());
				tempUser.setId(user.getId());
				tempUser.setBudgetAuthorizations(user.getBudgetAuthorizations());
				tempUser.setWarehouseCode(getSystemName(user.getWarehouseCode()));
				purchasingUserList.add(tempUser);
			}
			editUser = new User();
			selectedBudgetAuthorizationList.clear();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public String getSystemName(String aSystemName){
		String systemName = "";
//		if (aSystemName.equals(Constants.SYS_BUDGET_BUDGET)){
//			systemName = "ระบบงบประมาณ";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			systemName = "ระบบงานโภชการและสโมสร";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_SPORT)){
//			systemName = "ระบบงานกีฬา";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_NURSERY)){
//			systemName = "ระบบงานสถานรับเลี้ยงเด็ก";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_HOTEL)){
//			systemName = "ระบบงานห้องพักงามดูพลี";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_MEDICAL)){
//			systemName = "ระบบงานเวชภัณฑ์";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_SWIMMING)){
//			systemName = "ระบบงานสระว่ายน้ำ";	
//		}
//		if (aSystemName.equals(Constants.SYS_MAIN_LOG)){
//			systemName = "ระบบตรวจสอบ Log";
//		}
		if (aSystemName.equals(Constants.WH_MAHAMEK)) {
			systemName = "ทุ่งมหาเมฆ";
		}
		if (aSystemName.equals(Constants.WH_CHIANGMAI)) {
			systemName = "ศูนย์เชียงใหม่";
		}
		if (aSystemName.equals(Constants.WH_HADYAI)) {
			systemName = "ศูนย์หาดใหญ่";
		}
		if (aSystemName.equals(Constants.WH_HUAHIN)) {
			systemName = "ศูนย์หัวหิน";
		}
		if (aSystemName.equals(Constants.WH_NAKORN)) {
			systemName = "ศูนย์นครราชสีมา";
		}
		if (aSystemName.equals(Constants.WH_PHITSANULOK)) {
			systemName = "ศูนย์พิษณุโลก";
		}
		if (aSystemName.equals(Constants.WH_PUKET)) {
			systemName = "ศูนย์ภูเก็ต";
		}
		if (aSystemName.equals(Constants.WH_SURAT)) {
			systemName = "ศูนย์สุราษฎร์ธานี";
		}
		if (aSystemName.equals(Constants.WH_UBON)) {
			systemName = "ศูนย์อุบลราชธานี";
		}
		if (aSystemName.equals(Constants.WH_UDON)) {
			systemName = "ศูนย์อุดรธานี";
		}
		return systemName;
	}
}
