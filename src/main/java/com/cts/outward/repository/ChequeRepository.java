
package com.cts.outward.repository;

import com.cts.outward.config.DatabaseConnection;
import com.cts.outward.model.Cheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChequeRepository {

    // =========================================================
    // SAVE CHEQUE
    // =========================================================

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
                    cheque.getDrawerName()
            );

            statement.setBigDecimal(
                    5,
                    cheque.getAmount()
            );

            statement.setString(
                    6,
                    cheque.getMicrCode()
            );

            statement.setString(
                    7,
                    cheque.getIfscCode()
            );

            statement.setString(
                    8,
                    cheque.getStatus()
            );

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

            statement.setString(
                    10,
                    cheque.getFrontImagePath()
            );

            statement.setString(
                    11,
                    cheque.getBackImagePath()
            );

            statement.executeUpdate();
        }
    }

    // =========================================================
    // FIND ALL CHEQUES BY BATCH NUMBER
    // =========================================================

    public List<Cheque> findByBatchNumber(
            String batchNumber) throws SQLException {

        String sql =
                "SELECT cheque_number, batch_number, " +
                "account_number, drawer_name, amount, " +
                "micr_code, ifsc_code, status, checked_date, " +
                "front_image_path, back_image_path " +
                "FROM cheque " +
                "WHERE batch_number = ? " +
                "ORDER BY cheque_number";

        List<Cheque> cheques =
                new ArrayList<>();

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

                while (resultSet.next()) {

                    cheques.add(
                            mapCheque(resultSet)
                    );
                }
            }
        }

        return cheques;
    }

    // =========================================================
    // FIND SINGLE CHEQUE
    // =========================================================

    public Cheque findByBatchNumberAndChequeNumber(
            String batchNumber,
            String chequeNumber) throws SQLException {

        String sql =
                "SELECT cheque_number, batch_number, " +
                "account_number, drawer_name, amount, " +
                "micr_code, ifsc_code, status, checked_date, " +
                "front_image_path, back_image_path " +
                "FROM cheque " +
                "WHERE batch_number = ? " +
                "AND cheque_number = ?";

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

            statement.setString(
                    2,
                    chequeNumber
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapCheque(resultSet);
                }
            }
        }

        return null;
    }

    // =========================================================
    // UPDATE CHEQUE
    // =========================================================

    public void update(Cheque cheque)
            throws SQLException {

        String sql =
                "UPDATE cheque SET " +
                "account_number = ?, " +
                "drawer_name = ?, " +
                "amount = ?, " +
                "micr_code = ?, " +
                "ifsc_code = ?, " +
                "status = ?, " +
                "checked_date = ?, " +
                "front_image_path = ?, " +
                "back_image_path = ? " +
                "WHERE batch_number = ? " +
                "AND cheque_number = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cheque.getAccountNumber()
            );

            statement.setString(
                    2,
                    cheque.getDrawerName()
            );

            statement.setBigDecimal(
                    3,
                    cheque.getAmount()
            );

            statement.setString(
                    4,
                    cheque.getMicrCode()
            );

            statement.setString(
                    5,
                    cheque.getIfscCode()
            );

            statement.setString(
                    6,
                    cheque.getStatus()
            );

            if (cheque.getCheckedDate() != null) {

                statement.setDate(
                        7,
                        java.sql.Date.valueOf(
                                cheque.getCheckedDate()
                        )
                );

            } else {

                statement.setNull(
                        7,
                        java.sql.Types.DATE
                );
            }

            statement.setString(
                    8,
                    cheque.getFrontImagePath()
            );

            statement.setString(
                    9,
                    cheque.getBackImagePath()
            );

            statement.setString(
                    10,
                    cheque.getBatchNumber()
            );

            statement.setString(
                    11,
                    cheque.getChequeNumber()
            );

            statement.executeUpdate();
        }
    }

    // =========================================================
    // MAP RESULTSET TO CHEQUE OBJECT
    // =========================================================

    private Cheque mapCheque(
            ResultSet resultSet) throws SQLException {

        Cheque cheque = new Cheque();

        cheque.setChequeNumber(
                resultSet.getString("cheque_number")
        );

        cheque.setBatchNumber(
                resultSet.getString("batch_number")
        );

        cheque.setAccountNumber(
                resultSet.getString("account_number")
        );

        cheque.setDrawerName(
                resultSet.getString("drawer_name")
        );

        cheque.setAmount(
                resultSet.getBigDecimal("amount")
        );

        cheque.setMicrCode(
                resultSet.getString("micr_code")
        );

        cheque.setIfscCode(
                resultSet.getString("ifsc_code")
        );

        cheque.setStatus(
                resultSet.getString("status")
        );

        java.sql.Date checkedDate =
                resultSet.getDate("checked_date");

        if (checkedDate != null) {

            cheque.setCheckedDate(
                    checkedDate.toLocalDate()
            );
        }

        cheque.setFrontImagePath(
                resultSet.getString("front_image_path")
        );

        cheque.setBackImagePath(
                resultSet.getString("back_image_path")
        );

        return cheque;
    }
}

