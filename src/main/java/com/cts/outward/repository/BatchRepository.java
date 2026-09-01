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
                "completed_date, total_cheques) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // 1. Batch Number
            statement.setString(
                    1,
                    batch.getBatchNumber()
            );

            // 2. Branch Code
            statement.setString(
                    2,
                    batch.getBranchCode()
            );

            // 3. Branch Name
            statement.setString(
                    3,
                    batch.getBranchName()
            );

            // 4. Created By
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

            // 5. Schedule Date
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

            // 6. Submitted Date
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

            // 7. Completed Date
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

            // 8. Total Cheques
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

            statement.executeUpdate();
        }
    }


    // =========================================================
    // FIND SUBMITTED BATCHES
    // =========================================================

    public List<Batch> findSubmittedBatches()
            throws SQLException {

        List<Batch> batches =
                new ArrayList<>();

        String sql =
                "SELECT batch_number, " +
                "created_by, " +
                "submitted_date, " +
                "total_cheques " +
                "FROM batch " +
                "WHERE submitted_date IS NOT NULL " +
                "ORDER BY submitted_date DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Batch batch =
                        new Batch();

                // Batch Number
                batch.setBatchNumber(
                        resultSet.getString(
                                "batch_number"
                        )
                );

                // Created By
                int createdBy =
                        resultSet.getInt(
                                "created_by"
                        );

                if (!resultSet.wasNull()) {

                    batch.setCreatedBy(
                            createdBy
                    );
                }

                // Submitted Date
                Timestamp submittedDate =
                        resultSet.getTimestamp(
                                "submitted_date"
                        );

                if (submittedDate != null) {

                    batch.setSubmittedDate(
                            submittedDate.toLocalDateTime()
                    );
                }

                // Total Cheques
                int totalCheques =
                        resultSet.getInt(
                                "total_cheques"
                        );

                if (!resultSet.wasNull()) {

                    batch.setTotalCheques(
                            totalCheques
                    );
                }

                batches.add(batch);
            }
        }

        return batches;
    }
}