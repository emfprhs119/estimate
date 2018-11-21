package Estimate;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import Demand.Demand;
import FrameComponent.FrameLabel;
import FrameComponent.ViewManager;
import Main.CsvPasser;
import Main.Main;
//������ �ҷ�����
public class EstimateLoad extends JFrame {
	JTable table;
	JPanel panel;

	ViewManager viewManager;
	FrameLabel frameLabel;
	JLabel page;
	EstimateList estimateList;
	Object row[][];
	Object column[];
	
	JLabel searchLabel;

	JTextField searchCompField;
	JLabel searchCompLabel;
	JTextField searchItemField;
	JLabel searchItemLabel;
	Button searchButton;

	UtilDateModel model = new UtilDateModel();
	JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
	JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateFormatter());
	UtilDateModel model2 = new UtilDateModel();
	JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, new Properties());
	JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateFormatter());

	public EstimateLoad(ViewManager viewManager, FrameLabel frameLabel) {
		super("������ �ҷ�����");
		this.viewManager = viewManager;
		this.frameLabel = frameLabel;
		estimateList = new EstimateList();
		panel = new JPanel();
		setBounds(200 + 838, 200, 350, 700);
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setResizable(false);
		//searchInit();
		buttonInit();
		tableInit();
		add(panel);
	}

	private void buttonInit() {
		Button button[] = new Button[2];
		button[0] = new Button("����");
		button[0].setBounds(115, 610, 100, 40);
		button[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (table == null) {
					JOptionPane.showMessageDialog(null, "�ҷ��� �� �����ϴ�.");
					return;
				}
				if (index == -1){
					JOptionPane.showMessageDialog(null, "�������� ������ �ּ���.");
					return;
				}
				removeFile(estimateList.getLoadData(index).getPath());
			}

		});
		button[1] = new Button("�ҷ�����");
		button[1].setBounds(225, 610, 100, 40);
		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (table == null) {
					JOptionPane.showMessageDialog(null, "�ҷ��� �� �����ϴ�.");
					return;
				}
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "�������� ������ �ּ���.");
					return;
				}
				loadFile(estimateList.getLoadData(index).getPath());
			}
		});
		add(button[0]);
		add(button[1]);
	}
