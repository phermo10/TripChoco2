import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

import javax.sound.midi.SysexMessage;
import javax.swing.JFrame;
import java.util.Arrays;


public class Display1 {

	static int nbPointsRegroupement=2;
	static int nbPoints=16*nbPointsRegroupement;
	static int xWindow=1000;
	static int yWindow=700;
	static int tailleZoneRegroupement=20;
	static int distanceBetweenPointAndZone=30;

	public static void main(String[] args) throws Exception { 
		Graph userGraph;
		if(false){
			userGraph = new Graph(4375,627);	
		}else{
			CityGenerator city = new CityGenerator(10,3,3, 50000, 100000); // il faut nbZones * nbPoitsMaxParZone < nbPointsTotal
			User user = UserGenerator.generateBasicUser(city.getPlaces(), true, 1.11, 10800); // 1.11 m/s = 4 km/h ; 10800 sec = 180 minutes = 3h
			HashMap<Place,Integer> scores = UserGenerator.generateRandomScores(city.getPlaces());
			userGraph = new Graph(city.getTousPCC(),city.getPlaces(), new ArrayList<Tag>(), user,scores, city.getCityID(),city.getCityDiameter());
			userGraph.save();
		}
		userGraph.display();
		System.out.println("Graphe et user générés et sauvegardés");
		System.out.println("Calcul du meilleur chemin...");
		userGraph.solve();
		userGraph.display();
	} 

	private static String lireString(){
		String ligne_lue=null;
		try{ 
			InputStreamReader lecteur=new InputStreamReader(System.in); 
			BufferedReader entree=new BufferedReader(lecteur); 
			ligne_lue=Outils.neutraliser(entree.readLine(),true); 
		} 
		catch(IOException e){e.printStackTrace();}
		return ligne_lue;
	}
}