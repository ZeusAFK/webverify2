package forms;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdownloader.gui.laf.jddefault.JDDefaultLookAndFeel;

import data.models.ConfigurationItem;

public class MainFrame {

	private JFrame frame;

	private JDesktopPane desktopPaneDashboard;
	private JDesktopPane desktopPaneSchedules;

	public MainFrame() {
		String[] licenseInformation = { "Licensee=Annonymous", "LicenseRegistrationNumber=------", "Product=Synthetica", "LicenseType=Non Commercial",
				"ExpireDate=--.--.----", "MaxVersion=999.999.999" };
		UIManager.put("Synthetica.license.info", licenseInformation);
		UIManager.put("Synthetica.license.key", "3F3ECAB3-35CA4FF1-0EDD14FC-293EC659-E6FFE9C1");
		try {
			UIManager.setLookAndFeel(new JDDefaultLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		int state = frame.getExtendedState();
		state &= ~JFrame.ICONIFIED;
		frame.setExtendedState(state);
		frame.setAlwaysOnTop(true);
		frame.toFront();
		frame.requestFocus();
		frame.setAlwaysOnTop(false);
		// frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/forms/resources/img/icons/Logo-icon.png")));

		String revision = "";
		try {
			revision = new ConfigurationItem("revision").getValue().toString();
		} catch (Exception e) {
			revision = "Unknow";
		}
		frame.setTitle("webverify2: Integrity monitor for websites - revision " + revision);
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 565, 529);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// frame.setExtendedState(frame.getExtendedState() |
		// JFrame.MAXIMIZED_BOTH);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);

		desktopPaneDashboard = new JDesktopPane();
		desktopPaneDashboard.setBackground(Color.WHITE);
		tabbedPane.addTab("Dashboard", null, desktopPaneDashboard, null);

		GridBagLayout gbl_desktopPaneDashboard = new GridBagLayout();
		gbl_desktopPaneDashboard.columnWidths = new int[] { 514, 0 };
		gbl_desktopPaneDashboard.rowHeights = new int[] { 559, 0 };
		gbl_desktopPaneDashboard.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_desktopPaneDashboard.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		desktopPaneDashboard.setLayout(gbl_desktopPaneDashboard);

		DashboardFrame internalDashboardFrame = new DashboardFrame();
		internalDashboardFrame.pack();
		internalDashboardFrame.setVisible(true);
		internalDashboardFrame.setAlignmentX(0);
		internalDashboardFrame.setAlignmentY(0);
		((javax.swing.plaf.basic.BasicInternalFrameUI) internalDashboardFrame.getUI()).setNorthPane(null);
		GridBagConstraints gbc_internalDashboardFrame = new GridBagConstraints();
		gbc_internalDashboardFrame.fill = GridBagConstraints.BOTH;
		gbc_internalDashboardFrame.gridx = 0;
		gbc_internalDashboardFrame.gridy = 0;
		desktopPaneDashboard.add(internalDashboardFrame, gbc_internalDashboardFrame);

		desktopPaneSchedules = new JDesktopPane();
		desktopPaneSchedules.setBackground(Color.WHITE);
		tabbedPane.addTab("Scan schedules", null, desktopPaneSchedules, null);

		GridBagLayout gbl_desktopPaneSchedules = new GridBagLayout();
		gbl_desktopPaneSchedules.columnWidths = new int[] { 514, 0 };
		gbl_desktopPaneSchedules.rowHeights = new int[] { 559, 0 };
		gbl_desktopPaneSchedules.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_desktopPaneSchedules.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		desktopPaneSchedules.setLayout(gbl_desktopPaneSchedules);

		ScanSchedulesFrame internalSchedulesFrame = new ScanSchedulesFrame();
		internalSchedulesFrame.pack();
		internalSchedulesFrame.setVisible(true);
		internalSchedulesFrame.setAlignmentX(0);
		internalSchedulesFrame.setAlignmentY(0);
		((javax.swing.plaf.basic.BasicInternalFrameUI) internalSchedulesFrame.getUI()).setNorthPane(null);
		GridBagConstraints gbc_internalSchedulesFrame = new GridBagConstraints();
		gbc_internalSchedulesFrame.fill = GridBagConstraints.BOTH;
		gbc_internalSchedulesFrame.gridx = 0;
		gbc_internalSchedulesFrame.gridy = 0;
		desktopPaneSchedules.add(internalSchedulesFrame, gbc_internalSchedulesFrame);
	}
}
