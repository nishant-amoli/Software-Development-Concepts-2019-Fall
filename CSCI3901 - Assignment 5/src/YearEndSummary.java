import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "year_end_summary")
@XmlAccessorType (XmlAccessType.FIELD)

/*YearEndSummary class describes the following DTD description
*<!ELEMENT year_end_summary (year, customer_list, product_list) >
*/
public class YearEndSummary implements Serializable{

	private Year year;
	private CustomerList customer_list;
	private ProductList product_list;
	private StaffList staff_list;

	public YearEndSummary() {
		super();
	}
	public YearEndSummary(Year year, CustomerList custList, ProductList productList, StaffList straffList) {
		super();
		this.customer_list = custList;
		this.staff_list = straffList;
		this.product_list = productList;
		this.year = year;
	}
	public ProductList getProductList() {
		return product_list;
	}
	public void setProductList(ProductList productList) {
		this.product_list = productList;
	}
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public CustomerList getCustList() {
		return customer_list;
	}
	public void setCustList(CustomerList custList) {
		this.customer_list = custList;
	}
	public StaffList getStraffList() {
		return staff_list;
	}
	public void setStraffList(StaffList straffList) {
		this.staff_list = straffList;
	}
}
