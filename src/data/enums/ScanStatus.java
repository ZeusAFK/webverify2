package data.enums;

public enum ScanStatus {
	Created(1), Running(2), Failed(3), Completed(5), Deleted(0);

	@SuppressWarnings("unused")
	private int value;

	private ScanStatus(int value) {
		this.value = value;
	}
}
