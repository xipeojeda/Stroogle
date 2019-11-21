package edu.csulb;

import cecs429.gui.GUI;

public class Stroogle {
    public static void main(String[] args) {
    	GUI gui = new GUI();
    	try {
			gui.query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
