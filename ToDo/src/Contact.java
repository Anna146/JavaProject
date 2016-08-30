import java.sql.*;
import java.util.Locale;


public class Contact {
	
	int ID;
	long tel;
	String mail;
	
	public Contact(int id, long Tel, String Mail) {
		ID = id;
		tel = Tel;
		mail = Mail;
	}
}