/*
	private void searchInit() {
		int x = 140;
		int y = 15;
		datePicker.setBounds(3, 25, 133, 30);
		datePicker2.setBounds(3, 70, 133, 30);
		add(datePicker);
		add(datePicker2);
		searchLabel = new JLabel("�Ⱓ");
		searchLabel.setBounds(40, 0, 40, 30);
		add(searchLabel);
		searchButton = new Button("reset");
		searchButton.setFont(new Font(Main.font, 0, 10));
		searchButton.setBounds(90, 5, 30, 18);
		add(searchButton);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setValue(null);
				model2.setValue(null);
				estimateList.setDate(model.getValue(), model2.getValue());
				tableUpdate();
			}
		});

		searchLabel = new JLabel("~");
		searchLabel.setBounds(50, 50, 20, 20);
		add(searchLabel);
		datePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estimateList.setDate(model.getValue(), model2.getValue());
				tableUpdate();
			}
		});
		datePicker2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estimateList.setDate(model.getValue(), model2.getValue());
				tableUpdate();
			}
		});
		searchCompField = new JTextField(14);
		searchItemField = new JTextField(14);

		searchCompLabel = new JLabel("��ȣ");
		searchItemLabel = new JLabel("��ǰ");

		searchButton = new Button("�˻�");
		searchButton.setFont(new Font(Main.font, 0, 10));

		searchCompField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tableUpdate();
				}
			}
		});
		searchItemField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tableUpdate();
				}
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableUpdate();
			}

		});

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
*/
	void tableInit() {
		row = new Object[0][3];
		column = new Object[]{ "������", "��ȣ", "No." };

		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumn("������").setPreferredWidth(80);
		table.getColumn("��ȣ").setPreferredWidth(180);
		table.getColumn("No.").setPreferredWidth(1);
		table.setRowHeight(18);
		table.getTableHeader().setFont(new Font(Main.font, 0, 12));

		table.setFocusable(false);
		table.setAutoCreateRowSorter(true);
		table.getRowSorter().toggleSortOrder(0);
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			int colum;
			boolean decreasingFlag=false;
			
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (colum==table.columnAtPoint(e.getPoint()))
		        	decreasingFlag=!decreasingFlag;
		        else{
		        	colum=table.columnAtPoint(e.getPoint());
		        	decreasingFlag=true;
		        }
		        estimateList.arrSort(colum,decreasingFlag);
		    }
		});
		
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				int index = table.getSelectedRow();
				if (event.getClickCount() == 2) {
					if (table == null) {
						JOptionPane.showMessageDialog(null, "�ҷ��� �� �����ϴ�.");
						return;
					}
					if (index == -1){
						JOptionPane.showMessageDialog(null, "�������� ������ �ּ���.");
						return;
					}
					loadFile(estimateList.getLoadData(index).getPath());
				}
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(330, 570));
		panel.setBounds(3, 3, 330, 570);
		panel.add(scroll);
	}

	public void setVisible(boolean b) {
		if (b) {
			loadList();
			tableUpdate();
		}
		super.setVisible(b);
	}

	public void tableUpdate() {
		estimateList.setList(loadList());
		//estimateList.setMatchStr(searchCompField.getText(), searchItemField.getText());
		if (estimateList.getCount() == 0)
			return;
		tableSet(estimateList.getCount());
		for (int i = 0; i < estimateList.getCount(); i++) {
			table.setValueAt(estimateList.getLoadData(i).getDate(), i, 0);
			table.setValueAt(estimateList.getLoadData(i).getName(), i, 1);
			table.setValueAt(estimateList.getLoadData(i).getNo(), i, 2);
		}
	}

	private void tableSet(int n) {
		row = new Object[n][3];

		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table.setModel(model);
	}

	private EstimateList loadList() {
		String stn[];
		EstimateList estimateList;

		File dirFile = new File("save/");
		File[] fileList = dirFile.listFiles();
		estimateList = new EstimateList();
		if (fileList==null)
			return estimateList;
		for (File tempFile : fileList) {
			if (tempFile.isFile()) {
				String tmpName = tempFile.getName();

				stn = tmpName.split("_");
				if (stn.length != 4 || !stn[0].equals("������"))
					continue;
				String name = stn[1];
				String date = stn[2];
				String no = stn[3].split("\\.")[0];
				String ext = stn[3].split("\\.")[1];
				
				estimateList.addList(new LoadData(date, name, no,ext));
			}
		}
		return estimateList;
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
		if (fileName.equals("New Document") || !fileName.matches(demand.getName() + "_" + demand.getDate() + ".+")) {
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
			System.out.println("save\\" + "������_"+fileName);
			removeFile("save\\" +"������_"+ fileName);
			fileName="������_"+fileName.replace(".save", "");
			BufferedWriter fw = new BufferedWriter(new FileWriter("save\\" + fileName + ".csv"));
			fw.write("v1.0");
			fw.write("\r\n");
			stn = demand.getStrings();
			for (int j = 0; j < 4; j++) {
				fw.write(Main.checkString(stn[j]) + ",");
			}
			fw.write("\r\n");
			for (int i = 0; i < 8; i++) {
				fw.write(est.getTableWidth()[i] + ",");
			}
			fw.write("\r\n");
			for (int i = 0; i < est.getProductList().getMaxSize(); i++) {
				stn = est.getProductList().getProduct(i).getStrings();
				for (int j = 0; j < 6; j++) {
					fw.write(Main.checkString(stn[j]) + ",");
				}
				fw.write("\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			JOptionPane.showMessageDialog(null, "csv ������ �ݰ� �ٽ� �õ��ϼ���.");
			e3.printStackTrace();
		}
		tableUpdate();
		Main.modify = false;
		return fileName;
	}

	private void loadFile(String fileName) {
		Estimate est = new Estimate();
		BufferedReader fr = null;
		String st;
		try {
			fr = new BufferedReader(new FileReader(fileName));
			st = fr.readLine();
			if (st.equals("v1.0")) {
				String stn[] = CsvPasser.csvSplit(fr.readLine());
				for (int i = 0; i < 4; i++)
					if (stn[i].equals("-"))
						stn[i] = "";
				est.setDemand(new Demand(stn));
				stn = CsvPasser.csvSplit(fr.readLine());
				int intArr[] = new int[8];
				for (int i = 0; i < 8; i++) {
					if (stn[i].equals("-"))
						stn[i] = "";
					intArr[i] = Integer.parseInt(stn[i]);
				}
				est.setTableWidth(intArr);
				while (null != (st = fr.readLine())) {
					stn = CsvPasser.csvSplit(st);
					for (int i = 0; i < 6; i++)
						if (stn[i].equals("-"))
							stn[i] = "";
					
					boolean flag = false;
					for (int i = 0; i < 6; i++) {
						if (!stn[i].equals("")) {
							flag=true;
							break;
						}
					}
					if (flag)
						est.getProductList().addProduct(stn);
				}
			}else {
				String stl[] = st.split("/");
				String stn[] = new String[4];
				for (int i = 0; i < 4; i++) {
					stn[i] = stl[i];
					stn[i] = stn[i].replaceAll(" ","");
				}
				est.setDemand(new Demand(stn));
				if (stl.length>6) {
					int intArr[] = new int[8];
					for (int i = 0; i < 8; i++) {
						intArr[i] = Integer.parseInt(stl[i+5]);
					}
					est.setTableWidth(intArr);
				}else {
					est.setTableWidth(Main.tableSize);
				}
				while (null != (st = fr.readLine())) {
					stn = st.split("/");
					for (int i = 0; i < 6; i++)
						stn[i] = stn[i].replaceAll(" ","");
					boolean flag = false;
					
					for (int i = 0; i < 6; i++) {
						if (!stn[i].equals("")) {
							flag=true;
							break;
						}
					}
					
					if (flag)
						est.getProductList().addProduct(stn);
				}
			}
			fr.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		viewManager.setEstimate(est);

		frameLabel.setFileName(fileName);
		frameLabel.setPageText(new String(
				"page" + viewManager.getProductView().getCurrPage() + "/" + viewManager.getProductView().getMaxPage()));
		viewManager.swapPanel("front");
	}


	private void removeFile(String path) {
		File f = new File(path);
		f.delete();
		tableUpdate();
	}
}