import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parcer {


	private List<String> emails = new ArrayList<String>();
	
	private String page;
	
	public void connect(String adress) throws Exception {

		 URL url = new URL(adress);
		 URLConnection yc = url.openConnection();
		 String result= "";
		 
		 try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
		     String inputLine;
		     
		    while ((inputLine = buffReader.readLine()) != null) {
		         result += inputLine + System.lineSeparator();
		    }
		}
		page = result;

	}
	
	private void getEmail() {
		
		Pattern pat = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})");
		Matcher mat = pat.matcher(page);
		
		while (mat.find()) {
			emails.add(mat.group());
		}
		
	}
	
	
	public static void main(String[] args) {
		Parcer par = new Parcer();
		
		try {
			par.connect(args[0]);
			par.getEmail();
			
			for (int i=0; i<par.emails.size(); i++) {
				System.out.println(par.emails.get(i));
			}
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
