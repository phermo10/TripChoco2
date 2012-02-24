import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class CityGenerator {

	//int nbPoints;
	private PointsGenerator gen;
	private ShortestPathsCalculator calc;

	public CityGenerator(int cityID) throws IOException{
		this.gen = new PointsGenerator(cityID);
		this.calc = new ShortestPathsCalculator(cityID,gen.getPlaces());
	}

	public CityGenerator(int totalPoints, int nbZones, int maxPointsInEachZone, int minRealDiameter, int maxRealDiameter) throws IOException{
		this.gen = new PointsGenerator(totalPoints, nbZones, maxPointsInEachZone, minRealDiameter, maxRealDiameter);
		this.calc = new ShortestPathsCalculator(gen.getPlaces());
		saveResult();
	}

	private void saveResult() throws IOException{
		File dir = new File (Emplacements.DOSSIER_GRAPH_COMPLET(gen.getID()));
		dir.mkdirs();
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter(Emplacements.FICHIER_GRAPH_COMPLET(gen.getID()))));
		writer.println(gen.getCityDiameter());
		writer.println(gen.getPlaces().size());
		writer.println("<Places>");
		for (int i=0;i<gen.getPlaces().size();i++){
			writer.println(gen.getPlaces().get(i).getPosition().x);
			writer.println(gen.getPlaces().get(i).getPosition().y);
			writer.println(gen.getPlaces().get(i).getAverageTime());
		}
		writer.println("</Places>");
		writer.println("<Edges>");
		for (int i=0;i<gen.getPlaces().size();i++){
			for (int j=0;j<gen.getPlaces().size();j++){
				writer.println(gen.getEdgesMatrix()[i][j]);
			}
		}
		writer.println("</Edges>");
		writer.close();

		// Sauvegarde des pcc
		PrintWriter output=null;
		output = new PrintWriter(new BufferedWriter(new FileWriter(Emplacements.FICHIER_PCC(gen.getID()))));
		output.println("NBPOINTS");
		output.println(gen.getPlaces().size());
		for(Place from : gen.getPlaces()){
			output.println("FROM");
			output.println(from.getPosition().x);
			output.println(from.getPosition().y);
			for(Place to : gen.getPlaces()){
				if(!from.equals(to)){
					output.println("TO");
					output.println(to.getPosition().x);
					output.println(to.getPosition().y);
					ITIN etapes = calc.getPCC().get(from).get(to);
					output.println("NBETAPES");
					output.println(etapes.getEtapes().size()-2);
					for(Etape etape : etapes.getEtapes()){
						if(!(etape.getPlace().equals(to)||etape.getPlace().equals(from))){
							output.println("BY");
							output.println(etape.getPlace().getPosition().x);
							output.println(etape.getPlace().getPosition().y);
						}
					}
				}
			}
		}
		output.flush();
		output.close();
	}
	public int getCityDiameter(){return this.gen.getCityDiameter();}
	public int getCityID(){return this.gen.cityID;}

	public ArrayList<Place> getPlaces(){return gen.getPlaces();}
	public HashMap<Place,HashMap<Place,ITIN>> getTousPCC(){return calc.getPCC();}

	private class Balises{
		public static final String FROM_X = "<FROMX=";
		public static final String FROM_Y = "<FROMY=";
		public static final String TO_X = "\t<TOX=";
		public static final String TO_Y = "\t<TOY=";
		public static final String FROM_End = "</FROM>";
		public static final String TO_End = "\t</TO>";
		public static final String BY_X = "\t\t<BYX=";
		public static final String BY_Y = "\t\t<BYY=";
	}

	private class PointsGenerator{
		private ArrayList<Point[]> delaunayEdges;
		private Delaunay delaunay;

		private final int minAverageTime=300;
		private final int maxAverageTime=3000; // secondes

		private int nbPointsZone1;
		private int nbPointsZone2;

		private ArrayList<Place> places;
		private int cityID;
		private boolean[][] edgesMatrix;

		private int cityDiameter;

		public int getCityDiameter(){return cityDiameter;}

		public PointsGenerator(int cityID) throws IOException{
			this.cityID = cityID;
			this.places = new ArrayList<Place>();
			String filename = Emplacements.FICHIER_GRAPH_COMPLET(cityID);
			Scanner reader = new Scanner(new File (filename));
			String toRead = reader.nextLine();
			this.cityDiameter = Integer.parseInt(toRead);
			toRead = reader.nextLine();
			int nbPoints = Integer.parseInt(toRead); // size
			edgesMatrix = new boolean[nbPoints][nbPoints];
			toRead = reader.nextLine();// <Places>
			toRead = reader.nextLine();
			do {
				int x = Integer.parseInt(toRead);
				toRead=reader.nextLine();
				int y = Integer.parseInt(toRead);
				Point position = new Point(x,y);
				toRead = reader.nextLine();
				places.add(new Place(position,Integer.parseInt(toRead)));
				toRead=reader.nextLine();
			}while (!toRead.equals("</Places>"));

			toRead = reader.nextLine(); // </Edges>
			for (int i=0;i<places.size();i++){
				ArrayList<Path> pathsTmp = new ArrayList<Path>();
				for (int j=0;j<places.size();j++){
					toRead = reader.nextLine();
					if(Boolean.parseBoolean(toRead)){
						pathsTmp.add(new Path(places.get(i),places.get(j)));
						edgesMatrix[i][j] = true;
					}else edgesMatrix[i][j] = false;
				}
				places.get(i).setPathsFromThisPlace(pathsTmp);
			}
		}

		/**
		 * les diamètres reels representent un ordre de grandeur de la taille reelle en metres que l'on souhaite pour la ville
		 * Plus la taille est grand moins l'utilisateur pourra parcourir de points
		 * @param totalPoints
		 * @param nbZones
		 * @param maxPointsInEachZone
		 * @param minRealDiameter
		 * @param maxRealDiameter
		 * @throws IOException
		 */
		public PointsGenerator(int totalPoints, int nbZones, int maxPointsInEachZone, int minRealDiameter, int maxRealDiameter) throws IOException {
			Random r = new Random();
			this.cityDiameter = r.nextInt(maxRealDiameter-minRealDiameter)+minRealDiameter; 
			System.out.println("diam = " + cityDiameter);
			this.cityID=((int)(Math.random()*1000));
			this.delaunay = new Delaunay();
			this.places = new ArrayList<Place>();
			generatePlaces(totalPoints, nbZones, maxPointsInEachZone);
		}

		public boolean[][] getEdgesMatrix(){
			return this.edgesMatrix;
		}

		public ArrayList<Place> getPlaces() {
			return places;
		}

		public int getID(){
			return this.cityID;
		}

		private void generatePlaces(int totalPoints, int nbZones, int maxPointsInEachZone){
			ArrayList<Point> zoneCenter = new ArrayList<Point>();
			//nbPointsRegroupement has to inferior to totalPoints/maxPointsInEachZone
			int nbPointsAlreadyCreated=0;
			for (int i=0;i<nbZones;i++){
				//boolean zoneCenterFarFromTheOthers=false;
				int xZoneCenter=0;
				int yZoneCenter=0;
				//int nbTentatives=0;
				//while (!zoneCenterFarFromTheOthers&&nbTentatives<50){
				//	nbTentatives++;
				xZoneCenter=((int)((Display1.xWindow*Math.random()) ));
				yZoneCenter=((int)((Display1.yWindow*Math.random()) ));
				//	Point tempZoneCenter=new Point(xZoneCenter,yZoneCenter);
				/*for (Point p: zoneCenter){
						if (p.distance(tempZoneCenter)>Display1.distanceBetweenPointAndZone){
							zoneCenterFarFromTheOthers=true;
						}
						else{
							zoneCenterFarFromTheOthers=false;
						}
					}*/
				//}
				int nbPointsInThisZone=0;
				while(nbPointsInThisZone<3){
					nbPointsInThisZone=(int) (Math.random()*10);
				}
				zoneCenter.add(new Point(xZoneCenter,yZoneCenter));
				places.add(new Place(new Point(xZoneCenter,yZoneCenter), (int)(minAverageTime+Math.random()*(maxAverageTime-minAverageTime))));
				delaunay.insertPoint(new Point(xZoneCenter,yZoneCenter));
				nbPointsAlreadyCreated++;
				for (int j=0;j<nbPointsInThisZone;j++){
					int x=0;int y=0;
					if (Math.random()>0.5){
						x=xZoneCenter+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						x=xZoneCenter-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					if (Math.random()>0.5){
						y=yZoneCenter+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						y=yZoneCenter-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}		
					places.add(new Place(new Point(x,y), (int)(minAverageTime+Math.random()*(maxAverageTime-minAverageTime))));
					delaunay.insertPoint(new Point(x,y));
					nbPointsAlreadyCreated++;
				}
			}
			for (int i=0;i<totalPoints-nbPointsAlreadyCreated;i++){
				int x=((int)((Display1.xWindow*Math.random()) ));
				int y=((int)((Display1.yWindow*Math.random()) ));
				places.add(new Place(new Point(x,y), (int)(minAverageTime+Math.random()*(maxAverageTime-minAverageTime))));
				delaunay.insertPoint(new Point(x,y));
			}
			generateEdges();
		}
		private void generateEdges(){
			this.delaunayEdges = (ArrayList<Point[]>) delaunay.computeEdges();
			edgesMatrix=new boolean[places.size()][places.size()];
			//initialization to false
			for (int i=0;i<places.size();i++){ 
				for (int j=0;j<places.size();j++){
					edgesMatrix[i][j]=false;
				}
			}
			//transformation in matrix
			for (int k=0;k<delaunayEdges.size();k++){ 
				int i=places.indexOf(new Place(new Point(delaunayEdges.get(k)[0].x,delaunayEdges.get(k)[0].y),-1));
				int j=places.indexOf(new Place(new Point(delaunayEdges.get(k)[1].x,delaunayEdges.get(k)[1].y),-1));
				edgesMatrix[i][j]=true;
				edgesMatrix[j][i]=true;
			}
			// looking for a path between the two zones
			/*boolean pathBetweenZones=false;
			for (int i=nbPointsZone1;i<nbPointsZone1+nbPointsZone2;i++){ 
				for (int j=0;j<nbPointsZone1;j++){
					if (edgesMatrix[i][j]==true){
						pathBetweenZones=true;
					}
				}
			}*/
			//creating one path between the two zones
			/*if (pathBetweenZones==true){ 
				for (int i=nbPointsZone1;i<nbPointsZone1+nbPointsZone2;i++){ 
					for (int j=0;j<nbPointsZone1;j++){
						edgesMatrix[i][j]=false;
						edgesMatrix[j][i]=false;
					}
				}
				int i=(int) (nbPointsZone1+Math.random()*(nbPointsZone2-1));
				int j=(int) (Math.random()*(nbPointsZone1-1));
				edgesMatrix[i][j]=true;
				edgesMatrix[j][i]=true;
			}*/
			//creating only one path between a point and zone 1
			/*for (int i=nbPointsZone1+nbPointsZone2;i<places.size();i++){
				int indexPath=-1;
				for (int j=0;j<nbPointsZone1;j++){
					if (edgesMatrix[i][j]==true){
						indexPath=j;
					}
				}
				if (indexPath!=-1){
					for (int j=0;j<nbPointsZone1;j++){
						edgesMatrix[i][j]=false;
						edgesMatrix[j][i]=false;
					}

					edgesMatrix[i][indexPath]=true;
					edgesMatrix[indexPath][i]=true;
				}
			}*/
			//creating only one path between a point and zone 2
			/*for (int i=nbPointsZone1+nbPointsZone2;i<places.size();i++){
				int indexPath=-1;
				for (int j=nbPointsZone1;j<nbPointsZone1+nbPointsZone2;j++){
					if (edgesMatrix[i][j]==true){
						indexPath=j;
					}
				}
				if (indexPath!=-1){
					for (int j=nbPointsZone1;j<nbPointsZone1+nbPointsZone2;j++){
						edgesMatrix[i][j]=false;
						edgesMatrix[j][i]=false;
					}*/
			/*int a=(int) (20+Math.random()*(nbPoints-21));
					int b=(int) (10+Math.random()*9);*/
			/*edgesMatrix[i][indexPath]=true;
					edgesMatrix[indexPath][i]=true;
				}
			}*/



			//creating tempEdges via matrix
			for (int i=0;i<places.size();i++){
				ArrayList<Path> pathsTmp = new ArrayList<Path>();
				for (int j=0;j<places.size();j++){
					if(i!=j&&edgesMatrix[i][j]==true){
						Path edge=new Path(places.get(i),places.get(j));
						pathsTmp.add(edge);
					}
				}
				places.get(i).setPathsFromThisPlace(pathsTmp);
			}
		}
	}

	private class ShortestPathsCalculator{

		private HashMap<Place,HashMap<Place,ITIN>> pcc ;

		/**
		 * Calcule les pcc
		 * @param places
		 */
		public ShortestPathsCalculator(ArrayList<Place> places) {
			this.pcc = new HashMap<Place,HashMap<Place,ITIN>>();
			// Calcul des plus courts chemins parmi les places fournies
			int tot = places.size();
			for(int i =0; i<places.size();i++){
				System.out.println("" + i + "/" + tot);
				HashMap<Place,Place> predecesseurs = new HashMap<Place, Place>();
				HashMap<Place, Double> distances = new HashMap<Place, Double>();
				HashMap<Place, ITIN> plusCourtsChemins = new HashMap<Place, ITIN>();
				ArrayList<Place> nonVisites = new ArrayList<Place>(places);
				ArrayList<Place> visites = new ArrayList<Place>();
				Place depart = places.get(i);
				for(int j = 0 ; j<places.size();j++){
					if(i!=j){
						distances.put(places.get(j),Double.POSITIVE_INFINITY);
						ITIN it = new ITIN(depart, NiveauTemps.TEMPS_MOY, places.get(j), NiveauTemps.TEMPS_MOY);
						it.makeImpossible();
						plusCourtsChemins.put(places.get(j), it);
					}
				}
				distances.put(depart, 0.0);
				Place pp=null;
				while(nonVisites.size()>0){
					pp = getPlusProche(distances, nonVisites);
					if(pp!=null){
						//System.out.println("Plus proche : " + pp);
						// Collections.sort trie par ordre croissant (plus petit en 0)
						Collections.sort(visites, new ComparateurPlace(pp,true));
						boolean precNotFound = true;
						int k=0;
						Place prec;
						// On parcours les points visites tries par distance relative à pp
						// jusqu'à en trouver un qui soit dans les sommets atteignables de pp
						// (il y en a forcément un, sinon pp serait à une distance infinie et getplusproche retournerai null)
						// c'est ce point qui sera le prédecesseur de pp
						while(precNotFound&&k<visites.size()){
							prec = visites.get(k);
							if(prec!=null&&prec.getSommetsAtteignables().contains(pp)){
								precNotFound = false;
								ITIN shortestToPrec = plusCourtsChemins.get(prec);
								ITIN shortestToPP;
								if(shortestToPrec!=null){
									shortestToPP = shortestToPrec.prolonger(pp,NiveauTemps.TEMPS_MOY, NiveauTemps.PAS_DE_VISITE);
								}
								else{
									shortestToPP = new ITIN(depart, NiveauTemps.TEMPS_MOY, pp, NiveauTemps.TEMPS_MOY);
								}
								plusCourtsChemins.put(pp, shortestToPP);
							}
							k++;
						}

						for(Place s2 : pp.getSommetsAtteignables()){
							double ds2 = distances.get(s2);
							double dpp = distances.get(pp);
							double ds2pp = pp.getPosition().distance(s2.getPosition());
							if(ds2>dpp+ds2pp){
								distances.put(s2, ds2pp + dpp);
								predecesseurs.put(s2, pp);
							}
						}
						nonVisites.remove(pp);
						visites.add(pp);
					}
					else{
						nonVisites.clear();
					}
				}	
				pcc.put(depart,plusCourtsChemins);
				predecesseurs.clear();
				distances.clear();
				nonVisites.clear();
				nonVisites.trimToSize();
				visites.clear();
				visites.trimToSize();
			}			
		}

		/**
		 * Charge les pcc precalcules
		 * @param cityID
		 * @param places
		 * @throws IOException 
		 */
		public ShortestPathsCalculator(int cityID, ArrayList<Place> places) throws IOException{
			this.pcc = new HashMap<Place, HashMap<Place,ITIN>>();
			// Chargement des PCC
			BufferedReader input =  new BufferedReader(new FileReader(Emplacements.FICHIER_PCC(cityID)));
			input.readLine();
			int nbPoints = Integer.parseInt(input.readLine());
			int i = 1;
			while(i<=nbPoints){
				input.readLine();
				HashMap<Place,ITIN> plusCourtsChemins = new HashMap<Place, ITIN>();
				int fromX = Integer.parseInt(input.readLine());
				int fromY = Integer.parseInt(input.readLine());
				Place fromTmp = new Place(new Point(fromX,fromY),-1);
				Place from = places.get(places.indexOf(fromTmp));
				int j = 1;
				while(j<=nbPoints-1){
					input.readLine();
					int toX = Integer.parseInt(input.readLine());
					int toY = Integer.parseInt(input.readLine());
					Place toTmp = new Place(new Point(toX,toY),-1);
					Place to = places.get(places.indexOf(toTmp));
					ITIN chem = new ITIN(from, NiveauTemps.TEMPS_MOY, to, NiveauTemps.TEMPS_MOY);
					input.readLine();
					int nbEtapes = Integer.parseInt(input.readLine());
					System.out.println(nbEtapes);
					int k = 1;
					while(k<=nbEtapes){
						input.readLine();
						int byX = Integer.parseInt(input.readLine());
						System.out.println(byX);
						int byY = Integer.parseInt(input.readLine());
						System.out.println(byY);
						Place byTmp = new Place(new Point(byX,byY),-1);
						System.out.println(byTmp);
						System.out.println(places);
						Place by = places.get(places.indexOf(byTmp));
						chem.addEtape(by, NiveauTemps.PAS_DE_VISITE);
						k++;
					}
					plusCourtsChemins.put(to, chem);
					j++;
				}
				pcc.put(from, plusCourtsChemins);
				i++;
			}
		}

		/*private ArrayList<Place> getSommetsAtteignables(Place p, ArrayList<Place> places){
			ArrayList<Point> satt= new ArrayList<Point>();
			int indexP = places.indexOf(p);
			p.g
			for(int j=0;j<edgesMatrix[0].length;j++){
				if(edgesMatrix[indexP][j]&&indexP!=j){
					satt.add(myPoints.get(j));
				}
			}
			return satt;
		}*/

		private Place getPlusProche(HashMap<Place, Double> distances, ArrayList<Place> nonVisites){
			double dMin = Double.POSITIVE_INFINITY;
			Place pp = null;
			for(Place s : nonVisites){
				double d = distances.get(s);
				if(d!=Double.POSITIVE_INFINITY){
					boolean dMinIsInf = dMin==Double.POSITIVE_INFINITY;
					if(dMinIsInf||(!dMinIsInf&&d<dMin)){
						dMin = d;
						pp = s;
					}
				}
			}
			return pp;
		}

		public HashMap<Place,HashMap<Place,ITIN>> getPCC(){
			return pcc;
		}


	}

}
