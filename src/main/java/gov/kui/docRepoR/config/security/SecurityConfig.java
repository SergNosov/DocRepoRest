package gov.kui.docRepoR.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);

        http.authorizeRequests()
                .antMatchers("/api/**").access("hasRole('ADMIN')")
                .antMatchers("/api/**").access("hasRole('USER')")
                .and().csrf().disable()
                .httpBasic()
                .and()
                .formLogin();
    }
}

