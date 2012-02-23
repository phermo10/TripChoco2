import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;



public class UserGenerator {
	
	private static final int maxScore = 100;
	private static final int maxID = 5000;
	
	public static User generateRandomUser(ArrayList<Place> allplaces){
		Random generator = new Random();
		int id = generator.nextInt(maxID);
		int speed = 2000 + generator.nextInt(4000);
		int time = 30 + generator.nextInt(570);
		Place dep = allplaces.get(generator.nextInt(allplaces.size()));
		Place arr = allplaces.get(generator.nextInt(allplaces.size()));
		return new User (id,speed,time,dep,arr);
		
		
	}
	
	public static User generateBasicUser(ArrayList<Place> allplaces){
		Random generator = new Random();
		int id = generator.nextInt(maxID);
		Place dep = allplaces.get(generator.nextInt(allplaces.size()));
		Place arr = allplaces.get(generator.nextInt(allplaces.size()));
		return new User(id,4000,180,dep,arr);
		
	}
	public static HashMap<Place,Integer> generateRandomScores(ArrayList<Place> myPlaces){
		
		HashMap<Place,Integer> scoresmap = new HashMap<Place,Integer>();
		
		for (Place p : myPlaces){
			Random generator = new Random();
			int score = generator.nextInt(maxScore);
			scoresmap.put(p,score);
			
		}
		
		return scoresmap;
		
	}
	
	public static ArrayList<Tag> generateRanking(ArrayList<Tag> alltags){
		ArrayList<Tag> tagranking = new ArrayList<Tag>(alltags);
		Collections.shuffle(tagranking);
		return tagranking;
		
	}

}
