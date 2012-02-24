import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;



public class Place {
	
	Point position;
	ArrayList<Path> pathsFromThisPlace;
	int averageTime;

	public Place (Point position,ArrayList<Path> paths,int averageTime){
		this.position=position;
		this.pathsFromThisPlace=paths;
		this.averageTime=averageTime;
	}
	
	public Place (Point position,int averageTime){
		this.position=position;
		this.pathsFromThisPlace=new ArrayList<Path>();
		this.averageTime=averageTime;
	}

	public int getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(int averageTime) {
		this.averageTime = averageTime;
	}

	public Point getPosition() {
		return position;
	}


	public void setPosition(Point position) {
		this.position = position;
	}


	public ArrayList<Path> getPathsFromThisPlace() {
		return pathsFromThisPlace;
	}


	public void setPathsFromThisPlace(ArrayList<Path> pathsFromThisPlace) {
		this.pathsFromThisPlace = pathsFromThisPlace;
	}
	
	public boolean equals(Object o){
		if (o instanceof Place) {
			Place p = (Place) o;
			return p.getPosition().equals(getPosition());
			
		}else{return false;}
	}
	public int hashCode(){
		return 3*getPosition().hashCode();
	}
}
