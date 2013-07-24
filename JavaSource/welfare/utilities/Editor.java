package welfare.utilities;

import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.chrono.BuddhistChronology;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.exception.ControllerException;
import welfare.utils.CalendarUtils;

public class Editor {
	ArrayList<BudgetItem> budgetItemList = new ArrayList<BudgetItem>();
	BudgetItem editBudgetItem = new BudgetItem();
	BudgetController budgetController = new BudgetController();
	
	public ArrayList<BudgetItem> getBudgetItemList() {
		return budgetItemList;
	}

	public void setBudgetItemList(ArrayList<BudgetItem> budgetItemList) {
		this.budgetItemList = budgetItemList;
	}

	public BudgetItem getEditBudgetItem() {
		return editBudgetItem;
	}

	public void setEditBudgetItem(BudgetItem editBudgetItem) {
		this.editBudgetItem = editBudgetItem;
	}

	public Editor(){
		createBudgetItem();
	}
	
	public void createBudgetItem(){
		if(budgetItemList != null) budgetItemList.clear();
		
		try {
			Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
			budgetItemList.addAll(budgetController.getAllBugetItems(CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime())));
			System.out.println("size = "+budgetItemList.size());
			System.out.println("year = "+new DateTime(BuddhistChronology.getInstance()).getYear());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveBudgetItem(){
		budgetController.saveEditorBudgetItem(editBudgetItem);
		editBudgetItem = new BudgetItem();
	}
	
	public void clear(){
		editBudgetItem = new BudgetItem();
	}
}
