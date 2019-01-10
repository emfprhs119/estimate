package Demand;

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import FrameComponent.ViewManager;
import Main.CsvPasser;
import Main.Main;
// �ŷ�ó �ҷ�����
public class DemandLoad extends JFrame {
	ViewManager viewManager;
	JTable table;
	JPanel panel;
	JTextField searchField;
	JLabel searchLabel;
	Button searchButton;
	DemandAdd demandAdd;
	DemandList demandList;

	public DemandLoad(ViewManager viewManager) {
		super("�ŷ�ó �ҷ�����");
		this.viewManager = viewManager;
		panel = new JPanel();
		demandAdd = new DemandAdd(this);
		demandList = new DemandList();
		searchField = new JTextField(14);
		searchLabel = new JLabel("��ȣ");
		searchButton = new Button("�˻�");
		searchButton.setFont(new Font(Main.font, 0, 10));
		setBounds(200, 300, 380, 600);
		setLayout(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		buttonInit();
		tableInit();
		searchField.addKeyListener(new KeyAdapter() {
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
		searchLabel.setBounds(8, 10, 30, 20);
		searchField.setBounds(40, 10, 150, 20);
		searchButton.setBounds(195, 10, 55, 20);
		add(searchLabel);
		add(searchField);
		add(searchButton);
		add(panel);
	}

	public void buttonInit() {
		Button button[] = new Button[3];
		button[0] = new Button("�߰�");
		button[0].setBounds(75, 495, 80, 40);
		button[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				demandAdd.setVisible(true);
			}
		});

		button[1] = new Button("����");
		button[1].setBounds(170, 495, 80, 40);
		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				removeDemand(sel);// ����
			}
		});
		button[2] = new Button("�ҷ�����");
		button[2].setBounds(265, 495, 80, 40);
		button[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if (table == null) {
					JOptionPane.showMessageDialog(null, "�ҷ��� �� �����ϴ�.");
					return;
				}
				if (index == -1){
					JOptionPane.showMessageDialog(null, "�ŷ�ó�� ������ �ּ���.");
					return;
				}
				viewManager.setDemand(demandList.getMatch(index));
				setVisible(false);
			}
		});
		add(button[0]);
		add(button[1]);
		add(button[2]);
	}

	void tableInit() {
		Object row = new Object[0][3];
		Object column[] = { "��ȣ", "�����", "��ȭ��ȣ" };
		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumn("��ȣ").setPreferredWidth(90);
		table.getColumn("�����").setPreferredWidth(30);
		table.getColumn("��ȭ��ȣ").setPreferredWidth(40);

		table.setRowHeight(18);
		table.getTableHeader().setFont(new Font(Main.font, 0, 12));

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
						JOptionPane.showMessageDialog(null, "�ŷ�ó�� ������ �ּ���.");
						return;
					}
					viewManager.setDemand(demandList.getMatch(index));
					setVisible(false);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(350, 440));
		panel.setBounds(7, 40, 350, 440);
		panel.add(scroll);
	}

	public void setVisible(boolean b) {
		if (b)
			tableUpdate();
		super.setVisible(b);
	}

	void tableUpdate() {
		demandList.setList(loadList());
		demandList.setMatchStr(searchField.getText());
		if (demandList.getMatchCount() == 0)
			return;

		tableSet(demandList.getMatchCount());
		demandList.matchSort();
		for (int i = 0; i < demandList.getMatchCount(); i++) {
			table.setValueAt(demandList.getMatch(i).getName(), i, 0);
			table.setValueAt(demandList.getMatch(i).getTel(), i, 2);
			table.setValueAt(demandList.getMatch(i).getWho(), i, 1);
		}
	}

	public void tableSet(int n) {
		Object row = new Object[n][3];
		Object column[] = { "��ȣ", "�����", "��ȭ��ȣ" };
		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table.setModel(model);
	}

	// ������ ��� �ε�
	static public DemandList loadList() {
		String stn[];
		String st;
		DemandList demandList = new DemandList();
		try {
			File f = new File("demandList.csv");
			if (f.exists() == false) {
				return demandList;
			}
			BufferedReader fr = new BufferedReader(new FileReader("demandList.csv"));

			if (fr.readLine().replaceAll(",","").equals("v1.0")) {
				fr.readLine(); //��ȣ,��ȭ��ȣ,����� ���̺�
				while (null != (st = fr.readLine())) {
					stn = CsvPasser.csvSplit(st);
					for (int i = 0; i < 3; i++) {
						if (stn[i].equals("-"))
							stn[i] = "";
					}
					demandList.addList(new Demand(stn));
				}
			}
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return demandList;
	}

	// ������ ��� ����
	static public void saveList(DemandList list) {
		String fileName;
		fileName = new String("demandList.csv");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			fw.write("v1.0");
			fw.write("\r\n");
			fw.write("��ȣ,��ȭ��ȣ,�����");
			fw.write("\r\n");
			for (int i = 0; i < list.getCount(); i++) {
				String stn[] = list.getDemand(i).getStrings();
				for (int j = 1; j < 4; j++) {
					fw.write(Main.checkString(stn[j]) + ",");
				}
				fw.write("\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	public boolean addDemand(Demand demand) {
		DemandList list = DemandLoad.loadList();
		if (!list.addList(demand))
			return false;
		DemandLoad.saveList(list);
		tableUpdate();
		return true;
	}

	public void removeDemand(int sel) {
		DemandList list = DemandLoad.loadList();
		list.removeList(sel);
		DemandLoad.saveList(list);
		tableUpdate();
	}
}
