package egringotts;

import java.util.*;
import java.sql.*;

/**
 *
 * @author USER
 */
public class DBConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/egringotts";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection openConn() throws SQLException {
        return DriverManager.getConnection(URL, "root", "Xi@ojun159357");
    }

    public static void closeConn(Connection connection) {
        try {
            connection.close();
        }
        catch( Exception e ) {
           System.err.println ("Can't close the database connection.");
        }
    }

     public static Vector getNextRow(ResultSet rs,ResultSetMetaData rsmd) throws SQLException
	 {
	     Vector currentRow = new Vector();

	     for(int i=1;i<=rsmd.getColumnCount();i++)
	         switch(rsmd.getColumnType(i))
	         {
	              case Types.VARCHAR:
	              case Types.LONGVARCHAR:
	                   currentRow.addElement(rs.getString(i));
	                   break;
	              case Types.INTEGER:
	                   currentRow.addElement(rs.getLong(i));
	                   break;
	              case Types.DOUBLE:
	              	   currentRow.addElement(rs.getDouble(i));
	                   break;
	              case Types.FLOAT:
	              	   currentRow.addElement(rs.getFloat(i));
	                   break;
	              default:
	                   System.out.println("Type was: "+ rsmd.getColumnTypeName(i));
	          }

	          return currentRow;
    }
     
     
}

