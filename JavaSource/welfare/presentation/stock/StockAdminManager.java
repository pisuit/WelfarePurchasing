package welfare.presentation.stock;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import welfare.persistent.controller.StockAdminController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.security.SecurityUser;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

public class StockAdminManager {
	private StockAdminController stockAdminController;
	private ArrayList<SelectItem> subSystemSelectItemList = new ArrayList<SelectItem>();
	private String selectedSubSystem ;
	private ArrayList<User> userList;
	private User editUser = new User();
	private ArrayList<String> selectedUserAuthorizationList = new ArrayList<String>();
	private ArrayList<SelectItem> authorizationSelectItemList =  new ArrayList<SelectItem>();
	private String welcomeMsg = null;
	
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public ArrayList<SelectItem> getSubSystemSelectItemList() {
		return subSystemSelectItemList;
	}

	public void setSubSystemSelectItemList(
			ArrayList<SelectItem> subSystemSelectItemList) {
		this.subSystemSelectItemList = subSystemSelectItemList;
	}

	public String getSelectedSubSystem() {
		return selectedSubSystem;
	}

	public void setSelectedSubSystem(String selectedSubSystem) {
		this.selectedSubSystem = selectedSubSystem;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
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
			ArrayList<String> selectedUserAuthorizationList) {
		this.selectedUserAuthorizationList = selectedUserAuthorizationList;
	}

	public ArrayList<SelectItem> getAuthorizationSelectItemList() {
		return authorizationSelectItemList;
	}

	public void setAuthorizationSelectItemList(
			ArrayList<SelectItem> authorizationSelectItemList) {
		this.authorizationSelectItemList = authorizationSelectItemList;
	}

	public StockAdminManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		stockAdminController = new StockAdminController();
		createSubSystemSelectItemList();
		createAuthorizationSelectItemList();
		listUser();
	}
	
	private void createSubSystemSelectItemList(){
		selectedSubSystem = Constants.WH_MAHAMEK;
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_CANTEEN,"�к��ҹ����ҡ����������"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_SPORT,"�к��ҹ����"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_NURSERY,"�к��ҹʶҹ�Ѻ����§��"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_HOTEL,"�к��ҹ��ͧ�ѡ����پ��"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_MEDICAL,"�к��ҹ�Ǫ�ѳ��"));
//		subSystemSelectItemList.add(new SelectItem(Constants.SYS_PURCHASING_SWIMMING,"�к��ҹ������¹��"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_MAHAMEK,"���������"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_CHIANGMAI,"�ٹ����§����"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_HADYAI,"�ٹ���Ҵ�˭�"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_HUAHIN,"�ٹ������Թ"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_NAKORN,"�ٹ�칤��Ҫ����"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_PHITSANULOK,"�ٹ���ɳ��š"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_PUKET,"�ٹ������"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_SURAT,"�ٹ������ɮ��ҹ�"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_UBON,"�ٹ���غ��Ҫ�ҹ�"));
		subSystemSelectItemList.add(new SelectItem(Constants.WH_UDON,"�ٹ���شøҹ�"));
	}
	
	private void listUser(){
		try {
			if (userList != null) userList.clear();
			userList = stockAdminController.getSubSystemUserList(Constants.SYS_MAIN_STOCK, selectedSubSystem);
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
			User checkDuplicate = stockAdminController.checkDuplicateUser(editUser.getUsername());
			if (checkDuplicate != null && editUser.getId() == null) 
				throw new ControllerException("�ժ��ͼ��������к�����");	
			else {
			System.out.println("Save");
			ArrayList<Authorization> authorizationList = new ArrayList<Authorization>();
			if (selectedUserAuthorizationList.size() == 0) {
				throw new ControllerException("��س��к��Է�ԡ����ҹ���ҧ����˹���Է�� ");
			}
			Authorization newAuthorization;
			for (String authorizationStr : selectedUserAuthorizationList) {
				newAuthorization = new Authorization();
				newAuthorization.setUser(editUser);
				newAuthorization.setSystemRole(authorizationStr);
				authorizationList.add(newAuthorization);
			}
			editUser.setMainSystemName(Constants.SYS_MAIN_STOCK);
//			editUser.setSubSystemName(selectedSubSystem);
			editUser.setSubSystemName(Constants.SYS_STOCK_STOCK);
			editUser.setWarehouseCode(selectedSubSystem);
			if (editUser.getId() == null) {
				stockAdminController.saveUser(editUser, authorizationList);
				log.recordCreateStockUser(editUser.getUsername(), editUser.getMainSystemName());
			} else {
				stockAdminController.saveUser(editUser, authorizationList);
				log.recordCreateStockUser(editUser.getUsername(), editUser.getMainSystemName());
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
			stockAdminController.deleteUser(editUser);
//			log.recordDeletePurchasingUser(editUser.getUsername(), editUser.getSubSystemName());
			log.recordDeleteStockUser(editUser.getUsername(), editUser.getMainSystemName());
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
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_STOCK_MATGROUP, "�Ѵ��á������ʴ�"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_STOCK_MAT, "�Ѵ�����ʴ�"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_STOCK_ADMIN, "�������к�"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_LOG,"�������к� Log"));
	}
}
