package utils;

public final class ThreadUtils {

	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleepSeconds(int seconds) {
		sleep(seconds * 1000);
	}
}
