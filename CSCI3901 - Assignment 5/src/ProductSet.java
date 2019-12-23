import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "product_set")
@XmlAccessorType (XmlAccessType.FIELD)

/*ProductSet class describes the following DTD description
*<!ELEMENT product_set (product_line_name, product*) >
*/
public class ProductSet implements Serializable{
	String product_line_name;
	
	@XmlElement(name = "product")
	List<Product> products;
	
	public String getProduct_line_name() {
		return product_line_name;
	}
	public void setProduct_line_name(String product_line_name) {
		this.product_line_name = product_line_name;
	}
	public ProductSet(String product_line_name, List<Product> products) {
		super();
		this.product_line_name = product_line_name;
		this.products = products;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public ProductSet() {
		super();
	}
}
