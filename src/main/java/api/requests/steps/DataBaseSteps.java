package api.requests.steps;

import api.configs.Config;
import common.helpers.StepLogger;
import dao.AccountDao;
import dao.UserDao;
import database.Condition;
import database.DBRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseSteps {
    private static final int DB_READ_RETRIES = 15;
    private static final long DB_READ_RETRY_DELAY_MS = 200L;

    private static <T> T withDbRetries(java.util.function.Supplier<T> query) {
        T result = null;
        for (int attempt = 1; attempt <= DB_READ_RETRIES; attempt++) {
            result = query.get();
            if (result != null) {
                return result;
            }
            if (attempt < DB_READ_RETRIES) {
                try {
                    Thread.sleep(DB_READ_RETRY_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return result;
    }

    public static UserDao getUserByUsername(String username) {
        return StepLogger.log("Get user from database by username: " + username + " using db.url=" + Config.getProperty("db.url"), () ->
                withDbRetries(() -> {
                    try (Connection connection = DriverManager.getConnection(
                            Config.getProperty("db.url"),
                            Config.getProperty("db.username"),
                            Config.getProperty("db.password"));
                         PreparedStatement statement = connection.prepareStatement(
                                 "SELECT id, username, password, role, name FROM customers WHERE username = ?")) {
                        statement.setString(1, username);
                        try (var resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                return UserDao.builder()
                                        .id(resultSet.getLong("id"))
                                        .username(resultSet.getString("username"))
                                        .password(resultSet.getString("password"))
                                        .role(resultSet.getString("role"))
                                        .name(resultSet.getString("name"))
                                        .build();
                            }
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to get user by username from DB", e);
                    }
                })
        );
    }

    public static UserDao getUserById(Long id) {
        System.out.println("[DB-CHECK] getUserById id=" + id + " db.url=" + Config.getProperty("db.url"));
        return StepLogger.log("Get user from database by ID: " + id + " using db.url=" + Config.getProperty("db.url"), () ->
                withDbRetries(() -> {
                    try (Connection connection = DriverManager.getConnection(
                            Config.getProperty("db.url"),
                            Config.getProperty("db.username"),
                            Config.getProperty("db.password"));
                         PreparedStatement statement = connection.prepareStatement(
                                 "SELECT id, username, password, role, name FROM customers WHERE id = ?")) {
                        statement.setLong(1, id);
                        try (var resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                return UserDao.builder()
                                        .id(resultSet.getLong("id"))
                                        .username(resultSet.getString("username"))
                                        .password(resultSet.getString("password"))
                                        .role(resultSet.getString("role"))
                                        .name(resultSet.getString("name"))
                                        .build();
                            }
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to get user by id from DB", e);
                    }
                })
        );
    }

    public static UserDao getUserByRole(String role) {
        return StepLogger.log("Get user from database by role: " + role, () -> DBRequest.builder()
                .requestType(DBRequest.RequestType.SELECT)
                .table("customers")
                .where(Condition.equalTo("role", role))
                .extractAs(UserDao.class));
    }

    public static AccountDao getAccountByAccountNumber(String accountNumber) {
        return StepLogger.log("Get account from database by account number: " + accountNumber, () -> DBRequest.builder()
                .requestType(DBRequest.RequestType.SELECT)
                .table("accounts")
                .where(Condition.equalTo("account_number", accountNumber))
                .extractAs(AccountDao.class));
    }

    public static AccountDao getAccountById(Long id) {
        return StepLogger.log("Get account from database by ID: " + id, () -> DBRequest.builder()
                .requestType(DBRequest.RequestType.SELECT)
                .table("accounts")
                .where(Condition.equalTo("id", id))
                .extractAs(AccountDao.class));
    }

    public static AccountDao getAccountByCustomerId(Long customerId) {
        return StepLogger.log("Get account from database by customer ID: " + customerId, () -> DBRequest.builder()
                .requestType(DBRequest.RequestType.SELECT)
                .table("customers")
                .where(Condition.equalTo("customer_id", customerId))
                .extractAs(AccountDao.class));
    }

    public static void updateAccountBalance(Long accountId, Double newBalance) {
        StepLogger.log("Update account balance in database for account ID: " + accountId + " to: " + newBalance, () -> {
            try (Connection connection = DriverManager.getConnection(
                    Config.getProperty("db.url"),
                    Config.getProperty("db.username"),
                    Config.getProperty("db.password"))) {

                String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDouble(1, newBalance);
                    statement.setLong(2, accountId);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new RuntimeException("No account found with ID: " + accountId);
                    }

                    return rowsAffected;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update account balance", e);
            }
        });
    }
}
