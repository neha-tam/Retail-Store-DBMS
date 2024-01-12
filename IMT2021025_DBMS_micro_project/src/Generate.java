import java.sql.*;


public class Generate {
    Connection dbConnection;

    public Generate(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
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
        check = "select count(*) from Transaction";
		ResultSet rs = stmt.executeQuery(check);
        rs.next();
        int count = rs.getInt(1);
        if(count == 0)
        {
            transID = 100;
        }
        else
        {
            rs = stmt.executeQuery(sql);
            rs.next();
            int max = rs.getInt(1);
            transID = max+1;
        }

        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    return transID;
}

public Integer getNewCustomerID()
{
    Integer custID = 0;
    String sql,check;
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

        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    return custID;
}

    
}
