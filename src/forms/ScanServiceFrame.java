package forms;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import services.ScanScheduleService;
import services.ScanService;

public class ScanServiceFrame {

	private JFrame frame;
	private int schedule;
	private ScanScheduleService scanScheduleService;

	public ScanServiceFrame(int schedule) {
		this.schedule = schedule;
		scanScheduleService = ScanScheduleService.getInstance();
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 450, 300);

		ScanService service = scanScheduleService.getScans().getById(schedule);
		service.getScan().addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {

			}
		});
	}

}
