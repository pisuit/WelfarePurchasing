package welfare.persistent.domain.hr;

import java.util.Date;

public class Holiday {
	private Long id;
	private Date date;
	private String description;
	private Date insteadOfDate;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getInsteadOfDate() {
		return insteadOfDate;
	}

	public void setInsteadOfDate(Date insteadOfDate) {
		this.insteadOfDate = insteadOfDate;
	}

	public String toString() {
		return "Date: "+date+" : "+description;
	}
}
