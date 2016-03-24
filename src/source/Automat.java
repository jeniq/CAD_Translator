package source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import gui.Form;

public class Automat{
	
	private String data[][] = new String[66][5]; // can be exception outOfBounds
	
	Map<String, String> lexemeMap = new HashMap<>();
	{
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
	}
	
	private ArrayList<String> exception = new ArrayList<String>();
	private ArrayList<String> exception_pos = new ArrayList<String>();
	private Stack<Integer> stack = new Stack<>();
	
	private String[][] list;

	JTable pdaTable;
	Form table;
	
	{
		data[0][0] = "1";
		data[0][1] = "1"; // var
		data[0][2] = "2";
		data[0][3] = "-1";
		data[0][4] = " [≠] помилка";
		
		data[1][0] = "1";
		data[1][1] = "2"; // {
		data[1][2] = "21";
		data[1][3] = "8";
		data[1][4] = " [≠] помилка";
		
		data[2][0] = "2";
		data[2][1] = "27"; // id
		data[2][2] = "3";
		data[2][3] = "-1";
		data[2][4] = " [≠] помилка";
		
		data[3][0] = "3";
		data[3][1] = "23"; // :
		data[3][2] = "4";
		data[3][3] = "-1";
		data[3][4] = " [≠] помилка";
		
		data[4][0] = "3";
		data[4][1] = "25"; // ,
		data[4][2] = "2";
		data[4][3] = "-1";
		data[4][4] = " [≠] помилка";
		
		data[5][0] = "4";
		data[5][1] = "4"; // float
		data[5][2] = "5";
		data[5][3] = "-1";
		data[5][4] = " [≠] помилка";
		
		data[6][0] = "4";
		data[6][1] = "5"; // int
		data[6][2] = "5";
		data[6][3] = "-1";
		data[6][4] = " [≠] помилка";
		
		data[7][0] = "5";
		data[7][1] = "2"; // {
		data[7][2] = "21";
		data[7][3] = "6";
		data[7][4] = " [≠] помилка";
		
		data[8][0] = "5";
		data[8][1] = "24"; // ;
		data[8][2] = "2";
		data[8][3] = "-1";
		data[8][4] = " [≠] помилка";
		
		data[9][0] = "6";
		data[9][1] = "24"; // ;
		data[9][2] = "7";
		data[9][3] = "-1";
		data[9][4] = "[≠] помилка";
		
		data[10][0] = "7";
		data[10][1] = "3"; // }
		data[10][2] = "0";
		data[10][3] = "-1";
		data[10][4] = "out";
		
		data[11][0] = "7";
		data[11][1] = " ";
		data[11][2] = "21";
		data[11][3] = "6";
		data[11][4] = "null";
		
		data[62][0] = "8";
		data[62][1] = "24"; // ;
		data[62][2] = "7";
		data[62][3] = "7";
		data[62][4] = "[≠] помилка";
		
		// operator
		data[12][0] = "21";
		data[12][1] = "27"; // id
		data[12][2] = "22";
		data[12][3] = "-1";
		data[12][4] = " [≠] помилка";
		
		data[13][0] = "21"; // read
		data[13][1] = "7";
		data[13][2] = "30";
		data[13][3] = "-1";
		data[13][4] = " [≠] помилка";
		
		data[14][0] = "21";
		data[14][1] = "6"; // write
		data[14][2] = "30";
		data[14][3] = "-1";
		data[14][4] = " [≠] помилка";
		
		data[15][0] = "21";
		data[15][1] = "8"; // for 
		data[15][2] = "32";
		data[15][3] = "-1";
		data[15][4] = " [≠] помилка";
	
		data[16][0] = "21";
		data[16][1] = "9"; // if
		data[16][2] = "41";
		data[16][3] = "-1";
		data[16][4] = " [≠] помилка";

		data[17][0] = "22";
		data[17][1] = "10"; // =
		data[17][2] = "51";
		data[17][3] = "23";
		data[17][4] = " [≠] помилка";
		
		data[18][0] = "23";
		data[18][1] = "26";
		data[18][2] = "51";
		data[18][3] = "27";
		data[18][4] = " [≠] помилка";
		
		data[19][0] = "23";
		data[19][1] = " ";
		data[19][2] = " ";
		data[19][3] = "-1";
		data[19][4] = "out";
		
		data[20][0] = "27";
		data[20][1] = "23";
		data[20][2] = "28";
		data[20][3] = "-1";
		data[20][4] = " [≠] помилка";
		
		data[21][0] = "28";
		data[21][1] = " ";
		data[21][2] = " ";
		data[21][3] = "-1";
		data[21][4] = "out";

		data[23][0] = "30";
		data[23][1] = "27";
		data[23][2] = "31";
		data[23][3] = "-1";
		data[23][4] = " [≠] помилка";
		
		data[22][0] = "31";
		data[22][1] = " ";
		data[22][2] = " ";
		data[22][3] = "-1";
		data[22][4] = "out";	

		data[24][0] = "32";
		data[24][1] = "21";
		data[24][2] = "33";
		data[24][3] = "-1";
		data[24][4] = " [≠] помилка";
		
		data[25][0] = "33";
		data[25][1] = "27";
		data[25][2] = "34";
		data[25][3] = "-1";
		data[25][4] = " [≠] помилка";
	
		data[31][0] = "34";
		data[31][1] = "10";
		data[31][2] = "51";
		data[31][3] = "35";
		data[31][4] = " [≠] помилка";
	
		data[32][0] = "35";
		data[32][1] = "24";
		data[32][2] = "61";
		data[32][3] = "36";
		data[32][4] = " [≠] помилка";
	
		data[33][0] = "36";
		data[33][1] = "24";
		data[33][2] = "37";
		data[33][3] = "-1";
		data[33][4] = " [≠] помилка";
		
		data[34][0] = "37";
		data[34][1] = "27";
		data[34][2] = "38";
		data[34][3] = "-1";
		data[34][4] = " [≠] помилка";
	
		data[35][0] = "38";
		data[35][1] = "10";
		data[35][2] = "51";
		data[35][3] = "39";
		data[35][4] = " [≠] помилка";
	
		data[36][0] = "39";
		data[36][1] = "22";
		data[36][2] = "21";
		data[36][3] = "40";
		data[36][4] = " [≠] помилка";
	
		data[37][0] = "40";
		data[37][1] = " ";
		data[37][2] = " ";
		data[37][3] = "-1";
		data[37][4] = "out";
	
		data[38][0] = "41";
		data[38][1] = "21";
		data[38][2] = "51";
		data[38][3] = "42";
		data[38][4] = " [≠] помилка";
		
		data[39][0] = "42";
		data[39][1] = "14";
		data[39][2] = "51";
		data[39][3] = "43";
		data[39][4] = " [≠] помилка";
	
		data[40][0] = "42";
		data[40][1] = "15";
		data[40][2] = "51";
		data[40][3] = "43";
		data[40][4] = " [≠] помилка";		
		
		data[41][0] = "42";
		data[41][1] = "12";
		data[41][2] = "51";
		data[41][3] = "43";
		data[41][4] = " [≠] помилка";
		
		data[42][0] = "42";
		data[42][1] = "16";
		data[42][2] = "51";
		data[42][3] = "43";
		data[42][4] = " [≠] помилка";
		
		data[65][0] = "42";
		data[65][1] = "11";
		data[65][2] = "51";
		data[65][3] = "43";
		data[65][4] = " [≠] помилка";
		
		data[43][0] = "43";
		data[43][1] = "22";
		data[43][2] = "44";
		data[43][3] = "-1";
		data[43][4] = " [≠] помилка";
		
		data[44][0] = "44";
		data[44][1] = "2";
		data[44][2] = "21";
		data[44][3] = "45";
		data[44][4] = " [≠] помилка";
	
		data[45][0] = "45";
		data[45][1] = "24";
		data[45][2] = "46";
		data[45][3] = "-1";
		data[45][4] = "помилка";
	
		data[46][0] = "46";
		data[46][1] = "3";
		data[46][2] = " ";
		data[46][3] = "-1";
		data[46][4] = "out";
		
		data[47][0] = "46";
		data[47][1] = " ";
		data[47][2] = "21";
		data[47][3] = "45";
		data[47][4] = "null";
		
		// expression
		data[48][0] = "51";
		data[48][1] = "27";
		data[48][2] = "52";
		data[48][3] = "-1";
		data[48][4] = " [≠] помилка";
		
		data[49][0] = "51";
		data[49][1] = "28";
		data[49][2] = "52";
		data[49][3] = "-1";
		data[49][4] = " [≠] помилка";
	
		data[50][0] = "51";
		data[50][1] = "21";
		data[50][2] = "51";
		data[50][3] = "53";
		data[50][4] = " [≠] помилка";
		
		data[51][0] = "52";
		data[51][1] = "17";
		data[51][2] = "51";
		data[51][3] = "-1";
		data[51][4] = "";
		
		data[52][0] = "52";
		data[52][1] = "18";
		data[52][2] = "51";
		data[52][3] = "-1";
		data[52][4] = "";
	
		data[53][0] = "52";
		data[53][1] = "19";
		data[53][2] = "51";
		data[53][3] = "-1";
		data[53][4] = "";
	
		data[54][0] = "52";
		data[54][1] = "20";
		data[54][2] = "51";
		data[54][3] = "-1";
		data[54][4] = "";
		
		data[55][0] = "52";
		data[55][1] = " ";
		data[55][2] = " ";
		data[55][3] = "-1";
		data[55][4] = "out";
	
		data[56][0] = "53";
		data[56][1] = "21";
		data[56][2] = "52";
		data[56][3] = "-1";
		data[56][4] = " [≠] помилка";
		
		data[57][0] = "61";
		data[57][1] = "32";
		data[57][2] = "61";
		data[57][3] = "64";
		data[57][4] = " ";
		
		data[63][0] = "61";
		data[63][1] = " ";
		data[63][2] = "51";
		data[63][3] = "62";
		data[63][4] = "null";
	
		data[58][0] = "62";
		data[58][1] = "14"; // <
		data[58][2] = "51";
		data[58][3] = "63";
		data[58][4] = " [≠] помилка";
		
		data[59][0] = "62"; 
		data[59][1] = "15"; // >
		data[59][2] = "51";
		data[59][3] = "63";
		data[59][4] = " [≠] помилка";
		
		data[60][0] = "62";
		data[60][1] = "11";
		data[60][2] = "51";
		data[60][3] = "63";
		data[60][4] = " [≠] помилка";

		data[61][0] = "62";
		data[61][1] = "12";
		data[61][2] = "51";
		data[61][3] = "63";
		data[61][4] = " [≠] помилка";
		
		data[30][0] = "62";
		data[30][1] = "16";
		data[30][2] = "51";
		data[30][3] = "63";
		data[30][4] = " [≠] помилка";
	
		data[29][0] = "62";
		data[29][1] = "13";
		data[29][2] = "51";
		data[29][3] = "63";
		data[29][4] = " [≠] помилка";
	
		data[26][0] = "63";
		data[26][1] = "29";
		data[26][2] = "61";
		data[26][3] = "-1";
		data[26][4] = " [≠] вихід";
		
		data[27][0] = "63";
		data[27][1] = "30";
		data[27][2] = "61";
		data[27][3] = "-1";
		data[27][4] = " ";
		
		data[28][0] = "63";
		data[28][1] = " ";
		data[28][2] = " ";
		data[28][3] = "-1";
		data[28][4] = "out";		
		
		data[64][0] = "64";
		data[64][1] = "33";
		data[64][2] = "63";
		data[64][3] = "-1";
		data[64][4] = " [≠] помилка";
		
	}
	
