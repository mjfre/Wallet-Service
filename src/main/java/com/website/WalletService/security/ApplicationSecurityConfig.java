package com.website.WalletService.security;

import com.website.WalletService.jwt.HeaderFilter;
import com.website.WalletService.jwt.JwtConfig;
import com.website.WalletService.jwt.JwtTokenVerifier;
import com.website.WalletService.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.website.WalletService.repository.ApplicationUserRepository;
import com.website.WalletService.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService,
                                     SecretKey secretKey,
                                     JwtConfig jwtConfig,
                                     ApplicationUserRepository applicationUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new HeaderFilter(), JwtUsernameAndPasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey), HeaderFilter.class)
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig, applicationUserRepository), JwtUsernameAndPasswordAuthenticationFilter.class)
                //authorize requests
                .authorizeRequests()
                //these two lines whitelist pages matching these patterns
                .antMatchers(
                        "/login",
                        "/wallet-record",
                        "/wallet-record/**",
                        //swagger urls
                        "/",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/null/**",
                        "/swagger-resources/**",
                        "/v2/**"
                ).permitAll()
                .anyRequest()
                .authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

}
