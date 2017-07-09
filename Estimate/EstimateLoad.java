package Estimate;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DateFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import Demand.Demand;
import FrameComponent.FrameLabel;
import FrameComponent.ViewManager;
import Main.CsvPasser;
import Main.Main;
import Product.ProductView;
import Supply.Supply;

public class EstimateLoad extends JFrame {
	JTable table;
	JPanel panel;

	ViewManager viewManager;
	FrameLabel frameLabel;
	JLabel page;
	EstimateList estimateList;
	Object row[][];

	JLabel searchLabel;

	JTextField searchCompField;
	JLabel searchCompLabel;
	JTextField searchItemField;
	JLabel searchItemLabel;
	LoadAction loadAction;
	JButton searchButton;

	UtilDateModel model = new UtilDateModel();
	JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
	JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateFormatter());
	UtilDateModel model2 = new UtilDateModel();
	JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, new Properties());
	JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateFormatter());

	public EstimateLoad(ViewManager viewManager,FrameLabel frameLabel) {
		this.viewManager = viewManager;
		this.frameLabel =frameLabel;
		estimateList = new EstimateList();
		loadAction = new LoadAction(estimateList, this);
		panel = new JPanel();
		setBounds(200 + 838, 200, 350, 700);
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		searchInit();
		buttonInit();
		add(panel);
	}

	public void setVisible(boolean b) {
		if (b) {
			loadList();
			tableInit();
		}
		super.setVisible(b);
	}

	void tableInit() {
		tableSet(estimateList.getMatchCount());
		for (int i = 0; i < estimateList.matchCount; i++) {
			table.setValueAt(estimateList.getMatch(i).date, i, 0);
			table.setValueAt(estimateList.getMatch(i).name, i, 1);
			table.setValueAt(estimateList.getMatch(i).no, i, 2);
		}
	}

	private void tableSet(int n) {
		row = new Object[n][3];
		Object column[] = { "견적일", "상호", "No." };

		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumn("견적일").setPreferredWidth(80);
		table.getColumn("상호").setPreferredWidth(180);
		table.getColumn("No.").setPreferredWidth(1);
		table.setRowHeight(18);
		table.getTableHeader().setFont(new Font(Main.font, 0, 12));

		table.setFocusable(false);
		table.setAutoCreateRowSorter(true);
		table.setRowSorter(new TableRowSorter<TableModel>(table.getModel()));
		table.addMouseListener(new MouseAdapter() {
			int index = -1;

			public void mouseReleased(MouseEvent arg0) {
				if (index == table.getSelectedRow()) {
					if (index == -1)
						return;
					loadFile(new String(
							"견적서_" + estimateList.getMatch(index).name + "_" + estimateList.getMatch(index).date
									+ "_" + estimateList.getMatch(index).no + ".csv"));
					index = -1;
				}
				index = table.getSelectedRow();
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(330, 500));
		panel.setBounds(3, 100, 330, 800);
		panel.removeAll();
		panel.add(scroll);
	}

	private void searchInit() {
		int x = 140;
		int y = 15;
		datePicker.setBounds(3, 25, 133, 30);
		datePicker2.setBounds(3, 70, 133, 30);
		add(datePicker);
		add(datePicker2);
		searchLabel = new JLabel("기간");
		searchLabel.setBounds(40, 0, 40, 30);
		add(searchLabel);
		searchButton = new JButton("reset");
		searchButton.setFont(new Font(Main.font, 0, 10));
		searchButton.setBounds(90, 5, 30, 18);
		add(searchButton);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setValue(null);
				model2.setValue(null);
				estimateList.setDate(model.getValue(), model2.getValue());
				tableInit();
			}
		});

		searchLabel = new JLabel("~");
		searchLabel.setBounds(50, 50, 20, 20);
		add(searchLabel);
		datePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estimateList.setDate(model.getValue(), model2.getValue());
				tableInit();
			}
		});
		datePicker2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estimateList.setDate(model.getValue(), model2.getValue());
				tableInit();
			}
		});
		searchCompField = new JTextField(14);
		searchItemField = new JTextField(14);

		searchCompLabel = new JLabel("상호");
		searchItemLabel = new JLabel("물품");

		searchButton = new JButton("검색");
		searchButton.setFont(new Font(Main.font, 0, 10));

		loadAction.searchCompField = searchCompField;
		loadAction.searchItemField = searchItemField;

		searchCompField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loadAction.search();
				}
			}
		});
		searchItemField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loadAction.search();
				}
			}
		});
		searchButton.addActionListener(loadAction);

		searchCompLabel.setBounds(x + 8, y + 5, 30, 20);
		searchItemLabel.setBounds(x + 8, y + 25, 30, 20);
		searchCompField.setBounds(x + 40, y + 5, 150, 20);
		searchItemField.setBounds(x + 40, y + 25, 150, 20);

		searchButton.setBounds(x + 129, y + 50, 60, 25);
		add(searchLabel);
		add(searchCompLabel);
		add(searchItemLabel);
		add(searchCompField);
		add(searchItemField);
		add(searchButton);
	}

	private void buttonInit() {
		Button button[] = new Button[2];
		button[0] = new Button("제거");
		button[0].setBounds(115, 610, 100, 40);
		button[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (index == -1)
					return;
				removeList(new String("견적서_" + estimateList.loadDataMatch[index].name + "_"
						+ estimateList.loadDataMatch[index].date + "_" + estimateList.loadDataMatch[index].no + ".save"));
			}
		});
		button[1] = new Button("불러오기");
		button[1].setBounds(225, 610, 100, 40);
		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (index == -1)
					return;
				loadFile(new String("견적서_" + estimateList.loadDataMatch[index].name + "_"
						+ estimateList.loadDataMatch[index].date + "_" + estimateList.loadDataMatch[index].no + ".csv"));
				index=-1;
			}
		});
		add(button[0]);
		add(button[1]);
	}

	private void loadList() {
		File dirFile = new File("save/");
		File[] fileList = dirFile.listFiles();
		estimateList = new EstimateList();
		loadAction.estimateList = estimateList;
		for (File tempFile : fileList) {
			if (tempFile.isFile()) {
				String tmpName = tempFile.getName();
				estimateList.addList(tmpName);
			}
		}
	}

	public String saveFile(String fileName) {
		Estimate est = viewManager.getEstimate();
		Demand demand = viewManager.getEstimate().getDemand();
		String stn[];
		
		File a = new File("save");
		if (a.exists() == false) {
			a.mkdirs();
		}
		int number = 0;
		if (fileName.equals("New Document")) {
			while (true) {
				fileName = new String(demand.getName() + "_" + demand.getDate() + "_" + number);
				a = new File(fileName);
				if (a.exists() == true) {
					number++;
					continue;
				}
				break;
			}
		}
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("save\\견적서_" + fileName + ".csv"));
			fw.write("v1.0");
			fw.write("\r\n");
			stn=demand.getStrings();
			for(int j=0;j<4;j++){
				fw.write(Main.checkString(stn[j])+",");
			}
			fw.write("\r\n");
			for (int i = 0; i < 8; i++) {
				fw.write(est.getTableWidth()[i] + ",");
			}
			fw.write("\r\n");
			for (int i = 0; i < est.getProductList().getMaxSize()-1; i++) {
				stn=est.getProductList().getProduct(i).getStrings();
				for(int j=0;j<6;j++){
					fw.write(Main.checkString(stn[j])+",");
				}
				fw.write("\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		Main.modify = false;
		return fileName;
	}

	private void loadFile(String file) {
		Estimate est = new Estimate();
		BufferedReader fr = null;
		String st;
		try {
			fr = new BufferedReader(new FileReader("save/" + file));
			if (fr.readLine().equals("v1.0")) {
				String stn[] = CsvPasser.csvSplit(fr.readLine());
				for(int i=0;i<4;i++)
					if (stn[i].equals("-"))
						stn[i]="";
				est.setDemand(new Demand(stn));
				stn = CsvPasser.csvSplit(fr.readLine());
				int intArr[]=new int[8];
				for(int i=0;i<8;i++){
					if (stn[i].equals("-"))
						stn[i]="";
					intArr[i] = Integer.parseInt(stn[i]);
				}
				est.setTableWidth(intArr);
				while (null != (st = fr.readLine())) {
					stn = CsvPasser.csvSplit(st);
					for(int i=0;i<6;i++)
						if (stn[i].equals("-"))
							stn[i]="";
					est.getProductList().addProduct(stn);
				}
			}
			fr.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		viewManager.setEstimate(est);
		
		frameLabel.setFileName(file.replace(".csv", ""));
		frameLabel.setPageText(new String("page" + viewManager.getProductView().getCurrPage() + "/"
				+ viewManager.getProductView().getMaxPage()));
		viewManager.swapPanel("front");
	}

	private void removeList(String str) {
		File f = new File("save\\" + str);
		f.delete();
		loadList();
		tableInit();
	}


}

class LoadAction implements ActionListener {
	EstimateList estimateList;
	EstimateLoad estimateLoad;
	JTextField searchCompField;
	JTextField searchItemField;

	LoadAction(EstimateList estimateList, EstimateLoad estimateLoad) {
		this.estimateList = estimateList;
		this.estimateLoad = estimateLoad;
	}

	public void actionPerformed(ActionEvent e) {
		search();
	}

	public void search() {
		estimateList.setMatchStr(searchCompField.getText(), searchItemField.getText());
		estimateLoad.tableInit();
	}
}

