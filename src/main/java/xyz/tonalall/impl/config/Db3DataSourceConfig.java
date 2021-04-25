package xyz.tonalall.impl.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "xyz.tonalall.impl.mapper.Db3", sqlSessionFactoryRef = "db3SqlSessionFactory")
public class Db3DataSourceConfig {
    @Autowired
    private Db3DataSourceProperties db3DataSourceProperties;

    @Bean(name = "db3DataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(db3DataSourceProperties.getDriverClassName());
        dataSource.setUrl(db3DataSourceProperties.getUrl());
        dataSource.setUsername(db3DataSourceProperties.getUsername());
        dataSource.setPassword(db3DataSourceProperties.getPassword());
        dataSource.setInitialSize(db3DataSourceProperties.getInitialSize());
        dataSource.setMinIdle(db3DataSourceProperties.getMinIdle());
        dataSource.setMaxActive(db3DataSourceProperties.getMaxActive());
        return dataSource;
    }

    @Bean(name = "db3SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("db3DataSource") DataSource dataSource,
                                               @Qualifier("camelCaseConfiguration") org.apache.ibatis.session.Configuration configuration) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setConfiguration(configuration);     // 这是个坑。。。
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath:mapper/Db3/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "db3SqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("db3SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "db3TransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("db3DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
