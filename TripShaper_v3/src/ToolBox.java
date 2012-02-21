import java.awt.Point;
import java.util.ArrayList;


public class ToolBox {
	
	public static int getPoint(int x, int y, ArrayList<Point> myPoints){
		int index=0;
		while (index<Display1.nbPoints){
			if ((((Point)myPoints.get(index)).getX()==x)&&(((Point)myPoints.get(index)).getY()==y)){
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public static double pointToLineDistance(Point A, Point B, Point P) {
	    double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
	    return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
	  }
}
