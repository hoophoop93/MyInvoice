package com.kmichali.utility;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kmichali.model.Customer;
import com.kmichali.model.IdentityCard;
import com.kmichali.model.InvoiceField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.itextpdf.text.html.HtmlTags.FONT;

public class SellAgreement {
    private String PDFPATH;
    Document document;
    public static final String FONT = "C:\\IntellijProject\\MyInvoice\\src\\main\\resources\\fonts\\FreeSans.ttf";


    public SellAgreement(Customer customer,
                         DatePicker datePicker, TableView<InvoiceField> productTable, String path
    ) throws DocumentException, IOException {

        PDFPATH = path.replaceAll("\\\\", "/")+"/Zalaczniki/";
        CreateDirectory(PDFPATH);
        CreateDocument(customer,datePicker,productTable);
        OpenPDF(PDFPATH +"umowa_sprzedazy.pdf");
    }
    public void CreateDirectory(String path){
        File file = new File(path);
        if(!file.exists()){
            if(file.mkdirs()){
                System.out.println("Directory was created");
            }
        }
    }

    private void CreateDocument(Customer customer,
                                DatePicker datePicker,TableView<InvoiceField> productTable
    ) throws DocumentException, IOException {
        document = new Document(PageSize.A4, 5, 5, 20, 20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDFPATH + "umowa_sprzedazy.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        fillPdfDocument(customer,datePicker,productTable);

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

    private void fillPdfDocument(Customer customer,
                                 DatePicker datePicker, TableView<InvoiceField> productTable
    ) throws DocumentException, IOException {
        BaseFont unicodeFont = BaseFont.createFont(FONT,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfPTable mainTable = new PdfPTable(1); //2 columns
        mainTable.setWidthPercentage(98);   //Width 100%;

        PdfPCell cell;
        cell = new PdfPCell(new Paragraph("Umowa dostawy produktów rolnych", new Font(bf, 14,Font.BOLD)));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        for(int i=0; i<1;i++) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(0);
            mainTable.addCell(cell);
        }

        String statementTextPart1="zawarta w dniu "+ datePicker.getValue() +" w Jedliczu pomiędzy:\n" +
                "FHU UNIWERS Krzsztof Pac, Wrocanka 148 , 38-204 Tarnowiec, NIP 685-123-84-20\n zwany dalej KONTRAKTUJĄCYM\n" +
                "a "+customer.getName() +" "+customer.getSurname()+", zam. "+customer.getAddress()+", "+customer.getPostalCode()+"" +
            " "+customer.getCity()+" nr. dowodu osobistego "
                +customer.getIdentityCard().getSeriaAndNumber()+" zwanym dalej PRODUCENTEM";

        cell = new PdfPCell(new Paragraph(statementTextPart1,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setLeading(2f,1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        mainTable.addCell(cell);

        Font f = new Font(unicodeFont, 12);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 1",f));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase2 ="1. Producent oświadcza, że prowadzi gospodarstwo rolne.\n" +
                "2. Producent zobowiązuje się do wypordukowania i dostarczenia Kontraktującemu porduktów rolnych w postaci:\n";

        cell = new PdfPCell(new Paragraph(phrase2,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setLeading(2f,1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase3;
        int counter =1;
        for(InvoiceField row: productTable.getItems()){
            double amount = 0;
            if(row.getUnitMeasure().getSelectionModel().getSelectedItem().equals("tona")){
                amount = row.getAmount();
            }
            phrase3="   "+counter+") "+row.getNameProduct().getSelectionModel().getSelectedItem()+", w terminie do dnia "+ datePicker.getValue().plusDays(31)+
                    ", w ilości "+amount +" t i cenie "+row.getPriceNetto() +" zł NETTO + VAT ustalonej przez strony.";
            cell = new PdfPCell(new Paragraph(phrase3,new Font(bf, 12)));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(0);
            mainTable.addCell(cell);
            counter++;
        }
        String phrase4 ="   Produkty będa odpowiadać kryteriom jakościowym: Wilgotność max 14,5%, zanieczyszczenie masymalne ogółem" +
                " 6%, towar zdrowy jakości handlowej, wolny od żywych szkodników.\n" +
                "3. Kontraktujący zobowiązuje siędo odbioru produktów i zapłaty ceny zgodnie z warunkami niniejszej umowy." +
                "   Kontraktujący ni może odmówić przyjęcia świadczenia częsciowego.\n" +
                "4. Producent oświadcza, że produkty będące przedmiotem umowy pochodzą z prowadzonego przez niego gospodarstawa rolnego" +
                ", o którym mowa w ust. 1, stanowią jego własność, a pondato są wolne od obciążeń.\n" +
                "5. Producent zobowiązuje się powiadomić niezwłocznie Kontraktującego o wszelkich okolicznościach, które mogą mieć " +
                "wpływ na wykonanie umowy.";
        cell = new PdfPCell(new Paragraph(phrase4,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 2"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase5 ="1. Kontraktujący zobowiązuje się do odbioru produktów niezwłocznie po zawiadomieniu go przez Producenta o " +
                "przygotowaniu produktów do dostawy.";
        cell = new PdfPCell(new Paragraph(phrase5,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 3"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);
        String accountNumber;
        if(customer.getAccountNumber() == null){
            accountNumber ="........................................................";
        }else{
            accountNumber = setAccountNumber(customer);
        }
        String phrase6 ="1. Zapłata należności za dostarczone produkty nastąpi przelewem na rachunek bankowy Producenta nr: "+
                accountNumber+" w terminie 14 dni od dnia dostawy. Za dzień zapłaty " +
                "strony przyjmują datę uznania rachunku bankowego Producenta." +
                "\n2. W przypadku niuregulowania przez Kontraktującego należności w terminie Producent będzie naliczał odsetki " +
                "ustawowe za opóźnienie.";
        cell = new PdfPCell(new Paragraph(phrase6,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 4"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase7 ="1. Strona nie będzie ponosić odpowiedzialności za niewykonanie lub nienależyte wykonanie umowy w przypadku " +
                "gdy jest to skutkiem wystąpienia siły wyższej. W przypadku wystąpenia siły wyższej Producent nie będzie również " +
                "zobowiązany do zapłaty kary umownej przewidzianej w niniejszej umowie.\n" +
                "2. Siła wyższa oznacza zdazrenie zewnętrzne w stosunku do strony, którego nie mogła ona przewidzieć ani zapobiec, " +
                "przy zachowaniu należytej staranności.";
        cell = new PdfPCell(new Paragraph(phrase7,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 5"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase8 ="1. Umowa zostaje zawarta na czas oznaczony tj. do dnia "+ datePicker.getValue().plusDays(31) +"\n" +
                "2. Każda ze stron może rozwiązać umowe z zachowaniem miesięcznego terminu wypowiedzenia. Bieg terminu wypowiedzenia " +
                "rozpoczyna się w dniu otrzymiania przez drugą stronę wypowiedzenia w formie pisemnej pod rygorem nieważności.";
        cell = new PdfPCell(new Paragraph(phrase8,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 6"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase9 ="Zmiana niniejszej umowy wymaga formy pisemenej pod rygorem nieważności.";
        cell = new PdfPCell(new Paragraph(phrase9,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 7"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase10 ="Wszelkie poprzednie porozumienia, oświadczenia lub uzgodnienia pomiędzy stronami dotyczące przedmiotu " +
                "umowy zostają zastąpione niniejszą umową.";
        cell = new PdfPCell(new Paragraph(phrase10,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("\u00A7"+" 8"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        mainTable.addCell(cell);

        String phrase11 ="Umowę sporządzono w dwóch jednobrzmiących egzemplarzach, po jednym dla każdej ze stron.";
        cell = new PdfPCell(new Paragraph(phrase10,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        mainTable.addCell(cell);

        for(int i=0; i<1;i++) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(0);
            mainTable.addCell(cell);
        }

        PdfPTable signTable = new PdfPTable(3); //3 columns
        signTable.setWidthPercentage(90);   //Width 100%;
        //Set column widths
        float[] columnWidths6 = {47,6,47};
        signTable.setWidths(columnWidths6);

        PdfPTable sellerSignTable = new PdfPTable(1);
        sellerSignTable.setWidthPercentage(98);
        PdfPCell sellerSignCell;
        sellerSignCell = new PdfPCell(new Paragraph("Kontraktujący",new Font(bf, 12)));
        sellerSignCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        sellerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        sellerSignCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sellerSignCell.setUseVariableBorders(true);
        sellerSignCell.setBorderColorBottom(BaseColor.WHITE);
        sellerSignTable.addCell(sellerSignCell);

        sellerSignCell = new PdfPCell(new Paragraph(" "));
        sellerSignCell.setUseVariableBorders(true);
        sellerSignCell.setBorderColorTop(BaseColor.WHITE);
        sellerSignCell.setMinimumHeight(20);
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
        customerSignCell = new PdfPCell(new Paragraph("Producent",new Font(bf, 12)));
        customerSignCell.setBackgroundColor(BaseColor.LIGHT_GRAY );
        customerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        customerSignCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerSignCell.setUseVariableBorders(true);
        customerSignCell.setBorderColorBottom(BaseColor.WHITE);
        customerSignTable.addCell(customerSignCell);

        customerSignCell = new PdfPCell(new Paragraph(" "));
        customerSignCell.setUseVariableBorders(true);
        customerSignCell.setBorderColorTop(BaseColor.WHITE);
        customerSignCell.setMinimumHeight(20);
        customerSignTable.addCell(customerSignCell);

        PdfPCell signCell3 = new PdfPCell();
        signCell3.addElement(customerSignTable);
        signCell3.setBorder(0);
        signTable.addCell(signCell3);


        document.add(mainTable);
        document.add(signTable);
    }

    public String setAccountNumber(Customer customer) {
        String accountNum = customer.getAccountNumber();
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
