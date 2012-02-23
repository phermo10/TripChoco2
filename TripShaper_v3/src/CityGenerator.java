import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CityGenerator {
	String graphID;
	ArrayList<Point> myPoints;
	ArrayList<Point[]> delaunayEdges;
	ArrayList<Point[]> edges;
	//ArrayList<Point[]> tempEdges;
	//ArrayList<ArrayList<Point[]>> ZoneEdges;
	//ArrayList<ArrayList<Point>> zonesRegroupement;
	Delaunay delaunay;
	//ArrayList<Point> centresZonesRegroupement;
	boolean[][] edgesMatrix;
	int nbPointsZone1;
	int nbPointsZone2;
	
	public CityGenerator(String graphID){
		this.graphID = graphID;
		this.myPoints = new ArrayList<Point>();
		this.delaunay = new Delaunay();
		//pointsIDhashmap = new HashMap<Point,Integer>();
		//edgesIDhashmap = new HashMap<Point[],Integer>();
		this.edges=new ArrayList<Point[]>();
		try {
			String filename = "savings" + File.separator +graphID + File.separator +graphID +".txt";
			Scanner reader = new Scanner(new File (filename));
			String toRead = reader.nextLine();
			int nbpoints=((int)Integer.parseInt(toRead));
			toRead = reader.nextLine();
			toRead = reader.nextLine();

			do {
				System.out.println("toread : "+toRead);
				int x = (int)(Integer.parseInt(toRead));
				toRead=reader.nextLine();
				int y = Integer.parseInt(toRead);
				Point position = new Point(x,y);
				myPoints.add(position);
				delaunay.insertPoint(new Point(x,y));
				//pointsIDhashmap.put(position,(int)Integer.parseInt(toRead));
				toRead=reader.nextLine();
			}	
			while (!toRead.equals("</Places>"));
			toRead = reader.nextLine();
			//toRead = reader.nextLine();
			//System.out.println("toRead : "+toRead);
			this.edgesMatrix=new boolean[nbpoints][nbpoints];
			for (int i=0;i<nbpoints;i++){
				for (int j=0;j<nbpoints;j++){
					toRead = reader.nextLine();
					this.edgesMatrix[i][j]=Boolean.valueOf(toRead);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CityGenerator(int nbPoints, int nbPointsRegroupement) throws IOException {
		super();
		this.graphID="graph"+((int)(Math.random()*1000));
		this.delaunay = new Delaunay();
		this.myPoints = new ArrayList<Point>();
		generatePoints();
		generateEdges();
		saveGraph();
	}
	
	public String getID(){
		return this.graphID;
	}

	public void generatePoints(){
		ArrayList<Point> zone1 = null;
		ArrayList<Point> zone2 = null;
		int xPointRegroupement1=0;
		int yPointRegroupement1=0;
		int xPointRegroupement2=0;
		int yPointRegroupement2=0;
		nbPointsZone1=0;
		while(nbPointsZone1<3){
			System.out.println("nbPointsZone1 : "+nbPointsZone1);
			nbPointsZone1=(int) (Math.random()*10);
		}
		nbPointsZone2=0;
		while(nbPointsZone2<3){
			nbPointsZone2=(int) (Math.random()*10);
		}		
		System.out.println("nbPointsZone1 : "+nbPointsZone1);
		System.out.println("nbPointsZone2 : "+nbPointsZone2);
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
				System.out.println("case 0");
				xPointRegroupement1=((int)((Display1.xWindow*Math.random()) ));
				yPointRegroupement1=((int)((Display1.yWindow*Math.random()) ));
				x=xPointRegroupement1;
				y=yPointRegroupement1;
				System.out.println("xPointRegroupement : "+xPointRegroupement1);
				System.out.println("yPointRegroupement : "+yPointRegroupement1);
				//zone1.add(new Point(x,y));
				//centresZonesRegroupement.add(new Point(x,y));
				break;
			case 1:
				System.out.println("case 1");
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
				System.out.println("case 2");
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
				System.out.println("case 3");
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
				System.out.println("xPointRegroupement 2: "+xPointRegroupement2);
				System.out.println("yPointRegroupement 2: "+yPointRegroupement2);
				//zone2.add(new Point(x,y));
				//centresZonesRegroupement.add(new Point(x,y));
				break;
			case 4:
				System.out.println("case 4");
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
				System.out.println("case 5");
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
				System.out.println("case default");
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

	public void saveGraph() throws IOException{
		File dir = new File ("savings"+File.separator+graphID);
		dir.mkdirs();
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter("savings" + File.separator +graphID+File.separator +graphID+".txt")));
		writer.println(myPoints.size());
		writer.println("<Places>");
		for (int i=0;i<myPoints.size();i++){
			writer.println((int)myPoints.get(i).getX());
			writer.println((int)myPoints.get(i).getY());
			//writer.println((int)pointsIDhashmap.get(myPoints.get(i)));
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
	}
	
	

	public void generateEdges(){
		ArrayList<Point[]> delaunayEdges = (ArrayList<Point[]>) delaunay.computeEdges();
		this.delaunayEdges = delaunayEdges;

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
				/*int a=(int) (20+Math.random()*(nbPoints-21));
				System.out.println("(20+Math.random()*(nbPoints-21) : "+a);
				int b=(int) (Math.random()*9);
				System.out.println("Math.random()*9 : "+b);*/
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
	public ArrayList<Point> getMesPoints() {
		return myPoints;
	}

	public void setMesPoints(ArrayList<Point> mesPoints) {
		this.myPoints = mesPoints;
	}

	public ArrayList<Point[]> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Point[]> edges) {
		this.edges = edges;
	}

	public ArrayList<Point[]> getDelaunayEdges() {
		return delaunayEdges;
	}

	public void setDelaunayEdges(ArrayList<Point[]> delaunayEdges) {
		this.delaunayEdges = delaunayEdges;
	}

}
