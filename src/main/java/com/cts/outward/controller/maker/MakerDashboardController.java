
package com.cts.outward.controller.maker;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

import com.cts.outward.model.Batch;
import com.cts.outward.service.BatchService;

public class MakerDashboardController
        extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // WIRED UI COMPONENTS
    // =========================================================

    @Wire
    private Listbox batchList;

    @Wire
    private Label totalBatches;

    @Wire
    private Label availableBatches;

    @Wire
    private Label inProgressBatches;

    // =========================================================
    // SERVICE
    // =========================================================

    private final BatchService batchService =
            new BatchService();

    // =========================================================
    // LOGGED-IN MAKER ID
    // =========================================================

    // Temporary Maker ID
    // Later replace with session/login user ID
    private final Integer makerId = 101;

    // =========================================================
    // AFTER COMPOSE
    // =========================================================

    @Override
    public void doAfterCompose(Component component)
            throws Exception {

        super.doAfterCompose(component);

        loadBatches();
    }

    // =========================================================
    // LOAD BATCHES
    // =========================================================

    private void loadBatches() {

        try {

            // -------------------------------------------------
            // Get batches for current maker
            // -------------------------------------------------

            List<Batch> batches =
                    batchService.findMakerBatches(makerId);

            if (batches == null) {
                batches = new ArrayList<>();
            }

            // =================================================
            // DASHBOARD COUNTS
            // =================================================

            int total = batches.size();
            int available = 0;
            int inProgress = 0;

            for (Batch batch : batches) {

                if (batch == null) {
                    continue;
                }

                String status =
                        batch.getBatchStatus();

                if (status == null) {
                    continue;
                }

                // -------------------------------------------------
                // READY_FOR_ASSIGNMENT = Available
                // -------------------------------------------------

                if ("READY_FOR_ASSIGNMENT"
                        .equalsIgnoreCase(status)) {

                    available++;
                }

                // -------------------------------------------------
                // LOCKED / IN_PROGRESS = Maker Work
                // -------------------------------------------------

                if ("LOCKED".equalsIgnoreCase(status)
                        || "IN_PROGRESS".equalsIgnoreCase(status)) {

                    inProgress++;
                }
            }

            // =================================================
            // UPDATE DASHBOARD CARDS
            // =================================================

            if (totalBatches != null) {

                totalBatches.setValue(
                        String.valueOf(total)
                );
            }

            if (availableBatches != null) {

                availableBatches.setValue(
                        String.valueOf(available)
                );
            }

            if (inProgressBatches != null) {

                inProgressBatches.setValue(
                        String.valueOf(inProgress)
                );
            }

            // =================================================
            // SET LIST MODEL
            // =================================================

            ListModelList<Batch> model =
                    new ListModelList<>(batches);

            batchList.setModel(model);

            // =================================================
            // RENDER BATCH TABLE
            // =================================================

            batchList.setItemRenderer(
                    new ListitemRenderer<Batch>() {

                        @Override
                        public void render(
                                Listitem item,
                                Batch batch,
                                int index) {

                            // =================================================
                            // 1. BATCH NUMBER
                            // =================================================

                            Listcell batchNumberCell =
                                    new Listcell();

                            batchNumberCell.setLabel(
                                    batch.getBatchNumber() != null
                                            ? batch.getBatchNumber()
                                            : "-"
                            );

                            item.appendChild(
                                    batchNumberCell
                            );

                            // =================================================
                            // 2. TOTAL CHEQUES
                            // =================================================

                            Listcell totalChequeCell =
                                    new Listcell();

                            String totalCheques = "0";

                            if (batch.getTotalCheques() != null) {

                                totalCheques =
                                        String.valueOf(
                                                batch.getTotalCheques()
                                        );
                            }

                            totalChequeCell.setLabel(
                                    totalCheques
                            );

                            item.appendChild(
                                    totalChequeCell
                            );

                            // =================================================
                            // 3. STATUS
                            // =================================================

                            Listcell statusCell =
                                    new Listcell();

                            String status =
                                    batch.getBatchStatus();

                            if (status == null) {
                                status = "-";
                            }

                            statusCell.setLabel(
                                    status
                            );

                            item.appendChild(
                                    statusCell
                            );

                            // =================================================
                            // 4. CREATED BY
                            // =================================================

                            Listcell userCell =
                                    new Listcell();

                            String userId = "-";

                            if (batch.getCreatedBy() != null) {

                                userId =
                                        String.valueOf(
                                                batch.getCreatedBy()
                                        );
                            }

                            userCell.setLabel(
                                    userId
                            );

                            item.appendChild(
                                    userCell
                            );

                            // =================================================
                            // 5. ASSIGNMENT
                            // =================================================

                            Listcell assignmentCell =
                                    new Listcell();

                            // -------------------------------------------------
                            // READY FOR ASSIGNMENT
                            // -------------------------------------------------

                            if ("READY_FOR_ASSIGNMENT"
                                    .equalsIgnoreCase(status)) {

                                Button assignButton =
                                        new Button(
                                                "Assign to Me"
                                        );

                                assignButton.setSclass(
                                        "assign-button"
                                );

                                assignButton.addEventListener(
                                        "onClick",
                                        event -> assignBatch(batch)
                                );

                                assignmentCell.appendChild(
                                        assignButton
                                );
                            }

                            // -------------------------------------------------
                            // LOCKED
                            // -------------------------------------------------

                            else if ("LOCKED"
                                    .equalsIgnoreCase(status)) {

                                if (isMakerBatch(batch)) {

                                    assignmentCell.setLabel(
                                            "Assigned to Me"
                                    );

                                } else {

                                    assignmentCell.setLabel(
                                            "Assigned"
                                    );
                                }
                            }

                            // -------------------------------------------------
                            // IN PROGRESS
                            // -------------------------------------------------

                            else if ("IN_PROGRESS"
                                    .equalsIgnoreCase(status)) {

                                if (isMakerBatch(batch)) {

                                    assignmentCell.setLabel(
                                            "In Progress"
                                    );

                                } else {

                                    assignmentCell.setLabel(
                                            "Assigned"
                                    );
                                }
                            }

                            // -------------------------------------------------
                            // OTHER STATUS
                            // -------------------------------------------------

                            else {

                                assignmentCell.setLabel(
                                        "-"
                                );
                            }

                            item.appendChild(
                                    assignmentCell
                            );

                            // =================================================
                            // 6. OPEN BUTTON
                            // =================================================

                            Listcell openCell =
                                    new Listcell();

                            Button openButton =
                                    new Button("Open");

                            openButton.setSclass(
                                    "open-button"
                            );

                            // -------------------------------------------------
                            // Only current maker can open
                            // -------------------------------------------------

                            if (isMakerBatch(batch)) {

                                openButton.addEventListener(
                                        "onClick",
                                        event -> openBatch(batch)
                                );

                                openButton.setDisabled(false);

                            } else {

                                openButton.setDisabled(true);
                            }

                            openCell.appendChild(
                                    openButton
                            );

                            item.appendChild(
                                    openCell
                            );
                        }
                    }
            );

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to load batches from database:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK MAKER ASSIGNMENT
    // =========================================================

    private boolean isMakerBatch(Batch batch) {

        if (batch == null) {
            return false;
        }

        if (batch.getCreatedBy() == null) {
            return false;
        }

        if (makerId == null) {
            return false;
        }

        return makerId.equals(
                batch.getCreatedBy()
        );
    }

    // =========================================================
    // ASSIGN BATCH TO MAKER
    // =========================================================

    private void assignBatch(Batch batch) {

        if (batch == null) {

            Messagebox.show(
                    "Batch information is missing."
            );

            return;
        }

        String batchNumber =
                batch.getBatchNumber();

        if (batchNumber == null
                || batchNumber.trim().isEmpty()) {

            Messagebox.show(
                    "Batch number is missing."
            );

            return;
        }

        try {

            // -------------------------------------------------
            // Assign batch
            // -------------------------------------------------

            batchService.assignBatchToMaker(
                    batchNumber,
                    makerId
            );

            Messagebox.show(
                    "Batch "
                            + batchNumber
                            + " assigned successfully."
            );

            // -------------------------------------------------
            // Reload dashboard
            // -------------------------------------------------

            loadBatches();

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to assign batch:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // OPEN BATCH
    // =========================================================

    /*
     * Maker Dashboard
     *
     *       |
     *       | Open
     *       v
     *
     * DataEntry.zul?batchNumber=B001
     *
     *       |
     *       v
     *
     * DataEntryController
     *
     *       |
     *       v
     *
     * All cheques of selected batch
     */

    private void openBatch(Batch batch) {

        // -----------------------------------------------------
        // Validate batch
        // -----------------------------------------------------

        if (batch == null) {

            Messagebox.show(
                    "Batch information is missing."
            );

            return;
        }

        // -----------------------------------------------------
        // Security check
        // -----------------------------------------------------

        if (!isMakerBatch(batch)) {

            Messagebox.show(
                    "This batch is not assigned to you."
            );

            return;
        }

        // -----------------------------------------------------
        // Get batch number
        // -----------------------------------------------------

        String batchNumber =
                batch.getBatchNumber();

        if (batchNumber == null
                || batchNumber.trim().isEmpty()) {

            Messagebox.show(
                    "Batch number is missing."
            );

            return;
        }

        // -----------------------------------------------------
        // Open DataEntry.zul
        //
        // IMPORTANT:
        // No encodeURIComponent()
        // -----------------------------------------------------

        try {

            Executions.sendRedirect(
                    "DataEntry.zul?batchNumber="
                            + batchNumber
            );

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Unable to open batch:\n"
                            + e.getMessage()
            );
        }
    }
}

