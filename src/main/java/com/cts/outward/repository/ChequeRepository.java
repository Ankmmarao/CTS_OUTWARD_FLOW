
package com.cts.outward.repository;

import com.cts.outward.config.DatabaseConnection;
import com.cts.outward.model.Cheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChequeRepository {

    private static final String INSERT_CHEQUE =
            "INSERT INTO cheque " +
            "(cheque_number, batch_number, account_number, branch_code, " +
            "payee_name, amount, front_image_path, back_image_path) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public void save(Cheque cheque) throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(INSERT_CHEQUE)) {

            statement.setString(
                    1,
                    cheque.getChequeNumber()
            );

            statement.setString(
                    2,
                    cheque.getBatchNumber()
            );

            statement.setString(
                    3,
                    cheque.getAccountNumber()
            );

            statement.setString(
                    4,
                    cheque.getBranchCode()
            );

            statement.setString(
                    5,
                    cheque.getPayeeName()
            );

            statement.setBigDecimal(
                    6,
                    cheque.getAmount()
            );

            statement.setString(
                    7,
                    cheque.getFrontImagePath()
            );

            statement.setString(
                    8,
                    cheque.getBackImagePath()
            );

            statement.executeUpdate();
        }
    }
}

