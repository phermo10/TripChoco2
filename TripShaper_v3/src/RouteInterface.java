import java.util.ArrayList;


public interface RouteInterface {

	/**
	 * 
	 * @return the list of the ids of places or paths of the route (in the right order)
	 */
	public ArrayList<Integer> getIdlist();
	
	/**
	 * Sets the list of the ids of places or paths of the route
	 * @param idlist
	 */
	public void setIdlist(ArrayList<Integer> idlist);
	
	/**
	 * Adds an id to the end of the idlist
	 * @param id
	 */
	public void addId(int id);
	
	/**
	 * 
	 * @return the list of the times spent in the place of the same index in the idlist
	 */
	public ArrayList<Integer> getTimelist();
	
	/**
	 * Sets the list of the times spent in every place
	 * @param timelist
	 */
	public void setTimelist(ArrayList<Integer> timelist);
	
	/**
	 * Adds a time to the end of the timelist
	 * @param time
	 */
	public void addTime(int time);
	
	/**
	 * 
	 * @return the user who ordered the route
	 */
	public User getUser();
	
	/**
	 * Sets a user to the route
	 * @param us
	 */
	public void setUser(User us);
	
	/**
	 * Returns a String containing the information of the route, using the following syntax :
	 * ID1
	 * time1
	 * ID2
	 * time2
	 * ...
	 * @return the information of the route
	 */
	public String toString();
	
	
}
