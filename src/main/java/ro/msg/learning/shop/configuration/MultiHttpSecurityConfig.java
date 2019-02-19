package ro.msg.learning.shop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.msg.learning.shop.security.ShopUserDetailsService;

@EnableWebSecurity
@SuppressWarnings("squid:S1118")
public class MultiHttpSecurityConfig {

    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final ShopUserDetailsService shopUserDetailsService;

        @Override
        protected void configure(final AuthenticationManagerBuilder auth) {
            final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(shopUserDetailsService);
            authProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));

            auth.authenticationProvider(authProvider);
        }

        @Override
        protected void configure(final HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .anyRequest().hasAuthority("CUSTOMER")
                    .and()
                    .formLogin()
                    .permitAll()
                    .and()
                    .headers().frameOptions().sameOrigin();
        }
    }

    @Configuration
    @Order
    @RequiredArgsConstructor
    public static class BasicHttpWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final ShopUserDetailsService shopUserDetailsService;

        @Override
        protected void configure(final AuthenticationManagerBuilder auth) {
            final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(shopUserDetailsService);
            authProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));

            auth.authenticationProvider(authProvider);
        }

        @Override
        protected void configure(final HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable()
                    .authorizeRequests()
                    .anyRequest().hasAuthority("CUSTOMER")
                    .and()
                    .httpBasic()
                    .and()
                    .headers().frameOptions().sameOrigin();
        }
    }
}