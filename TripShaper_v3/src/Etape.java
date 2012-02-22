public class Etape {
		private Place p;
		private NiveauTemps niveauDeTempsReste;
		Etape(Place p, NiveauTemps niveauDeTempsReste){
			this.p = p;
			this.niveauDeTempsReste = niveauDeTempsReste;
		}
		public Place getPlace(){return p;}
		public NiveauTemps getNiveauTemps(){return niveauDeTempsReste;}
		public boolean equals(Object o){
			if (o instanceof Etape) {
				return ((Etape)o).getPlace().equals(getPlace());
			}else{return false;}
		}
	}