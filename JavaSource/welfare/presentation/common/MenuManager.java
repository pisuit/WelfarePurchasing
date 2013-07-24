/**
 * 
 */
package welfare.presentation.common;

import javax.faces.context.FacesContext;

import welfare.persistent.domain.purchasing.PurchaseOrder;
import welfare.presentation.purchasing.PurchasingGRManager;
import welfare.presentation.purchasing.PurchasingPOManager;
import welfare.presentation.purchasing.PurchasingPRManager;
import welfare.presentation.purchasing.PurchasingVendorManager;
import welfare.presentation.stock.StockGIManager;
import welfare.presentation.stock.StockMovementManager;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class MenuManager {
	
	public MenuManager() {
	}
	
	public String pr(){
		PurchasingPRManager prManager =  (PurchasingPRManager) FacesUtils.getSessionObject("purchasingPR", PurchasingPRManager.class);
		if (prManager != null){
			prManager.refreshData();
		}
		return "MENU_PURCHASING_PR";
	}
	
	public String po(){
		PurchasingPOManager poManager = (PurchasingPOManager) FacesUtils.getSessionObject("purchasingPO", PurchasingPOManager.class);
		if (poManager != null ){
			poManager.refreshData();
		}
		return "MENU_PURCHASING_PO";
	}
	
	public String gr(){
		PurchasingGRManager grManager = (PurchasingGRManager) FacesUtils.getSessionObject("purchasingGR", PurchasingGRManager.class);
		if (grManager != null ){
			grManager.refreshData();
		}
		return "MENU_PURCHASING_GR";
	}
	
	public String movement(){
		StockMovementManager manager = (StockMovementManager) FacesUtils.getSessionObject("purchasingMovement", StockMovementManager.class);
		if (manager != null ){
			manager.refreshData();
		}
		return "MENU_STOCK_MOVEMENT";
	}
	public String gi(){
		StockGIManager manager = (StockGIManager) FacesUtils.getSessionObject("purchasingGI", StockGIManager.class);
		if (manager != null ){
			manager.refreshData();
		}
		return "MENU_STOCK_GI";
	}

}
