package com.cts.outward.controller.capture;

import com.cts.outward.service.XMLService;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class BatchImportController
        extends SelectorComposer<Component> {

    @Wire
    private Textbox batchNumber;

    @Wire
    private Label selectedXmlFile;

    private Media uploadedMedia;

    private String currentBatchNumber;

    private final XMLService xmlService =
            new XMLService();


    @Override
    public void doAfterCompose(Component component)
            throws Exception {

        super.doAfterCompose(component);

        // Get batch number from URL
        currentBatchNumber =
                Executions.getCurrent()
                        .getParameter("batchNumber");

        // Display batch number
        if (currentBatchNumber != null &&
                !currentBatchNumber.isBlank()) {

            batchNumber.setValue(
                    currentBatchNumber
            );
        }
    }


    /*
     * Choose XML File
     */
    @Listen("onUpload=#chooseXmlButton")
    public void chooseXmlFile(UploadEvent event) {

        uploadedMedia = event.getMedia();

        if (uploadedMedia == null) {
            return;
        }

        String fileName =
                uploadedMedia.getName();

        // Check extension
        if (fileName == null ||
                !fileName.toLowerCase()
                        .endsWith(".xml")) {

            Messagebox.show(
                    "Please select a valid XML file."
            );

            uploadedMedia = null;

            selectedXmlFile.setValue(
                    "No XML file selected"
            );

            return;
        }

        // Display selected file name
        selectedXmlFile.setValue(
                fileName
        );
    }


    /*
     * Submit XML
     */
    @Listen("onClick=#submitButton")
    public void submitXML() {

        // Check XML file
        if (uploadedMedia == null) {

            Messagebox.show(
                    "Please select an XML file."
            );

            return;
        }


        // Check batch number
        if (currentBatchNumber == null ||
                currentBatchNumber.isBlank()) {

            Messagebox.show(
                    "Batch number is missing."
            );

            return;
        }


        File tempFile = null;

        try {

            /*
             * Create temporary XML file
             */
            tempFile = File.createTempFile(
                    "cts_batch_",
                    ".xml"
            );


            /*
             * Read uploaded XML
             */
            String xmlContent =
                    uploadedMedia.getStringData();


            /*
             * Write XML to temporary file
             */
            Files.writeString(
                    tempFile.toPath(),
                    xmlContent,
                    StandardCharsets.UTF_8
            );


            /*
             * Import XML
             */
            int count =
                    xmlService.importXML(
                            tempFile.getAbsolutePath(),
                            currentBatchNumber
                    );


            /*
             * Success
             */
            Messagebox.show(
                    "XML imported successfully.\n\n"
                    + "Batch Number: "
                    + currentBatchNumber
                    + "\n"
                    + "Cheques Imported: "
                    + count
            );
         
                Executions.sendRedirect("batch.zul");
            


        } catch (Exception e) {

            e.printStackTrace();

            Messagebox.show(
                    "XML import failed:\n"
                    + e.getMessage()
            );


        } finally {

            /*
             * Delete temporary XML
             */
            if (tempFile != null &&
                    tempFile.exists()) {

                tempFile.delete();
            }
        }
    }
}