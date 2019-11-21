package cecs429.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.DiskIndexWriter;
import cecs429.index.DiskPositionalIndex;
import cecs429.index.Index;
import cecs429.index.PositionalInvertedIndex;
import cecs429.index.Posting;
import cecs429.query.Accumalator;
import cecs429.query.BooleanQueryParser;
import cecs429.query.RankedRetrieval;
import cecs429.text.EnglishTokenStream;
import cecs429.text.Normalize;

public class GUI  extends JPanel{
	private URI uri;
	//index default corpus
	private DocumentCorpus corpus;
    // DocumentCorpus corpus = DirectoryCorpus.loadTextDirectory(Paths.get(selectDirectory()).toAbsolutePath(), ".txt"); THIS IS FOR .txt FILES
    private Index index;
    private DiskPositionalIndex dpi;
    private String directory = "";
    private DiskIndexWriter diw;
    private static String langOp;
      
	// Default constructor
	public GUI(){
		this.directory = selectDirectory();
		String temp = JOptionPane.showInputDialog("Please input language in use");
		if(temp.equals("english")||temp.equals("English"))
			langOp="en";
		else if(temp.equals("french")||temp.equals("French"))
			langOp="fr";
		if(jsonFiles(this.directory).get(0).endsWith(".json"))
			this.corpus = DirectoryCorpus.loadJsonDirectory(Paths.get(this.directory).toAbsolutePath(), ".json");// THIS IS FOR .json FILES
		else if(!jsonFiles(this.directory).get(0).endsWith(".json"))
			this.corpus = DirectoryCorpus.loadTextDirectory(Paths.get(this.directory).toAbsolutePath(), ".txt"); // THIS IS FOR .txt FILES
		this.index = indexCorpus(corpus);
		this.diw = new DiskIndexWriter(this.directory, this.index,langOp);
		this.diw.buildIndex();
		this.dpi = new DiskPositionalIndex(this.directory);
		
	}
	
