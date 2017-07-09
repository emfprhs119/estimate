package FrameComponent;

import java.awt.Graphics;

//배경이 하얀색이고 layout이 null인 Container 외곽선 draw
public class WhiteRectPanel extends WhitePanel {
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, 755,875);
	}
	
}