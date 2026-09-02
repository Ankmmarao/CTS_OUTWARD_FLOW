package com.cts.outward.controller.maker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.cts.outward.model.Cheque;
import com.cts.outward.service.ChequeService;

public class DataEntryController
        extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // LIST
    // =========================================================

    @Wire
    private Listbox chequeList;

    @Wire
    private Label batchNumberLabel;

    @Wire
    private Label chequeCount;

    @Wire
    private Label errorCount;

    // =========================================================
    // CHEQUE DETAILS
    // =========================================================

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

    // =========================================================
    // IMAGES
    // =========================================================

    @Wire
    private Image frontImage;

    @Wire
    private Image backImage;

    // =========================================================
    // SERVICE
    // =========================================================

    private final ChequeService chequeService =
            new ChequeService();

    // =========================================================
    // DATA
    // =========================================================

    private String batchNumber;

    private List<Cheque> cheques =
            new ArrayList<>();

    private int currentIndex = -1;

    private Cheque currentCheque;

    // =========================================================
    // AFTER COMPOSE
    // =========================================================
    private void openCheque(Cheque cheque) {

        if (cheque == null) {
            Messagebox.show("Cheque details not available.");
            return;
        }

        String batchNumber = cheque.getBatchNumber();
        String chequeNumber = cheque.getChequeNumber();

        if (batchNumber == null || batchNumber.trim().isEmpty()) {
            Messagebox.show("Batch number is missing.");
            return;
        }

        if (chequeNumber == null || chequeNumber.trim().isEmpty()) {
            Messagebox.show("Cheque number is missing.");
            return;
        }

        Executions.sendRedirect(
            "dataentry_repair.zul"
            + "?batchNumber="
            + Executions.encodeURL(batchNumber)
            + "&chequeNumber="
            + Executions.encodeURL(chequeNumber)
        );
    }

    @Override
    public void doAfterCompose(Component component)
            throws Exception {

        super.doAfterCompose(component);

        /*
         * Get batch number from:
         *
         * DataEntry.zul?batchNumber=BATCH001
         */
        batchNumber =
                Executions.getCurrent()
                        .getParameter("batchNumber");

        if (batchNumber == null ||
                batchNumber.trim().isEmpty()) {

            Messagebox.show(
                    "Batch number is missing."
            );

            return;
        }

        batchNumber = batchNumber.trim();

        // Display batch number
        if (batchNumberLabel != null) {

            batchNumberLabel.setValue(
                    "Batch: " + batchNumber
            );
        }

        // Load ALL cheques for this batch
        loadCheques();
    }

    // =========================================================
    // LOAD ALL CHEQUES
    // =========================================================

    private void loadCheques() {

        try {

            /*
             * IMPORTANT:
             *
             * This loads ALL cheques belonging
             * to the selected batch.
             *
             * It does NOT filter by ERROR.
             */
            cheques =
                    chequeService.findChequesByBatch(
                            batchNumber
                    );

            if (cheques == null) {

                cheques = new ArrayList<>();
            }

            updateCounts();

            displayChequeList();

            /*
             * Automatically open first cheque
             * if available.
             */
            if (!cheques.isEmpty()) {

                currentIndex = 0;

                loadChequeDetails(
                        cheques.get(0)
                );

                chequeList.setSelectedIndex(0);
            }

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to load cheques:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // UPDATE COUNTS
    // =========================================================

    private void updateCounts() {

        int total = cheques.size();

        int errors = 0;

        for (Cheque cheque : cheques) {

            if (cheque == null) {
                continue;
            }

            String chequeStatus =
                    cheque.getStatus();

            if (isErrorStatus(chequeStatus)) {

                errors++;
            }
        }

        if (chequeCount != null) {

            chequeCount.setValue(
                    "Total Cheques: " + total
            );
        }

        if (errorCount != null) {

            errorCount.setValue(
                    "Error Cheques: " + errors
            );
        }
    }

    // =========================================================
    // DISPLAY CHEQUE LIST
    // =========================================================

    private void displayChequeList() {

        ListModelList<Cheque> model =
                new ListModelList<>(cheques);

        chequeList.setModel(model);

        chequeList.setItemRenderer(
                new ListitemRenderer<Cheque>() {

                    @Override
                    public void render(
                            Listitem item,
                            Cheque cheque,
                            int index) {

                        // =====================================
                        // CHEQUE NUMBER
                        // =====================================

                        Listcell chequeNumberCell =
                                new Listcell();

                        chequeNumberCell.setLabel(
                                cheque.getChequeNumber() != null
                                        ? cheque.getChequeNumber()
                                        : "-"
                        );

                        item.appendChild(
                                chequeNumberCell
                        );

                        // =====================================
                        // BATCH NUMBER
                        // =====================================

                        Listcell batchCell =
                                new Listcell();

                        batchCell.setLabel(
                                cheque.getBatchNumber() != null
                                        ? cheque.getBatchNumber()
                                        : "-"
                        );

                        item.appendChild(
                                batchCell
                        );

                        // =====================================
                        // ACCOUNT NUMBER
                        // =====================================

                        Listcell accountCell =
                                new Listcell();

                        accountCell.setLabel(
                                cheque.getAccountNumber() != null
                                        ? cheque.getAccountNumber()
                                        : "-"
                        );

                        item.appendChild(
                                accountCell
                        );

                        // =====================================
                        // STATUS
                        // =====================================

                        Listcell statusCell =
                                new Listcell();

                        String chequeStatus =
                                cheque.getStatus();

                        if (chequeStatus == null ||
                                chequeStatus.trim().isEmpty()) {

                            chequeStatus = "PENDING";
                        }

                        statusCell.setLabel(
                                chequeStatus
                        );

                        item.appendChild(
                                statusCell
                        );

                        // =====================================
                        // ACTION
                        // =====================================

                     // =========================================================
                     // ACTION
                     // =========================================================

                     Listcell actionCell = new Listcell();

                     Button actionButton;

                     if (isErrorStatus(chequeStatus)) {

                         actionButton = new Button("Repair");

                         actionButton.setSclass(
                                 "repair-button"
                         );

                     } else {

                         actionButton = new Button("Open");

                         actionButton.setSclass(
                                 "open-button"
                         );
                     }

                     // ---------------------------------------------------------
                     // OPEN / REPAIR CLICK
                     // ---------------------------------------------------------

                     actionButton.addEventListener(
                             "onClick",
                             event -> openCheque(cheque)
                     );

                     actionCell.appendChild(
                             actionButton
                     );

                     item.appendChild(
                             actionCell
                     );
                    }
                }
        );
    }

    // =========================================================
    // CHECK ERROR STATUS
    // =========================================================

    private boolean isErrorStatus(
            String chequeStatus) {

        if (chequeStatus == null) {

            return false;
        }

        String value =
                chequeStatus.trim()
                        .toUpperCase();

        return value.equals("ERROR")
                || value.equals("DATA_ENTRY_ERROR")
                || value.equals("REPAIR")
                || value.equals("REJECTED");
    }

    // =========================================================
    // LOAD CHEQUE DETAILS
    // =========================================================

    private void loadChequeDetails(
            Cheque cheque) {

        if (cheque == null) {
            return;
        }

        currentCheque = cheque;

        // =============================================
        // CHEQUE NUMBER
        // =============================================

        if (chequeNumber != null) {

            chequeNumber.setValue(
                    safeValue(
                            cheque.getChequeNumber()
                    )
            );
        }

        // =============================================
        // ACCOUNT NUMBER
        // =============================================

        if (accountNumber != null) {

            accountNumber.setValue(
                    safeValue(
                            cheque.getAccountNumber()
                    )
            );
        }

        // =============================================
        // DRAWER NAME
        // =============================================

        if (drawerName != null) {

            drawerName.setValue(
                    safeValue(
                            cheque.getDrawerName()
                    )
            );
        }

        // =============================================
        // AMOUNT
        // =============================================

        if (amount != null) {

            BigDecimal chequeAmount =
                    cheque.getAmount();

            if (chequeAmount != null) {

                amount.setValue(
                        chequeAmount.toPlainString()
                );

            } else {

                amount.setValue("");
            }
        }

        // =============================================
        // MICR
        // =============================================

        if (micrCode != null) {

            micrCode.setValue(
                    safeValue(
                            cheque.getMicrCode()
                    )
            );
        }

        // =============================================
        // IFSC
        // =============================================

        if (ifscCode != null) {

            ifscCode.setValue(
                    safeValue(
                            cheque.getIfscCode()
                    )
            );
        }

        // =============================================
        // STATUS
        // =============================================

        if (status != null) {

            status.setValue(
                    safeValue(
                            cheque.getStatus()
                    )
            );
        }

        // =============================================
        // CHECKED DATE
        // =============================================

        if (checkedDate != null) {

            LocalDate date =
                    cheque.getCheckedDate();

            if (date != null) {

                checkedDate.setValue(
                        java.sql.Date.valueOf(date)
                );

            } else {

                checkedDate.setValue(null);
            }
        }

        // =============================================
        // FRONT IMAGE
        // =============================================

        loadFrontImage(cheque);

        // =============================================
        // BACK IMAGE
        // =============================================

        loadBackImage(cheque);
    }

    // =========================================================
    // FRONT IMAGE
    // =========================================================

    private void loadFrontImage(
            Cheque cheque) {

        if (frontImage == null) {
            return;
        }

        String imagePath =
                cheque.getFrontImagePath();

        if (imagePath != null &&
                !imagePath.trim().isEmpty()) {

            frontImage.setSrc(
                    imagePath
            );

            frontImage.setVisible(true);

        } else {

            frontImage.setSrc("");

            frontImage.setVisible(false);
        }
    }

    // =========================================================
    // BACK IMAGE
    // =========================================================

    private void loadBackImage(
            Cheque cheque) {

        if (backImage == null) {
            return;
        }

        String imagePath =
                cheque.getBackImagePath();

        if (imagePath != null &&
                !imagePath.trim().isEmpty()) {

            backImage.setSrc(
                    imagePath
            );

            backImage.setVisible(true);

        } else {

            backImage.setSrc("");

            backImage.setVisible(false);
        }
    }

    // =========================================================
    // SAVE
    // =========================================================

    @Listen("onClick=#saveButton")
    public void saveCheque() {

        if (currentCheque == null) {

            Messagebox.show(
                    "Please select a cheque."
            );

            return;
        }

        try {

            updateCurrentChequeFromUI();

            chequeService.updateCheque(
                    currentCheque
            );

            Messagebox.show(
                    "Cheque saved successfully."
            );

            // Refresh list
            loadCheques();

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to save cheque:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // SAVE AND NEXT
    // =========================================================

    @Listen("onClick=#saveNextButton")
    public void saveAndNext() {

        if (currentCheque == null) {

            Messagebox.show(
                    "Please select a cheque."
            );

            return;
        }

        try {

            updateCurrentChequeFromUI();

            chequeService.updateCheque(
                    currentCheque
            );

            /*
             * Move to next cheque.
             */
            if (currentIndex <
                    cheques.size() - 1) {

                currentIndex++;

                Cheque nextCheque =
                        cheques.get(
                                currentIndex
                        );

                loadChequeDetails(
                        nextCheque
                );

                chequeList.setSelectedIndex(
                        currentIndex
                );

            } else {

                Messagebox.show(
                        "This is the last cheque in the batch."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to save cheque:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // UPDATE OBJECT FROM UI
    // =========================================================

    private void updateCurrentChequeFromUI()
            throws Exception {

        if (currentCheque == null) {
            return;
        }

        // Account
        if (accountNumber != null) {

            currentCheque.setAccountNumber(
                    accountNumber.getValue()
            );
        }

        // Drawer
        if (drawerName != null) {

            currentCheque.setDrawerName(
                    drawerName.getValue()
            );
        }

        // Amount
        if (amount != null) {

            String amountValue =
                    amount.getValue();

            if (amountValue != null &&
                    !amountValue.trim().isEmpty()) {

                currentCheque.setAmount(
                        new BigDecimal(
                                amountValue.trim()
                        )
                );

            } else {

                currentCheque.setAmount(null);
            }
        }

        // MICR
        if (micrCode != null) {

            currentCheque.setMicrCode(
                    micrCode.getValue()
            );
        }

        // IFSC
        if (ifscCode != null) {

            currentCheque.setIfscCode(
                    ifscCode.getValue()
            );
        }

        // Status
        if (status != null) {

            currentCheque.setStatus(
                    status.getValue()
            );
        }

        // Checked date
        if (checkedDate != null) {

            if (checkedDate.getValue() != null) {

                java.util.Date date =
                        checkedDate.getValue();

                currentCheque.setCheckedDate(
                        new java.sql.Date(
                                date.getTime()
                        )
                        .toLocalDate()
                );

            } else {

                currentCheque.setCheckedDate(
                        null
                );
            }
        }
    }

    // =========================================================
    // FIRST
    // =========================================================

    @Listen("onClick=#firstButton")
    public void firstCheque() {

        if (cheques.isEmpty()) {
            return;
        }

        currentIndex = 0;

        loadChequeDetails(
                cheques.get(0)
        );

        chequeList.setSelectedIndex(0);
    }

    // =========================================================
    // PREVIOUS
    // =========================================================

    @Listen("onClick=#prevButton")
    public void previousCheque() {

        if (cheques.isEmpty()) {
            return;
        }

        if (currentIndex > 0) {

            currentIndex--;

            loadChequeDetails(
                    cheques.get(currentIndex)
            );

            chequeList.setSelectedIndex(
                    currentIndex
            );
        }
    }

    // =========================================================
    // NEXT
    // =========================================================

    @Listen("onClick=#nextButton")
    public void nextCheque() {

        if (cheques.isEmpty()) {
            return;
        }

        if (currentIndex <
                cheques.size() - 1) {

            currentIndex++;

            loadChequeDetails(
                    cheques.get(currentIndex)
            );

            chequeList.setSelectedIndex(
                    currentIndex
            );
        }
    }

    // =========================================================
    // LAST
    // =========================================================

    @Listen("onClick=#lastButton")
    public void lastCheque() {

        if (cheques.isEmpty()) {
            return;
        }

        currentIndex =
                cheques.size() - 1;

        loadChequeDetails(
                cheques.get(currentIndex)
        );

        chequeList.setSelectedIndex(
                currentIndex
        );
    }

    // =========================================================
    // REJECT
    // =========================================================

    @Listen("onClick=#rejectButton")
    public void rejectCheque() {

        if (currentCheque == null) {

            Messagebox.show(
                    "Please select a cheque."
            );

            return;
        }

        currentCheque.setStatus(
                "REJECTED"
        );

        try {

            chequeService.updateCheque(
                    currentCheque
            );

            Messagebox.show(
                    "Cheque rejected."
            );

            loadCheques();

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to reject cheque:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // BACK TO LIST
    // =========================================================

    @Listen("onClick=#backToListButton")
    public void backToList() {

        Executions.sendRedirect(
                "MakerDashboard.zul"
        );
    }
    private String safeValue(
            String value) {

        if (value == null) {

            return "";
        }

        return value;
    }
    
}