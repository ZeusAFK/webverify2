package data.enums;

public enum ScanScheduleStatus {
	Deleted(0), Scheduled(1), Scanning(2), Failed(3), Completed(4);

	private int value;

	private ScanScheduleStatus(int value) {
		this.value = value;
	}

	public static ScanScheduleStatus fromValue(int value) throws IllegalArgumentException {
		try {
			return ScanScheduleStatus.values()[value];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unknown enum value :" + value);
		}
	}

	public int getValue() {
		return value;
	}
}
