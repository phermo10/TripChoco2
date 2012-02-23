import java.awt.Point;
import java.util.ArrayList;


public class Path implements PathInterface {
	
	private Graph graph;
	
	private int id;
	
	private Place p1;
	
	private Place p2;
	
	private double dist;
	
	private int score;
	
	private int time;

	
	public Path(Graph graph, int id, Place p1, Place p2, double dist, ArrayList<Tag> alltags){
		this.setGraph(graph);
		this.id = id;
		this.p1 = p1;
		this.p2 = p2;
		this.dist=dist;
		this.score = this.rankToScore();
		this.setTime((int) (this.getDist()/(this.getGraph().getUser().getSpeed())));
		
		this.graph.addPath(this);
		this.graph.putPathID(this.id, this);
		
	}
	
	//Constructors for testing
	/*public Path (Graph graph, Place p1, Place p2, int time, int score){
		this.graph = graph;
		this.p1=p1;
		this.p2 = p2;
		this.score=score;
		this.time = time;
		//Random id :
		do {
			this.id = (int) (Graph.maxID*Math.random());
		}
		while (!this.getGraph().availableID(this.id));
		
		this.graph.addPath(this);
	}*/
	
	public Path (Graph graph, int id, Place p1, Place p2){
		this(graph,id,p1,p2,Integer.MAX_VALUE,Integer.MAX_VALUE);
	}
	public Path(Graph graph, int id, Place p1, Place p2, int time, int score){
		this.graph = graph;
		this.p1=p1;
		this.p2 = p2;
		this.score=score;
		this.time = time;
		this.id=id;
		
		this.graph.addPath(this);
		this.graph.putPathID(this.id,this);
	}
	
	public Path(Graph graph, Place p1, Place p2, int score){
		this.graph = graph;
		this.p1=p1;
		this.p2 = p2;
		this.score=score;
		this.dist = p1.getPosition().distance(p2.getPosition());
		System.out.println("p1.getPosition() : "+p1.getPosition());
		System.out.println("p2.getPosition() : "+p2.getPosition());
		Point[] edge = {p1.getPosition(),p2.getPosition()};
		//Point[] edgebis = {p2.getPosition(),p1.getPosition()};
		System.out.println("edge  "+edge[0].getX()+","+edge[0].getY()+";"+edge[1].getX()+","+edge[1].getY());
		for (int i=0;i<this.getGraph().getDispersion().getEdgesIDhashmap().size();i++){
			//System.out.println("getEdgesIDhashmap : "+this.getGraph().getDispersion().getEdgesIDhashmap().get(this.)
		}
		System.out.println("getEdgesIDhashmap : "+this.getGraph().getDispersion().getEdgesIDhashmap());
		System.out.println(this.getGraph().getDispersion().getEdgesIDhashmap().get(edge));
	    this.setId(this.getGraph().getDispersion().getEdgesIDhashmap().get(edge));
		
		this.graph.addPath(this);
		this.graph.putPathID(this.id,this);
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

	public Place getP1() {
		return p1;
	}

	public void setP1(Place p1) {
		this.p1 = p1;
	}

	public Place getP2() {
		return p2;
	}

	public void setP2(Place p2) {
		this.p2 = p2;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int d) {
		this.time = d;
	}

	public int rankToScore () {
		
		/*int score = 0;
		for (Tag t : this.getGraph().getAlltags()){
			score += t.getPathranking().size()-t.getPathranking().indexOf(t);
			
		*/
		
		int score = 0;
		for (Tag t : this.getGraph().getAlltags()){
			score += this.getGraph().getUser().getScore(t)*(t.getPathranking().size() - t.getPlaceranking().indexOf(this));
		}
		
		return score;
		
	}

		
	
	public String toString(){
		return id + " : (from " + p1.getId() + " to " + p2.getId() + ") ; score : " + this.getScore() + " ; time : " + this.getTime();
		
	}
	
	public boolean equals(Object o){
		return (o instanceof Path && ((Path) o).getId() == this.getId());
	}
	

}
