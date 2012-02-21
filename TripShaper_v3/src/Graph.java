import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;





public class Graph implements GraphInterface,Serializable {

	private static final long serialVersionUID = 1L;

	public final static int maxID = 5000;

	//public final static int zone_time = 2;

	private ArrayList<Place> allplaces;

	private ArrayList<Path> allpaths;

	private ArrayList<Tag> alltags;

	private User user;

	private Graphique dispersion;

	public Graph(){
		this((User)null);
	}

	public Graph(User user) {
		this(new ArrayList<Place>(), new ArrayList<Path>(), new ArrayList<Tag> (), user);
	}


	public Graph(ArrayList<Place> allplaces, ArrayList<Path> allpaths, ArrayList<Tag> alltags,User user){

		this.allplaces = allplaces;
		this.allpaths = allpaths;
		this.alltags= alltags;
		this.setUser(user);
	}

	//--------- Graph Generator------------

	public Graph(int nbPoints){
		this();
		try {
			this.setDispersion(new Graphique (nbPoints,2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Point> mesPoints = this.getDispersion().getMesPoints();
		ArrayList<Point[]> mesArcs = this.getDispersion().getEdges();
		HashMap<Point,Place> link = new HashMap<Point,Place>();
		ArrayList<Point> zone1Edges = this.getDispersion().getZonesRegroupement().get(0);
		ArrayList<Point> zone2Edges = this.getDispersion().getZonesRegroupement().get(1);
		//TO DO : ------------------------
		//Lorsque 2 points sont dans une zone de regroupement, le chemin est Path p = new Path(this,p1,p2,Graph.zone_time,0);
		//Génération des places
		for (int i=0;i<mesPoints.size();i++){
			//time : entre 0 et 120min ; score : entre 0 et 100;
			//Sur 4 places, on booste le score
			//A sa création, une place est déjà ajoutée à son graph
			if (i==nbPoints/2 || i == nbPoints/3 || i == nbPoints/6 || i==  nbPoints/10 ){
				Place p = new Place (this, mesPoints.get(i),10 + (int) (110*(1-Math.random())), (int)(100*(1-0.5*Math.random())));
				link.put(mesPoints.get(i),p);
			}
			else {
				Place p = new Place(this,mesPoints.get(i),10 + (int)(110*(1- Math.random())),(int)(100*(1-Math.random())));
				link.put(mesPoints.get(i),p);
			}
		}

		for (int i=0;i<mesArcs.size();i++){
			//time : entre 0 et 30min  ; score : etre 0 et 30
			Point point1 = mesArcs.get(i)[0];
			Point point2 = mesArcs.get(i)[1];
			Place p1 = link.get(point1);
			Place p2 = link.get(point2);
			if ((zone1Edges.contains(point1) && zone1Edges.contains(point2)) || (zone2Edges.contains(point1) && zone2Edges.contains(point2)) ){
				Path path = new Path(this,p1,p2,0);
			}
			else {
				Path path = new Path(this,p1,p2,(int)(30*Math.random()));
			}
		}




	}

	//---------------------------------


	public ArrayList<Place> getAllplaces() {
		return allplaces;
	}


	public void setAllplaces(ArrayList<Place> allplaces) {
		this.allplaces = allplaces;
	}

	public void addPlace(Place pl){
		if(!this.getAllplaces().contains(pl))
		this.allplaces.add(pl);
	}

	public ArrayList<Path> getAllpaths() {
		return allpaths;
	}


	public void setAllpaths(ArrayList<Path> allpaths) {
		this.allpaths = allpaths;
	}

	public void addPath(Path p){
		if (!this.getAllpaths().contains(p))
		this.allpaths.add(p);
	}

	public ArrayList<Tag> getAlltags() {
		return alltags;
	}

	public void setAlltags(ArrayList<Tag> alltags) {
		this.alltags = alltags;
	}

	public void addTag(Tag t){
		if (!this.getAlltags().contains(t))
		this.alltags.add(t);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Graphique getDispersion() {
		return dispersion;
	}

	public void setDispersion(Graphique graphic) {
		this.dispersion = graphic;
	}

	public boolean availableID(int id){
		int i =0;
		while(i<this.getAllplaces().size() && this.getAllplaces().get(i).getId() !=id){
			i++;
		}
		if (i<this.getAllplaces().size()){
			return false;
		}
		else {
			i=0;
			while(i<this.getAllpaths().size() && this.getAllpaths().get(i).getId() !=id){
				i++;
			}
			if (i<this.getAllpaths().size()){
				return false;
			}
			else {
				while(i<this.getAlltags().size() && this.getAlltags().get(i).getId() !=id){
					i++;
				}
				if (i<this.getAlltags().size()){
					return false;
				}
				else {
					return true;
				}

			}
		}
	}

	public Graph simplify() {
		Graph simple = new Graph(this.getUser());
		simple.setAlltags((this.getAlltags()));
		// Tri par rayon parcourable
		double distanceMaxParcourable = this.getUser().getSpeed()*60 * this.getUser().getTime();
		System.out.println(distanceMaxParcourable);
		for(Place s : this.getAllplaces()){
			if(this.getUser().getDep().getPosition().distance(s.getPosition())<distanceMaxParcourable/2){
				simple.addPlace(s);
				/*for(ARRET a : s.getListeArrets()){
					TRANSPORT t = a.getTransport();
					if(!transports.contains(t)){transports.add(t);}
				}*/
			}
		for (Path p : this.getAllpaths()){
			if (simple.getAllplaces().contains(p.getP1()) && simple.getAllplaces().contains(p.getP2())){
				simple.addPath(p);
			}
		}
		}
		// -------------------------
		return simple;


	}

	public Route solve() {
		return new Route();
			}



	public void save() throws IOException {
		/*System.out.println("before serialization:" + this);
		//XMLEncoder encoder = new XMLEncoder(new FileOutputStream(File.separator+ "savings" + File.separator + "Graph" + this.getUser().getId() +".xml"));
		XMLEncoder encoder = new XMLEncoder(new FileOutputStream("savings" + File.separator +"Graphtest.xml"));
		encoder.writeObject(this);
		encoder.close();*/

		//POUR LE TEST UNIQUEMENT
		//this.user = new User(300,this.getAllplaces().get(1),this.getAllplaces().get(1));


		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter("savings" + File.separator +"Graph" + this.getUser().getId() +".txt")));
		writer.println("<User>");
		writer.println(this.getUser().getId());
		writer.println(this.getUser().getTime());
		writer.println(this.getUser().getDep().getId());
		writer.println(this.getUser().getArr().getId());
		writer.println("</User>");
		System.out.println("Writing places ID");
		writer.println("<Places>");
		for (Place p : this.getAllplaces()){
			writer.println(p.getId());
			writer.println((int)p.getPosition().getX());
			writer.println((int)p.getPosition().getY());
			writer.println(p.getTav());
			writer.println(p.getBasicscore());
		}
		writer.println("</Places>");
		System.out.println("Writing paths ID");
		writer.println("<Paths>");
		for (Path p : this.getAllpaths()){
			writer.println(p.getId());
			writer.println(p.getP1().getId());
			writer.println(p.getP2().getId());
			writer.println(p.getTime());
			writer.println(p.getScore());

		}
		writer.println("</Paths>");
		System.out.println("Writing Tags ID");
		writer.println("<Tags>");
		for (Tag t : this.getAlltags()){
			writer.println(t.getId());
		}
		writer.println("</Tags>");

		writer.close();
	}

	public void display() {
		int xWindow=Display1.xWindow;
		int yWindow=Display1.yWindow;
		JFrame frame = new JFrame(); 

		frame.setSize(new Dimension(xWindow+50,yWindow+50)); 

		ArrayList<Place> allplaces = this.getAllplaces();
		ArrayList<Path> allpaths = this.getAllpaths();
		for (Place p : allplaces) {
			System.out.println(p);
		}
		for (Path path : allpaths){
			System.out.println(path);
		}

		MonCompo compo = new MonCompo(this.getDispersion());
		frame.add(compo); 
		frame.setVisible(true);
	}

	public static Graph restore(int userid) throws FileNotFoundException {
		/*XMLDecoder decoder = new XMLDecoder(new FileInputStream("Graph" + userid + ".xml"));
		Graph g = (Graph) decoder.readObject();
		decoder.close();

		System.out.println("after deserialization:" + g);

		return g;*/
		Graph g = new Graph();
		String filename = "savings" + File.separator +"Graph" + userid +".txt";
		Scanner reader = new Scanner(new File (filename));
		//<User>
		String toRead = reader.nextLine();
		System.out.println(toRead);
		//userid (already known)
		toRead = reader.nextLine();
		int time = Integer.parseInt(reader.nextLine());
		int depid = Integer.parseInt(reader.nextLine());
		int arrid = Integer.parseInt(reader.nextLine());
		//</User>
		toRead = reader.nextLine();
		//<Places>
		toRead = reader.nextLine();
		//This hashmap matches places to their ids
		HashMap<Integer,Place> placemap = new HashMap<Integer,Place>();
		toRead = reader.nextLine();
		do {
			int id = Integer.parseInt(toRead);
			toRead=reader.nextLine();
			int x = Integer.parseInt(toRead);
			toRead=reader.nextLine();
			int y = Integer.parseInt(toRead);
			Point position = new Point(x,y);
			toRead=reader.nextLine();
			int placetime = Integer.parseInt(toRead);
			toRead=reader.nextLine();
			int placescore = Integer.parseInt(toRead);
			Place p = new Place(g,position,placetime,placescore);
			p.setId(id);
			placemap.put(id, p);

			toRead=reader.nextLine();
		}	
		while (!toRead.equals("</Places>"));
		User user = new User (userid,time,placemap.get(depid),placemap.get(arrid));
		g.setUser(user);
		//<Paths>
		toRead = reader.nextLine();

		toRead = reader.nextLine();
		do {
			int id = Integer.parseInt(toRead);
			toRead=reader.nextLine();
			Place p1 = placemap.get(Integer.parseInt(toRead));
			toRead=reader.nextLine();
			Place p2 = placemap.get(Integer.parseInt(toRead));
			toRead=reader.nextLine();
			int pathtime = Integer.parseInt(toRead);
			toRead=reader.nextLine();
			int pathscore = Integer.parseInt(toRead);

			Path p = new Path (g,id,p1,p2,pathtime,pathscore);
			toRead = reader.nextLine();

		}
		while(!toRead.equals("</Paths>"));
		return g;

	}



}