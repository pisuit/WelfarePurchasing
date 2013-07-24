package welfare.persistent.domain.security;

import java.util.ArrayList;
import java.util.List;

public class BudgetAuthorization {
	private Long id;
	private String budgetAuth; 
	private User user = new User();

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public String getBudgetAuth() {
		return budgetAuth;
	}

	public void setBudgetAuth(String budgetAuth) {
		this.budgetAuth = budgetAuth;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String toString(){
		return budgetAuth;
	}

}
