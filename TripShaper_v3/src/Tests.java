import java.io.IOException;


public class Tests {

	
	public static void main(String[] arg0){
		
		try {
			/*Graph mygraph = new Graph(30);
			mygraph.setUser(new User(2012,4000,180,mygraph.getAllplaces().get(0),mygraph.getAllplaces().get(0)));
			mygraph.save();*/
			Graph.restore(2012,"graph115");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
