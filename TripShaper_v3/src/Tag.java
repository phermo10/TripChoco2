import java.util.ArrayList;


public class Tag implements TagInterface {

	private int id;

	private String name;

	private ArrayList<Place> placeranking;

	private ArrayList<Path> pathranking;

	public Tag(int id, String name, ArrayList<Place> placeranking, ArrayList<Path> pathranking){

		this.id= id;
		this.name = name;
		this.placeranking = placeranking;
		this.pathranking = pathranking;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Place> getPlaceranking() {
		return placeranking;
	}

	public void setPlaceranking(ArrayList<Place> placeranking) {
		this.placeranking = placeranking;
	}

	public void addPlace(Place p) {
		this.placeranking.add(p);
		
	}

	public void swapPlaces(Place p1, Place p2) {
		if (this.getPlaceranking().contains(p1) && this.getPlaceranking().contains(p2)){
		int indexp1 = this.getPlaceranking().indexOf(p1);
		int indexp2 = this.getPlaceranking().indexOf(p2);
		this.placeranking.set(indexp2,p1);
		this.placeranking.set(indexp1,p2);
		}
		
	}

	public ArrayList<Path> getPathranking() {
		return pathranking;
	}

	public void setPathranking(ArrayList<Path> pathranking) {
		this.pathranking = pathranking;
	}

	
	public void addPath(Path p) {
		this.pathranking.add(p);
		
	}

	public void swapPath(Path p1, Path p2) {
		if (this.getPathranking().contains(p1) && this.getPathranking().contains(p2)){
			int indexp1 = this.getPathranking().indexOf(p1);
			int indexp2 = this.getPathranking().indexOf(p2);
			this.pathranking.set(indexp2,p1);
			this.pathranking.set(indexp1,p2);
			}
	}


	public boolean equals(Object o){
		return (o instanceof Tag && ((Tag) o).getId() == this.getId());
	}

}
