import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;



public class UserGenerator {
	
<<<<<<< HEAD
	private static final int maxID = 5000;
	
	/*public static User generateRandomUser(Place[] allplaces){
=======
	private static final int maxScore = 100;
	private static final int maxID = 5000;
	
	public static User generateRandomUser(ArrayList<Place> allplaces){
>>>>>>> 253afe134b9fe275e0c8f3df2a665ca8f785f1c5
		Random generator = new Random();
		int id = generator.nextInt(maxID);
		int speed = 2000 + generator.nextInt(4000);
		int time = 30 + generator.nextInt(570);
<<<<<<< HEAD
		Place dep = allplaces[generator.nextInt(allplaces.length - 1)];
		Place arr = allplaces[generator.nextInt(allplaces.length - 1)];
		return new User (id,speed,time,dep,arr);
		
		
	}*/
=======
		Place dep = allplaces.get(generator.nextInt(allplaces.size()));
		Place arr = allplaces.get(generator.nextInt(allplaces.size()));
		return new User (id,speed,time,dep,arr);
		
		
	}
>>>>>>> 253afe134b9fe275e0c8f3df2a665ca8f785f1c5
	
	public static User generateBasicUser(ArrayList<Place> allplaces){
		Random generator = new Random();
		int id = generator.nextInt(maxID);
<<<<<<< HEAD
		Place dep = allplaces.get(generator.nextInt(allplaces.size() - 1));
		Place arr = allplaces.get(generator.nextInt(allplaces.size() - 1));
=======
		Place dep = allplaces.get(generator.nextInt(allplaces.size()));
		Place arr = allplaces.get(generator.nextInt(allplaces.size()));
>>>>>>> 253afe134b9fe275e0c8f3df2a665ca8f785f1c5
		return new User(id,4000,180,dep,arr);
		
	}
	public static HashMap<Place,Integer> generateRandomScores(ArrayList<Place> myPlaces){
		
		HashMap<Place,Integer> scoresmap = new HashMap<Place,Integer>();
		
		for (Place p : myPlaces){
			Random generator = new Random();
<<<<<<< HEAD
			int score = generator.nextInt(computeMaxScore(myPlaces.size()));
=======
			int score = generator.nextInt(maxScore);
>>>>>>> 253afe134b9fe275e0c8f3df2a665ca8f785f1c5
			scoresmap.put(p,score);
			
		}
		
		return scoresmap;
		
	}
	
<<<<<<< HEAD
	
	public static ArrayList<Tag> generateRandomRanking(ArrayList<Tag> alltags){
=======
	public static ArrayList<Tag> generateRanking(ArrayList<Tag> alltags){
>>>>>>> 253afe134b9fe275e0c8f3df2a665ca8f785f1c5
		ArrayList<Tag> tagranking = new ArrayList<Tag>(alltags);
		Collections.shuffle(tagranking);
		return tagranking;
		
	}
<<<<<<< HEAD
	
	private static int computeMaxScore(int nbPOI){
		int ms = 0;
		for(int i=1;i<nbPOI;i++){
			ms = ms + i*nbPOI;
		}
		return ms;
	}
=======
>>>>>>> 253afe134b9fe275e0c8f3df2a665ca8f785f1c5

}
