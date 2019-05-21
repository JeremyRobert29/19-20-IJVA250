package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.service.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlleur pour réaliser les exports.
 */
@Controller
@RequestMapping("/")
public class ExportController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        PrintWriter writer = response.getWriter();
        List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();
        writer.println("Id;Nom;Prenom;Date de Naissance");

        for (Client client : allClients) {
            writer.println(client.getId() +";"
                    + client.getNom() +";"
                    + client.getPrenom() +";"
                    + client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));

        }

    }


    @GetMapping("/clients/xlsx")
    public void ClientXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");

        List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("clients");
        Row headerRow = sheet.createRow(0);

        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("Id");

        Cell cellNom = headerRow.createCell(1);
        cellNom.setCellValue("Nom");

        Cell cellPrenom = headerRow.createCell(2);
        cellPrenom.setCellValue("Prenom");

        int iRow = 1;
        for (Client client : allClients) {
            Row row = sheet.createRow(iRow);

            Cell id = row.createCell(0);
            id.setCellValue(client.getId());

            Cell nom = row.createCell(1);
            nom.setCellValue(client.getNom());

            Cell prenom = row.createCell(2);
            prenom.setCellValue(client.getPrenom());

            iRow = iRow + 1;
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @Autowired
    private FactureService factureService;

    private LigneFacture ligneFacture;


    @GetMapping("/clients/{id}/factures/xlsx")
    public void ClientsFactureXLSX(@PathVariable ("id") Long clientId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures.xlsx\"");

        List<Facture> factures = factureService.findFacturesClient(clientId);
        List<Client> clients = clientService.findAllClients();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("clients");
        Sheet sheet = workbook.createSheet("factures");

        Row headerRow1 = sheet1.createRow(0);
        Cell cellId1 = headerRow1.createCell(0);
        cellId1.setCellValue("Id");
        Cell cellNom = headerRow1.createCell(1);
        cellNom.setCellValue("Nom");
        Cell cellPrenom = headerRow1.createCell(2);
        cellPrenom.setCellValue("Prenom");


        Row headerRow = sheet.createRow(0);
        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("Id");
        Cell cellTotal = headerRow.createCell(1);
        cellTotal.setCellValue("Total");


        int iRow1 = 1;
        for (Client client : clients) {
            Row row1 = sheet1.createRow(iRow1);

            Cell id1 = row1.createCell(0);
            id1.setCellValue(client.getId());

            Cell nom = row1.createCell(1);
            nom.setCellValue(client.getNom());

            Cell prenom = row1.createCell(2);
            prenom.setCellValue(client.getPrenom());

            iRow1 = iRow1 + 1;
        }
        int iRow = 1;
        for (Facture facture : factures) {
            Row row = sheet.createRow(iRow);

            Cell id = row.createCell(0);
            id.setCellValue(facture.getId());

            Cell Total = row.createCell(1);
            Total.setCellValue(facture.getTotal());

            iRow = iRow + 1;
        }
        workbook.write(response.getOutputStream());
        workbook.close();

    }

    /*@Autowired
    private FactureService factureService;

    @GetMapping("/factures/xlsx")
    public void FactureXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures.xlsx\"");

        List<Facture> allFactures = factureService.findAllFactures();
        LocalDate now = LocalDate.now();


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("factures");
        Row headerRow = sheet.createRow(0);

        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("Id");

        Cell cellClient = headerRow.createCell(1);
        cellClient.setCellValue("Client");

        Cell cellLigneFacture = headerRow.createCell(2);
        cellLigneFacture.setCellValue("LigneFacture");

        int iRow = 1;
        for (Facture facture : allFactures) {
            Row row = sheet.createRow(iRow);

            Cell id = row.createCell(0);
            id.setCellValue(facture.getId());



            iRow = iRow + 1;
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }*/
}
