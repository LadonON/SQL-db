
# SQL-db

A implementation of HikariCP for Minecraft plugins 


## Installation

Add to pom.xml:

```maven
  	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
add the dependency:

```maven 
	<dependency>
	    <groupId>com.github.LadonON</groupId>
	    <artifactId>SQL-db</artifactId>
	    <version>1.0-SNAPSHOT</version>
	</dependency>
```
    
## Usage/Examples

```java
package my.plugin.schema;

import github.LadonON.DatabaseManager;
import github.LadonON.Schema.PluginSchema;

public class BoardSchema implements PluginSchema {

    @Override
    public String name() {
        return "board";
    }

    @Override
    public int latestVersion() {
        return 2;
    }

    @Override
    public void migrate(DatabaseManager db, int from, int to) {

        if (from < 1) {
            db.update("""
                CREATE SCHEMA IF NOT EXISTS board;

                CREATE TABLE board.leaderboards (
                    id TEXT PRIMARY KEY,
                    title TEXT NOT NULL
                );
            """);
        }

        if (from < 2) {
            db.update("""
                ALTER TABLE board.leaderboards
                ADD COLUMN created_at TIMESTAMP DEFAULT now();
            """);
        }
    }
}

```


## License

[MIT](https://choosealicense.com/licenses/mit/)

