package com.lj.autoRemote.source;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 由于Druid 部分配置 暂时不在Spring Bootz中的直接支持，故需要进行配置信息的定制：
 * @author James
 */
@Configuration
public class DruidDBForSqlLiteConfig {

    private Logger logger = Logger.getLogger(DruidDBForSqlLiteConfig.class);

    @Value("${spring.datasource.sqllite.url}")
    private String dbUrl;

    @Bean     //声明其为Bean实例
    public JdbcTemplate jdbcTemplateForSqlLite(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceForSqlLite());
        return jdbcTemplate;
    }

    @Bean     //声明其为Bean实例
    public NamedParameterJdbcTemplate namedParameterJdbcTemplateForSqlLite(){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceForSqlLite());
        return namedParameterJdbcTemplate;
    }

    @Bean     //声明其为Bean实例
    public DataSource dataSourceForSqlLite(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        return datasource;
    }

}
