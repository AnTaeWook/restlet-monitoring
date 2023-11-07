package org.example.config;

import org.example.config.DatabaseManager;

public class TestDatabaseManager extends DatabaseManager {

	public TestDatabaseManager(){
		super("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
		createTable();
	}

}
