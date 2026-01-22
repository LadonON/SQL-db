package github.LadonON;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(String host, int port, String user, String database, String password) {
        //Configure the database
        //TODO: Create a config file
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10_000);
        config.setLeakDetectionThreshold(30_000);
        this.dataSource = new HikariDataSource(config);
    }

    public void query(String sql, Consumer<ResultSet> handler, Object... params) {
        /*
        Get data from a table
         */
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            bind(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                handler.accept(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    public int update(String sql, Object... params) {
        /*
        Add values to database
         */
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            bind(stmt, params);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    private void bind(PreparedStatement stmt, Object @NotNull [] params) throws SQLException {
        /*
        Bind SQL code without injection
         */
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    public void close() {
        /*
        Close the connection
         */
        dataSource.close();
    }
}
