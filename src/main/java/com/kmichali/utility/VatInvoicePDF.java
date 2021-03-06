package com.kmichali.utility;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kmichali.model.*;
import com.kmichali.model.Date;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import pl.allegro.finance.tradukisto.MoneyConverters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.*;
import java.util.List;


public class VatInvoicePDF {

    private static DecimalFormat df2 = new DecimalFormat("#,##0.00");
    private String PDFPATH;
    public  String IMG1 = "/images/grain.png";
    Document document;
    String invoiceNum;

    public VatInvoicePDF(Invoice invoice, Date date, Company companySeller,Company companyCustomer,
                         TableView<InvoiceField> productTable, ComboBox paidType,String invoiceNumber, String path) throws DocumentException, IOException {
        PDFPATH = path.replaceAll("\\\\", "/")+"/VAT/";
        CreateDirectory(PDFPATH,invoiceNumber);
        CreateDocument(invoice ,date,companySeller,companyCustomer,productTable,paidType,invoiceNumber);
        OpenPDF(PDFPATH+invoiceNum+".pdf");
    }
    public void CreateDirectory(String path, String invoiceNumber){
        String yearWithInvoiceNumber;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearWithInvoiceNumber = Integer.toString(year);
//        if(invoiceNumber.length() >= 10)yearWithInvoiceNumber = invoiceNumber.substring(6,10);
//        else yearWithInvoiceNumber = invoiceNumber.substring(5,9);
        File file = new File(path+yearWithInvoiceNumber);
        if(!file.exists()){
            if(file.mkdirs()){
                System.out.println("Directory was created");
            }
        }
//        if(invoiceNumber.length() >= 10) invoiceNumber = invoiceNumber.substring(3,5);
//        else invoiceNumber = invoiceNumber.substring(2, 4);
//        String tmpInvoiceNumber = invoiceNumber.substring(0,1);
//        if(tmpInvoiceNumber.equals("0")){
//            invoiceNumber = invoiceNumber.substring(1,2);
//        }
//        else {
//            invoiceNumber = invoiceNumber.substring(0,2);
//        }
        int month = Calendar.getInstance().get(Calendar.MONTH);
        Month monthEnum = Month.values()[month];

        File file2 = new File(PDFPATH+yearWithInvoiceNumber+"/"+monthEnum);
        if(!file2.exists()){
            if(file2.mkdir()){
                System.out.println("Directory was created");
            }
        }
        PDFPATH += yearWithInvoiceNumber+"/"+monthEnum+"/";
    }
    private void CreateDocument(Invoice invoice, Date date, Company companySeller,Company companyCustomer,
                                TableView<InvoiceField> productTable, ComboBox paidType,String invoiceNumber) throws DocumentException, IOException {

        invoiceNum = invoiceNumber.replaceAll("/","-");
        document = new Document(PageSize.A4,5,5,20,20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDFPATH+ invoiceNum+".pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        fillPdfDocument(invoice ,date,companySeller,companyCustomer,productTable,paidType);

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

    private void fillPdfDocument(Invoice invoice, Date date, Company companySeller, Company companyCustomer,
                                 TableView<InvoiceField> productTable, ComboBox paidType) throws DocumentException, IOException {

        List<String> taxList = new ArrayList<>();
        HashSet<String> taxListWithoutDuplicated = new HashSet(); //list no duplicated
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfPTable table = new PdfPTable(3); //3 columns
        table.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] columnWidths = {30,50,20};
        table.setWidths(columnWidths);

        Paragraph paragraph2 = new Paragraph("FAKTURA VAT "+invoice.getInvoiceNumber(),new Font(bf, 16, Font.BOLD));
        paragraph2.add(Chunk.NEWLINE);
        paragraph2.setLeading(32);
        paragraph2.setFont(new Font(bf,12));
        PdfPCell invoiceNameCell = new PdfPCell(paragraph2);
        invoiceNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceNameCell.setVerticalAlignment(Element.ALIGN_TOP);
        invoiceNameCell.setBorder(0);

        PdfPTable dateTable = new PdfPTable(1); //3 columns
        dateTable.setWidthPercentage(100);   //Width 100%;

        PdfPCell cellDate;

        cellDate = new PdfPCell(new Paragraph("Miejsce wystawienia",new Font(bf, 11, Font.BOLD)));
        cellDate.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        cellDate = new PdfPCell(new Paragraph("Wrocanka",new Font(bf, 11)));
        cellDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

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
        cellDate.setBorder(0);
        dateTable.addCell(cellDate);

        PdfPCell dateCell = new PdfPCell(dateTable);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setBorder(0);

        table.addCell(createImageCell(IMG1));
        table.addCell(invoiceNameCell);
        table.addCell(dateCell);

        //add empty row
        PdfPTable emptyTable = new PdfPTable(1); //10 columns
        emptyTable.setWidthPercentage(98);   //Width 100%;
        //Set column widths};
        PdfPCell emptyCell;
        emptyCell = new PdfPCell(new Paragraph(" "));
        emptyCell.setBorder(0);
        emptyTable.addCell(emptyCell);

        PdfPTable customerTable = new PdfPTable(3); //2 columns
        customerTable.setWidthPercentage(98);   //Width 100%;
        float[] columnWidths2 = {47,6,47};
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
        cellNameSeller.setBorderColorLeft(BaseColor.LIGHT_GRAY);
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
            paragraph4.add(companySeller.getSeller().getPostalCode()+" "+companySeller.getSeller().getCity()+", Tel. "+companySeller.getPhoneNumber());
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
        cellNameSeller.setBorderColorRight(BaseColor.LIGHT_GRAY);
        nameCustomerCell.setBorderColor(BaseColor.WHITE);
        nameCustomerTable.addCell(nameCustomerCell);


        Paragraph paragraph5 = new Paragraph();
        paragraph5.setLeading(2f);
        paragraph5.setFont(new Font(bf,11));
        if(companyCustomer != null) {
            if(!companyCustomer.getName().equals("")) {
                paragraph5.add(companyCustomer.getName());
                paragraph5.add(Chunk.NEWLINE);
            }
            if(companyCustomer.getCustomer().getName()!=null) {
                paragraph5.add(companyCustomer.getCustomer().getName() + " " + companyCustomer.getCustomer().getSurname());
                paragraph5.add(Chunk.NEWLINE);
            }
            paragraph5.add(companyCustomer.getCustomer().getAddress());
            paragraph5.add(Chunk.NEWLINE);
            paragraph5.add(companyCustomer.getCustomer().getPostalCode()+" "+companyCustomer.getCustomer().getCity());
            paragraph5.add(Chunk.NEWLINE);
            paragraph5.add("NIP: " + companyCustomer.getNip());

            if(companyCustomer.getCustomer().getName() != null && !companyCustomer.getName().equals("")) {
                for (int i = 0; i < 2; i++) {
                    paragraph5.add(Chunk.NEWLINE);
                }
            }else{
                for (int i = 0; i < 3; i++) {
                    paragraph5.add(Chunk.NEWLINE);
                }
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
        PdfPTable invoiceTable = new PdfPTable(9); //10 columns
        invoiceTable.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] invoiceTableColumnWidth = {4,23,9,9,11,11,11,11,11};
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

        cell= new PdfPCell(new Paragraph("J.m",new Font(bf, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell= new PdfPCell(new Paragraph(new Paragraph("Ilość",new Font(bf, 10, Font.BOLD))));
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
            cell= new PdfPCell(new Paragraph(row.getNameProduct().getEditor().getText(),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getUnitMeasure().getSelectionModel().getSelectedItem()),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(df2.format(row.getAmount()).replaceAll(",",".")),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(df2.format(row.getPriceNetto()).replaceAll(",",".")),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(df2.format(row.getProductValue()).replaceAll(",",".")),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(row.getTax().getSelectionModel().getSelectedItem()),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(df2.format(row.getPriceVat()).replaceAll(",",".")),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
            cell= new PdfPCell(new Paragraph(String.valueOf(df2.format(row.getPriceBrutto()).replaceAll(",",".")),new Font(bf, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceTable.addCell(cell);
        }

        //add nextRow and hiddern first 6 cell through set border color
        for(int i=0; i<4;i++){
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
        totalBrutto = round(totalBrutto,2);
        totalVat = round(totalVat,2);
        totalProductValue = round(totalProductValue,2);
        cell = new PdfPCell(new Paragraph("Razem:", new Font(bf, 10, Font.BOLD)));
        cell.setUseVariableBorders(true);
        cell.setBorderColorBottom(BaseColor.WHITE);
        cell.setBorderColorLeft(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(String.valueOf(df2.format(totalProductValue)).replaceAll(",",".")+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(" "));
        cell.setUseVariableBorders(true);
        cell.setBorderColorBottom(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(String.valueOf(df2.format(totalVat)).replaceAll(",",".")+" zł", new Font(bf, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(String.valueOf(df2.format(totalBrutto)).replaceAll(",",".")+" zł",  new Font(bf, 10, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.addCell(cell);

//        //add empty row
//        PdfPTable emptyTable = new PdfPTable(1); //10 columns
//        emptyTable.setWidthPercentage(98);   //Width 100%;
//        //Set column widths};
//        PdfPCell emptyCell;
//            emptyCell = new PdfPCell(new Paragraph(" "));
//            emptyCell.setBorder(0);
//            emptyTable.addCell(emptyCell);

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
                     totalProductValueSameTaxList.add(round(totalProductValueSameTax,2));
                     totalVatSameTaxList.add(round(totalVatSameTax,2));
                     totalBruttoSameTaxList.add(round(totalVatSameTax+totalProductValueSameTax,2));
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

        totalProductValueSameTaxList.add(round(totalProductValueSameTax,2));
        totalVatSameTaxList.add(round(totalVatSameTax,2));
        totalBruttoSameTaxList.add(round((totalVatSameTax+totalProductValueSameTax),2));
        taxValueList.add(tax);

        PdfPTable summaryTable = new PdfPTable(9); //10 columns

        summaryTable.setWidthPercentage(98);   //Width 100%;
        summaryTable.setWidths(invoiceTableColumnWidth);

        PdfPCell cell2;

        for (int i = 0; i < 5; i++) {
            cell2 = new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorderColor(BaseColor.WHITE);
            summaryTable.addCell(cell2);
            if(i == 4){
                cell2 = new PdfPCell(new Paragraph("Zestawienie sprzedaży w/g stawek podatku:", new Font(bf, 11)));
                cell2.setColspan(4);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_CENTER);
                cell2.setBorder(0);
                summaryTable.addCell(cell2);
            }
        }
        for (int i = 0; i < 5; i++) {
            cell2 = new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorderColor(BaseColor.WHITE);
            summaryTable.addCell(cell2);
        }
        cell2= new PdfPCell(new Paragraph("Netto",new Font(bf, 10)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        cell2= new PdfPCell(new Paragraph("Stawka",new Font(bf, 10)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        cell2= new PdfPCell( new Paragraph("Kwota Vat",new Font(bf, 10)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        cell2= new PdfPCell(new Paragraph("Brutto",new Font(bf, 10)));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY );
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);


        for(int k=0; k<distinctValueInTaxList; k++) {

            for (int i = 0; i < 5; i++) {
                cell2 = new PdfPCell();
                cell2.setUseVariableBorders(true);
                cell2.setBorderColor(BaseColor.WHITE);
                summaryTable.addCell(cell2);

            }

            cell2 = new PdfPCell(new Paragraph(df2.format(totalProductValueSameTaxList.get(k)).
                    replaceAll(",",".") + " zł", new Font(bf, 10)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);

            cell2 = new PdfPCell(new Paragraph(taxValueList.get(k)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);

            cell2 = new PdfPCell(new Paragraph(df2.format(totalVatSameTaxList.get(k)).
                    replaceAll(",",".") + " zł", new Font(bf, 10)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);

            cell2 = new PdfPCell(new Paragraph(df2.format(totalBruttoSameTaxList.get(k)).replaceAll(",",".") + " zł", new Font(bf, 10)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(cell2);
        }

        //add nextRow and hiddern first 8 cell through set border color
        for(int i=0; i<6;i++){
            cell2= new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorderColor(BaseColor.WHITE);
            if(i>4){
                cell2.setUseVariableBorders(true);
                cell2.setBorderColorTop(BaseColor.BLACK);
            }
            summaryTable.addCell(cell2);
            if(i == 5){
                cell2 = new PdfPCell(new Paragraph("Razem:", new Font(bf, 11)));
                cell2.setColspan(2);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                summaryTable.addCell(cell2);
            }
        }

        cell2 = new PdfPCell(new Paragraph(String.valueOf(df2.format(totalBrutto)).replaceAll(",",".")+" zł", new Font(bf, 11)));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        summaryTable.addCell(cell2);

        PdfPTable mainSummaryTable = new PdfPTable(3); //3 columns
        mainSummaryTable.setWidthPercentage(98);   //Width 100%;
        //Set column widths
        float[] columnWidths3 = {47,6,47};
        mainSummaryTable.setWidths(columnWidths3);

        PdfPTable paymentTable = new PdfPTable(2); //3 columns
        paymentTable.setWidthPercentage(100);   //Width 100%;
        //Set column widths
        float[] columnWidths4 = {50,50};
        paymentTable.setWidths(columnWidths4);
        PdfPCell paymentCell;
        paymentCell = new PdfPCell(new Paragraph("Sposób zapłaty:",new Font(bf, 12)));
        paymentCell.setUseVariableBorders(true);
        paymentCell.setBorderColorRight(BaseColor.WHITE);
        paymentCell.setBorderColorLeft(BaseColor.WHITE);
        paymentCell.setBorderColorBottom(BaseColor.WHITE);
        paymentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentTable.addCell(paymentCell);

        paymentCell = new PdfPCell(new Paragraph(paidType.getSelectionModel().getSelectedItem().toString(),new Font(bf, 12)));
        paymentCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentCell.setUseVariableBorders(true);
        paymentCell.setBorderColorRight(BaseColor.WHITE);
        paymentCell.setBorderColorLeft(BaseColor.WHITE);
        paymentCell.setBorderColorBottom(BaseColor.WHITE);
        paymentTable.addCell(paymentCell);

        paymentCell = new PdfPCell(new Paragraph("Termin zapłaty:",new Font(bf, 12)));
        paymentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentCell.setUseVariableBorders(true);
        paymentCell.setBorderColorRight(BaseColor.WHITE);
        paymentCell.setBorderColorLeft(BaseColor.WHITE);
        paymentCell.setBorderColorBottom(BaseColor.WHITE);
        paymentTable.addCell(paymentCell);

        paymentCell = new PdfPCell(new Paragraph(date.getPaymentDate(),new Font(bf, 12)));
        paymentCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paymentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentCell.setUseVariableBorders(true);
        paymentCell.setBorderColorRight(BaseColor.WHITE);
        paymentCell.setBorderColorLeft(BaseColor.WHITE);
        paymentCell.setBorderColorBottom(BaseColor.WHITE);
        paymentTable.addCell(paymentCell);


        paymentCell = new PdfPCell(new Paragraph("Numer konta:",new Font(bf, 12)));
        paymentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentCell.setUseVariableBorders(true);
        paymentCell.setBorderColorRight(BaseColor.WHITE);
        paymentCell.setBorderColorLeft(BaseColor.WHITE);
        paymentCell.setBorderColorBottom(BaseColor.WHITE);
        paymentCell.setColspan(2);
        paymentTable.addCell(paymentCell);

        paymentCell = new PdfPCell(new Paragraph(setAccountNumber(companySeller.getAccountNumber()),new Font(bf, 12)));
        paymentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentCell.setUseVariableBorders(true);
        paymentCell.setBorderColorRight(BaseColor.WHITE);
        paymentCell.setBorderColorLeft(BaseColor.WHITE);
        paymentCell.setBorderColorBottom(BaseColor.WHITE);
        paymentCell.setColspan(2);
        paymentTable.addCell(paymentCell);


        PdfPCell mainCell1 = new PdfPCell();
        mainCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        mainCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        mainCell1.setBorder(0);
        mainCell1.addElement(paymentTable);
        mainSummaryTable.addCell(mainCell1);

        PdfPCell mainCell2 = new PdfPCell();
        mainCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        mainCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        mainCell2.setBorder(0);
        mainSummaryTable.addCell(mainCell2);

        PdfPTable finalPriceTable = new PdfPTable(2); //3 columns
        finalPriceTable.setWidthPercentage(100);   //Width 100%;
        //Set column widths
        float[] columnWidths5 = {60,40};
        finalPriceTable.setWidths(columnWidths5);
        PdfPCell finalPriceCell;
        finalPriceCell = new PdfPCell(new Paragraph("Razem do zapłaty:",new Font(bf, 14, Font.BOLD)));
        finalPriceCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        finalPriceCell.setUseVariableBorders(true);
        finalPriceCell.setBorderColorRight(BaseColor.WHITE);
        finalPriceCell.setBorderColorLeft(BaseColor.WHITE);
        finalPriceCell.setBorderColorBottom(BaseColor.WHITE);
        finalPriceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        finalPriceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        finalPriceTable.addCell(finalPriceCell);

        finalPriceCell = new PdfPCell(new Paragraph(String.valueOf(df2.format(totalBrutto)).replaceAll(",",".")+" zł",new Font(bf, 14, Font.BOLD)));
        finalPriceCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        finalPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        finalPriceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        finalPriceCell.setUseVariableBorders(true);
        finalPriceCell.setBorderColorRight(BaseColor.WHITE);
        finalPriceCell.setBorderColorLeft(BaseColor.WHITE);
        finalPriceCell.setBorderColorBottom(BaseColor.WHITE);
        finalPriceTable.addCell(finalPriceCell);

        MoneyConverters converter = MoneyConverters.POLISH_BANKING_MONEY_VALUE;
        String moneyAsWords  = converter.asWords(new BigDecimal(Double.toString(totalBrutto)));

        finalPriceCell = new PdfPCell(new Paragraph("Słownie: "+moneyAsWords,new Font(bf, 9)));
        finalPriceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        finalPriceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        finalPriceCell.setUseVariableBorders(true);
        finalPriceCell.setBorderColorRight(BaseColor.WHITE);
        finalPriceCell.setBorderColorLeft(BaseColor.WHITE);
        finalPriceCell.setBorderColorBottom(BaseColor.WHITE);
        finalPriceCell.setColspan(2);
        finalPriceTable.addCell(finalPriceCell);


        PdfPCell mainCell3 = new PdfPCell();
        mainCell3.addElement(finalPriceTable);
        mainCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        mainCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        mainCell3.setBorder(0);
        mainSummaryTable.addCell(mainCell3);

        PdfPTable emptyTable2 = new PdfPTable(1);
        emptyTable2.setWidthPercentage(98);
        PdfPCell emptyCell2;
        for(int i =0; i<2; i++) {
            emptyCell2 = new PdfPCell(new Paragraph(" "));
            emptyCell2.setBorder(0);
            emptyTable2.addCell(emptyCell2);
        }

        PdfPTable signTable = new PdfPTable(3); //3 columns
        table.setWidthPercentage(90);   //Width 100%;
        //Set column widths
        float[] columnWidths6 = {47,6,47};
        signTable.setWidths(columnWidths6);

        PdfPTable sellerSignTable = new PdfPTable(1);
        sellerSignTable.setWidthPercentage(98);
        PdfPCell sellerSignCell;
        sellerSignCell = new PdfPCell(new Paragraph("Wystawił(a)",new Font(bf, 12)));
        sellerSignCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        sellerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        sellerSignCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sellerSignCell.setUseVariableBorders(true);
        sellerSignCell.setBorderColorBottom(BaseColor.WHITE);
        sellerSignTable.addCell(sellerSignCell);

        sellerSignCell = new PdfPCell(new Paragraph(" "));
        sellerSignCell.setUseVariableBorders(true);
        sellerSignCell.setBorderColorTop(BaseColor.WHITE);
        sellerSignCell.setMinimumHeight(70);
        sellerSignTable.addCell(sellerSignCell);


        PdfPCell signCell1 = new PdfPCell();
        signCell1.addElement(sellerSignTable);
        signCell1.setBorder(0);
        signTable.addCell(signCell1);

        PdfPCell signCell2 = new PdfPCell();
        signCell2.setBorder(0);
        signTable.addCell(signCell2);


        PdfPTable customerSignTable = new PdfPTable(1);
        customerSignTable.setWidthPercentage(98);
        PdfPCell customerSignCell;
        customerSignCell = new PdfPCell(new Paragraph("Odebrał(a)",new Font(bf, 12)));
        customerSignCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        customerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        customerSignCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerSignCell.setUseVariableBorders(true);
        customerSignCell.setBorderColorBottom(BaseColor.WHITE);
        customerSignTable.addCell(customerSignCell);

        customerSignCell = new PdfPCell(new Paragraph(" "));
        customerSignCell.setUseVariableBorders(true);
        customerSignCell.setBorderColorTop(BaseColor.WHITE);
        customerSignCell.setMinimumHeight(70);
        customerSignTable.addCell(customerSignCell);

        PdfPCell signCell3 = new PdfPCell();
        signCell3.addElement(customerSignTable);
        signCell3.setBorder(0);
        signTable.addCell(signCell3);


        document.add(table);
        document.add(emptyTable);
        document.add(customerTable);
        document.add(invoiceTable);
        document.add(emptyTable);
        document.add(summaryTable);
        document.add(emptyTable);
        document.add(mainSummaryTable);
        document.add(emptyTable2);
        document.add(signTable);


    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(VatInvoicePDF.class.getResource(path));
        System.out.println(VatInvoicePDF.class.getResource(path));
        PdfPCell cell = new PdfPCell(img, true);
        cell.setMinimumHeight(60);
        cell.setBorder(0);
        return cell;
    }
    public String setAccountNumber(String string ){
        String accountNum = string;
        String numberAccount = "";
        int counter2 = 0;
        for (int i = 0; i < accountNum.length(); i++) {

            numberAccount += accountNum.charAt(i);

            if (i == 1) numberAccount += " ";
            if (i > 1) counter2++;
            if (counter2 == 4) {
                numberAccount += " ";
                counter2 = 0;
            }
        }
        return numberAccount;
    }
}