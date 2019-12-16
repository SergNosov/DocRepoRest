package gov.kui.docRepoR.config.security;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

@Configuration
@PropertySource(value = "classpath:persistence-mysql.properties")
public class DataSecurityConfig {
    private Environment env;
    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public DataSecurityConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource securityDataSource() {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        try {
            securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        logger.info(">>> jdbc.url = " + env.getProperty("jdbc.url"));
        logger.info(">>> jdbc.user = " + env.getProperty("jdbc.user"));

        securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        securityDataSource.setUser(env.getProperty("jdbc.user"));
        securityDataSource.setPassword(env.getProperty("jdbc.password"));

        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

        return securityDataSource;
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    private int getIntProperty(String propName) {
        String strProp = env.getProperty(propName);
        int intProp = Integer.parseInt(strProp);
        return intProp;
    }
}
