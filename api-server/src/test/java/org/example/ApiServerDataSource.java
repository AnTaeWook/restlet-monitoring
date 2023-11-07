package org.example;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ApiServerDataSource {

	private final SqlSessionFactory sqlSessionFactory;

	private ApiServerDataSource() {
		try {
			String resource = "mybatis-config.xml";
			InputStream mybatis = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatis, "test");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static SqlSessionFactory sqlSessionFactory() {
		return LazyHolder.INSTANCE.sqlSessionFactory;
	}


	private static class LazyHolder {
		private static final ApiServerDataSource INSTANCE = new ApiServerDataSource();
	}

}
