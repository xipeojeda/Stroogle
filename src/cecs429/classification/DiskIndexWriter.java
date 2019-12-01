package cecs429.classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cecs429.documents.TextFileDocument;
import cecs429.text.EnglishTokenStream;
import cecs429.text.Normalize;

public class DiskIndexWriter {
	private ArrayList<Integer> docLengthList;
	private PositionalInvertedIndex mIndex;
	private int mDocID = 0;
	
	public DiskIndexWriter() {
		mIndex = new PositionalInvertedIndex();
		docLengthList = new ArrayList<>();
	}
	/** 
	 * Build index for the given directory
	 * @param index the given index
	 * @param folderPath the folder path
	 */
	public void buildIndex(String path) {
        // Index the directory using a naive index
        indexFiles(path + "\\ALL", path);
        indexFiles(path + "\\DISPUTED", path);
        addAvgDocLength(path);
        String[] dictionary = mIndex.getVocabulary();
        // an array of positions in the vocabulary file
        long[] vPos = new long[dictionary.length];
        buildVocabFile(path, dictionary, vPos, "vocab.bin");
        buildPostingFile(path, dictionary, vPos);
	}
	
	/**
	 * Builds the posting file
	 * @param folderPath the folder path
	 * @param index the given index
	 * @param vocab list of vocabulary terms
	 * @param vPos the positions of vocab
	 */
    private void buildPostingFile(String folder, String[] dictionary, long[] vPos) {
        FileOutputStream postingsFile = null;
        
        try {
            postingsFile = new FileOutputStream(new File(folder, "postings.bin"));
            FileOutputStream vocabTable = new FileOutputStream(new File(folder, "vocabTable.bin"));

            byte[] tSize = ByteBuffer.allocate(4).putInt(dictionary.length).array();
            vocabTable.write(tSize, 0, tSize.length);
            int vocabI = 0;

            for (String s : dictionary) {
             
                List<Posting> postings = mIndex.getPosting(s);

                byte[] vPosBytes = ByteBuffer.allocate(8).putLong(vPos[vocabI]).array();
                vocabTable.write(vPosBytes, 0, vPosBytes.length);

                byte[] pPosBytes = ByteBuffer.allocate(8).putLong(postingsFile.getChannel().position()).array();
                vocabTable.write(pPosBytes, 0, pPosBytes.length);

                byte[] docFreqBytes = ByteBuffer.allocate(4).putInt(postings.size()).array();
                postingsFile.write(docFreqBytes, 0, docFreqBytes.length);

               
                //int lastDocID = 0;
                int lastPos = 0;
                for (Posting p : postings) {
                    byte[] docIdBytes = ByteBuffer.allocate(4).putInt(p.getDocumentID()).array();
                    postingsFile.write(docIdBytes, 0, docIdBytes.length);
                    //lastDocID = p.getDocumentID();
                    
                    int termFrequency = p.getPositions().size();
                    byte[] termFreqBytes = ByteBuffer.allocate(4).putInt(termFrequency).array();
                    postingsFile.write(termFreqBytes, 0, termFreqBytes.length);
                    
                    for (Integer pos : p.getPositions()) {
                        byte[] positionBytes = ByteBuffer.allocate(4).putInt(pos - lastPos).array();
                        postingsFile.write(positionBytes, 0, positionBytes.length);
                        lastPos = pos;
                    }
                }

                vocabI++;
            }
            vocabTable.close();
            postingsFile.close();
        } 
        catch (IOException ex) {System.out.println(ex.toString());}
        finally {
            try {
                postingsFile.close();
            }
            catch (IOException ex) {System.out.println(ex.toString());}
        }
    } 
	/** 
	 * Builds the vocab file
	 * @param folderPath the folder path
	 * @param index the given index
	 * @param vocab list of vocabulary terms
	 * @param vPos the positions of vocab
	 * @param file the file
	 */
    public void buildVocabFile(String folder, String[] dictionary, long[] vPos, String file) {
        OutputStreamWriter vocabList = null;
        try {
            int vocabI = 0;
            vocabList = new OutputStreamWriter(new FileOutputStream(new File(folder, file)), "UTF-8"
            );

            int vocabPos = 0;
            for (String vocabWord : dictionary) {
            	vPos[vocabI] = vocabPos;
                vocabList.write(vocabWord);
                vocabI++;
                vocabPos += vocabWord.length();
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } finally {
            try {
                vocabList.close();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
        }
    }
	/** 
	 * Create index file
	 * @param path the path
	 * @param vocab list of vocabulary terms
	 * @param index the given index
	 */
    private void indexFiles(String folder, String mainDirectory) {
        final Path currentWorkingPath = Paths.get(folder).toAbsolutePath();
        
        try {
            Files.walkFileTree(currentWorkingPath,new SimpleFileVisitor<Path>() {

                public FileVisitResult preVisitDirectory(Path dir,
                        BasicFileAttributes attrs) {
                    // make sure we only process the current working directory
                    if (currentWorkingPath.equals(dir)) {
                        return FileVisitResult.CONTINUE;
                    }
                    // process the current working directory and subdirectories
                    return FileVisitResult.SKIP_SUBTREE;
                }

                public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attrs) {
                    // looking to process only .txt files
                    if (file.toString().endsWith(".txt")) {
                    	//indexing .txt files
                    	indexTxtFile(file.toFile(), mDocID, folder, mainDirectory);
                        mDocID++;
                    }
                    return FileVisitResult.CONTINUE;
                }
            	// don't throw exceptions if files are locked/other errors occur
                public FileVisitResult visitFileFailed(Path file,IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
	/** 
	 * Indexes .txt files to positional inverted index
	 * @param file the file
	 * @param index the index
	 * @param vocab the vocabulary
	 * @param docID the id of the document
	 * @param fileType the file type
	 */
    private void indexTxtFile(File file, int docID, String folder, String mainDirectory) {
        HashMap<String, Integer> termFreqMap = new HashMap<>();
		List<String> terms = new ArrayList<String>();
		TextFileDocument txtDoc = new TextFileDocument(docID, Paths.get(file.getAbsolutePath()));
		EnglishTokenStream etsTxt = new EnglishTokenStream(txtDoc.getContent());
		Normalize normal = new Normalize("en");
		int position = 0;
		for(String str: etsTxt.getTokens()) {
			terms = normal.processToken(str);
			
			for(String term: terms) {
				mIndex.addTerm(term, docID, position);
				updateTermFreqMap(term, termFreqMap);
				position++;
			}
		}
		buildWeight(mainDirectory, termFreqMap, file.length());
    }
    
	/** 
	 * Builds the weight given a folder path
	 * @param folder the location of the folder
	 * @param index for terms
	 * @param file size
	 */
    private void buildWeight(String folder, HashMap<String, Integer> termIndex, long fileSize) {
        try {
            double wdtSum = 0.0;
            double tfsum = 0.0;
            // create a file called docWeights.bin
            File weightsFile = new File(folder, "docWeights.bin");
            FileOutputStream weightsFileOutput = null;
            
            //if files exits add to end of it
            if(weightsFile.exists()) {
                weightsFileOutput = new FileOutputStream(weightsFile, true);
            }
            // otherwise create new file
            else {
                weightsFile.createNewFile();
                weightsFileOutput = new FileOutputStream(weightsFile);
            }          
            
            // Iterate through each term sum the wdt values and square it
            for(Entry<String, Integer> term : termIndex.entrySet()) {
                tfsum += term.getValue();                
                double wdt = 1 + Math.log((double)term.getValue());                
                wdtSum += Math.pow(wdt, 2);
            }
            
            //get ld write to file
            double ld = Math.sqrt(wdtSum);
            byte[] docWeightBytes = ByteBuffer.allocate(8).putDouble(ld).array();
            weightsFileOutput.write(docWeightBytes, 0, docWeightBytes.length);
            
            //get doclength write to file
            double docLengthd = termIndex.size();
            docLengthList.add((int)docLengthd);
            byte[] docLengthBytes = ByteBuffer.allocate(8).putDouble(docLengthd).array();
            weightsFileOutput.write(docLengthBytes, 0, docLengthBytes.length);
            
            byte[] fileSizeBytes = ByteBuffer.allocate(8).putDouble((double)fileSize).array();
            weightsFileOutput.write(fileSizeBytes, 0, fileSizeBytes.length);
            
            //get avgtf write to file
            double avgtf = tfsum/termIndex.size();
            byte[] docAvgBytes = ByteBuffer.allocate(8).putDouble(avgtf).array();
            // write the buffer to the file
            weightsFileOutput.write(docAvgBytes, 0, docAvgBytes.length);
            weightsFileOutput.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
    
    /**
     * Updates the hashmap with the new term frequency
     * @param string term - represents term in index
     * @param termFreqMap - hashmap that holds term frequencies
     */
    private static void updateTermFreqMap(String term, HashMap<String, Integer> termFreqMap) {
        // check if term is in the hasmap if so replace
        if(termFreqMap.containsKey(term)) {
            termFreqMap.replace(term, termFreqMap.get(term) + 1);
        }
        // if not, insert with value of 1
        else {
            termFreqMap.put(term, 1);
        }
    }
	/*
	 *adds average doc length to doc weights file 
	 */
    private void addAvgDocLength(String folder) {
        try {
            File weightsFile = new File(folder, "docWeights.bin");
            FileOutputStream weightsFileOutput = null;

            if (weightsFile.exists())
                weightsFileOutput = new FileOutputStream(weightsFile, true);
            else {
                weightsFile.createNewFile();
                weightsFileOutput = new FileOutputStream(weightsFile);
            }
            
            int docLengthSum = 0;
            
            for(int docLen : docLengthList)
                docLengthSum += docLen;
            
            double docLengthA = (double)docLengthSum/docLengthList.size();
            byte[] docLengthBytes = ByteBuffer.allocate(8).putDouble(docLengthA)
                    .array();
            // write the buffer to the file
            weightsFileOutput.write(docLengthBytes, 0, docLengthBytes.length);
            weightsFileOutput.close();
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
