package com.cts.outward.service;

import com.cts.outward.model.Batch;
import com.cts.outward.repository.BatchRepository;

import java.sql.SQLException;

public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService() {
        this.batchRepository = new BatchRepository();
    }

    public Batch createBatch(
            String branchCode,
            String branchName,
            Integer totalCheques,
            Integer createdBy
    ) throws SQLException {

        Batch batch = new Batch();

        // Generate batch number
        String batchNumber =
                "BATCH" + System.currentTimeMillis();

        batch.setBatchNumber(batchNumber);

        // Branch details
        batch.setBranchCode(branchCode);
        batch.setBranchName(branchName);

        // User who created the batch
        batch.setCreatedBy(createdBy);

        // Total number of cheques
        batch.setTotalCheques(totalCheques);

        // Save batch
        batchRepository.save(batch);

        return batch;
    }
}