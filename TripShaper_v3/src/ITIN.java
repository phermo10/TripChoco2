import java.util.ArrayList;

public class ITIN {
	private ArrayList<Etape> etapes;
	public int lastSize;
	private double distanceTotale;
	private boolean isImpossible;

	public ITIN(Place depart, NiveauTemps nivTpsDep, Place arrivee, NiveauTemps nivTpsArr){
		isImpossible = false;
		etapes = new ArrayList<Etape>();
		lastSize = 0;
		distanceTotale = 0;
		etapes.add(new Etape(depart, nivTpsDep));
		etapes.add(new Etape(arrivee, nivTpsArr));
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
		if(!etapes.contains(e)){
			etapes.add(etapes.size()-1,e);
		}
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
	public double getDureeTot(int vitesse){
		double dureeDesEtapes = 0;
		dureeDesEtapes+=etapes.get(0).getPlace().getTav();
		Etape e = etapes.get(0);
		for(int i = 1;i<etapes.size();i++){
			dureeDesEtapes+=e.getPlace().getPosition().distance(etapes.get(i).getPlace().getPosition())/vitesse;
			dureeDesEtapes+=etapes.get(i).getPlace().getTav();
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
	 * @param newArrivee
	 * @return
	 */
	public ITIN prolonger(Place newArrivee, NiveauTemps nivTpsNewArr){
		ITIN clone = new ITIN(etapes.get(0).getPlace(),etapes.get(0).getNiveauTemps(), newArrivee, nivTpsNewArr);
		for(Etape e : etapes){
			clone.addEtape(e);
		}	
		return clone;
	}

	public boolean isPossible(){
		return !isImpossible;
	}

	/**
	 * On recherche le meilleur endroit où inserer cette nouvelle étape pour minimiser la distance parcourue
	 * Si aucune des étapes actuelle ne permet de rejoindre la nouvelle étape, retourne false. True si c'est possible.
	 * @param newEtape l'étape à ajouter
	 * @param newPath l'itineraire résultant de l'addition (modification via reference)
	 * @return
	 */
	public boolean tryToGoBy(Place newEtape, NiveauTemps nivTpsNewEtape, ITIN result){
		boolean ok = false;
		Etape bestPrec = null; // candidat meilleur precedent
		double bestDistTot = -1;
		for(int i = 0;i<etapes.size()&&!ok;i++){
			Etape prec = etapes.get(i);
			for(int j = 0;i<etapes.size()&&!ok;i++){
				Etape suiv = etapes.get(j);
				double distPrecNew = prec.getPlace().getPlusCourtsChemins().get(newEtape).getDistTot();
				double distNewSuiv = newEtape.getPlusCourtsChemins().get(suiv).getDistTot();
				if(distPrecNew!=Double.POSITIVE_INFINITY && distNewSuiv!=Double.POSITIVE_INFINITY){
					ok = true;
					if(distPrecNew + distNewSuiv < bestDistTot){
						bestPrec = prec;
						bestDistTot = distPrecNew + distNewSuiv;
					}
				}

			}
		}
		if(ok){
			result = new ITIN(etapes.get(0).getPlace(),etapes.get(0).getNiveauTemps(), etapes.get(etapes.size()-1).getPlace(),etapes.get(etapes.size()-1).getNiveauTemps());
			for(int i=0; i<etapes.size();i++){
				result.addEtape(etapes.get(i));
				if(etapes.get(i)==bestPrec){
					result.addEtape(newEtape,nivTpsNewEtape);
				}
			}
		}
		return ok;
	}

	public ArrayList<Etape> getEtapes(){
		return etapes;
	}

	public String toString(){
		String s = "";
		s = "Pour aller de " + etapes.get(0).getPlace().getId() + " à " + etapes.get(0).getPlace().getId();
		if(isPossible()){
			s+= " il faut passer par : ";
			for(Etape e : etapes){
				s+="\n" + e.getPlace().getId(); 
			}
			s+="\nDistance à vol d'oiseau : " + etapes.get(0).getPlace().getPosition().distance(etapes.get(etapes.size()-1).getPlace().getPosition());
		}
		else{ s+="\nIl n'existe pas de chemin.";}
		return s;
	}

	public String toBeautifulString(int vitesse){
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
	}
}
