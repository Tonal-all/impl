package xyz.tonalall.impl.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Primary
@Configuration
@MapperScan(basePackages = "xyz.tonalall.impl.mapper.Db2", sqlSessionFactoryRef = "db2SqlSessionFactory")
public class Db2DataSourceConfig {

//    @Bean(name = "db2DataSource")
//    // 表示这个数据源是默认数据源
//    @Primary//这个一定要加，如果两个数据源都没有@Primary会报错
//    @ConfigurationProperties(prefix = "spring.datasource.primary")//我们配置文件中的前缀
//    public DataSource getPrimaryDateSource() {
//        return DataSourceBuilder.create().build();
//    }
    @Autowired
    private Db2DataSourceProperties db2DataSourceProperties;

    @Bean(name = "db2DataSource")
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(db2DataSourceProperties.getDriverClassName());
        dataSource.setUrl(db2DataSourceProperties.getUrl());
        dataSource.setUsername(db2DataSourceProperties.getUsername());
        dataSource.setPassword(db2DataSourceProperties.getPassword());
        dataSource.setInitialSize(db2DataSourceProperties.getInitialSize());
        dataSource.setMinIdle(db2DataSourceProperties.getMinIdle());
        dataSource.setMaxActive(db2DataSourceProperties.getMaxActive());
        return dataSource;
    }

    @Bean(name = "db2SqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource,
                                               @Qualifier("camelCaseConfiguration") org.apache.ibatis.session.Configuration configuration) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setConfiguration(configuration);     // 这是个坑。。。
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath:mapper/Db2/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "db2SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "db2TransactionManager")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("db2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "camelCaseConfiguration")
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        return new org.apache.ibatis.session.Configuration();
    }


}
