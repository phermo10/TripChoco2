import java.util.ArrayList;


public class User implements UserInterface {
	
	private int id;
	
	private ArrayList<Tag> userranking;
	
	private boolean disabled;
	
	private int speed;
	
	private int time;
	
	private Place dep;
	
	private Place arr;
	
	private ArrayList<Place> inter;
	
	
	public User (int id, ArrayList<Tag> userranking, boolean disabled, int speed, int time, Place dep, Place arr, ArrayList<Place> inter ) {
		
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

}
