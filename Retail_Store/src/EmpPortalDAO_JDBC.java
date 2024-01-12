import java.sql.*;

public class EmpPortalDAO_JDBC implements EmployeePortalDAO
{
    Connection dbConnection;

    public EmpPortalDAO_JDBC(Connection dbconn)
	{
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

    public void checkEmployeeDetails()
    {
        String sql;
            Statement stmt = null;

            try{
    			stmt = dbConnection.createStatement();
    			sql = "select * from Employee";
    			ResultSet rs = stmt.executeQuery(sql);

                // This format has to be changed
    			ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("EmpID"+"\t\t"+"Name"+"\t\t\t"+"JoinDate"+"\t\t"+"isOwner");
                while (rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        if (i > 1)
                        {
                            System.out.print("\t\t");
                        }
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println("");
                }
                System.out.println("");

    		} catch (SQLException ex) {
    		    // handle any errors
    		    System.out.println("SQLException: " + ex.getMessage());
    		    System.out.println("SQLState: " + ex.getSQLState());
    		    System.out.println("VendorError: " + ex.getErrorCode());
    		}
    		// Add exception handling when there is no matching record

    }

    public String maxSoldProduct(java.sql.Date beg,java.sql.Date end)
    {
        String sql = null;
        PreparedStatement stmt = null;

        try
        {
            sql = "select productID,sum(quantityBought) as total from Transaction as t where t.dateOfPurchase between ? and ? group by productID order by total DESC limit 1";
            stmt = dbConnection.prepareStatement(sql);
            stmt.setDate(1,beg);
            stmt.setDate(2,end);
            ResultSet rs = stmt.executeQuery();
            dbConnection.commit();

            if(rs.next())
            {
                Integer prodID = rs.getInt("productID");
                return "Max sold product:\nProduct ID - "+prodID+"\nProduct Name - "+getProductName(prodID)+"\nquantity sold = "+rs.getInt("total");
            }

        }
        catch (SQLException ex) {
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

            return "No product bought in this period.\n";
        }

    // check if the product exists or not and generate a product id and stuff if necessary 
    public void addProduct(String name,Integer price,Integer quantity)
    {
        Integer uniqID;
        Integer prodID;
        String sql = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try
        {
            for(int i = 0;i<quantity;i++)
            {
                uniqID = getUniqueID();
                prodID = getProdID(name,price);
                
                dbConnection.commit();

                if(isProduct(name,price))    //if the product was already in the inventory then the row only has to be updated
                {
                    sql = "update Inventory set quantity = quantity + 1 where productName = ? and price = ?";
                    stmt2 = dbConnection.prepareStatement(sql);
                    stmt2.setString(1,name);
                    stmt2.setInt(2,price);
                }
                else     //if not, then the entry has to be newly insertd in the inventory
                {
                    sql = "insert into Inventory (productID,productName,price,quantity) values(?,?,?,?)";
                    stmt2 = dbConnection.prepareStatement(sql);
                    stmt2.setInt(1,prodID);
                    stmt2.setString(2,name);
                    stmt2.setInt(3,price);
                    stmt2.setInt(4,1);
                }

                stmt2.executeUpdate();
                dbConnection.commit();

                sql = "insert into Product (uniqueID,productID) values(?,?)";     //insert made in Product after inventory because of the foreign key relationship
                stmt1 = dbConnection.prepareStatement(sql);
                stmt1.setInt(1,uniqID);
                stmt1.setInt(2,prodID);
                stmt1.executeUpdate();
                dbConnection.commit();
            }

            System.out.println("\nProducts successfully added!\n");

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
            if (stmt1 != null)
            {
                stmt1.close();
            }
            if (stmt2 != null)
            {
                stmt2.close();
            }


        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
   

    }

    public Integer getUniqueID()
    {
        Integer uniqID = 1;
        String sql = null;
        PreparedStatement stmt = null;
        try
        {
            sql = "select max(uniqueID) from Product";
            stmt = dbConnection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                uniqID = rs.getInt(1) + 1;
            }
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

        return uniqID;
    }

    public Integer getProdID(String name,Integer price)
    {
        Integer prodID = 1;
        String sql = null;
        PreparedStatement stmt = null;
        try
        {
            sql = "select productID from Inventory where productName = ? and price = ?";
            stmt = dbConnection.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setInt(2,price);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                prodID = rs.getInt(1);
            }
            else
            {
                sql = "select max(productID) from Inventory";
                rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    prodID = rs.getInt(1) + 1;
                }

            }
        }
        catch (SQLException ex)
        {
            // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}

        return prodID;
    }

    public Boolean isProduct(String name,Integer price)     //helper function for addProduct, to find out whether a given product is currently in the inventory
    {
        String sql = null;
        PreparedStatement stmt = null;
        try
        {
            sql = "select price from Inventory where productName = ? and price = ?";
            stmt = dbConnection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2,price);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                return true;
            }    
            return false;      
        }
        catch (SQLException ex)
        {
            // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}

        return true;
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

    public Employee getEmployee(Integer empID)
    {
	String sql;
    Statement stmt = null;
    Employee e = new Employee(empID, null);

    try{
		stmt = dbConnection.createStatement();
		sql = "select * from Employee where employeeID = " + empID;
		ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next())
        {
            String empName = rs.getString("employeeName");
            e.setEmployeeName(empName);
            break;
        }
        

	}
    catch (SQLException ex)
    {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}

    return e;
    
    }


    public Boolean isOwner(Employee emp)
    {
        String sql = null;
        PreparedStatement stmt = null;

        try
        { 
            sql = "select employeeID from Employee where isOwner = 1";
            stmt = dbConnection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                if(rs.getInt(1) == emp.getEmployeeID())
                {
                    return true;
                }
            }

        }
        catch (SQLException ex)
        {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	    }

        return false;
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
