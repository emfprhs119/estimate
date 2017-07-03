
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;



public class DemandView extends WhitePanel {
	Demand demand;
	WhitePanel leftPanel, rightPanel;
	JPanel rightTextPanel[];
	JPanel leftLabelPanel[];
	JTextField rightTextField[];
	DemandLoad demandLoad;
	UtilDateModel model = new UtilDateModel();
	JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
	JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateFormatter());
	JButton demandLoadButton;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public DemandView(Demand demand) {
		setBounds(0, 124, 350, 290);
		this.demand=demand;
		demandLoad = new DemandLoad();
		leftPanel = new WhitePanel();
		rightPanel = new WhitePanel();
		leftPanel.setBounds(45, 4, 350, 120);
		rightPanel.setBounds(15, 0, 115, 120);

		rightTextPanel = new JPanel[5];
		leftLabelPanel = new JPanel[5];
		rightTextField = new JTextField[5];
		for (int i = 0; i < 5; i++) {
			rightTextPanel[i] = new JPanel();
			leftLabelPanel[i] = new JPanel();
		}
		JLabel leftLabel[] = new JLabel[5];
		leftLabelPanel[0].add(leftLabel[0] = new JLabel("견 적 일 :"));
		leftLabelPanel[1].add(leftLabel[1] = new JLabel("상      호 :"));
		leftLabelPanel[2].add(leftLabel[2] = new JLabel("전화번호:"));
		leftLabelPanel[3].add(leftLabel[3] = new JLabel("담 당 자 :"));

		rightTextPanel[0].add(datePicker);
		rightTextPanel[1].add(rightTextField[1] = new JTextField(14));
		rightTextPanel[2].add(rightTextField[2] = new JTextField(14));
		rightTextPanel[3].add(rightTextField[3] = new JTextField(14));
		
		demandLoadButton = new JButton(new ImageIcon("forder.png"));
		demandLoadButton.setBounds(181,1,28,25);
		demandLoadButton.setPreferredSize(new Dimension(28, 25));
		
		
		demandLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demandLoad.setVisible(true);
			}
		});
		rightTextPanel[1].add(demandLoadButton);
		model.setSelected(true);
		for (int i = 0; i < 4; i++) {
			
			rightTextPanel[i].setBounds(80, i*30+5, 210, 32);
			leftLabelPanel[i].setBounds(10, i * 30, 105, 35);
			rightTextPanel[i].setBackground(Color.WHITE);
			leftLabelPanel[i].setBackground(Color.WHITE);
			leftLabel[i].setFont(new Font(Main.font, Font.BOLD, 22));
			leftLabel[i].setHorizontalAlignment(JLabel.RIGHT);
			if (i != 0) {
				rightTextPanel[i].setLayout(null);
				rightTextField[i].setBounds(7, 1, 174, 25);
				rightTextField[i].setFont(new Font(Main.font, Font.BOLD, 15));
				rightTextField[i].setHorizontalAlignment(JTextField.LEFT);
				rightTextField[i].addKeyListener(new DemandListener(i,rightTextField,demand));
			}
			rightPanel.add(leftLabelPanel[i]);
			leftPanel.add(rightTextPanel[3 - i]);
		}
		
		//위치 조정
		rightTextPanel[0].setBounds(15, 0, 346, 200);
		leftLabelPanel[1].setBounds(10 - 1, 1 * 30, 105, 35);
		leftLabelPanel[2].setBounds(10 - 3, 2 * 30, 105, 35);
		panelInit(this);
	}

	void panelInit(WhitePanel mainPanel) {

		mainPanel.add(rightPanel);
		mainPanel.add(leftPanel);
	}

	public void setDemand(Demand demand) {
		if (demand.date != null) {
			String[] token = demand.date.split("-");
			model.setDate(Integer.parseInt(token[0]), Integer.parseInt(token[1])-1, Integer.parseInt(token[2]));
			model.setSelected(true);
		}
		rightTextField[1].setText(demand.name);
		rightTextField[2].setText(demand.tel);
		rightTextField[3].setText(demand.who);
	}

	public Demand getDemand() {
		Demand demand = new Demand();
		//sdf.set2DigitYearStart(model.getValue());
		demand.date =sdf.format(model.getValue());//datePicker.get;
		demand.name = rightTextField[1].getText();
		demand.tel = rightTextField[2].getText();
		demand.who = rightTextField[3].getText();
		return demand;
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
class DemandListener extends KeyAdapter{
	JTextField textField[];
	int key;
	Demand demand;
	DemandListener(int key,JTextField textField[],Demand demand){
		this.key=key;
		this.textField=textField;
		this.demand=demand;
	}
		public void keyPressed(KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				if (key != 3)
					textField[key + 1].requestFocus();
			} else
				Main.modify = true;
			switch(key){
				case 1:demand.setName(((JTextField)keyEvent.getSource()).getText());break;
				case 2:demand.setTel(((JTextField)keyEvent.getSource()).getText());break;
				case 3:demand.setWho(((JTextField)keyEvent.getSource()).getText());break;
			}
		}
}