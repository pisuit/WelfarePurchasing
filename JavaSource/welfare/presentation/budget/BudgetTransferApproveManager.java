/**
 * 
 */
package welfare.presentation.budget;

import java.util.ArrayList;
import java.util.Calendar;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.customtype.BudgetItemType;
import welfare.persistent.customtype.BudgetTransferStatus;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.budget.BudgetTransfer;
import welfare.persistent.domain.security.Authorization;
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
public class BudgetTransferApproveManager {
	private BudgetController budgetController;
	private Budget editBudget;						// งบประมาณประจำปีที่ต้องการโอน
	/* list panel */
	private int listBudgetYear;						// ปีงบประมาณ
	private ArrayList<BudgetTransfer> budgetTransferList = new ArrayList<BudgetTransfer>();	// รายการใบโอนงบประมาณ
	private BudgetTransfer editBudgetTransfer;			// ใบโอนงบประมาณที่กำลังถูกดำเนินการ
	/* edit panel */
	private ArrayList<SelectItem> budgetItemSelectItemList = new ArrayList<SelectItem>(); // combobox Item สำหรับให้เลือกหมวดงบประมาณ
	private Long selectedFromBudgetItemId; // ID ของหมวดงบประมาณที่ต้องการโอนออก
	private Long selectedToBudgetItemId; // ID ของหมวดงบประมาณที่ต้องการออนเข้า
	private BudgetItem editFromBudgetItem; // หมวดงบประมาณที่ต้องการโอนออก
	private BudgetItem editToBudgetItem; // หมวดงบประมาณที่ต้องการโอนเข้า
	private String welcomeMsg = null;
	private boolean isMainTransfer = false;
	private boolean isMainApproveAuth = false;
	private boolean isSubApproveAuth = false;

	public BudgetTransferApproveManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		for (Authorization auth : securityUser.getAuthorizations()) {
			if (auth.getSystemRole().equals(Constants.ROLE_BUDGET_TRANSFER_APPROVE_MAIN)) {
				isMainApproveAuth = true;
			}
			if (auth.getSystemRole().equals(Constants.ROLE_BUDGET_TRANSFER_APPROVE_SUB)) {
				isSubApproveAuth = true;
			}
		}
		welcomeMsg = securityUser.getFullName();
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		budgetController = new BudgetController();
		listBudgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		listBudgetTransfer();
		buildBudgetItemSelectItemList();
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public int getListBudgetYear() {
		return listBudgetYear;
	}


	public void setListBudgetYear(int listBudgetYear) {
		this.listBudgetYear = listBudgetYear;
	}


	public BudgetTransfer getEditBudgetTransfer() {
		return editBudgetTransfer;
	}


	public void setEditBudgetTransfer(BudgetTransfer editBudgetTransfer) {
		this.editBudgetTransfer = editBudgetTransfer;
	}


	public Budget getEditBudget() {
		return editBudget;
	}


	public ArrayList<BudgetTransfer> getBudgetTransferList() {
		return budgetTransferList;
	}
	
	public ArrayList<SelectItem> getBudgetItemSelectItemList() {
		return budgetItemSelectItemList;
	}


	public Long getSelectedFromBudgetItemId() {
		return selectedFromBudgetItemId;
	}


	public void setSelectedFromBudgetItemId(Long selectedFromBudgetItemId) {
		this.selectedFromBudgetItemId = selectedFromBudgetItemId;
	}


	public Long getSelectedToBudgetItemId() {
		return selectedToBudgetItemId;
	}


	public void setSelectedToBudgetItemId(Long selectedToBudgetItemId) {
		this.selectedToBudgetItemId = selectedToBudgetItemId;
	}


	public BudgetItem getEditFromBudgetItem() {
		return editFromBudgetItem;
	}


	public BudgetItem getEditToBudgetItem() {
		return editToBudgetItem;
	}


