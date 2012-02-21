import java.util.ArrayList;


public class Route implements RouteInterface {
	
	private ArrayList<Integer> idlist;
	
	private ArrayList<Integer> timelist;
	
	private User user;
	
	public Route(){
		this(new ArrayList<Integer>(), new ArrayList<Integer>());
	}
	
	public Route(ArrayList<Integer> idlist, ArrayList<Integer> timelist){
		this.setIdlist(idlist);
		this.setTimelist(timelist);
	}

	public ArrayList<Integer> getIdlist() {
		return idlist;
	}

	public void setIdlist(ArrayList<Integer> idlist) {
		this.idlist = idlist;
	}

	public void addId(int id){
		this.idlist.add(id);
	}
	public ArrayList<Integer> getTimelist() {
		return timelist;
	}

	public void setTimelist(ArrayList<Integer> timelist) {
		this.timelist = timelist;
	}
	
	public void addTime(int time){
		this.timelist.add(time);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString(){
		String s = "";
		for (int i=0;i<this.getIdlist().size();i++){
			s+= this.getIdlist().get(i) + "\n";
			s+= this.getTimelist().get(i) + "\n";
			
		}
		
		return s;
	}
	

}
