package com.AuctionApp.Auction.Config;

import com.AuctionApp.Auction.Component.Role;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SpringSecurity  {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            return  http.csrf(AbstractHttpConfigurer::disable)
//                    .cors(cors ->  cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                    .cors(cors -> {})
                    .authorizeHttpRequests(request ->


                            request
                                    .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                    .requestMatchers("/user/login","/user/user-registration","/ws-auction/**","/auction/get-auction/*").permitAll()
                                    .anyRequest().authenticated()
                    )
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


