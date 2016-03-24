package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import source.LexemeAnalyzer;

public class Form extends JFrame{

	private static final long serialVersionUID = 1L;
	static private JFrame mainWindow; 
	static private JFrame relationTable;
	static private JFrame grammarTable;
	static private JFrame polizTable;
	static private JFrame lexemeTable;
	static private JFrame errorTable;
	
	static private JButton getRelation;
	static private JButton getGrammar;
	static private JButton getProg;
	static private JButton setProg;
	static private JButton getPoliz;
	static private JButton getLexemes;
	
	private JTextArea textField;
	
	static private JPanel topPanel;
	
	static private StringBuilder buffer;
	
	static private Font font = new Font("Verdana", Font.PLAIN, 16);
	static private Font fontPoliz = new Font("Verdana", Font.PLAIN, 14);
	
	public Form(){
		
		// main frame initialization / non-visible by default
		mainWindow = new JFrame("Лабораторна робота №5");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setPreferredSize(new Dimension(500, 600));
		mainWindow.pack();
		
		// relation frame initialization / non-visible by default
		relationTable = new JFrame("Таблиця відношень передування");
		relationTable.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		relationTable.setVisible(false);
		
		// exceptions table
		errorTable= new JFrame("Таблиця помилок");
		errorTable.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		errorTable.setVisible(false);
		
		// table of lexemes
		lexemeTable = new JFrame("Таблиця лексем");
		lexemeTable.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		lexemeTable.setVisible(false);
		
		// grammar frame initialization / non-visible by default
		grammarTable = new JFrame("Граматика");
		grammarTable.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		grammarTable.setVisible(false);
		
		// POLIZ table
		polizTable = new JFrame("Таблиця відношень передування");
		polizTable.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		polizTable.setVisible(false);

		// create buttons
		getRelation = new JButton("Таблиця відношень");
		getGrammar = new JButton("Граматика");
		getProg = new JButton("Програма");
		setProg = new JButton("Зберегти");
		getPoliz = new JButton("ПОЛІЗ");
		getLexemes = new JButton("Лексеми");
		
		// creating input field
		textField = new JTextArea(22, 30);
		textField.setFont(font);
		
		topPanel = new JPanel(new FlowLayout()); // create layer
		topPanel.add(getRelation); // add element to layer
		topPanel.add(getGrammar);
		topPanel.add(getLexemes);
		topPanel.add(getPoliz);
		topPanel.add(textField);
		topPanel.add(getProg);
		topPanel.add(setProg);
		
		mainWindow.add(topPanel); // add layer to frame
		
		mainWindow.setVisible(true);
		
		// Button's actions
		getRelation.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (relationTable.isVisible()){
					relationTable.setVisible(false);
				}else{
					relationTable.setVisible(true);
				}
			}
		});
		
		getGrammar.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (grammarTable.isVisible()){
					grammarTable.setVisible(false);
				}else{
					grammarTable.setVisible(true);
				}
			}
		});
		
		getLexemes.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (lexemeTable.isVisible()){
					lexemeTable.setVisible(false);
				}else{
					lexemeTable.setVisible(true);
				}
			}
		});
		
		getPoliz.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (polizTable.isVisible()){
					polizTable.setVisible(false);
				}else{
					polizTable.setVisible(true);
				}
			}
		});
		
		getProg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.setText(null);
				try(FileReader file = new FileReader("D:\\SAPR\\input.txt")){
					int c;
					while((c=file.read())!=-1){
						textField.append(Character.toString((char)c));
					}
					file.close();
				}catch(IOException ex){
		            System.out.println(ex.getMessage());
		        }
			}
		});
		
		setProg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buffer = new StringBuilder();
				buffer.append(textField.getText());
				try(FileWriter writer = new FileWriter("D:\\SAPR\\input.txt", false)){
					// запись по символам
			        writer.write(buffer.toString());
				}
				catch(IOException ex){
					System.out.println(ex.getMessage());
				} 
			}
		});
	}
	
	public void grammarTable(){
		String[] columnNames = {
				"№", " "
			};
			
			final int K = 2;
			int row = 25;
			String[][] data = new String[row][K];
		    
		    for (int j = 0; j < row; j++){
		    	data[j][1] = " ";
		    }
	        
	        try(FileReader file = new FileReader("D:\\SAPR\\grammar.txt")){
				int c;
				int i = 0;
				while((c=file.read())!=-1){
					if (Character.toString((char)c).equals("\n")){
						data[i][0] = Integer.toString(i + 1);
						data[i + 1][0] = Integer.toString(i + 2);
						i++;
						continue;
					}
					data[i][1] += Character.toString((char)c);
				}
				file.close();
			}catch(IOException ex){
	            System.out.println(ex.getMessage());
	        }
	        
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);

		table.getColumnModel().getColumn(0).setMaxWidth(25);
		grammarTable.getContentPane().add(scrollPane);
		grammarTable.pack();
		grammarTable.setLocation(300, 100);
	}

	public static void relationTable(String[][] relation, List<String> colNames){
		final int N = colNames.size()+1; // header's size
		String[] columnNames = new String[N];
		columnNames[0] = " ";
		
		for (int i = 1; i < N; i++){
			columnNames[i] = colNames.get(i-1);
		}
		
		String data[][] = new String[N][N];
			
		for (int i = 1; i < N;  i++){
			data[i-1][0] = colNames.get(i-1);
			for(int j = 1; j < N; j++){
				if (relation[i-1][j-1].equals("0")){
					data[i-1][j] = "";
				}else if (relation[i-1][j-1].equals("1") || relation[i-1][j-1].equals("<")){
					data[i-1][j] = "<";
				}else if (relation[i-1][j-1].equals("2") || relation[i-1][j-1].equals("=")){
					data[i-1][j] = "=";
				}else if (relation[i-1][j-1].equals("3") || relation[i-1][j-1].equals(">")){
					data[i-1][j] = ">";
				}else if (relation[i-1][j-1].equals("12") || relation[i-1][j-1].equals("<|=")){
					data[i-1][j] = "<|=";
				}else if (relation[i-1][j-1].equals("23") || relation[i-1][j-1].equals(">|=")){
					data[i-1][j] = ">|=";
				}else if (relation[i-1][j-1].equals("13")){
					data[i-1][j] = "<|>";
				}else{
					data[i-1][j] = relation[i-1][j-1];
				}
			}
		}
		
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		
		relationTable.getContentPane().add(scrollPane);
		relationTable.pack();
		relationTable.setLocationRelativeTo(null);
	}
	
	public void lexemeTable(String[][] info){

		int N = LexemeAnalyzer.getLexemeCount();
		
		String[] columnNames = {
			"Строка",
			"Підстрока",
			"Код",
			"Індекс IDN/CON"
		};
		
		final int K = 4;
		String data[][] = new String[N][K];
		
		for (int i = 0; i < N;  i++){
			for(int j = 0; j < K; j++){
				data[i][j] = info[i][j];
			}
		}
		
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		
		lexemeTable.getContentPane().add(scrollPane);
		lexemeTable.setPreferredSize(new Dimension(450, 600));
		lexemeTable.pack();
		lexemeTable.setLocation(300, 100);
	}
	
	public void polizTable(String info[][], int N){
		String[] columnNames = {
			"Стек",
			"Відношення",
			"Вх. ланцюг",
			"ПОЛІЗ",
			"Значення"
		};
		
		final int K = 5;
		String data[][] = new String[N][K];
		
		for (int i = 0; i < N;  i++){
			for(int j = 0; j < K; j++){
				data[i][j] = info[i][j];
			}
		}
		
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		table.getColumnModel().getColumn(0).setMaxWidth(600);
		table.getColumnModel().getColumn(1).setMaxWidth(30);
		table.getColumnModel().getColumn(2).setMaxWidth(600);
		table.getColumnModel().getColumn(3).setMaxWidth(130);
		table.getColumnModel().getColumn(4).setMaxWidth(80);
		table.setFont(fontPoliz);
		polizTable.getContentPane().add(scrollPane);
		polizTable.setPreferredSize(new Dimension(750, 900));
		polizTable.pack();
		polizTable.setLocation(300, 100);
	}
	
	public void errorTable(int N,String[][] info){
		String[] columnNames = {
			" № ",
			"Таблиця помилок"
		};
		
		final int K = 2;
		String data[][] = new String[N][K];
		
		for (int i = 0; i < N;  i++){
			for(int j = 0; j < K; j++){
				data[i][j] = info[i][j];
			}
		}
		
		if (N<1){
			errorTable.setVisible(false);
		}else{
			errorTable.setVisible(true);
		}
		
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		errorTable.getContentPane().add(scrollPane);
		errorTable.setPreferredSize(new Dimension(300, 180));
		errorTable.pack();
		errorTable.setLocation(800, 400);
	}
}
