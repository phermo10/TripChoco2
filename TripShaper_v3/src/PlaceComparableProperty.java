
public enum PlaceComparableProperty {
	SCORE(0),
	DUREEVISITE(1),
	DISTANCERELATIVE(2);
	private final int propertyCode;

	private PlaceComparableProperty(int propertyCode) {
		this.propertyCode = propertyCode;
	}	
}
