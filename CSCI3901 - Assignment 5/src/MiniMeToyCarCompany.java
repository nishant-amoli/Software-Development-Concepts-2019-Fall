import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//The runner class that that contains the main method
public class MiniMeToyCarCompany {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Instantiating DatabaseAccess class to connect to the database CSCI 3901. 
		DatabaseAccess dbObj = new DatabaseAccess();
		try{
			
			//Creating an object of Connection class by invoking
			//createConnectiong method of the class DatabaseAccess
			Connection con = dbObj.createConnection();
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(System.in));			
			
			String startDate;
			String endDate;
			
			System.out.print("\nEnter the start date:\n");	
			
			//Taking the start date from the user
			startDate = br.readLine();
			System.out.print("\nEnter the end date:\n");
			
			//Taking the end date from the user
			endDate = br.readLine();
			System.out.print("\nEnter the filename:\n");
			
			//Taking the filename from the user to store the summary
			//extracted from the database
			String fileName = "year_end_summary.xml";
			fileName = br.readLine();
			
			/*Instantiating the Year class having the following DTD description
			 * <!ELEMENT year (start_date, end_date) >
			 */
			Year year = new Year(startDate,endDate);
			
			//customer_list
			/*
			 * Question 2, Query a
			 * Report the customer name, address, number of orders in the period, and total
			 * order value
			 */
			String queryA = 
							" WITH orderWithDetails AS" +						
							" (SELECT DISTINCT o.orderNumber, o.customerNumber, SUM(od.priceEach * od.quantityOrdered) AS totalOrderValue FROM " +
							" orders o INNER JOIN orderdetails od ON o.orderNumber = od.orderNumber " +
							" WHERE o.orderDate BETWEEN " +
							" '" + startDate +"' AND '" + endDate +"' GROUP BY o.orderNumber " +
							" ) " + 
							" SELECT c.customerName,  " +
							" c.addressLine1 AS streetAddress, " +
							" c.city, c.postalCode, c.country, COUNT(owd.orderNumber) AS orderCount, SUM(owd.totalOrderValue) AS totalValue FROM " +
							" customers c INNER JOIN orderWithDetails owd ON  " +
							" c.customerNumber = owd.customerNumber " +
							" GROUP BY owd.customerNumber;";
			
			
			PreparedStatement statement = con.prepareStatement(queryA);
			ResultSet res = statement.executeQuery();
			
			/*Extracting the summary information from the database after
			 *executing the query into the CustomerList class object with the 
			 *following DTD description
			 *<!ELEMENT customer_list (customer*) >
			 */
			Customer customer;
			CustomerList custList = new CustomerList();
			List<Customer> list = new ArrayList<>();
			while(res.next()){
				/*Extracting the summary information from the database after
				 *executing the query into the Customer class object with the 
				 *following DTD description
				 *<!ELEMENET customer (customer_name, address, num_orders, order_value) >
				 */
				customer = new Customer(
						res.getString("customerName"), 
						new Address(res.getString("streetAddress"), res.getString("city"), res.getString("postalCode"), res.getString("country")),
						res.getInt("orderCount"), 
						res.getDouble("totalValue"));
				
				//Appending the objects of Customer class to a list
				//so that it can be attributed to CustomerList class 
				list.add(customer);
			}
			
			//Calling the setter method of CustomerList class to
			//update the state of its object
			custList.setCustomer_list(list);
			
			res.close();
			statement.close();
			
			//product_list
			/*
			 * Question 2, Query b
			 * Report, for each product line, the product line name and for each
			 * product in the product line report the name, vendor, units sold, 
			 * and total value of products sold
			 */
			String queryB = "WITH orderDesc AS"+
					" ("+
					" SELECT od.productCode, SUM(od.quantityOrdered) as unitsSold,"+
					" od.priceEach FROM"+
					" orderdetails od INNER JOIN orders o ON o.orderNumber = od.orderNumber"+
					" WHERE o.orderDate BETWEEN '"+startDate+"' AND '"+endDate+"'"+
					" GROUP BY od.productCode"+
					" )"+
					" SELECT p.productLine,p.productName, p.productVendor, od.unitsSold,"+
					" od.unitsSold * od.priceEach AS totalSales FROM"+
					" products p INNER JOIN orderDesc od ON p.productCode = od.productCode;";
			statement = con.prepareStatement(queryB);
			res = statement.executeQuery();
			
