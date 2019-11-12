package spellChecker;

//Name: Michael Flasco
//Date: 03/10/2019	
//Program Name: SpellChecker UI
//Purpose: Checks if words in testState.txt are spelled correctly in comparison to dictionary.txt with a User Interface
//Changes: Refactored to apply unit testing

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Flasco_SpellChecker extends Application implements EventHandler<ActionEvent> {

	Button button;
	Text text;
	ArrayList<String> testStatesList;
	static ArrayList<String> dictionaryList;
	private TextField testStatesFile;
	private TextField dictionaryFile;
	static Connection conn;

	public static void main(String[] args) {
		try {
			connection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			createTable();
		} catch (Exception e) {
			System.out.println(e);
		}

	
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Label testFileLabel = new Label("Check File:");
		Label dictionaryLabel = new Label("Dictionary:");

		testStatesFile = new TextField();
		dictionaryFile = new TextField();
		testStatesFile.setText("testStates.txt");
		dictionaryFile.setText("dictionary.txt");

		button = new Button();
		button.setText("Submit");
		button.setOnAction(this);

		text = new Text();

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(10));
		pane.setHgap(5);
		pane.setVgap(5);
		pane.add(testFileLabel, 0, 0);
		pane.add(testStatesFile, 1, 0);
		pane.add(dictionaryLabel, 0, 1);
		pane.add(dictionaryFile, 1, 1);
		pane.add(button, 0, 3);
		pane.add(text, 0, 4);

		Scene scene = new Scene(pane);
		primaryStage.setTitle("Spell Check");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		ArrayList getRows= null;
		try {
			
			dictionary();

			getRows = getDictionaryWords();
		} catch (Exception e) {
			
			System.out.println(e);
		}
		if (event.getSource() == button) {
			boolean test = true;
			while (test) {

				// Dictionary file Reader adds elements in file to ArrayList
				
				//dictionary();
	
				// TestStates file Reader adds elements in file to ArrayList
				
				testStates();

				// Spell Check Logic implementation
				for (int i = 0; i < testStatesList.size(); i++) {
					if (getRows.contains(testStatesList.get(i))) {
						System.out.println('"' + testStatesList.get(i) + '"' + ": found");

					} else {

						System.out.println('"' + testStatesList.get(i) + '"' + ": unknown word");
					}
				}
				text.setText("Spell Check Processed");

				test = false;
			}

		}

	}

	// Dictionary function and test method
	public static int dictionary() {
		boolean run = true;
		dictionaryList = null;
		String dictionaryText;

		
		
		
		while (run) {
			dictionaryList = new ArrayList<>();
			ArrayList<?> getRows = null;
			File dictionary = new File("dictionary.txt");
			BufferedReader dictionaryBuffReader = null;
			try {
				dictionaryBuffReader = new BufferedReader(new FileReader(dictionary));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}

			try {
				getRows = getDictionaryWords();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
			// check is database is empty, if empty load dictionary.txt
			try {
				if (getRows.isEmpty()) {
					while ((dictionaryText = dictionaryBuffReader.readLine()) != null) {
//					dictionaryList.add(dictionaryText);

						try {
							conn = connection();
							PreparedStatement posted = conn.prepareStatement(
									"insert into spelling.word (words) values ('" + dictionaryText.toString() + "')");
							posted.executeUpdate();

						} catch (Exception e) {
							
							System.out.println(e);
						} finally {
							System.out.println("Insert completed");
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
			run = false;
		}
		// testing
		int count = 0;
		for (int i = 0; i < dictionaryList.size(); i++) {
			dictionaryList.get(i);
			count++;

		}

		return count;
	}

	// Test function and test method
	public int testStates() {

		boolean run = true;

		while (run) {
			testStatesList = new ArrayList<>();
			File testStates = new File("testStates.txt");
			BufferedReader testStateBuffReader = null;
			try {
				testStateBuffReader = new BufferedReader(new FileReader(testStates));
			} catch (FileNotFoundException e) {
				System.out.println(e);
			}

			String testStateText;
			try {
				while ((testStateText = testStateBuffReader.readLine()) != null)
					testStatesList.add(testStateText);

			} catch (IOException e) {
			
				System.out.println(e);
			}
			run = false;
		}
		int count = 0;
		for (int i = 0; i < testStatesList.size(); i++) {
			testStatesList.get(i);
			count++;

		}

		return count;

	}

	// DATABASE IMPLEMENTATION===================================================
	
	public static Connection connection() throws Exception {
		Statement statement = null;
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/spelling";
			String username = "";
			String password = "";
//			String sqlStatement = "CREATE DATABASE SPELLING";
//			statement = connection.createStatement();
//			statement.executeUpdate(sqlStatement);
			
			Class.forName(driver);

			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");

			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	public static void createTable() throws Exception {
		try {
		
			PreparedStatement create = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS word(id int NOT NULL AUTO_INCREMENT, words varchar(255), PRIMARY KEY(id))");
			create.executeUpdate();
		} catch (Exception e) {

			System.out.println(e);
			
		} finally {
			System.out.println("Table Created");
		}
	}

	public static ArrayList<String> getDictionaryWords() throws Exception {
		try {
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM word");

			ResultSet result = statement.executeQuery();
			int count = 0;
			
//			System.out.println("Dictionary words");
//			System.out.println("\n====================================");
			while (result.next()) {
				
//				System.out.println(result.getString("words"));

				dictionaryList.add(result.getString("words"));
				count++;
			}
			System.out.println("====================================	");

			

			return dictionaryList;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}
	

}
