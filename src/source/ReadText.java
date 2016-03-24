package source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadText {
	public static String read(String fileName) throws FileNotFoundException {
	    StringBuilder sb = new StringBuilder();
	    
	    exists(fileName); // check file existing in directory
	    File file = new File(fileName);
	 
	    try {
	    	// object for file reading in buffer 
	        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
	        try {
	        	// row file reading
	            String s;
	            while ((s = in.readLine()) != null) {
	                sb.append(s);
	                sb.append("\n");
	            }
	        } finally {
	            in.close();
	        }
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	    return sb.toString();
	}
	
	private static void exists(String fileName) throws FileNotFoundException {
	    File file = new File(fileName);
	    if (!file.exists()){
	        throw new FileNotFoundException(file.getName());
	    }
	}
}
