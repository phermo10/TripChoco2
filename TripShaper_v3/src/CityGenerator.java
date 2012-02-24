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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class CityGenerator {
	//int nbPoints;
	private int graphID;
	private ArrayList<Point> myPoints;
	private ArrayList<Point[]> delaunayEdges;
	private ArrayList<Point[]> edges;
	private Delaunay delaunay;
	private boolean[][] edgesMatrix;
	private ArrayList<Integer> averageTimes;

	private static int minAverageTime=5;
	private static int maxAverageTime=50;

	private ArrayList<Place> places;
	private ArrayList<ArrayList<Path>> paths;

	private int nbPointsZone1;
	private int nbPointsZone2;

	private ArrayList<HashMap<Point,ArrayList<Point>>> tousPlusCourtsChemins;
	private ArrayList<HashMap<Place,ITIN>> pccITIN ;

	public CityGenerator(int graphID) throws IOException{
		this.graphID = graphID;
		this.myPoints = new ArrayList<Point>();
		this.delaunay = new Delaunay();
		averageTimes=new ArrayList<Integer>();
		this.edges=new ArrayList<Point[]>();
		String filename = Emplacements.FICHIER_GRAPH_COMPLET(graphID);
		Scanner reader = new Scanner(new File (filename));
		String toRead = reader.nextLine();

		toRead = reader.nextLine();
		toRead = reader.nextLine();
		do {
			int x = (int)(Integer.parseInt(toRead));
			toRead=reader.nextLine();
			int y = Integer.parseInt(toRead);
			Point position = new Point(x,y);
			myPoints.add(position);
			delaunay.insertPoint(new Point(x,y));
			averageTimes.add(((int)Integer.parseInt(reader.nextLine())));
			toRead=reader.nextLine();
		}	
		while (!toRead.equals("</Places>"));
		toRead = reader.nextLine();
		this.edgesMatrix=new boolean[myPoints.size()][myPoints.size()];
		for (int i=0;i<myPoints.size();i++){
			for (int j=0;j<myPoints.size();j++){
				toRead = reader.nextLine();
				this.edgesMatrix[i][j]=Boolean.parseBoolean(toRead);
			}
		}


		// Chargement des PCC
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader input =  new BufferedReader(new FileReader(Emplacements.FICHIER_PCC(graphID)));
		String line = null; 

		while (( line = input.readLine()) != null){
			lines.add(line);
		}
		HashMap<Point,ArrayList<Point>> plusCourtsChemins = null;
		int fromX=-1;
		int fromIndex=-1;
		int toX=-1;
		int toIndex=-1;
		int byX=-1;
		int byIndex=-1;
		ArrayList<Point> chem = null;
		for(int i=0;i<lines.size(); i++){
			line = lines.get(i);
			if(line.startsWith(Balises.FROM_X)){
				fromX = Integer.parseInt(line.substring(Balises.FROM_X.length(),line.length()-1));
			}
			if(line.startsWith(Balises.FROM_Y)){
				int fromY = Integer.parseInt(line.substring(Balises.FROM_Y.length(),line.length()-1));
				Point from = new Point(fromX,fromY);
				fromIndex = myPoints.indexOf(from);
				if(fromIndex > -1){
					plusCourtsChemins = new HashMap<Point, ArrayList<Point>>();
				}else{
					plusCourtsChemins = null;
				}
			}
			if(line.startsWith(Balises.TO_X)&&fromIndex > -1){
				toX = Integer.parseInt(line.substring(Balises.TO_X.length(),line.length()-1));
			}
			if(line.startsWith(Balises.TO_Y)&&fromIndex > -1){
				int toY = Integer.parseInt(line.substring(Balises.TO_X.length(),line.length()-1));
				Point to = new Point(toX,toY);
				toIndex = myPoints.indexOf(to);
				to = null;
				if(toIndex>-1){
					chem = new ArrayList<Point>();
					chem.add(myPoints.get(fromIndex));
					chem.add(myPoints.get(toIndex));
				}else{
					chem = null;
				}
			}
			if(line.startsWith(Balises.BY_X)&&fromIndex > -1&&toIndex>-1){
				byX = Integer.parseInt(line.substring(Balises.BY_X.length(),line.length()-1));
			}
			if(line.startsWith(Balises.BY_Y)&&fromIndex > -1&&toIndex>-1){
				int byY = Integer.parseInt(line.substring(Balises.BY_Y.length(),line.length()-1));
				Point by= new Point(byX,byY); 
				byIndex = myPoints.indexOf(by);
				by = null;
				if(byIndex>-1){
					chem.add(chem.size()-1,myPoints.get(byIndex));
				}
			}
			if(line.startsWith(Balises.TO_End)&&fromIndex > -1&&toIndex>-1&&byIndex>-1){
				plusCourtsChemins.put(myPoints.get(toIndex), chem);
			}
			if(line.startsWith(Balises.FROM_End)&&fromIndex > -1&&toIndex>-1&&byIndex>-1){
				tousPlusCourtsChemins.add(plusCourtsChemins);
				fromIndex=-1;
				toIndex=-1;
				byIndex=-1;
			}
		}
		generatePlaces();
	}

	public CityGenerator(int nbPoints, int nbPointsRegroupement) throws IOException {
		super();
		this.graphID=((int)(Math.random()*1000));
		this.delaunay = new Delaunay();
		this.myPoints = new ArrayList<Point>();
		generatePoints();
		generateEdges();
		this.averageTimes=new ArrayList<Integer>();
		for (int i=0;i<this.myPoints.size();i++){
			this.averageTimes.add((int)(minAverageTime+Math.random()*(maxAverageTime-minAverageTime)));
		}
		computeShortestPaths();
		saveGraph();
		generatePlaces();
	}

	public ArrayList<HashMap<Place,ITIN>> getPCC(){
		return this.pccITIN;
	}
	public boolean[][] getEdgesMatrix() {
		return edgesMatrix;
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}

	private void setEdgesMatrix(boolean[][] edgesMatrix) {
		this.edgesMatrix = edgesMatrix;
	}

	public int getID(){
		return this.graphID;
	}

	private void generatePoints(){
		ArrayList<Point> zone1 = null;
		ArrayList<Point> zone2 = null;
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
		zone1=new ArrayList<Point>();
		zone2=new ArrayList<Point>();
		for (int i=0;i<Display1.nbPoints;i++){
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
			myPoints.add(new Point(x,y));
			delaunay.insertPoint(new Point(x,y));
		}
	}

	public void generatePlaces(){
		// Creation de Place a partir des Point
		places = new ArrayList<Place>();
		// creation of the places without paths
		for (int i=0;i<myPoints.size();i++){
			places.add(new Place(myPoints.get(i),averageTimes.get(i)));
		}
		//creation of the paths
		this.paths = new ArrayList<ArrayList<Path>>();
		for (int i=0;i<myPoints.size();i++){
			ArrayList<Path> tempPaths=new ArrayList<Path>();
			for (int j=0;j<myPoints.size();j++){
				if (edgesMatrix[i][j]){
					tempPaths.add(new Path(places.get(i), places.get(j)));
				}
			}
			paths.add(tempPaths);
		}
		//link between places and paths
		for (int i=0;i<myPoints.size();i++){
			places.get(i).setPathsFromThisPlace(paths.get(i));
		}

		//Creation des itineraires d'etapes à partir des liste de points
		// ArrayList<HashMap<Point,ArrayList<Point>>> tousPlusCourtsChemins;
		pccITIN = new ArrayList<HashMap<Place,ITIN>>(tousPlusCourtsChemins.size());
		for(int indexDepart =0; indexDepart < tousPlusCourtsChemins.size();indexDepart++){
			Place depart = places.get(indexDepart);
			HashMap<Place,ITIN> lesPCC = new HashMap<Place, ITIN>();
			for(int indexArrivee=0;indexArrivee<tousPlusCourtsChemins.get(indexDepart).keySet().size();indexArrivee++){
				Place arrivee = places.get(indexArrivee);
				ITIN lePCC = new ITIN(depart, NiveauTemps.TEMPS_MOY,arrivee, NiveauTemps.TEMPS_MOY);
				//TODO
				System.out.println("sfdlskdfhldf");
				System.out.println(indexArrivee);
				System.out.println(myPoints.get(indexArrivee));
				for(int index = 0; index < tousPlusCourtsChemins.size();index++){
					System.out.println("abcdef   " + index);
					for(Point dest : tousPlusCourtsChemins.get(index).keySet()){
						System.out.println(dest);
					}
					System.out.println("abcdef");
				}
				System.out.println("sfdlskdfhldf");
				for(Point etapeP : tousPlusCourtsChemins.get(indexDepart).get(myPoints.get(indexArrivee))){
					Place etape = places.get(places.indexOf(etapeP));
					lePCC.addEtape(etape, NiveauTemps.PAS_DE_VISITE);
				}
				lesPCC.put(arrivee,lePCC);
			}
			pccITIN.add(lesPCC);
		}
	}

	public void saveGraph() throws IOException{
		File dir = new File (Emplacements.DOSSIER_GRAPH_COMPLET(graphID));
		dir.mkdirs();
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter(Emplacements.FICHIER_GRAPH_COMPLET(graphID))));
		writer.println(myPoints.size());
		writer.println("<Places>");
		for (int i=0;i<myPoints.size();i++){
			writer.println((int)myPoints.get(i).getX());
			writer.println((int)myPoints.get(i).getY());
			writer.println(averageTimes.get(i));
		}
		writer.println("</Places>");
		writer.println("<Edges>");
		for (int i=0;i<myPoints.size();i++){
			for (int j=0;j<myPoints.size();j++){
				writer.println(edgesMatrix[i][j]);
			}
		}
		writer.println("</Edges>");
		writer.close();

		// Sauvegarde des pcc
		PrintWriter output=null;
		output = new PrintWriter(new BufferedWriter(new FileWriter(Emplacements.FICHIER_PCC(graphID))));
		String line;
		Point from;
		for(int fromIndex = 0; fromIndex<myPoints.size();fromIndex++){
			from = myPoints.get(fromIndex);
			output.println(Balises.FROM_X +from.x+">");
			output.println(Balises.FROM_Y +from.y+">");
			for(Point to : myPoints){
				if(!from.equals(to)){
					output.println(Balises.TO_X+to.x+">");
					output.println(Balises.TO_Y+to.y+">");
					ArrayList<Point> etapes = tousPlusCourtsChemins.get(fromIndex).get(to);
					for(Point etape : etapes){
						if(!(etape.equals(to)||etape.equals(from))){
							output.println(Balises.BY_X + etape.x+">");
							output.println(Balises.BY_Y + etape.y+">");}
					}
					output.println(Balises.TO_End);
				}
			}
			output.println(Balises.FROM_End);
		}

	}

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


	private void computeShortestPaths(){
		this.tousPlusCourtsChemins = new ArrayList<HashMap<Point,ArrayList<Point>>>();
		this.pccITIN = new ArrayList<HashMap<Place,ITIN>>();
		int count = 0;
		int tot = this.myPoints.size();
		for(Point depart : this.myPoints){
			count++;
			System.out.println("" + count + "/" + tot);
			HashMap<Point,Point> predecesseurs = new HashMap<Point, Point>();
			HashMap<Point, Double> distances = new HashMap<Point, Double>();
			HashMap<Point, ArrayList<Point>> plusCourtsChemins = new HashMap<Point, ArrayList<Point>>();
			ArrayList<Point> nonVisites = new ArrayList<Point>( this.myPoints);
			ArrayList<Point> visites = new ArrayList<Point>();

			for(Point s : this.myPoints){
				if(!s.equals(depart)){
					distances.put(s,Double.POSITIVE_INFINITY);
					plusCourtsChemins.put(s, null);
				}
			}
			distances.put(depart, 0.0);
			Point pp=null;
			while(nonVisites.size()>0){
				pp = getPlusProche(distances, nonVisites);
				if(pp!=null){
					//System.out.println("Plus proche : " + pp);
					// Collections.sort trie par ordre croissant (plus petit en 0)
					Collections.sort(visites, new ComparateurPoint(pp));
					boolean precNotFound = true;
					int i=0;
					Point prec;
					// On parcours les points visites tries par distance relative à pp
					// jusqu'à en trouver un qui soit dans les sommets atteignables de pp
					// (il y en a forcément un, sinon pp serait à une distance infinie et getplusproche retournerai null)
					// c'est ce point qui sera le prédecesseur de pp
					while(precNotFound&&i<visites.size()){
						prec = visites.get(i);
						if(prec!=null&&getSommetsAtteignables(prec).contains(pp)){
							precNotFound = false;
							ArrayList<Point> shortestToPrec = plusCourtsChemins.get(prec);
							ArrayList<Point> shortestToPP;
							if(shortestToPrec!=null){
								shortestToPP = shortestToPrec;
								shortestToPP.add(pp);
							}
							else{
								shortestToPP = new ArrayList<Point>();
								shortestToPP.add(depart);
								shortestToPP.add(pp);
							}
							plusCourtsChemins.put(pp, shortestToPP);
						}
						i++;
					}

					for(Point s2 : getSommetsAtteignables(pp)){
						double ds2 = distances.get(s2);
						double dpp = distances.get(pp);
						double ds2pp = pp.distance(s2);
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
			this.tousPlusCourtsChemins.add(plusCourtsChemins);
			predecesseurs.clear();
			distances.clear();
			nonVisites.clear();
			nonVisites.trimToSize();
			visites.clear();
			visites.trimToSize();
		}
	}

	private ArrayList<Point> getSommetsAtteignables(Point p){
		ArrayList<Point> satt= new ArrayList<Point>();
		int indexP = myPoints.indexOf(p);
		for(int j=0;j<edgesMatrix[0].length;j++){
			if(edgesMatrix[indexP][j]&&indexP!=j){
				satt.add(myPoints.get(j));
			}
		}
		return satt;
	}

	private class ComparateurPoint implements Comparator<Point>{
		private Point origine;
		ComparateurPoint(Point origine){
			this.origine = origine;
		}
		public int compare(Point s1, Point s2) {
			double property1 = origine.distance(s1);
			double property2 = origine.distance(s2);
			int result;
			if(property1==property2){result=0;}
			else{
				result=property1<property2?-1:1;
			}			
			return result;
		}
	}

	private Point getPlusProche(HashMap<Point, Double> distances, ArrayList<Point> nonVisites){
		double dMin = Double.POSITIVE_INFINITY;
		Point pp = null;
		for(Point s : nonVisites){
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


	private void generateEdges(){
		this.delaunayEdges = (ArrayList<Point[]>) delaunay.computeEdges();
		this.edges=new ArrayList<Point[]>();

		edgesMatrix=new boolean[Display1.nbPoints][Display1.nbPoints];
		//initialization to false
		for (int i=0;i<Display1.nbPoints;i++){ 
			for (int j=0;j<Display1.nbPoints;j++){
				edgesMatrix[i][j]=false;
			}
		}
		//transformation in matrix
		for (int k=0;k<delaunayEdges.size();k++){ 
			int i=ToolBox.getPoint((int)delaunayEdges.get(k)[0].getX(),(int)delaunayEdges.get(k)[0].getY(),myPoints);
			int j=ToolBox.getPoint((int)delaunayEdges.get(k)[1].getX(),(int)delaunayEdges.get(k)[1].getY(),myPoints);
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
		for (int i=nbPointsZone1+nbPointsZone2;i<Display1.nbPoints;i++){
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
		for (int i=nbPointsZone1+nbPointsZone2;i<Display1.nbPoints;i++){
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
		edges=new ArrayList<Point[]>();
		for (int i=0;i<Display1.nbPoints;i++){
			for (int j=0;j<i;j++){
				if (edgesMatrix[i][j]==true){
					Point[] edge=new Point[2];
					edge[0]=myPoints.get(i);
					edge[1]=myPoints.get(j);
					edges.add(edge);
				}
			}
		}
	}

	public Point[] getMesPoints() {
		Point[] mesPoints = new Point[myPoints.size()];
		myPoints.toArray(mesPoints);
		return mesPoints;
	}

	private void setMesPoints(ArrayList<Point> mesPoints) {
		this.myPoints = mesPoints;
	}

	private ArrayList<Point[]> getEdges() {
		return edges;
	}

	private void setEdges(ArrayList<Point[]> edges) {
		this.edges = edges;
	}

	private ArrayList<Point[]> getDelaunayEdges() {
		return delaunayEdges;
	}

	private void setDelaunayEdges(ArrayList<Point[]> delaunayEdges) {
		this.delaunayEdges = delaunayEdges;
	}

}
