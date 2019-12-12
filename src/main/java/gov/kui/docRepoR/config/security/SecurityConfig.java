package gov.kui.docRepoR.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*
    private DataSource securityDataSource;

    @Autowired
    public SecurityConfig(DataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

     */


        @Autowired
        private UserDetailsService userDetailsService;
        @Bean
        public UserDetailsService userDetailsService() {
            User.UserBuilder users = User.withDefaultPasswordEncoder();
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(users.username("user").password("user").roles("USER").build());
            manager.createUser(users.username("admin").password("admin").roles("ADMIN").build());
            return manager;
        }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*
        http.authorizeRequests()
                .antMatchers("/api/**").access("hasRole('ADMIN')")
                .antMatchers("/api/**").access("hasRole('EMPLOYEE')")
                .and().csrf().disable()
//                .cors().disable()
                .formLogin()
                .defaultSuccessUrl("/", false);
         */
        http.authorizeRequests()
                .antMatchers("/api/**").access("hasRole('ADMIN')")
                .antMatchers("/api/**").access("hasRole('USER')")
               // .antMatchers("/").permitAll()
                .and().csrf().disable().cors().disable()
                .formLogin()
                .defaultSuccessUrl("/", false);
    }
/*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(securityDataSource);
    }

 */
}

