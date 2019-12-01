package cecs429.classification;

import java.util.ArrayList;
import java.util.List;
/**
 * A Posting encapulates a document ID associated with a search query component.
 */
public class Posting {
    private int docID;
    private int termFreq;
    private List<Integer> mPositions = new ArrayList<>();
    
 
    public Posting(int docID, int termFreq, ArrayList<Integer> positions) {
        this.docID = docID;
        this.termFreq = termFreq;
        this.mPositions = positions;
    }
    
    public Posting(int docID, int termFreq) {
        this.docID = docID;
        this.termFreq = termFreq;
    }
    
    public int getDocumentID() {
        return this.docID;
    }

    public void setDocumentID(int docID) {
        this.docID = docID;
    }
    
    public List<Integer> getPositions() {
        return this.mPositions;
    }
     
    public void setPositions(List<Integer> positions) {
        this.mPositions = positions;
    }

    public int getTermFreq() {
        return termFreq;
    }

    public void setTermFreq(int termFreq) {
        this.termFreq = termFreq;
    }
    
    public boolean isPositionsEmpty() {
        return mPositions.isEmpty();
    }
}
