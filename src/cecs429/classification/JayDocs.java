package cecs429.classification;

import java.util.ArrayList;
import java.util.HashMap;

public class JayDocs {
    private ArrayList<Integer> files;
    private double[] centroid;
    private double[] itc;
    private HashMap<String, Integer> ftcMap;
    private int ftcSum;
    private ArrayList<Double> ptcj;
    
    public JayDocs(ArrayList<Integer> files, int size) {
    	this.files = files;
    	setFtcMap(new HashMap<>());
    	setItc(new double[size]);
    }
    
    public ArrayList<Integer> getFiles() {
    	return this.files;
    }
    
    public void setFiles(ArrayList<Integer> files) {
    	this.files = files;
    }
    
    public double[] getCentroid() {
    	return this.centroid;
    }
    
    public void setCentroid(double[] centroid) {
    	this.centroid = centroid;
    }

	public double[] getItc() {
		return itc;
	}

	public void setItc(double[] itc) {
		this.itc = itc;
	}

	public HashMap<String, Integer> getFtcMap() {
		return ftcMap;
	}

	public void setFtcMap(HashMap<String, Integer> ftcMap) {
		this.ftcMap = ftcMap;
	}

	public int getFtcSum() {
		return ftcSum;
	}

	public void setFtcSum(int ftcSum) {
		this.ftcSum = ftcSum;
	}

	public ArrayList<Double> getPtcj() {
		return ptcj;
	}

	public void setPtcj(ArrayList<Double> ptcj) {
		this.ptcj = ptcj;
	}
}