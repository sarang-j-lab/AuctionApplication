package com.AuctionApp.Auction.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity  {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            return  http.csrf(customizer -> customizer.disable())
                    .cors(cors -> cors.configure(http))
                    .authorizeHttpRequests(request ->
                            request.requestMatchers("/user/login","/user/user-registration").permitAll()
                                    .anyRequest().authenticated())
                    .sessionManagement(sessionManage ->
                            sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }

//    here AuthenticationProvider verify user from database
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(4));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    //here we implementing AuthenticationManager because we want to use custom authentication not basic auth
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

/*
    the flow of authentication
 Client Request:

 --A user sends a login request with credentials (e.g., username/mobile number and password).
 --Spring Security intercepts the request through its filters (e.g., UsernamePasswordAuthenticationFilter).
   AuthenticationManager:

 --It receives the authentication request and delegates the actual authentication process to one or more AuthenticationProvider implementations.
        AuthenticationProvider:

 --The AuthenticationProvider checks the credentials (e.g., validates the username/mobile number and password against a database or other data source).
        If successful, it returns an Authentication object containing user details and roles.

        SecurityContext:
        Once authenticated, the Authentication object is stored in the SecurityContext, making it accessible throughout the session.*\
 */
