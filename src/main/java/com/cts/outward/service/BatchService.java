
package com.cts.outward.service;

import com.cts.outward.model.Batch;
import com.cts.outward.repository.BatchRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService() {

        this.batchRepository = new BatchRepository();

    }

    public Batch createBatch(
            String branchCode,
            String branchName,
            Integer totalCheque
    ) throws SQLException {

        Batch batch = new Batch();

        // Auto-generate batch number
        String batchNumber =
                "BATCH" + System.currentTimeMillis();

        batch.setBatchNumber(batchNumber);

        batch.setBranchCode(branchCode);

        batch.setBranchName(branchName);

        batch.setCaptureDate(
                LocalDateTime.now()
        );

        // Temporary static user
        batch.setCreatedBy(1);

        batch.setTotalCheque(totalCheque);

        batch.setStatus("CAPTURED");

        // Save into database
        batchRepository.save(batch);

        return batch;
    }
}