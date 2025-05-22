package com.example.client.services;

import com.example.client.entites.Action;
import com.example.client.entites.Actionnaire;
import com.example.client.entites.Portefeuille;
import com.example.client.entites.Transaction;
import com.example.client.repositories.ActionRepository;
import com.example.client.repositories.ActionnaireRepository;
import com.example.client.repositories.PortefeuilleRepository;
import com.example.client.repositories.TransactionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionImportService {

    @Autowired
    private ActionnaireRepository actionnaireRepository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private PortefeuilleRepository portefeuilleRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public void importTransactions(MultipartFile file) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                String idTransaction = getString(row, 0);
                String numeroActionnaire = getString(row, 3);
                String nomActionnaire = getString(row, 4);
                String isin = getString(row, 5);
                int quantite = parseIntSafe(getString(row, 6));
                double prix = parsePrix(getString(row, 7));
                double montant = parsePrix(getString(row, 8));

                Actionnaire actionnaire = actionnaireRepository.findByMatriculeActionnaire(numeroActionnaire)
                        .orElseGet(() -> {
                            Actionnaire a = new Actionnaire();
                            a.setNomActionnaire(nomActionnaire);
                            a.setPrenomActionnaire("—");
                            a.setMatriculeActionnaire(numeroActionnaire);
                            a.setEmailActionnaire(numeroActionnaire + "@placeholder.com");
                            a.setTelephone(00000000L);
                            a.setUserCreation("import_excel");
                            a.setUserModification("import_excel");
                            a.setDateCreation(LocalDateTime.now());
                            a.setDateModification(LocalDateTime.now());
                            return actionnaireRepository.save(a);
                        });

                Action action = actionRepository.findByIsin(isin)
                        .orElseGet(() -> {
                            Action act = new Action();
                            act.setIsin(isin);
                            act.setNomSociete("Société inconnue");
                            act.setEnVente(false);
                            act.setPrix(prix);
                            act.setSecteur("N/A");
                            act.setDevice("import_excel");
                            act.setUserCreation("import_excel");
                            act.setUserModification("import_excel");
                            act.setDateCreation(LocalDateTime.now());
                            act.setDateModification(LocalDateTime.now());
                            return actionRepository.save(act);
                        });

                Portefeuille portefeuille = portefeuilleRepository.findByActionnaireAndAction(actionnaire, action)
                        .orElseGet(() -> {
                            Portefeuille p = new Portefeuille();
                            p.setAction(action);
                            p.setActionnaire(actionnaire);
                            p.setQuantite(0);
                            p.setValeurTotale(0.0);
                            p.setUserCreation("import_excel");
                            p.setUserModification("import_excel");
                            p.setDateCreation(LocalDateTime.now());
                            p.setDateModification(LocalDateTime.now());
                            return portefeuilleRepository.save(p);
                        });

                Cell dateCell = row.getCell(1);
                LocalDate dateTransaction;
                if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                    dateTransaction = dateCell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    String dateStr = getString(row, 1);
                    dateTransaction = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }

                Long idType = parseLongSafe(getString(row, 2));

                boolean exists = transactionRepository.existsByAllFields(
                        dateTransaction,
                        (long) quantite,
                        montant,
                        idType,
                        portefeuille.getIdPortefeuille()
                );

                if (exists) {
                    System.out.println("⚠️ Transaction déjà existante ignorée (ligne " + i + ")");
                    continue;
                }

                Transaction tx = new Transaction();
                tx.setDateTransaction(dateTransaction);
                tx.setQuantite((long) quantite);
                tx.setMontants(montant);
                tx.setIdPortefeuille(portefeuille.getIdPortefeuille());
                tx.setIdType(idType);

                transactionRepository.save(tx);
                System.out.println("✅ Transaction enregistrée : " + tx.getIdTransactions());

            } catch (Exception e) {
                System.err.println("❌ Erreur à la ligne " + i + " : " + e.getMessage());
            }
        }

        workbook.close();
    }

    public List<Transaction> previewTransactions(MultipartFile file) throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(1) == null) continue;

            try {
                Transaction tx = new Transaction();

                // Lecture de la date
                Cell dateCell = row.getCell(1);
                LocalDate dateTransaction;
                if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                    dateTransaction = dateCell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    dateTransaction = LocalDate.parse(getString(row, 1));
                }

                // Lecture des données
                int quantite = parseIntSafe(getString(row, 6));
                double montant = parsePrix(getString(row, 8));
                Long idType = parseLongSafe(getString(row, 2));

                tx.setDateTransaction(dateTransaction);
                tx.setQuantite((long) quantite);
                tx.setMontants(montant);
                tx.setIdType(idType);

                // Recherche portefeuille simulée (ou laisse null)
                String numeroActionnaire = getString(row, 3);
                String isin = getString(row, 5);
                Actionnaire actionnaire = actionnaireRepository.findByMatriculeActionnaire(numeroActionnaire).orElse(null);
                Action action = actionRepository.findByIsin(isin).orElse(null);
                if (actionnaire != null && action != null) {
                    Portefeuille portefeuille = portefeuilleRepository.findByActionnaireAndAction(actionnaire, action).orElse(null);
                    if (portefeuille != null) {
                        tx.setIdPortefeuille(portefeuille.getIdPortefeuille());

                        // Vérifie si déjà existante
                        boolean exists = transactionRepository.existsByDateTransactionAndQuantiteAndMontantsAndIdTypeAndIdPortefeuille(
                                dateTransaction,
                                (long) quantite,
                                montant,
                                idType,
                                portefeuille.getIdPortefeuille()
                        );
                        if (exists) {
                            // Marque comme doublon (ex : tx.setObservations("EXISTE"));
                            System.out.println("⚠️ Déjà présente (ligne " + i + ")");
                        }
                    }
                }

                transactions.add(tx);

            } catch (Exception e) {
                System.err.println("⚠️ Ignoré ligne " + i + " : " + e.getMessage());
            }
        }

        workbook.close();
        return transactions;
    }


    private String getString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private double parsePrix(String input) {
        if (input == null || input.isEmpty()) return 0;
        return Double.parseDouble(input.replace("TND", "").replace(",", "").trim());
    }

    private int parseIntSafe(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Long parseLongSafe(String input) {
        try {
            return Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
