//import org.alicebot.ab.*;
//import org.alicebot.ab.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;



public class file_handling {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = null;

        try {
            String file = ".\\input.txt";
            reader = new BufferedReader(new FileReader(new File(file)));
            
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
