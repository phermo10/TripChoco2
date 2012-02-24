import java.util.ArrayList;

public class ITIN {
	private ArrayList<Etape> etapes;
	public int lastSize;
	private double distanceTotale;
	private boolean isImpossible;

	public ITIN(Place depart, NiveauTemps nivTpsDep, Place arrivee, NiveauTemps nivTpsArr){
		this(new Etape(depart, nivTpsDep),new Etape(arrivee, nivTpsArr));
	}

	
	public ITIN(Etape depart, Etape arrivee){
		isImpossible = false;
		etapes = new ArrayList<Etape>();
		etapes.add(depart);
		etapes.add(arrivee);
		distanceTotale = 0;
		lastSize = 0;
	}
	
	/**
	 * Voir addEtape(Etape e)
	 * @param s
	 * @param niveauDeTempsReste
	 */
	public void addEtape(Place s, NiveauTemps niveauDeTempsReste){
		addEtape(new Etape(s, niveauDeTempsReste));
	}

	/**
	 * Ajoute une étape à this.
	 * Concrètement, cela représente l'insertion de e en avant dernière position dans la liste des etapes (juste avant l'arrivee donc).
	 * @param e
	 */
	public void addEtape(Etape e){
			etapes.add(etapes.size()-1,e);
	}

	private void majDistanceTotale(){
		lastSize = etapes.size();
		if(!isImpossible){
			distanceTotale = 0;
			for(int i=1; i<etapes.size();i++){
				distanceTotale = distanceTotale + etapes.get(i-1).getPlace().getPosition().distance(etapes.get(i).getPlace().getPosition());
			}
		}else{distanceTotale = Double.POSITIVE_INFINITY;}
	}

	/*public void ordonnerEtapes(){
		if(!etapesOrdonnees){
			ArrayList<Etape> etapesTriees = new ArrayList<Etape>();
			etapesTriees.add(etapes.get(0));
			etapes.remove(0);
			double dMin = -1;
			Etape pp = depart;
			while(etapes.size()>0){
				dMin = -1;
				for(int i=0;i<etapes.size();i++){
					Place s = etapes.get(i).getPlace();
					double d = s.getPosition().distance(pp.getPosition());
					if(dMin == -1 || dMin > d){
						dMin = d;
						pp = s;
					}
				}
				etapes.remove(pp);
				etapesTriees.add(pp);
			}
			etapesTriees.add(etapes.get(etapes.size()-1));
			etapes = etapesTriees;
			etapesOrdonnees=true;
		}
	}*/

	/**
	 * Distance totale de l'itineraire. Les distances entre étapes sont à vol d'oiseau
	 */
	public double getDistTot(){if(!isImpossible){if(lastSize!=etapes.size()){majDistanceTotale();}}return distanceTotale;}

	/**
	 * Duree totale de l'itineraire. Les trajets entre étapes sont à vol d'oiseau
	 * @param vitesse
	 * @return
	 */
	public double getDureeTot(double vitesse, double coeffDistance){
		double dureeDesEtapes = 0;
		dureeDesEtapes+=etapes.get(0).getPlace().getAverageTime();
		Etape e = etapes.get(0);
		for(int i = 1;i<etapes.size();i++){
			dureeDesEtapes+=e.getPlace().getPosition().distance(etapes.get(i).getPlace().getPosition())*coeffDistance/vitesse;
			dureeDesEtapes+=etapes.get(i).getPlace().getAverageTime();
			e = etapes.get(i);
		}
		return dureeDesEtapes;
	}


	public void makeImpossible(){
		distanceTotale = Double.POSITIVE_INFINITY;
		isImpossible = true;
	}

	/**
	 * Retourne un itineraire de this.depart à newArrivee passant par toutes les etapes de this
	 * Eventuellement, on modifie le temps de visite de l'ancienne derniere etape 
	 * @param newArrivee
	 * @return
	 */
	public ITIN prolonger(Place newArrivee, NiveauTemps nivTpsNewArr, NiveauTemps nivTpsOldArr){
		ITIN clone = new ITIN(etapes.get(0).getPlace(),etapes.get(0).getNiveauTemps(), newArrivee, nivTpsNewArr);
		for(int i=0;i<etapes.size()-1;i++){
			clone.addEtape(etapes.get(i));
		}	
		clone.addEtape(new Etape(etapes.get(etapes.size()-1).getPlace(), nivTpsOldArr));
		return clone;
	}

	public boolean isPossible(){
		return !isImpossible;
	}

