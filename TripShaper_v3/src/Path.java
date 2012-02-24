public class Path implements PathInterface {
	Place end1;
	Place end2;
	
	public Path(Place end1, Place end2){
		this.end1=end1;
		this.end2=end2;
	}
	
	public double getDistance(){
		return this.end1.getPosition().distance(end2.getPosition());
	}

	public boolean equals(Object o){
		if (o instanceof Path) {
			Path p = (Path) o;
			return (p.end1.getPosition().equals(end1.getPosition())&&p.end2.getPosition().equals(end2.getPosition()))||(p.end1.getPosition().equals(end2.getPosition())&&p.end2.getPosition().equals(end1.getPosition()));
			
		}else{return false;}
	}
	public int hashCode(){
		return 7*(end1.getPosition().hashCode() + end2.getPosition().hashCode());
	}
}
