package github.LadonON.Schema;

import github.LadonON.DatabaseManager;
public interface PluginSchema {
    /*
    Create context for plugin database installs
     */
    String name();
    int latestVersion();
    void migrate(DatabaseManager db, int fromVer, int toVer);
}
