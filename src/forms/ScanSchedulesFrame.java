package forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import data.models.ScanSchedule;
import services.ScanScheduleService;
import utils.ThreadUtils;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ScanSchedulesFrame extends JInternalFrame {

	private JTable shedulesTable;
	private MyTableModel schedulesListModel;
	private ScanScheduleService scanScheduleService;
	private boolean updating_schedules_table;

	public ScanSchedulesFrame() {
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
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblDescripcionDelModulo = new JLabel("Active scan schedules list");
		GridBagConstraints gbc_lblDescripcionDelModulo = new GridBagConstraints();
		gbc_lblDescripcionDelModulo.gridwidth = 2;
		gbc_lblDescripcionDelModulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescripcionDelModulo.anchor = GridBagConstraints.WEST;
		gbc_lblDescripcionDelModulo.gridx = 0;
		gbc_lblDescripcionDelModulo.gridy = 0;
		panel.add(lblDescripcionDelModulo, gbc_lblDescripcionDelModulo);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 1;
		panel.add(separator_1, gbc_separator_1);

		schedulesListModel = new MyTableModel();
		schedulesListModel.addColumn("Id");
		schedulesListModel.addColumn("Site");
		schedulesListModel.addColumn("Url");
		schedulesListModel.addColumn("Ip");
		schedulesListModel.addColumn("Scan interval");
		schedulesListModel.addColumn("Last scan");
		schedulesListModel.addColumn("Status");

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 7;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		panel.add(scrollPane, gbc_scrollPane);

		shedulesTable = new JTable();
		shedulesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shedulesTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(shedulesTable);

		shedulesTable.setModel(schedulesListModel);

		shedulesTable.getColumnModel().getColumn(0).setMaxWidth(0);
		shedulesTable.getColumnModel().getColumn(0).setMinWidth(0);
		shedulesTable.getColumnModel().getColumn(0).setPreferredWidth(0);

		scanScheduleService = ScanScheduleService.getInstance();

		updating_schedules_table = false;

		scanScheduleService.getSchedules().addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				while (updating_schedules_table) {
					ThreadUtils.sleep(100);
				}
				updating_schedules_table = true;
				try {
					updateSchedulesTable();
				} catch (Exception e) {

				}
				updating_schedules_table = false;
			}
		});

		JButton btnViewScanDetails = new JButton("Open selected scan");
		btnViewScanDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (shedulesTable.getSelectedRow() >= 0) {
					int schedule = Integer.valueOf(String.valueOf(shedulesTable.getValueAt(shedulesTable.getSelectedRow(), 0)));
					new ScanServiceFrame(schedule);
				}
			}
		});

		GridBagConstraints gbc_btnViewScanDetails = new GridBagConstraints();
		gbc_btnViewScanDetails.insets = new Insets(0, 0, 5, 0);
		gbc_btnViewScanDetails.gridx = 2;
		gbc_btnViewScanDetails.gridy = 0;
		panel.add(btnViewScanDetails, gbc_btnViewScanDetails);

		scanScheduleService.getSchedules().update();
	}

	public synchronized void updateSchedulesTable() {
		schedulesListModel.setRowCount(0);
		for (ScanSchedule schedule : scanScheduleService.getSchedules().getCollection()) {
			Vector<String> row = new Vector<String>();
			row.add(String.valueOf(schedule.getId()));
			row.add(schedule.getSite().getName());
			row.add(schedule.getSite().getUrl());
			row.add(schedule.getSite().getIp());
			row.add(String.valueOf(schedule.getInterval()) + " minute(s)");
			try {
				row.add(new SimpleDateFormat("yyyy/dd/MM HH:mm:ss").format(schedule.getLastScan()) + " ("
						+ ((int) ((new Date().getTime() / 60000) - (schedule.getLastScan().getTime() / 60000))) + " minute(s) ago)");
			} catch (Exception e) {
				row.add("");
			}

			String scheduleStatus = "unknow";
			switch (schedule.getStatus()) {
			case Completed:
				scheduleStatus = "Completed";
				break;
			case Deleted:
				scheduleStatus = "Deleted";
				break;
			case Failed:
				scheduleStatus = "Failed";
				break;
			case Scanning:
				scheduleStatus = "Scanning";
				break;
			case Scheduled:
				scheduleStatus = "Scheduled";
				break;
			default:
				break;
			}
			row.add(scheduleStatus);
			schedulesListModel.addRow(row);
		}
	}

	public class MyTableModel extends DefaultTableModel {

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

	}
}
