import java.io.File;
import javax.xml.bind.*;

public class XMLFileWriter {
	public static void write(YearEndSummary data, String xmlFileName) throws JAXBException
	{
	    JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	    //Invoking marshal method to print the year_end_summary in console
	    jaxbMarshaller.marshal(data, System.out);
	     
	    //Invoking marshal method to store the year_end_summary in the file
	    //that has been input by the user
	    jaxbMarshaller.marshal(data, new File(xmlFileName));
	}
}
