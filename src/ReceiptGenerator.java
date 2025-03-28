import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptGenerator {

    private static final String RECEIPT_DIRECTORY = "Receipts/";
    private static final String LOGO_PATH = "./assets/logo_facture.png";
    private static final String POPPINS_FONT_PATH = "./assets/fonts/Poppins-Regular.ttf";

    public static void generateReceipt(List<MenuItem> items, double total) {
        Document document = new Document(PageSize.A6, 20, 20, 20, 20);

        try {
            new java.io.File(RECEIPT_DIRECTORY).mkdir();

            String receiptId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String filePath = RECEIPT_DIRECTORY + "receipt_" + receiptId + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Register Poppins font
            FontFactory.register(POPPINS_FONT_PATH, "Poppins");

            document.open();

            addHeader(document);
            addDateAndReceiptInfo(document, receiptId);
            addItems(document, items);
            addTotal(document, total);

            // Add thanks message
            document.add(Chunk.NEWLINE);
            Font thanksFont = FontFactory.getFont("Poppins", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10, Font.ITALIC, BaseColor.BLACK);
            Paragraph thanksParagraph = new Paragraph("Thank you for choosing Pizzeria Al Fadl!", thanksFont);
            thanksParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(thanksParagraph);

            document.close();

            System.out.println("Receipt generated successfully. File saved at: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addHeader(Document document) {
        try {
            // Create a table to hold the logo, title, and pizzeria name
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(80);
            headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Add logo
            Image logo = Image.getInstance(LOGO_PATH);
            logo.scaleAbsolute(140, 140); // Set the size of the logo
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Center the logo horizontally
            headerTable.addCell(logoCell);

            // Add the header table to the document
            document.add(headerTable);

            document.add(Chunk.NEWLINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addDateAndReceiptInfo(Document document, String receiptId) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        Font infoFont = FontFactory.getFont("Poppins", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10, Font.NORMAL, BaseColor.BLACK);

        document.add(new Paragraph("Date: " + currentDate, infoFont));
        document.add(new Paragraph("Receipt ID: " + receiptId, infoFont));
        document.add(new Paragraph("Cashier: " + LoginPanel.USERNAME, infoFont));
        document.add(Chunk.NEWLINE);
    }

    private static void addItems(Document document, List<MenuItem> items) {
        try {
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            // Set column widths
            float[] columnWidths = {2, 1, 1};
            table.setWidths(columnWidths);

            // Table headers
            Font headerFont =  FontFactory.getFont("Poppins", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
            PdfPCell itemHeaderCell = new PdfPCell(new Phrase("Item", headerFont));
            PdfPCell quantityHeaderCell = new PdfPCell(new Phrase("Quantity", headerFont));
            PdfPCell priceHeaderCell = new PdfPCell(new Phrase("Price", headerFont));

            // Set header cell styles
            itemHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            quantityHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            priceHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Add headers to the table
            table.addCell(itemHeaderCell);
            table.addCell(quantityHeaderCell);
            table.addCell(priceHeaderCell);

            // Create a temporary list to store unique items
            List<MenuItem> uniqueItems = getUniqueItems(items);

            // Table content
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

            for (MenuItem item : uniqueItems) {
                int totalQuantity = getTotalQuantity(items, item);
                PdfPCell itemNameCell = new PdfPCell(new Phrase(item.getName(), contentFont));
                PdfPCell quantityCell = new PdfPCell(new Phrase(String.valueOf(totalQuantity), contentFont));
                PdfPCell priceCell = new PdfPCell(new Phrase(String.format("$%.2f", item.getPrice() * totalQuantity), contentFont));

                // Add cells to the table
                table.addCell(itemNameCell);
                table.addCell(quantityCell);
                table.addCell(priceCell);
            }

            // Add the table to the document
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<MenuItem> getUniqueItems(List<MenuItem> items) {
        // Create a temporary list to store unique items
        List<MenuItem> uniqueItems = new ArrayList<>();

        // Iterate through the items and add unique items to the list
        for (MenuItem item : items) {
            if (!uniqueItems.contains(item)) {
                uniqueItems.add(item);
            }
        }

        return uniqueItems;
    }

    private static int getTotalQuantity(List<MenuItem> items, MenuItem targetItem) {
        // Calculate the total quantity of a specific item in the order
        int totalQuantity = 0;

        for (MenuItem item : items) {
            if (item.equals(targetItem)) {
                totalQuantity++;
            }
        }

        return totalQuantity;
    }

    private static void addTotal(Document document, double total) throws Exception {
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Total: " + String.format("%.2f$", total), totalFont));
    }
}