	public void listBudgetTransfer(){
		try {
			editBudget = budgetController.getBudget(listBudgetYear);
			if (editBudget != null){
				budgetTransferList = budgetController.getBudgetTransfers(listBudgetYear);
			} else {
				budgetTransferList.clear();
			}
			editBudgetTransfer = null;
			editFromBudgetItem = null;
			editToBudgetItem = null;
			selectedFromBudgetItemId = null;
			selectedToBudgetItemId = null;
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void buildBudgetItemSelectItemList() {
		try {
			//editBudget = budgetController.getBudget(listBudgetYear);
			budgetItemSelectItemList.clear();
			ArrayList<BudgetItem> budgetItemList = budgetController.getAllBugetItems(listBudgetYear);
			SelectItem selectItem;
			for (BudgetItem budgetItem : budgetItemList) {
				selectItem = new SelectItem();
				selectItem.setValue(budgetItem.getId());
				selectItem.setLabel(budgetItem.getCategory());
				budgetItemSelectItemList.add(selectItem);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void tableBudgetTransferRowClicked(){
		editFromBudgetItem = editBudgetTransfer.getFromBudgetItem();
		editToBudgetItem = editBudgetTransfer.getToBudgetItem();
		if (editBudgetTransfer.getOldFromBudgetItem().getId().equals(editBudgetTransfer.getFromBudgetItem().getId())) {
			selectedFromBudgetItemId = null;
		} else {
			selectedFromBudgetItemId = editFromBudgetItem.getId();
		}
		if (editBudgetTransfer.getOldToBudgetItem().getId().equals(editBudgetTransfer.getToBudgetItem().getId())) {
			selectedToBudgetItemId = null;
		} else {
			selectedToBudgetItemId = editToBudgetItem.getId();
		}		
//		if (editFromBudgetItem.getAccountCode().substring(0, 4).equals(editToBudgetItem.getAccountCode().substring(0, 4))) {
//			isMainTransfer = false;
//		} else {
//			isMainTransfer = true;
//		}
		if (editFromBudgetItem.getBudgetType().equals(BudgetItemType.M) || editToBudgetItem.getBudgetType().equals(BudgetItemType.M)) {
			isMainTransfer = true;
		} else {
			isMainTransfer = false;
		}
		
		System.out.println("From = "+editFromBudgetItem.getCategory());
		System.out.println("To = "+editToBudgetItem.getCategory());
	}
		
	public void fromBudgetItemComboboxSelected(){
		try {
			editFromBudgetItem = budgetController.getBudgetItem(selectedFromBudgetItemId);
		} catch (ControllerException e) {
			e.printStackTrace();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
		}
	}
	
	public void toBudgetItemComboboxSelected(){
		try {
			editToBudgetItem = budgetController.getBudgetItem(selectedToBudgetItemId);
		} catch (ControllerException e) {
			e.printStackTrace();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
		}
	}
	
	/**
	 * อนุมัติการโอนงบประมาณ
	 */
	public void approveTransfer(){
		BudgetItem oldFromBudgetItem = null;
		BudgetItem oldToBudgetItem = null;
		LogManager log = new LogManager();
		try {
			// ถ้ามีการแก้ไขหมวดงบประมาณโอนออก
			oldFromBudgetItem = editBudgetTransfer.getOldFromBudgetItem();
			if (editFromBudgetItem != null){
				editBudgetTransfer.setFromBudgetItem(editFromBudgetItem); 
			} else {
				editBudgetTransfer.setFromBudgetItem(oldFromBudgetItem);
			}
			if (!editBudgetTransfer.getFromBudgetItem().getBudget().isAvailable()
					|| !editBudgetTransfer.getFromBudgetItem().isAvailable() 
					|| ( editBudgetTransfer.getFromBudgetItem().getAvailableAmount().doubleValue() <= 0 && editBudgetTransfer.getFromBudgetItem().isControlled())){
				throw new ControllerException("หมวดงบประมาณ \""+editFromBudgetItem.getCategory()+"\" จำนวนเงินไม่เพียงพอตามที่ท่านต้องการหรือมีการปิดการใช้งบประมาณ");
			}
			// ถ้ามีการแก้ไขหมวดงบประมาณโอนเข้า
			oldToBudgetItem = editBudgetTransfer.getOldToBudgetItem();
			if (editFromBudgetItem != null){
				editBudgetTransfer.setToBudgetItem(editToBudgetItem); 
			} else {
				editBudgetTransfer.setToBudgetItem(oldToBudgetItem);
			}
			if (!editBudgetTransfer.getToBudgetItem().getBudget().isAvailable()
					|| !editBudgetTransfer.getToBudgetItem().isAvailable()) {
				throw new ControllerException("หมวดงบประมาณ \""+editFromBudgetItem.getCategory()+"\" มีการปิดการใช้งบประมาณ");
			}
			// อนุญาติให้โอนงบประมาณภายในหมวดหลักเดียวกันเท่านั้น
			/*
			boolean isTransferable = false;
			if (editFromBudgetItem.getParentCategory() == null && editToBudgetItem.getParentCategory() == null ){
				isTransferable = true;
			} else {
				if (editFromBudgetItem.getParentCategory() != null && editToBudgetItem.getParentCategory() != null ) {
					if (editFromBudgetItem.getParentCategory().getId().equals(editToBudgetItem.getParentCategory().getId())){
						isTransferable = true;
					} else {
						isTransferable = false;
					}
				} else {
					isTransferable = false;
				}
			}
			if (!isTransferable) {
				throw new ControllerException("อนุญาตให้โอนงบภายใต้หมวดหลักเดียวกันเท่านั้น");
			}
			*/
			budgetController.approveBudgetTransfer(editBudgetTransfer);
			log.recordApproveTransfer(editBudgetTransfer.getFormattedTransferNumber());
			listBudgetTransfer();		
		} catch (ControllerException e) {
			editBudgetTransfer.setFromBudgetItem(oldFromBudgetItem);
			editBudgetTransfer.setToBudgetItem(oldToBudgetItem);
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * ไม่อนุมัติการโอนงบประมาณ
	 */
	public void discardTransfer(){
		LogManager log = new LogManager();
		try {
			editBudgetTransfer.setApproveDate(CalendarUtils.getDateTimeInstance().getTime());
			editBudgetTransfer.setStatus(BudgetTransferStatus.DISCARDED);
			budgetController.saveBudgetTransfer(editBudgetTransfer,false);
			log.recordDiscardTransfer(editBudgetTransfer.getFormattedTransferNumber());
			listBudgetTransfer();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	/**
	 * สามารถอนุมัติใบโอนได้หรือไม่
	 * @return
	 */
	public boolean isApproveable(){
		return editBudget != null && editBudget.isAvailable() && editBudgetTransfer != null && editBudgetTransfer.isApproveable() && isUserApprovable();  
	}
	
	private boolean isUserApprovable(){
		if (isMainTransfer == true && isMainApproveAuth == true && isSubApproveAuth == false) {
			return true;
		} else if (isMainTransfer == false && isSubApproveAuth == true && isMainApproveAuth == false) {
			return true;
		} else if (isMainTransfer == true && isMainApproveAuth == true && isSubApproveAuth == true) {
			return true;
		} else if(isMainTransfer == false && isMainApproveAuth == true && isSubApproveAuth == true){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * สามารถอนุมัติใบโอนได้หรือไม่
	 * @return
	 */
	public boolean isDiscardable(){
		return editBudget != null && editBudget.isAvailable() && editBudgetTransfer != null && editBudgetTransfer.isDiscardable();  
	}

}
