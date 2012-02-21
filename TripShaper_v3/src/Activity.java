
public class Activity implements ActivityInterface {
	
	private int id;
	
	private Place place;
	
	private int score;
	
	private int time;
	
	private String description;
	
	private String audiofileName;

	public Activity(int id, Place place, int score, int time, String description,
			String audiofileName) {
		this.id=id;
		this.place = place;
		this.score = score;
		this.time = time;
		this.description = description;
		this.audiofileName = audiofileName;
	}
	
	

	public Activity(int id,Place place, int score, int time) {
		
		this(id,place,score,time,"","");
		
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAudiofileName() {
		return audiofileName;
	}

	public void setAudiofileName(String audiofileName) {
		this.audiofileName = audiofileName;
	}
	
	

}
