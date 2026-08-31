package com.cts.outward.repository;

import com.cts.outward.config.DatabaseConnection;
import com.cts.outward.model.Cheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChequeRepository {

    public void save(Cheque cheque) throws SQLException {

        String sql =
                "INSERT INTO cheque " +
                "(cheque_number, batch_number, account_number, " +
                "drawer_name, amount, micr_code, ifsc_code, " +
                "status, checked_date, front_image_path, " +
                "back_image_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // 1. Cheque Number
            statement.setString(
                    1,
                    cheque.getChequeNumber()
            );

            // 2. Batch Number
            statement.setString(
                    2,
                    cheque.getBatchNumber()
            );

            // 3. Account Number
            statement.setString(
                    3,
                    cheque.getAccountNumber()
            );

            // 4. Drawer Name
            statement.setString(
                    4,
                    cheque.getDrawerName()
            );

            // 5. Amount
            statement.setBigDecimal(
                    5,
                    cheque.getAmount()
            );

            // 6. MICR Code
            statement.setString(
                    6,
                    cheque.getMicrCode()
            );

            // 7. IFSC Code
            statement.setString(
                    7,
                    cheque.getIfscCode()
            );

            // 8. Status
            statement.setString(
                    8,
                    cheque.getStatus()
            );

            // 9. Checked Date
            if (cheque.getCheckedDate() != null) {

                statement.setDate(
                        9,
                        java.sql.Date.valueOf(
                                cheque.getCheckedDate()
                        )
                );

            } else {

                statement.setNull(
                        9,
                        java.sql.Types.DATE
                );
            }

            // 10. Front Image Path
            statement.setString(
                    10,
                    cheque.getFrontImagePath()
            );

            // 11. Back Image Path
            statement.setString(
                    11,
                    cheque.getBackImagePath()
            );

            statement.executeUpdate();
        }
    }
}