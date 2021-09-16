package util.gson;

import java.time.LocalDate;

/**
 * @author ctl
 * @date 2021/8/10
 */
public class Employee {
	private Integer id;
	private String firstName;
	private Boolean active;
	private LocalDate dob;

	public Employee() {
	}

	public Employee(Integer id, String firstName, Boolean active, LocalDate dob) {
		this.id = id;
		this.firstName = firstName;
		this.active = active;
		this.dob = dob;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", active=" + active +
				", dob=" + dob +
				'}';
	}
}
