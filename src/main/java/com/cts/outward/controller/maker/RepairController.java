package com.cts.outward.controller.maker;

import java.io.File;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.cts.outward.model.Cheque;
import com.cts.outward.service.ChequeService;
import com.cts.outward.service.BatchService;

public class RepairController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // WIRED COMPONENTS
    // =========================================================

    // Header
    @Wire
    private Label batchIdLabel;

    @Wire
    private Label chequeCounterLabel;

    @Wire
    private Label progressLabel;

    // Images
    @Wire
    private Image frontImage;

    @Wire
    private Image backImage;

    // Error Section
    @Wire
    private Label errorMessage;

    @Wire
    private Label errorFieldLabel;

    @Wire
    private Label scannedValueLabel;

    // Fields
    @Wire
    private Textbox chequeNumber;

    @Wire
    private Textbox accountNumber;

    @Wire
    private Textbox drawerName;

    @Wire
    private Textbox amount;

    @Wire
    private Textbox micrCode;

    @Wire
    private Textbox ifscCode;

    @Wire
    private Textbox status;

    @Wire
    private Datebox checkedDate;

    // Buttons
    @Wire
    private Button firstButton;

    @Wire
    private Button prevButton;

    @Wire
    private Button nextButton;

    @Wire
    private Button lastButton;

    @Wire
    private Button saveButton;

    @Wire
    private Button saveNextButton;

    @Wire
    private Button rejectButton;

    @Wire
    private Button backToListButton;

    // =========================================================
    // SERVICES
    // =========================================================

    private final ChequeService chequeService = new ChequeService();
    private final BatchService batchService = new BatchService();

    // =========================================================
    // STATE
    // =========================================================

    private String batchNumber;
    private String chequeNumberParam;
    private List<Cheque> cheques;
    private int currentIndex = 0;
    private Cheque currentCheque;
    private Window repairWindow;

    // =========================================================
    // AFTER COMPOSE
    // =========================================================

    @Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);

        System.out.println("==========================================");
        System.out.println("REPAIR CONTROLLER STARTED");
        System.out.println("==========================================");

        repairWindow = (Window) component;

        // Get parameters
        batchNumber = Executions.getCurrent().getParameter("batchNumber");
        chequeNumberParam = Executions.getCurrent().getParameter("chequeNumber");

        System.out.println("Batch Number = " + batchNumber);
        System.out.println("Cheque Number = " + chequeNumberParam);

        if (batchNumber == null || batchNumber.trim().isEmpty()) {
            Messagebox.show("Batch number is missing.", "Error", Messagebox.OK, Messagebox.ERROR);
            repairWindow.detach();
            return;
        }

        // Load data
        loadBatchDetails();
        loadCheques();

        // If cheque number is passed, show that specific cheque
        if (chequeNumberParam != null && !chequeNumberParam.trim().isEmpty() && cheques != null) {
            for (int i = 0; i < cheques.size(); i++) {
                if (cheques.get(i).getChequeNumber().equals(chequeNumberParam)) {
                    System.out.println("Found cheque: " + chequeNumberParam + " at index: " + i);
                    showCheque(i);
                    return;
                }
            }
            Messagebox.show("Cheque not found: " + chequeNumberParam, "Error", Messagebox.OK, Messagebox.ERROR);
        }

        // Show first cheque
        if (cheques != null && !cheques.isEmpty()) {
            showCheque(0);
        } else {
            Messagebox.show("No cheques found for this batch.", "Info", Messagebox.OK, Messagebox.INFORMATION);
            repairWindow.detach();
        }
    }

    // =========================================================
    // LOAD DATA
    // =========================================================

    private void loadBatchDetails() {
        try {
            if (batchIdLabel != null) {
                batchIdLabel.setValue("Batch: " + batchNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCheques() {
        try {
            cheques = chequeService.findChequesByBatch(batchNumber);

            if (cheques == null || cheques.isEmpty()) {
                System.out.println("No cheques found for batch: " + batchNumber);
                return;
            }

            System.out.println("Loaded " + cheques.size() + " cheques");

        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Error loading cheques: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // =========================================================
    // SHOW CHEQUE
    // =========================================================

    private void showCheque(int index) {
        if (cheques == null || index < 0 || index >= cheques.size()) {
            System.out.println("Invalid index: " + index);
            return;
        }

        currentIndex = index;
        currentCheque = cheques.get(index);

        System.out.println("==========================================");
        System.out.println("SHOWING CHEQUE: " + currentCheque.getChequeNumber());
        System.out.println("Index: " + (index + 1) + " of " + cheques.size());
        System.out.println("Status: " + currentCheque.getStatus());

        // Update labels
        if (chequeCounterLabel != null) {
            chequeCounterLabel.setValue("Cheque " + (index + 1) + " of " + cheques.size());
        }

        if (progressLabel != null) {
            progressLabel.setValue((index + 1) + " / " + cheques.size());
        }

        // Populate fields
        populateFields();

        // Load images
        loadImages();

        // Update buttons
        updateButtons();

        // Update error section
        updateErrorSection();
    }

    // =========================================================
    // POPULATE FIELDS
    // =========================================================

    private void populateFields() {
        if (currentCheque == null) return;

        chequeNumber.setValue(valueOrEmpty(currentCheque.getChequeNumber()));
        accountNumber.setValue(valueOrEmpty(currentCheque.getAccountNumber()));
        drawerName.setValue(valueOrEmpty(currentCheque.getDrawerName()));
        amount.setValue(currentCheque.getAmount() != null ? currentCheque.getAmount().toString() : "");
        micrCode.setValue(valueOrEmpty(currentCheque.getMicrCode()));
        ifscCode.setValue(valueOrEmpty(currentCheque.getIfscCode()));
        status.setValue(valueOrEmpty(currentCheque.getStatus()));

        if (checkedDate != null) {
            if (currentCheque.getCheckedDate() != null) {
                checkedDate.setValue(java.sql.Date.valueOf(currentCheque.getCheckedDate()));
            } else {
                checkedDate.setValue(null);
            }
        }

        // Set scanned value
        if (scannedValueLabel != null) {
            scannedValueLabel.setValue(valueOrEmpty(currentCheque.getAccountNumber()));
        }
    }

    // =========================================================
    // LOAD IMAGES
    // =========================================================

    private void loadImages() {
        if (currentCheque == null) return;

        String frontPath = currentCheque.getFrontImagePath();
        String backPath = currentCheque.getBackImagePath();

        System.out.println("Front Image: " + frontPath);
        System.out.println("Back Image: " + backPath);

        loadImage(frontImage, frontPath);
        loadImage(backImage, backPath);
    }

    private void loadImage(Image img, String path) {
        if (img == null) return;

        //img.setContent(null);

        if (path == null || path.trim().isEmpty()) {
            return;
        }

        try {
            File file = new File(path.trim());
            if (file.exists() && file.isFile()) {
                AImage aimg = new AImage(file);
                img.setContent(aimg);
                System.out.println("Image loaded successfully: " + path);
            } else {
                System.out.println("Image not found: " + path);
                // Try to load default image if available
                try {
                    String defaultPath = getClass().getResource("/images/no-image.png").getPath();
                    if (defaultPath != null) {
                        File defaultFile = new File(defaultPath);
                        if (defaultFile.exists()) {
                            AImage defaultImg = new AImage(defaultFile);
                            img.setContent(defaultImg);
                            System.out.println("Loaded default image");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("No default image available");
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    // =========================================================
    // UPDATE ERROR SECTION
    // =========================================================

    private void updateErrorSection() {
        if (currentCheque == null) return;

        String statusText = currentCheque.getStatus();
        
        if (statusText != null && (statusText.contains("Error") || statusText.contains("ERROR"))) {
            errorMessage.setValue("⚠️ The following fields have Data Entry errors. Please correct and save.");
            errorFieldLabel.setValue(statusText);
            errorFieldLabel.setStyle("color:#d32f2f;font-weight:bold;");
        } else {
            errorMessage.setValue("✅ No errors found.");
            errorFieldLabel.setValue("All fields are correct.");
            errorFieldLabel.setStyle("color:#4CAF50;font-weight:bold;");
        }
    }

    // =========================================================
    // UPDATE BUTTONS
    // =========================================================

    private void updateButtons() {
        if (cheques == null || cheques.isEmpty()) {
            firstButton.setDisabled(true);
            prevButton.setDisabled(true);
            nextButton.setDisabled(true);
            lastButton.setDisabled(true);
            return;
        }

        firstButton.setDisabled(currentIndex == 0);
        prevButton.setDisabled(currentIndex == 0);
        nextButton.setDisabled(currentIndex >= cheques.size() - 1);
        lastButton.setDisabled(currentIndex >= cheques.size() - 1);
    }

    // =========================================================
    // @Listen EVENTS
    // =========================================================

    @Listen("onClick = #firstButton")
    public void goFirst() {
        System.out.println("First button clicked");
        if (cheques != null && !cheques.isEmpty()) {
            showCheque(0);
        }
    }

    @Listen("onClick = #prevButton")
    public void goPrev() {
        System.out.println("Prev button clicked");
        if (currentIndex > 0) {
            showCheque(currentIndex - 1);
        }
    }

    @Listen("onClick = #nextButton")
    public void goNext() {
        System.out.println("Next button clicked");
        if (currentIndex < cheques.size() - 1) {
            showCheque(currentIndex + 1);
        }
    }

    @Listen("onClick = #lastButton")
    public void goLast() {
        System.out.println("Last button clicked");
        if (cheques != null && !cheques.isEmpty()) {
            showCheque(cheques.size() - 1);
        }
    }

    @Listen("onClick = #saveButton")
    public void saveCheque() {
        System.out.println("Save button clicked");
        if (currentCheque == null) {
            Messagebox.show("No cheque is currently loaded.");
            return;
        }

        try {
            // Get values from UI
            if (accountNumber != null && accountNumber.getValue() != null) {
                currentCheque.setAccountNumber(accountNumber.getValue().trim());
            }

            if (drawerName != null && drawerName.getValue() != null) {
                currentCheque.setDrawerName(drawerName.getValue().trim());
            }

            if (micrCode != null && micrCode.getValue() != null) {
                currentCheque.setMicrCode(micrCode.getValue().trim());
            }

            if (ifscCode != null && ifscCode.getValue() != null) {
                currentCheque.setIfscCode(ifscCode.getValue().trim());
            }

            if (amount != null && amount.getValue() != null && !amount.getValue().trim().isEmpty()) {
                try {
                    currentCheque.setAmount(new BigDecimal(amount.getValue().trim()));
                } catch (NumberFormatException e) {
                    Messagebox.show("Invalid amount format. Please enter a valid number.", "Error", Messagebox.OK, Messagebox.ERROR);
                    return;
                }
            }

            if (checkedDate != null && checkedDate.getValue() != null) {
                currentCheque.setCheckedDate(checkedDate.getValue().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }

            // Update status if corrections were made
            String originalStatus = currentCheque.getStatus();
            if (originalStatus != null && (originalStatus.contains("Error") || originalStatus.contains("ERROR"))) {
                currentCheque.setStatus("CORRECTED");
            }

            // Update in database
            chequeService.updateCheque(currentCheque);

            // Update error section
            updateErrorSection();

            Messagebox.show("✅ Cheque saved successfully.", "Success", Messagebox.OK, Messagebox.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Unable to save cheque: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Listen("onClick = #saveNextButton")
    public void saveAndNext() {
        System.out.println("Save & Next button clicked");
        saveCheque();
        goNext();
    }

    @Listen("onClick = #rejectButton")
    public void rejectCheque() {
        System.out.println("Reject button clicked");
        if (currentCheque == null) return;

        try {
            int response = Messagebox.show("Are you sure you want to reject this cheque?", 
                                           "Confirm Reject", 
                                           Messagebox.YES | Messagebox.NO,
                                           Messagebox.QUESTION);

            if (response == Messagebox.YES) {
                currentCheque.setStatus("REJECTED");
                chequeService.updateCheque(currentCheque);
                updateErrorSection();
                Messagebox.show("Cheque rejected.", "Success", Messagebox.OK, Messagebox.INFORMATION);
                goNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Unable to reject cheque: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Listen("onClick = #backToListButton")
    public void backToList() {
        System.out.println("Back to List button clicked");
        try {
            if (repairWindow != null) {
                repairWindow.detach();
            }
            Executions.sendRedirect("DataEntry.zul?batchNumber=" + batchNumber);
        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Unable to return: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}