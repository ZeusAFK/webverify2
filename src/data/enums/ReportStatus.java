package data.enums;

public enum ReportStatus {
	Scheduled(1), Sended(2), Failed(3), Deleted(0);

	@SuppressWarnings("unused")
	private int value;

	private ReportStatus(int value) {
		this.value = value;
	}
}