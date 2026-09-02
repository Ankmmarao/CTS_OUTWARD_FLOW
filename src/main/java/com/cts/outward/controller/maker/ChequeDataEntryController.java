package com.cts.outward.controller.maker;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.cts.outward.model.Cheque;
import com.cts.outward.service.ChequeService;

public class ChequeDataEntryController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    // WIRED COMPONENTS
    @Wire
    private Textbox accountNumber;

    @Wire
    private Textbox correctValue;

    @Wire
    private Label scannedValue;

    @Wire
    private Image frontImage;

    // SERVICE
    private final ChequeService chequeService = new ChequeService();

    // PARAMETERS
    private String batchNumber = "B009";
    private String chequeNumberValue;

    private Cheque currentCheque;

    // CHEQUE DATA MAP FOR DEMO
    private Map<String, Cheque> chequeDataMap = new HashMap<>();

    @Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);

        System.out.println("==========================================");
        System.out.println("CHEQUE DATA ENTRY CONTROLLER STARTED");
        System.out.println("==========================================");

        // Initialize demo data
        initializeDemoData();

        // Check if cheque number parameter is passed
        chequeNumberValue = Executions.getCurrent().getParameter("chequeNumber");
        
        if (chequeNumberValue != null && !chequeNumberValue.trim().isEmpty()) {
            loadCheque(chequeNumberValue);
        }

        System.out.println("Batch Number  = " + batchNumber);
        System.out.println("Cheque Number = " + chequeNumberValue);
    }

    // INITIALIZE DEMO DATA
    private void initializeDemoData() {
        // Sample cheque data with images
        Cheque cheque1 = new Cheque();
        cheque1.setChequeNumber("10000021");
        cheque1.setAccountNumber("123456789010");
        cheque1.setDrawerName("ABC Enterprises");
        cheque1.setAmount(new BigDecimal("50000.00"));
        cheque1.setMicrCode("0523000115");
        cheque1.setIfscCode("SBIN0000691");
        cheque1.setStatus("Account Number Error");
        cheque1.setCheckedDate(LocalDate.of(2025, 6, 15));
        cheque1.setFrontImagePath("C:/cheque_images/front_10000021.png");
        cheque1.setBackImagePath("C:/cheque_images/back_10000021.png");
        chequeDataMap.put("10000021", cheque1);

        Cheque cheque2 = new Cheque();
        cheque2.setChequeNumber("10000022");
        cheque2.setAccountNumber("123456789011");
        cheque2.setDrawerName("XYZ Corporation");
        cheque2.setAmount(new BigDecimal("75000.00"));
        cheque2.setMicrCode("0523000116");
        cheque2.setIfscCode("SBIN0000692");
        cheque2.setStatus("MICR Error");
        cheque2.setCheckedDate(LocalDate.of(2025, 6, 16));
        cheque2.setFrontImagePath("C:/cheque_images/front_10000022.png");
        cheque2.setBackImagePath("C:/cheque_images/back_10000022.png");
        chequeDataMap.put("10000022", cheque2);

        // Add more cheque data as needed...
    }

    // LOAD CHEQUE FROM REPAIR BUTTON
    public void loadCheque(String chequeNumber) {
        System.out.println("------------------------------------------");
        System.out.println("LOADING CHEQUE: " + chequeNumber);
        System.out.println("------------------------------------------");

        try {
            // Get cheque from map (in real app, call service)
            currentCheque = chequeDataMap.get(chequeNumber);

            if (currentCheque == null) {
                System.out.println("Cheque not found with number: " + chequeNumber);
                Messagebox.show("Cheque not found: " + chequeNumber);
                return;
            }

            System.out.println("Cheque found successfully.");
            populateFields();
            loadImages();

        } catch (Exception e) {
            System.out.println("ERROR WHILE LOADING CHEQUE");
            e.printStackTrace();
            Messagebox.show("Error loading cheque: " + e.getMessage());
        }
    }

    // POPULATE FIELDS
    private void populateFields() {
        if (currentCheque == null) {
            System.out.println("populateFields(): currentCheque is NULL");
            return;
        }

        // Account Number
        if (accountNumber != null) {
            accountNumber.setValue(valueOrEmpty(currentCheque.getAccountNumber()));
        }

        // Correct Value (same as account number for now)
        if (correctValue != null) {
            correctValue.setValue(valueOrEmpty(currentCheque.getAccountNumber()));
        }

        // Scanned Value
        if (scannedValue != null) {
            scannedValue.setValue(valueOrEmpty(currentCheque.getAccountNumber()));
        }
    }

    // LOAD IMAGES
    private void loadImages() {
        if (currentCheque == null) {
            System.out.println("loadImages(): currentCheque is NULL");
            return;
        }

        System.out.println("------------------------------------------");
        System.out.println("LOADING CHEQUE IMAGES");
        System.out.println("------------------------------------------");

        // Load front image
        String frontPath = currentCheque.getFrontImagePath();
        System.out.println("FRONT PATH FROM MODEL = " + frontPath);
        loadImage(frontImage, frontPath, "FRONT");
    }

    // LOAD ONE IMAGE
    private void loadImage(Image zkImageComponent, String imagePath, String imageName) {
        if (zkImageComponent == null) {
            System.out.println(imageName + " IMAGE COMPONENT IS NULL");
            return;
        }

        if (imagePath == null || imagePath.trim().isEmpty()) {
            System.out.println(imageName + " IMAGE PATH IS NULL/EMPTY");
            System.out.println(imageName + " FRAME WILL REMAIN EMPTY");
            // Try to load a default image if available
            try {
                // Load default image from resources
                String defaultPath = getClass().getResource("/images/default_cheque.png").getPath();
                File defaultFile = new File(defaultPath);
                if (defaultFile.exists()) {
                    AImage defaultImage = new AImage(defaultFile);
                    zkImageComponent.setContent(defaultImage);
                    System.out.println("Loaded default image for " + imageName);
                }
            } catch (Exception e) {
                System.out.println("Could not load default image: " + e.getMessage());
            }
            return;
        }

        try {
            String path = imagePath.trim();
            System.out.println(imageName + " IMAGE PATH = " + path);

            File imageFile = new File(path);
            System.out.println(imageName + " ABSOLUTE PATH = " + imageFile.getAbsolutePath());
            System.out.println(imageName + " EXISTS = " + imageFile.exists());
            System.out.println(imageName + " IS FILE = " + imageFile.isFile());
            System.out.println(imageName + " SIZE = " + imageFile.length() + " bytes");

            if (!imageFile.exists()) {
                System.out.println(imageName + " IMAGE DOES NOT EXIST");
                System.out.println(imageName + " FRAME WILL REMAIN EMPTY");
                return;
            }

            if (!imageFile.isFile()) {
                System.out.println(imageName + " PATH IS NOT A FILE");
                return;
            }

            AImage zkImage = new AImage(imageFile);
            System.out.println(imageName + " AImage CREATED");

            zkImageComponent.setContent(zkImage);
            System.out.println(imageName + " IMAGE CONTENT SET SUCCESSFULLY");

        } catch (Exception e) {
            System.out.println("ERROR LOADING " + imageName + " IMAGE");
            e.printStackTrace();
        }
    }

    // SAVE CHEQUE
    public void saveCheque() {
        if (currentCheque == null) {
            Messagebox.show("No cheque is currently loaded.");
            return;
        }

        try {
            // Get corrected value
            String correctedAccount = correctValue != null ? correctValue.getValue() : null;
            
            if (correctedAccount != null && !correctedAccount.trim().isEmpty()) {
                currentCheque.setAccountNumber(correctedAccount);
                currentCheque.setStatus("Corrected");
                System.out.println("Account number corrected to: " + correctedAccount);
            }

            // Update cheque (in real app, call service)
            // chequeService.updateCheque(currentCheque);

            Messagebox.show("Cheque saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Unable to save cheque:\n" + e.getMessage());
        }
    }

    // BACK TO DATA ENTRY
    public void backToDataEntry() {
        try {
            Executions.sendRedirect("DataEntry.zul");
        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Unable to return to cheque list:\n" + e.getMessage());
        }
    }

    // UTILITY
    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}