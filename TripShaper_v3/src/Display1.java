import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.File;

import javax.sound.midi.SysexMessage;
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

		Graph userGraph=null;
		File dossierSavings = new File(Emplacements.DOSSIER_SAVINGS);
		File villes[]=null;
		if(dossierSavings.exists()){
			villes = dossierSavings.listFiles();
		}
		boolean newCity;
		if(villes!=null&&villes.length>0){
			System.out.println("Il y a déjà " + villes.length + " villes générées :");
			for(File ville : villes){
				System.out.println(ville.getName());
			}
			System.out.println("Voulez-vous en utiliser une plutot qu'en generer une nouvelle ? o(oui)/n(non)");
			String input = lireString();
			if(input.startsWith("o")){
				System.out.println("Veuillez entrer l'ID de la ville à utiliser : ");
				input = lireString();
				int cityID;
				cityID = Integer.parseInt(input);
				newCity = false;
				File dossierCity = new File(Emplacements.DOSSIER_GRAPH_COMPLET(cityID));
				if(dossierCity.exists()){
					File users[] = dossierCity.listFiles();
					if(users.length>0){
						System.out.println("Il y a déjà " + users.length + " users générés :");
						for(File user : users){
							System.out.println(user.getName());
						}
						System.out.println("Souhaitez-vous en utiliser un plutot qu'en créer un nouveau ? o/n");
						input = lireString();
						if(input.startsWith("o")){
							System.out.println("Veuillez entrer l'ID du user à utiliser");
							input = lireString();
							int id = Integer.parseInt(input);
							userGraph = Graph.restore(id,cityID);
						}						
					}
				}else{
					System.out.println("Erreur");
					System.exit(0);
				}
			}else{
				newCity = true;
			}
		}else{
			newCity = true;
		}
		if(newCity){
			System.out.println("Veuillez entrer le nombre de points désirés dans le nouveau graphe");
			String input = lireString();
			int n;
			try{n=Integer.parseInt(input);}catch(Exception e){n=-1;}
			if(n>-1){
				userGraph = new Graph(n);
				User user = new User (2012,4000,180,userGraph.getAllplaces().get(0),userGraph.getAllplaces().get(0));
				userGraph.setUser(user);
				userGraph.save();
				userGraph.display();
				System.out.println("Graphe et user générés et sauvegardés");
			}else{
				System.out.println("Erreur");
			}
		}

		// Qd on arrive ici le graphe est considéré (chargé) ou (généré et personnalisé)
		//ITIN bestPath = userGraph.solve();
		userGraph.solve();
		userGraph.display();
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
