
package com.cts.outward.service;

import com.cts.outward.model.Batch;
import com.cts.outward.repository.BatchRepository;

import java.sql.SQLException;
import java.util.List;

public class BatchService {

    // =========================================================
    // REPOSITORY
    // =========================================================

    private final BatchRepository batchRepository;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public BatchService() {

        this.batchRepository = new BatchRepository();

    }

    // =========================================================
    // CREATE / SAVE BATCH
    // =========================================================

    public void createBatch(Batch batch)
            throws SQLException {

        if (batch == null) {

            throw new IllegalArgumentException(
                    "Batch cannot be null"
            );

        }

        if (batch.getBatchNumber() == null
                || batch.getBatchNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Batch number is required"
            );

        }

        // -----------------------------------------------------
        // Default status for newly created batch
        // -----------------------------------------------------

        if (batch.getBatchStatus() == null
                || batch.getBatchStatus().trim().isEmpty()) {

            batch.setBatchStatus("CREATED");

        }

        // -----------------------------------------------------
        // Save into database
        // -----------------------------------------------------

        batchRepository.save(batch);
    }

    // =========================================================
    // FIND SUBMITTED BATCHES
    //
    // Used by Capture Operator / Maker
    // =========================================================

    public List<Batch> findSubmittedBatches()
            throws SQLException {

        return batchRepository.findSubmittedBatches();

    }

    // =========================================================
    // FIND MAKER BATCHES
    //
    // Returns:
    //
    // 1. SUBMITTED batches
    // 2. LOCKED batches belonging to current Maker
    // 3. IN_PROGRESS batches belonging to current Maker
    // =========================================================

    public List<Batch> findMakerBatches(
            Integer makerId
    ) throws SQLException {

        if (makerId == null) {

            throw new IllegalArgumentException(
                    "Maker ID cannot be null"
            );

        }

        return batchRepository.findMakerBatches(
                makerId
        );

    }

    // =========================================================
    // ASSIGN BATCH TO MAKER
    //
    // Maker clicks "Assign to Me"
    //
    // SUBMITTED
    //      ↓
    // LOCKED
    //
    // created_by = makerId
    // =========================================================

    public void assignBatchToMaker(
            String batchNumber,
            Integer makerId
    ) throws SQLException {

        if (batchNumber == null
                || batchNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Batch number is required"
            );

        }

        if (makerId == null) {

            throw new IllegalArgumentException(
                    "Maker ID is required"
            );

        }

        batchRepository.assignBatchToMaker(
                batchNumber,
                makerId
        );

    }

    // =========================================================
    // FIND SINGLE BATCH
    //
    // Used when Maker opens a batch
    // =========================================================

    public Batch findByBatchNumber(
            String batchNumber
    ) throws SQLException {

        if (batchNumber == null
                || batchNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Batch number is required"
            );

        }

        return batchRepository.findByBatchNumber(
                batchNumber
        );

    }

}

