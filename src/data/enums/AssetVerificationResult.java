package data.enums;

public enum AssetVerificationResult {
	Deleted(0), Correct(1), Modified(2), Created(3);

	private int value;

	private AssetVerificationResult(int value) {
		this.value = value;
	}

	public static AssetVerificationResult fromValue(int value) throws IllegalArgumentException {
		try {
			return AssetVerificationResult.values()[value];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unknown enum value :" + value);
		}
	}

	public int getValue() {
		return value;
	}
}
