/**
 * 
 */
package welfare.presentation.purchasing;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import welfare.persistent.controller.PurchasingAdminController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.security.SecurityUser;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class PurchasingAdminManager {
	private PurchasingAdminController purchasingAdminController;
	/* list panel */
	private ArrayList<SelectItem> subSystemSelectItemList = new ArrayList<SelectItem>();
	private String selectedSubSystem ;
	private ArrayList<User> userList;
	/* edit panel */
	private User editUser = new User();
	private ArrayList<String> selectedUserAuthorizationList = new ArrayList<String>();
	private ArrayList<SelectItem> authorizationSelectItemList =  new ArrayList<SelectItem>();
	private String welcomeMsg = null;
	
	
	public PurchasingAdminManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		purchasingAdminController = new PurchasingAdminController();
		createSubSystemSelectItemList();
		createAuthorizationSelectItemList();
		listUser();
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public String getSelectedSubSystem() {
		return selectedSubSystem;
	}

	public void setSelectedSubSystem(String selectedSubSystem) {
		this.selectedSubSystem = selectedSubSystem;
	}

	public ArrayList<SelectItem> getSubSystemSelectItemList() {
		return subSystemSelectItemList;
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
	
	private void createSubSystemSelectItemList(){
		selectedSubSystem = Constants.WH_MAHAMEK;
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_CANTEEN,"ระบบงานโภชนาการและสโมสร"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_SPORT,"ระบบงานกีฬา"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_NURSERY,"ระบบงานสถานรับเลี้ยงเด็ก"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_HOTEL,"ระบบงานห้องพักงามดูพลี"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_MEDICAL,"ระบบงานเวชภัณฑ์"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_SWIMMING,"ระบบงานสระว่ายน้ำ"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_MAHAMEK,"ทุ่งมหาเมฆ"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_CHIANGMAI,"ศูนย์เชียงใหม่"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_HADYAI,"ศูนย์หาดใหญ่"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_HUAHIN,"ศูนย์หัวหิน"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_NAKORN,"ศูนย์นครราชสีมา"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_PHITSANULOK,"ศูนย์พิษณุโลก"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_PUKET,"ศูนย์ภูเก็ต"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_SURAT,"ศูนย์สุราษฎร์ธานี"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_UBON,"ศูนย์อุบลราชธานี"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_UDON,"ศูนย์อุดรธานี"));
	}

	private void listUser(){
		try {
			if (userList != null) userList.clear();
			userList = purchasingAdminController.getSubSystemUserList(Constants.SYS_MAIN_PURCHASING, selectedSubSystem);
			editUser = new User();
			selectedUserAuthorizationList.clear();
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
			User checkDuplicate = purchasingAdminController.checkDuplicateUser(editUser.getUsername());
			if (checkDuplicate != null && editUser.getId() == null) 
				throw new ControllerException("มีชื่อผู้ใช้นี้ในระบบแล้ว");	
			else {
			System.out.println("Save");
			ArrayList<Authorization> authorizationList = new ArrayList<Authorization>();
			if (selectedUserAuthorizationList.size() == 0) {
				throw new ControllerException("กรุณาระบุสิทธิการใช้งานอย่างน้อยหนึ่งสิทธิ ");
			}
			Authorization newAuthorization;
			for (String authorizationStr : selectedUserAuthorizationList) {
				newAuthorization = new Authorization();
				newAuthorization.setUser(editUser);
				newAuthorization.setSystemRole(authorizationStr);
				authorizationList.add(newAuthorization);
			}
			editUser.setMainSystemName(Constants.SYS_MAIN_PURCHASING);
//			editUser.setSubSystemName(selectedSubSystem);
			editUser.setSubSystemName(Constants.SYS_PURCHASING_PURCHASING);
			editUser.setWarehouseCode(selectedSubSystem);
			if (editUser.getId() == null) {
				purchasingAdminController.saveUser(editUser, authorizationList);
//				log.recordCreatePurchasingUser(editUser.getUsername(), editUser.getSubSystemName());
				log.recordCreatePurchasingUser(editUser.getUsername(), editUser.getMainSystemName());
			} else {
				purchasingAdminController.saveUser(editUser, authorizationList);
//				log.recordUpdatePurchasingUser(editUser.getUsername(), editUser.getSubSystemName());
				log.recordCreatePurchasingUser(editUser.getUsername(), editUser.getMainSystemName());
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
			purchasingAdminController.deleteUser(editUser);
//			log.recordDeletePurchasingUser(editUser.getUsername(), editUser.getSubSystemName());
			log.recordDeletePurchasingUser(editUser.getUsername(), editUser.getMainSystemName());
			listUser();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void subSytemComboboxSelected(){
		createAuthorizationSelectItemList();
		listUser();
	}
	
	private void createAuthorizationSelectItemList() {
		authorizationSelectItemList.clear();
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_PR,"จัดการใบแจ้งจัดหา"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_PO,"จัดการใบรายงานอมุติจัดหา/ใบสั่งซื้อ"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_GR,"จัดการใบรับวัสดุ"));
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_GI,"เบิกวัสดุ"));
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_NURSERY) || selectedSubSystem.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_FF_PO,"สั่งซื้ออาหารสด"));
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_FF_GR,"รับอาหารสด"));
//		}
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_NURSERY)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_NURSERY_CHILDEN,"สถิติเด็ก"));
//		}	
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_MAT,"จัดการวัสดุ"));
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_CANTEEN) || selectedSubSystem.equals(Constants.SYS_PURCHASING_HOTEL)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_TRANSFER,"โอนวัสดุ"));
//		}
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_TRANSFER,"โอนวัสดุ"));
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_RETURN,"คืนวัสดุ"));
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_BANQUET,"จอง/เบิกเพื่อจัดเลี้ยง"));
//		}
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_SWIMMING)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_SWIMMING,"นำเข้าข้อมูลสระว่ายน้ำ"));
//		}
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_VENDOR,"รายชื่อร้านค้า"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_ADMIN,"ผู้ดูแลระบบ"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_LOG,"ผู้ดูแลระบบ Log"));
	}
}
