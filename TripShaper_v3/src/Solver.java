import java.util.*;


public class Solver {
	private Graph graph;

	public Solver(Graph graph){
		this.graph = graph;
	}

	/**
	 * 
	 * @param dep
	 * @param arr
	 * @param vitesse En unité de coordonnée par heure (1 unité = 1 mètre ==> equivalent a du Mètre Par Heure, exemple pour 4km/h mettre 4000)
	 * @param minutesDispo
	 * @return
	 * @throws Exception 
	 */
	public ITIN computeBestPath() throws Exception{
		
		ITIN bestPath = null;
		Place depart = graph.getUser().getDep();
		Place arrivee = graph.getUser().getArr();
		System.out.println(depart);
		System.out.println(depart.getSommetsAtteignables());
		System.out.println(arrivee);
		
		int secondesDispo = graph.getUser().getTime();
		double vitesse = graph.getUser().getSpeed();
		// ----------------------------
		System.out.println("Simplification...");
		Date t1 = new Date();
		// Simplification du graphe
		Graph g = graph.simplify();
		if(depart.equals(arrivee)){
			bestPath = new ITIN(depart, NiveauTemps.TEMPS_MOY, arrivee, NiveauTemps.TEMPS_MOY);
		}else{
			ITIN shPaToDest = graph.getAllShPa().get(depart).get(arrivee);
			bestPath = shPaToDest;			
		}
		
		if(bestPath.isPossible()){
			double vitesseNecessaire = bestPath.getDistTot()*graph.coeffDistance()/(secondesDispo);
			/*System.out.println("Distance totale en pixels entre arrivee et depart : " + graph.getAllShPa().get(depart).get(arrivee).getDistTot());
			System.out.println("Distance totale reelle entre arrivee et depart : " + graph.getAllShPa().get(depart).get(arrivee).getDistTot()*graph.coeffDistance());
			System.out.println("Secondes dispo : " + secondesDispo);
			System.out.println("Vitesse necessaire " + vitesseNecessaire);*/
			if(!g.getAllplaces().contains(arrivee)||vitesse<vitesseNecessaire){
				// Si l'arrivee n'est pas dans le rayon atteignable
				// OU
				// Si le plus court chemin entre depart et arrivee a une durée plus longue que le tps dispo
				// A améliorer : Ce calcul ne prend pas en compte les temps de visite du départ et de l'arrivee
				System.out.println("L'arrivee demandée n'est pas atteignable dans le temps imparti. Vitesse nécessaire = " + vitesseNecessaire + " > " + vitesse);
			}else{
				System.out.println("Recherche du meilleur chemin...");
				/*System.out.println(depart);
				System.out.println(depart.getSommetsAtteignables());
				System.out.println(arrivee);*/
				//Classement des Places par score décroissant, classement des Places par duree de visite croissante.
				ArrayList<Place> classementParScore = new ArrayList<Place>(g.getAllplaces());
				ArrayList<Place> classementParDuree = new ArrayList<Place>(g.getAllplaces());
				//classementParScore.remove(depart);
				//classementParDuree.remove(arrivee);
				
				ComparateurPlace comparateurScore = new ComparateurPlace(PlaceComparableProperty.SCORE, false, g);
				ComparateurPlace comparateurDuree = new ComparateurPlace(PlaceComparableProperty.DUREEVISITE, true,g);
				Collections.sort(classementParScore, comparateurScore);
				Collections.sort(classementParDuree, comparateurDuree);		
				// Tant qu'il reste des points qui sont potentiellement visitables
				while(classementParScore.size() > 0){
					//System.out.println("fglkh");
					//System.out.println("Classement");
					//System.out.println(classementParScore);
					// 6) Sélectionner le point ayant le meilleur score
					//System.out.println("Etape potentielle");
					Place etapePotentielle = classementParScore.get(0);
					//System.out.println(etapePotentielle);
					//System.out.println("Best Path");
					//System.out.println(bestPath);
					/*System.out.println(vitesse);
					System.out.println(etapePotentielle.getAverageTime());
					System.out.println(bestPath.getDureeTot(vitesse,graph.coeffDistance()));
					System.out.println(secondesDispo);*/

					// 7) Vérifier que la durée minimale de la visite de ce point est inférieure au temps restant
					if(etapePotentielle.getAverageTime()+bestPath.getDureeTot(vitesse, graph.coeffDistance())<secondesDispo){
						//System.out.println("abcdelkj");
						//8) Calcul du temps minimal que prendrait un trajet ne visitant que ces points, en utilisant la base des distances précalculées.
						ITIN newPath = bestPath.tryToGoBy(etapePotentielle, this.graph);
						// s'il est possible de passer par cette etape potentielle et que le temps que cela prendrait est inferieur au temps dispo
						if(newPath!=null){
							if (newPath.getDureeTot(vitesse,graph.coeffDistance())<=secondesDispo){
								bestPath = newPath;
								//System.out.println("True");
							}
						}else{
							//System.out.println("False");
						}

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
					//System.out.println("Best path APRES");
					//System.out.println(bestPath);
				}
				System.out.println("OK");
			}
		}else{System.out.println("Il n'existe aucun chemin joignant votre depart et votre arrivee");}
		//Date t4 = new Date();
		//System.out.println("Durée : " + (t4.getTime() - t3.getTime()) + "ms");
		//bestPath.ordonnerEtapes();
		System.out.println(bestPath);
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












}