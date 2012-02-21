import java.awt.Dimension;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import javax.swing.JFrame;


public class Display1 {
	
	static int nbPointsRegroupement=2;
	static int nbPoints=16*nbPointsRegroupement;
	static int xWindow=1000;
	static int yWindow=700;
	static int tailleZoneRegroupement=20;
	static int distanceBetweenPointAndZone=30;
	
	public static void main(String[] args) throws IOException { 
		/*JFrame frame = new JFrame(); 

		frame.setSize(new Dimension(xWindow+50,yWindow+50)); 
		
		Graph mygraph = new Graph(30);
		ArrayList<Place> allplaces = mygraph.getAllplaces();
		ArrayList<Path> allpaths = mygraph.getAllpaths();
		for (Place p : allplaces) {
			System.out.println(p);
		}
		for (Path path : allpaths){
			System.out.println(path);
		}
		
		MonCompo compo = new MonCompo(mygraph.getDispersion());
		frame.add(compo); 
		frame.setVisible(true);
		mygraph.save();
		System.out.println("End of writing");*/
		
		Graph mygraph = new Graph(30);
		User myuser = new User (2012,4000,180,mygraph.getAllplaces().get(0),mygraph.getAllplaces().get(0));
		mygraph.setUser(myuser);
		mygraph.save();
		Graph newgraph = Graph.restore(2012);
		System.out.println("Ces graphs sont a priori les mêmes : " + mygraph.getAllplaces().get(0).equals(newgraph.getAllplaces().get(0)));
		
	} 

	
}
