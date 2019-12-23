import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)

/*Address class describes the following DTD description
*<!ELEMENT address (street_address, city, postal_code, country) >
*/
public class Address implements Serializable{
	private String street_address;
	private String city;
	private String postal_code;
	private String country;
	public Address() {
		super();
	}
	public Address(String streetAddress, String city, String postalCode, String country) {
		super();
		this.street_address = streetAddress;
		this.city = city;
		this.postal_code = postalCode;
		this.country = country;
	}
	public String getStreetAddress() {
		return street_address;
	}
	public void setStreetAddress(String streetAddress) {
		this.street_address = streetAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postal_code;
	}
	public void setPostalCode(String postalCode) {
		this.postal_code = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
