package com.kmichali.utility;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kmichali.model.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StatementPDF {
    private String PDFPATH = "C:/Users/Kamil/Desktop/pdf/";
    Document document;


    public StatementPDF(Customer customer, ComboBox veterynaryInspectoreCB,
                        DatePicker datePicker,TableView<InvoiceField> productTable) throws DocumentException, IOException {
        CreateDocument(customer,veterynaryInspectoreCB,datePicker,productTable);
        OpenPDF("C:/Users/Kamil/Desktop/pdf/t2.pdf");
    }

    private void CreateDocument(Customer customer, ComboBox veterynaryInspectoreCB,
                                DatePicker datePicker,TableView<InvoiceField> productTable) throws DocumentException, IOException {
        document = new Document(PageSize.A4, 5, 5, 60, 20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDFPATH + "t2.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        fillPdfDocument(customer,veterynaryInspectoreCB,datePicker,productTable);

        document.close();
    }

    private void OpenPDF(String path) {
        File file = new File(path);
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillPdfDocument( Customer customer, ComboBox veterynaryInspectoreCB,
                                   DatePicker datePicker,TableView<InvoiceField> productTable) throws DocumentException, IOException {

        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfPTable customerTable = new PdfPTable(2); //2 columns
        customerTable.setWidthPercentage(98);   //Width 100%;
        float[] columnWidths= {40,60};
        customerTable.setWidths(columnWidths);

        PdfPCell cell;
        cell = new PdfPCell(new Paragraph(customer.getName()+" "+customer.getSurname(),new Font(bf, 12)));
        cell.setLeading(2f,1);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(customer.getAddress()+", "+customer.getPostalCode()+" "+customer.getCity(),new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        customerTable.addCell(cell);

        for(int i =0; i<7; i++) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(0);
            customerTable.addCell(cell);
            if(i == 4){
                cell = new PdfPCell(new Paragraph("OŚWIADCZENIE", new Font(bf, 16,Font.BOLD)));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                customerTable.addCell(cell);
            }
        }

        PdfPTable mainTable = new PdfPTable(1); //2 columns
        mainTable.setWidthPercentage(98);   //Width 100%;
        String statementTextPart1="Oświadczam, że jestem zarejestrowanym w Państowym Inspktoracie Weterynarii w " +veterynaryInspectoreCB.getSelectionModel().getSelectedItem()+
                " podmiotem działającym na rynku pasz w rozumieniu przepisów Rozporządzenia (WE) Nr 183/2006\n" +
                "Parlamentu Europejskiego i Rady z dnia 12 stycznia 2005 r. ustanawiającego wymagania dotyczące\n" +
                "higieny pasz.";
        cell = new PdfPCell(new Paragraph(statementTextPart1,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setLeading(2f,1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(" "));
        cell.setBorder(0);
        mainTable.addCell(cell);

        String statementTextPart2="";
        for (InvoiceField row: productTable.getItems()) {
             statementTextPart2 = "Oświadczam jednocześnie, że dostarczony przeze mnie w dniu " + datePicker.getValue() + " towar " + row.getNameProduct() + " został wyprodukowany, przetoworzony i dystrybuowany zgodnie" +
                    "z prawem UE, ustawodastwem krajowym oraz zasadami dobrej praktyki w zakresie ochrony przed ryzykiem biologicznego, chemicznego i fizycznego zanieczyszczenia paszy.";
        }
        cell = new PdfPCell(new Paragraph(statementTextPart2,new Font(bf, 12)));
        cell.setLeading(2f,1);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        mainTable.addCell(cell);

        for(int i =0; i<3; i++) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(0);
            mainTable.addCell(cell);
        }


        PdfPTable signTable = new PdfPTable(2); //2 columns
        signTable.setWidthPercentage(98);   //Width 100%;
        float[] columnWidths2= {60,40};
        signTable.setWidths(columnWidths2);

        cell = new PdfPCell(new Paragraph(" "));
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("   "+customer.getIdentityCard().getSeriaAndNumber(),new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("......................................................." +
                "...................................",new Font(bf, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("seria i nr dowodu osobistego oraz podpis",new Font(bf, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(0);
        signTable.addCell(cell);



        document.add(customerTable);
        document.add(mainTable);
        document.add(signTable);
    }
}
