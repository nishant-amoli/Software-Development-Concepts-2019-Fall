import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)

/*Customer class describes the following DTD description
*<!ELEMENT customer (customer_name, address, num_orders, order_value) >
*/
public class Customer implements Serializable{
	private String customer_name;
	private Address address;
	private int num_orders;
	private double order_value;
	public Customer() {
		super();
	}
	public Customer(String customerName, Address address, int orderCount, double totalValue) {
		super();
		this.customer_name = customerName;
		this.address = address;
		this.num_orders = orderCount;
		this.order_value = totalValue;
	}
	public String getCustomerName() {
		return customer_name;
	}
	public void setCustomerName(String customerName) {
		this.customer_name = customerName;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public int getOrderCount() {
		return num_orders;
	}
	public void setOrderCount(int orderCount) {
		this.num_orders = orderCount;
	}
	public double getTotalValue() {
		return order_value;
	}
	public void setTotalValue(double totalValue) {
		this.order_value = totalValue;
	}
}
