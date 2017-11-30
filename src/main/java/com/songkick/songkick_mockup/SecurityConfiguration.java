package com.songkick.songkick_mockup;

import com.songkick.songkick_mockup.services.UserDetailsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{


    private UserDetailsLoader usersLoader;

    @Autowired
    public SecurityConfiguration(UserDetailsLoader usersLoader){
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                //login (what happens after you login)
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/profile")
                .permitAll()
                .and()
                //logout (what happens when you logout)

                .logout()
                .logoutSuccessUrl("/login?logout")
                .and()

                //non-logged-in users CAN go here:
                .authorizeRequests()
                .antMatchers("/", "/register", "/bandsProfile", "/band/search","/show/search","/users/showUsers","/users/showIndividualUser/{id}","/show/{show_id}/moreInfo")
                .permitAll()
                .and()


                //restricted areas (non-logged in users CAN'T go here)
                .authorizeRequests()
                .antMatchers("/request/ignore/{id}","/request/approve/{id}","/request/{id}","/response/{id}","/addband", "/create/review","/show/add")
                .authenticated();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersLoader).passwordEncoder(passwordEncoder());

    }


}
