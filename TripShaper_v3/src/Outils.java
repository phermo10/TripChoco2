
import java.text.Normalizer;


public class Outils {
		
	/**
	 * Permet une plus grande souplesse du programme en negligeant les espaces, accents et majuscules de certaines string (ex : recherches de l'utilisateur)
	 * @param avecAccentsMajusculesEtEspaces la chaine a neutraliser
	 * @param enleverEspaces sil faut aussi enlever les espaces
	 * @return la chaine neutralisee cad sans majuscules, sans accents et eventuellement sans espaces
	 */
	public static String neutraliser(String avecAccentsMajusculesEtEspaces, boolean enleverEspaces) {
		String sansAccentsNiMajusculesNiEspaces = Normalizer.normalize(avecAccentsMajusculesEtEspaces.toLowerCase(), Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
		if(enleverEspaces){
			sansAccentsNiMajusculesNiEspaces = sansAccentsNiMajusculesNiEspaces.replaceAll(" ", "");
		}
		return sansAccentsNiMajusculesNiEspaces;
	}

}
