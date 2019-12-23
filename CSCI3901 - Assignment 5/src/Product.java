import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "product")
@XmlAccessorType (XmlAccessType.FIELD)

/*Product class describes the following DTD description
*<!ELEMENT product (product_name, product_vendor, units_sold, total_sales) >
*/
public class Product implements Serializable{

	private String product_name;
	private String product_vendor;
	private int units_sold;
	private double total_sales;
	public Product() {
		super();
	}
	public Product(String product_name, String product_vendor, int units_sold, double total_sales) {
		super();
		this.product_name = product_name;
		this.product_vendor = product_vendor;
		this.units_sold = units_sold;
		this.total_sales = total_sales;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_vendor() {
		return product_vendor;
	}
	public void setProduct_vendor(String product_vendor) {
		this.product_vendor = product_vendor;
	}
	public int getUnits_sold() {
		return units_sold;
	}
	public void setUnits_sold(int units_sold) {
		this.units_sold = units_sold;
	}
	public double getTotal_sales() {
		return total_sales;
	}
	public void setTotal_sales(double total_sales) {
		this.total_sales = total_sales;
	}
}
