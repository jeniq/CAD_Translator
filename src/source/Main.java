package source;

import java.util.List;

import javax.swing.JOptionPane;

import exceptions.LexemeNotFound;
import gui.Form;
import poliz.Poliz;
import relation.Execute;
import relation.RelationGrid;

public class Main {

	public static void main(String[] args) {
		Form frame = new Form(); // create window
		frame.grammarTable(); // create table with grammar, by default is invisible
		Execute.doRelation(); // create relation in grammar rules (extended Backus-Naur form [EBNF])
		String[][] lexemeTable = LexemeAnalyzer.Do(); // start lexeme analyzer and create data table with lemexes
		frame.lexemeTable(lexemeTable); // add lexeme table to frame
		
		List<String> headers = RelationGrid.getHeaders(); // get all elements from EBNF
		
		try{
			// check for errors
			 Automat automat = new Automat(frame, lexemeTable);
			 automat.execute();
			 int size = automat.getExceptionSize();
			 frame.errorTable(size, automat.toArray());
		   // end check for errors
			if (size > 0){
				
			}else{
				Poliz poliz = new Poliz(lexemeTable, RelationGrid.getRelations(), headers, RelationGrid.getParsedGram());
				String output = poliz.doPoliz(0);
				if (!output.equals(headers.get(0))){
					JOptionPane.showMessageDialog(null, "јкс≥ома не знайдена!");
				}
				int n = poliz.getRows();
				String[][] data = poliz.getData();
				frame.polizTable(data, n);
			}
		}catch(LexemeNotFound e){
			
		}
	}

}
