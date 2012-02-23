import java.io.File;

/**
 * Emplacements et noms de base des différents fichiers et dossiers.
 * Structure générale :
 * savings/Graph<Graph_ID>/Graph<Graph_ID>.txt
 * savings/Graph<Graph_ID>/User<User_ID>.txt
 * @author PH
 *
 */
public class Emplacements {
	public static final String DOSSIER_SAVINGS = "savings";
	private static final String DOSSIER_GRAPH_BASE =  DOSSIER_SAVINGS+File.separator+"Graph";
	private static final String FICHIER_GRAPH_BASE = File.separator+"Graph";
	private static final String FICHIER_USER_BASE = File.separator+"User";
	public static String DOSSIER_GRAPH_COMPLET(int graphID){
		return DOSSIER_GRAPH_BASE + graphID;
	}
	public static String FICHIER_USER_COMPLET(int graphID, int userID){
		return DOSSIER_GRAPH_COMPLET(graphID) + File.separator + FICHIER_USER_BASE.toString() + userID +".txt";
	}
	public static String FICHIER_GRAPH_COMPLET(int graphID){
		return DOSSIER_GRAPH_BASE + graphID + FICHIER_GRAPH_BASE + graphID +".txt";
	}
	public static String FICHIER_PCC(int graphID){
		return DOSSIER_GRAPH_COMPLET(graphID) + File.separator + "pcc" +".txt";
	}
}
