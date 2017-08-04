package Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import Demand.Demand;
import Estimate.Estimate;
import Main.Main;
import Product.Product;
import Product.ProductList;
import Supply.Supply;

public class PdfSave {
	Font titleFont;
	Font bold20Font;
	Font bold12Font;
	Font bold10Font;
	Font normal10Font;
	BaseColor rgb;

	public PdfSave() {
		rgb = new BaseColor(255, 255, 150);
		try {
			BaseFont objFont = BaseFont.createFont("font/batang.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			titleFont = new Font(objFont, 50);
			titleFont.setStyle(Font.BOLD);

			bold20Font = new Font(objFont, 20);
			bold20Font.setStyle(Font.BOLD);

			bold12Font = new Font(objFont, 12);
			bold12Font.setStyle(Font.BOLD);

			bold10Font = new Font(objFont, 10);
			bold10Font.setStyle(Font.BOLD);

			normal10Font = new Font(objFont, 10);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "폰트를 넣고 다시 시도하세요.");
		}

	}

	public void exportPDF(String fName, Estimate est) {
		Document document = new Document();
		document.setMargins(45, 45, 45, 45);
		String path = "save_pdf";
		Paragraph paragraph;
		try {
			File a = new File(path);
			if (a.exists() == false) {

				a.mkdirs();
			}
			PdfWriter.getInstance(document, new FileOutputStream(path + "\\" + fName + ".pdf"));
			document.open();
			Paragraph 견적서 = new Paragraph("견적서", titleFont);
			견적서.setAlignment(Element.ALIGN_CENTER);
			견적서.setSpacingAfter(27f);
			document.add(견적서);

			writeInit(document, est);
			document.add(headTable(est.getTableWidth()));
			int page = 1;
			int index = 0;
			do {
				document.add(createTable(est.getTableWidth(), est.getProductList(), index));
				paragraph = new Paragraph(String.valueOf(page++));
				paragraph.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph);
				if (index == 0)
					index += Main.FrontRow;
				else
					index += Main.BackRow;
			} while (index < est.getProductList().getMaxSize());
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "pdf 파일을 닫고 다시 시도하세요.");
		} catch (DocumentException e) {
			JOptionPane.showMessageDialog(null, "pdf 파일을 닫고 다시 시도하세요.");
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "pdf 파일을 닫고 다시 시도하세요.");
		}
		document.close();

	}

	private void writeInit(Document document, Estimate est) {
		try {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setSpacingAfter(8);
			PdfPCell deCell = new PdfPCell(
					writeDemand(est.getDemand(), Main.longToMoneyString(est.getProductList().getSumMoney())));
			deCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			deCell.setBorder(0);
			PdfPCell supCell = new PdfPCell(writeSupply(est.getSupply()));
			supCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			supCell.setBorder(0);
			table.addCell(deCell);
			table.addCell(supCell);
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	private PdfPTable writeDemand(Demand demand, String sumMoney) {
		PdfPTable table = new PdfPTable(4);
		PdfPCell title[] = new PdfPCell[4];
		PdfPCell titleSub[] = new PdfPCell[4];
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		try {
			table.setWidths(new int[] { 6, 2, 13, 1 });
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		title[0] = new PdfPCell(new Paragraph(" 견 적 일 :", bold12Font));
		title[1] = new PdfPCell(new Paragraph(" 상     호 :", bold12Font));
		title[2] = new PdfPCell(new Paragraph(" 전화번호:", bold12Font));
		title[3] = new PdfPCell(new Paragraph(" 담 당 자 :", bold12Font));
		titleSub[0] = new PdfPCell(new Paragraph(demand.getDate(), bold10Font));
		titleSub[1] = new PdfPCell(new Paragraph(demand.getName(), bold10Font));
		titleSub[2] = new PdfPCell(new Paragraph(demand.getTel(), bold10Font));
		titleSub[3] = new PdfPCell(new Paragraph(demand.getWho(), bold10Font));
		PdfPCell tmp = new PdfPCell(new Paragraph(" "));
		tmp.setBorder(0);
		for (int i = 0; i < 4; i++) {
			title[i].setFixedHeight(21f);
			title[i].setHorizontalAlignment(Element.ALIGN_LEFT);
			title[i].setVerticalAlignment(Element.ALIGN_MIDDLE);
			title[i].setBorder(0);
			titleSub[i].setHorizontalAlignment(Element.ALIGN_LEFT);
			titleSub[i].setVerticalAlignment(Element.ALIGN_MIDDLE);
			titleSub[i].setColspan(2);
			titleSub[i].setBorder(0);
			table.addCell(title[i]);
			table.addCell(titleSub[i]);
			// table.addCell(tmp);
			table.addCell(tmp);
		}
		PdfPCell sumStr = new PdfPCell(new Paragraph("합계금액", bold20Font));
		PdfPCell sum = new PdfPCell(new Paragraph(sumMoney + "원", bold20Font));
		sumStr.setVerticalAlignment(Element.ALIGN_CENTER);
		sumStr.setColspan(2);
		sumStr.setBorder(0);
		sum.setVerticalAlignment(Element.ALIGN_CENTER);
		sum.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sum.setBackgroundColor(rgb);
		tmp.setFixedHeight(4f);
		table.addCell(tmp);
		table.addCell(tmp);
		table.addCell(tmp);
		table.addCell(tmp);
		table.addCell(sumStr);
		table.addCell(sum);
		table.addCell(tmp);
		return table;
	}

	private PdfPTable writeSupply(Supply supply) {
		PdfPTable table = new PdfPTable(5);
		try {
			table.setWidths(new int[] { 8, 12, 30, 12, 30 });
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);

		table.setWidthPercentage(50f);
		PdfPCell title[] = new PdfPCell[9];

		title[0] = new PdfPCell(new Paragraph("공급자", bold20Font));
		title[1] = new PdfPCell(new Paragraph("등록번호", bold12Font));
		title[2] = new PdfPCell(new Paragraph("상호", bold12Font));
		title[3] = new PdfPCell(new Paragraph("성명", bold12Font));
		title[4] = new PdfPCell(new Paragraph("주소", bold12Font));
		title[5] = new PdfPCell(new Paragraph("업태", bold12Font));
		title[6] = new PdfPCell(new Paragraph("종목", bold12Font));
		title[7] = new PdfPCell(new Paragraph("전화", bold12Font));
		title[8] = new PdfPCell(new Paragraph("팩스", bold12Font));
		// 컬럼 바탕색

		for (PdfPCell tit : title) {
			tit.setFixedHeight(23f);
			tit.setHorizontalAlignment(Element.ALIGN_CENTER);
			tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
		}
		title[0].setRowspan(5);
		table.addCell(title[0]);

		title[1].setFixedHeight(28f);
		table.addCell(title[1]);
		PdfPCell 등록번호 = new PdfPCell(new Paragraph(supply.getNum(), bold20Font));
		등록번호.setVerticalAlignment(Element.ALIGN_MIDDLE);
		등록번호.setHorizontalAlignment(Element.ALIGN_CENTER);
		등록번호.setColspan(3);
		table.addCell(등록번호);

		table.addCell(title[2]);
		PdfPCell 상호 = new PdfPCell(new Paragraph(supply.getCompany(), bold10Font));
		상호.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(상호);

		table.addCell(title[3]);
		PdfPCell 성명 = new PdfPCell(new Paragraph(supply.getName(), bold10Font));
		성명.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(성명);

		table.addCell(title[4]);
		PdfPCell 주소 = new PdfPCell(new Paragraph(supply.getAddress(), bold10Font));
		주소.setVerticalAlignment(Element.ALIGN_MIDDLE);
		주소.setColspan(3);
		table.addCell(주소);

		table.addCell(title[5]);
		PdfPCell 업태 = new PdfPCell(new Paragraph(supply.getWork(), bold10Font));
		업태.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(업태);

		table.addCell(title[6]);
		PdfPCell 종목 = new PdfPCell(new Paragraph(supply.getWork2(), bold10Font));
		종목.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(종목);

		table.addCell(title[7]);
		PdfPCell 전화 = new PdfPCell(new Paragraph(supply.getTel(), bold10Font));
		전화.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(전화);

		table.addCell(title[8]);
		PdfPCell 팩스 = new PdfPCell(new Paragraph(supply.getFax(), bold10Font));
		팩스.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(팩스);
		return table;
	}

	public PdfPTable headTable(int tableWidth[]) {
		PdfPTable tableHead = new PdfPTable(8);
		tableHead.setWidthPercentage(100f);
		try {
			tableHead.setWidths(tableWidth);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PdfPCell titleCell[] = new PdfPCell[8];
		titleCell[0] = new PdfPCell(new Paragraph("품목", bold12Font));
		titleCell[1] = new PdfPCell(new Paragraph("규격", bold12Font));
		titleCell[2] = new PdfPCell(new Paragraph("자재비", bold12Font));
		titleCell[3] = new PdfPCell(new Paragraph("가공비", bold12Font));
		titleCell[4] = new PdfPCell(new Paragraph("수량", bold12Font));
		titleCell[5] = new PdfPCell(new Paragraph("단가", bold12Font));
		titleCell[6] = new PdfPCell(new Paragraph("공급가액", bold12Font));
		titleCell[7] = new PdfPCell(new Paragraph("비고", bold12Font));
		// 컬럼 바탕색
		for (PdfPCell cell : titleCell) {
			cell.setFixedHeight(24f);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableHead.addCell(cell);
		}
		return tableHead;
	}

	public PdfPTable createTable(int tableWidth[], ProductList productList, int index) {
		int count;
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100f);
		try {
			table.setWidths(tableWidth);
		} catch (DocumentException e) {
			JOptionPane.showMessageDialog(null, "PDF 저장 오류");
		}
		PdfPCell cell = null;
		Product product;
		if (index == 0)
			count = Main.FrontRow;
		else
			count = index + Main.BackRow;

		for (int i = index; i < count; i++) {

			product = productList.getProduct(i);
			for (int j = 0; j < 8; j++) {
				switch (j) {
				case 0:
					cell = new PdfPCell(new Paragraph(product.getName(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					break;
				case 1:
					cell = new PdfPCell(new Paragraph(product.getStandard(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					break;
				case 2:
					cell = new PdfPCell(new Paragraph(product.getMaterialCost(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					break;
				case 3:
					cell = new PdfPCell(new Paragraph(product.getProcessedCost(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					break;
				case 4:
					cell = new PdfPCell(new Paragraph(product.getCount(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					break;
				case 5:
					cell = new PdfPCell(new Paragraph(product.getCost(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					break;
				case 6:
					cell = new PdfPCell(new Paragraph(product.getSumMoneyString(), normal10Font));
					cell.setBackgroundColor(rgb);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					break;
				case 7:
					cell = new PdfPCell(new Paragraph(product.getEtc(), normal10Font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					break;
				}
				cell.setFixedHeight(20f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
		}
		if (productList.getMaxSize() != count)
			cell = new PdfPCell(new Paragraph(""));
		else
			cell = new PdfPCell(new Paragraph("합계", bold12Font));
		cell.setColspan(2);
		cell.setFixedHeight(30f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(""));
		cell.setColspan(4);
		cell.setFixedHeight(30f);
		table.addCell(cell);

		if (productList.getMaxSize() != count)
			cell = new PdfPCell(new Paragraph(""));
		else
			cell = new PdfPCell(new Paragraph(Main.longToMoneyString(productList.getSumMoney()), normal10Font));
		cell.setFixedHeight(30f);
		cell.setBackgroundColor(rgb);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(""));
		cell.setFixedHeight(30f);
		table.addCell(cell);
		return table;
	}

}
