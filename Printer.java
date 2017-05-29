import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

class Printer implements Printable {
		final Component comp;
		Container contentPane;
		int flag=0;
		public Printer(Component comp,Container contentPane) {
			this.comp = comp;
			this.contentPane=contentPane;
		}

		public int Printer(Graphics g, PageFormat format, int page_index) throws PrinterException {
			if (page_index >= flag) {
				return Printable.NO_SUCH_PAGE;
			}

			Dimension dim = comp.getSize();
			double cHeight = dim.getHeight();
			double cWidth = dim.getWidth();

			double pHeight = format.getImageableHeight();
			double pWidth = format.getImageableWidth();

			double pXStart = format.getImageableX();
			double pYStart = format.getImageableY();

			double xRatio = pWidth / cWidth;
			double yRatio = pHeight / cHeight;

			Graphics2D g2 = (Graphics2D) g;
			g2.translate(pXStart, pYStart);
			g2.scale(xRatio, yRatio);
			comp.paint(g2);

			return Printable.PAGE_EXISTS;
		}
		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			Graphics2D g2d;
			if (pageIndex > flag - 1)
				return Printable.NO_SUCH_PAGE;
			if (pageIndex <= flag - 1) { // 첫페이지라면
				g2d = (Graphics2D) graphics;
				g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY()); // (0,0)
				g2d.scale(0.72, 0.72);
				contentPane.paint(g2d);
				return (PAGE_EXISTS);
			} else {
				return (NO_SUCH_PAGE);
			}
		}
	}