package forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

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

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ScanSchedulesFrame extends JInternalFrame {

	private JTable schedulesTable;
	private ScanScheduleService scanScheduleService;

	public ScanSchedulesFrame() {
		scanScheduleService = ScanScheduleService.getInstance();

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

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 7;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		panel.add(scrollPane, gbc_scrollPane);

		schedulesTable = new JTable(new ScanSchedulesTableModel(scanScheduleService.getSchedules().getCollection()));
		schedulesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		schedulesTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(schedulesTable);

		schedulesTable.getColumnModel().getColumn(0).setMaxWidth(0);
		schedulesTable.getColumnModel().getColumn(0).setMinWidth(0);
		schedulesTable.getColumnModel().getColumn(0).setPreferredWidth(0);

		JButton btnViewScanDetails = new JButton("Open selected scan");
		btnViewScanDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (schedulesTable.getSelectedRow() >= 0) {
					int schedule = Integer.valueOf(String.valueOf(schedulesTable.getValueAt(schedulesTable.getSelectedRow(), 0)));
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

	private class ScanSchedulesTableModel extends DefaultTableModel {

		private String[] columnNames = { "Id", "Site", "Url", "Ip", "Scan interval", "Last scan", "Status" };
		private ArrayList<ScanSchedule> list;

		public ScanSchedulesTableModel(ArrayList<ScanSchedule> list) {
			this.list = list;
			scanScheduleService.getSchedules().addObserver(new Observer() {
				@Override
				public void update(Observable arg0, Object arg1) {
					fireTableDataChanged();
				}
			});
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return list != null ? list.size() : 0;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Object getValueAt(int row, int col) {
			ScanSchedule schedule = list.get(row);
			switch (col) {
			case 0:
				return schedule.getId();
			case 1:
				return schedule.getSite().getName();
			case 2:
				return schedule.getSite().getUrl();
			case 3:
				return schedule.getSite().getIp();
			case 4:
				return String.valueOf(schedule.getInterval()) + " minute(s)";
			case 5:
				try {
					return new SimpleDateFormat("yyyy/dd/MM HH:mm:ss").format(schedule.getLastScan()) + " ("
							+ ((int) ((new Date().getTime() / 60000) - (schedule.getLastScan().getTime() / 60000))) + " minute(s) ago)";
				} catch (Exception e) {
					return "";
				}
			case 6:
				switch (schedule.getStatus()) {
				case Completed:
					return "Completed";
				case Deleted:
					return "Deleted";
				case Failed:
					return "Failed";
				case Scanning:
					return "Scanning";
				case Scheduled:
					return "Scheduled";
				default:
					return "";
				}
			default:
				return "unknow";
			}
		}

		public Class<? extends Object> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}
