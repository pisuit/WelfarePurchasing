package welfare.persistent.domain.hr;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Department {
	private Long id;
	private String engShortName;
	private String engFullName;
	private String thaiShortName;
	private String thaiFullName;
	private boolean isActive;
	private List<DepartmentAssignment> departmentAssignments = new ArrayList<DepartmentAssignment>();

	public String getEngFullName() {
		return engFullName;
	}

	public void setEngFullName(String aEngFullName) {
		engFullName = aEngFullName;
	}

	public String getEngShortName() {
		return engShortName;
	}

	public void setEngShortName(String aEngShortName) {
		engShortName = aEngShortName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public String getThaiFullName() {
		return thaiFullName;
	}

	public void setThaiFullName(String aThaiFullName) {
		thaiFullName = aThaiFullName;
	}

	public String getThaiShortName() {
		return thaiShortName;
	}

	public void setThaiShortName(String aThaiShortName) {
		thaiShortName = aThaiShortName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean aIsActive) {
		isActive = aIsActive;
	}

	public List<DepartmentAssignment> getDepartmentAssignments() {
		return departmentAssignments;
	}

	public void setDepartmentAssignments(List<DepartmentAssignment> aDepartmentAssignments) {
		departmentAssignments = aDepartmentAssignments;
	}

	@Override
	public String toString() {
		return thaiShortName+"-"+thaiFullName;
	}

}
