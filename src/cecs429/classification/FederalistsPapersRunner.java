package cecs429.classification;

public class FederalistsPapersRunner{
	public static void main(String[] args) {
		//runs gui
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FederalistsGUI().setVisible(true);
            }
        });
	}
}
