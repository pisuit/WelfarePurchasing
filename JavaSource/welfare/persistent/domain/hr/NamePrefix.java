package welfare.persistent.domain.hr;

public class NamePrefix {
	private Long id;
	private String engShortName;
	private String engFullName;
	private String thaiShortName;
	private String thaiFullName;

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
}