			List<ProductSet> listObj = new ArrayList();
			HashMap <String, ArrayList<Product>> mapObj = null;
			while(res.next()){
				String productLine = res.getString("productLine");
				
				/*Extracting the summary information from the database after
				 *executing the query into the Product class object with the 
				 *following DTD description
				 *<!ELEMENT product_set (product_line_name, product*) >
				 *<!ELEMENT product (product_name, product_vendor, units_sold, total_sales) >
				 */
				Product product = new Product(res.getString("productName"),
						res.getString("productVendor"),res.getInt("unitsSold"),
						res.getDouble("totalSales"));
				
				
				//If map is empty
				if(mapObj == null){
					mapObj = new HashMap<String, ArrayList<Product>>();	
					mapObj.put(productLine, new ArrayList<Product>());
				}
				
				//If map exists but doesn't contains the productLine name as a key
				if(!mapObj.containsKey(productLine)){
					mapObj.put(productLine, new ArrayList<Product>());					
				}
				
				mapObj.get(productLine).add(product);				
			}
			
			/*Extracting the summary information from the database after
			 *executing the query into the ProductList class object with the 
			 *following DTD description
			 *<!ELEMENT product_list (product_set*) >
			 */			
			List productSetList = new ArrayList<ProductSet>();
			if(null != mapObj){
				for(String key:mapObj.keySet()){
					ProductSet productset = new ProductSet(key,mapObj.get(key));
					productSetList.add(productset);
				}
			}
			ProductList productlist = new ProductList(productSetList);			
			res.close();
			statement.close();
			
			
			//staff_List
			/*
			 * Question 2, Query c
			 * Report, for each employee, their name, office, number of customers
			 * active in the period, and total order value
			 */
			String queryC = "WITH orderDesc AS"+
					" ("+
					" SELECT o.orderNumber, o.customerNumber,"+
					" SUM(od.quantityOrdered * od.priceEach) AS totalOrderSales FROM"+
					" orders o INNER JOIN orderdetails od ON o.orderNumber = od.orderNumber"+
					" WHERE o.orderDate BETWEEN '"+startDate+"' AND '"+endDate+"'"+
					" GROUP BY od.orderNumber"+
					" ),"+
					" customerSales AS"+
					" ("+
					" SELECT c.customerNumber, c.salesRepEmployeeNumber,"+
					" SUM(od.totalOrderSales) AS totalCustomerSales FROM"+
					" customers c INNER JOIN orderDesc od"+
					" ON od.customerNumber = c.customerNumber"+
					" GROUP BY c.customerNumber"+
					" ),"+
					" employeeOffices AS"+
					" ("+
					" SELECT e.employeeNumber, e.firstName, e.lastName, o.city FROM"+
					" employees e INNER JOIN offices o ON o.officeCode = e.officeCode"+
					" )"+
					" SELECT eo.firstName, eo.lastName, eo.city,"+
					" COUNT(cs.salesRepEmployeeNumber) AS activeCustomers,"+
					" SUM(cs.totalCustomerSales) AS totalSales FROM"+
					" employeeOffices eo INNER JOIN customerSales cs ON"+
					" eo.employeeNumber = cs.salesRepEmployeeNumber"+
					" GROUP BY cs.salesRepEmployeeNumber;";
			statement = con.prepareStatement(queryC);
			res = statement.executeQuery();
			
			/*Extracting the summary information from the database after
			 *executing the query into the Employee class object with the 
			 *following DTD description
			 *<!ELEMENT staff_list (employee*)>
			 *<!ELEMENT employee (first_name, last_name, office_city, active_customers, total_sales>
			 */
			Employee employee;
			StaffList staffList = new StaffList();
			List<Employee> empList = new ArrayList<>();
			while(res.next()){
				employee = new Employee(res.getString("firstName"),
						res.getString("lastName"), res.getString("city"),
						res.getInt("activeCustomers"), res.getDouble("totalSales"));
				empList.add(employee);
			}
			/*Extracting the summary information from the database after
			 *executing the query into the StaffList class object with the 
			 *following DTD description
			 *<!ELEMENT staff_list (employee*)>
			 */
			staffList.setStaff_list(empList);			
			res.close();
			statement.close();
			
			//Invoking the closeConnection method of DatabaseAccess class
			//to terminate the connection
			dbObj.closeConnection(con);
			
			
			/*Extracting the summary information from the database after
			 *executing the query into the YearEndSummary class object with the 
			 *following DTD description
			 *<!ELEMENT year_end_summary (year, customer_list, product_list) >
			 */
			YearEndSummary summary = new YearEndSummary(year,custList,productlist,staffList);
			XMLFileWriter.write(summary, fileName);
			
		}
		catch(Exception e){e.printStackTrace();}
	}

}
