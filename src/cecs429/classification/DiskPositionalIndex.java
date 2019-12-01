package cecs429.classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiskPositionalIndex {

    private String mPath;
    private RandomAccessFile mVocabList;
    private RandomAccessFile mPostings;
    private long[] mVocabTable;
    private ArrayList<String> mFileNames = new ArrayList<>();
    private RandomAccessFile docWeights;

    public DiskPositionalIndex(String path) throws IOException {
        try {
            mPath = path;
            mVocabList = new RandomAccessFile(new File(path, "vocab.bin"), "r");
            mPostings = new RandomAccessFile(new File(path, "postings.bin"), "r");
            mVocabTable = readVocabTable(path, "vocabTable.bin");
            mFileNames = readFileNames(path + "\\ALL");
            mFileNames.addAll(readFileNames(path + "\\DISPUTED"));
            docWeights = new RandomAccessFile(new File(path, "docWeights.bin"), "r");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }

	/**
	 * Read the positional postings
	 * @param postings the postings
	 * @param position the position
	 * @return the list of postings
	 */
    private static ArrayList<Posting> readPositionalPostings (RandomAccessFile postings, long postingsPosition) {
        try {
            postings.seek(postingsPosition);

            byte[] buffer = new byte[4];
            postings.read(buffer, 0, buffer.length);
            
            int documentFrequency = ByteBuffer.wrap(buffer).getInt();
            
            int lastPosId = 0;
            ArrayList<Posting> posPostList = new ArrayList<>();
            
            for (int i = 0; i < documentFrequency; i++) {
                // list of positions
                ArrayList<Integer> positions = new ArrayList<>();

                byte[] docBuffer = new byte[4];
                postings.read(docBuffer, 0, docBuffer.length);
                int documentID = ByteBuffer.wrap(docBuffer).getInt();

                byte[] tfBuffer = new byte[4];
                postings.read(tfBuffer, 0, tfBuffer.length);
                int termFrequency = ByteBuffer.wrap(tfBuffer).getInt();

                for (int j = 0; j < termFrequency; j++) {
                    byte[] posBuffer = new byte[4];
                    postings.read(posBuffer, 0, posBuffer.length);
                    int pos = ByteBuffer.wrap(posBuffer).getInt() + lastPosId;
                    lastPosId = pos;
                    positions.add(pos);
                }

                Posting p = new Posting(documentID,termFrequency, positions);
                posPostList.add(p);
            }

            return posPostList;
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }
    
	/**
	 * Read the postings bin file
	 * @param postings the postings
	 * @param pos the position
	 * @return a list of postings
	 */
    private static ArrayList<Posting> readPostingsBin (RandomAccessFile postings, long postingsPosition) {
        try {
        	//seek to the position where postings start
            postings.seek(postingsPosition);

            //read the 4 bytes for doc freq
            byte[] buffer = new byte[4];
            postings.read(buffer, 0, buffer.length);
            
            //use ByteBuffer to convert the 4 bytes to int
            int documentFrequency = ByteBuffer.wrap(buffer).getInt();
            
            ArrayList<Posting> posPostList = new ArrayList<>();
            
            for (int i = 0; i < documentFrequency; i++) {
                byte[] docBuffer = new byte[4];
                postings.read(docBuffer, 0, docBuffer.length);
                int documentID = ByteBuffer.wrap(docBuffer).getInt();

                byte[] tfBuffer = new byte[4];
                postings.read(tfBuffer, 0, tfBuffer.length);
                int termFrequency = ByteBuffer.wrap(tfBuffer).getInt();
                
                postings.skipBytes(termFrequency*4);

                Posting p = new Posting(documentID, termFrequency);
                posPostList.add(p);
            }

            return posPostList;
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }
    
	/**
	 * Get the postings given a term and positions
	 * @param term the term
	 * @param positions the positions of the term
	 * @return the list containing the postings
	 */
    public ArrayList<Posting> getPostings(String term, boolean positions) {
        long position = binarySearchVocabulary(term, mVocabTable, mVocabList);
        if (position >= 0) {
            if(positions == true)
                return readPositionalPostings(mPostings, position);
            else
                return readPostingsBin(mPostings, position);
        }
        return null;
    }
    
    /**
     * Do a binary search to find the location of the vocab
     * @param term the term to look for
     * @param vocabTable the array containing the locations of vocabs
     * @param vocabList the file of vocab words
     * @return the long value of the location
     */
    private long binarySearchVocabulary(String term, long[] vocabTable,
            RandomAccessFile vocabList) {
        // do a binary search over the vocabulary, using the vocabTable and the file vocabList.
        int i = 0, j = vocabTable.length / 2 - 1;
        while (i <= j) {
            try {
                int m = (i + j) / 2;
                long vListPosition = vocabTable[m * 2];
                int termLength;
                if (m == vocabTable.length / 2 - 1) {
                    termLength = (int) (vocabList.length() - vocabTable[m * 2]);
                } else {
                    termLength = (int) (vocabTable[(m + 1) * 2] - vListPosition);
                }

                vocabList.seek(vListPosition);

                byte[] buffer = new byte[termLength];
                vocabList.read(buffer, 0, termLength);
                String fileTerm = new String(buffer, "ASCII");

                int compareValue = term.compareTo(fileTerm);
                if (compareValue == 0) {
                    // found it!
                    return vocabTable[m * 2 + 1];
                } else if (compareValue < 0) {
                    j = m - 1;
                } else {
                    i = m + 1;
                }
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
        return -1;
    }

	/**
	 * Read the vocab table
	 * @param indexName the name of the index
	 * @param fileName the name of the file
	 * @return the long array of the vocab
	 */
    private static long[] readVocabTable(String indexName, String fileName) {
        try {
            long[] vocabTable;

            RandomAccessFile tableFile = new RandomAccessFile( new File(indexName, fileName),"r");

            byte[] byteBuffer = new byte[4];
            tableFile.read(byteBuffer, 0, byteBuffer.length);

            int tableIndex = 0;
            vocabTable = new long[ByteBuffer.wrap(byteBuffer).getInt() * 2];
            byteBuffer = new byte[8];
          //while we keep reading 8 bytes
            while (tableFile.read(byteBuffer, 0, byteBuffer.length) > 0) {
                vocabTable[tableIndex] = ByteBuffer.wrap(byteBuffer).getLong();
                tableIndex++;
            }
            tableFile.close();
            return vocabTable;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

	/** 
	 * Get the term count
	 * @return the term count calculation
	 */
    public int getTermCount() {
        return mVocabTable.length / 2;
    }
   
	/**
	 * Walk file tree to get file names
	 * @param path Directory path
	 * @return arraylist of file names
	 */
    public static ArrayList<String> readFileNames(String path) throws IOException {
        final Path currentWorkingPath = Paths.get(path).toAbsolutePath();
        ArrayList<String> files = new ArrayList<>();
        
        Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {

            public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes attrs) {
                if (currentWorkingPath.equals(dir)) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

            public FileVisitResult visitFile(Path file,BasicFileAttributes attrs) {
                // only process .txt files
                if (file.toString().endsWith(".txt"))
                    files.add(file.getFileName().toString());
                return FileVisitResult.CONTINUE;
            }
			// We dont want to throw exceptions if files are locked
			//or other errors occur
            public FileVisitResult visitFileFailed(Path file, IOException e) {
                return FileVisitResult.CONTINUE;
            }

        });
        
        return files;
    }
    
	/**
	 * Return the number of documents
	 * @return the size of mFileNames
	 */
    public int getDocumentCount() {
        return mFileNames.size();
    }
    
	/**
	 * Returns the file name
	 * @return the list of file names
	 */
    public String getFileNames(int docID) {
        return mFileNames.get(docID);
    }
    
    /**
     * Get the terms in the positional inverted index
     * @return an arraylist version of vocab from files
     */
    public ArrayList<String> getPositionalIndexTerms() {
        return getVocab(mVocabTable, mVocabList);
    }
    
	/**
	 * Get the vocabulary
	 * @return the array of vocabulary
	 */
    private ArrayList<String> getVocab(long[] vocabTable, RandomAccessFile vocabFile) {
        int termLength = 0;
        ArrayList<String> terms = new ArrayList<>();
        long vocabPos = 0;
        byte[] buffer;
        
        for(int i = 0; i < vocabTable.length; i+=2) {
            try {
                vocabPos = vocabTable[i];
                vocabFile.seek(vocabPos);
                
                if(i == vocabTable.length - 2) {
                    termLength = (int) (vocabFile.length() - vocabTable[i]);
                }
                else {
                    termLength = (int) (vocabTable[i+2] - vocabPos);
                }
                
                buffer = new byte[termLength];
                vocabFile.read(buffer, 0, termLength);
                terms.add(new String(buffer, "ASCII"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return terms;
    }
    
    /**
     * eturn doc weight based on doc id
     * @param docID the document id
     * @param docWeightsFile the document weights file
     * @param offset the offset to seek by
     * @return a double representing the doc weight
     */
    private double getDocWeight(int docID, 
            RandomAccessFile docWeightsFile, int offset) {
        try {
            docWeightsFile.seek(docID*32+offset);
            byte[] buffer = new byte[8];
            docWeightsFile.read(buffer, 0, buffer.length);
            
            return ByteBuffer.wrap(buffer).getDouble();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        
        return 0.0;
    }
    
	/**
	 * Return doc weight based on doc id
	 * @param docID the document id
	 * @return the doc weight
	 */
    public double getDocWeight(int documentID) {
        return getDocWeight(documentID, docWeights, 0);
    }
    
	/**
	 * The document length
	 * @param docID the document id
	 * @return document length
	 */
    public double getDocLength(int documentID) {
        return getDocWeight(documentID, docWeights, 8);
    }
    
	/**
	 * The document byte size
	 * @param docID the doc id
	 * @return the size of the doc in bytes, null if nothing
	 */
    public double getDocByteSize(int documentID) {
        return getDocWeight(documentID, docWeights, 16);
    }
    
	/**
	 * Given a document id, return the avg term freq related to that doc id
	 * @param docID the document id
	 * @return the average term frequency
	 */
    public double getAverageTermFreq(int documentID) {
        return getDocWeight(documentID, docWeights, 24);
    }
    
	/** 
	 * Returns average document length
	 * @return the document length, null if there is none
	 */
    public double getAverageDocLength() {
        return getDocWeight(mFileNames.size(), docWeights, 32);
    }
    
    /**
     * Get a list of all the file names
     * @return arraylist of file names
     */
    public ArrayList<String> getFileNameList() {
        return this.mFileNames;
    }
}