	/**
	 * On recherche le meilleur endroit où inserer cette nouvelle étape pour minimiser la distance parcourue
	 * Si aucune des étapes actuelle ne permet de rejoindre la nouvelle étape, retourne null.
	 * @param newEtape l'étape à ajouter
	 * @param newPath l'itineraire résultant de l'addition (modification via reference)
	 * @return
	 * @throws Exception 
	 */
	public ITIN tryToGoBy(Place newEtape, Graph graph) throws Exception{
		ITIN result = null;
		
		System.out.println("ITIN " + getEtapes() + " TryToGoBy " + newEtape);
		
		int indexOfNew = getEtapes().indexOf(new Etape(newEtape, null));
		if(indexOfNew>-1){
			getEtapes().set(indexOfNew, new Etape(newEtape, NiveauTemps.TEMPS_MOY));
			result = this;
		}else{

			Etape bestPrec = null; // candidat meilleur precedent
			Etape bestSuiv = null;
			double bestDistTot = -1;
			boolean ok = false;

			for(int i = 0;i<etapes.size();i++){
				Etape prec = etapes.get(i);
				double distPrecNew = graph.getAllShPa().get(prec.getPlace()).get(newEtape).getDistTot();

				for(int j = i;j<=i+1&&j<etapes.size();j++){
					Etape suiv = etapes.get(j);
					double distNewSuiv = graph.getAllShPa().get(suiv.getPlace()).get(newEtape).getDistTot();
					if(distPrecNew!=Double.POSITIVE_INFINITY && distNewSuiv!=Double.POSITIVE_INFINITY){
						ok = true;
						if(bestDistTot==-1||distPrecNew + distNewSuiv < bestDistTot){
							bestPrec = prec;
							bestSuiv = suiv;
							bestDistTot = distPrecNew + distNewSuiv;
						}
					}
				}
			}
			if(ok){
				/*System.out.println("PCC prec => new");
				System.out.println(graph.getAllShPa().get(bestPrec.getPlace()).get(newEtape));
				System.out.println("PCC new => suiv");
				System.out.println(graph.getAllShPa().get(newEtape).get(bestSuiv.getPlace()));*/
				//result = new ITIN(etapes.get(0).getPlace(),etapes.get(0).getNiveauTemps(), etapes.get(etapes.size()-1).getPlace(),etapes.get(etapes.size()-1).getNiveauTemps());
				ArrayList<Etape> resultTmp = new ArrayList<Etape>();
				for(int i=0; i<etapes.size();i++){
					resultTmp.add(etapes.get(i));
					if(etapes.get(i)==bestPrec){
						ITIN pathPrecToNew = graph.getAllShPa().get(bestPrec.getPlace()).get(newEtape);
						for(int j = 1; j<pathPrecToNew.getEtapes().size()-1;j++){
							resultTmp.add(pathPrecToNew.getEtapes().get(j));
						}
						resultTmp.add(new Etape(newEtape,NiveauTemps.TEMPS_MOY));
						ITIN pathNewToSuiv = graph.getAllShPa().get(newEtape).get(bestSuiv.getPlace());
						for(int j = 1; j<pathNewToSuiv.getEtapes().size()-1;j++){
							resultTmp.add(pathNewToSuiv.getEtapes().get(j));
						}
						if(i<etapes.size()-1&&etapes.get(i+1)!=etapes.get(i)&&bestPrec==bestSuiv){resultTmp.add(new Etape(bestSuiv.getPlace(), NiveauTemps.PAS_DE_VISITE));}
					}
				}
				result = new ITIN(resultTmp.get(0),resultTmp.get(resultTmp.size()-1));
				for(int i = 1; i < resultTmp.size()-1;i++){result.addEtape(resultTmp.get(i));}
			}
		}
		
		return result;
	}

	public ArrayList<Etape> getEtapes(){
		return etapes;
	}

	public String toString(){
		String s = "";
		s = "Pour aller de " + etapes.get(0).getPlace() + " à " + etapes.get(etapes.size()-1).getPlace();
		if(isPossible()){
			s+= " il faut passer par : ";
			for(int i = 1; i<etapes.size()-1;i++){
				s+="\n" + etapes.get(i).getPlace(); 
			}
			s+="\nDistance à vol d'oiseau : " + etapes.get(0).getPlace().getPosition().distance(etapes.get(etapes.size()-1).getPlace().getPosition());
		}
		else{ s+="\nIl n'existe pas de chemin.";}
		return s;
	}

	/*public String toBeautifulString(int vitesse){
		String s = "Votre itineraire de " + etapes.get(0).getPlace().getId() + " à " + etapes.get(etapes.size()-1).getPlace().getId() + " : ";
		int dureeActuelle = 0;
		double distanceActuelle = 0;
		double distanceTmp = 0;
		dureeActuelle = etapes.get(0).getPlace().getTav();
		s+="Visite de " + etapes.get(0).getPlace().getId() + " : " + etapes.get(0).getPlace().getTav() + " Minutes";;
		int i;
		for(i=1; i<etapes.size()-1;i++){
			distanceTmp = etapes.get(i-1).getPlace().getPosition().distance(etapes.get(i).getPlace().getPosition());
			distanceActuelle += distanceTmp;
			s+="\nMarchez " + distanceTmp + " km";
			dureeActuelle += 60*distanceTmp/vitesse + etapes.get(i).getPlace().getTav();
			s+="\nVisite de " + etapes.get(i).getPlace().getId() + " : " + etapes.get(i).getPlace().getTav() + " Minutes";;
		}
		s+="\nMarchez " + etapes.get(etapes.size()-2).getPlace().getPosition().distance(etapes.get(etapes.size()-1).getPlace().getPosition())
		+"\nVisite de " + etapes.get(etapes.size()-1).getPlace().getId() + " : " + etapes.get(etapes.size()-1).getPlace().getTav() + " Minutes"
		+"\nVous êtes arrivés à destination."
		+"\nDistance parcourue : " + distanceActuelle
		+"\nTemps écoulé : " + dureeActuelle;
		return s;
	}*/
}
