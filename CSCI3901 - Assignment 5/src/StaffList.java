import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "staff_list")
@XmlAccessorType(XmlAccessType.FIELD)

/*StaffList class describes the following DTD description
*<!ELEMENT staff_list (employee*)>
*/
public class StaffList implements Serializable{
	
	@XmlElement(name = "employee")
	private List<Employee> staff_list;

	public StaffList() {
		super();
	}

	public StaffList(List<Employee> staff_list) {
		super();
		this.staff_list = staff_list;
	}

	public List<Employee> getStaff_list() {
		return staff_list;
	}

	public void setStaff_list(List<Employee> staff_list) {
		this.staff_list = staff_list;
	}
	

}
