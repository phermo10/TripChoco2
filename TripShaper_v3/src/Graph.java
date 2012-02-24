import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;





public class Graph implements GraphInterface,Serializable {

	private static final long serialVersionUID = 1L;

	public final static int maxID = 5000;

	//public final static int zone_time = 2;

	private ArrayList<Place> allplaces;
	private HashMap<Place,HashMap<Place,ITIN>> allShPa;

	//private HashMap<Integer,Place> placesID;

	//private ArrayList<Path> allpaths;

	//private HashMap<Integer,Path> pathsID;

	private ArrayList<Tag> alltags;

	private HashMap<Integer,Tag> tagsID;

	private User user;

	//private Graphique dispersion;

	private ITIN bestPath;

	private HashMap<Place,Integer> scores;
	
	private int cityID;
	
	/**
	 * diametre de la ville en kilometre
	 */
	private int cityDiameter;
	
	
	public Graph(int userID, int cityID) throws IOException{
		this.cityID = cityID;
		CityGenerator citygen = new CityGenerator(cityID);
		this.cityDiameter = citygen.getCityDiameter();
		this.allplaces = citygen.getPlaces();
		this.allShPa = citygen.getTousPCC();
		//Now we have to put scores and times on both places and paths
		String filename = Emplacements.FICHIER_USER_COMPLET(cityID, userID);
		Scanner reader = new Scanner(new File (filename));
		String toRead = reader.nextLine();		//<User>
		int userId = Integer.parseInt(reader.nextLine());
		int speed = Integer.parseInt(reader.nextLine());
		int time = Integer.parseInt(reader.nextLine());
		int depX = Integer.parseInt(reader.nextLine());
		int depY = Integer.parseInt(reader.nextLine());
		int arrX = Integer.parseInt(reader.nextLine());
		int arrY = Integer.parseInt(reader.nextLine());
		Place depTmp = new Place(new Point(depX,depY), -1);
		Place arrTmp = new Place(new Point(arrX,arrY), -1);
		int indexDep = this.allplaces.indexOf(depTmp);
		int indexArr = this.allplaces.indexOf(arrTmp);
		this.user = new User(userId,new ArrayList<Tag>(), false, speed, time, allplaces.get(indexDep) , allplaces.get(indexArr), new ArrayList<Place>());
		reader.nextLine(); // </User>
			
		reader.nextLine(); // <Places>
		int placeIndex = 0;
		while((toRead=reader.nextLine())!="</Places>"){
			int pScore = Integer.parseInt(toRead);
			scores.put(this.allplaces.get(placeIndex), pScore);
			placeIndex++;
		}
		//TODO les tags ne sont pas lus
		reader.close();
		
	}
	public Graph(HashMap<Place,HashMap<Place,ITIN>> allShPa, ArrayList<Place> allplaces, ArrayList<Tag> alltags, User user, HashMap<Place,Integer> scores, int cityID, int cityDiameter){
		this.cityDiameter = cityDiameter;
		this.allShPa = allShPa;
		this.cityID = cityID;
		this.bestPath = null;
		this.allplaces = allplaces;
		this.scores = scores;
		this.alltags= alltags;
		this.user = user;
		this.tagsID = new HashMap<Integer,Tag>();
		for (Tag t : this.alltags){
			this.tagsID.put(t.getId(),t);
		}
	}

	public int getCityID(){return this.cityID;}
	
	public ArrayList<Tag> getAlltags() {
		return alltags;
	}

	public HashMap<Place,HashMap<Place,ITIN>> getAllShPa(){
		return this.allShPa;
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

	public HashMap<Integer,Tag> getTagsID() {
		return tagsID;
	}

	public void setTagsID(HashMap<Integer,Tag> tagsID) {
		this.tagsID = tagsID;
	}

	public void putTagID (int tagid, Tag tag){
		this.tagsID.put(tagid,tag);
	}

	public Graph simplify() {
		return this;
		/*ArrayList<Place> places = new ArrayList<Place>();
		// Tri par rayon parcourable
		double rayonMaxParcourable = this.getUser().getSpeed() * this.getUser().getTime() / 2;
		System.out.println("Rayon max parcourable " + rayonMaxParcourable);
		for(Place pl : this.getAllplaces()){
			if(this.getUser().getDep().getPosition().distance(pl.getPosition())*coeffDistance()<rayonMaxParcourable){
				places.add(pl);
				System.out.println("Add " + pl);
			}
		}
		return new Graph(allShPa, places,this.getAlltags(),this.getUser(),this.getScores(),this.cityID,this.cityDiameter);*/
	}

	public HashMap<Place, Integer> getScores() {
		return scores;
	}

	public void setScores(HashMap<Place, Integer> scores) {
		this.scores = scores;
	}

	public void solve() {
		Solver s = new Solver(this);
		this.bestPath = s.computeBestPath();
	}



	public void save() throws IOException {
		File dir = new File(Emplacements.DOSSIER_GRAPH_COMPLET(this.cityID));
		dir.mkdirs();
		File f = new File(Emplacements.FICHIER_USER_COMPLET(this.cityID,this.getUser().getId()));
		f.createNewFile();
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter(Emplacements.FICHIER_USER_COMPLET(this.cityID,this.getUser().getId()))));
		writer.println("<User>");
		writer.println(this.getUser().getId());
		writer.println(this.getUser().getSpeed());
		writer.println(this.getUser().getTime());
		writer.println(this.getUser().getDep().getPosition().x);
		writer.println(this.getUser().getDep().getPosition().y);
		writer.println(this.getUser().getArr().getPosition().x);
		writer.println(this.getUser().getArr().getPosition().y);
		writer.println("</User>");
		writer.println("<Places>");
		for (int i = 0; i<this.getAllplaces().size();i++){
			writer.println(this.scores.get(this.getAllplaces().get(i)));
		}
		writer.println("</Places>");
		writer.close();
	}

	public void display() {
		int xWindow=Display1.xWindow;
		int yWindow=Display1.yWindow;
		JFrame frame = new JFrame(); 

		frame.setSize(new Dimension(xWindow+50,yWindow+50)); 

		MonCompo compo;
		if(this.bestPath!=null){
		compo = new MonCompo(this,bestPath);}
		else{compo = new MonCompo(this);}
		frame.add(compo); 
		frame.setVisible(true);
	}

	public ArrayList<Place> getAllplaces() {
		return allplaces;
	}

	public void setAllplaces(ArrayList<Place> allplaces) {
		this.allplaces = allplaces;
	}

	public double coeffDistance(){
		return cityDiameter/Math.min(Display1.xWindow, Display1.yWindow);
	}

}