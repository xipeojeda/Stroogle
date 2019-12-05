package cecs429.classification;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class FederalistsGUI extends javax.swing.JFrame{
	    private JayDocs jDocs;
	    private MadisonDocs mDocs;
	    private HamiltonDocs hDocs;
	    private ArrayList<Integer> disputedDocs;
	    private ArrayList<String> disputedDocsTerms;
	    private HashMap<Integer, ArrayList<String>> disputedDocsMap;
	    private DiskPositionalIndex dindex;
	    private HashMap<Integer, double[]> documentVectors;
	    private double[] evidence;
	    private ArrayList<String> evidenceTerms;
	    
	    private javax.swing.JButton bayesianButton;
	    private javax.swing.JFileChooser directoryChooser;
	    private javax.swing.JMenu fileMenu;
	    private javax.swing.JMenuBar fileMenuBar;
	    private javax.swing.JLabel hamiltonFileLabel;
	    private javax.swing.JLabel hamiltonLabel;
	    private javax.swing.JPanel hamiltonPanel;
	    private javax.swing.JScrollPane hamiltonScrollPane;
	    private javax.swing.JTable hamiltonTable;
	    private javax.swing.JLabel jayFileLabel;
	    private javax.swing.JLabel jayLabel;
	    private javax.swing.JPanel jayPanel;
	    private javax.swing.JScrollPane jayScrollPane;
	    private javax.swing.JTable jayTable;
	    private javax.swing.JLabel madisonFileLabel;
	    private javax.swing.JLabel madisonLabel;
	    private javax.swing.JPanel madisonPanel;
	    private javax.swing.JScrollPane madisonScrollPane;
	    private javax.swing.JTable madisonTable;
	    private javax.swing.JPanel mainPanel;
	    private javax.swing.JMenuItem openExistingMenuItem;
	    private javax.swing.JMenuItem openMenuItem;
	    private javax.swing.JButton rocchioButton;
	
    public FederalistsGUI() {
        initComponents();
    }
/*
 * GUI STUFF BELOW
 */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        directoryChooser = new javax.swing.JFileChooser();
        mainPanel = new javax.swing.JPanel();
        hamiltonPanel = new javax.swing.JPanel();
        hamiltonScrollPane = new javax.swing.JScrollPane();
        hamiltonTable = new javax.swing.JTable();
        madisonPanel = new javax.swing.JPanel();
        madisonScrollPane = new javax.swing.JScrollPane();
        madisonTable = new javax.swing.JTable();
        jayPanel = new javax.swing.JPanel();
        jayScrollPane = new javax.swing.JScrollPane();
        jayTable = new javax.swing.JTable();
        rocchioButton = new javax.swing.JButton();
        bayesianButton = new javax.swing.JButton();
        hamiltonLabel = new javax.swing.JLabel();
        madisonLabel = new javax.swing.JLabel();
        jayLabel = new javax.swing.JLabel();
        hamiltonFileLabel = new javax.swing.JLabel();
        madisonFileLabel = new javax.swing.JLabel();
        jayFileLabel = new javax.swing.JLabel();
        fileMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        openExistingMenuItem = new javax.swing.JMenuItem();

        directoryChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Federalists Paper Stroogle Engine");
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        hamiltonTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Papers"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        hamiltonTable.getSelectionModel().addListSelectionListener(new
            ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int row = hamiltonTable.getSelectedRow();

                    if(row >= 0 && !e.getValueIsAdjusting() && !hamiltonTable.getSelectionModel()
                        .isSelectionEmpty()) {
                        String file = hamiltonTable.getValueAt(row, 0).toString();

                    
                    }
                }
            });
            hamiltonScrollPane.setViewportView(hamiltonTable);

            javax.swing.GroupLayout hamiltonPanelLayout = new javax.swing.GroupLayout(hamiltonPanel);
            hamiltonPanel.setLayout(hamiltonPanelLayout);
            hamiltonPanelLayout.setHorizontalGroup(
                hamiltonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(hamiltonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            hamiltonPanelLayout.setVerticalGroup(
                hamiltonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(hamiltonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            madisonTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Papers"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            madisonTable.getSelectionModel().addListSelectionListener(new
                ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int row = madisonTable.getSelectedRow();

                        if(row >= 0 && !e.getValueIsAdjusting() && !madisonTable.getSelectionModel()
                            .isSelectionEmpty()) {
                            String file = madisonTable.getValueAt(row, 0).toString();

                          
                        }
                    }
                });
                madisonScrollPane.setViewportView(madisonTable);

                javax.swing.GroupLayout madisonPanelLayout = new javax.swing.GroupLayout(madisonPanel);
                madisonPanel.setLayout(madisonPanelLayout);
                madisonPanelLayout.setHorizontalGroup(
                    madisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(madisonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                );
                madisonPanelLayout.setVerticalGroup(
                    madisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(madisonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                );

                jayTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "Papers"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                jayTable.getSelectionModel().addListSelectionListener(new
                    ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            int row = jayTable.getSelectedRow();

                            if(row >= 0 && !e.getValueIsAdjusting() && !jayTable.getSelectionModel()
                                .isSelectionEmpty()) {
                                String file = jayTable.getValueAt(row, 0).toString();

                           
                            }
                        }
                    });
                    jayScrollPane.setViewportView(jayTable);

                    javax.swing.GroupLayout jayPanelLayout = new javax.swing.GroupLayout(jayPanel);
                    jayPanel.setLayout(jayPanelLayout);
                    jayPanelLayout.setHorizontalGroup(
                        jayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jayScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    );
                    jayPanelLayout.setVerticalGroup(
                        jayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jayScrollPane)
                    );

                    rocchioButton.setText("Rocchio Classification");
                    rocchioButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            rocchioButtonActionPerformed(evt);
                        }
                    });

                    bayesianButton.setText("Bayesian Classification");
                    bayesianButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            bayesianButtonActionPerformed(evt);
                        }
                    });

                    hamiltonLabel.setText("Hamilton:");

                    madisonLabel.setText("Madison:");

                    jayLabel.setText("Jay:");

                    hamiltonFileLabel.setText("Documents found: ");

                    madisonFileLabel.setText("Documents found:");

                    jayFileLabel.setText("Documents found:");

                    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
                    mainPanel.setLayout(mainPanelLayout);
                    mainPanelLayout.setHorizontalGroup(
                        mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(hamiltonLabel)
                                        .addComponent(hamiltonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(hamiltonFileLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(madisonLabel)
                                        .addComponent(madisonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(madisonFileLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jayFileLabel)
                                        .addComponent(jayLabel)
                                        .addComponent(jayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addGap(125, 125, 125)
                                    .addComponent(rocchioButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(bayesianButton)))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    mainPanelLayout.setVerticalGroup(
                        mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(hamiltonLabel)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(madisonLabel)
                                    .addComponent(jayLabel)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(madisonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hamiltonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jayPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(hamiltonFileLabel)
                                .addComponent(madisonFileLabel)
                                .addComponent(jayFileLabel))
                            .addGap(18, 18, 18)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rocchioButton)
                                .addComponent(bayesianButton))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    getContentPane().add(mainPanel);

                    fileMenu.setText("File");

                    openMenuItem.setText("Open Directory");
                    openMenuItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            openMenuItemActionPerformed(evt);
                        }
                    });
                    fileMenu.add(openMenuItem);

                    openExistingMenuItem.setText("Open Existing Files");
                    openExistingMenuItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            openExistingMenuItemActionPerformed(evt);
                        }
                    });
                    fileMenu.add(openExistingMenuItem);

                    fileMenuBar.add(fileMenu);

                    setJMenuBar(fileMenuBar);

                    pack();
    }
    /*
     * allows user to select a the path and indexes displays message to user when complete
     */
    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                DiskIndexWriter index = new DiskIndexWriter();
                String currentDir = directoryChooser.getSelectedFile()
                        .toString();
                index.buildIndex(currentDir);
                dindex = new DiskPositionalIndex(currentDir);
                initializeClasses(currentDir);
                
                JOptionPane.showMessageDialog(this, "Indexing Complete"
                        + currentDir + " files.", "Indexed",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(FederalistsGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    /**
     * Updates files to display
     * @param table the table to update
     * @param files arraylist of ints that holds the files
     * @param label shows the docs that were found
     */
    private void displayFiles(JTable table, ArrayList<Integer> files, JLabel label) {
        String columnNames[] = new String[] {"Papers"};
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        model.setColumnCount(1);
        model.setColumnIdentifiers(columnNames);
        
        for(Integer file : files) {
            model.addRow(new Object[] {dindex.getFileNames(file)});
        }
        
        label.setText("Documents found: " + files.size());
    }
    /*
     * gives user to open an existing index that has already been written to disk
     */
    private void openExistingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String currentDir = directoryChooser.getSelectedFile().toString();
                dindex = new DiskPositionalIndex(currentDir);
                initializeClasses(currentDir);

                JOptionPane.showMessageDialog(this, "Successfully indexed "
                        + currentDir + " files.", "Indexed",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(FederalistsGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }

    /*
     * END OF GUI STUFF
     */
    
    /*
     * displays rocchio results to user when they click rocchio button
     */
    private void rocchioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (dindex == null) 
            JOptionPane.showMessageDialog(this, "Index is null.", "Index error!", JOptionPane.ERROR);
        else {
        	//get centroid for each doc type
            double[] hCentroid = hDocs.getCentroid();
            double[] mCentroid = mDocs.getCentroid();
            double[] jCentroid = jDocs.getCentroid();
            ArrayList<Integer> hDocList = new ArrayList<>();
            hDocList.addAll(hDocs.getFiles());
            ArrayList<Integer> mDocList = new ArrayList<>();
            mDocList.addAll(mDocs.getFiles());
            ArrayList<Integer> jDocList = new ArrayList<>();
            jDocList.addAll(jDocs.getFiles());
            String results = "";
            //looping through disputed docs calculate closes centroid and display results
            for(int doc : disputedDocs) {
                double[] docVector = documentVectors.get(doc);
                double hResults = calculateClosestCentroid(hCentroid, docVector);
                double mResults = calculateClosestCentroid(mCentroid, docVector);
                double jResults = calculateClosestCentroid(jCentroid, docVector);
                results += String.format("In the case of %s who is the founding father : ham: %.5f    mad: %.5f    jay: %.5f\n",
                        dindex.getFileNames(doc), hResults, mResults, jResults);
                
                //add disputed docs to correct type
               if(hResults < mResults) {
                    if(hResults < jResults) {
                        hDocList.add(doc);
                    }
                    else {
                        jDocList.add(doc); 
                    }
                }
                else {
                    if(mResults < jResults) {
                        mDocList.add(doc);
                    }
                    else {
                        jDocList.add(doc);
                    }
                }
            }           
            JOptionPane.showMessageDialog(this, results, "Rocchio Maury Show", JOptionPane.INFORMATION_MESSAGE);
            displayFiles(hamiltonTable, hDocList, hamiltonFileLabel);
            displayFiles(madisonTable, mDocList, madisonFileLabel);
            displayFiles(jayTable, jDocList, jayFileLabel);
        }
    }
	/*
	 * displays bayeseian results to user
	 */
    private void bayesianButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (dindex == null)
            JOptionPane.showMessageDialog(this, "Index is null.", "Index error!", JOptionPane.ERROR);
        else {
            int docCount = dindex.getDocumentCount();
            int termCount = dindex.getTermCount();
            ArrayList<Integer> hDocList = new ArrayList<>();
            hDocList.addAll(hDocs.getFiles());
            ArrayList<Integer> mDocList = new ArrayList<>();
            mDocList.addAll(mDocs.getFiles());
            ArrayList<Integer> jDocList = new ArrayList<>();
            jDocList.addAll(jDocs.getFiles());
            String results = "";
            
            //calculatations methods
            getMutualInfoScores();
            getEvidenceVector(50);
            selectEvidence();
            getFrequencyTerms();
            trainBayesianClassifier();
            
            ArrayList<Double> ptchList = new ArrayList<>();
            ptchList.addAll(hDocs.getPtch());
            ArrayList<Double> ptcjList = new ArrayList<>();
            ptcjList.addAll(jDocs.getPtcj());
            ArrayList<Double> ptcmList = new ArrayList<>();
            ptcmList.addAll(mDocs.getPtcm());
            //display results add to correct type
            for(int doc : disputedDocs) {
                double hSum = 0.0, mSum = 0.0, jSum = 0.0;
                
                if (disputedDocsMap.containsKey(doc)) {
                    ArrayList<String> terms = disputedDocsMap.get(doc);

                    for (String term : terms) {
                        int index = evidenceTerms.indexOf(term);
                        hSum += Math.log(ptchList.get(index));
                        mSum += Math.log(ptcmList.get(index));
                        jSum += Math.log(ptcjList.get(index));
                    }

                    hSum += Math.log((double) hDocs.getFiles().size() / docCount);
                    mSum += Math.log((double) mDocs.getFiles().size() / docCount);
                    jSum += Math.log((double) jDocs.getFiles().size() / docCount);

                    if (hSum > mSum) {
                        if (hSum > jSum) {
                            hDocList.add(doc);
                        }
                        else {
                            jDocList.add(doc);
                        }
                    } 
                    else if (mSum > jSum) {
                        mDocList.add(doc);
                    } 
                    else {
                        jDocList.add(doc);
                    }

                    results += String.format(" In the case of %s who is the founding father : ham: %.5f    mad: %.5f    jay: %.5f\n",
                        dindex.getFileNames(doc), hSum, mSum, jSum);
                }
            }
            
            JOptionPane.showMessageDialog(this, results, "Bayesian Maury Show",
            JOptionPane.INFORMATION_MESSAGE);
            displayFiles(hamiltonTable, hDocList, hamiltonFileLabel);
            displayFiles(madisonTable, mDocList, madisonFileLabel);
            displayFiles(jayTable, jDocList, jayFileLabel);
        }
    }

    /*
     * reads from disk positional index for each folder path
     * and initializes each class type (e.g. Hamilton, Madison, Jay)
     */
	private void initializeClasses(String path) {
        try {
            ArrayList<String> files;
            ArrayList<Integer> docIDs;
            int size = dindex.getTermCount();
            
            initializeDocumentVectors();
            
            files = DiskPositionalIndex.readFileNames(path + "\\JAY");
            docIDs = getDocIdList(files);
            jDocs = new JayDocs(docIDs, size);
            jDocs.setCentroid(calculateCentroid(jDocs.getFiles()));
            
            files = DiskPositionalIndex.readFileNames(path + "\\MADISON");
            docIDs = getDocIdList(files);
            mDocs = new MadisonDocs(docIDs, size);
            mDocs.setCentroid(calculateCentroid(mDocs.getFiles()));
            
            files = DiskPositionalIndex.readFileNames(path + "\\HAMILTON");
            docIDs = getDocIdList(files);
            hDocs = new HamiltonDocs(docIDs, size);
            hDocs.setCentroid(calculateCentroid(hDocs.getFiles()));
            
            files = DiskPositionalIndex.readFileNames(path + "\\DISPUTED");
            disputedDocs = getDocIdList(files);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
    /**
     * Converts doc ids to a usable arraylists of docIDs using arraylist of files
     */
    private ArrayList<Integer> getDocIdList(ArrayList<String> files) {
        ArrayList<String> allFiles = dindex.getFileNameList();
        ArrayList<Integer> docIDs = new ArrayList<>();
        
        for(int i = 0; i < files.size(); i++) {
            for(int j = 0; j < allFiles.size(); j++) {
                if(files.get(i).compareTo(allFiles.get(j)) == 0) {
                    docIDs.add(j);
                    break;
                }
            }
        }
        
        return docIDs;
    }
    
    /**

     * Initializes doc vectors by creating a hashmap, then loops through each term
     * in the index then looping through each posting and creates a vector for it
     */
    private void initializeDocumentVectors() {
        documentVectors = new HashMap<>(dindex.getDocumentCount());
        ArrayList<String> terms = dindex.getPositionalIndexTerms();
        int i = 0;
        for(String s: terms) {
            List<Posting> postings = dindex.getPostings(s, true);
            
            for(Posting posting : postings) {
                double ld = dindex.getDocWeight(posting.getDocumentID());
               // System.out.println("ld"+ld); LD is working fine
                double[] wdts;
                
                if(documentVectors.containsKey(posting.getDocumentID())) {
                    wdts = documentVectors.get(posting.getDocumentID());
                }
                else {
                    wdts = new double[terms.size()];
                }
                //added 1 because of 0 values THIS IS NOT THE CORRECT DID THIS FOR TESTING
                double tftd = posting.getPositions().size();
                if(tftd == 0) {
                	break;
                }
               
                double wdt = 1 + Math.log(tftd);//which gives us oof numbers
               // System.out.println("wdt"+wdt);
                wdts[i] = wdt / ld;
                documentVectors.put(posting.getDocumentID(), wdts);
               
            }
            i++;
        }
        /*prints terms from paper_52.txt
       for(String s: terms) {
    	   ArrayList<Posting> postings = dindex.getPostings(s, true);
    	   for(Posting p: postings) {
    		   if(p.getDocumentID() == 74) {
    			   System.out.println(s);
    		   }
    	   }
       }*/
    }
    
    /**
     * Calculates the centroid of the class by adding its doc vectors
     * then dividing each value in the vector by dc value
     */
    private double[] calculateCentroid(ArrayList<Integer> files) {
        double[] results = documentVectors.get(files.get(0));
        double dc = files.size();
        
        for(int i = 1; i < files.size(); i++) {
            double[] docVec = documentVectors.get(files.get(i));
            //System.out.println(docVec[i]);
            for(int j = 0; j < results.length; j++) {
                results[j] += docVec[j];
                //System.out.println("results" + results[j]);
            }
        }        
        
        for(int i = 0; i < results.length; i++) {
            results[i] = results[i] / dc;
           // System.out.println("results22" + results[i]);
        }
        return results;
    }
    
    /**
     * Subtract class centroid with doc vector then square the value
     * sum them up and take the square root
     */
    private double calculateClosestCentroid(double[] mCentroid, double[] doc) {
        double sum = 0.0;
        
        for(int i = 0; i < mCentroid.length; i++) {
            sum += Math.pow(mCentroid[i] - doc[i], 2);
        }
        
        return Math.sqrt(sum);
    }
    
    /**
     * Loop through each term and get the positional postings for each term
     * to use to calculate the mutual information score
     */
    private void getMutualInfoScores() {
        ArrayList<String> terms = dindex.getPositionalIndexTerms();

        for(int i = 0; i < terms.size(); i++) {
            ArrayList<Posting> postings = dindex.getPostings(terms.get(i), true);
            calculateMutualInfoScores(postings, i);
        }
    }
    
    
    /**
     *Calculates mutual scores using postings, and int index for each father
     */
    private void calculateMutualInfoScores(ArrayList<Posting> postings, int index) {
        int jN11 = 0, hN11 = 0, mN11 = 0;
        int jN01, hN01, mN01;
        int jN10, hN10, mN10;
        int jN00, hN00, mN00;
        ArrayList<Integer> hDocsFiles = hDocs.getFiles();
        ArrayList<Integer> mDocsFiles = mDocs.getFiles();
        ArrayList<Integer> jDocsFiles = jDocs.getFiles();
        int docCount = dindex.getDocumentCount();
        
        for (Posting posting : postings) {
            int docID = posting.getDocumentID();
            
            if (hDocsFiles.contains(docID)) {
                hN11++;
            } 
            else if (mDocsFiles.contains(docID)) {
                mN11++;
            } 
            else if (jDocsFiles.contains(docID)) {
                jN11++;
            }
        }

        jN01 = jDocs.getFiles().size() - jN11;
        mN01 = mDocs.getFiles().size() - mN11;
        hN01 = hDocs.getFiles().size() - hN11;

        jN10 = hN11 + mN11;
        hN10 = jN11 + mN11;
        mN10 = jN11 + hN11;

        jN00 = docCount - jN11 - jN01 - jN10;
        hN00 = docCount - hN11 - hN01 - hN10;
        mN00 = docCount - mN11 - mN01 - mN10;
        


        double jItcScore = calculateItc((double) jN11, (double) jN01,
                (double) jN10, (double) jN00);
        double mItcScore = calculateItc((double) mN11, (double) mN01,
                (double) mN10, (double) mN00);
        double hItcScore = calculateItc((double) hN11, (double) hN01,
                (double) hN10, (double) hN00);

        double[] jItc = jDocs.getItc();
        jItc[index] = jItcScore;
        jDocs.setItc(jItc);
        double[] mItc = mDocs.getItc();
        mItc[index] = mItcScore;
        mDocs.setItc(mItc);
        double[] hItc = hDocs.getItc();
        hItc[index] = hItcScore;
        hDocs.setItc(hItc);
    }
    
    /**
     * Calculate the mutual information score based on the I(t,c) formula
     */
    private double calculateItc(double N11, double N01, double N10, double N00) {
        double N = (double)dindex.getDocumentCount();
        double w, x, y, z;

        w = (N11/N)*(Math.log10((N*N11)/((N11+N10)*(N11+N01)))/Math.log10(2.0));

        if(Double.isNaN(w)) {
            w = 0.0;
        }
        
        x = (N01/N)*(Math.log10((N*N01)/((N01+N00)*(N11+N01)))/Math.log10(2.0));       
        
        if(Double.isNaN(x)) {
            x = 0.0;
        }
        
        y = (N10/N)*(Math.log10((N*N10)/((N11+N10)*(N10+N00)))/Math.log10(2.0));
        
        if(Double.isNaN(y)) {
            y = 0.0;
        }
        
        z = (N00/N)*(Math.log10((N*N00)/((N01+N00)*(N10+N00)))/Math.log10(2));       
        
        if(Double.isNaN(z)) {
            z = 0.0;
        }
        
        return w+x+y+z;
    }
    
    /**
     * Get the max score of the 3 classes
     */
    private void getEvidenceVector(int size) {
        double[] jScores = jDocs.getItc();
        double[] mScores = mDocs.getItc();
        double[] hScores = hDocs.getItc();
        evidence = new double[size];
        
        for(int i = 0; i < size; i++) {
            if(mScores[i] > hScores[i]) {
                if(mScores[i] > jScores[i]) {
                    evidence[i] = mScores[i];
                }
                else {
                    evidence[i] = jScores[i];
                }
            }
            else {
                if(hScores[i] > jScores[i]) {
                    evidence[i] = hScores[i];
                }
                else {
                    evidence[i] = jScores[i];
                }
            }
        }
    }
    
    /**
     * Selects the 50 terms for evidence to use
     */
    private void selectEvidence() {
        evidenceTerms = new ArrayList<>();
        ArrayList<String> terms = dindex.getPositionalIndexTerms();
        
        for(int i = 0; i < evidence.length; i++) {
           //if(evidence[i] > value) to get highest scores
        		evidenceTerms.add(terms.get(i));
                
              
        }  
    }
    
    /**
     * Loops through the discriminating set of vocabulary terms and 
     * gets the frequency of the term found in document if the document is part
     * of the class. Also if the term is found in the disputed documents then
     * add that document and the words to the hash map.
     */
    private void getFrequencyTerms() {
        disputedDocsTerms = new ArrayList<>();
        disputedDocsMap = new HashMap<>();
        HashMap<String, Integer> hFtcMap = new HashMap<>();
        HashMap<String, Integer> mFtcMap = new HashMap<>();
        HashMap<String, Integer> jFtcMap = new HashMap<>();
        int hFtcSum = 0, mFtcSum = 0, jFtcSum = 0;
        ArrayList<Integer> hDocsFiles = hDocs.getFiles();
        ArrayList<Integer> mDocsFiles = mDocs.getFiles();
        ArrayList<Integer> jDocsFiles = jDocs.getFiles();
        
        for(String term : evidenceTerms) {
            ArrayList<Posting> postings = dindex.getPostings(term, true);
            
            for (Posting posting : postings) {
                int docID = posting.getDocumentID();
                int termFreq = posting.getTermFreq();
                
                if (hDocsFiles.contains(docID)) {
                    if (hFtcMap.containsKey(term)) {
                        int ftc = hFtcMap.get(term);
                        hFtcMap.put(term, ftc + termFreq);
                    }
                    else {
                        hFtcMap.put(term, termFreq);
                    }

                    hFtcSum += termFreq;
                }
                else if (mDocsFiles.contains(docID)) {
                    if (mFtcMap.containsKey(term)) {
                        int ftc = mFtcMap.get(term);
                        mFtcMap.put(term, ftc + termFreq);
                    } 
                    else {
                        mFtcMap.put(term, termFreq);
                    }

                    mFtcSum += termFreq;
                } 
                else if (jDocsFiles.contains(docID)) {
                    if (jFtcMap.containsKey(term)) {
                        int ftc = jFtcMap.get(term);
                        jFtcMap.put(term, ftc + termFreq);
                    } 
                    else {
                        jFtcMap.put(term, termFreq);
                    }

                    jFtcSum += termFreq;
                }
                else if(disputedDocs.contains(docID)) {
                    if(!disputedDocsTerms.isEmpty()) {
                        if(disputedDocsTerms.get(disputedDocsTerms.size()-1)
                                .compareTo(term) != 0) {
                            disputedDocsTerms.add(term);
                        }
                    }
                    else {
                        disputedDocsTerms.add(term);
                    }
                    if(!disputedDocsMap.containsKey(docID)) {
                        ArrayList<String> docTerms = new ArrayList<>();
                        docTerms.add(term);
                        disputedDocsMap.put(docID, docTerms);
                    }
                    else {
                        ArrayList<String> docTerms = disputedDocsMap
                                .get(posting.getDocumentID());
                        
                        if(!docTerms.contains(term)) {
                            docTerms.add(term);
                            disputedDocsMap.put(docID, docTerms);
                        }
                    }
                }
            }
        }
        
        jDocs.setFtcSum(jFtcSum);
        hDocs.setFtcSum(hFtcSum);
        mDocs.setFtcSum(mFtcSum);
        
        jDocs.setFtcMap(jFtcMap);
        hDocs.setFtcMap(hFtcMap);
        mDocs.setFtcMap(mFtcMap);
    }
    
    /**
     * Calculates the probabilities that the term from the discriminating set
     * appears in each class by calculating p(t|c) using laplace smoothing
     */
    private void trainBayesianClassifier() {
        ArrayList<Double> ptchList = new ArrayList<>();
        ArrayList<Double> ptcmList = new ArrayList<>();
        ArrayList<Double> ptcjList = new ArrayList<>();
        HashMap<String, Integer> hFtcMap = hDocs.getFtcMap();
        HashMap<String, Integer> jFtcMap = jDocs.getFtcMap();
        HashMap<String, Integer> mFtcMap = mDocs.getFtcMap();
        double hFtcSum = hDocs.getFtcSum();
        double mFtcSum = mDocs.getFtcSum();
        double jFtcSum = jDocs.getFtcSum();
        double tSize = evidenceTerms.size();

        for(String term : evidenceTerms) {
            double hFtc = 0.0, mFtc = 0.0, jFtc = 0.0;
            
            if(hFtcMap.containsKey(term)) {
                hFtc = hFtcMap.get(term);
            }
            if(mFtcMap.containsKey(term)) {
                mFtc = mFtcMap.get(term);
            }
            if(jFtcMap.containsKey(term)) {
                jFtc = jFtcMap.get(term);
            }
            
            double ptch = (hFtc + 1)/(tSize + hFtcSum);
            double ptcm = (mFtc + 1)/(tSize + mFtcSum);
            double ptcj = (jFtc + 1)/(tSize + jFtcSum);

            ptchList.add(ptch);
            ptcmList.add(ptcm);
            ptcjList.add(ptcj);
        }
        
        jDocs.setPtcj(ptcjList);
        hDocs.setPtch(ptchList);
        mDocs.setPtcm(ptcmList);
    }
   
}
