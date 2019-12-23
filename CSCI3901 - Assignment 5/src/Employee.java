import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)

/*Employee class describes the following DTD description
*<!ELEMENT employee (first_name, last_name, office_city, active_customers, total_sales>
*/
public class Employee implements Serializable {
	private String first_name, last_name,office_city;
	private int active_customers;
	private double total_sales;
	public Employee() {
		super();
	}
	public Employee(String firstName, String lastName, String officeCity, int activeCustomers, double totalSales) {
		super();
		this.first_name = firstName;
		this.last_name = lastName;
		this.office_city = officeCity;
		this.active_customers = activeCustomers;
		this.total_sales = totalSales;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	public String getOfficeCity() {
		return office_city;
	}
	public void setOfficeCity(String officeCity) {
		this.office_city = officeCity;
	}
	public int getActiveCustomers() {
		return active_customers;
	}
	public void setActiveCustomers(int activeCustomers) {
		this.active_customers = activeCustomers;
	}
	public double getTotalSales() {
		return total_sales;
	}
	public void setTotalSales(double totalSales) {
		this.total_sales = totalSales;
	}
	

}
