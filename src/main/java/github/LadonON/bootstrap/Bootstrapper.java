package github.LadonON.bootstrap;

import github.LadonON.DatabaseManager;
import github.LadonON.Schema.PluginSchema;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class Bootstrapper {

    public static void bootstrap(@org.jetbrains.annotations.NotNull DatabaseManager db, @NotNull PluginSchema schema) {

        db.update("""
            CREATE TABLE IF NOT EXISTS plugin_versions (
                plugin TEXT PRIMARY KEY,
                version INT NOT NULL
            )
        """);

        final int[] current = {0};

        db.query(
                "SELECT version FROM plugin_versions WHERE plugin = ?",
                rs -> {
                    try {
                        if (rs.next()) {
                            current[0] = rs.getInt("version");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                schema.name()
        );

        if (current[0] < schema.latestVersion()) {

            schema.migrate(db, current[0], schema.latestVersion());

            db.update(
                    "INSERT INTO plugin_versions (plugin, version) VALUES (?, ?) " +
                            "ON CONFLICT (plugin) DO UPDATE SET version = EXCLUDED.version",
                    schema.name(),
                    schema.latestVersion()
            );
        }
    }
}
