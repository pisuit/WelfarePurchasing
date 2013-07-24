/**
 * 
 */
package welfare.presentation.stock;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import welfare.persistent.controller.MatController;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.WarehouseController;
import welfare.security.SecurityUser;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author pisuit
 *
 */
public class StockMinimumManager {
	
	private ArrayList<Material> materialList = new ArrayList<Material>();
	private MatController controller = new MatController();
	private String warehouseCode;
	private WarehouseController warehouseController = new WarehouseController();
	
	public StockMinimumManager() {
		
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		try {
			warehouseCode = warehouseController.getWareHouseCode(securityUser);
			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		getMaterial();
	}
	
	public void getMaterial() {		
		try {
			materialList = controller.getMinimumMaterials(warehouseCode);

		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}		
	}

	public ArrayList<Material> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(ArrayList<Material> materialList) {
		this.materialList = materialList;
	}

}
