import java.awt.Point;


public interface PlaceInterface {

	/**
	 * 
	 * @return the graph this place belongs to
	 */
	public Graph getGraph();
	
	/**
	 * Sets the graph of the place
	 * @param g
	 */
	public void setGraph(Graph g);
	
	/**
	 * 
	 * @return the id of the place
	 */
	public int getId();
	
	/**
	 * Sets the id of the place
	 * @param id
	 */
	public void setId(int id);
	
	/**
	 * 
	 * @return the coordinates of the place (point)
	 */
	public Point getPosition();
	
	/**
	 * Sets the point (coordinates) where the place is
	 * @param p
	 */
	public void setPosition(Point p);
	
	/**
	 * 
	 * @return the average time spent on this place (given by the tourist office)
	 */
	public int getTav();
	
	/**
	 * Sets the average time spent on this place
	 * @param t
	 */
	public void setTav(int t);
	
	/**
	 * 
	 * @return the standard deviation for the time spent on this place
	 */
	public int getSigma();
	
	/**
	 * Sets the standard deviation for the time spent on this place
	 * @param t
	 */
	public void setSigma(int t);
	
	/**
	 * 
	 * @return the minimal advised time the user should spend on this place
	 */
	public int getTmin();
	
	/**
	 * Sets the minimal advised time the user should spend on this place
	 * @param t
	 */
	public void setTmin(int t);
	
	/**
	 * 
	 * @return the maximal advised time the user should spend on this place
	 */
	public int getTmax();
	
	/**
	 * Sets the maximal advised time the user should spend on this place
	 * @param t
	 */
	public void setTmax(int t);
	
	/**
	 * 
	 * @return the basic score when time spent by the user is maximal
	 */
	public int getBasicscore();
	
	/**
	 * Sets the basic score when time spent by the user is maximal
	 * @param score
	 */
	public void setBasicscore(int score);
	
	/**
	 * 
	 * @return the score when time spent by the user is minimal
	 */
	public int getMinscore();
	
	/**
	 * Sets the score when time spent by the user is minimal
	 * @param score
	 */
	public void setMinscore(int score);
	
	/**
	 * Calculates the basic score of the place according to the user tag ranking + place ranking by tag
	 * @return
	 */
	public int basicScore();
	
	/**
	 * 
	 * @return the maximum score a place could have
	 */
	public int scoreMax();
	
	/**
	 * Calculates the minimum time to spend by the user if he/she visits the place
	 * @return
	 */
	public int minimumTime();
	
	/**
	 * Calculates the maximum time to spend by the user if he/she visits the place
	 * @return
	 */
	public int maximumTime();
	
	/**
	 * Calculates the score if the user spends the minimum advised time in the place
	 * @return
	 */
	public int minimumScore();
	
	
	
	
}
