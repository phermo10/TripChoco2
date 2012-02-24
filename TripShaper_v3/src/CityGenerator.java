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

	private static final int minDiameter = 1000;
	private static final int maxDiameter = 10000;
	//int nbPoints;
	private PointsGenerator gen;
	private ShortestPathsCalculator calc;

	public CityGenerator(int cityID) throws IOException{
		this.gen = new PointsGenerator(cityID);
		this.calc = new ShortestPathsCalculator(cityID,gen.getPlaces());
	}
	
	public CityGenerator(int nbPoints, int nbRegroupements) throws IOException{
		this.gen = new PointsGenerator(nbPoints, nbRegroupements);
		this.calc = new ShortestPathsCalculator(gen.getPlaces());
		saveResult();
	}

	private void saveResult() throws IOException{
		File dir = new File (Emplacements.DOSSIER_GRAPH_COMPLET(gen.getID()));
		dir.mkdirs();
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter(Emplacements.FICHIER_GRAPH_COMPLET(gen.getID()))));
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
		Place from;
		for(int fromIndex = 0; fromIndex<gen.getPlaces().size();fromIndex++){
			from = gen.getPlaces().get(fromIndex);
			output.println(Balises.FROM_X +from.getPosition().x+">");
			output.println(Balises.FROM_Y +from.getPosition().y+">");
			for(Place to : gen.getPlaces()){
				if(!from.equals(to)){
					output.println(Balises.TO_X+to.getPosition().x+">");
					output.println(Balises.TO_Y+to.getPosition().y+">");
					ITIN etapes = calc.getPCC().get(from).get(to);
					for(Etape etape : etapes.getEtapes()){
						if(!(etape.getPlace().equals(to)||etape.getPlace().equals(from))){
							output.println(Balises.BY_X + etape.getPlace().getPosition().x+">");
							output.println(Balises.BY_Y + etape.getPlace().getPosition().y+">");}
					}
					output.println(Balises.TO_End);
				}
			}
			output.println(Balises.FROM_End);
		}

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
			String filename = Emplacements.FICHIER_GRAPH_COMPLET(cityID);
			Scanner reader = new Scanner(new File (filename));
			String toRead = reader.nextLine();
			this.cityDiameter = Integer.parseInt(toRead);
			toRead = reader.nextLine();
			toRead = reader.nextLine();
			toRead = reader.nextLine();
			do {
				int x = Integer.parseInt(toRead);
				toRead=reader.nextLine();
				int y = Integer.parseInt(toRead);
				Point position = new Point(x,y);
				places.add(new Place(position,Integer.parseInt(reader.nextLine())));
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

		public PointsGenerator(int nbPoints, int nbPointsRegroupement) throws IOException {
			Random r = new Random();
			this.cityDiameter = r.nextInt(maxDiameter-minDiameter)+minDiameter; 
			System.out.println("diam = " + cityDiameter);
			this.cityID=((int)(Math.random()*1000));
			this.delaunay = new Delaunay();
			this.places = new ArrayList<Place>();
			generatePlaces(nbPoints);
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

		private void generatePlaces(int nbPoints){
			int xPointRegroupement1=0;
			int yPointRegroupement1=0;
			int xPointRegroupement2=0;
			int yPointRegroupement2=0;
			nbPointsZone1=0;
			while(nbPointsZone1<3){
				nbPointsZone1=(int) (Math.random()*10);
			}
			nbPointsZone2=0;
			while(nbPointsZone2<3){
				nbPointsZone2=(int) (Math.random()*10);
			}
			for (int i=0;i<nbPoints;i++){
				int x=0;
				int y=0;
				int whichCase=-1;
				if (i==0){//first point zone1
					whichCase=0;
				}
				if (i>0&&i<nbPointsZone1-1){//points zone1
					whichCase=1;
				}
				if (i==nbPointsZone1-1){//last point zone1
					whichCase=2;
				}
				if (i==nbPointsZone1){//first point zone2
					whichCase=3;
				}
				if (i>nbPointsZone1&&i<nbPointsZone1+nbPointsZone2-1){//points zone2
					whichCase=4;
				}
				if (i==nbPointsZone1+nbPointsZone2-1){//last point zone2
					whichCase=5;
				}
				switch(whichCase){
				case 0 :
					xPointRegroupement1=((int)((Display1.xWindow*Math.random()) ));
					yPointRegroupement1=((int)((Display1.yWindow*Math.random()) ));
					x=xPointRegroupement1;
					y=yPointRegroupement1;
					break;
				case 1:
					if (Math.random()>0.5){
						x=xPointRegroupement1+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						x=xPointRegroupement1-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					if (Math.random()>0.5){
						y=yPointRegroupement1+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						y=yPointRegroupement1-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					//zone1.add(new Point(x,y));
					break;
				case 2:
					if (Math.random()>0.5){
						x=xPointRegroupement1+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						x=xPointRegroupement1-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					if (Math.random()>0.5){
						y=yPointRegroupement1+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						y=yPointRegroupement1-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					//zone1.add(new Point(x,y));
					//zonesRegroupement.add(zone1);
					break;
				case 3:
					boolean xOK=false;
					while (xOK==false){
						xPointRegroupement2=(((int)(Display1.xWindow*Math.random()) ));
						if (Math.abs(xPointRegroupement2-xPointRegroupement1)>Display1.distanceBetweenPointAndZone){
							xOK=true;
						}
					}
					boolean yOK=false;
					while (yOK==false){
						yPointRegroupement2=(((int)(Display1.yWindow*Math.random()) ));
						if (Math.abs(yPointRegroupement2-yPointRegroupement1)>Display1.distanceBetweenPointAndZone){
							yOK=true;
						}
					}
					x=xPointRegroupement2;
					y=yPointRegroupement2;
					//zone2.add(new Point(x,y));
					//centresZonesRegroupement.add(new Point(x,y));
					break;
				case 4:
					if (Math.random()>0.5){
						x=xPointRegroupement2+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						x=xPointRegroupement2-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					if (Math.random()>0.5){
						y=yPointRegroupement2+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						y=yPointRegroupement2-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					//zone2.add(new Point(x,y));
					break;
				case 5:
					if (Math.random()>0.5){
						x=xPointRegroupement2+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						x=xPointRegroupement2-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					if (Math.random()>0.5){
						y=yPointRegroupement2+((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					else{
						y=yPointRegroupement2-((int)(Display1.tailleZoneRegroupement*Math.random()));
					}
					//zone2.add(new Point(x,y));
					//zonesRegroupement.add(zone2);
					break;
				default :
					boolean xOK2=false;
				while (xOK2==false){
					x=((int)((Display1.xWindow*Math.random()) ));
					if ((Math.abs(x-xPointRegroupement1)>Display1.distanceBetweenPointAndZone)&&(Math.abs(x-xPointRegroupement2)>Display1.distanceBetweenPointAndZone)){
						xOK2=true;
					}
				}
				boolean yOK2=false;
				while (yOK2==false){
					y=((int)((Display1.yWindow*Math.random()) ));
					if ((Math.abs(y-yPointRegroupement1)>Display1.distanceBetweenPointAndZone)&&(Math.abs(y-yPointRegroupement2)>Display1.distanceBetweenPointAndZone)){
						yOK2=true;
					}
				}
				}
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
			boolean pathBetweenZones=false;
			for (int i=nbPointsZone1;i<nbPointsZone1+nbPointsZone2;i++){ 
				for (int j=0;j<nbPointsZone1;j++){
					if (edgesMatrix[i][j]==true){
						pathBetweenZones=true;
					}
				}
			}
			//creating one path between the two zones
			if (pathBetweenZones==true){ 
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
			}
			//creating only one path between a point and zone 1
			for (int i=nbPointsZone1+nbPointsZone2;i<places.size();i++){
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
			}
			//creating only one path between a point and zone 2
			for (int i=nbPointsZone1+nbPointsZone2;i<places.size();i++){
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
					}
					/*int a=(int) (20+Math.random()*(nbPoints-21));
					int b=(int) (10+Math.random()*9);*/
					edgesMatrix[i][indexPath]=true;
					edgesMatrix[indexPath][i]=true;
				}
			}



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
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader input =  new BufferedReader(new FileReader(Emplacements.FICHIER_PCC(cityID)));
			String line = null; 

			while (( line = input.readLine()) != null){
				lines.add(line);
			}
			HashMap<Place,ITIN> plusCourtsChemins = null;
			int fromX=-1;
			int fromIndex=-1;
			int toX=-1;
			int toIndex=-1;
			int byX=-1;
			int byIndex=-1;
			ITIN chem = null;
			for(int i=0;i<lines.size(); i++){
				line = lines.get(i);
				if(line.startsWith(Balises.FROM_X)){
					fromX = Integer.parseInt(line.substring(Balises.FROM_X.length(),line.length()-1));
				}
				if(line.startsWith(Balises.FROM_Y)){
					int fromY = Integer.parseInt(line.substring(Balises.FROM_Y.length(),line.length()-1));
					Place from = new Place(new Point(fromX,fromY),-1);
					fromIndex = places.indexOf(from);
					if(fromIndex > -1){
						plusCourtsChemins = new HashMap<Place, ITIN>();
					}else{
						plusCourtsChemins = null;
					}
				}
				if(line.startsWith(Balises.TO_X)&&fromIndex > -1){
					toX = Integer.parseInt(line.substring(Balises.TO_X.length(),line.length()-1));
				}
				if(line.startsWith(Balises.TO_Y)&&fromIndex > -1){
					int toY = Integer.parseInt(line.substring(Balises.TO_X.length(),line.length()-1));
					Place to = new Place(new Point(toX,toY),-1);
					toIndex = places.indexOf(to);
					to = null;
					if(toIndex>-1){
						chem = new ITIN(places.get(fromIndex),NiveauTemps.TEMPS_MOY,places.get(toIndex),NiveauTemps.TEMPS_MOY);
					}else{
						chem = null;
					}
				}
				if(line.startsWith(Balises.BY_X)&&fromIndex > -1&&toIndex>-1){
					byX = Integer.parseInt(line.substring(Balises.BY_X.length(),line.length()-1));
				}
				if(line.startsWith(Balises.BY_Y)&&fromIndex > -1&&toIndex>-1){
					int byY = Integer.parseInt(line.substring(Balises.BY_Y.length(),line.length()-1));
					Place by= new Place(new Point(byX,byY),-1); 
					byIndex = places.indexOf(by);
					by = null;
					if(byIndex>-1){
						chem.addEtape(new Etape(places.get(byIndex),NiveauTemps.PAS_DE_VISITE));
					}
				}
				if(line.startsWith(Balises.TO_End)&&fromIndex > -1&&toIndex>-1&&byIndex>-1){
					plusCourtsChemins.put(places.get(toIndex), chem);
				}
				if(line.startsWith(Balises.FROM_End)&&fromIndex > -1&&toIndex>-1&&byIndex>-1){
					pcc.put(places.get(fromIndex),plusCourtsChemins);
					fromIndex=-1;
					toIndex=-1;
					byIndex=-1;
				}
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
