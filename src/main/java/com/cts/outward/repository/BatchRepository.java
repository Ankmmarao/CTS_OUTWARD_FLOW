
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

    // ==========================================
    // SAVE BATCH
    // ==========================================

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

            // ==========================================
            // 1. BATCH NUMBER
            // ==========================================

            statement.setString(
                    1,
                    batch.getBatchNumber()
            );

            // ==========================================
            // 2. BRANCH CODE
            // ==========================================

            statement.setString(
                    2,
                    batch.getBranchCode()
            );

            // ==========================================
            // 3. BRANCH NAME
            // ==========================================

            statement.setString(
                    3,
                    batch.getBranchName()
            );

            // ==========================================
            // 4. CREATED BY
            // ==========================================

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

            // ==========================================
            // 5. SCHEDULE DATE
            // ==========================================

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

            // ==========================================
            // 6. SUBMITTED DATE
            // ==========================================

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

            // ==========================================
            // 7. COMPLETED DATE
            // ==========================================

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

            // ==========================================
            // 8. TOTAL CHEQUES
            // ==========================================

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

            // ==========================================
            // 9. BATCH STATUS
            // Database column = batch_status
            // ==========================================

            statement.setString(
                    9,
                    batch.getBatchStatus()
            );

            // ==========================================
            // EXECUTE INSERT
            // ==========================================

            statement.executeUpdate();
        }
    }


    // ==========================================
    // FIND SUBMITTED BATCHES
    // Capture Operator
    // ==========================================

    public List<Batch> findSubmittedBatches()
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
                "ORDER BY submitted_date DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // ==========================================
            // ONLY SUBMITTED BATCHES
            // ==========================================

            statement.setString(
                    1,
                    "SUBMITTED"
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


    // ==========================================
    // FIND MAKER BATCHES
    //
    // Maker Dashboard displays:
    //
    // 1. All SUBMITTED batches
    // 2. LOCKED batches assigned to this maker
    // 3. IN_PROGRESS batches of this maker
    //
    // It does NOT display another maker's
    // LOCKED / IN_PROGRESS batches.
    // ==========================================

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

            // ==========================================
            // 1. AVAILABLE BATCHES
            // ==========================================

            statement.setString(
                    1,
                    "SUBMITTED"
            );

            // ==========================================
            // 2. CURRENT MAKER ID
            // ==========================================

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

            // ==========================================
            // 3. LOCKED
            // ==========================================

            statement.setString(
                    3,
                    "LOCKED"
            );

            // ==========================================
            // 4. IN PROGRESS
            // ==========================================

            statement.setString(
                    4,
                    "IN_PROGRESS"
            );

            // ==========================================
            // EXECUTE QUERY
            // ==========================================

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


    // ==========================================
    // ASSIGN BATCH TO MAKER
    //
    // Maker clicks:
    //
    // ASSIGN TO ME
    //
    // Database changes:
    //
    // created_by  = makerId
    // batch_status = LOCKED
    //
    // Only SUBMITTED batches can be assigned.
    // ==========================================

    public void assignBatchToMaker(
            String batchNumber,
            Integer makerId
    ) throws SQLException {

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

            // ==========================================
            // 1. MAKER ID
            // ==========================================

            if (makerId != null) {

                statement.setInt(
                        1,
                        makerId
                );

            } else {

                statement.setNull(
                        1,
                        java.sql.Types.INTEGER
                );
            }

            // ==========================================
            // 2. NEW STATUS
            // ==========================================

            statement.setString(
                    2,
                    "LOCKED"
            );

            // ==========================================
            // 3. BATCH NUMBER
            // ==========================================

            statement.setString(
                    3,
                    batchNumber
            );

            // ==========================================
            // 4. OLD STATUS
            // ==========================================

            statement.setString(
                    4,
                    "SUBMITTED"
            );

            // ==========================================
            // EXECUTE UPDATE
            // ==========================================

            int updatedRows =
                    statement.executeUpdate();

            // ==========================================
            // CHECK RESULT
            // ==========================================

            if (updatedRows == 0) {

                throw new SQLException(
                        "Batch is already assigned " +
                        "or not available: "
                                + batchNumber
                );
            }
        }
    }


    // ==========================================
    // FIND SINGLE BATCH
    //
    // Used when Maker clicks OPEN
    // ==========================================

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


    // ==========================================
    // MAP RESULTSET TO BATCH
    // ==========================================

    private void mapBatch(
            ResultSet resultSet,
            Batch batch
    ) throws SQLException {

        // ==========================================
        // BATCH NUMBER
        // ==========================================

        batch.setBatchNumber(
                resultSet.getString(
                        "batch_number"
                )
        );

        // ==========================================
        // BRANCH CODE
        // ==========================================

        batch.setBranchCode(
                resultSet.getString(
                        "branch_code"
                )
        );

        // ==========================================
        // BRANCH NAME
        // ==========================================

        batch.setBranchName(
                resultSet.getString(
                        "branch_name"
                )
        );

        // ==========================================
        // CREATED BY
        // ==========================================

        int createdBy =
                resultSet.getInt(
                        "created_by"
                );

        if (!resultSet.wasNull()) {

            batch.setCreatedBy(
                    createdBy
            );
        }

        // ==========================================
        // SCHEDULE DATE
        // ==========================================

        java.sql.Date scheduleDate =
                resultSet.getDate(
                        "schedule_date"
                );

        if (scheduleDate != null) {

            batch.setScheduleDate(
                    scheduleDate.toLocalDate()
            );
        }

        // ==========================================
        // SUBMITTED DATE
        // ==========================================

        Timestamp submittedDate =
                resultSet.getTimestamp(
                        "submitted_date"
                );

        if (submittedDate != null) {

            batch.setSubmittedDate(
                    submittedDate.toLocalDateTime()
            );
        }

        // ==========================================
        // COMPLETED DATE
        // ==========================================

        Timestamp completedDate =
                resultSet.getTimestamp(
                        "completed_date"
                );

        if (completedDate != null) {

            batch.setCompletedDate(
                    completedDate.toLocalDateTime()
            );
        }

        // ==========================================
        // TOTAL CHEQUES
        // ==========================================

        int totalCheques =
                resultSet.getInt(
                        "total_cheques"
                );

        if (!resultSet.wasNull()) {

            batch.setTotalCheques(
                    totalCheques
            );
        }

        // ==========================================
        // BATCH STATUS
        // Database column = batch_status
        // ==========================================

        batch.setBatchStatus(
                resultSet.getString(
                        "batch_status"
                )
        );
    }
}