import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.print.attribute.standard.MediaSize.Engineering;


public class Graphique {
	ArrayList<Point> myPoints;
	ArrayList<Point[]> delaunayEdges;
	ArrayList<Point[]> edges;
	ArrayList<Point[]> tempEdges;
	ArrayList<ArrayList<Point[]>> ZoneEdges;
	ArrayList<ArrayList<Point>> zonesRegroupement;
	Delaunay delaunay;
	ArrayList<Point> centresZonesRegroupement;
	boolean[][] edgesMatrix;

	public Graphique(int nbPoints, int nbPointsRegroupement) throws IOException {
		super();
		this.delaunay = new Delaunay();
		this.myPoints = new ArrayList<Point>();
		this.zonesRegroupement= new ArrayList<ArrayList<Point>>();
		this.centresZonesRegroupement=new ArrayList<Point>();
		generatePoints();
		generateEdges();
		simplifyEdges();
		saveGraph();
	}
	
	public void generatePoints(){
		ArrayList<Point> zone1 = null;
		ArrayList<Point> zone2 = null;
		int xPointRegroupement1=0;
		int yPointRegroupement1=0;
		int xPointRegroupement2=0;
		int yPointRegroupement2=0;
		zone1=new ArrayList<Point>();
		zone2=new ArrayList<Point>();
		for (int i=0;i<Display1.nbPoints;i++){
			int x=0;
			int y=0;
			int whichCase=-1;
			if (i==0){//first point zone1
				whichCase=0;
			}
			if (i>0&&i<9){//points zone1
				whichCase=1;
			}
			if (i==9){//last point zone1
				whichCase=2;
			}
			if (i==10){//first point zone2
				whichCase=3;
			}
			if (i>10&&i<19){//points zone2
				whichCase=4;
			}
			if (i==19){//last point zone2
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
				zone1.add(new Point(x,y));
				centresZonesRegroupement.add(new Point(x,y));
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
				zone1.add(new Point(x,y));
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
				zone1.add(new Point(x,y));
				zonesRegroupement.add(zone1);
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
				zone2.add(new Point(x,y));
				centresZonesRegroupement.add(new Point(x,y));
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
				zone2.add(new Point(x,y));
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
				zone2.add(new Point(x,y));
				zonesRegroupement.add(zone2);
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
		PrintWriter writer =  new PrintWriter(new BufferedWriter
				(new FileWriter("savings" + File.separator +"graph.txt")));
		writer.println("places");
		for (Point i : myPoints){
			writer.println(i.getX());
			writer.println(i.getY());
		}
		writer.println("end places");
		writer.println("edges");
		for (Point[] i : edges){
			writer.println(i[0].getX());
			writer.println(i[0].getY());
			writer.println(i[1].getX());
			writer.println(i[1].getY());
		}
		writer.println("end edges");
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
		for (int i=10;i<20;i++){ 
			for (int j=0;j<10;j++){
				if (edgesMatrix[i][j]==true){
					pathBetweenZones=true;
				}
			}
		}
		//creating one path between the two zones
		if (pathBetweenZones==true){ 
			for (int i=10;i<20;i++){ 
				for (int j=0;j<10;j++){
					edgesMatrix[i][j]=false;
					edgesMatrix[j][i]=false;
				}
			}
			int i=(int) (10+Math.random()*9);
			int j=(int) (Math.random()*9);
			edgesMatrix[i][j]=true;
			edgesMatrix[j][i]=true;
		}
		//creating only one path between a point and zone 1
		for (int i=20;i<Display1.nbPoints;i++){
			int indexPath=-1;
			for (int j=0;j<10;j++){
				if (edgesMatrix[i][j]==true){
					indexPath=j;
				}
			}
			if (indexPath!=-1){
				for (int j=0;j<10;j++){
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
		for (int i=20;i<Display1.nbPoints;i++){
			int indexPath=-1;
			for (int j=10;j<20;j++){
				if (edgesMatrix[i][j]==true){
					indexPath=j;
				}
			}
			if (indexPath!=-1){
				for (int j=10;j<20;j++){
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
		tempEdges=new ArrayList<Point[]>();
		for (int i=0;i<Display1.nbPoints;i++){
			for (int j=0;j<i;j++){
				if (edgesMatrix[i][j]==true){
					Point[] edge=new Point[2];
					edge[0]=myPoints.get(i);
					edge[1]=myPoints.get(j);
					tempEdges.add(edge);
				}
			}
		}
	}
	
	public void simplifyEdges(){
		edges=tempEdges;
		//erasing edges too closer to each other
		/*for (int i=0;i<tempEdges.size();i++){
			for (int j=0;j<tempEdges.size();j++){
				if (i!=j){
					int whichCase=-1;
					int alpha=-1;
					int beta=-1;
					double xi1=tempEdges.get(i)[0].getX();
					double xi2=tempEdges.get(i)[1].getX();
					double yi1=tempEdges.get(i)[0].getY();
					double yi2=tempEdges.get(i)[1].getY();
					double lengthi=Math.sqrt((xi1-xi2)*(xi1-xi2)+(yi1-yi2)*(yi1-yi2));
					double xj1=tempEdges.get(j)[0].getX();
					double xj2=tempEdges.get(j)[1].getX();
					double yj1=tempEdges.get(j)[0].getY();
					double yj2=tempEdges.get(j)[1].getY();
					double lengthj=Math.sqrt((xj1-xj2)*(xj1-xj2)+(yj1-yj2)*(yj1-yj2));
					double dist=0;

					if (tempEdges.get(i)[0].equals(tempEdges.get(j)[0])){
						whichCase=1;
					}
					else{
						if (tempEdges.get(i)[0].equals(tempEdges.get(j)[1])){
							whichCase=2;
						}
						else{
							if (tempEdges.get(i)[1].equals(tempEdges.get(j)[0])){
								whichCase=3;
							}
							else{
								if (tempEdges.get(i)[1].equals(tempEdges.get(j)[1])){
									whichCase=4;
								}
								else{
									if (!edges.contains(tempEdges.get(i))){
										//edges.add(tempEdges.get(i));
									}					
								}
							}
						}
					}
					boolean toDelete=false;
					int edge=0;
					int point=0;
					int edgePoint=0;
					switch(whichCase){
					case 1 :
						if (lengthi>=lengthj&&2*lengthj>=lengthi){
							edge=i;edgePoint=j;point=1;
							toDelete=true;
						}
						if (lengthj>=lengthi&&2*lengthi>=lengthj){
							edge=j;edgePoint=i;point=1;
							toDelete=true;
						}
						break;
					case 2:
						if ((lengthi>=lengthj)&&2*lengthj>=lengthi){
							edge=i;edgePoint=j;point=0;
							toDelete=true;
						}
						if (lengthj>=lengthi&&2*lengthi>=lengthj){
							edge=j;edgePoint=i;point=1;
							toDelete=true;
						}
						break;
					case 3:
						if ((lengthi>=lengthj)&&2*lengthj>=lengthi){
							edge=i;edgePoint=j;point=1;
							toDelete=true;
						}
						if (lengthj>=lengthi&&2*lengthi>=lengthj){
							edge=j;edgePoint=i;point=0;
							toDelete=true;
						}
						break;
					case 4:
						if ((lengthi>=lengthj)&&2*lengthj>=lengthi){
							edge=i;edgePoint=j;point=0;
							toDelete=true;
						}
						if (lengthj>=lengthi&&2*lengthi>=lengthj){
							edge=j;edgePoint=i;point=0;
							toDelete=true;
						}
						break;
					}	
					dist=ToolBox.pointToLineDistance(tempEdges.get(edge)[0],tempEdges.get(edge)[1],tempEdges.get(edgePoint)[point]);
					if ((dist>Display1.minDistPaths)&&toDelete==true){
						if (!edges.contains(tempEdges.get(edge))){
							edges.add(tempEdges.get(edge));
						}
					}
				}
			}
		}*/
		
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

	public ArrayList<ArrayList<Point>> getZonesRegroupement() {
		return zonesRegroupement;
	}

	public ArrayList<Point> getCentreZonesRegroupement() {
		return centresZonesRegroupement;
	}




}
