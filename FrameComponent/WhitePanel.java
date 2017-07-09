package FrameComponent;
import java.awt.Color;
import javax.swing.JPanel;

//배경이 하얀색이고 layout이 null인 panel
public class WhitePanel extends JPanel {
	public WhitePanel(){
		setBackground(Color.WHITE);
		setLayout(null);
	}
}