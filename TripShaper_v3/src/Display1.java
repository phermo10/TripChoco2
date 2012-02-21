import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.File;
import javax.swing.JFrame;
import java.util.Arrays;


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

		Graph city=null;
		File savings = new File("savings");
		File graphs[]=null;
		if(savings.exists()){
			graphs = savings.listFiles();
		}
		boolean newGraph;
		if(graphs!=null&&graphs.length>0){
			System.out.println("Il y a déjà " + graphs.length + " graphes personnalisés disponibles :");
			for(File graph : graphs){
				System.out.println(graph.getName());
			}
			System.out.println("Voulez-vous en utiliser un plutot qu'en generer un nouveau ? o(oui)/n(non)");
			String input = lireString();
			if(input.startsWith("y")){
				newGraph = false;
				System.out.println("Veuillez entrer l'ID du user");
				input = lireString();
				int id;
				try{id = Integer.parseInt(input);}catch(Exception e){id=-1;}
				if(id > -1){
					city = Graph.restore(Integer.parseInt(input));
				}else{
					System.out.println("Erreur");
				}
			}else{
				newGraph = true;
			}
		}else{newGraph = true;}
		if(newGraph){
			System.out.println("Veuillez entrer le nombre de points désirés dans le nouveau graphe");
			String input = lireString();
			int n;
			try{n=Integer.parseInt(input);}catch(Exception e){n=-1;}
			if(n>-1){
				city = new Graph(n);
				User user = new User (2012,4000,180,city.getAllplaces().get(0),city.getAllplaces().get(0));
				city.setUser(user);
				city.save();
				System.out.println("Graphe généré et sauvegardé");
			}else{
				System.out.println("Erreur");
			}
		}
		
		// Qd on arrive ici le graphe est considéré (chargé) ou (généré et personnalisé)
		
		Route bestPath = city.solve();
		// Afficher l'itineraire calculer
	} 

	private static String lireString(){
		String ligne_lue=null;
		try{ 
			InputStreamReader lecteur=new InputStreamReader(System.in); 
			BufferedReader entree=new BufferedReader(lecteur); 
			ligne_lue=Outils.neutraliser(entree.readLine(),true); 
		} 
		catch(IOException e){e.printStackTrace();}
		return ligne_lue;
	}
}
