package relation;

public class ParseGram {
	public static String[] parse(String inputGram){
		int lines;
		int i = 0;
		
		lines = countLines(inputGram); // number of rules in grammar
		
		String[] rule = new String[lines];
		StringBuilder row = new StringBuilder();
		
		for (int current = 0; current < inputGram.length(); current++){  
			if (inputGram.charAt(current) == '\n'){
				String[] currRule = row.toString().trim().split("::=");
				rule[i] = ""; 
				for (String str : currRule){
					rule[i]+=str;
				}
				row = new StringBuilder();
				i++;
				continue;
			}else{
				row.append(inputGram.charAt(current));
			}		
		}
		return rule;		
	}
	
	private static int countLines(String inputGram){
		int newLine = 0;
		for (int current = 0; current < inputGram.length(); current++){
			if (inputGram.charAt(current) == '\n'){
				newLine++;
			}
		}
		return newLine;
	}
	
}
