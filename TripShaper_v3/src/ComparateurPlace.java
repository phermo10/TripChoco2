import java.util.Comparator;


public class ComparateurPlace implements Comparator<Place> {

		
		private PlaceComparableProperty propertyCode;
		private Place origin;
		private boolean ordreCroissant;
		private Graph graph;

		public ComparateurPlace(PlaceComparableProperty propertyCode, boolean ordreCroissant, Graph graph){
			this.graph = graph;
			this.propertyCode = propertyCode;
			this.origin = null;
			this.ordreCroissant = ordreCroissant;
		}

		public ComparateurPlace(Place origin, boolean ordreCroissant){
			this.graph = null;
			this.propertyCode = PlaceComparableProperty.DISTANCERELATIVE;
			this.origin = origin;
			this.ordreCroissant = ordreCroissant;
		}

		public int compare(Place s1, Place s2) {
			int result;
			double property1;
			double property2;
			switch (propertyCode)
			{
			case SCORE:property1 = graph.getScores().get(s1);property2 = graph.getScores().get(s2);break;
			case DISTANCERELATIVE:
				property1 = origin.getPosition().distance(s1.getPosition());
				property2 = origin.getPosition().distance(s2.getPosition());
			default:property1 = s1.getAverageTime();property2 = s2.getAverageTime();break;	
			}
			if(property1==property2){result=0;}
			else{

				if(ordreCroissant){
					result=property1<property2?-1:1;
				}
				else{
					result=property1<property2?1:-1;
				}
			}			
			return result;
		}
	}