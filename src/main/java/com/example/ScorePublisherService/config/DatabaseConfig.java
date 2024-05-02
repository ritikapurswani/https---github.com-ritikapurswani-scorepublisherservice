package com.example.ScorePublisherService.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${jdbc.master.url}")
    private String jdbcMasterUrl;
    @Value("${jdbc.slave.url}")
    private String jdbcSlaveUrl;
    @Value("${jdbc.user}")
    private String jdbcUser;
    @Value("${max.pool.size.slave:100}")
    private int maxPoolSizeSlave;
    @Value("${max.pool.size:60}")
    private int maxPoolSizeMaster;

    @Autowired
    private Environment commonProperties;


    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setUsername(jdbcUser);
        hikariDataSource.setPassword("AVNS_Khs1oZjsGDq_7hZ5qes");
        hikariDataSource.setJdbcUrl(jdbcMasterUrl);
        hikariDataSource.setMinimumIdle(5);
        hikariDataSource.setMaximumPoolSize(maxPoolSizeMaster);
        return hikariDataSource;
    }

    @Bean
    public DataSource dataSourceSlave() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl(jdbcSlaveUrl);
        hikariDataSource.setUsername(jdbcUser);
        hikariDataSource.setPassword("AVNS_Khs1oZjsGDq_7hZ5qes");
        hikariDataSource.setMinimumIdle(5);
        hikariDataSource.setMaximumPoolSize(maxPoolSizeSlave);
        return hikariDataSource;
    }

    @Bean
    @Primary
    public JdbcTemplate masterJdbcTemplate(@Qualifier("dataSource") DataSource dsMaster) {
        return new JdbcTemplate(dsMaster);
    }

    @Bean
    public JdbcTemplate slaveJdbcTemplate(@Qualifier("dataSourceSlave") DataSource dsSlave) {
        return new JdbcTemplate(dsSlave);
    }

}