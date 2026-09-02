package com.cts.outward.repository;

import com.cts.outward.config.DatabaseConnection;
import com.cts.outward.model.Batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BatchRepository {

    // =========================================================
    // SAVE BATCH
    // =========================================================

    public void save(Batch batch) throws SQLException {

        String sql =
                "INSERT INTO batch " +
                "(batch_number, branch_code, branch_name, " +
                "created_by, schedule_date, submitted_date, " +
                "completed_date, total_cheques, batch_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // 1. BATCH NUMBER
            statement.setString(
                    1,
                    batch.getBatchNumber()
            );

            // 2. BRANCH CODE
            statement.setString(
                    2,
                    batch.getBranchCode()
            );

            // 3. BRANCH NAME
            statement.setString(
                    3,
                    batch.getBranchName()
            );

            // 4. CREATED BY
            if (batch.getCreatedBy() != null) {

                statement.setInt(
                        4,
                        batch.getCreatedBy()
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            // 5. SCHEDULE DATE
            if (batch.getScheduleDate() != null) {

                statement.setDate(
                        5,
                        java.sql.Date.valueOf(
                                batch.getScheduleDate()
                        )
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }

            // 6. SUBMITTED DATE
            if (batch.getSubmittedDate() != null) {

                statement.setTimestamp(
                        6,
                        Timestamp.valueOf(
                                batch.getSubmittedDate()
                        )
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.TIMESTAMP
                );
            }

            // 7. COMPLETED DATE
            if (batch.getCompletedDate() != null) {

                statement.setTimestamp(
                        7,
                        Timestamp.valueOf(
                                batch.getCompletedDate()
                        )
                );

            } else {

                statement.setNull(
                        7,
                        java.sql.Types.TIMESTAMP
                );
            }

            // 8. TOTAL CHEQUES
            if (batch.getTotalCheques() != null) {

                statement.setInt(
                        8,
                        batch.getTotalCheques()
                );

            } else {

                statement.setNull(
                        8,
                        java.sql.Types.INTEGER
                );
            }

            // 9. BATCH STATUS
            statement.setString(
                    9,
                    batch.getBatchStatus()
            );

            statement.executeUpdate();
        }
    }


    // =========================================================
    // FIND BATCHES READY FOR MAKER
    //
    // Capture Operator creates:
    //
    // READY_FOR_ASSIGNMENT
    //
    // These batches are available for Maker.
    // =========================================================

    public List<Batch> findReadyForAssignmentBatches()
            throws SQLException {

        List<Batch> batches =
                new ArrayList<>();

        String sql =
                "SELECT batch_number, " +
                "branch_code, " +
                "branch_name, " +
                "created_by, " +
                "schedule_date, " +
                "submitted_date, " +
                "completed_date, " +
                "total_cheques, " +
                "batch_status " +
                "FROM batch " +
                "WHERE batch_status = ? " +
                "ORDER BY submitted_date DESC NULLS LAST";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "READY_FOR_ASSIGNMENT"
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Batch batch =
                            new Batch();

                    mapBatch(
                            resultSet,
                            batch
                    );

                    batches.add(batch);
                }
            }
        }

        return batches;
    }


    // =========================================================
    // OLD METHOD
    //
    // Kept for compatibility with existing Capture Operator
    // controller/service code.
    //
    // It now returns READY_FOR_ASSIGNMENT batches.
    // =========================================================

    public List<Batch> findSubmittedBatches()
            throws SQLException {

        return findReadyForAssignmentBatches();
    }


    // =========================================================
    // FIND MAKER BATCHES
    //
    // Maker sees:
    //
    // 1. READY_FOR_ASSIGNMENT
    //      -> Available
    //
    // 2. LOCKED belonging to current maker
    //      -> Assigned
    //
    // 3. IN_PROGRESS belonging to current maker
    //      -> In Progress
    //
    // Another maker's LOCKED/IN_PROGRESS batches
    // are NOT displayed.
    // =========================================================

    public List<Batch> findMakerBatches(
            Integer makerId
    ) throws SQLException {

        List<Batch> batches =
                new ArrayList<>();

        String sql =
                "SELECT batch_number, " +
                "branch_code, " +
                "branch_name, " +
                "created_by, " +
                "schedule_date, " +
                "submitted_date, " +
                "completed_date, " +
                "total_cheques, " +
                "batch_status " +
                "FROM batch " +
                "WHERE batch_status = ? " +
                "OR (created_by = ? " +
                "AND batch_status IN (?, ?)) " +
                "ORDER BY submitted_date DESC NULLS LAST";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // =================================================
            // 1. AVAILABLE BATCHES
            // =================================================

            statement.setString(
                    1,
                    "READY_FOR_ASSIGNMENT"
            );

            // =================================================
            // 2. CURRENT MAKER
            // =================================================

            if (makerId != null) {

                statement.setInt(
                        2,
                        makerId
                );

            } else {

                statement.setNull(
                        2,
                        java.sql.Types.INTEGER
                );
            }

            // =================================================
            // 3. LOCKED
            // =================================================

            statement.setString(
                    3,
                    "LOCKED"
            );

            // =================================================
            // 4. IN PROGRESS
            // =================================================

            statement.setString(
                    4,
                    "IN_PROGRESS"
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Batch batch =
                            new Batch();

                    mapBatch(
                            resultSet,
                            batch
                    );

                    batches.add(batch);
                }
            }
        }

        return batches;
    }


    // =========================================================
    // ASSIGN BATCH TO MAKER
    //
    // Before:
    //
    // READY_FOR_ASSIGNMENT
    //
    // After:
    //
    // created_by = makerId
    // batch_status = LOCKED
    //
    // The WHERE condition prevents two makers from
    // assigning the same batch.
    // =========================================================

    public void assignBatchToMaker(
            String batchNumber,
            Integer makerId
    ) throws SQLException {

        if (batchNumber == null ||
                batchNumber.trim().isEmpty()) {

            throw new SQLException(
                    "Batch number is required."
            );
        }

        if (makerId == null) {

            throw new SQLException(
                    "Maker ID is required."
            );
        }

        String sql =
                "UPDATE batch " +
                "SET created_by = ?, " +
                "batch_status = ? " +
                "WHERE batch_number = ? " +
                "AND batch_status = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // =================================================
            // 1. MAKER ID
            // =================================================

            statement.setInt(
                    1,
                    makerId
            );

            // =================================================
            // 2. NEW STATUS
            // =================================================

            statement.setString(
                    2,
                    "LOCKED"
            );

            // =================================================
            // 3. BATCH NUMBER
            // =================================================

            statement.setString(
                    3,
                    batchNumber
            );

            // =================================================
            // 4. CURRENT STATUS
            // =================================================

            statement.setString(
                    4,
                    "READY_FOR_ASSIGNMENT"
            );

            // =================================================
            // EXECUTE
            // =================================================

            int updatedRows =
                    statement.executeUpdate();

            // =================================================
            // NO ROW UPDATED
            // =================================================

            if (updatedRows == 0) {

                throw new SQLException(
                        "Batch is already assigned " +
                        "or not available: "
                                + batchNumber
                );
            }
        }
    }


    // =========================================================
    // MOVE LOCKED BATCH TO IN_PROGRESS
    //
    // Call this when Maker actually opens/starts processing
    // the batch.
    // =========================================================

    public void markBatchInProgress(
            String batchNumber,
            Integer makerId
    ) throws SQLException {

        if (batchNumber == null ||
                batchNumber.trim().isEmpty()) {

            throw new SQLException(
                    "Batch number is required."
            );
        }

        if (makerId == null) {

            throw new SQLException(
                    "Maker ID is required."
            );
        }

        String sql =
                "UPDATE batch " +
                "SET batch_status = ? " +
                "WHERE batch_number = ? " +
                "AND created_by = ? " +
                "AND batch_status = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // NEW STATUS
            statement.setString(
                    1,
                    "IN_PROGRESS"
            );

            // BATCH NUMBER
            statement.setString(
                    2,
                    batchNumber
            );

            // MAKER
            statement.setInt(
                    3,
                    makerId
            );

            // OLD STATUS
            statement.setString(
                    4,
                    "LOCKED"
            );

            int updatedRows =
                    statement.executeUpdate();

            if (updatedRows == 0) {

                throw new SQLException(
                        "Batch is not assigned to this maker " +
                        "or is not locked: "
                                + batchNumber
                );
            }
        }
    }


    // =========================================================
    // FIND SINGLE BATCH
    // =========================================================

    public Batch findByBatchNumber(
            String batchNumber
    ) throws SQLException {

        String sql =
                "SELECT batch_number, " +
                "branch_code, " +
                "branch_name, " +
                "created_by, " +
                "schedule_date, " +
                "submitted_date, " +
                "completed_date, " +
                "total_cheques, " +
                "batch_status " +
                "FROM batch " +
                "WHERE batch_number = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    batchNumber
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    Batch batch =
                            new Batch();

                    mapBatch(
                            resultSet,
                            batch
                    );

                    return batch;
                }
            }
        }

        return null;
    }


    // =========================================================
    // COMPLETE BATCH
    //
    // When all cheque processing is completed:
    //
    // IN_PROGRESS
    //       ↓
    // COMPLETED
    // =========================================================

    public void completeBatch(
            String batchNumber,
            Integer makerId
    ) throws SQLException {

        if (batchNumber == null ||
                batchNumber.trim().isEmpty()) {

            throw new SQLException(
                    "Batch number is required."
            );
        }

        if (makerId == null) {

            throw new SQLException(
                    "Maker ID is required."
            );
        }

        String sql =
                "UPDATE batch " +
                "SET batch_status = ?, " +
                "completed_date = CURRENT_TIMESTAMP " +
                "WHERE batch_number = ? " +
                "AND created_by = ? " +
                "AND batch_status = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "COMPLETED"
            );

            statement.setString(
                    2,
                    batchNumber
            );

            statement.setInt(
                    3,
                    makerId
            );

            statement.setString(
                    4,
                    "IN_PROGRESS"
            );

            int updatedRows =
                    statement.executeUpdate();

            if (updatedRows == 0) {

                throw new SQLException(
                        "Unable to complete batch: "
                                + batchNumber
                );
            }
        }
    }


    // =========================================================
    // MAP RESULTSET TO BATCH
    // =========================================================

    private void mapBatch(
            ResultSet resultSet,
            Batch batch
    ) throws SQLException {

        // =====================================================
        // BATCH NUMBER
        // =====================================================

        batch.setBatchNumber(
                resultSet.getString(
                        "batch_number"
                )
        );

        // =====================================================
        // BRANCH CODE
        // =====================================================

        batch.setBranchCode(
                resultSet.getString(
                        "branch_code"
                )
        );

        // =====================================================
        // BRANCH NAME
        // =====================================================

        batch.setBranchName(
                resultSet.getString(
                        "branch_name"
                )
        );

        // =====================================================
        // CREATED BY / MAKER ID
        // =====================================================

        int createdBy =
                resultSet.getInt(
                        "created_by"
                );

        if (!resultSet.wasNull()) {

            batch.setCreatedBy(
                    createdBy
            );
        }

        // =====================================================
        // SCHEDULE DATE
        // =====================================================

        java.sql.Date scheduleDate =
                resultSet.getDate(
                        "schedule_date"
                );

        if (scheduleDate != null) {

            batch.setScheduleDate(
                    scheduleDate.toLocalDate()
            );
        }

        // =====================================================
        // SUBMITTED DATE
        // =====================================================

        Timestamp submittedDate =
                resultSet.getTimestamp(
                        "submitted_date"
                );

        if (submittedDate != null) {

            batch.setSubmittedDate(
                    submittedDate.toLocalDateTime()
            );
        }

        // =====================================================
        // COMPLETED DATE
        // =====================================================

        Timestamp completedDate =
                resultSet.getTimestamp(
                        "completed_date"
                );

        if (completedDate != null) {

            batch.setCompletedDate(
                    completedDate.toLocalDateTime()
            );
        }

        // =====================================================
        // TOTAL CHEQUES
        // =====================================================

        int totalCheques =
                resultSet.getInt(
                        "total_cheques"
                );

        if (!resultSet.wasNull()) {

            batch.setTotalCheques(
                    totalCheques
            );
        }

        // =====================================================
        // BATCH STATUS
        // =====================================================

        batch.setBatchStatus(
                resultSet.getString(
                        "batch_status"
                )
        );
    }
}