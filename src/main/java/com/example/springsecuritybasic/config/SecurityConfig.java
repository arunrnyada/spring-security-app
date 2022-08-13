package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,  jsr250Enabled = true)
public class SecurityConfig {

    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {

     */

        /**
         * Default configurations which will secure all the requests
         */

        /*
         * http .authorizeRequests() .anyRequest().authenticated() .and()
         * .formLogin().and() .httpBasic();
         */

        /**
         * Custom configurations as per our requirement
         */

        /*
         * http .authorizeRequests() .antMatchers("/myAccount").authenticated()
         * .antMatchers("/myBalance").authenticated()
         * .antMatchers("/myLoans").authenticated()
         * .antMatchers("/myCards").authenticated() .antMatchers("/notices").permitAll()
         * .antMatchers("/contact").permitAll() .and() .formLogin().and() .httpBasic();
         */

        /**
         * Configuration to deny all the requests
         */

        /*
         * http .authorizeRequests() .anyRequest().denyAll() .and() .formLogin().and()
         * .httpBasic();
         */

        /**
         * Configuration to permit all the requests
         */

/*
        http.authorizeRequests()
                .antMatchers("/myAccount").authenticated()
                .antMatchers("/myBalance").authenticated()
                .antMatchers("/myCards").authenticated()
                .antMatchers("/myLoans").authenticated()
                .antMatchers("/contact").authenticated()
                .antMatchers("/notices").authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();


    }
 */

        /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/myAccount").authenticated()
                .antMatchers("/myBalance").authenticated()
                .antMatchers("/myCards").authenticated()
                .antMatchers("/myLoans").authenticated()
                .antMatchers("/contact").permitAll()
                .antMatchers("/notices").permitAll()
                .and()
                .formLogin()
                .and()
                .httpBasic();
        return http.build();
    }
         */
        @Bean
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .cors().configurationSource(request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }).and().csrf().disable()
                    .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                    .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                    .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                    .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                    .authorizeHttpRequests((auth) -> auth
                            .antMatchers("/myAccount").hasRole("USER")
                            .antMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                            .antMatchers("/myLoans").authenticated()
                            .antMatchers( "/myCards").hasAnyRole("USER","ADMIN")
                            .antMatchers( "/user").authenticated()
                            .antMatchers("/notices", "/contact").permitAll()
                    ).httpBasic(Customizer.withDefaults());
            return http.build();
        }

    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        UserDetails admin = User.withUsername("admin").password("12345").authorities("admin").build();
        UserDetails user = User.withUsername("user").password("12345").authorities("read").build();
        userDetailsService.createUser(admin);
        userDetailsService.createUser(user);
        return userDetailsService;
    }
     */

    /*
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

}
