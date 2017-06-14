import java.awt.Button;
import java.awt.Color;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

class DemandF {

	String date;
	String name;
	String tel;
	String who;

	public DemandF() {
	}

	public DemandF(String name, String tel, String who) {
		this.date = null;
		this.name = name;
		this.tel = tel;
		this.who = who;
	}

	public boolean equals(Object obj) {
		if (name.equals(((DemandF) obj).name) && tel.equals(((DemandF) obj).tel) && who.equals(((DemandF) obj).who))
			return true;
		else
			return false;
	}
}

class AddDemandFrame extends JFrame {
	private WhitePanel pane0;
	private WhitePanel pane00;
	private JPanel[] menu;
	private JPanel[] m;
	private JTextField[] t;
	private JButton button;
	private int i;

	AddDemandFrame(final Func func) {
		setLayout(null);
		setBounds(500, 500, 235, 170);
		pane0 = new WhitePanel();
		pane00 = new WhitePanel();
		pane0.setLayout(null);
		pane0.setBounds(65, 0, 180, 130);
		pane00.setBounds(0, 0, 65, 130);

		menu = new JPanel[3];
		m = new JPanel[3];
		t = new JTextField[3];
		for (int i = 0; i < 3; i++) {
			menu[i] = new JPanel();
			m[i] = new JPanel();
		}
		JLabel jl[] = new JLabel[3];
		m[0].add(jl[0] = new JLabel("상      호 :"));
		m[1].add(jl[1] = new JLabel("전화번호:"));
		m[2].add(jl[2] = new JLabel("담 당 자 :"));

		menu[0].add(t[0] = new JTextField(11));
		menu[1].add(t[1] = new JTextField(11));
		menu[2].add(t[2] = new JTextField(11));

		for (i = 0; i < 3; i++) {
			menu[i].setBounds(0, i * 30, 150, 35);
			m[i].setBounds(-10, i * 30, 90, 35);
			menu[i].setBackground(Color.WHITE);
			m[i].setBackground(Color.WHITE);
			jl[i].setFont(new Font(Main.font, Font.BOLD, 13));
			jl[i].setHorizontalAlignment(JLabel.RIGHT);
			t[i].setFont(new Font(Main.font, Font.BOLD, 13));
			t[i].setHorizontalAlignment(JTextField.LEFT);
			t[i].addKeyListener(new KeyAdapter() {
				int key = i;

				public void keyPressed(KeyEvent arg0) {
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						if (key != 2)
							t[key + 1].requestFocus();
						else {
							DemandF demand = getDemand();
							func.addDemand(demand);
							func.loadDemand();
							removeField();
						}
					}
				}
			});
			pane00.add(m[i]);
			pane0.add(menu[2 - i]);
		}
		button = new JButton("추가");
		button.setFont(new Font(Main.font, 0, 12)); //
		button.setBounds(80, 100, 60, 20);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DemandF demand = getDemand();
				func.addDemand(demand);
				func.loadDemand();
				removeField();
			}
		});
		add(button);// 저장 add(button);//닫기
		button = new JButton("닫기");
		button.setFont(new Font(Main.font, 0, 12)); //
		button.setBounds(150, 100, 60, 20);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		add(button);
		add(pane00);
		add(pane0);
	}

	public DemandF getDemand() {
		DemandF demand = new DemandF();
		demand.name = t[0].getText();
		demand.tel = t[1].getText();
		demand.who = t[2].getText();
		return demand;
	}

	private void removeField() {
		for (i = 0; i < 3; i++) {
			t[i].setText("");
		}
		t[0].requestFocus();
	}
}

public class Demand extends WhitePanel {
	WhitePanel pane0, pane00;
	JPanel menu[];
	JPanel m[];
	JTextField t[];

	JTable table;
	JFrame frame;
	JPanel panel;
	JTextField searchField;
	JLabel searchLabel;
	JButton searchButton;
	Object row[][];
	ManageLi manageLi;

