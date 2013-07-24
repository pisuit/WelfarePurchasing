package welfare.persistent.domain.budget;

import java.util.ArrayList;
import java.util.List;

public class Budget {
	private Long id;
	private int budgetYear;					// �է�����ҳ
	private boolean isAvailable = false;	// ����ö�������������� (true=��, false=�����)
	private boolean isEditable = true;		// ����ö������ա? (true=��, false=�����)
	private List<BudgetItem> budgetItems = new ArrayList<BudgetItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(int budgetYear) {
		this.budgetYear = budgetYear;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}
	
	public List<BudgetItem> getMainBudgetItem(){
		List<BudgetItem> items = new ArrayList<BudgetItem>();
		for (BudgetItem budgetItem : budgetItems) {
			if (budgetItem.getParentBudgetItem() == null){
				items.add(budgetItem);
			}
		}
		return items;
	}

}
