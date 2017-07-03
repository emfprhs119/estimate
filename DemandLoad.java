
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

public class DemandLoad extends JFrame {
	JTable table;
	JPanel panel;
	JTextField searchField;
	JLabel searchLabel;
	JButton searchButton;
	DemandAdd demandAdd;
	DemandList demandList;
	Estimate est;

	DemandLoad() {
		
		panel = new JPanel();
		demandList = new DemandList();
		demandAdd=new DemandAdd(demandList);
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
					demandList.setMatchStr(searchField.getText());
					tableInit();
				}
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demandList.setMatchStr(searchField.getText());
				tableInit();
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

	void tableInit() {
		if (demandList.getMatchCount() == 0)
			return;
		tableSet(demandList.getMatchCount());
		for (int i = 0; i < demandList.mattop; i++) {
			table.setValueAt(demandList.demandMatch[i].name, i, 0);
			table.setValueAt(demandList.demandMatch[i].tel, i, 2);
			table.setValueAt(demandList.demandMatch[i].who, i, 1);
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
				remove(sel);
				loadList();
				tableInit();
				// 제거
			}
		});
		button[2] = new Button("불러오기");
		button[2].setBounds(265, 495, 80, 40);
		button[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				est.setDemand(demandList.demandMatch[sel]);
				setVisible(false);
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
					est.setDemand(demandList.demandMatch[sel]);
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
		/*
		 * frame.repaint(); panel.repaint();
		 */
	}
	//수요자 목록 저장
	static public void saveList(DemandList list) {
		String name, tel, who;
		String fileName;
		fileName = new String("demandList");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < list.top; i++) {
					name = list.demands[i].name;
					tel = list.demands[i].tel;
					who = list.demands[i].who;
					fw.write(name + "/" + tel + "/" + who + "/\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}
	//수요자 목록 로드
	static public DemandList loadList() {
		BufferedReader fr;
		File f;
		String st, name, tel, who, stn[];
		DemandList demandList = new DemandList();
		try {
			f = new File("demandList");
			if (f.exists() == false) {
				return demandList;
			}
			fr = new BufferedReader(new FileReader("demandList.list"));
			while (null != (st = fr.readLine())) {
				stn = st.split("/");
				if (stn.length == 0) {
					break;
				}
				name = stn[0].replaceAll(" ", "");
				tel = stn[1].replaceAll(" ", "");
				who = stn[2].replaceAll(" ", "");
				demandList.addList(new Demand(name,tel,who));
			}
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return demandList;
	}

	public void remove(int sel) {//
		demandList.removeMat(sel);
	}
}
