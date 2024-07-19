package it.unisalento.rec.rec_email.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import it.unisalento.rec.rec_email.dto.EmailBillDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {
    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    public byte[] generateInvoice(EmailBillDTO emailBillDTO) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            ClassPathResource imageResource = new ClassPathResource("logo.png");
            Image logo = new Image(ImageDataFactory.create(imageResource.getURL()));
            logo.scaleToFit(100, 100);
            document.add(logo);

            Paragraph companyName = new Paragraph("RecSystem")
                    .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(0, 102, 204)) // Colore blu
                    .setMarginBottom(10);
            document.add(companyName);

            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            Cell clientInfoCell = new Cell().add(new Paragraph("Billed To:\n" + emailBillDTO.getClientEmail()))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(null);
            headerTable.addCell(clientInfoCell);

            Cell companyInfoCell = new Cell().add(new Paragraph("RecSystem\n123 Business Road\nBusiness City, BC 12345\nsupport@recsystem.com\n+1 (123) 456-7890"))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(null);
            headerTable.addCell(companyInfoCell);

            document.add(headerTable);

            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            infoTable.addCell(new Cell().add(new Paragraph("Invoice Number:").setBold()).setBackgroundColor(new DeviceRgb(0, 102, 204)).setFontColor(ColorConstants.WHITE));
            infoTable.addCell(new Cell().add(new Paragraph("INV-" + System.currentTimeMillis())).setTextAlignment(TextAlignment.RIGHT));
            infoTable.addCell(new Cell().add(new Paragraph("Invoice Date:").setBold()).setBackgroundColor(new DeviceRgb(0, 102, 204)).setFontColor(ColorConstants.WHITE));
            infoTable.addCell(new Cell().add(new Paragraph(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))).setTextAlignment(TextAlignment.RIGHT));
            infoTable.addCell(new Cell().add(new Paragraph("Task Name:").setBold()).setBackgroundColor(new DeviceRgb(0, 102, 204)).setFontColor(ColorConstants.WHITE));
            infoTable.addCell(new Cell().add(new Paragraph(emailBillDTO.getName())).setTextAlignment(TextAlignment.RIGHT));
            infoTable.addCell(new Cell().add(new Paragraph("Description:").setBold()).setBackgroundColor(new DeviceRgb(0, 102, 204)).setFontColor(ColorConstants.WHITE));
            infoTable.addCell(new Cell().add(new Paragraph(emailBillDTO.getDescription())).setTextAlignment(TextAlignment.RIGHT));
            infoTable.addCell(new Cell().add(new Paragraph("Total Amount:").setBold()).setBackgroundColor(new DeviceRgb(0, 102, 204)).setFontColor(ColorConstants.WHITE));
            infoTable.addCell(new Cell().add(new Paragraph("$" + emailBillDTO.getCost())).setTextAlignment(TextAlignment.RIGHT));

            document.add(infoTable);

            Table itemTable = new Table(UnitValue.createPercentArray(new float[]{4, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            itemTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()).setBackgroundColor(new DeviceRgb(204, 204, 255)));
            itemTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()).setBackgroundColor(new DeviceRgb(204, 204, 255)));
            itemTable.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()).setBackgroundColor(new DeviceRgb(204, 204, 255)));

            itemTable.addCell(new Cell().add(new Paragraph(emailBillDTO.getDescription())));
            itemTable.addCell(new Cell().add(new Paragraph("1")).setTextAlignment(TextAlignment.RIGHT));
            itemTable.addCell(new Cell().add(new Paragraph("$" + emailBillDTO.getCost())).setTextAlignment(TextAlignment.RIGHT));

            document.add(itemTable);

            Paragraph footer = new Paragraph("Thank you for your business!\nIf you have any questions, please contact us at support@recsystem.com")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY)
                    .setMarginTop(20)
                    .setBackgroundColor(new DeviceRgb(204, 255, 204));
            document.add(footer);

            Paragraph greenEnergyMessage = new Paragraph("We appreciate your commitment to a greener planet! Thank you for choosing green energy for your computational tasks.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GREEN)
                    .setMarginTop(10);
            document.add(greenEnergyMessage);

        } catch (IOException e) {
            logger.error("Errore durante la generazione del PDF: {}", e.getMessage());
            throw e;
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
}








