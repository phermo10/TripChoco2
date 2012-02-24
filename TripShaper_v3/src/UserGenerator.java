import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;



public class UserGenerator {
	
	private static final int maxID = 5000;
	
	/*public static User generateRandomUser(Place[] allplaces){
		Random generator = new Random();
		int id = generator.nextInt(maxID);
		int speed = 2000 + generator.nextInt(4000);
		int time = 30 + generator.nextInt(570);
		Place dep = allplaces[generator.nextInt(allplaces.length - 1)];
		Place arr = allplaces[generator.nextInt(allplaces.length - 1)];
		return new User (id,speed,time,dep,arr);
		
		
	}*/
	
	public static User generateBasicUser(ArrayList<Place> allplaces){
		Random generator = new Random();
		int id = generator.nextInt(maxID);
		Place dep = allplaces.get(generator.nextInt(allplaces.size() - 1));
		Place arr = allplaces.get(generator.nextInt(allplaces.size() - 1));
		return new User(id,4000,180,dep,arr);
		
	}
	public static HashMap<Place,Integer> generateRandomScores(ArrayList<Place> myPlaces){
		
		HashMap<Place,Integer> scoresmap = new HashMap<Place,Integer>();
		
		for (Place p : myPlaces){
			Random generator = new Random();
			int score = generator.nextInt(computeMaxScore(myPlaces.size()));
			scoresmap.put(p,score);
			
		}
		
		return scoresmap;
		
	}
	
	
	public static ArrayList<Tag> generateRandomRanking(ArrayList<Tag> alltags){
		ArrayList<Tag> tagranking = new ArrayList<Tag>(alltags);
		Collections.shuffle(tagranking);
		return tagranking;
		
	}
	
	private static int computeMaxScore(int nbPOI){
		int ms = 0;
		for(int i=1;i<nbPOI;i++){
			ms = ms + i*nbPOI;
		}
		return ms;
	}

}
