package Supply;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import FrameComponent.WhitePanel;
import Main.CsvPasser;
import Main.Main;

//�ŷ�ó �߰�
class SupplyEdit extends JFrame {
	JTextField textField[];

	SupplyEdit(SupplyView supplyView) {
		super("������ �����ϱ�");
		setLayout(null);
		setBounds(500, 500, 460, 320);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		WhitePanel leftPanel = new WhitePanel();
		WhitePanel rightPanel = new WhitePanel();
		leftPanel.setLayout(null);
		leftPanel.setBounds(80, 0, 410, 300);
		rightPanel.setBounds(0, 0, 130, 300);

		JPanel[] rightTextPanel = new JPanel[8];
		JPanel[] leftLabelPanel = new JPanel[8];
		textField = new JTextField[8];
		JLabel label[] = new JLabel[8];
		for (int i = 0; i < textField.length; i++) {
			rightTextPanel[i] = new JPanel();
			leftLabelPanel[i] = new JPanel();
			rightTextPanel[i].add(textField[i] = new JTextField(25));
			leftLabelPanel[i].add(label[i] = new JLabel(Supply.supplyArr[i]+" : "));
			
			rightTextPanel[i].setBounds(60, i * 30, 300, 35);
			leftLabelPanel[i].setBounds(20, i * 30, 120, 35);
			rightTextPanel[i].setBackground(Color.WHITE);
			leftLabelPanel[i].setBackground(Color.WHITE);
			label[i].setFont(new Font(Main.font, Font.BOLD, 13));
			label[i].setHorizontalAlignment(JLabel.RIGHT);
			textField[i].setFont(new Font(Main.font, Font.BOLD, 13));
			textField[i].setHorizontalAlignment(JTextField.LEFT);
			textField[i].addKeyListener(new supplyEditListener(i, supplyView, this));
			rightPanel.add(leftLabelPanel[i]);
			leftPanel.add(rightTextPanel[i]);
			
		}
		for (int i = 0; i < 8; i++) {
			
		}
		JButton button = new JButton("����");
		button.setFont(new Font(Main.font, 0, 12));
		button.setBounds(350-70, 255, 60, 20);
		button.addActionListener(new supplyEditListener(0, supplyView, this));
		add(button);
		button = new JButton("���");
		button.setFont(new Font(Main.font, 0, 12));
		button.setBounds(350, 255, 60, 20);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		add(button);
		add(rightPanel);
		add(leftPanel);
		if (new File("./supply.csv").exists())
			loadSupply();
	}
	void loadSupply() {
		BufferedReader fr = null;
		String st = null;
		try {
			fr = new BufferedReader(new FileReader("./supply.csv"));
				for (int i = 0; i < 8; i++) {
					st = fr.readLine();
					String stn[] = CsvPasser.csvSplit(st);
					if (stn.length>1)
						textField[i].setText(stn[1]);
				}
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "supply.csv�� �ҷ��� �� �����ϴ�.");
			}
		}
	Supply getSupply() {
		Supply supply = new Supply();
		supply.setNum(textField[0].getText()); // ����� ��Ϲ�ȣ
		supply.setCompany(textField[1].getText()); // ��ȣ
		supply.setName(textField[2].getText()); // ��ǥ
		supply.setAddress(textField[3].getText()); // �ּ�
		supply.setWork(textField[4].getText()); // ����
		supply.setWork2(textField[5].getText()); // ����
		supply.setTel(textField[6].getText()); // ��ȭ
		supply.setFax(textField[7].getText()); // �ѽ�
		return supply;
	}

	void removeField() {
		for (int i = 0; i < 3; i++) {
			textField[i].setText("");
		}
		textField[0].requestFocus();
	}
	
	void writeSupply() {
		BufferedWriter fr = null;
			String st = null;
			System.out.println("ww");
			try {
				fr = new BufferedWriter(new FileWriter("supply.csv"));

					for (int i = 0; i < 8; i++) {
						fr.write(Supply.supplyArr[i]+","+textField[i].getText()+"\r\n");
					}
					fr.flush();
					fr.close();
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, "supply.csv�� �ҷ��� �� �����ϴ�.");
				}
			
	}
}

class supplyEditListener extends KeyAdapter implements ActionListener {
	int key;
	SupplyView supplyView;
	SupplyEdit supplyEdit;

	supplyEditListener(int key, SupplyView supplyView, SupplyEdit supplyEdit) {
		this.key = key;
		this.supplyView = supplyView;
		this.supplyEdit = supplyEdit;
	}

	public void actionPerformed(ActionEvent e) {
		supplyEdit.writeSupply();
		supplyView.loadSupply();
		supplyEdit.setVisible(false);

	}

	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
			if (key < 7)
				supplyEdit.textField[key + 1].requestFocus();
			else{
				supplyEdit.writeSupply();
				supplyView.loadSupply();
				supplyEdit.setVisible(false);
			}
		}
	}
}