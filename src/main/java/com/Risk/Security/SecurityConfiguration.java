package com.Risk.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationProvider provider) throws Exception {
        http
                // CSRF con cookie (per SPA o Thymeleaf)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                )
                // Autorizzazioni
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/Accedi").permitAll()               // login page GET/POST
                        .requestMatchers("/Accedi/Private/**").authenticated() // area protetta
                        .anyRequest().permitAll()
                )
                // Provider custom per password
                .authenticationProvider(provider)
                // Form login
                .formLogin(form -> form
                        .loginPage("/Accedi")                       // pagina login GET
                        .loginProcessingUrl("/Accedi")             // Spring intercetta POST
                        .failureHandler((request, response, exception) -> {
                            // Salva il messaggio nella sessione
                            request.getSession().setAttribute("errorMessage", "Username o password non validi");
                            response.sendRedirect("/Accedi");      // redirect GET login
                        })
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/Accedi?logout")
                );

        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService(){
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
