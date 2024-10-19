package com.mindhub.homebanking.controllers;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PDFExportController {

//    private final PDFGeneratorService pdfGeneratorService;

    @Autowired
    PDFGeneratorService pdfGeneratorService;

    public PDFExportController(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Autowired
    ClientService clientService;

/*
    @GetMapping("/pdf/generate")
    public void generatePDF(HttpServletResponse response, Authentication authentication, @RequestParam String number,
                            @RequestParam String dateFrom, @RequestParam String dateTo) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        String email = authentication.getName();
        LocalDate dateFrom1 = LocalDate.parse(dateFrom);
        LocalDate dateTo1 = LocalDate.parse(dateTo);

        this.pdfGeneratorService.export(response, email, number, dateFrom1, dateTo1);
    }*/
}
