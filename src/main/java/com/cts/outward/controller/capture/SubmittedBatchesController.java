package com.cts.outward.controller.capture;

import com.cts.outward.model.Batch;
import com.cts.outward.repository.BatchRepository;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListitemRenderer;

public class SubmittedBatchesController
        extends SelectorComposer<Component> {

    @Wire
    private Listbox submittedBatchList;

    private final BatchRepository batchRepository =
            new BatchRepository();

    @Override
    public void doAfterCompose(Component component)
            throws Exception {

        super.doAfterCompose(component);

        loadSubmittedBatches();
    }

    private void loadSubmittedBatches() {

        try {

            List<Batch> batches =
                    batchRepository.findSubmittedBatches();

            ListModelList<Batch> model =
                    new ListModelList<>(batches);

            submittedBatchList.setModel(model);

            submittedBatchList.setItemRenderer(
                    new ListitemRenderer<Batch>() {

                        @Override
                        public void render(
                                Listitem item,
                                Batch batch,
                                int index) {

                            // Batch Number
                            item.appendChild(
                                    new Listcell(
                                            batch.getBatchNumber()
                                    )
                            );

                            // Total Cheques
                            String totalCheques = "0";

                            if (batch.getTotalCheques() != null) {

                                totalCheques =
                                        String.valueOf(
                                                batch.getTotalCheques()
                                        );
                            }

                            item.appendChild(
                                    new Listcell(
                                            totalCheques
                                    )
                            );

                            // Status
                            item.appendChild(
                                    new Listcell(
                                            "READY_FOR_ASSIGNMENT"
                                    )
                            );
                        }
                    }
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    @Listen("onClick=#batchCaptureButton")
    public void openBatchCapture() {
        Executions.sendRedirect("batch.zul");
    }
    
    
    
}