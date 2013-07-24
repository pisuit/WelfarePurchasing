package welfare.persistent.domain.hr;

import java.util.ArrayList;
import java.util.List;

public class Employee {
	private Long id;
	private String employeeCode;
	private String thaiFirstName;
	private String thaiLastName;
	private String engFirstName;
	private String engLastName;
	private NamePrefix namePrefix;
	private List<DepartmentAssignment> departmentAssignments = new ArrayList<DepartmentAssignment>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getThaiFirstName() {
		return thaiFirstName;
	}

	public void setThaiFirstName(String thaiFirstName) {
		this.thaiFirstName = thaiFirstName;
	}

	public String getThaiLastName() {
		return thaiLastName;
	}

	public void setThaiLastName(String thaiLastName) {
		this.thaiLastName = thaiLastName;
	}

	public String getEngFirstName() {
		return engFirstName;
	}

	public void setEngFirstName(String engFirstName) {
		this.engFirstName = engFirstName;
	}

	public String getEngLastName() {
		return engLastName;
	}

	public void setEngLastName(String engLastName) {
		this.engLastName = engLastName;
	}
	
	public NamePrefix getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(NamePrefix namePrefix) {
		this.namePrefix = namePrefix;
	}

	public void setDepartmentAssignments(List<DepartmentAssignment> departmentAssignments) {
		this.departmentAssignments = departmentAssignments;
	}

	public List<DepartmentAssignment> getDepartmentAssignments() {
		return departmentAssignments;
	}
	
	public DepartmentAssignment getCurrentDepartmentAssignment() {
		if (departmentAssignments != null && departmentAssignments.size() > 0) {
			return departmentAssignments.get(departmentAssignments.size()-1);
		}
		return null;
	}

	public String toString() {
		return thaiFirstName+" "+thaiLastName;
	}
}
