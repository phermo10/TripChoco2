import java.io.File;

/**
 * Emplacements et noms de base des différents fichiers et dossiers.
 * Structure générale :
 * savings/Graph<Graph_ID>/Graph<Graph_ID>.txt
 * savings/Graph<Graph_ID>/User<User_ID>.txt
 * @author PH
 *
 */
public enum Emplacements {
	DOSSIER_SAVINGS{
		public String toString(){
			return "savings";
		}
	},
	DOSSIER_GRAPH_BASE{
		public String toString(){
			return DOSSIER_SAVINGS+File.separator+"Graph";
		}
	},
	FICHIER_GRAPH_BASE{
		public String toString(){
			return File.separator+"Graph";
		}
	},
	FICHIER_USER_BASE{
		public String toString(){
			return File.separator+"User";
		}
	};
}
