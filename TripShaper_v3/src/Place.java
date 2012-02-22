import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;



public class Place implements PlaceInterface {
	
	private Graph graph;
	
	private int id;
	
	private Point position;
	
	private int tav;
	
	private int sigma;
	
	private int tmin;
	
	private int tmax;
	
	private int basicscore;
	
	private int minscore;
	

	private HashMap<Place,ITIN> itineraires;
	
	
	/*
	 * Si on utilise le regroupement :
	 * 
	 *  On garde les variables d'instances graph,id,position
	 * 
	 * Toutes les activités du lieu
	 * private ArrayList<Activity> allactivities;
	 * 
	 * L'activité choisie
	 * private Activity electedActivity
	 * 
	 * Le score de l'activité choisie
	 * private int score;
	 * 
	 * Le temps de l'activité choisie
	 * private int time;
	 * 
	 * 
	 */
	
	
	public Place (Graph graph,int id, Point position, int tmoy, int ecarttype){
		this.graph = graph;
		this.id = id;
		this.position = position;
		this.tav = tmoy;
		this.sigma = ecarttype;
		this.tmin=this.minimumTime();
		this.tmax=this.maximumTime();
		this.basicscore = this.basicScore();
		this.minscore=this.minimumScore();
		
		this.graph.addPlace(this);
		this.graph.putPlaceID(this.id, this);
		
	}

	//Constructors for testing
	public Place (Graph graph, Point position, int time, int score){
		this.graph = graph;
		this.setId(this.getGraph().getDispersion().getPointsIDhashmap().get(position));
		this.position = position;
		this.tav = time;
		this.basicscore = score;
		
		this.graph.addPlace(this);
		this.graph.putPlaceID(this.id, this);
		
	}
	
	public Place (Graph graph, int id, Point position){
		this.graph = graph;
		this.id=id;
		this.position = position;
		
		this.graph.addPlace(this);
		this.graph.putPlaceID(this.id, this);
	}
	
	public Place (Graph graph, int id){
		this(graph,id,new Point(),0,0);
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
	public int getTav() {
		return tav;
	}

	public void setTav(int tmoy) {
		this.tav = tmoy;
	}

	public int getSigma() {
		return sigma;
	}

	public void setSigma(int ecarttype) {
		this.sigma = ecarttype;
	}

	public int getTmin() {
		return tmin;
	}

	public void setTmin(int tmin) {
		this.tmin = tmin;
	}

	public int getTmax() {
		return tmax;
	}

	public void setTmax(int tmax) {
		this.tmax = tmax;
	}

	public int getBasicscore() {
		return basicscore;
	}

	public void setBasicscore(int basicscore) {
		this.basicscore = basicscore;
	}

	public int getMinscore() {
		return minscore;
	}

	public void setMinscore(int minscore) {
		this.minscore = minscore;
	}
	
	public int scoreMax(){
		int nbTag = this.getGraph().getAlltags().size();
		int nbPlaces = this.getGraph().getAllplaces().size();
		int nbPaths = this.getGraph().getAllpaths().size();
		int maxrank = nbPlaces + nbPaths;
		//The maximum score that a place can get is when it is top-ranked for every tag.
		//In any case, the user will have to rank every tag, so we can factorize
		//1 + 2 + 3 +... N = N(N-1)/2
		return (maxrank*(nbTag*(nbTag-1)/2));
	}
	
	public int basicScore(){
		int score = 0;
		for (Tag t : this.getGraph().getAlltags()){
			score += this.getGraph().getUser().getScore(t)*(t.getPlaceranking().size() - t.getPlaceranking().indexOf(this));
		}
		
		return score;
		
	}
	
	public int minimumTime(){
		return (2*(this.getTav() - this.getSigma())*(this.basicScore()/this.scoreMax()));
	
	}
	
	public int maximumTime() {
		return (2*this.getTav()*this.basicScore()/this.scoreMax());
	}
	
	public int minimumScore(){
		return this.scoreMax()*this.minimumTime()/this.maximumTime();
	}
	

	public String toString(){
		return id + " : " + "(" + this.getPosition().getX() + "," + this.getPosition().getY() + ") ; score : " + this.getBasicscore() + " ; time : " + this.getTav();
		
	}

	public HashMap<Place,ITIN> getPlusCourtsChemins() {
		return itineraires;
	}

	public void setPlusCourtsChemins(HashMap<Place,ITIN> itineraires) {
		this.itineraires = itineraires;
	}
	
	public boolean equals(Object o){
		return (o instanceof Place && ((Place) o).getId() == this.getId());
	}

	public ArrayList<Place> getSommetsAtteignables(Graph graph){
		//updateModifs();
		ArrayList<Place> att = new ArrayList<Place>();
		ArrayList<Place> allplaces = this.getGraph().getAllplaces();
		for (Path p  : this.getGraph().getAllpaths()){
			if (this.equals(p.getP1()) && allplaces.contains(p.getP2())){
				att.add(p.getP2());
			}
			else {
				if (this.equals(p.getP2()) && allplaces.contains(p.getP1())){
					att.add(p.getP1());
				}
			}
		}
		
		
	return att;
	}
	
	public int rankToScore(){
		
		int score = 0;
		for (Tag t : this.getGraph().getAlltags()){
			score += t.getPlaceranking().size()-t.getPlaceranking().indexOf(t);
			
		}
		
		
		return score;
	}
	
	public int getTime(NiveauTemps niv){
		if (niv == NiveauTemps.PAS_DE_VISITE){
			return 0;
		}
		else {
			return this.getTav();
			}
	
	}
	

	
}
