package poliz;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import exceptions.LexemeNotFound;

public class Poliz {

	private String[][] lexemeList;
	private String[][] relations;
	private String[][] splitGram;
	private String[][] table;
	private List<String> stackList = new ArrayList<>(); // for table data
	private List<String> relationList = new ArrayList<>(); // for table data
	private List<String> chainList = new ArrayList<>(); // for table data
	private List<String> polizList = new ArrayList<>();
	private Stack<String> constant = new Stack<String>();
	private Stack<String> identifier = new Stack<String>(); 
	private List<String> ruleHeaders;
	private Stack<String> stack = new Stack<String>();
	private Stack<String> poliz = new Stack<String>();
	private int row;
	
	public Poliz(String[][] lexemeList, String[][] relations, List<String> ruleHeaders, String[] parsedGram){
		this.lexemeList =  lexemeList;
		this.relations = relations;
		this.ruleHeaders = ruleHeaders;
		splitGram = splitGram(parsedGram);
		stackList.add("#");
		relationList.add("<");
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < lexemeList.length; i++){ // fill input chain
			if (lexemeList[i][1]!=null){
				buffer.append(lexemeList[i][1]);
			}
		}
		chainList.add(buffer.toString());
		row = 1;
	}
	
	public String doPoliz(int pos) throws LexemeNotFound{
		String current;
		String currentCode;
		current = lexemeList[pos][1];
		currentCode = lexemeList[pos][2];
		String lastInStack = peek();
		
		if (current==null){
			currentCode = "null";
			current = pop();
			if (!stack.isEmpty()){
				lastInStack = pop();
				push(lastInStack);
			}
			push(current);
		}
		
		if (!stack.isEmpty()){
			stackList.add(stack.toString());
			if (current!=null){
				StringBuilder buffer = new StringBuilder();
				for (int i = pos; i < lexemeList.length; i++){
					if (lexemeList[i][1]!=null){
						buffer.append(lexemeList[i][1]);
					}
				}
				chainList.add(buffer.toString());
			}
		}
		if (currentCode.equals("27")){
	//		push(identifier, current);
			current = "id";
	//		push(poliz, pop(identifier));
		//	polizList.add(poliz.toString().replace(",", ""));
		}else if (currentCode.equals("28")){
	//		push(constant, current);
			current = "con";
	//		push(poliz, pop(constant));
	//		polizList.add(poliz.toString().replace(",", "").replace("[", "").replace("]", ""));
		}
		// clean info for poliz after end of row
		/*if (peek().equals("вираз1")){
			while (!identifier.isEmpty()){
				pop(identifier);
			}
			while (!constant.isEmpty()){
				pop(constant);
			}
			while (!poliz.isEmpty()){
				pop(poliz);
			}
		}*/
		if (lastInStack.isEmpty()){
			push(current);
			pos++;
			doPoliz(pos);
		}else if (peek().equals(ruleHeaders.get(0)) && stack.size() == 1){
			return peek(); // successfull parsing  
		}
		int relation;
		if (currentCode.equals("null") ){
			relation = 2;
		}else{
			relation = defineRelation(lastInStack, current);
		}
		
		if (relation == 0){
			relationList.add("<");
		}else if (relation == 1){
			relationList.add("=");
		}else if (relation == 2){
			relationList.add(">");
		}
		if ((relation == 0 || relation == 1) && !currentCode.equals("null")){
			push(current);
			pos++;
			doPoliz(pos);
		}else if (relation == 2 || currentCode.equals("null")){
			// если один элемент, то сделать редукцию
			// иначе определить отношение между элементами стека
			// до тех пор, пока не будет отношение меньше 
			// и сделать их редукцию
			if (getStackSize() == 1){ // if only one element in stack 
				reduction(lastInStack, currentCode);   // than make reduction
	//			polizList.add(poliz.toString().replace(",", "").replace("[", "").replace("]", ""));
				doPoliz(pos);
			}else{ 
				StringBuilder buffer = new StringBuilder();
					while (relation != 0){
						String lastElement = pop();
						String preLastElement = peek();
						buffer.insert(0, lastElement); // собираю буффер до тех пор, пока отношение не будет меньше
						if (stack.isEmpty()){
							break;
						}
						relation = defineRelation(preLastElement, lastElement);
					}
					push(buffer.toString());
					// replace terminal with non-terminal 
						reduction(buffer.toString(), currentCode);
					
//						polizList.add(poliz.toString().replace(",", "").replace("[", "").replace("]", ""));
					doPoliz(pos);
			}
		}
		return peek();
	}
	
	private boolean reduction(String element, String elementCode){ // function of replacing rule to non-terminal
		StringBuilder ruleBuffer = new StringBuilder();
		int line = 0;
		int i = 1;		
		
		while (line < splitGram.length){
			for (; i < splitGram[line].length; i++){
				if (splitGram[line][i].equals("|")){
					i++;
					break;
				}
				ruleBuffer.append(splitGram[line][i]);
			}
			if (ruleBuffer.toString().equals(element)){
				poliz(splitGram[line][0]);
				pop();
				push(splitGram[line][0]);
				return true;
			}
			ruleBuffer = new StringBuilder();
			if (i == splitGram[line].length){
				line++;
				i = 1;
			}
		}
		return false;
	}
	
	//  Clarification to relations
	//	-1 relation not found
	//	 0 less
	//	 1 equals
	//	 2 more
	private int defineRelation(String previous, String current){
		for (int line = 0; line < relations.length; line++){
			if (ruleHeaders.get(line).equals(previous)){
				for (int headline = 0; headline < ruleHeaders.size(); headline++){
					if (ruleHeaders.get(headline).equals(current)){ // search intersection between lexemes 
						if (relations[line][headline].equals("<") || relations[line][headline].equals("1")){
							return 0;
						}
						if (relations[line][headline].equals("=") || relations[line][headline].equals("2")){
							return 1;
						}
						if (relations[line][headline].equals(">") || relations[line][headline].equals("3")){
							return 2;
						}
						if (stack.size()==1){
							return 0;
						}else{ 
							return 2;
						}
					}
				}
			}
		}
		return -1;
	}
	
	private String[][] splitGram(String[] gramRules){
		String[][] splitGram = new String[gramRules.length][];
		for (int i = 0; i < gramRules.length; i++){
			splitGram[i] = gramRules[i].trim().split(" ");
		}
		return splitGram;
	}
	private void push(Stack<String> stack, String element){
		stack.push(element);
	}
	private String pop(Stack<String> stack){ 
		return stack.pop();
	}
	private String peek(Stack<String> stack){
		return stack.peek();
	}
	private void push(String item){
		stack.push(item);
	}
	
	private String pop(){ // get with deleting
		return stack.pop();
	}
	
	private String peek(){ // get without delete in stack
		if (stack.empty()){
			return "";
		}
		return stack.peek();
	}
	
	private int getStackSize(){
		return stack.size();
	}
	
	public int getRows(){
		return stackList.size();
	}
	public String[][] getData(){
		relationList.add(">");
		table = new String[getRows()][5];
		for (int i = 0; i < stackList.size(); i++){
			table[i][0] = stackList.get(i);
			table[i][1] = relationList.get(i);			
			table[i][2] = chainList.get(i);
			if (chainList.get(i).isEmpty()){ // add end of input chain 
				table[i][2] = "#";
			}
		//	table[i][3] = polizList.get(i);
		/*	if (stackList.get(i).equals("вираз ≥нц") && (polizList.get(i).length() > 0)){
					table[i][4] = countLine(polizList.get(i));
			}*/
		}
		return table;
	}
	public String getErrSymb(){
		return chainList.get(chainList.size()-1).substring(0,1);
	}
	public int getRow(){
		return row;
	}
	private void poliz(String element){
		if (element.equals("вираз")){			
			if (element.length() > 1){
				semanticExpression(element);
				polizList.add(poliz.toString().replace(",", ""));
				return;
			}
		}
		if (element.equals("терм")){
			if (element.length() > 1){
				semanticTerm(element);
				polizList.add(poliz.toString().replace(",", ""));
				return;
			}
		}		
		return;
	}
	private String countLine(String line){
		String[] lineSplit = line.split(" ");
		Stack<Integer> lineStack = new Stack<Integer>();
		for (String curr : lineSplit){
			if (curr.matches("\\d+")){
				lineStack.push(Integer.parseInt(curr));
			}else if (curr.endsWith("+")){
				lineStack.push(lineStack.pop() + lineStack.pop());
			}else if (curr.endsWith("-")){
				int temp = lineStack.pop() ;
				lineStack.push(lineStack.pop() - temp);
			}else if (curr.endsWith("/")){
				int temp = lineStack.pop() ;
				lineStack.push(lineStack.pop() / temp);
			}else if (curr.endsWith("*")){
				lineStack.push(lineStack.pop() * lineStack.pop());
			}
		}
		return Integer.toString(lineStack.pop());
	}
	
	// SEMANTIC SUBPROGRAMs
	// Expression
	private void semanticExpression(String element){
		String temp = peek(stack);
		for (int i = 0; i < temp.length(); i++){
			if (temp.charAt(i) == '+'){
				push(poliz, "+");
			}
			if (temp.charAt(i) == '-'){
				push(poliz, "-");
			}	
		}
	}
	// Terminal
	private void semanticTerm(String element){
		String temp = peek(stack);
		for (int i = 0; i < temp.length(); i++){
			if (temp.charAt(i) == '*'){
				push(poliz, "*");
			}else if (temp.charAt(i) == '/'){
				push(poliz, "/");
			}	
		}
	}
	// END SEMANTIC SUBPROGRAMs
}