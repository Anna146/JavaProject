import java.util.Calendar;


public class Task {
	
	public int ID;
	public String Name;
	public String Description;
	public Calendar day;
	public Calendar time;
	public int urgency;
	public int smail;
	public int ssms;
	
	public Task() {
	}
	
	public void setDay(String s) {
		int y = Integer.parseInt(s.substring(0, 4));
		int m = Integer.parseInt(s.substring(5, 7));
		int d = Integer.parseInt(s.substring(8, 10));
		day = Calendar.getInstance();
		day.set(y, m, d);
	}
	
	public void setTime(String s) {
		time = Calendar.getInstance();
		time.set(time.get(Calendar.YEAR),time.get(Calendar.MONTH),time.get(Calendar.DATE),Integer.parseInt(s.substring(0, 2)), Integer.parseInt(s.substring(3, 5)));
	}
	
	public void setID(int id) {
		ID = id;
	}
	
	public void setSmail(int id) {
		smail= id;
	}
	
	public void setSsms(int id) {
		ssms = id;
	}
}
