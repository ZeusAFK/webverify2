package forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import services.ScanScheduleService;

@SuppressWarnings("serial")
public class DashboardFrame extends JInternalFrame {

	private JLabel scheduledScansCountLabel;
	private JLabel runningScansCountLabel;

	private ScanScheduleService scanScheduleService;

	public DashboardFrame() {
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 541, 489);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		scanScheduleService = ScanScheduleService.getInstance();

		scheduledScansCountLabel = new JLabel("Site scans scheduled: " + scanScheduleService.getSchedules().size());
		GridBagConstraints gbc_scheduledScansCountLabel = new GridBagConstraints();
		gbc_scheduledScansCountLabel.gridwidth = 2;
		gbc_scheduledScansCountLabel.insets = new Insets(0, 0, 5, 5);
		gbc_scheduledScansCountLabel.anchor = GridBagConstraints.WEST;
		gbc_scheduledScansCountLabel.gridx = 0;
		gbc_scheduledScansCountLabel.gridy = 0;
		panel.add(scheduledScansCountLabel, gbc_scheduledScansCountLabel);

		runningScansCountLabel = new JLabel("Active scans count: " + scanScheduleService.getScans().size());
		GridBagConstraints gbc_runningScansCountLabel = new GridBagConstraints();
		gbc_runningScansCountLabel.gridwidth = 2;
		gbc_runningScansCountLabel.insets = new Insets(0, 0, 5, 0);
		gbc_runningScansCountLabel.anchor = GridBagConstraints.WEST;
		gbc_runningScansCountLabel.gridx = 0;
		gbc_runningScansCountLabel.gridy = 1;
		panel.add(runningScansCountLabel, gbc_runningScansCountLabel);

		scanScheduleService.getSchedules().addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				scheduledScansCountLabel.setText("Site scans scheduled: " + scanScheduleService.getSchedules().size());
			}
		});

		scanScheduleService.getScans().addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				runningScansCountLabel.setText("Running scans: " + scanScheduleService.getScans().size());
			}
		});
	}
}
