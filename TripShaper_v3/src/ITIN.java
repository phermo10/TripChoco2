import java.util.ArrayList;

public class ITIN {
	private ArrayList<Place> etapes;
	private Place depart;
	private Place arrivee;
	public int lastSize;
	private double distanceTotale;
	private boolean isImpossible;

	public ITIN(Place depart, Place arrivee){
		isImpossible = false;
		etapes = new ArrayList<Place>();
		this.depart = depart;
		this.arrivee = arrivee;
		lastSize = 0;
		distanceTotale = 0;
		etapes.add(depart);
		etapes.add(arrivee);
	}
	public boolean addEtape(Place s){
		if(!etapes.contains(s)){etapes.add(s);return false;}{return true;}
	}

	private void majDistanceTotale(){
		lastSize = etapes.size();
		if(!isImpossible){
			ordonnerEtapes();
			distanceTotale = 0;
			for(int i=1; i<etapes.size();i++){
				distanceTotale = distanceTotale + etapes.get(i-1).getPosition().distance(etapes.get(i).getPosition());
			}
		}else{distanceTotale = Double.POSITIVE_INFINITY;}
	}

	private void ordonnerEtapes(){
		ArrayList<Place> etapesTriees = new ArrayList<Place>();
		etapes.remove(depart);
		etapesTriees.add(depart);
		double dMin = -1;
		Place pp = depart;
		while(etapes.size()>0){
			dMin = -1;
			for(Place s : etapes){
				double d = s.getPosition().distance(pp.getPosition());
				if(dMin == -1 || dMin > d){
					dMin = d;
					pp = s;
				}
			}
			etapes.remove(pp);
			etapesTriees.add(pp);
		}
		etapesTriees.add(arrivee);
		etapes = etapesTriees;
	}


	public double getDistTot(){if(!isImpossible){if(lastSize!=etapes.size()){majDistanceTotale();}}return distanceTotale;}
	
	public int getDureeTot(int vitesse){
		int dureeDesEtapes = 0;
		for(Place etape : etapes){
			dureeDesEtapes+=etape.getTav(); 
		}
		return ((int)Math.floor(getDistTot()/vitesse))+1+dureeDesEtapes;
	}


	public void makeImpossible(){
		distanceTotale = Double.POSITIVE_INFINITY;
		isImpossible = true;
	}


	public ITIN prolonger(Place newArrivee){
		ITIN clone = new ITIN(depart, newArrivee);
		for(Place s : etapes){
			clone.addEtape(s);
		}	
		return clone;
	}

	public boolean goesBy(Place s){
		return etapes.contains(s);
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
	public boolean tryToGoBy(Place newEtape, ITIN result){
		boolean ok = false;
		Place bestPrec = null; // candidat meilleur precedent
		double bestDistTot = -1;
		for(int i = 0;i<etapes.size()&&!ok;i++){
			Place prec = etapes.get(i);
			for(int j = 0;i<etapes.size()&&!ok;i++){
				Place suiv = etapes.get(j);
				double distPrecNew = prec.getPlusCourtsChemins().get(newEtape).getDistTot();
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
			result = new ITIN(depart, arrivee);
			for(int i=0; i<etapes.size();i++){
				result.addEtape(etapes.get(i));
				if(etapes.get(i)==bestPrec){
					result.addEtape(newEtape);
				}
			}
		}
		return ok;
	}

	public ArrayList<Place> getEtapes(){
		return etapes;
	}
	
	public String toString(){
		String s = "";
		s = "Pour aller de " + depart.getId() + " à " + arrivee.getId();
		if(isPossible()){
		s+= " il faut passer par : ";
		for(Place e : etapes){
			s+="\n" + e.getId(); 
		}
		s+="\n Distance à vol d'oiseau : " + depart.getPosition().distance(arrivee.getPosition());
		}
		else{ s+=" il n'existe pas de chemin.";}
		return s;
	}
	
	public String toBeautifulString(int vitesse){
		String s = "Votre itineraire de " + depart.getId() + " à " + arrivee.getId() + " : ";
		int dureeActuelle = 0;
		double distanceActuelle = 0;
		double distanceTmp = 0;
		ordonnerEtapes();
		dureeActuelle = depart.getTav();
		s+="Visite de " + depart.getId() + " : " + depart.getTav() + " Minutes";;
		int i;
		for(i=1; i<etapes.size()-1;i++){
			distanceTmp = etapes.get(i-1).getPosition().distance(etapes.get(i).getPosition());
			distanceActuelle += distanceTmp;
			s+="\nMarchez " + distanceTmp + " km";
			dureeActuelle += 60*distanceTmp/vitesse + etapes.get(i).getTav();
			s+="\nVisite de " + etapes.get(i).getId() + " : " + etapes.get(i).getTav() + " Minutes";;
		}
		s+="\nMarchez " + etapes.get(etapes.size()-2).getPosition().distance(arrivee.getPosition())
		+"\nVisite de " + arrivee.getId() + " : " + arrivee.getTav() + " Minutes"
		+"\nVous êtes arrivés à destination."
		+"\nDistance parcourue : " + distanceActuelle
		+"\nTemps écoulé : " + dureeActuelle;
		return s;
	}

}
