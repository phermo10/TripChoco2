

public interface PathInterface {
	
	/**
	 * 
	 * @return the graph that this path belongs to
	 */
	public Graph getGraph();
	
	/**
	 * Sets a graph to the path
	 * @param g
	 */
	public void setGraph(Graph g);
	
	/**
	 * 
	 * @return the id of the path
	 */
	public int getId();
	
	/**
	 * Sets the id of the path
	 * @param id
	 */
	public void setId (int id);
	
	
	/**
	 * 
	 * @return an edge of the path
	 */
	public Place getP1();
	
	/**
	 * Sets an edge to the path
	 * @param p
	 */
	public void setP1(Place p);
	
	/**
	 * 
	 * @return an edge of the path
	 */
	public Place getP2();
	
	/**
	 * Sets an edge to the path
	 * @param p
	 */
	public void setP2(Place p);
	
	/**
	 * 
	 * @return the length of the path (in kilometers)
	 */
	public double getDist();
	
	/**
	 * Sets the length of the path
	 * @param dist (in kilometers)
	 * 
	 */
	public void setDist(double dist);
	
	/**
	 * 
	 * @return the satisfaction score of the path
	 */
	public int getScore();
	
	/**
	 * Sets the satisfaction score of the path
	 * @param score
	 */
	public void setScore(int score);
	

	/**
	 * 
	 * @return the time the user spends on this path (in minutes)
	 */
	public int getTime();
	
	/**
	 * Sets the time the user spends on this path
	 * @param t (in minutes)
	 */
	public void setTime(int t);
	
	/**
	 * Converts the tag ranks of the path to a score
	 * @return the score of the path 
	 */
	public int rankToScore();

}
