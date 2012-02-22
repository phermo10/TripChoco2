import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	public Solver(/*String emplacementArretId,*/ Graph graph){

		/*
		System.out.println("Calcul du score max :");
		Date t1 = new Date();
		//maxScore = computeMaxScore(graph.getNbNoeuds());
		this.maxScore = graph.getAllplaces().get(0).scoreMax();
		Date t2 = new Date();
		System.out.println("Scoremax = " + maxScore);
		System.out.println("Durée : " + (t2.getTime() - t1.getTime()) + "ms");
		/*System.out.println("Génération aléatoire des scores et durées de visite :");
		t1 = new Date();
		generateScoresAndTimes(graph);
		t2 = new Date();
		System.out.println("Durée : " + (t2.getTime() - t1.getTime()) + "ms");*/

		//boolean cheminsDejaCalcules = chargerPlusCourtsChemins(graph);

		/*System.out.println("Chargement des coordonnées...");
		t1 = new Date();
		loadCoords(graph);
		t2 = new Date();
		System.out.println("Durée : " + (t2.getTime() - t1.getTime()) + "ms");
		*/
		/*
		if(!cheminsDejaCalcules){
			System.out.println("Echec de chargement du fichier des plus courts chemins.");
			System.out.println("Calcul des plus courts chemins");
			boolean sauvegardeReussie = false;
			t1 = new Date();
			sauvegardeReussie = computeShortestPaths(graph);
			t2 = new Date();
			System.out.println("Durée : " + (t2.getTime() - t1.getTime()) + "ms");
			if(sauvegardeReussie){
				System.out.println("Les plus courts chemins ont été sauvegardés avec succès.");
			}else{
				System.out.println("La sauvegarde des plus courts chemin a échoué.");
			}
		}
		
		else{
			System.out.println("Plus courts chemins précalculés chargés.");
		}*/
		//showNetwork();
		//DialogMap dm = new DialogMap("Saisie des Places", null, graph, 0, maxScore);
		//dm.setVisible(true);
		Place source = graph.getUser().getDep();
		Place destination = graph.getUser().getArr();
		//if(dm.getResult()!=null){
			//Place source = dm.getResult()[0];
			//Place destination = dm.getResult()[1];
			//if(source!=null && destination!=null){
				System.out.println("Vous souhaitez aller de " + source + " vers " + destination);
				System.out.println("Vitesse de déplacement : 4km/h");
				System.out.println("Temps dispo : " + graph.getUser().getTime() +"minutes");
				System.out.println("Calcul de l'itineraire maximisant le score");
				Date t1 = new Date();
				ITIN best = computeBestPath(graph);
				Date t2 = new Date();
				System.out.println("Durée : " + (t2.getTime() - t1.getTime()) + "ms");
				if(best!=null){
					/*DialogMap displayBestResult = new DialogMap("Votre itineraire", best, graph, 4000, maxScore);
					displayBestResult.setVisible(true);
					displayBestResult.dispose();*/
					this.best = best;
					System.out.println("Itinéraire ok");
				}
				else
				{
					System.out.println("Aucun itineraire possible entre ces deux Places dans le temps imparti");}

			//}
			//else{System.out.println("Vous n'avez sélectionné aucune Place");
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

	private boolean computeShortestPaths(Graph graph){
		int count = 0;
		int tot = graph.getAllplaces().size();
		for(Place depart : graph.getAllplaces()){
			count++;
			System.out.println("" + count + "/" + tot);
			/*if(!Outils.neutraliser(depart.getNom(), true).equals("tertre")){
				continue;
			}

			System.out.println("DEPART = " + depart);*/
			HashMap<Place,Place> predecesseurs = new HashMap<Place, Place>();
			HashMap<Place, Double> distances = new HashMap<Place, Double>();
			HashMap<Place, ITIN> plusCourtsChemins = new HashMap<Place, ITIN>();
			ArrayList<Place> nonVisites = new ArrayList<Place>(graph.getAllplaces());
			ArrayList<Place> visites = new ArrayList<Place>();

			for(Place s : graph.getAllplaces()){
				if(s!=depart){
					distances.put(s,Double.POSITIVE_INFINITY);
					ITIN it = new ITIN(depart, NiveauTemps.TEMPS_MOY, s, NiveauTemps.TEMPS_MOY);
					it.makeImpossible();
					plusCourtsChemins.put(s, it);
				}
			}
			distances.put(depart, 0.0);
			Place pp=null;
			while(nonVisites.size()>0){
				pp = getPlusProche(distances, nonVisites);
				if(pp!=null){
					//System.out.println("Plus proche : " + pp);
					// Collections.sort trie par ordre croissant (plus petit en 0)
					Collections.sort(visites, new ComparateurPOI(pp, true));
					boolean precNotFound = true;
					int i=0;
					Place prec;
					// On parcours les points visites tries par distance relative à pp
					// jusqu'à en trouver un qui soit dans les sommets atteignables de pp
					// (il y en a forcément un, sinon pp serait à une distance infinie et getplusproche retournerai null)
					// c'est ce point qui sera le prédecesseur de pp
					while(precNotFound&&i<visites.size()){
						prec = visites.get(i);
						if(prec!=null&&prec.getSommetsAtteignables(graph).contains(pp)){
							precNotFound = false;
							ITIN shortestToPrec = plusCourtsChemins.get(prec);
							ITIN shortestToPP;
							if(shortestToPrec!=null)
								shortestToPP = shortestToPrec.prolonger(pp,NiveauTemps.PAS_DE_VISITE);
							else
								shortestToPP = new ITIN(depart,NiveauTemps.TEMPS_MOY, pp,NiveauTemps.TEMPS_MOY);
							plusCourtsChemins.put(pp, shortestToPP);
						}
						i++;
					}

					for(Place s2 : pp.getSommetsAtteignables(graph)){
						double ds2 = distances.get(s2);
						double dpp = distances.get(pp);
						double ds2pp = pp.getPosition().distance(s2.getPosition());
						if(ds2>dpp+ds2pp){
							distances.put(s2, ds2pp + dpp);
							predecesseurs.put(s2, pp);
						}
					}
					nonVisites.remove(pp);
					visites.add(pp);
				}
				else{
					nonVisites.clear();
				}
			}	
			depart.setPlusCourtsChemins(plusCourtsChemins);
			predecesseurs.clear();
			distances.clear();
			nonVisites.clear();
			nonVisites.trimToSize();
			visites.clear();
			visites.trimToSize();
		}
		return sauvegarderPlusCourtsChemins(graph);
	}

	private Place getPlusProche(HashMap<Place, Double> distances, ArrayList<Place> nonVisites){
		double dMin = Double.POSITIVE_INFINITY;
		Place pp = null;
		for(Place s : nonVisites){
			double d = distances.get(s);
			if(d!=Double.POSITIVE_INFINITY){
				boolean dMinIsInf = dMin==Double.POSITIVE_INFINITY;
				if(dMinIsInf||(!dMinIsInf&&d<dMin)){
					dMin = d;
					pp = s;
				}
			}
		}
		return pp;

	}

	/**
	 * 
	 * @param dep
	 * @param arr
	 * @param vitesse En unité de coordonnée par heure (1 unité = 1 mètre ==> equivalent a du Mètre Par Heure, exemple pour 4km/h mettre 4000)
	 * @param minutesDispo
	 * @return
	 */
	public ITIN computeBestPath(Graph graph){
		ITIN bestPath = null;
		Place depart = graph.getUser().getDep();
		Place arrivee = graph.getUser().getArr();
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
		double vitesseNecessaire = depart.getPlusCourtsChemins().get(arrivee).getDistTot()/(minutesDispo/60);
		if(!g.getAllplaces().contains(arrivee)||vitesse<vitesseNecessaire){
			// Si l'arrivee n'est pas dans le rayon atteignable
			// OU
			// Si le plus court chemin entre depart et arrivee a une durée plus longue que le tps dispo
			// A améliorer : Ce calcul ne prend pas en compte les temps de visite du départ et de l'arrivee

			System.out.println("L'arrivee demandée n'est pas atteignable dans le temps imparti. Vitesse nécessaire = " + vitesseNecessaire + " > " + vitesse);
		}else{
			System.out.println("Recherche du meilleur chemin...");
			bestPath = new ITIN(depart,NiveauTemps.TEMPS_MOY, arrivee,NiveauTemps.TEMPS_MOY);
			//Classement des Places par score décroissant, classement des Places par duree de visite croissante.
			ArrayList<Place> classementParScore = new ArrayList<Place>(g.getAllplaces());
			ArrayList<Place> classementParDuree = new ArrayList<Place>(g.getAllplaces());
			ComparateurPOI comparateurScore = new ComparateurPOI(POIproperties.SCORE, false);
			ComparateurPOI comparateurDuree = new ComparateurPOI(POIproperties.DUREEVISITE, true);
			Collections.sort(classementParScore, comparateurScore);
			Collections.sort(classementParDuree, comparateurDuree);		

			// Tant qu'il reste des points qui sont potentiellement visitables
			while(classementParScore.size() > 0){
				// 6) Sélectionner le point ayant le meilleur score
				Place etapePotentielle = classementParScore.get(0);

				// 7) Vérifier que la durée minimale de la visite de ce point est inférieure au temps restant
				if(etapePotentielle.getTav()+bestPath.getDureeTot(vitesse)>minutesDispo){

					//8) Calcul du temps minimal que prendrait un trajet ne visitant que ces points, en utilisant la base des distances précalculées.
					ITIN newPath = null;
					// s'il est possible de passer par cette etape potentielle et que le temps que cela prendrait est inferieur au temps dispo
					if(bestPath.tryToGoBy(etapePotentielle,NiveauTemps.TEMPS_MOY, newPath)){ if (newPath.getDureeTot(vitesse)<=minutesDispo){
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


	private class ComparateurPOI implements Comparator<Place> {

		private POIproperties propertyCode;
		private Place origin;
		private boolean ordreCroissant;

		public ComparateurPOI(POIproperties propertyCode, boolean ordreCroissant){
			this.propertyCode = propertyCode;
			this.origin = null;
			this.ordreCroissant = ordreCroissant;
		}

		public ComparateurPOI(Place origin, boolean ordreCroissant){
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
			case SCORE:property1 = s1.getBasicscore();property2 = s2.getBasicscore();break;
			case DISTANCERELATIVE:
				property1 = origin.getPosition().distance(s1.getPosition());
				property2 = origin.getPosition().distance(s2.getPosition());
			default:property1 = s1.getTav();property2 = s2.getTav();break;	
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

	private class Balises{
		public static final String FROM_Begin = "<FROM=";
		public static final String TO_Begin = "\t<TO=";
		public static final String FROM_End = "</FROM>";
		public static final String TO_End = "\t</TO>";
		public static final String BY = "\t\t<BY=";
	}

	private boolean sauvegarderPlusCourtsChemins(Graph graph){
		boolean success = true;	
		PrintWriter output=null;
		System.out.println("Sauvegarde des plus courts chemin...");
		try {
			output = new PrintWriter(new BufferedWriter(new FileWriter("plusCourtsChemins.txt")));
			String line;
			for(Place from : graph.getAllplaces()){
				output.println(Balises.FROM_Begin +from.getId()+">");

				for(Place to : graph.getAllplaces()){
					if(!from.equals(to)){
						output.println(Balises.TO_Begin+to.getId()+">");

						ArrayList<Etape> etapes = from.getPlusCourtsChemins().get(to).getEtapes();
						for(Etape etape : etapes){
							if(!(etape.equals(to)||etape.equals(from)))
								output.println(Balises.BY + etape.getPlace().getId()+">");
						}
						output.println(Balises.TO_End);
					}
				}
				output.println(Balises.FROM_End);
			}
			System.out.println("OK.");			
		}	    
		catch (Exception e) {
			System.out.println("Echec.");
			success = false;
		}finally {
			try{
				output.flush();
				output.close();
			}catch(Exception e){}
		}
		return success;
	}

	private boolean chargerPlusCourtsChemins(Graph graph){
		//if(graph.isCoordsLoaded()) throw new IllegalArgumentException("Graph coords must not be loaded before calling this method.");
		boolean success = true;
		
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader input =  new BufferedReader(new FileReader("plusCourtsChemins.txt"));
			try {
				String line = null; 

				while (( line = input.readLine()) != null){
					lines.add(line);
				}
			}
			finally {
				input.close();
			}
		}
		catch (Exception ex){
			success = false;
		}

		String line;
		HashMap<Place,ITIN> plusCourtsChemins = null;
		Place from=null;
		int fromIndex=-1;
		Place to=null;
		int toIndex=-1;
		Place by=null;
		int byIndex=-1;
		ITIN chem = null;

		for(int i=0;i<lines.size()&&success; i++){
			line = lines.get(i);
			if(line.startsWith(Balises.FROM_Begin)){
				int fromID = Integer.parseInt(line.substring(Balises.FROM_Begin.length(),line.length()-1));

				///////////////////////////////////
				from = new Place(graph,fromID);
				fromIndex = graph.getAllplaces().indexOf(from);
				if(fromIndex > -1){
					plusCourtsChemins = new HashMap<Place, ITIN>();
				}else{
					plusCourtsChemins = null;
				}
			}
			if(line.startsWith(Balises.TO_Begin)&&fromIndex > -1){
				int toID = Integer.parseInt(line.substring(Balises.TO_Begin.length(),line.length()-1));
				to = new Place(graph,toID);
				toIndex = graph.getAllplaces().indexOf(to);
				if(toIndex>-1){
					chem = new ITIN(from, NiveauTemps.TEMPS_MOY,to, NiveauTemps.TEMPS_MOY);
				}else{
					chem = null;
				}
			}
			if(line.startsWith(Balises.BY)&&fromIndex > -1&&toIndex>-1){
				int byID = Integer.parseInt(line.substring(Balises.BY.length(),line.length()-1));
				by = new Place(graph,byID);
				byIndex = graph.getAllplaces().indexOf(by);
				if(byIndex>-1){
					chem.addEtape(by, NiveauTemps.PAS_DE_VISITE);
				}else{
					// Le fichier est corrompu (peut etre que le graphe a changé) 
					//il faut recalculer les itineraire
					success = false; 
				}
			}
			if(line.startsWith(Balises.TO_End)&&fromIndex > -1&&toIndex>-1&&byIndex>-1){
				plusCourtsChemins.put(to, chem);
			}
			if(line.startsWith(Balises.FROM_End)&&fromIndex > -1&&toIndex>-1&&byIndex>-1){
				from.setPlusCourtsChemins(plusCourtsChemins);
				plusCourtsChemins = null;
				from=null;
				fromIndex=-1;
				to=null;
				toIndex=-1;
				by=null;
				byIndex=-1;
				chem = null;
			}
		}
		return success;
	}

}