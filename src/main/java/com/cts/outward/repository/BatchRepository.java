package com.cts.outward.repository;

import com.cts.outward.config.DatabaseConnection;
import com.cts.outward.model.Batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchRepository {

    public void save(Batch batch) throws SQLException {

        String sql = "INSERT INTO batch " +
                "(batch_number, branch_code, branch_name, " +
                "capture_date, created_by, total_cheque, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, batch.getBatchNumber());
            statement.setString(2, batch.getBranchCode());
            statement.setString(3, batch.getBranchName());
            statement.setTimestamp(
                    4,
                    java.sql.Timestamp.valueOf(batch.getCaptureDate())
            );
            statement.setInt(5, batch.getCreatedBy());
            statement.setInt(6, batch.getTotalCheque());
            statement.setString(7, batch.getStatus());

            statement.executeUpdate();
        }
    }
}