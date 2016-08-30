
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Sunny
 */
public class Connect {
    static Connection con;
    static Statement stmt;
    static ResultSet rs;
    
    public Connect() {
    	loadDriver();
        makeConnection();
    }
    

    
    public static void loadDriver() {
        try {
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch(java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
    }

    
    public static void makeConnection() {
    	Locale.setDefault(Locale.ENGLISH);
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

    
    
     //insert data to table Contact
     public static void newContact(String[] vals) {
        try {
        	stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO Contact " +  "VALUES (" + vals[0] + "," + vals[1] + "," + vals[2]  +")");

        }catch(SQLException ex) {
            System.err.println("InsertDataError: " + ex.getMessage());
        }
     }

     
     
     
    //use PreparedStatement to precompiled sql statement
    public static void changeContact(String[] vals) {
        try {
            PreparedStatement updateCon;
            String updateString = "update Contact " +
                      "set Telephone = ?, Mail = ? where ContactID = ?";
            updateCon = con.prepareStatement(updateString);
            updateCon.setString(0, vals[1]);
            updateCon.setString(1, vals[2]);
            updateCon.setString(2, vals[0]);
            updateCon.executeUpdate();
        }catch(SQLException ex) {
            System.err.println("UsePreparedStatement: " + ex.getMessage());
       }
    }
    

    
    //retrieve data from table Contact
    public static Contact getContact(int id) {
       try {
           String gdta="SELECT * FROM CONTACT where ContactID = " + id + "\n";
           //String test = "select * from CONTACT\n";
           stmt = con.createStatement();
           rs = stmt.executeQuery(gdta);
           rs.next();
               	int m = rs.getInt("ContactID");
                long n = rs.getLong("Telephone");
                String s = rs.getString("Mail");
           return new Contact(m,n,s);
       }catch(SQLException ex) {
           System.err.println("RetrieveData: " + ex.getMessage());
       }
       return null;
     }

    
    public static void newTask(String[] vals) {
        try {
        	stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO Task " +  "VALUES (" + vals[0] + "," + vals[1] + "," + vals[2] + "," + vals[3]+ "," + vals[4] + "," + vals[5] + "," + vals[6] + "," + vals[7]  +")");

        }catch(SQLException ex) {
            System.err.println("InsertDataError: " + ex.getMessage());
        }
     }

     
     
     
    //use PreparedStatement to precompiled sql statement
   /* public static void changeTask(String[] vals) {
        try {
            PreparedStatement updateCon;
            String updateString = "update Contact " +
                      "set Telephone = ?, Mail = ? where ContactID = ?";
            updateCon = con.prepareStatement(updateString);
            updateCon.setString(0, vals[1]);
            updateCon.setString(1, vals[2]);
            updateCon.setString(2, vals[0]);
            updateCon.executeUpdate();
        }catch(SQLException ex) {
            System.err.println("UsePreparedStatement: " + ex.getMessage());
       }
    }*/
    

    
    //retrieve data from table Contact
    public static Task getTask(int id) {
       try {
           String gdta="SELECT * FROM Task where ID = " + id + "\n";
           //String test = "select * from CONTACT\n";
           stmt = con.createStatement();
           rs = stmt.executeQuery(gdta);
           Task ts = new Task();
           ts.ID = rs.getInt("ID");
           ts.Name = rs.getString("NAME");
           ts.Description = rs.getString("DESCRIPTION");
           ts.day.setTime(rs.getDate("DAY"));
           ts.time.setTime(rs.getDate("TIME"));
           ts.urgency = rs.getInt("URGENCY");
           ts.setSmail(rs.getInt("SENDEMAIL"));
           ts.setSsms(rs.getInt("SENDSMS"));
        	return ts;	   
       }catch(SQLException ex) {
           System.err.println("RetrieveData: " + ex.getMessage());
       }
       return null;
     }
    
    
    List<Task> daily = new ArrayList<Task>();
    
    public void dailyChecker() {
    	daily.clear();
    	Calendar today = Calendar.getInstance();
    	String day = today.get(Calendar.YEAR) + "." + today.get(Calendar.MONTH) + "." + today.get(Calendar.DATE);
    	String gdta="SELECT * FROM Task WHERE DAY = '" + day + "'\n";
        try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(gdta);
	        while (rs.next()) {
	        	Task ts = new Task();
	            ts.ID = rs.getInt("ID");
	            ts.Name = rs.getString("NAME");
	            ts.Description = rs.getString("DESCRIPTION");
	            ts.setDay(rs.getString("DAY"));
	            ts.setTime(rs.getString("TIME"));
	            ts.urgency = rs.getInt("URGENCY");
	            ts.setSmail(rs.getInt("SENDEMAIL"));
	            ts.setSsms(rs.getInt("SENDSMS"));
	            daily.add(ts);
		        System.out.println(ts.Name);
	        }
		} catch (SQLException e) {
			 System.err.println("RetrieveData: " + e.getMessage());
		}
	}
    
    
    
    public void hourChecker() {
    	for (Task t: daily) {
    		if (Calendar.getInstance().get(Calendar.HOUR) == t.time.get(Calendar.HOUR)) {
    			if (t.smail != -1) {
    				//sends email
    			}
    			if (t.ssms != -1) {
    				//sends sms
    			}
    			daily.remove(t);
    		}
    	}
    }
    
    
    
     public static void closeAll() {
         try {
            stmt.close();
            con.close();
         } catch(SQLException ex) {
            System.err.println("closeAll: " + ex.getMessage());
         }
    }
     
     
     public static void main(String[] args) throws InterruptedException {
 		Connect con = new Connect();
 		int currentHour = Calendar.getInstance().get(Calendar.HOUR);
 		Calendar currentTime = Calendar.getInstance();
 		Calendar targetTime = Calendar.getInstance();
 		int currentDate = Calendar.getInstance().get(Calendar.DATE);
 		targetTime.set(Calendar.HOUR, currentHour+1);
 		con.dailyChecker();
 		con.hourChecker();
 		Thread.sleep(targetTime.getTimeInMillis() - currentTime.getTimeInMillis());
 		while (true) {
	 		while (Calendar.getInstance().get(Calendar.DATE) == currentDate) {
	 			con.hourChecker();
	 			Thread.sleep(60*60000);
	 		}
	 		currentDate += 1;
	 		con.dailyChecker();
 		}
 	}
}


