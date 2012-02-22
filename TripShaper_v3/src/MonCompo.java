import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class MonCompo extends Component{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Graphique graph;
	private ITIN itin;

		public void paint(Graphics arg0) { 
			/*Graphics2D g = (Graphics2D)arg0; 
			g.setColor(Color.blue);
			for (Point p : this.graph.getMesPoints()){
				g.fill3DRect((int)p.getX(),(int)p.getY(), 10, 10, true);
			}
			ArrayList<Point[]> edges = this.graph.getEdges();
			System.out.println("this.graph.getEdges().size() : "+this.graph.getEdges().size());
			System.out.println("this.graph.getDelaunayEdges().size() : "+this.graph.getDelaunayEdges().size());
			System.out.println("MonCompo : zonesRegroupement.size() : "+this.graph.getZonesRegroupement().size());
			for (Point[] arc : edges){
				Point p1 = arc[0];
				Point p2 = arc[1];
				g.drawLine((int)p1.getX(),(int) p1.getY(), (int)p2.getX(),(int) p2.getY());
			}
			for (int i=0;i<this.graph.getCentreZonesRegroupement().size();i++){
				int x=(int) this.graph.getCentreZonesRegroupement().get(i).getX();
				int y=(int) this.graph.getCentreZonesRegroupement().get(i).getY();
				g.drawOval(x-25,y-25,80,80);
			}*/
			Graphics2D g = (Graphics2D)arg0; 
			g.setColor(Color.blue);
			
			int n=0;
			for (Point p : this.graph.getMesPoints()){
				n++;
				g.fill3DRect((int)p.getX(),(int)p.getY(), 10, 10, true);
			}
			ArrayList<Point[]> edges = this.graph.getEdges();
			System.out.println("this.graph.getEdges().size() : "+this.graph.getEdges().size());
			//System.out.println("this.graph.getDelaunayEdges().size() : "+this.graph.getDelaunayEdges().size());
			//System.out.println("MonCompo : zonesRegroupement.size() : "+this.graph.getZonesRegroupement().size());
			for (Point[] arc : edges){
				Point p1 = arc[0];
				Point p2 = arc[1];
				g.drawLine((int)p1.getX(),(int) p1.getY(), (int)p2.getX(),(int) p2.getY());
			}
			
			if (itin!=null){
				g.setColor(Color.red);
				for (int i=0;i<itin.getEtapes().size()-1;i++){
					g.fill3DRect((int)itin.getEtapes().get(i).getPosition().getX(),(int)itin.getEtapes().get(i).getPosition().getY(), 10, 10, true);
					g.drawLine((int)itin.getEtapes().get(i).getPosition().getX(),(int)itin.getEtapes().get(i).getPosition().getY(), (int)itin.getEtapes().get(i+1).getPosition().getX(),(int)itin.getEtapes().get(i+1).getPosition().getY());
				}
				g.fill3DRect((int)itin.getEtapes().get(itin.getEtapes().size()-1).getPosition().getX(),(int)itin.getEtapes().get(itin.getEtapes().size()-1).getPosition().getY(), 10, 10, true);
			}
			

			/*for (int i=0;i<this.graph.getCentreZonesRegroupement().size();i++){
				int x=(int) this.graph.getCentreZonesRegroupement().get(i).getX();
				int y=(int) this.graph.getCentreZonesRegroupement().get(i).getY();
				g.drawOval(x-25,y-25,80,80);
			}*/
			/*int x1=(int) this.graph.getCentreZonesRegroupement().get(0).getX();
			int y1=(int) this.graph.getCentreZonesRegroupement().get(0).getY();
			g.drawOval(x1-25,y1-25,80,80);
			int x2=(int) this.graph.getCentreZonesRegroupement().get(1).getX();
			int y2=(int) this.graph.getCentreZonesRegroupement().get(1).getY();
			g.drawOval(x2-25,y2-25,110,110);*/
		}

	
			//g.drawLine(50,50,100,100);
			/*ArrayList<Point[]> edges = (ArrayList<Point[]>) delaunay.computeEdges();
			for (int i=0;i<nbPoints;i++){
				g.fill3DRect((int)mesPoints.get(i).getX(),(int)mesPoints.get(i).getY(), 10, 10, true);
			}
			for (int n=0;n<edges.size();n++){
				//System.out.println(n);
				g.drawLine((int)edges.get(n)[0].getX(), (int)edges.get(n)[0].getY(),(int)edges.get(n)[1].getX(),(int) edges.get(n)[1].getY());
			}

		}*/
		
		

		public MonCompo(Graphique graph) {
			super();
			this.graph = graph;
			this.itin = null;
		}
		public MonCompo(Graphique graph, ITIN itin) {
			super();
			this.graph = graph;
			this.itin = itin;
		}
	}