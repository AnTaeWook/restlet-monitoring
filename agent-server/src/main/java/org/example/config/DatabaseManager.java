package org.example.config;

import javax.sql.DataSource;

import org.example.PathVariables;
import org.flywaydb.core.Flyway;
import org.sqlite.SQLiteDataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import lombok.Setter;

public class DatabaseManager{
	protected String DB_URL = PathVariables.getInstance().getString("url");

	protected SQLiteDataSource dataSource;

	public DatabaseManager() {
		dataSource = new SQLiteConnectionPoolDataSource();
		dataSource.setUrl(DB_URL);
	}

	public DatabaseManager(String dynamicDbUrl) {
		dataSource = new SQLiteConnectionPoolDataSource();
		dataSource.setUrl(dynamicDbUrl);
	}

	public DataSource getDataSource(){
		return dataSource;
	}

	public void createTable(){
		Flyway flyway = getFlyway();
		flyway.migrate();
	}

	public void dropTable() {
		Flyway flyway = getFlyway();
		flyway.clean();
	}

	private Flyway getFlyway(){
		return Flyway.configure()
			.dataSource(dataSource)
			.load();
	}

}
