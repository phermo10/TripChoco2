import java.util.ArrayList;


public interface TagInterface {
	
	/**
	 * 
	 * @return the id of the tag
	 */
	public int getId();
	
	/**
	 * Sets the id of the tag
	 * @param id
	 */
	public void setId(int id);
	
	/**
	 * 
	 * @return tag name
	 */
	public String getName();
	
	/**
	 * Sets the name of the tag
	 * @param s
	 */
	public void setName (String s);
	
	/**
	 * 
	 * @return the place ranking for this tag (given by the tourist office)
	 */
	public ArrayList<Place> getPlaceranking();
	
	/**
	 * Sets the place ranking
	 * @param placeranking
	 */
	public void setPlaceranking(ArrayList<Place> placeranking);
	
	/**
	 * Adds a place at the end of the placeranking
	 * @param p
	 */
	public void addPlace(Place p);
	
	/**
	 * Swaps p1 with p2 in the place ranking
	 * @param p1
	 * @param p2
	 */
	public void swapPlaces (Place p1, Place p2);
	
	/**
	 * 
	 * @return the path ranking for this tag (given by the tourist office)
	 */
	public ArrayList<Path> getPathranking();
	
	/**
	 * Sets the path ranking
	 * @param pathranking
	 */
	public void setPathranking(ArrayList<Path> pathranking);
	
	/**
	 * Adds a path at the end of the path ranking
	 * @param p
	 */
	public void addPath(Path p);
	
	/**
	 * Swaps p1 with p2 in the path ranking
	 * @param p1
	 * @param p2
	 */
	public void swapPath(Path p1, Path p2);

}
