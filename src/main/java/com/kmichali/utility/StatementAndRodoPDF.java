package com.kmichali.utility;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
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

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StatementAndRodoPDF {
    private String PDFPATH = "C:/Users/Kamil/Desktop/pdf/";
    Document document;


    public StatementAndRodoPDF(Customer customer, ComboBox veterynaryInspectoreCB,
                               DatePicker datePicker, TableView<InvoiceField> productTable, String path, IdentityCard identityCard)
            throws DocumentException, IOException {
        PDFPATH = path.replaceAll("\\\\", "/")+"/Zalaczniki/";
        CreateDirectory(PDFPATH);
        CreateDocument(customer,veterynaryInspectoreCB,datePicker,productTable,identityCard);
        OpenPDF(PDFPATH +"oswiadczenie_RODO.pdf");
    }
    public void CreateDirectory(String path){
        File file = new File(path);
        if(!file.exists()){
            if(file.mkdirs()){
                System.out.println("Directory was created");
            }
        }
    }

    private void CreateDocument(Customer customer, ComboBox veterynaryInspectoreCB,
                                DatePicker datePicker,TableView<InvoiceField> productTable,IdentityCard identityCard
    ) throws DocumentException, IOException {
        document = new Document(PageSize.A4, 7, 7, 20, 20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDFPATH + "oswiadczenie_RODO.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        fillPdfDocument(customer,veterynaryInspectoreCB,datePicker,productTable,identityCard);

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
                                   DatePicker datePicker,TableView<InvoiceField> productTable,IdentityCard identityCard
    ) throws DocumentException, IOException {

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
        String statementTextPart1="Oświadczam, że jestem zarejestrowanym w Państowym Inspktoracie Weterynarii w " +veterynaryInspectoreCB.getEditor().getText()+
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
             statementTextPart2 = "Oświadczam jednocześnie, że dostarczony przeze mnie w dniu " + datePicker.getValue() + " towar " + row.getNameProduct().getEditor().getText() + " został wyprodukowany, przetoworzony i dystrybuowany zgodnie" +
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

        cell = new PdfPCell(new Paragraph("   "+identityCard.getSeriaAndNumber(),new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        signTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("......................................................." +
                "..................................",new Font(bf, 9)));
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
        for(int i =0; i<4; i++) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(0);
            signTable.addCell(cell);
        }

        PdfPTable rodoTable = new PdfPTable(1); //2 columns
        rodoTable.setWidthPercentage(98);   //Width 100%;

        String rodoTitle="Informacja dla strony umowy o administratorze (RODO)";
        cell = new PdfPCell(new Paragraph(rodoTitle,new Font(bf, 14,Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setLeading(2f,1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        rodoTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(" "));
        cell.setBorder(0);
        rodoTable.addCell(cell);


        String rodoText="1. Administratorem danych jest Krzysztof Pac - FHU UNIWERS Krzysztof Pac, 38-204 Tarnowiec, Wrocanka 148," +
                " NIP: 685-123-84-20\n" +
                "2. Administrator prowadzi operacje przetwarzania danych osobowych Producenta niezbędnych do wykonania umowy," +
                " której stroną jest osoba, której dane dotyczą lub do podjęcia działań na żądania osoby," +
                " której dane dotyczą, przed zawarciem umowy.\n" +
                "3. Podstawą przetwarzania danych osobowych jest umowa - art. 6 ust. 1 lit b RODO.\n" +
                "4. Producent ma posiada prawo:\n" +
                "-żądania od Administratora dostępu do swoich danych, ich sprostowania, usunięcia lub ograniczenia przetwarzania danych" +
                "osobowych\n" +
                "-wniesienia sprzeciwu wobce takiego przetwarzania,\n" +
                "-przenoszenia danych,\n" +
                "-wniesienia skargi do organu nadzorczego,\n" +
                "-cofnięcia zgody na przetwarzanie danych osobowych.\n" +
                "5. Dane osobowe Producenta nie podlegają zautomatyzowaniu podejmowaniu decyzji, w tym profilowaniu.\n" +
                "6. Dane osobowe wynikające z zawarcia ninijszej umowy będą przetwarzane przez okres, w którym moga ujawnić się " +
                "roszczenia związane z tą umową, czyli przez 5+1 lat od końca roku, w którym wygasła umowa, w tym 6 lat, to najdłuższy " +
                "możliwy okres przedawnienia roszczeń. Dodatkowy rok jest na wypadek roszczeń zgłoszonych w ostaniej chwili i " +
                "problemtów z doręczeniem, a liczenie od końca roku służy określeniu jednej daty usunięcia danych dla umów kończących sie " +
                "w danym roku.\n" +
                "7. Odbiorcami danych osobowych Producenta będą te podmioty, którym mamy obowiązek przekazywania dane na gruncie obowiązujących" +
                " przepisów prawa, a także podmioty świadczące na Naszą rzecz usługi podlgejace na dostarczaniu przesyłek i poczty, biuro rachunkowe, banki.";
        cell = new PdfPCell(new Paragraph(rodoText,new Font(bf, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setLeading(2f,1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        rodoTable.addCell(cell);

        for(int i =0; i<2; i++) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(0);
            rodoTable.addCell(cell);
        }

        PdfPTable signRodoTable = new PdfPTable(2); //2 columns
        signTable.setWidthPercentage(98);   //Width 100%;
        float[] columnWidths3= {60,40};
        signRodoTable.setWidths(columnWidths3);


        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        signRodoTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("......................................................." +
                ".................",new Font(bf, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(0);
        signRodoTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(""));
        cell.setBorder(0);
        signRodoTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("podpis",new Font(bf, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(0);
        signRodoTable.addCell(cell);

        document.add(customerTable);
        document.add(mainTable);
        document.add(signTable);
        document.add(rodoTable);
        document.add(signRodoTable);
    }
}
