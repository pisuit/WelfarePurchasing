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
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_PR,"�Ѵ�����駨Ѵ��"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_PO,"�Ѵ������§ҹ��صԨѴ��/���觫���"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_GR,"�Ѵ�����Ѻ��ʴ�"));
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_GI,"�ԡ��ʴ�"));
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_NURSERY) || selectedSubSystem.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_FF_PO,"��觫��������ʴ"));
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_FF_GR,"�Ѻ�����ʴ"));
//		}
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_NURSERY)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_NURSERY_CHILDEN,"ʶԵ���"));
//		}	
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_MAT,"�Ѵ�����ʴ�"));
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_CANTEEN) || selectedSubSystem.equals(Constants.SYS_PURCHASING_HOTEL)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_TRANSFER,"�͹��ʴ�"));
//		}
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_TRANSFER,"�͹��ʴ�"));
//		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_RETURN,"�׹��ʴ�"));
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_BANQUET,"�ͧ/�ԡ���ͨѴ����§"));
//		}
//		if (selectedSubSystem.equals(Constants.SYS_PURCHASING_SWIMMING)){
//			authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_SWIMMING,"����Ң�����������¹��"));
//		}
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_VENDOR,"��ª�����ҹ���"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_PURCHASING_ADMIN,"�������к�"));
		authorizationSelectItemList.add(new SelectItem(Constants.ROLE_LOG,"�������к� Log"));
	}
}
