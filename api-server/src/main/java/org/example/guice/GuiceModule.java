package org.example.guice;

import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.PathVariables;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Gson.class).toInstance(new GsonBuilder().serializeNulls().create());
		bind(SqlSessionFactory.class).toInstance(getSessionFactory());
	}

	private SqlSessionFactory getSessionFactory() {
		SqlSessionFactory factory;
		String resource = "mybatis-config.xml";
		try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(inputStream, getDBProperties());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.SQL_SESSION_ERROR, e);
		}
		return factory;
	}

	private Properties getDBProperties() {
		Properties properties = new Properties();
		properties.put("driver", PathVariables.getInstance().getString("driver"));
		properties.put("url", PathVariables.getInstance().getString("url"));
		properties.put("username", PathVariables.getInstance().getString("username"));
		properties.put("password", PathVariables.getInstance().getString("password"));
		return properties;
	}

}
