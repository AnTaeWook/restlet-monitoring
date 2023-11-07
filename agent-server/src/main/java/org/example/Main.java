package org.example;

import org.example.config.DependencyManager;
import org.example.config.XmlRpcServerConf;
import org.example.metric.utils.SystemdNotify;

public class Main {
	public static void main(String[] args) {
		// xml server
		XmlRpcServerConf server = new XmlRpcServerConf();
		server.start();

		// db
		DependencyManager.databaseManager.createTable();

		// zookeeper
		DependencyManager.zookeeperConnection.registerZookeeper();

		// notify ready
		SystemdNotify.ready();
	}
}
