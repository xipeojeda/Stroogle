package cecs429.classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PositionalInvertedIndex {
	private final HashMap<String, List<Posting>> map;
	
	public PositionalInvertedIndex() {
		this.map = new HashMap<String, List<Posting>>();
	}
	/*
	 * Add term to hashmap must run at O(1)
	 */
	public void addTerm(String term, int docID, int position) {
		//if maps contains the term
		if(!map.containsKey(term)) {
			List<Posting> postings = new ArrayList<>();
			postings.add(new Posting(docID, position));
			map.put(term, postings);
		}
		else {
			//if terms docID is not at last posting in the last add that position
			List<Posting> postings = map.get(term);
			if(postings.get(postings.size() - 1).getDocumentID() != docID) {
				postings.add(new Posting(docID, position));
				map.replace(term, postings);
			}
			//if not, add the position of last term
			else {
				Posting posPost = postings.get(postings.size() - 1);
				List<Integer> positions = posPost.getPositions();
				positions.add(position);
				posPost.setPositions(positions);
				postings.set(postings.size()-1,  posPost);
				map.replace(term, postings);
			}
		}
	}
	
	public List<Posting> getPosting(String term){
		if(map.containsKey(term))
			return map.get(term);
		else
			return new ArrayList<>();
	}
	/*
	 *  Gets vocabulary of positional inverted index
	 */
	public String[] getVocabulary() {
		//sorts vocab
		String[] terms = map.keySet().toArray(new String[map.size()]);
		Arrays.sort(terms);
		//returns terms
		return terms;
	}
	/*
	 *  Gets the amount of terms in map
	 */
	public int getTermCount() {
		return map.size();
	}
}
