import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

import javax.swing.JOptionPane;


public class Solver {

	//public static final String FICHIER_COORDONNEES = "coordonnees.txt";

	private int maxScore;
	private final int dureeMaxDesVisites = 50; //minutes
	private ITIN best;
	private Graph graph;

	public Solver(Graph graph){
		this.graph = graph;
	}



	/*private void showNetwork(Graph graph){
		/*for(Place s : graph.getListePlaces()){
			//System.out.println(s.getNom() + " " + s.getCoords());
			if(s.getCoords()==null){System.out.println(s.getNom());}
		}
		DialogMap dm = new DialogMap("Nantes", null, graph, 0, maxScore);
		dm.setVisible(true);
		dm.dispose();
	}*/

	/*private void generateScoresAndTimes(RESEAU graph){
		Random generator = new Random();
		for(Place poi : graph.getListePlaces()){
			poi.setScore(generator.nextInt(maxScore));
			poi.setDureeVisite(generator.nextInt(dureeMaxDesVisites));
		}
	}*/

	/*private void loadCoords(RESEAU graph){
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(FICHIER_COORDONNEES), "UTF-8"));
			String nom = br.readLine();
			while(nom!=null){
				boolean trouvee = false;
				for(int i =0; i<graph.getListePlaces().size()-1&&!trouvee;i++){
					if(Outils.neutraliser(graph.getListePlaces().get(i).getNom(),true).equals(Outils.neutraliser(nom,true))){
						trouvee = true;
						int x = (int)Integer.parseInt(br.readLine());
						int y = (int)Integer.parseInt(br.readLine());
						graph.getListePlaces().get(i).setCoords(new Point(x,y));
					}
				}
				nom = br.readLine();
			}
			ArrayList<Place> PlacesTriees = new ArrayList<Place>();
			for(Place s : graph.getListePlaces()){
				if(s.getCoords()!=null){PlacesTriees.add(s);}
			}
			graph.setPlaces(PlacesTriees);
			graph.setCoordsLoaded(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	private int computeMaxScore(int nbPOI){
		int ms = 0;
		for(int i=1;i<nbPOI;i++){
			ms = ms + i*nbPOI;
		}
		return ms;
	}*/





	/**
	 * 
	 * @param dep
	 * @param arr
	 * @param vitesse En unité de coordonnée par heure (1 unité = 1 mètre ==> equivalent a du Mètre Par Heure, exemple pour 4km/h mettre 4000)
	 * @param minutesDispo
	 * @return
	 */
	public ITIN computeBestPath(){
		
		ITIN bestPath = null;
		int indexDepart = graph.getAllplaces().indexOf(graph.getUser().getDep());
		int indexArrivee = graph.getAllplaces().indexOf(graph.getUser().getArr());
		int minutesDispo = graph.getUser().getTime();
		int vitesse = graph.getUser().getSpeed();
		// ----------------------------
		System.out.println("Simplification...");
		Date t1 = new Date();
		// Simplification du graphe
		Graph g = graph.simplify();

		//simplifyGraph(depart, vitesse, minutesDispo, graph);
		Date t2 = new Date();
		System.out.println("Durée : " + (t2.getTime() - t1.getTime()) + "ms");
		//	----------------------------


		System.out.println("Recherche du meilleur chemin");
		Date t3 = new Date();
		System.out.println(graph.getTousPCC().get(indexDepart).get(graph.getUser().getArr()));
		double vitesseNecessaire = graph.getTousPCC().get(indexDepart).get(graph.getAllplaces().get(indexArrivee)).getDistTot()/(minutesDispo/60);
		if(!g.getAllplaces().contains(graph.getAllplaces().get(indexArrivee))||vitesse<vitesseNecessaire){
			// Si l'arrivee n'est pas dans le rayon atteignable
			// OU
			// Si le plus court chemin entre depart et arrivee a une durée plus longue que le tps dispo
			// A améliorer : Ce calcul ne prend pas en compte les temps de visite du départ et de l'arrivee

			System.out.println("L'arrivee demandée n'est pas atteignable dans le temps imparti. Vitesse nécessaire = " + vitesseNecessaire + " > " + vitesse);
		}else{
			System.out.println("Recherche du meilleur chemin...");
			bestPath = new ITIN(graph.getAllplaces().get(indexDepart),NiveauTemps.TEMPS_MOY, graph.getAllplaces().get(indexArrivee),NiveauTemps.TEMPS_MOY);
			//Classement des Places par score décroissant, classement des Places par duree de visite croissante.
			ArrayList<Place> classementParScore = new ArrayList<Place>(g.getAllplaces());
			ArrayList<Place> classementParDuree = new ArrayList<Place>(g.getAllplaces());
			ComparateurPlace comparateurScore = new ComparateurPlace(POIproperties.SCORE, false, g);
			ComparateurPlace comparateurDuree = new ComparateurPlace(POIproperties.DUREEVISITE, true,g);
			Collections.sort(classementParScore, comparateurScore);
			Collections.sort(classementParDuree, comparateurDuree);		

			// Tant qu'il reste des points qui sont potentiellement visitables
			while(classementParScore.size() > 0){
				// 6) Sélectionner le point ayant le meilleur score
				Place etapePotentielle = classementParScore.get(0);

				// 7) Vérifier que la durée minimale de la visite de ce point est inférieure au temps restant
				if(etapePotentielle.getAverageTime()+bestPath.getDureeTot(vitesse)>minutesDispo){

					//8) Calcul du temps minimal que prendrait un trajet ne visitant que ces points, en utilisant la base des distances précalculées.
					ITIN newPath = null;
					// s'il est possible de passer par cette etape potentielle et que le temps que cela prendrait est inferieur au temps dispo
					if(bestPath.tryToGoBy(etapePotentielle,NiveauTemps.TEMPS_MOY, newPath, this.graph)){ if (newPath.getDureeTot(vitesse)<=minutesDispo){
						bestPath = newPath;					
					}}// Dans tous les cas on a vérifié cette option donc on la supprime
					classementParDuree.remove(etapePotentielle);
					classementParScore.remove(etapePotentielle);

				}else{
					/*9) Si la durée est OK, on retourne à 6) en gardant ce point et en sélectionnant le point suivant dans lordre des meilleurs scores.
					Sinon, on note ce point comme étant invisitable et on retourne à 6)*/

					// sinon, tout les points de temps de visite superieure seront invisitables aussi
					boolean indexToDeleteFound = false;
					int size = classementParDuree.size();
					int indexToDelete = -1;
					for(int i = 0; i<size; i++){
						if(indexToDeleteFound){
							classementParScore.remove(classementParDuree.get(indexToDelete));
							classementParDuree.remove(indexToDelete);
						}else{
							indexToDeleteFound = classementParDuree.get(i) == etapePotentielle;
							if(indexToDeleteFound){
								indexToDelete = i;
								i--;
							}
						}
					}
				}
			}
		}
		//Date t4 = new Date();
		//System.out.println("Durée : " + (t4.getTime() - t3.getTime()) + "ms");
		//bestPath.ordonnerEtapes();
		return bestPath;
	}

