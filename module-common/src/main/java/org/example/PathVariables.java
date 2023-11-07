package org.example;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class PathVariables {

	private Configuration configuration = new Configurations().properties("application.properties");

	public PathVariables() throws ConfigurationException {
	}

	private static class SingletonHelper {
		private static final PathVariables INSTANCE;

		static {
			try {
				INSTANCE = new PathVariables();
			} catch (ConfigurationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Configuration getInstance() {
		return SingletonHelper.INSTANCE.configuration;
	}

}