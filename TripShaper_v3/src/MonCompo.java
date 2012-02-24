import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MonCompo extends Component{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ITIN itin;

		public void paint(Graphics arg0) { 
			Graphics2D g = (Graphics2D)arg0; 
			g.setColor(Color.blue);

			for (Place p1 : this.graph.getAllplaces()){
				g.fill3DRect(p1.getPosition().x,p1.getPosition().y, 10, 10, true);
				g.drawString(p1.toString() + "=" + graph.getScores().get(p1),p1.getPosition().x,p1.getPosition().y);
				for(Path p : p1.getPathsFromThisPlace()){
					g.drawLine(p.end1.getPosition().x,p.end1.getPosition().y,p.end2.getPosition().x,p.end2.getPosition().y);
				}				
			}
			if (itin!=null){
				g.setColor(Color.red);
				for (int i=0;i<itin.getEtapes().size()-1;i++){
					if(itin.getEtapes().get(i).getNiveauTemps()==NiveauTemps.TEMPS_MOY) g.setColor(Color.GREEN);
					g.fill3DRect(itin.getEtapes().get(i).getPlace().getPosition().x,itin.getEtapes().get(i).getPlace().getPosition().y, 10, 10, true);
					g.setColor(Color.red);
					g.drawLine(itin.getEtapes().get(i).getPlace().getPosition().x,itin.getEtapes().get(i).getPlace().getPosition().y, itin.getEtapes().get(i+1).getPlace().getPosition().x,itin.getEtapes().get(i+1).getPlace().getPosition().y);
				}
				if(itin.getEtapes().get(itin.getEtapes().size()-1).getNiveauTemps()==NiveauTemps.TEMPS_MOY) g.setColor(Color.GREEN);
				g.fill3DRect(itin.getEtapes().get(itin.getEtapes().size()-1).getPlace().getPosition().x,itin.getEtapes().get(itin.getEtapes().size()-1).getPlace().getPosition().y, 10, 10, true);
				g.setColor(Color.red);
			}
		}


		public MonCompo(Graph graph) {
			super();
			this.graph = graph;
			this.itin = null;
		}
		public MonCompo(Graph graph, ITIN itin) {
			super();
			this.graph = graph;
			this.itin = itin;
		}
		
		private Graph graph;
	}