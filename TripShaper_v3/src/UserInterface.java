import java.util.ArrayList;


public interface UserInterface {

	/**
	 * 
	 * @return the id of the user order
	 */
	public int getId();
	
	/**
	 * Sets the id of the user order
	 * @param id
	 */
	public void setId(int id);
	
	/**
	 * 
	 * @return the tag ranking of the user
	 */
	public ArrayList<Tag> getUserranking();
	
	/**
	 * Sets the tag ranking of the user
	 * @param tagranking
	 */
	public void setUserranking(ArrayList<Tag> tagranking);
	
	/**
	 * 
	 * @return if the user is disabled or not
	 */
	public boolean isDisabled();
	
	/**
	 * Sets the state of the user (disabled / not disabled)
	 * @param dis
	 */
	public void setDisabled(boolean dis);
	
	/**
	 * 
	 * @return the speed of the user (in m/s)
	 */
	public double getSpeed();
	
	/**
	 * Sets the speed of the user
	 * @param speed
	 */
	public void setSpeed(int speed);
	
	/**
	 * 
	 * @return the time the user has for his visit (in seconds)
	 */
	public int getTime();
	
	/**
	 * Sets the available time of the user for the visit
	 * @param time
	 */
	public void setTime(int time);
	
	/**
	 * 
	 * @return the user departure place
	 */
	public Place getDep();
	
	/**
	 * Sets the user departure place
	 * @param dep
	 */
	public void setDep(Place dep);
	
	/**
	 * 
	 * @return the user arrival place
	 */
	public Place getArr();
	
	/**
	 * Sets the user arrival place
	 * @param arr
	 */
	public void setArr(Place arr);
	
	/**
	 * 
	 * @return the list of intermediate places the user wants to go to
	 */
	public ArrayList<Place> getInter();
	
	/**
	 * Sets the list of intermediate places the user will go to
	 * @param inter
	 */
	public void setInter(ArrayList<Place> inter);
	
	/**
	 * Calculates the score of a tag thanks to the user ranking of tags.
	 * @param t
	 * @return the score of the tag t
	 */
	public int getScore(Tag t);
	
	
}
