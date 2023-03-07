package org.petproject.sensors.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig {
  private final String userName;
  private final String userPass;
  private final String adminName;
  private final String adminPass;

  public SecurityConfig(@Value("${default.user.name}") final String userName,
                        @Value("${default.user.pass}") final String userPass,
                        @Value("${default.admin.name}") final String adminName,
                        @Value("${default.admin.pass}") final String adminPass) {
    this.userName = userName;
    this.userPass = userPass;
    this.adminName = adminName;
    this.adminPass = adminPass;
  }


  @Bean
  public InMemoryUserDetailsManager userDetailsService(final PasswordEncoder passwordEncoder) {
    final UserDetails user = User.withUsername(userName)
            .password(passwordEncoder.encode(userPass))
            .roles(Constants.USER_ROLE_NAME)
            .build();

    final UserDetails admin = User.withUsername(adminName)
            .password(passwordEncoder.encode(adminPass))
            .roles(Constants.USER_ROLE_NAME, Constants.ADMIN_ROLE_NAME)
            .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
            .requestMatchers("/test/**").permitAll()
            .requestMatchers("/admin/**").hasAnyRole(Constants.ADMIN_ROLE_NAME)
            .requestMatchers("/user/**").hasAnyRole(Constants.USER_ROLE_NAME, Constants.ADMIN_ROLE_NAME)
            .and()
            .formLogin().permitAll()
            .and()
            .logout().permitAll()
            .logoutSuccessUrl("/")
            .and()
            .httpBasic()
            .and()
            .csrf().disable();
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}

