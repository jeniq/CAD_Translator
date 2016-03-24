package relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.Form;

public class RelationGrid {
	private static List<String> terminal = new ArrayList<>();
	private static List<String> nonTerminal = new ArrayList<>();
	private static Map<String, String> lexemeMap = new HashMap<>();
	private static List<String> headers = new ArrayList<>();
	private static List<String> lexemesList = new ArrayList<>();
	
	private static String[][] equalsGrid;
	private static String[][] firstPlusGrid;
	private static String[][] grammarGrid;
	private static String[][] highPriorityGrid;
	private static String[][] lastPlusGrid;
	private static String[][] relationGrid;
	private static String[] parsedGram;
	
	public static void start(){
		lexemeMap.put("1", "var"); 	
		lexemeMap.put("2","{"); 	
		lexemeMap.put("3","}"); 	
		lexemeMap.put("4","float"); 
		lexemeMap.put("5","int"); 	
		lexemeMap.put("6","write"); 
		lexemeMap.put("7","read"); 	
		lexemeMap.put("8","for"); 	
		lexemeMap.put("9","if"); 		
		lexemeMap.put("10","="); 		
		lexemeMap.put("11","<="); 		
		lexemeMap.put("12",">="); 		
		lexemeMap.put("13","!="); 		
		lexemeMap.put("14","<"); 		
		lexemeMap.put("15",">"); 		
		lexemeMap.put("16","=="); 		
		lexemeMap.put("17","+"); 		
		lexemeMap.put("18","-"); 		
		lexemeMap.put("19","*"); 		
		lexemeMap.put("20","/"); 	
		lexemeMap.put("21","("); 	
		lexemeMap.put("22",")"); 	
		lexemeMap.put("23",":"); 	
		lexemeMap.put("24",";"); 	
		lexemeMap.put("25",","); 	
		lexemeMap.put("26","?"); 	
		lexemeMap.put("27","id");	
		lexemeMap.put("28","con");	
		lexemeMap.put("29","and"); 	
		lexemeMap.put("30","or"); 	
		lexemeMap.put("31","not");
		lexemeMap.put("32","[");
		lexemeMap.put("33","]");
		
		lexemesList.add("var"); 	// 1
		lexemesList.add("{"); 		// 2
		lexemesList.add("}"); 		// 3
		lexemesList.add("float"); 	// 4
		lexemesList.add("int"); 	// 5
		lexemesList.add("write"); 	// 6
		lexemesList.add("read"); 	// 7
		lexemesList.add("for"); 	// 8
		lexemesList.add("if"); 		// 9
		lexemesList.add("="); 		// 10
		lexemesList.add("<="); 		// 11
		lexemesList.add(">="); 		// 12
		lexemesList.add("!="); 		// 13
		lexemesList.add("<"); 		// 14
		lexemesList.add(">"); 		// 15
		lexemesList.add("=="); 		// 16
		lexemesList.add("+"); 		// 17
		lexemesList.add("-"); 		// 18
		lexemesList.add("*"); 		// 19
		lexemesList.add("/"); 		// 20
		lexemesList.add("("); 		// 21
		lexemesList.add(")"); 		// 22
		lexemesList.add(":"); 		// 23
		lexemesList.add(";"); 		// 24
		lexemesList.add(","); 		// 25
		lexemesList.add("?"); 		// 26
		lexemesList.add("id");		// 27
		lexemesList.add("con");		// 28
		lexemesList.add("and"); 	// 29
		lexemesList.add("or"); 		// 30
		lexemesList.add("not"); 	// 31
		lexemesList.add("["); 		// 32
		lexemesList.add("]"); 		// 33
		
		terminal.add("+");
		terminal.add("-");
		terminal.add("*");
		terminal.add("/");
		terminal.add("(");
		terminal.add(")");
		terminal.add("id");
		
		nonTerminal.add("E1");
		nonTerminal.add("E");
		nonTerminal.add("T1");
		nonTerminal.add("T");
		nonTerminal.add("-T1");
		nonTerminal.add("F");
		
		headers = header(splitGram(getParsedGram()));
		grammarGrid = splitGram(getParsedGram()); 
		equalsGrid = equals(grammarGrid, headers); 
		firstPlusGrid = firstPlus(grammarGrid, headers, lexemeMap);
		highPriorityGrid = highPriority(equalsGrid, firstPlusGrid, headers);
		lastPlusGrid = lastPlus(grammarGrid, headers, lexemeMap);
		relationGrid = relations(equalsGrid, firstPlusGrid, lastPlusGrid, highPriorityGrid, headers);
	}
	