	public Automat(Form table, String[][] list){
		this.list = list;
		this.table = table;
	}
	
	private void errMsg(String msg, String pos){
		exception.add(msg);
		exception_pos.add(pos);
	}
	
	public int getExceptionSize(){
		return exception.size();
	}
	
	public String[][] toArray(){
		String[][] data = new String[exception.size()][2];
		
		for (int i = 0; i < exception.size(); i++){
			data[i][0] = exception_pos.get(i);
			data[i][1] = exception.get(i);
		}
		return data;
	}
	
	public void execute(){	
		String output[] = new String[2];
		output[0] = "1"; // state
		output[1] = "1"; // lexeme
		
		while (!(output[1].equals("end"))){
			output = execute(output[0], output[1]);
			if (output[0].equals("-1")){
				JOptionPane.showMessageDialog(null, "Error!");
				break;
			}else if (output[0].equals("0")){
				//JOptionPane.showMessageDialog(null, "DONE!!! :))");
				break; 
			}
		}
	}
		
	static int iter = 1;
	String[] result = new String[2];
	String temp = "0";
	
	public String[] execute(String alfa, String label){
		int current = 0;
		result[0] = "-1";
		
		for (; current < data.length; current++){
			if (data[current][0].equals(alfa)){
				if (list[iter-1][2].equals(data[current][1])){
					result[0] = data[current][2]; // next state
					result[1] = list[iter][2]; // next lexeme
					iter++;
					if (data[current][2].equals(" ")){ // if next state empty, get it from stack
						result[0] = stack.pop().toString();
					}
					if (!data[current][3].equals("-1")){
						stack.push(Integer.parseInt(data[current][3]));
					}
					temp = result[0]; // remember last right result
					break;
				}else if (data[current][1].equals(" ")){
					if (data[current][4].equals("null")){
						result[0] = data[current][2];
						temp = result[0];
						stack.push(Integer.parseInt(data[current][3]));
					}else if (data[current][4].equals("out")){
						result[0] = stack.pop().toString();
						temp = result[0];
						result[1] = list[iter-1][2];
					}
				}
			}
		}
		if (result[0].equals("-1")){
				for (int state = 0; state < data.length - 1; state++){
					if (data[state][0].equals(temp)){
						errMsg("Очікується " + lexemeMap.get(data[state][1]), list[iter][0]);
						
					}
				}
		}
		return result;
	}
}
