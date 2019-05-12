package com.kmichali.utility;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kmichali.model.*;
import javafx.scene.control.TableView;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VatInvoicePDF {

    private String PDFPATH="C:/Users/Kamil/Desktop/pdf/";
    Document document;


    public VatInvoicePDF(Invoice invoice, Date date,Seller seller, Company companySeller,Company companyCustomer, Customer customer,
                         TableView<InvoiceField> productTable
    ) throws DocumentException, IOException {
        CreateDocument(invoice ,date,seller,companySeller,companyCustomer,customer,productTable);
        OpenPDF("C:/Users/Kamil/Desktop/pdf/t.pdf");
    }

    private void CreateDocument(Invoice invoice, Date date,Seller seller, Company companySeller,Company companyCustomer, Customer customer,
                                TableView<InvoiceField> productTable
    ) throws DocumentException, IOException {
        document = new Document(PageSize.A4,0,0,20,20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDFPATH+"t.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        fillPdfDocument(invoice ,date,seller,companySeller,companyCustomer,customer,productTable);

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

    private void fillPdfDocument(Invoice invoice, Date date,Seller seller, Company companySeller,Company companyCustomer, Customer customer,
    TableView<InvoiceField> productTable) throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfPTable table = new PdfPTable(3); //3 columns
        table.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] columnWidths = {30,40,30};
        table.setWidths(columnWidths);

        PdfPCell companyLogoCell = new PdfPCell(new Paragraph("UNIWERS",new Font(bf, 16)));
        companyLogoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyLogoCell.setVerticalAlignment(Element.ALIGN_CENTER);
        companyLogoCell.setMinimumHeight(150);

        Paragraph paragraph2 = new Paragraph("FAKTURA VAT "+invoice.getInvoiceNumber(),new Font(bf, 16));
        paragraph2.add(Chunk.NEWLINE);
        paragraph2.setFont(new Font(bf,12));
        paragraph2.add("ORYGINAŁ");
        PdfPCell invoiceNameCell = new PdfPCell(paragraph2);
        invoiceNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceNameCell.setVerticalAlignment(Element.ALIGN_TOP);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.setFont(new Font(bf,11));
        paragraph3.add("Data wystawienia: "+ date.getIssueDate());
        paragraph3.add(Chunk.NEWLINE);
        paragraph3.add("Data sprzedaży: "+ date.getSellDate());
        paragraph3.add(Chunk.NEWLINE);
        paragraph3.add("Tarmin płatności :"+date.getPaymentDate());
        PdfPCell dateCell = new PdfPCell(paragraph3);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setVerticalAlignment(Element.ALIGN_TOP);

        table.addCell(companyLogoCell);
        table.addCell(invoiceNameCell);
        table.addCell(dateCell);

        PdfPTable customerTable = new PdfPTable(2); //2 columns
        customerTable.setWidthPercentage(98);   //Width 100%;

        Paragraph paragraph4 = new Paragraph("SPRZEDAWCA",new Font(bf, 16));
        paragraph4.setFont(new Font(bf,12));
        paragraph4.add(Chunk.NEWLINE);
        if(companySeller != null) {
            paragraph4.add(companySeller.getName());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add(companySeller.getSeller().getName() + " " + companySeller.getSeller().getSurname());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add(companySeller.getSeller().getAddress());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add(companySeller.getSeller().getCity()+" "+companySeller.getSeller().getPostalCode());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add("NIP: " + companySeller.getNip());
//        }else if(company == null){
//            paragraph4.add(seller.getName() + " " + seller.getSurname());
//            paragraph4.add(seller.getAddress());
//            paragraph4.add(seller.getCity()+" "+seller.getPostalCode());
        }

        PdfPCell sellerCell = new PdfPCell(paragraph4);
        sellerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        sellerCell.setVerticalAlignment(Element.ALIGN_TOP);

        Paragraph paragraph5 = new Paragraph("NABYWCA",new Font(bf, 16));
        paragraph5.setFont(new Font(bf,12));
        paragraph5.add(Chunk.NEWLINE);
        if(companyCustomer != null) {
            paragraph5.add(companyCustomer.getName());
            paragraph5.add(Chunk.NEWLINE);
            paragraph5.add(companyCustomer.getCustomer().getName() + " " + companyCustomer.getCustomer().getSurname());
            paragraph5.add(Chunk.NEWLINE);
            paragraph5.add(companyCustomer.getCustomer().getAddress());
            paragraph5.add(Chunk.NEWLINE);
            paragraph5.add(companyCustomer.getCustomer().getCity()+" "+companyCustomer.getCustomer().getPostalCode());
            paragraph5.add(Chunk.NEWLINE);
            paragraph5.add("NIP: " + companyCustomer.getNip());

            for(int i=0; i<4; i++) {
                paragraph5.add(Chunk.NEWLINE);
            }
//        }else if(company == null){
//            paragraph5.add(customer.getName() + " " + seller.getSurname());
//            paragraph5.add(customer.getAddress());
//            paragraph5.add(customer.getCity()+" "+seller.getPostalCode());
        }
        PdfPCell customerCell = new PdfPCell(paragraph5);
        customerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        customerCell.setVerticalAlignment(Element.ALIGN_TOP);

        customerTable.addCell(sellerCell);
        customerTable.addCell(customerCell);

        //Invoice table
        PdfPTable invoiceTable = new PdfPTable(10); //10 columns
        invoiceTable.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] invoiceTableColumnWidth = {4,22,8,8,8,10,10,10,10,10};
        invoiceTable.setWidths(invoiceTableColumnWidth);

        PdfPCell cell;
        cell = new PdfPCell(new Paragraph("LP",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Nazwa produktu",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell( new Paragraph("Klasa",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph(new Paragraph("Ilość",new Font(bf, 10))));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("J.m",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Cenna netto",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Wartość netto",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Stawka Vat",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell( new Paragraph("Kwota Vat",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Wartość brutto",new Font(bf, 10)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        for (InvoiceField row: productTable.getItems()) {
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getLp()),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(row.getNameProduct(),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(row.getProductClass(),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(row.getUnitMeasure(),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getAmount()),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getPriceNetto())+" zł",new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getProductValue())+" zł",new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getTax().getSelectionModel().getSelectedItem()),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getPriceVat())+" zł",new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getPriceBrutto())+" zł",new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
        }

        //add nextRow and hiddern first 6 cell through set border color
        for(int i=0; i<6;i++){
            cell= new PdfPCell();
            cell.setUseVariableBorders(true);
            cell.setBorderColorTop(BaseColor.BLACK);
            cell.setBorderColor(BaseColor.WHITE);

            invoiceTable.addCell(cell);

        }

        //summary price row
        double totalProductValue=0;
        double totalVat =0;
        double totalBrutto=0;
        for (InvoiceField row: productTable.getItems()) {
            totalProductValue = totalProductValue + row.getProductValue();
            totalVat = totalVat + row.getPriceVat();
            totalBrutto = totalBrutto+row.getPriceBrutto();
        }
        cell = new PdfPCell(new Paragraph(String.valueOf(totalProductValue)+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(" "));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(String.valueOf(totalVat)+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(String.valueOf(totalBrutto)+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        //add nextRow and hiddern first 8 cell through set border color
        for(int i=0; i<7;i++){
            cell= new PdfPCell();
            cell.setUseVariableBorders(true);
            cell.setBorderColor(BaseColor.WHITE);
            if(i>5){
                cell.setUseVariableBorders(true);
                cell.setBorderColorTop(BaseColor.BLACK);
            }
            invoiceTable.addCell(cell);
            if(i == 6){
                cell = new PdfPCell(new Paragraph("Razem do zapłaty:", new Font(bf, 10)));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                invoiceTable.addCell(cell);
            }
        }

        cell = new PdfPCell(new Paragraph(String.valueOf(totalBrutto)+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);


        document.add(table);
        document.add(customerTable);
        document.add(invoiceTable);

    }

}