	/**
	 * 
	 * @param depart
	 * @param vitesse En unité de coordonnée par heure (1 unité = 1 mètre ==> equivalent a du Mètre Par Heure, exemple pour 4km/h mettre 4000)
	 * @param full
	 * @return
	 */
	/*private RESEAU simplifyGraph(Place depart, int vitesse, int minutesDispo, RESEAU full){
		RESEAU simple = new RESEAU_IMP(full.getCheminRep());
		ArrayList<Place> Places = new ArrayList<Place>();
		ArrayList<TRANSPORT> transports = new ArrayList<TRANSPORT>();

		// Tri par rayon parcourable
		double distanceMaxParcourable = vitesse * minutesDispo/60;
		System.out.println(distanceMaxParcourable);
		for(Place s : full.getListePlaces()){
			if(depart.getCoords().distance(s.getCoords())<distanceMaxParcourable/2){
				Places.add(s);
				for(ARRET a : s.getListeArrets()){
					TRANSPORT t = a.getTransport();
					if(!transports.contains(t)){transports.add(t);}
				}
			}
		}
		simple.setPlaces(Places);
		simple.setTransports(transports);
		// -------------------------
		return simple;
	}*/







	

	public enum POIproperties {
		SCORE(0),
		DUREEVISITE(1),
		DISTANCERELATIVE(2);
		private final int propertyCode;

		private POIproperties(int propertyCode) {
			this.propertyCode = propertyCode;
		}	
	}
	
	public class ComparateurPlace implements Comparator<Place> {

		
		private POIproperties propertyCode;
		private Place origin;
		private boolean ordreCroissant;
		private Graph graph;

		public ComparateurPlace(POIproperties propertyCode, boolean ordreCroissant, Graph graph){
			this.graph = graph;
			this.propertyCode = propertyCode;
			this.origin = null;
			this.ordreCroissant = ordreCroissant;
		}

		public ComparateurPlace(Place origin, boolean ordreCroissant){
			this.graph = null;
			this.propertyCode = POIproperties.DISTANCERELATIVE;
			this.origin = origin;
			this.ordreCroissant = ordreCroissant;
		}

		public int compare(Place s1, Place s2) {
			int result;
			double property1;
			double property2;
			switch (propertyCode)
			{
			case SCORE:property1 = graph.getScores().get(s1);property2 = graph.getScores().get(s2);break;
			case DISTANCERELATIVE:
				property1 = origin.getPosition().distance(s1.getPosition());
				property2 = origin.getPosition().distance(s2.getPosition());
			default:property1 = s1.getAverageTime();property2 = s2.getAverageTime();break;	
			}
			if(property1==property2){result=0;}
			else{

				if(ordreCroissant){
					result=property1<property2?-1:1;
				}
				else{
					result=property1<property2?1:-1;
				}
			}			
			return result;
		}
	}
}