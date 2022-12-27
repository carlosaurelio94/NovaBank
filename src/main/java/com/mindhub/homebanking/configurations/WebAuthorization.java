package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/rest/**", "/api/clients", "/api/accounts").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/web/accounts.html", "/web/account.html", "/web/cards.html", "/web/createCards.html", "/web/transfers.html", "/web/loanApplication.html", "/pdf/generate").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/transactions", "/api/loans").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/message").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/clients/current", "/api/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/web/index.html", "/api/loans").permitAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .successHandler((req, res, auth) -> clearAuthenticationAttributes(req))
                .failureHandler((req, res, exc) ->  res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.sessionManagement()
                .invalidSessionUrl("/web/index.html")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/web/index.html")
                .sessionRegistry(sessionRegistry());

        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID", "cookie")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
}

