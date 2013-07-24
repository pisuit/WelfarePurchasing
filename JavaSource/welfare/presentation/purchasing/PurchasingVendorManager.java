/**
 * 
 */
package welfare.presentation.purchasing;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import welfare.persistent.controller.VendorController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.purchasing.Vendor;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.security.SecurityUser;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class PurchasingVendorManager {
	private VendorController controller ;
	
	private List<Vendor> venderList = new ArrayList<Vendor>(); // รายชื่อร้านค้า
	private Vendor editVendor = new Vendor(); // ร้านค้าที่ถูกแก้ไข
	private String welcomeMsg = null;
	
	public PurchasingVendorManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		controller = new VendorController();
		listVendor();
		newVendor();
	}
	
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public Vendor getEditVendor() {
		return editVendor;
	}

	public void setEditVendor(Vendor editVendor) {
		this.editVendor = editVendor;
	}

	public List<Vendor> getVenderList() {
		return venderList;
	}

	public void listVendor(){
		try {
			venderList = controller.getVendors();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void vendorTableRowClicked(){
		
	}
	
	public void saveVendor(){
		LogManager log = new LogManager();
		try {
			if (!editVendor.getStatus().equals(Status.DELETED)){
				if (editVendor.getId() == null){
					editVendor = controller.saveVendor(editVendor);
					log.recordCreateVendor(editVendor.getFormattedVendorNumber());
				} else {
					editVendor = controller.saveVendor(editVendor);
					log.recordUpdateVendor(editVendor.getFormattedVendorNumber());
				}
			} else {
				editVendor = controller.saveVendor(editVendor);
			}
			listVendor();
			newVendor();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteVendor(){
		LogManager log = new LogManager();
		editVendor.setStatus(Status.DELETED);
		log.recordDeleteVendor(editVendor.getFormattedVendorNumber());
		saveVendor();
	}
	
	public void newVendor(){
		editVendor = new Vendor();
	}
	
	public boolean isVendorDeletable(){
		return editVendor != null && editVendor.getId() != null && !editVendor.isDeleted();
	}
	
	public boolean isVendorNewable(){
		return editVendor != null && editVendor.getId() != null;
	}
	
	public boolean isVendorEditable(){
		return editVendor != null && !editVendor.isDeleted();
	}

}
