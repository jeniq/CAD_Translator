package relation;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import source.ReadText;

public class Execute {
	
	private static String filePath = "D://SAPR/gramstrat.txt";
	private static String grammarInLine;
	private	static String[] parsedGram;
	
	public static void doRelation(){
		
		try{
			grammarInLine = ReadText.read(filePath);
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "Файл з граматикою не знайдено!");
		}
		
		parsedGram = ParseGram.parse(grammarInLine);		
		
		RelationGrid.setParsedGram(parsedGram);
		RelationGrid.start();
		RelationGrid.toTable();
	}

}