	/*
	 *  GUI for special queries and general search
	 */
	public void query() throws Exception{
		SwingUtilities.invokeLater(new Runnable() {
		public void run(){
			// Creating GUI Elements
			JFrame frame = new JFrame("Stroogle Search Engine");
			JPanel panel = new JPanel();
			JTextField textField = new JTextField(35);
			JButton search = new JButton("Search");
			JButton browse = new JButton("Change Directory");
			JButton options = new JButton("Boolean Query");
			JTextArea results = new JTextArea(19,55);
			JScrollPane scrollPane = new JScrollPane(results);
			
			// When user clicks on browse button 
			browse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selectNewIndex = selectDirectory();
					changeDirectory(selectNewIndex);
				}
			});

			options.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("its here");
					String query = textField.getText().toLowerCase();
					BooleanQueryParser test = new BooleanQueryParser();
					Normalize normalize = new Normalize(langOp);
					test.parseQuery(query);
					int counter = 0;
					for(Posting p: test.parseQuery(query).getPostings(dpi, normalize))
					{
						results.append("Document ID: " + p.getDocumentId() + "\n");
						//results.append("File Name: " + corpus.getDocument(p.getDocumentId()).getFileName() + "\n");
						//results.append("Title: " + corpus.getDocument(p.getDocumentId()).getTitle() + "\n");
						//results.append("Positions: " + p.getPositions() + "\n");
						//results.append("\n");
						counter++;
					}
					results.append("Number of Documents Returned With Boolean Query: " +counter);
				}
			});

			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String query = textField.getText().toLowerCase();
					// If '-' is at the beginning of the first word switch the second word with first
					if(query.charAt(0)=='-')
			        {
			            int whiteSpace =0;
			            for(int i =0;i<query.length();i++)
			            {
			                if(query.charAt(i)==' ')
			                {
			                    whiteSpace = i+1;
			                }
			            }
			            query = query.substring(whiteSpace, query.length()) +" "+ query.substring(0, whiteSpace);
			        }
					results.setText("");
					
					String special[] = textField.getText().split(" ", 2);
					// If string equals ':vocab' prints the first 1000 words in vocabulary
					if(textField.getText().equals(":vocab")) {
						// Get vocab in index
						List<String> vocabList = index.getVocabulary();
						//initilize counter
						int counter = 0;
						for(String s : vocabList)
						{
							//append to results
							results.append(s + "\n");
							counter++;
							if(counter >= 1000) {
								break;
							}
						}
						// Printing ending statement
						results.append("End of Vocabulary - Counter: " + counter + "\n ");
					}
					// If first word in string array is ':stem'
					// Demos the stemming of 1 word
					else if(special[0].equals(":stem")) {
						Normalize normal = new Normalize(langOp);
						List<String> normalized = normal.processToken(special[1]);
						String stemmedWord = "";
						for (String term: normalized) {
			                stemmedWord += " " + term;
			             }
						// Prints ending result
						results.append(stemmedWord + "\n");				
					}
					// If first word in string array is ':index'
					// Gives user an alternative method to change index
					else if(textField.getText().equals(":index")) {
						String selectNewIndex = selectDirectory();
						changeDirectory(selectNewIndex);
					}
					// If first word is ':q' quit the program
					else if(textField.getText().equals(":q")) {
						System.exit(0);
						
					}
					// If no special queries are entered run the program as usual
					else {
						int count = 0;
						BooleanQueryParser booleanQueryParser = new BooleanQueryParser();
						Normalize normalize = new Normalize(langOp);
						RankedRetrieval rankedRetrieval = new RankedRetrieval();
				        //for (Posting p : booleanQueryParser.parseQuery(query).getPostings(dpi, normalize)) { ////////MAGIC
						for (Accumalator a : rankedRetrieval.rankAlgorithm(query, dpi,langOp)) {
							/*
				        	results.append("Document ID: " + p.getDocumentId() + "\n");
				            //results.append("File Name: " + corpus.getDocument(p.getDocumentId()).getFileName() + "\n");
				            //results.append("Title: " + corpus.getDocument(p.getDocumentId()).getTitle() + "\n");
				            results.append("Positions: " + p.getPositions() + "\n");
				            results.append("\n");
				            */
				            
							count++;
							results.append("Doc ID: " + a.getDocID()+ " -- " + a.getAccumulator() + "\n");
						}
				        // Prints the amount of results returned
						results.append("Returned: " + count + "\n");
						
					}
				}
				
			});
			
			results.setEditable(false);
			// Adds GUI elements to panel and frame
			panel.add(textField);
			panel.add(search);
			panel.add(browse);
			panel.add(options);
			panel.add(scrollPane);
			frame.add(panel);
			frame.setSize(800, 400);
			frame.getRootPane().setDefaultButton(search);
			
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			}
		});
	}

	/** 
	 * Allows user to select from a directory on disk
	 * @return the directory
	 */
	public String selectDirectory() {
		JFileChooser chooser = new JFileChooser();
		String temp = null;
		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Select Your Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {			
			File currentDirectory = chooser.getSelectedFile();
			
			uri = currentDirectory.toURI();
			// Converting uri to a string
			temp = uri.toString();
			
			String file = "file:/";
			// Remove 'file:/' from string
			temp = temp.replaceAll(file, "");
			
		}
		//if no file was chosen
		else {
			System.out.println("No Selection ");
		}
		return temp;
	}

	
	/** 
	 * Changes directory to new user selected directory by updating corpus and index
	 * @param directory the directory
	 */
	public void changeDirectory(String directory) {
		String temp = JOptionPane.showInputDialog("Please input language in use");
		if(temp.equals("english")||temp.equals("English"))
			langOp="en";
		else if(temp.equals("french")||temp.equals("French"))
			langOp="fr";
		if(jsonFiles(this.directory).get(1).endsWith(".json"))
			this.corpus = DirectoryCorpus.loadJsonDirectory(Paths.get(this.directory).toAbsolutePath(), ".json");// THIS IS FOR .json FILES
		else if(!jsonFiles(this.directory).get(1).endsWith(".json"))
			this.corpus = DirectoryCorpus.loadTextDirectory(Paths.get(this.directory).toAbsolutePath(), ".txt"); // THIS IS FOR .txt FILES
	    this.index = indexCorpus(corpus);
	}
	
	/** 
	 * Creates a PositionalInvertedIndex 
	 * @param corpus the DocumentCorpus
	 * @return a PositionalInvertedIndex object
	 */
    private static PositionalInvertedIndex indexCorpus(DocumentCorpus corpus) {
    	// Display dialog box to user when indexCorpus is ran
    	JOptionPane.showMessageDialog(null, "Indexing Please Wait...");
    	PositionalInvertedIndex pInvIdx = new PositionalInvertedIndex(); // Positional Inverted index
        Iterable<Document> documentsIterable = corpus.getDocuments(); //Make documents iterable
        Normalize normalize = new Normalize(langOp); //WORD STEMMING
        HashSet<String> vocabulary = new HashSet<>();
        // Goes through the documents in the corpus
        for (Document doc : documentsIterable) {
            EnglishTokenStream ets = new EnglishTokenStream(doc.getContent());

            int count = 0; //Keep track of position
            for (String str : ets.getTokens()) {
            	// Process strings
                List<String> terms = normalize.processToken(str);
                // Go through processed terms list and add each term to vocabulary and to the index	
                for (String term: terms) {
                    vocabulary.add(term);
                    pInvIdx.addTerm(term, doc.getId(), count);
                    count++;
                    
                }
            }
            try {
                ets.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pInvIdx;
	}
	
    /** 
     * returns an array list of files in directory
     * @param directory Directory of where files are located
     */
    private static List<String> jsonFiles(String directory) {
    	  List<String> textFiles = new ArrayList<String>();
    	  File dir = new File(directory);
    	  int i = 0;
    	  while(i < 1) {
    	  for (File file : dir.listFiles()) {
    	    if (file.getName().endsWith((".json"))) {
    	      textFiles.add(file.getName());
    	      break;
    	    }
    	    else {
    	    	textFiles.add(file.getName());
    	    	break;
    	    }
    	  }
    	  i++;
    	}
		return textFiles;
    }

}
