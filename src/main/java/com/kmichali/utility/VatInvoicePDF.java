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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

        List<String> taxList = new ArrayList<>();
        HashSet<String> taxListWithoutDuplicated = new HashSet(); //list no duplicated
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfPTable table = new PdfPTable(3); //3 columns
        table.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] columnWidths = {30,50,20};
        table.setWidths(columnWidths);

        PdfPCell companyLogoCell = new PdfPCell(new Paragraph("UNIWERS",new Font(bf, 16)));
        companyLogoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyLogoCell.setVerticalAlignment(Element.ALIGN_CENTER);
        companyLogoCell.setMinimumHeight(120);
        companyLogoCell.setBorder(0);

        Paragraph paragraph2 = new Paragraph("FAKTURA VAT "+invoice.getInvoiceNumber(),new Font(bf, 16, Font.BOLD));
        paragraph2.add(Chunk.NEWLINE);
        paragraph2.setLeading(32);
        paragraph2.setFont(new Font(bf,12));
        paragraph2.add("ORYGINAŁ");
        PdfPCell invoiceNameCell = new PdfPCell(paragraph2);
        invoiceNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceNameCell.setVerticalAlignment(Element.ALIGN_TOP);
        invoiceNameCell.setBorder(0);

        PdfPTable dateTable = new PdfPTable(1); //3 columns
        dateTable.setWidthPercentage(100);   //Width 100%;

        PdfPCell cellDate;
        cellDate = new PdfPCell(new Paragraph("Data wystawienia",new Font(bf, 11, Font.BOLD)));
        cellDate.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        cellDate = new PdfPCell(new Paragraph(date.getIssueDate(),new Font(bf, 11)));
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        cellDate = new PdfPCell(new Paragraph("Data sprzedaży",new Font(bf, 11, Font.BOLD)));
        cellDate.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        cellDate = new PdfPCell(new Paragraph(date.getSellDate(),new Font(bf, 11)));
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        cellDate = new PdfPCell(new Paragraph("Tarmin płatności",new Font(bf, 11, Font.BOLD)));
        cellDate.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        cellDate = new PdfPCell(new Paragraph(date.getPaymentDate(),new Font(bf, 11)));
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        PdfPCell dateCell = new PdfPCell(dateTable);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dateCell.setBorder(0);

        table.addCell(companyLogoCell);
        table.addCell(invoiceNameCell);
        table.addCell(dateCell);

        PdfPTable customerTable = new PdfPTable(3); //2 columns
        customerTable.setWidthPercentage(98);   //Width 100%;
        float[] columnWidths2 = {45,10,45};
        customerTable.setWidths(columnWidths2);

        PdfPTable nameSellerTable = new PdfPTable(1); //3 columns
        nameSellerTable.setWidthPercentage(98);   //Width 100%;
        PdfPCell cellNameSeller;
        cellNameSeller = new PdfPCell(new Paragraph("Sprzedawca",new Font(bf, 13, Font.BOLD)));
        cellNameSeller.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cellNameSeller.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellNameSeller.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellNameSeller.setUseVariableBorders(true);
        cellNameSeller.setBorderColorTop(BaseColor.BLACK);
        cellNameSeller.setBorderColor(BaseColor.WHITE);
        nameSellerTable.addCell(cellNameSeller);

        Paragraph paragraph4 = new Paragraph();
        paragraph4.setLeading(2f);
        paragraph4.setFont(new Font(bf,11));
        if(companySeller != null) {
            paragraph4.add(companySeller.getName());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add(companySeller.getSeller().getName() + " " + companySeller.getSeller().getSurname());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add(companySeller.getSeller().getAddress());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add(companySeller.getSeller().getCity()+" "+companySeller.getSeller().getPostalCode());
            paragraph4.add(Chunk.NEWLINE);
            paragraph4.add("NIP: " + companySeller.getNip() +"   "+ "REGON " +companySeller.getRegon());
        }

        cellNameSeller = new PdfPCell(paragraph4);
        cellNameSeller.setNoWrap(true);
        cellNameSeller.setBorderColor(BaseColor.WHITE);
        cellNameSeller.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellNameSeller.setLeading(15,0);
        nameSellerTable.addCell(cellNameSeller);

        PdfPCell sellerCell = new PdfPCell(nameSellerTable);
        sellerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        sellerCell.setVerticalAlignment(Element.ALIGN_TOP);
        sellerCell.setBorder(0);

        PdfPCell emptyMiddleCell = new PdfPCell();
        emptyMiddleCell.setBorder(0);

        PdfPTable nameCustomerTable = new PdfPTable(1); //3 columns
        nameCustomerTable.setWidthPercentage(98);   //Width 100%;

        PdfPCell nameCustomerCell;
        nameCustomerCell = new PdfPCell(new Paragraph("Nabywca",new Font(bf, 13, Font.BOLD)));
        nameCustomerCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        nameCustomerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        nameCustomerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nameCustomerCell.setUseVariableBorders(true);
        nameCustomerCell.setBorderColorTop(BaseColor.BLACK);
        nameCustomerCell.setBorderColor(BaseColor.WHITE);
        nameCustomerTable.addCell(nameCustomerCell);


        Paragraph paragraph5 = new Paragraph();
        paragraph5.setLeading(2f);
        paragraph5.setFont(new Font(bf,11));
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

            for(int i=0; i<2; i++) {
                paragraph5.add(Chunk.NEWLINE);
            }

            nameCustomerCell = new PdfPCell(paragraph5);
            nameCustomerCell.setNoWrap(true);
            nameCustomerCell.setBorderColor(BaseColor.WHITE);
            nameCustomerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nameCustomerCell.setLeading(15,0);
            nameCustomerTable.addCell(nameCustomerCell);

        }

        PdfPCell customerCell = new PdfPCell(nameCustomerTable);
        customerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        customerCell.setVerticalAlignment(Element.ALIGN_TOP);
        customerCell.setBorder(0);

        customerTable.addCell(sellerCell);
        customerTable.addCell(emptyMiddleCell);
        customerTable.addCell(customerCell);


        //Invoice table
        PdfPTable invoiceTable = new PdfPTable(10); //10 columns
        invoiceTable.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] invoiceTableColumnWidth = {4,22,8,8,8,10,10,10,10,10};
        invoiceTable.setWidths(invoiceTableColumnWidth);

        PdfPCell cell;
        cell = new PdfPCell(new Paragraph("LP",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Nazwa produktu",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell( new Paragraph("Klasa",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph(new Paragraph("Ilość",new Font(bf, 10, Font.BOLD))));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("J.m",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Cenna netto",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Wartość netto",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Stawka Vat",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell( new Paragraph("Kwota Vat",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph("Wartość brutto",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        for (InvoiceField row: productTable.getItems()) {
            taxList.add(row.getTax().getSelectionModel().getSelectedItem());
            taxListWithoutDuplicated.add(row.getTax().getSelectionModel().getSelectedItem());

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
        for(int i=0; i<5;i++){
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
        cell = new PdfPCell(new Paragraph("Razem:", new Font(bf, 10, Font.BOLD)));
        cell.setBorderColorBottom(BaseColor.WHITE);
        cell.setBorderColorRight(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(String.valueOf(totalProductValue)+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("-"));
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

        //add empty row

        PdfPTable emptyTable = new PdfPTable(10); //10 columns
        emptyTable.setWidthPercentage(98);   //Width 100%;
        //Set column widths};
        emptyTable.setWidths(invoiceTableColumnWidth);
        PdfPCell emptyCell;
        for(int i=0; i<10; i++){
            emptyCell = new PdfPCell(new Paragraph(" "));
            emptyCell.setBorder(0);
            emptyTable.addCell(emptyCell);
        }


        //summary with broken down to tax
        long distinctValueInTaxList = taxList.stream().distinct().count();
        double totalProductValueSameTax=0;
        double totalVatSameTax =0;
        double totalBruttoSameTax=0;
        String tax = null;
        List<Double> totalProductValueSameTaxList = new ArrayList<>();
        List<Double> totalVatSameTaxList = new ArrayList<>();
        List<Double> totalBruttoSameTaxList = new ArrayList<>();
        List<String> taxValueList = new ArrayList<>();
            List<String> stringsList = new ArrayList<>(taxListWithoutDuplicated);
            for (int r = 0; r < taxListWithoutDuplicated.size(); r++) {
                 if(r != 0){
                     totalProductValueSameTaxList.add(totalProductValueSameTax);
                     totalVatSameTaxList.add(totalVatSameTax);
                     totalBruttoSameTaxList.add(totalVatSameTax+totalProductValueSameTax);
                     taxValueList.add(tax);
                     totalProductValueSameTax=0;
                     totalVatSameTax =0;
                     totalBruttoSameTax=0;
                 }
                String getElement = stringsList.get(r);
                for (InvoiceField row: productTable.getItems()) {
                    if(getElement.equals(row.getTax().getSelectionModel().getSelectedItem())) {
                        totalProductValueSameTax=totalProductValueSameTax+row.getProductValue();
                        totalVatSameTax = totalVatSameTax + row.getPriceVat();
                        totalBruttoSameTax = totalBruttoSameTax+row.getPriceBrutto();
                        tax=row.getTax().getSelectionModel().getSelectedItem();
                    }
                }
            }

            totalProductValueSameTaxList.add(totalProductValueSameTax);
            totalVatSameTaxList.add(totalVatSameTax);
            totalBruttoSameTaxList.add(totalVatSameTax + totalProductValueSameTax);
            taxValueList.add(tax);

        PdfPTable summaryTable = new PdfPTable(10); //10 columns

        summaryTable.setWidthPercentage(98);   //Width 100%;
        summaryTable.setWidths(invoiceTableColumnWidth);

        PdfPCell cell2;

        for (int i = 0; i < 6; i++) {
            cell2 = new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorderColor(BaseColor.WHITE);
            summaryTable.addCell(cell2);
            if(i == 5){
                cell2 = new PdfPCell(new Paragraph("Zestawienie sprzedaży w/g stawek podatku:", new Font(bf, 10)));
                cell2.setColspan(4);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_CENTER);
                cell2.setBorder(0);
                summaryTable.addCell(cell2);
            }
        }
        for (int i = 0; i < 6; i++) {
            cell2 = new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorderColor(BaseColor.WHITE);
            summaryTable.addCell(cell2);
        }
        cell2= new PdfPCell(new Paragraph("Netto",new Font(bf, 10, Font.BOLD)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        cell2= new PdfPCell(new Paragraph("Stawka",new Font(bf, 10, Font.BOLD)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        cell2= new PdfPCell( new Paragraph("Kwota Vat",new Font(bf, 10, Font.BOLD)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        cell2= new PdfPCell(new Paragraph("Brutto",new Font(bf, 10, Font.BOLD)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);


        for(int k=0; k<distinctValueInTaxList; k++) {

            for (int i = 0; i < 6; i++) {
                cell2 = new PdfPCell();
                cell2.setUseVariableBorders(true);
                cell2.setBorderColor(BaseColor.WHITE);
                summaryTable.addCell(cell2);

            }

            cell2 = new PdfPCell(new Paragraph(totalProductValueSameTaxList.get(k) + " zł", new Font(bf, 10)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);

            cell2 = new PdfPCell(new Paragraph(taxValueList.get(k)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);

            cell2 = new PdfPCell(new Paragraph(totalVatSameTaxList.get(k) + " zł", new Font(bf, 10)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);

            cell2 = new PdfPCell(new Paragraph(totalBruttoSameTaxList.get(k) + " zł", new Font(bf, 10)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);
        }

        //add nextRow and hiddern first 8 cell through set border color
        for(int i=0; i<7;i++){
            cell2= new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorderColor(BaseColor.WHITE);
            if(i>5){
                cell2.setUseVariableBorders(true);
                cell2.setBorderColorTop(BaseColor.BLACK);
            }
            summaryTable.addCell(cell2);
            if(i == 6){
                cell2 = new PdfPCell(new Paragraph("Razem do zapłaty:", new Font(bf, 10, Font.BOLD)));
                cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell2.setColspan(2);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                summaryTable.addCell(cell2);
            }
        }

        cell2 = new PdfPCell(new Paragraph(String.valueOf(totalBrutto)+" zł", new Font(bf, 10)));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);


        document.add(table);
        document.add(customerTable);
        document.add(invoiceTable);
        document.add(emptyTable);
        document.add(summaryTable);


    }

}