	UtilDateModel model = new UtilDateModel();
	JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
	JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateFormatter());
	

	JButton demandLoadButton;
	Func func;
	AddDemandFrame addFrame;
	int i;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public Demand() {
		manageLi = new ManageLi();
		frame = new JFrame();
		panel = new JPanel();
		searchField = new JTextField(14);
		searchLabel = new JLabel("상호");
		searchButton = new JButton("검색");
		searchButton.setFont(new Font(Main.font, 0, 10));
		frame.setBounds(200, 300, 380, 600);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);
		buttonInit();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					manageLi.setMatchStr(searchField.getText());
					tableInit();
				}
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageLi.setMatchStr(searchField.getText());
				tableInit();
			}
		});
		searchLabel.setBounds(8, 10, 30, 20);
		searchField.setBounds(40, 10, 150, 20);
		searchButton.setBounds(195, 10, 55, 20);
		frame.add(searchLabel);
		frame.add(searchField);
		frame.add(searchButton);
		frame.add(panel);
		pane0 = new WhitePanel();
		pane00 = new WhitePanel();
		pane0.setBounds(45, 4, 350, 120);
		pane00.setBounds(15, 0, 115, 120);

		menu = new JPanel[5];
		m = new JPanel[5];
		t = new JTextField[5];
		for (int i = 0; i < 5; i++) {
			menu[i] = new JPanel();
			m[i] = new JPanel();
		}
		JLabel jl[] = new JLabel[5];
		m[0].add(jl[0] = new JLabel("견 적 일 :"));
		m[1].add(jl[1] = new JLabel("상      호 :"));
		m[2].add(jl[2] = new JLabel("전화번호:"));
		m[3].add(jl[3] = new JLabel("담 당 자 :"));

		menu[0].add(datePicker);
		// menu[0].add(t[0] = new JTextField(9));
		menu[1].add(t[1] = new JTextField(14));
		menu[2].add(t[2] = new JTextField(14));
		menu[3].add(t[3] = new JTextField(14));
		/*
		 * Date dt = new Date(); SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 */
		demandLoadButton = new JButton(new ImageIcon("forder.png"));
		demandLoadButton.setBounds(181,1,28,25);
		demandLoadButton.setPreferredSize(new Dimension(28, 25));
		
		
		demandLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				func.loadDemand();
			}
		});
		menu[1].add(demandLoadButton);
		model.setSelected(true);
		// System.out.println(sdf.format(dt).toString());

		for (i = 0; i < 4; i++) {
			
			menu[i].setBounds(80, i*30+5, 210, 32);
			//menu[i].setBounds(30, i * 30, 290, 35);
			m[i].setBounds(10, i * 30, 105, 35);
			menu[i].setBackground(Color.WHITE);
			m[i].setBackground(Color.WHITE);
			jl[i].setFont(new Font(Main.font, Font.BOLD, 22));
			jl[i].setHorizontalAlignment(JLabel.RIGHT);
			if (i != 0) {
				menu[i].setLayout(null);
				t[i].setBounds(7, 1, 174, 25);
				t[i].setFont(new Font(Main.font, Font.BOLD, 15));
				t[i].setHorizontalAlignment(JTextField.LEFT);

				t[i].addKeyListener(new KeyAdapter() {
					int key = i;

					public void keyPressed(KeyEvent arg0) {

						if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
							if (key != 3)
								t[key + 1].requestFocus();
						} else
							Main.modify = true;
					}

				});
			}
			pane00.add(m[i]);
			pane0.add(menu[3 - i]);
		}
		menu[0].setBounds(15, 0, 346, 200);
		//menu[1].setBounds(80, 35, 210, 32);
		m[1].setBounds(10 - 1, 1 * 30, 105, 35);
		m[2].setBounds(10 - 3, 2 * 30, 105, 35);
		panelInit(this);
	}

	void setFunc(Func func) {
		this.func = func;
		addFrame = new AddDemandFrame(func);
	}

	void panelInit(WhitePanel mainPanel) {

		mainPanel.add(pane00);
		mainPanel.add(pane0);
	}

	public void setDemand(DemandF demand) {
		if (demand.date != null) {
			String[] token = demand.date.split("-");
			model.setDate(Integer.parseInt(token[0]), Integer.parseInt(token[1])-1, Integer.parseInt(token[2]));
			model.setSelected(true);
		}
		t[1].setText(demand.name);
		t[2].setText(demand.tel);
		t[3].setText(demand.who);
	}

	public DemandF getDemand() {
		DemandF demand = new DemandF();
		//sdf.set2DigitYearStart(model.getValue());
		demand.date =sdf.format(model.getValue());//datePicker.get;
		demand.name = t[1].getText();
		demand.tel = t[2].getText();
		demand.who = t[3].getText();
		return demand;
	}

	public void tableSet(int n) {
		row = new Object[n][3];
		Object column[] = { "상호", "담당자", "전화번호" };
		DefaultTableModel model = new DefaultTableModel((Object[][]) row, column){
	    public boolean isCellEditable(int row, int col) {
	     return false;
	    }};
	    table = new JTable(model);
	    table.getTableHeader().setReorderingAllowed(false);
		table.getColumn("상호").setPreferredWidth(90);
		table.getColumn("담당자").setPreferredWidth(30);
		table.getColumn("전화번호").setPreferredWidth(40);

		table.setRowHeight(18);
		table.getTableHeader().setFont(new Font(Main.font, 0, 12));

		table.setAutoCreateRowSorter(true);
		TableRowSorter sorter = new TableRowSorter(table.getModel());
		table.setRowSorter(sorter);
		table.addMouseListener(new MouseAdapter() {
			int sel = -1;

			public void mouseReleased(MouseEvent arg0) {
				if (sel == table.getSelectedRow()) {
					if (sel == -1)
						return;
					setDemand(manageLi.demandMatch[sel]);
					frame.setVisible(false);
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
		frame.setVisible(true);
		/*
		 * frame.repaint(); panel.repaint();
		 */
	}

	public void removeDemand(int sel) {//
		manageLi.removeMat(sel);

	}

	public void buttonInit() {
		Button button[] = new Button[3];
		button[0] = new Button("추가");
		button[0].setBounds(75, 495, 80, 40);
		button[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addFrame.setVisible(true);
			}
		});

		button[1] = new Button("제거");
		button[1].setBounds(170, 495, 80, 40);
		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				removeDemand(sel);
				loadList();
				tableInit();
				// 제거
			}
		});
		button[2] = new Button("불러오기");
		button[2].setBounds(265, 495, 80, 40);
		button[2].addActionListener(new ActionListener() {
			DemandF demand;

			public void actionPerformed(ActionEvent arg0) {
				int sel = table.getSelectedRow();
				if (sel == -1)
					return;
				setDemand(manageLi.demandMatch[sel]);
				frame.setVisible(false);
			}
		});
		frame.add(button[0]);
		frame.add(button[1]);
		frame.add(button[2]);
	}

	public void loadList() {
		String st, name, tel, who, stn[];
		manageLi.top = 0; // new ManageLi();
		BufferedReader fr;
		File f;
		searchField.setText("");
		manageLi.setMatchStr("");
		try {
			f = new File("demandList.list");
			if (f.exists() == false) {
				return;
			}
			fr = new BufferedReader(new FileReader("demandList.list"));
			while (null != (st = fr.readLine())) {
				stn = st.split("/");
				if (stn.length == 0)
					return;
				name = stn[0].replaceAll(" ", "");
				tel = stn[1].replaceAll(" ", "");
				who = stn[2].replaceAll(" ", "");
				manageLi.addList(name, tel, who);
			}
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void tableInit() {
		if (manageLi.getMatchCount() == 0)
			return;
		tableSet(manageLi.getMatchCount());
		for (int i = 0; i < manageLi.mattop; i++) {
			table.setValueAt(manageLi.demandMatch[i].name, i, 0);
			table.setValueAt(manageLi.demandMatch[i].tel, i, 2);
			// table.setValueAt(SupTable.toNumFormat(Integer.parseInt(manageList.demandst[i].money)), i,
			// 2);
			table.setValueAt(manageLi.demandMatch[i].who, i, 1);
		}
	}
}

class ManageLi {
	DemandF[] demand;
	DemandF[] demandMatch;
	String match;
	int maxSize;
	int top;
	int mattop;

	// int num;

	ManageLi() {
		maxSize = 100;
		demand = new DemandF[maxSize];
		demandMatch = new DemandF[maxSize];
		top = 0;
		mattop = 0;
		match = "";
		// num = 0;
	}

	public void removeMat(int sel) {
		String name, tel, who;
		String fileName;
		fileName = new String("demandList.list");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < top; i++) {
				if (demand[i] == demandMatch[sel])
					continue;
				name = demand[i].name;
				tel = demand[i].tel;
				who = demand[i].who;
				if (name == "" || name == null || name.length() == 0)
					continue;
				if (tel == "" || tel == null || tel.length() == 0) {
					tel = " ";
				}
				if (who == "" || who == null || who.length() == 0)
					who = " ";
				fw.write(name + "/" + tel + "/" + who + "/\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	public int getMatchCount() {
		mattop = 0;
		for (int i = 0; i < top; i++) {
			if (demand[i].name.matches(".*" + match + ".*")) {
				addMatch(demand[i]);
			}
		}
		return mattop;
	}

	private void addMatch(DemandF demandF) {
		demandMatch[mattop++] = demandF;
	}

	public void setMatchStr(String text) {
		match = text;
	}

	void addList(String name, String tel, String who) {
		if (!name.equals("")) {
			if (top == maxSize)
				resize();
			demand[top++] = new DemandF(name, tel, who);

		}
	}

	void resize() {
		maxSize *= 2;
		demandMatch = new DemandF[maxSize];
		DemandF temp[] = new DemandF[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = demand[i];
		demand = temp;
	}
}
class DateFormatter extends AbstractFormatter {

	private String datePattern = "yyyy-MM-dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	@Override
	public Object stringToValue(String text) throws ParseException {
		return dateFormatter.parseObject(text);
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		if (value != null) {
			Calendar cal = (Calendar) value;
			return dateFormatter.format(cal.getTime());
		}
		return "";
	}

}