import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class User implements UserInterface {
	
	private int id;
	
	private ArrayList<Tag> userranking;
	
	private boolean disabled;
	
	private int speed;
	
	private int time;
	
	private Place dep;
	
	private Place arr;
	
	private ArrayList<Place> inter;
	
	private ITIN dernierItinCalcule;
	
	public User (int id, ArrayList<Tag> userranking, boolean disabled, int speed, int time, Place dep, Place arr, ArrayList<Place> inter ) {
		this.dernierItinCalcule = null;
		this.id = id;
		this.userranking = userranking;
		this.disabled = disabled;
		this.speed = speed;
		this.time = time;
		this.dep = dep;
		this.arr=arr;
		this.inter = inter;
		
	}
	
	public User(int id,ArrayList<Tag> userranking,int time, Place dep, Place arr){
		
		this (id,userranking,false,5,time,dep,arr,new ArrayList<Place>());
	}
	
	public User (int id, int speed, int time, Place dep, Place arr){
		this (id,new ArrayList<Tag> (), false, speed, time, dep, arr, null);
	}

	public User (int time, Place dep, Place arr){
		this(0,time,dep,arr);
	}
	
	public User (int id, int time, Place dep, Place arr){
		this(id,new ArrayList<Tag>(),time,dep,arr);
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Tag> getUserranking() {
		return userranking;
	}

	public void setUserranking(ArrayList<Tag> userranking) {
		this.userranking = userranking;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Place getDep() {
		return dep;
	}

	public void setDep(Place dep) {
		this.dep = dep;
	}

	public Place getArr() {
		return arr;
	}

	public void setArr(Place arr) {
		this.arr = arr;
	}

	public ArrayList<Place> getInter() {
		return inter;
	}

	public void setInter(ArrayList<Place> inter) {
		this.inter = inter;
	}
	
	public int getScore(Tag t){
		return (this.getUserranking().size()-this.getUserranking().indexOf(t));
		
		
	}
	
	public void save() throws IOException{
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter("savings" + File.separator +"Users" + File.separator + this.getId() +".txt")));
		
		writer.println(this.getId());
		writer.println(this.getTime());
		writer.println(this.getDep().getId());
		writer.println(this.getArr().getId());
	}
	
	public static User restore (int userid, Graph graph) throws FileNotFoundException{
		String filename = "savings" + File.separator +"Users" + File.separator + userid +".txt";
		Scanner reader = new Scanner(new File (filename));
		int id = Integer.parseInt(reader.nextLine());
		int time = Integer.parseInt(reader.nextLine());
		int depID = Integer.parseInt(reader.nextLine());
		int arrID = Integer.parseInt(reader.nextLine());
		int okwith2 = 0;
		int i = 0;
		int indexOfdep=0;
		int indexOfarr=0;
		while (i<graph.getAllplaces().size() && okwith2<2){
			if (graph.getAllplaces().get(i).getId() == depID){
				indexOfdep = i;
				okwith2++;
			}
			if(graph.getAllplaces().get(i).getId() == arrID){
				indexOfarr = i;
				okwith2++;
			}
			i++;
			
		}
		
		return new User(id,time,graph.getAllplaces().get(indexOfdep),graph.getAllplaces().get(indexOfarr));
		/*Now we have to create 2 places from their id
		//int cityID = city.getId();
		int cityID = 0;
		String file2name = "savings" + File.separator + cityID + File.separator + cityID + ".txt";
		Scanner reader2 = new Scanner (new File (filename));
		//<Places>
		String toRead = reader2.nextLine();
		toRead=reader2.nextLine();
		int line = Integer.parseInt(toRead);
		ArrayList<Place> fromto = new ArrayList<Place>();
		while (!toRead.equals("</Places>") && fromto.size() !=2){
			if (line == depID){
				toRead=reader2.nextLine();
				
			}
			
		}*/
		
		
		
	}
	
	

}
