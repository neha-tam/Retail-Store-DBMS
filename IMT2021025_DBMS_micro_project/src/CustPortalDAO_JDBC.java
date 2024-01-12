import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;  
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustPortalDAO_JDBC implements CustomerPortalDAO{

    Connection dbConnection;

    public CustPortalDAO_JDBC(Connection dbconn)
	{
		dbConnection = dbconn;
	}

    public void OrderItemsByPrice()
    {
        String sql;
        Statement stmt = null;

        try{
			stmt = dbConnection.createStatement();
            sql = "select count(*) from Inventory";    //checking the number of entries in the Inventory, if there are 0 rows, display a message to the customer
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            if(rs.getInt(1) == 0)
            {
                System.out.println("No items currently available.Sorry!");
            }
			else
            {
                sql = "select productID, productName, price, quantity from Inventory order by productName,price";
			    rs = stmt.executeQuery(sql);
    
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("ProductID"+"\t"+"Product"+"\t"+"Price"+"\t"+"Quantity");
                while (rs.next())   //printing out the inventory items in the order of price as in the result set
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        if (i > 1)
                        {
                            System.out.print("\t");
                        }
                        if(i == 2)
                        {
                            System.out.print("\t");
                        }
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println("");
                }
            }
            System.out.println("");
            
            System.out.println("Press enter to continue.\n");
            inputclass.in.nextLine();
            inputclass.in.nextLine();
            
		}
        catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
        
    }

    public void checkInventory()
    {
        String sql;
        Statement stmt = null;

        try{
			stmt = dbConnection.createStatement();
            sql = "select count(*) from Inventory";    //checking the number of entries in the Inventory, if there are 0 rows, display a message to the customer
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            if(rs.getInt(1) == 0)
            {
                System.out.println("No items currently available.Sorry!");
            }
			else
            {
                sql = "select productID, productName, price, quantity from Inventory";
			    rs = stmt.executeQuery(sql);

			    ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("ProductID"+"\t"+"Product"+"\t"+"Price"+"\t"+"Quantity");   //displaying the Inventory
                while (rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        if (i > 1)
                        {
                            System.out.print("\t");
                        }
                        if(i == 2)
                        {
                            System.out.print("\t");
                        }
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println("");

                }
                
            }
            System.out.println("Press enter to continue.");
            inputclass.in.nextLine();
            inputclass.in.nextLine();   //still testing

            
		}
        catch (SQLException ex)
        {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

	public void buyItems(Integer transNo, Customer cust,Integer transID, Integer prodID, Integer quantity)
	{

        String sql1, sql2, sql3, sql = null;
        // Initialization
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement stmt = null;

		 try
         {

            Integer quan;

            sql = "SELECT quantity FROM Inventory WHERE productID = (?)";
            stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1,prodID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
			{
                quan = rs.getInt("quantity");
                if(quantity>quan || quantity < 1)
                {
                    System.out.println("Please check the inventory below and buy a valid number of products.");
                    checkInventory();
                }
            
                else
                {

                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String strDate = currentDate.format(formatter);

                // java.sql.Date beg = new java.sql.Date(dateFormat.parse(strDate).getTime());

                // sql statements
                sql1 = "INSERT INTO Transaction (transactionNo, transactionID, customerID, dateOfPurchase, productID, quantityBought)VALUES (?, ?, ?, ?, ?, ?);";
                sql2 = "DELETE FROM Product WHERE productID IN (SELECT productID FROM Inventory WHERE productID = (?)) LIMIT 1;";
                sql3 = "UPDATE Inventory SET quantity = quantity - 1 WHERE quantity > 0 and productID = (?);";
                
                // Prepearing the statements
                preparedStatement1 = dbConnection.prepareStatement(sql1);
                preparedStatement1.setInt(1, transNo);
                preparedStatement1.setInt(2, transID);
                preparedStatement1.setInt(3, cust.getCustomerID());
                preparedStatement1.setString(4, strDate);
                preparedStatement1.setInt(5, prodID);
                preparedStatement1.setInt(6, quantity);

                preparedStatement2 = dbConnection.prepareStatement(sql2);
                preparedStatement2.setInt(1, prodID);
                // preparedStatement2.setInt(2, quantity);
                
                preparedStatement3 = dbConnection.prepareStatement(sql3);
                // preparedStatement3.setInt(1, quantity);
                preparedStatement3.setInt(1, prodID);

                preparedStatement1.executeUpdate();
                dbConnection.commit();


			    for(int i=0; i<quantity;i++)    //check whether the quantity required is available 
			    {
                    // executing the statements
                    preparedStatement2.executeUpdate();
                    dbConnection.commit();
                    preparedStatement3.executeUpdate();
                    dbConnection.commit();

			    }
                System.out.println("Press enter to continue.\n");
                inputclass.in.nextLine();
                inputclass.in.nextLine();

                System.out.println("Item successfully bought!\n");

		        }
                
            }
            else
            {
                System.out.println("Item not in stock. Please try again later!");
            }
        }
        catch (SQLException ex)
        {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
        catch(InputMismatchException i)
        {
            System.out.println("Invalid input.");
        }

        try
        {
            if (preparedStatement1 != null)
            {
                preparedStatement1.close();
            }
            if (preparedStatement2 != null)
            {
                preparedStatement2.close();
            }
            if (preparedStatement3 != null)
            {
                preparedStatement3.close();
            }
            if (stmt != null)
            {
                stmt.close();
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    
		

	}

	
// function to generate a new customer ID
public Integer getNewCustomerID(String name)
{
    Integer custID = 0;
    String sql,check, sql2;
    Statement stmt = null;

    try
    {
        stmt = dbConnection.createStatement();
        sql = "select max(customerID) from Customer ";
        check = "select count(*) from Customer";
		ResultSet rs = stmt.executeQuery(check);
        rs.next();
        int count = rs.getInt(1);
        if(count == 0)
        {
            custID = 1;
        }
        else
        {
            rs = stmt.executeQuery(sql);
            rs.next();
            int max = rs.getInt(1);
            custID = max+1;
        }

    Customer cust = new Customer(custID, name);     // creating a customer object to add to the database
    //Inserting the customer to the database

    PreparedStatement preparedStatement = null;
    
    sql2="insert into Customer values (?, ?);";
    
    try
    {
        
        preparedStatement = dbConnection.prepareStatement(sql2);
        
        preparedStatement.setInt(1, cust.getCustomerID());
		preparedStatement.setString(2, cust.getCustomerName());
        // execute insert SQL statement
        int result = preparedStatement.executeUpdate();
        dbConnection.commit();
        if(result>0)
        {
            System.out.println("Successfully registered!");
        }
        else
        {
            System.out.println("Couldn't register :(");
        }
         

        
    }
    catch (SQLException e)
    {
        System.out.println(e.getMessage());
    }

    try
    {
        if (preparedStatement != null)
        {
            preparedStatement.close();
        }
    }
    catch (SQLException e)
    {
        System.out.println(e.getMessage());
    }

        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    

    return custID;
}


public void generateReceipt(Customer cust,Integer transID)
{
    String sql = null;
    PreparedStatement stmt = null; 
    Integer total = 0;

    try
    {
        sql = "select t.dateOfPurchase, t.productID, t.quantityBought, i.price * t.quantityBought as totalCost from Transaction as t join Inventory as i on i.productID = t.productID where t.transactionID = ?";
        stmt = dbConnection.prepareStatement(sql);
        stmt.setInt(1,transID);
        ResultSet rs = stmt.executeQuery();
        dbConnection.commit();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        System.out.println("");
        System.out.println("Receipt:\n");
        System.out.println("Transaction ID: "+ transID.toString());
        System.out.println("Customer ID: "+cust.getCustomerID().toString());
        System.out.println("CustomerName: "+cust.getCustomerName());
        
        if(rs.next())   //to get the date from the first line, and print the rest of the attributes 
        {
            String date = rs.getString("dateOfPurchase");
            System.out.println("Date:" +date);
            System.out.println("Product"+"\t\t"+"Quantity"+"\t"+"Price");
            for(int i=2;i<=columnsNumber;i++)
            {
                if (i > 2)
                {
                    System.out.print("\t\t");
                }
                String columnValue = rs.getString(i);
                if(i == 2)
                {
                    System.out.print(getProductName(Integer.parseInt(columnValue)));
                    continue;
                }
                else if(i == 4)
                {
                    total = total + Integer.parseInt(columnValue);
                }
                System.out.print(columnValue);

            }
        }
        else
        {
            System.out.println("");
            System.out.println("No transactions to show.");
            System.out.println("");
        }

        System.out.println("");
    
        while (rs.next())
        {
            for (int i = 2; i <= columnsNumber; i++)
            {
                if (i > 2)
                {
                    System.out.print("\t\t");
                }
                String columnValue = rs.getString(i);
                if(i == 2)
                {
                    System.out.print(getProductName(Integer.parseInt(columnValue)));
                    continue;
                }
                else if(i == 4)
                {
                    total = total + Integer.parseInt(columnValue);
                }
                System.out.print(columnValue);
            }
            System.out.println("");
        }
        
        System.out.println("Total Cost = "+total+"/-\n");
        System.out.println("Thanks for shopping with us!\n");
        System.out.println("");
    }
    catch (SQLException ex)
    {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

    try
	{
        if (stmt != null)
        {
			stmt.close();
		}
	}
    catch (SQLException e)
    {
 		System.out.println(e.getMessage());
 	}
    
}




public Integer generateTransactionID()
{
    int transID = 0;
    String sql,check;
    Statement stmt = null;
    
    try
    {
        stmt = dbConnection.createStatement();
        sql = "select max(transactionID) from Transaction ";
        check = "select count(*) from Transaction";    //query used to check whether the transaction table is empty or not
		ResultSet rs = stmt.executeQuery(check);
        rs.next();
        int count = rs.getInt(1);
        if(count == 0)   //this means that the table is empty
        {
            transID = 100;   //first tansacion ID = 100
        }
        else
        {
            rs = stmt.executeQuery(sql);
            rs.next();
            int max = rs.getInt(1);
            transID = max+1;  //the max transaction ID is incrementded by 1 to get a new transaction ID
        }

        System.out.println("Press enter to continue.");
        
        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    return transID;
}

public Integer getNextTransNo()
{
    int transNo = 0;
    String sql,check;
    Statement stmt = null;
    
    try
    {
        stmt = dbConnection.createStatement();
        sql = "select max(transactionNo) from Transaction ";
        check = "select count(*) from Transaction";    //query used to check whether the transaction table is empty or not
		ResultSet rs = stmt.executeQuery(check);
        rs.next();
        int count = rs.getInt(1);
        if(count == 0)   //this means that the table is empty
        {
            transNo = 1;   //first tansacionNo = 1
        }
        else
        {
            rs = stmt.executeQuery(sql);
            rs.next();
            int max = rs.getInt(1);
            transNo = max+1;  //the max transactionNo till now is incremented by 1 to get a next transactionNo
        }
        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    return transNo;
}




public Customer getCustomer(Integer custID)
{
	String sql;
    Statement stmt = null;
    Customer c = new Customer(custID,null);
    

    try{
		stmt = dbConnection.createStatement();
		sql = "select * from Customer where customerID = " + custID.toString();
		ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next())
        {
            String custName = rs.getString(2);
            c.setCustomerID(custID);
            c.setCustomerName(custName);
        } 
        

	} catch (SQLException ex) {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	// Add exception handling when there is no matching record
    return c;
    
}

public String getProductName(Integer prodID)
{
    String sql = null;
    Statement stmt = null;

    try
    {
        stmt = dbConnection.createStatement();
        sql = "select productName from Inventory where productID = "+prodID.toString();
        ResultSet rs = stmt.executeQuery(sql);

        if(rs.next())
        {
            return rs.getString("productName");
        }

    }
    catch (SQLException ex) {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
    return null;
}

    
}


// class inputclass
// {
//     static Scanner in = new Scanner(System.in);
// }
