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
	private ArrayList<HashMap<Place,ITIN>> pccITIN;

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
	
	
	public Graph(int userID, int cityID) throws IOException{
		this.cityID = cityID;
		CityGenerator citygen = new CityGenerator(cityID);
		this.allplaces = citygen.getPlaces();
		this.pccITIN = citygen.getPCC();
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
		Place arrTmp = new Place(new Point(depX,depY), -1);
		int indexDep = this.allplaces.indexOf(depTmp);
		int indexArr = this.allplaces.indexOf(arrTmp);
		this.user = new User(userId,new ArrayList<Tag>(), false, speed, time, allplaces.get(indexDep) , allplaces.get(indexArr), new ArrayList<Place>());
		reader.nextLine(); // </User>
			
		reader.nextLine(); // <Places>
		int placeIndex = 0;
		while((toRead=reader.nextLine())!="</Places>"){
			int pScore = Integer.parseInt(reader.nextLine());
			scores.put(this.allplaces.get(placeIndex), pScore);
			placeIndex++;
		}
		//TODO les tags ne sont pas lus
		reader.close();
		
	}
	public Graph(ArrayList<HashMap<Place,ITIN>> pccITIN, ArrayList<Place> allplaces, ArrayList<Tag> alltags, User user, HashMap<Place,Integer> scores, int cityID){
		this.pccITIN = pccITIN;
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

	public ArrayList<HashMap<Place,ITIN>> getTousPCC(){
		return this.pccITIN;
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
		ArrayList<Place> places = new ArrayList<Place>();
		// Tri par rayon parcourable
		double distanceMaxParcourable = this.getUser().getSpeed()*60 * this.getUser().getTime();
		System.out.println(distanceMaxParcourable);
		for(Place pl : this.getAllplaces()){
			if(this.getUser().getDep().getPosition().distance(pl.getPosition())<distanceMaxParcourable/2){
				places.add(pl);
				/*for(ARRET a : s.getListeArrets()){
					TRANSPORT t = a.getTransport();
					if(!transports.contains(t)){transports.add(t);}
				}*/
			}
		}
		return new Graph(pccITIN, places,this.getAlltags(),this.getUser(),this.getScores(),this.cityID);
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

		ArrayList<Place> allplaces = this.getAllplaces();
		for (Place p : allplaces) {
			System.out.println(p);
		}

		MonCompo compo = new MonCompo(this.getAllplaces());
		frame.add(compo); 
		frame.setVisible(true);
	}

	public ArrayList<Place> getAllplaces() {
		return allplaces;
	}

	public void setAllplaces(ArrayList<Place> allplaces) {
		this.allplaces = allplaces;
	}



}