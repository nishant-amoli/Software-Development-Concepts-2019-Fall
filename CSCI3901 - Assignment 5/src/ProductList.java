import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "product_list")
@XmlAccessorType (XmlAccessType.FIELD)

/*ProductList class describes the following DTD description
*<!ELEMENT product_list (product_set*) >
*/
public class ProductList implements Serializable{
	
@XmlElement(name = "product_set")
private List<ProductSet> productSet;


public ProductList() {
	super();
}
public ProductList(List<ProductSet> productSet) {
	super();
	this.productSet = productSet;
}

public List<ProductSet> getProductSet() {
	return productSet;
}

public void setProductSet(List<ProductSet> productSet) {
	this.productSet = productSet;
}

}
