package source;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LexemeAnalyzer {
	// list of lexemes
	private static ArrayList<String> lexemesList = new ArrayList<String>();
	// list of white separators
	private static ArrayList<String> whiteSeparator = new ArrayList<String>();
	// list of separators 
	private static ArrayList<String> separator = new ArrayList<String>();
	// list of signs
	private static ArrayList<String> sign = new ArrayList<String>();
	
	private static ArrayList<String> CON = new ArrayList<String>(); // list of constants
	private static ArrayList<String> IDN = new ArrayList<String>(); // list of identifiers
	
	private static Map<String, String> idnValue = new HashMap<String, String>();
	
	private static int tableLines = 0; // rows for lexeme's table
	
	public static String[][] Do(){
		sign.add("-");
		
		// get the input file size
		File file = new File("D:\\SAPR\\input.txt");
		long size = file.length();
		String[][] data = new String[(int)size][5]; // database of lexemes
		
		whiteSeparator.add(" ");
		whiteSeparator.add("\n");
		whiteSeparator.add("\r");
		whiteSeparator.add("\t");
		
		int line = 1;		// lemexe line
		int code;			// lexeme code
		
		boolean found_CON = false; 
		boolean found_IDN = false; 
		boolean dublicate = true; // check for id dublicate in declaration
		
		uploadData(lexemesList, "D:\\SAPR\\lexemes\\lexemeList.txt");
		uploadData(separator, "D:\\SAPR\\lexemes\\separator.txt");

		 try(FileReader reader = new FileReader("D:\\SAPR\\input.txt"))
	        {
	            int current;
                StringBuilder buffer = new StringBuilder();
                char currentChar;
                
	            while((current=reader.read())!=-1){
	                
	            	currentChar = (char)current;
	            	
	            	if (whiteSeparator.contains(Character.toString(currentChar)) && buffer.length() == 0){
	            		if (currentChar == '\n'){
	            			line++;
	            		}
	            		buffer = new StringBuilder();
	            		continue;
	            	}
	            	
	        		buffer.append(currentChar);
	        		
	        		if (separator.contains(Character.toString(currentChar)) || whiteSeparator.contains(Character.toString(currentChar))){ // если текущий элемент - разделитель и буфер не содержится в таблице лексем
	        			if (!buffer.toString().equals("-")){	
	        				buffer.deleteCharAt(buffer.length()-1); // удалить разделитель и проверить на IDN/CON
	        			}
	        			
	        			if (lexemesList.contains(buffer.toString())){
		        			for (String lexeme : lexemesList) { // pass through lexemes list
			                	if (buffer.toString().equals(lexeme) && !sign.contains(lexeme)) { // compare input string with current lexeme
			            			code = lexemesList.indexOf(lexeme) + 1;
			                		Lexeme(lexeme, line, code, 0, data, tableLines);
			                		tableLines++;
			                		if (lexeme.equals("{")){
			                			dublicate = false;
			                		}
			                		buffer = new StringBuilder();
			                		break;
			                	}
		        			}
		        		}else if (buffer.toString().length()>0){ // if in buffer separate only -> write and exit it, else compare for IND/CON
		        			if ((buffer.toString().charAt(0) >= 'a' && buffer.toString().charAt(0) <= 'z')) {
		              			if (!IDN.contains(buffer.toString())) {
		              				if (!dublicate){
		              					code = -2;
		              				}else{
		              					IDN.add(buffer.toString());
		              					code = 27;
		              				}
		                		}else if (dublicate){
		                			code = -1; 
		                		}else{
		                			code = 27;
		                		}
		              			int index = IDN.indexOf(buffer.toString()) + 1;
		              			String lex = new String(buffer.toString());
		              			Lexeme(lex, line, code, index, data, tableLines);
		              			found_IDN = true;
		              			++tableLines;
		              			buffer = new StringBuilder();
		                	}else if ((isNumber(buffer.toString()))) { // compare to number (constant)
		                		if (!CON.contains(buffer.toString())) {
		                			CON.add(buffer.toString());
		                		}
		                		int index = CON.indexOf(buffer.toString()) + 1;
		                		String lex = new String(buffer.toString());
		                		Lexeme(lex, line, 28, index, data, tableLines);
		                		found_CON =	true;
		                		++tableLines;
		              			// get idn value
		                		if (data[tableLines-1][1].equals("=")){
		                			idnValue.put(data[tableLines-2][1], lex);
		                		}
		              			//
			        			buffer = new StringBuilder();
		                	}else if (!(found_IDN) && !(found_CON)) { // if no one found then returns ERROR
		                		String lex = new String(buffer.toString());
		                		Lexeme(lex, line, 0, 0, data, tableLines);
		                		++tableLines;
			        			buffer = new StringBuilder();
		                	}
		        		}
	        			
	        			if (separator.contains(Character.toString(currentChar))){ // add sepatator to table
	        				if (currentChar == '='){
	                			if (data[tableLines-1][2].equals("10")){
	                				data[tableLines-1][2] = "16";
	                				data[tableLines-1][1] = "==";
	                			}else if (data[tableLines-1][1].equals("!")){
	                				data[tableLines-1][2] = "13";
	                				data[tableLines-1][1] = "!=";
	                			}else if (data[tableLines-1][2].equals("14")){ // <
	                				data[tableLines-1][2] = "11";
	                				data[tableLines-1][1] = "<=";
	                			}else if (data[tableLines-1][2].equals("15")){ // >
	                				data[tableLines-1][2] = "12";
	                				data[tableLines-1][1] = ">=";
	                			}else{
	                				code = lexemesList.indexOf(Character.toString(currentChar)) + 1;
		        					Lexeme(Character.toString(currentChar), line, code, 0, data, tableLines);
		        					++tableLines;	
	                			}
	        				}else{
	                			if (!buffer.toString().equals("-")){
		        					code = lexemesList.indexOf(Character.toString(currentChar)) + 1;
		        					Lexeme(Character.toString(currentChar), line, code, 0, data, tableLines);
		        					++tableLines;
		        				}	
	                		}
	        			}

	        			found_IDN = false;
	        			found_CON = false;
	        		}
	            }
	            reader.close();
            }
	        catch(IOException ex){
	            System.out.println(ex.getMessage());
	        }   
		 return data;
	}

	public static void Lexeme(String lex, int ln, int code, int ind, String[][] array, int tableLines) {
		
		array[tableLines][0] = Integer.toString(ln); 
		array[tableLines][1] = lex;
		if (code == 0){
			array[tableLines][2] = "ERROR";
		}else if (code == -1){
			array[tableLines][2] = "dublicate variable";
		}else if (code == -2){
			array[tableLines][2] = "undeclared variable";
		}else{ 
			array[tableLines][2] = Integer.toString(code);
		}
		if (ind == 0){
			array[tableLines][3] = " ";
		}else{
			array[tableLines][3] = Integer.toString(ind);
		}
	} 

	public static boolean isNumber(String string) { // method checks is number input lexeme 
		if (string == null || string.length() == 0) // if input is empty - break method
			return false;

		int i = 0;
		if (string.charAt(0) == '-') { // if length of input is 1 and first symbol is minus - no CONSTANT 
			if (string.length() == 1) {
				return false;
			}
			i = 1;
		}

		char c;
		for (; i < string.length(); i++) {
			c = string.charAt(i);
			if (!(c >= '0' && c <= '9' || c == '.' || i>1 && c == 'e')) {
				return false;
			}
		}
		return true;	
	}

	private static void uploadData(ArrayList<String> List, String path){
		String[] data = {"Empty"};
		try{
			data = ReadText.read(path).split("\n");
		}catch(IOException e){
			System.out.println(e);
		}
		
		for (int current = 0; current < data.length; current++){
			List.add(data[current]);
		}
		
	}
	public static int getLexemeCount(){
		return tableLines;
	}
	public static String getIdnValue(String idn){
		if (idnValue.containsValue(idn)){
			for (Map.Entry idnHash : idnValue.entrySet()){
				if (idnHash.equals(idn)){
					return idnHash.getKey().toString();
				}
			}
		}
		return ("Не ініціалізована змінна" + idn);
	}

}
