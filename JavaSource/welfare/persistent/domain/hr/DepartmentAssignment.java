package welfare.persistent.domain.hr;

import java.util.Date;

public class DepartmentAssignment implements Comparable<DepartmentAssignment>{
	private Long id;
	private Date effectiveDate;
	private Date expiredDate;
	private Employee employee;
	private Department department;

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date aEffectiveDate) {
		effectiveDate = aEffectiveDate;
	}

	public Employee getEmployees() {
		return employee;
	}

	public void setEmployee(Employee aEmployee) {
		employee = aEmployee;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department aDepartment) {
		department = aDepartment;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date aExpiredDate) {
		expiredDate = aExpiredDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public Employee getEmployee() {
		return employee;
	}

	@Override
	public String toString() {
		return employee+" : "+effectiveDate+" - "+expiredDate+" : "+department;
	}

	public int compareTo(DepartmentAssignment o) {
		if (o == null) {
			throw new IllegalArgumentException("Compared object must be instance of DepartmentAssignment.");
		}
		//if (id != null) return id.compareTo(o.getId());
		if (effectiveDate.equals(o.getEffectiveDate())) {
			return expiredDate.compareTo(o.getExpiredDate());
		} else {
			return effectiveDate.compareTo(o.getEffectiveDate());
		}
		
	}
	
}
