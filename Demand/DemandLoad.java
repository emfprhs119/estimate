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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import FrameComponent.ViewManager;
import Main.CsvPasser;
import Main.Main;

public class DemandLoad extends JFrame {
	ViewManager viewManager;
	JTable table;
	JPanel panel;
	JTextField searchField;
	JLabel searchLabel;
	JButton searchButton;
	DemandAdd demandAdd;
	DemandList demandList;

	public DemandLoad(ViewManager viewManager) {
		this.viewManager = viewManager;
		panel = new JPanel();
		demandList = DemandLoad.loadList();
		demandAdd = new DemandAdd(this);
		searchField = new JTextField(14);
		searchLabel = new JLabel("상호");
		searchButton = new JButton("검색");
		searchButton.setFont(new Font(Main.font, 0, 10));
		setBounds(200, 300, 380, 600);
		setLayout(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		buttonInit();
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

	void tableUpdate() {
		demandList.setList(loadList());
		demandList.setMatchStr(searchField.getText());
		if (demandList.getMatchCount() == 0)
			return;
		tableSet(demandList.getMatchCount());
		for (int i = 0; i < demandList.getMatchCount(); i++) {
			table.setValueAt(demandList.getMatch(i).getName(), i, 0);
			table.setValueAt(demandList.getMatch(i).getTel(), i, 2);
			table.setValueAt(demandList.getMatch(i).getWho(), i, 1);
		}
	}

	public void buttonInit() {
		Button button[] = new Button[3];
		button[0] = new Button("추가");
		button[0].setBounds(75, 495, 80, 40);
		button[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				demandAdd.setVisible(true);
			}
		});

		button[1] = new Button("제거");
		button[1].setBounds(170, 495, 80, 40);
		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				removeDemand(sel);// 제거
			}
		});
		button[2] = new Button("불러오기");
		button[2].setBounds(265, 495, 80, 40);
		button[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				viewManager.setDemand(demandList.getMatch(sel));
				setVisible(false);
				sel = -1;
			}
		});
		add(button[0]);
		add(button[1]);
		add(button[2]);
	}

	public void tableSet(int n) {
		Object row = new Object[n][3];
		Object column[] = { "상호", "담당자", "전화번호" };
		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumn("상호").setPreferredWidth(90);
		table.getColumn("담당자").setPreferredWidth(30);
		table.getColumn("전화번호").setPreferredWidth(40);

		table.setRowHeight(18);
		table.getTableHeader().setFont(new Font(Main.font, 0, 12));

		table.setAutoCreateRowSorter(true);
		table.setRowSorter(new TableRowSorter<TableModel>(table.getModel()));
		table.addMouseListener(new MouseAdapter() {
			int sel = -1;

			public void mouseReleased(MouseEvent arg0) {
				if (sel == table.getSelectedRow()) {
					if (sel == -1)
						return;
					viewManager.setDemand(demandList.getMatch(sel));
					setVisible(false);
					sel = -1;
				}
				sel = table.getSelectedRow();
			}
		});
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(350, 440));
		panel.setBounds(7, 40, 350, 440);
		panel.removeAll();
		panel.add(scroll);
		setVisible(true);
	}

	// 수요자 목록 저장
	static public void saveList(DemandList list) {
		String fileName;
		fileName = new String("demandList.csv");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			fw.write("v1.0");
			fw.write("\r\n");
			for (int i = 0; i < list.getCount(); i++) {
				String stn[]=list.getDemand(i).getStrings();
				for(int j=0;j<4;j++){
					fw.write(Main.checkString(stn[j])+",");
				}
				fw.write("\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	// 수요자 목록 로드
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
			if (fr.readLine().equals("v1.0")) {

				while (null != (st = fr.readLine())) {
					stn = CsvPasser.csvSplit(st);
					for (int i = 0; i < 6; i++)
						if (stn[i].equals("-"))
							stn[i] = "";
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
