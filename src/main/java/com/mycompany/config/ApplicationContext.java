package com.mycompany.config;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@ComponentScan("com.mycompany")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@PropertySource("classpath:/jdbc.properties")
public class ApplicationContext {

	/**
	 * spring接管环境属性
	 */
	@Autowired
	Environment env;

	/**
	 * spring接管c3p0数据源
	 * 
	 * @return
	 * @throws PropertyVetoException
	 */
	@Bean(name = "dataSource", destroyMethod = "close")
	public ComboPooledDataSource getComboPooledDataSource() throws PropertyVetoException {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		
		ds.setDriverClass(env.getProperty("jdbc.driverClassName"));
		ds.setJdbcUrl(env.getProperty("jdbc.url"));
		ds.setUser(env.getProperty("jdbc.username"));
		ds.setPassword(env.getProperty("jdbc.password"));
		
		//设置c3p0属性
		ds.setMinPoolSize(5);
		ds.setMaxPoolSize(30);
		ds.setMaxStatements(50);
		
		return ds;
	}

	/**
	 * spring通过JPA接管hibbernate5的entityManagerFactory
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean("entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(
			@Value("#{dataSource}") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		
		/**
		 * 供应商适配器，指明所使用的是哪一个厂商的JPA实现
		 */
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
		emf.setJpaVendorAdapter(adapter);

		/**
		 * JPA接管hibernate.cfg.xml里的属性设置
		 */
		Map<String, String> map = new HashMap<String, String>();
		map.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		map.put("hibernate.hbm2ddl.auto", "update");
		map.put("hibernate.show_sql", "true");
		
		//设置二级缓存
		map.put("hibernate.cache.use_second_level_cache", "true");
		map.put("hibernate.cache.use_query_cache", "true");
		map.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
		map.put("hibernate.javax.cache.missing_cache_strategy", "create");
		
		emf.setJpaPropertyMap(map);

		emf.setPackagesToScan("com.mycompany.entity");
		return emf;
	}

	/**
	 * spring通过JPA接管hibbernate5的事务管理
	 * EnableTransactionManagement注解需要需要这个TransactionManager事务管理bean
	 * 
	 * @param ds
	 * @return
	 */
	@Bean(name = "transactionManager")
	public JpaTransactionManager getPlatformTransactionManager(
			@Value("#{entityManagerFactory}") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
