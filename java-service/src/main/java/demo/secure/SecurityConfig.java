package demo.secure;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class SecurityConfig {
  private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
  @Bean UserDetailsService users(){
    UserDetails admin=User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build();
    UserDetails auditor=User.withUsername("auditor").password(encoder.encode("audit123")).roles("AUDITOR").build();
    return new InMemoryUserDetailsManager(admin,auditor);
  }
  @Bean SecurityFilterChain filter(HttpSecurity http)throws Exception{
    return http.csrf(c->c.disable()).authorizeHttpRequests(a->a
      .requestMatchers("/auth/token","/actuator/health").permitAll()
      .requestMatchers("/audit/**").hasAnyRole("ADMIN","AUDITOR")
      .anyRequest().authenticated()).httpBasic(b->{}).build();
  }
  public String demoToken(String user,String role){
    var key=Keys.hmacShaKeyFor("replace-this-demo-secret-with-a-long-key-123456".getBytes(StandardCharsets.UTF_8));
    return Jwts.builder().subject(user).claim("role",role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+3600000)).signWith(key).compact();
  }
}
