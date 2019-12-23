import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "year")
@XmlAccessorType (XmlAccessType.FIELD)

/*Year class describes the following DTD description
*<!ELEMENT year (start_date, end_date) >
*/
public class Year implements Serializable{

	private String start_date;
	private String end_date;
	public Year() {
		super();
	}
	public Year(String startDate, String endDate) {
		super();
		this.start_date = startDate;
		this.end_date = endDate;
	}
	public String getStartDate() {
		return start_date;
	}
	public void setStartDate(String startDate) {
		this.start_date = startDate;
	}
	public String getEndDate() {
		return end_date;
	}
	public void setEndDate(String endDate) {
		this.end_date = endDate;
	}
}
