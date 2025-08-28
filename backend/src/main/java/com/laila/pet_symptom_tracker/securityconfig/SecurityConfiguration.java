package com.laila.pet_symptom_tracker.securityconfig;

import com.laila.pet_symptom_tracker.mainconfig.SecurityPaths;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
  private final Logger log = Logger.getLogger(SecurityConfiguration.class.getName());
  private final JwtAuthFilter jwtAuthFilter;
  private final CorsConfig corsConfig;

  public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, CorsConfig corsConfig) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.corsConfig = corsConfig;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfig.corsConfiguration()))
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request ->
                request
                    // Non-authenticated Post paths
                    .requestMatchers(HttpMethod.POST, SecurityPaths.OPEN_POST_PATHS)
                    .permitAll()
                    // Get Paths
                    .requestMatchers(HttpMethod.GET, SecurityPaths.AUTHENTICATED_GET_PATHS)
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, SecurityPaths.MODERATOR_PATHS)
                    .hasAnyRole("ADMIN", "MODERATOR")
                    .requestMatchers(HttpMethod.GET, SecurityPaths.ADMIN_ONLY_PATHS)
                    .hasRole("ADMIN")
                    // Post paths
                    .requestMatchers(HttpMethod.POST, SecurityPaths.AUTHENTICATED_MANAGE_PATHS)
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, SecurityPaths.MODERATOR_MANAGE_PATHS)
                    .hasAnyRole("ADMIN", "MODERATOR")
                    .requestMatchers(HttpMethod.POST, SecurityPaths.ADMIN_ONLY_PATHS)
                    .hasRole("ADMIN")
                    // Patch paths
                    .requestMatchers(HttpMethod.PATCH, SecurityPaths.AUTHENTICATED_MANAGE_PATHS)
                    .authenticated()
                    .requestMatchers(HttpMethod.PATCH, SecurityPaths.MODERATOR_MANAGE_PATHS)
                    .hasAnyRole("ADMIN", "MODERATOR")
                    .requestMatchers(HttpMethod.PATCH, SecurityPaths.ADMIN_ONLY_PATHS)
                    .hasRole("ADMIN")
                    // Delete Paths
                    .requestMatchers(HttpMethod.DELETE, SecurityPaths.AUTHENTICATED_MANAGE_PATHS)
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, SecurityPaths.MODERATOR_MANAGE_PATHS)
                    .hasAnyRole("ADMIN", "MODERATOR")
                    .requestMatchers(HttpMethod.DELETE, SecurityPaths.ADMIN_ONLY_PATHS)
                    .hasRole("ADMIN")

                    // Remainder paths
                    .anyRequest()
                    .authenticated());

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(
        configures -> configures.authenticationEntryPoint(new NotAuthorizedEntryPoint()));

    return http.build();
  }
}