	public static void toTable(){
		Form.relationTable(relationGrid, headers);
	}
	
	private static List<String> header(String[][] grammar){
		List<String> headers = new ArrayList<>();
		for (int i = 0; i < grammar.length; i++){
			for (int j = 0; j < grammar[i].length; j++){
				if (!headers.contains(grammar[i][j]) && !grammar[i][j].equals("|")){
					headers.add(grammar[i][j]);
				}
			}
		}
		return headers;
	}
	
	// EQUALS TABLE
	private static String[][] equals(String[][] grammar, List<String> headers){ // first stage
		String[][] table = new String[headers.size()][headers.size()];
		
		for (int i = 0; i < headers.size(); i++){
			for (int j = 0; j < headers.size(); j++){
				table[i][j] = "0";
			}
		}
		for (int row = 0; row < grammar.length; row++){ 
			for (int column = 2; column < grammar[row].length; column++){
					if (!grammar[row][column-1].equals("|") && !grammar[row][column].equals("|")){
						table[headers.indexOf(grammar[row][column-1])][headers.indexOf(grammar[row][column])] = "2";
					}
				}
			}
		return table;
	}
	
	// FIRST PLUS 
	private static String[][] firstPlus(String[][] grammar, List<String> headers, Map<String, String> lexemeList){ // second stage
		String[][] table = new String[headers.size()][headers.size()];
		
		for (int i = 0; i < headers.size(); i++){
			for (int j = 0; j < headers.size(); j++){
				table[i][j] = "0";
			}
		}
		for (int i = 0; i < headers.size(); i++){
			if (!terminal.contains(headers.get(i))){
				firstPlus(headers.get(i), headers.get(i), table);
			}
		}
		return table;
	}
	
	// relation <
	private static String[][] highPriority(String[][] equalsGrid, String[][] firstPlusGrid, List<String> headers){ // third stage
		String[][] table = new String[headers.size()][headers.size()];
		
		for (int i = 0; i < headers.size(); i++){
			for (int j = 0; j < headers.size(); j++){
				table[i][j] = "0";
			}
		}
		for (int row = 0; row < equalsGrid.length; row++){
			for (int column = 0; column < equalsGrid[row].length; column++){
				if (equalsGrid[row][column].equals("2") || equalsGrid[row][column].equals("=")){
					table[row][column] = "=";   
					if (terminal.contains(headers.get(column))){ // terminals don't have F+ elements
						continue;
					}
					for (int headElem = 0; headElem < headers.size(); headElem++){ // search high priority
						if (firstPlusGrid[column][headElem].equals("F")){
							if (table[row][headElem].equals("2") || table[row][headElem].equals("=")){ 
								table[row][headElem] = "12";
							}else{
								table[row][headElem] = "<";
							}
						}
					}
				}
			}
		}
		return table;
	}
	
	// LAST PLUS
	private static String[][] lastPlus(String[][] grammar, List<String> headers, Map<String, String> lexemeList){ // fourth stage
		String[][] table = new String[headers.size()][headers.size()];
		
		for (int i = 0; i < headers.size(); i++){
			for (int j = 0; j < headers.size(); j++){
				table[i][j] = "0";
			}
		}
		for (int i = 0; i < headers.size(); i++){
			if (!terminal.contains(headers.get(i))){
				lastPlus(headers.get(i), headers.get(i), table);
			}
		}
		return table;
	}
	
