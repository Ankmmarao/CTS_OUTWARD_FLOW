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
    private Label selectedFile;

    private Media uploadedMedia;

    private String currentBatchNumber;

    private final XMLService xmlService =
            new XMLService();

    @Override
    public void doAfterCompose(Component component)
            throws Exception {

        super.doAfterCompose(component);

        currentBatchNumber =
                Executions.getCurrent()
                        .getParameter("batchNumber");

        if (currentBatchNumber != null) {

            batchNumber.setValue(
                    currentBatchNumber
            );
        }
    }

    @Listen("onUpload=#chooseFileButton")
    public void chooseFile(UploadEvent event) {

        uploadedMedia = event.getMedia();

        if (uploadedMedia == null) {
            return;
        }

        selectedFile.setValue(
                uploadedMedia.getName()
        );
    }

    @Listen("onClick=#importButton")
    public void importXML() {

        if (uploadedMedia == null) {

            Messagebox.show(
                    "Please select an XML file."
            );

            return;
        }

        if (!uploadedMedia.getName()
                .toLowerCase()
                .endsWith(".xml")) {

            Messagebox.show(
                    "Please select a valid XML file."
            );

            return;
        }

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
             * Read uploaded XML as String
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
             * Parse XML and save
             * Cheque objects into DB
             */
            int count =
                    xmlService.importXML(
                            tempFile.getAbsolutePath(),
                            currentBatchNumber
                    );

            Messagebox.show(
                    "XML imported successfully.\n\n"
                    + "Batch Number: "
                    + currentBatchNumber
                    + "\n"
                    + "Cheques Imported: "
                    + count
            );

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