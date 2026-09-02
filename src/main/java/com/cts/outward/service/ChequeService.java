
package com.cts.outward.service;

import java.sql.SQLException;
import java.util.List;

import com.cts.outward.model.Cheque;
import com.cts.outward.repository.ChequeRepository;

public class ChequeService {

    private final ChequeRepository chequeRepository;

    public ChequeService() {
        this.chequeRepository = new ChequeRepository();
    }

    // =========================================================
    // FIND ALL CHEQUES FOR A BATCH
    // Used by Maker Data Entry screen
    // =========================================================

    public List<Cheque> findChequesByBatch(
            String batchNumber) throws SQLException {

        if (batchNumber == null ||
                batchNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Batch number is required"
            );
        }

        return chequeRepository.findByBatchNumber(
                batchNumber
        );
    }

    // =========================================================
    // FIND SINGLE CHEQUE
    // Used when Maker clicks Open
    // =========================================================

    public Cheque findCheque(
            String batchNumber,
            String chequeNumber) throws SQLException {

        if (batchNumber == null ||
                batchNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Batch number is required"
            );
        }

        if (chequeNumber == null ||
                chequeNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Cheque number is required"
            );
        }

        return chequeRepository.findByBatchNumberAndChequeNumber(
                batchNumber,
                chequeNumber
        );
    }

    // =========================================================
    // UPDATE CHEQUE
    // Used later when Maker saves data
    // =========================================================

    public void updateCheque(
            Cheque cheque) throws SQLException {

        if (cheque == null) {

            throw new IllegalArgumentException(
                    "Cheque cannot be null"
            );
        }

        if (cheque.getChequeNumber() == null ||
                cheque.getChequeNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Cheque number is required"
            );
        }

        if (cheque.getBatchNumber() == null ||
                cheque.getBatchNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Batch number is required"
            );
        }

        chequeRepository.update(cheque);
    }
}

