import java.io.IOException;
import java.util.ArrayList;


public interface GraphInterface {

	/**
	 * 
	 * @return the list of all the places the graph contains
	 */
	public ArrayList<Place> getAllplaces();
	
	/**
	 * Sets a new arraylist of places
	 * @param places
	 */
	public void setAllplaces(ArrayList<Place> places);
	
	
	/**
	 * 
	 * @return the list of all the tags
	 */
	public ArrayList<Tag> getAlltags();
	
	/**
	 * Sets a new arraylist of tags
	 * @param tags
	 */
	public void setAlltags(ArrayList<Tag> tags);
	
	/**
	 * Adds a new tag
	 * @param t
	 */
	public void addTag(Tag t);
	
	/**
	 * 
	 * @return the user who ordered the graph
	 */
	public User getUser();
	
	/**
	 * Sets the user of the graph
	 * @param user
	 */
	public void setUser(User user);
	
	
	/**
	 * Changes the graph in order to ignore some places or paths
	 */
	public Graph simplify();
	
	/**
	 * 
	 * @return the best route for the user
	 */
	public void solve() throws Exception;
	
	/**
	 * Saves the Graph to a textfile
	 * Structure of the file :
	 * <User>
	 * userid
	 * </User>
	 * <Places>
	 * id1
	 * id2
	 * ...
	 * </Places>
	 * <Paths>
	 * id1
	 * id2
	 * ...
	 * </Paths>
	 * <Tags>
	 * id1
	 * id2
	 * ...
	 * </Tags>
	 * @throws IOException 
	 */
	public void save() throws IOException;
	
	/**
	 * Displays the graph 
	 */
	public void display();
	
	
}
