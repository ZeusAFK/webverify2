package forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import data.models.Link;
import data.models.ScanAsset;
import services.ScanScheduleService;
import services.ScanService;

public class ScanServiceFrame {

	private JFrame frame;
	private ScanScheduleService scanScheduleService;
	private ScanService scanService;
	private JTable scanAssetsTable;
	private JTable LinksCrawlTable;

	public ScanServiceFrame(int schedule) {
		scanScheduleService = ScanScheduleService.getInstance();
		scanService = scanScheduleService.getScans().getById(schedule);
		if (scanService == null) {
			JOptionPane.showMessageDialog(null, "The selected scan is not running now");
		} else {
			initialize();
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 541, 489);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		frame.setTitle("Scan thread information: " + scanService.getScan().getId());

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblDescripcionDelModulo = new JLabel("Site scan information for " + scanService.getScan().getSite().getName() + " ["
				+ scanService.getScan().getSite().getUrl() + "]");
		GridBagConstraints gbc_lblDescripcionDelModulo = new GridBagConstraints();
		gbc_lblDescripcionDelModulo.gridwidth = 2;
		gbc_lblDescripcionDelModulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescripcionDelModulo.anchor = GridBagConstraints.WEST;
		gbc_lblDescripcionDelModulo.gridx = 0;
		gbc_lblDescripcionDelModulo.gridy = 0;
		panel.add(lblDescripcionDelModulo, gbc_lblDescripcionDelModulo);

		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.gridwidth = 2;
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 1;
		panel.add(separator_2, gbc_separator_2);

		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridheight = 3;
		gbc_scrollPane_2.gridwidth = 3;
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 2;
		panel.add(scrollPane_2, gbc_scrollPane_2);

		LinksCrawlTable = new JTable(new LinksCollectionTableModel(scanService.getScan().getCrawlLinks().getCollection()));
		LinksCrawlTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		LinksCrawlTable.getTableHeader().setReorderingAllowed(false);
		scrollPane_2.setViewportView(LinksCrawlTable);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 6;
		panel.add(separator_1, gbc_separator_1);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 4;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		panel.add(scrollPane, gbc_scrollPane);

		scanAssetsTable = new JTable(new ScanAssetsTableModel(scanService.getScan().getAssets().getCollection()));
		scanAssetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scanAssetsTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(scanAssetsTable);

		scanAssetsTable.getColumnModel().getColumn(0).setMaxWidth(0);
		scanAssetsTable.getColumnModel().getColumn(0).setMinWidth(0);
		scanAssetsTable.getColumnModel().getColumn(0).setPreferredWidth(0);

		scanService.getScan().addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {

			}
		});
	}

	@SuppressWarnings("serial")
	private class LinksCollectionTableModel extends DefaultTableModel {

		private String[] columnNames = { "Url", "Extern", "Type", "Size", "Active" };
		private ArrayList<Link> list;

		public LinksCollectionTableModel(ArrayList<Link> list) {
			this.list = list;
			scanService.getScan().getCrawlLinks().addObserver(new Observer() {
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
			Link link = list.get(row);
			switch (col) {
			case 0:
				return link.getUrl();
			case 1:
				return link.isExtern() ? "Yes" : "No";
			case 2:
				return link.isText() ? "Text" : link.isImage() ? "Image" : "Binary";
			case 3:
				return link.getSize();
			case 4:
				return link.isActive() ? "Yes" : "No";
			default:
				return "unknow";
			}
		}

		public Class<? extends Object> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}

	@SuppressWarnings("serial")
	private class ScanAssetsTableModel extends DefaultTableModel {

		private String[] columnNames = { "Url", "Type", "Url", "Size", "Status" };
		private ArrayList<ScanAsset> list;

		public ScanAssetsTableModel(ArrayList<ScanAsset> list) {
			this.list = list;
			scanService.getScan().getAssets().addObserver(new Observer() {
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
			ScanAsset asset = list.get(row);
			switch (col) {
			case 0:
				return asset.getId();
			case 1:
				return asset.getType().getName() + "(" + asset.getType().getMime() + ")";
			case 2:
				return asset.getUrl();
			case 3:
				return asset.getSize();
			case 4:
				switch (asset.getStatus()) {
				case 1:
					return "Active";
				case 0:
					return "Deleted";
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
