
package com.cts.outward.repository;

import com.cts.outward.config.DatabaseConnection;
import com.cts.outward.model.Batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchRepository {

    private static final String INSERT_BATCH =
            "INSERT INTO batch " +
            "(batch_number, branch_code, branch_name, capture_date, " +
            "created_by, total_cheque, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void save(Batch batch) throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(INSERT_BATCH)) {

            statement.setString(
                    1,
                    batch.getBatchNumber()
            );

            statement.setString(
                    2,
                    batch.getBranchCode()
            );

            statement.setString(
                    3,
                    batch.getBranchName()
            );

            statement.setTimestamp(
                    4,
                    java.sql.Timestamp.valueOf(
                            batch.getCaptureDate()
                    )
            );

            if (batch.getCreatedBy() != null) {

                statement.setInt(
                        5,
                        batch.getCreatedBy()
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.INTEGER
                );
            }

            if (batch.getTotalCheque() != null) {

                statement.setInt(
                        6,
                        batch.getTotalCheque()
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.INTEGER
                );
            }

            statement.setString(
                    7,
                    batch.getStatus()
            );

            statement.executeUpdate();
        }
    }
}

