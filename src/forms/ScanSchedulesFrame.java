package forms;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ScanSchedulesFrame {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScanSchedulesFrame window = new ScanSchedulesFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ScanSchedulesFrame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
	}

}