	// RELATIONS >   // last stage
	private static String[][] relations(String[][] equalsGrid, String[][] firstPlusGrid, String[][] lastPlusGrid, String[][] highPriority, List<String> headers){ 
		
		String[][] table = new String[headers.size()][headers.size()];
		
		for (int i = 0; i < headers.size(); i++){
			for (int j = 0; j < headers.size(); j++){
				table[i][j] = "0";
			}
		}
		
		for (int row = 0; row < headers.size(); row++){
			for (int column = 0; column < headers.size(); column++){
				if (!equalsGrid[row][column].equals("0")){ // just set data from equals grid // no necessity in this part
					table[row][column] = equalsGrid[row][column];
				} // end of not necessity part
				if (!highPriority[row][column].equals("0")){ // just set data from 
					table[row][column] = highPriority[row][column];
				}
				if (equalsGrid[row][column].equals("2")){ // make '>' relations
					if (lexemesList.contains(headers.get(column).toString())){ // for terminals
						for (int head = 0; head < headers.size(); head++){
							if (lastPlusGrid[row][head].equals("L")){ 
								if (table[head][column].equals("0")){
									table[head][column] = "3";
								}else if (table[head][column].equals("2") || table[head][column].equals("=")){ // if cell containts '='
									table[head][column] = "23";
								}else if (table[head][column].equals("<")){ // if cell containts '<'
									table[head][column] = "13";
								}
							}
						}
					}else{
						for (int head = 0; head < headers.size(); head++){ // for non-terminals
							if (lastPlusGrid[row][head].equals("L")){ // last*
								for (int first = 0; first < headers.size(); first++){ // first*
									if (firstPlusGrid[head][first].equals("F")){
										if (table[head][first].equals("0")){
											table[head][column] = "3";
										}else if (table[head][first].equals("=") || table[head][first].equals("2")){ // if cell containts '='
											table[head][column] = "23";
										}else if (table[head][first].equals("<") || table[head][first].equals("1")){ // if cell containts '<'
											table[head][column] = "13";
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return table;
	}
	
	private static String[][] splitGram(String[] gram){
		String[][] splitStr = new String[gram.length][];
		for (int i = 0; i < gram.length; i++){
			splitStr[i] = gram[i].trim().split(" ");
		}
		return splitStr;
	}
	
	
	public static List<String> getHeaders(){
		return headers;
	}
	
	public static String[][] getRelations(){
		return relationGrid;
	}

	public static String[] getParsedGram() {
		return parsedGram;
	}

	public static void setParsedGram(String[] parsedGram) {
		RelationGrid.parsedGram = parsedGram;
	}
	
	// FIRST PLUS search
	private static void firstPlus(String startElem, String currElem, String[][] table){
		int horiz_start;
		int horiz_curr;
		int vertic;

		for (int rule = 0; rule < grammarGrid.length; rule++){
			if (grammarGrid[rule][0].equals(currElem)){
				horiz_start = headers.indexOf(startElem);
				horiz_curr = headers.indexOf(currElem);
				vertic = headers.indexOf(grammarGrid[rule][1]);
				table[horiz_start][vertic] = "F"; // relation between first element
				table[horiz_curr][vertic] = "F"; // relation between first element
				if (!terminal.contains(grammarGrid[rule][1])){
					// reason of endless loop could be same element with rule name on first place
					if (!currElem.equals(grammarGrid[rule][1])){
						firstPlus(startElem, grammarGrid[rule][1], table);
					}
				}
				// check alternative variants
				for (int altern = 1; altern < grammarGrid[rule].length; altern++){
					if (grammarGrid[rule][altern].equals("|")){
						vertic = headers.indexOf(grammarGrid[rule][altern+1]);
						table[horiz_start][vertic] = "F";
						table[horiz_curr][vertic] = "F";
						if (currElem.equals(grammarGrid[rule][altern+1]) || (terminal.contains(grammarGrid[rule][altern+1]))){
							continue;
						}else{
							firstPlus(startElem, grammarGrid[rule][altern+1], table);
						}
					}
				}
			}
		}
	}
	
	// LAST PLUS search
	private static void lastPlus(String startElem, String currElem, String[][] table){
		int horiz_start;
		int horiz_curr;
		int vertic;
		
		for (int rule = 0; rule < grammarGrid.length; rule++){
			if (grammarGrid[rule][0].equals(currElem)){
				horiz_start = headers.indexOf(startElem);
				horiz_curr = headers.indexOf(currElem);
				vertic = headers.indexOf(grammarGrid[rule][grammarGrid[rule].length-1]);
				table[horiz_start][vertic] = "L"; // relation between first element
				table[horiz_curr][vertic] = "L"; // relation between first element
				if (!terminal.contains(grammarGrid[rule][grammarGrid[rule].length-1])){
					// reason of endless loop could be same element with rule name on first place
					if (!currElem.equals(grammarGrid[rule][grammarGrid[rule].length-1])){
						lastPlus(startElem, grammarGrid[rule][grammarGrid[rule].length-1], table);
					}
				}
				// check alternative variants
				for (int altern = 1; altern < grammarGrid[rule].length; altern++){
					if (grammarGrid[rule][altern].equals("|")){
						vertic = headers.indexOf(grammarGrid[rule][altern-1]);
						table[horiz_start][vertic] = "L";
						table[horiz_curr][vertic] = "L";
						if (currElem.equals(grammarGrid[rule][altern-1]) || (terminal.contains(grammarGrid[rule][altern-1]))){
							continue;
						}else{
							lastPlus(startElem, grammarGrid[rule][altern-1], table);
						}
					}
				}
			}
		}
	}

}
