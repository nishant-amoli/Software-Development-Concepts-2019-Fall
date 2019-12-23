import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@XmlRootElement(name = "customer_list")
@XmlAccessorType(XmlAccessType.FIELD)

/*CustomerList class describes the following DTD description
*<!ELEMENT customer_list (customer*) >
*/
public class CustomerList implements Serializable {
	
	@XmlElement(name = "customer")
	private List<Customer> customer_list;

	public CustomerList() {
		super();
	}

	public CustomerList(List<Customer> customer_list) {
		super();
		this.customer_list = customer_list;
	}
	
	public List<Customer> getCustomer_list() {
		return customer_list;
	}
	
	public void setCustomer_list(List<Customer> customer_list) {
		this.customer_list = customer_list;
	}
}
