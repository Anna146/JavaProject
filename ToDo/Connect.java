
import java.sql.*;
import java.util.Locale;

/**
 *
 * @author Sunny
 */
public class Connect {
    static Connection con;
    static Statement stmt;
    static ResultSet rs;
    public static void main(String[] args) {

        //step 1: load driver
        loadDriver();

        //step 3: establish connection
        makeConnection();

        //create a table
        //createTable();

        //insert data
        //insertData();

        //use precompiled statement to update data
        //usePreparedStatement();

        //retrieve data
        retrieveData();

        //close all resources
        closeAll();
    }

    // load a driver
    static void loadDriver() {
        try {
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch(java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
    }

    // make a connection  step 3: establish connection
    static void makeConnection() {
    	Locale.setDefault(Locale.ENGLISH);
        //for one of Oracle drivers
      //step 2: Define connection URL
      String host = "127.0.0.1";
      String dbName = "xe";
      int port = 1521;
      String oracleURL = "jdbc:oracle:thin:@" + host +
      ":" + port + ":" + dbName;
        //for how to set up data source name see below.
        try {
           con = DriverManager.getConnection(oracleURL, "Anna146", "1");
        }catch(SQLException ex) {
           System.err.println("database connection: " + ex.getMessage());
        }
    }

    //create a table
    static void createTable() {
        String createString = "create table COFFEES \n" +
                          "(NAME VARCHAR2(200), \n" +
                          "SUP_ID NUMBER, \n" +
                          "PRICE NUMBER, \n" +
                          "SALES NUMBER, \n" +
                          "TOTAL NUMBER)\n";



        try {
            //step 4: create a statement
            stmt = con.createStatement();
            //step 5: execute a query or update.
            stmt.execute("drop table COFFEES");//if exists, drop it, get new one
            stmt.executeUpdate(createString);

        }catch(SQLException ex) {
            System.err.println("CreateTable: " + ex.getMessage());
        }
     }

     //insert data to table COFFEES
     static void insertData() {
        try {
        	stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO Contact " +  "VALUES (1, 89151617729, 'belpku001@mail.ru')");

        }catch(SQLException ex) {
            System.err.println("InsertData: " + ex.getMessage());
        }
     }

    //use PreparedStatement to precompile sql statement
    static void usePreparedStatement() {
        try {
            PreparedStatement updateSales;
            String updateString = "update COFFEES " +
                      "set SALES = ? where COF_NAME like ?";
            updateSales = con.prepareStatement(updateString);
            int [] salesForWeek = {175, 150, 60, 155, 90};
            String [] coffees = {"Colombian", "French_Roast", "Espresso",
                     "Colombian_Decaf", "French_Roast_Decaf"};
            int len = coffees.length;
            for(int i = 0; i < len; i++) {
                updateSales.setInt(1, salesForWeek[i]);
                updateSales.setString(2, coffees[i]);
                updateSales.executeUpdate();
            }
        }catch(SQLException ex) {
            System.err.println("UsePreparedStatement: " + ex.getMessage());
       }
    }

    //retrieve data from table COFFEES
    static void retrieveData() {
       try {
           String gdta="SELECT * FROM CONTACT\n";
           String test = "select * from CONTACT\n";
           //step 6: process the results.
           stmt = con.createStatement();
           rs = stmt.executeQuery(gdta);
           while (rs.next()) {
               	int m = rs.getInt("ContactID");
                long n = rs.getLong("Telephone");
                String s = rs.getString("Mail");
                System.out.println(m + "   " + n + "   " + s);
           }
       }catch(SQLException ex) {
           System.err.println("RetrieveData: " + ex.getMessage());
       }
     }

    //close statement and connection
    //step 7: close connection, etc. 
     static void closeAll() {
         try {
            stmt.close();
            con.close();
         } catch(SQLException ex) {
            System.err.println("closeAll: " + ex.getMessage());
         }
    }
}


