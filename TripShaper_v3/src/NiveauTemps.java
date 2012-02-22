/**
 * Décrit les différent niveaux de temps que peut rester l'utilisateur en un lieu.
 * @author PH
 *
 */
public enum NiveauTemps {
	PAS_DE_VISITE(0),
	TEMPS_MOY(1);
	private final int codeDeNiveau;
	private NiveauTemps(int codeDeNiveau) {
		this.codeDeNiveau = codeDeNiveau;
	}
}
