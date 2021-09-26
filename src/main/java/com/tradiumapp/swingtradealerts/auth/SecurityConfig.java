package com.tradiumapp.swingtradealerts.auth;

//import javax.validation.Valid;

import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseAuthenticationProvider;
import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseFilter;
import com.tradiumapp.swingtradealerts.auth.service.FirebaseService;
import com.tradiumapp.swingtradealerts.auth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        @Qualifier(value = UserServiceImpl.NAME)
        private UserDetailsService userService;

        @Autowired
        private FirebaseAuthenticationProvider firebaseProvider;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService);
            auth.authenticationProvider(firebaseProvider);
        }
    }

    @Configuration
//	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
//                    "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v2/swagger.json");
//        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .anonymous().authorities(BuiltInRoleDefinitions.ROLE_ANONYMOUS).and()
                    .authorizeRequests().antMatchers("*").permitAll();
//            http.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter.class)
//            http.anonymous().per
//                    .antMatchers("*")
//                    .permitAll()
//                    .and()
//                    .anonymous().authorities(BuiltInRoleDefinitions.ROLE_ANONYMOUS);
        }

        @Autowired(required = false)
        private FirebaseService firebaseService;

        private FirebaseFilter tokenAuthorizationFilter() {
            return new FirebaseFilter(firebaseService);
        }

    }
}
