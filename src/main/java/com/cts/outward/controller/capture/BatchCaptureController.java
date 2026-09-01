package com.cts.outward.controller.capture;

import com.cts.outward.model.Batch;
import com.cts.outward.service.BatchService;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class BatchCaptureController
        extends SelectorComposer<Component> {

    @Wire
    private Textbox branchCode;

    @Wire
    private Textbox branchName;

    @Wire
    private Intbox totalCheque;

    private final BatchService batchService =
            new BatchService();


    // ==========================================
    // CREATE BATCH
    // ==========================================

    @Listen("onClick=#createBatchButton")
    public void createBatch() {

        try {

            // Get values from UI
            String branchCodeValue =
                    branchCode.getValue();

            String branchNameValue =
                    branchName.getValue();

            Integer totalChequeValue =
                    totalCheque.getValue();


            // Validation
            if (totalChequeValue == null ||
                    totalChequeValue <= 0) {

                Messagebox.show(
                        "Enter valid total cheques."
                );

                return;
            }


            // Create and save batch
            Batch batch =
                    batchService.createBatch(
                            branchCodeValue,
                            branchNameValue,
                            totalChequeValue,
                            101
                    );


            // Redirect to Batch Import
            Executions.sendRedirect(
                    "batchimport.zul?batchNumber="
                    + batch.getBatchNumber()
            );

        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "Batch creation failed:\n"
                    + e.getMessage()
            );
        }
    }


    // ==========================================
    // OPEN SUBMITTED BATCHES
    // ==========================================

    @Listen("onClick=#submittedBatchesMenu")
    public void openSubmittedBatches() {

        Executions.sendRedirect(
                "submitted.zul"
        		
        );
    }
}