package com.securityModel.security;

import com.securityModel.models.Admin;
import com.securityModel.models.ERole;
import com.securityModel.models.Role;
import com.securityModel.repository.AdminRepository;
import com.securityModel.repository.RoleRepository;
import com.securityModel.security.jwt.AuthEntryPointJwt;
import com.securityModel.security.jwt.AuthTokenFilter;
import com.securityModel.security.services.SecurityConfig;
import com.securityModel.security.services.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
@Import(SecurityConfig.class)
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	AdminRepository adminRepository;

	@PostConstruct
	public void init() {

		System.out.println("Checking if admin role exists...");
		if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
			boolean isAdminRoleNull = roleRepository.findByName(ERole.ROLE_ADMIN).isPresent();
			System.out.println("Is admin role null? " + isAdminRoleNull);
			Role adminRole = new Role(ERole.ROLE_ADMIN);
			roleRepository.save(adminRole);
			System.out.println("Admin role created.");
		}


		if (!roleRepository.findByName(ERole.ROLE_PATIENT).isPresent()){
			Role patientRole = new Role(ERole.ROLE_PATIENT);
			roleRepository.save(patientRole);

		}
		if (!roleRepository.findByName(ERole.ROLE_SECRETARY).isPresent()){
			Role secretaryRole = new Role(ERole.ROLE_SECRETARY);
			roleRepository.save(secretaryRole);

		}
		if (!roleRepository.findByName(ERole.ROLE_DOCTOR).isPresent()){
			Role doctorRole = new Role(ERole.ROLE_DOCTOR);
			roleRepository.save(doctorRole);

		}
		if (!adminRepository.findByUsername("admin").isPresent()){
			System.out.println("checking first");
			Admin admin=new Admin();
			admin.setUsername("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(passwordEncoder.encode("123456"));
			Set<Role> roles = new HashSet<>();
			Role role=roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			admin.setRoles(roles);
			admin.setConfirm(true);
			adminRepository.save(admin);
		}




	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

//	@Override
//	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	}
	
	@Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder((passwordEncoder));
   
      return authProvider;
  }

//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
	
	@Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }



//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable()
//			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//			.authorizeRequests().antMatchers("/api/auth/**").permitAll()
//			.antMatchers("/api/test/**").permitAll()
//			.anyRequest().authenticated();
//
//		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//	}
	
	@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/test/**").permitAll()
			.requestMatchers("/api/auth/Patient**").permitAll()
			.requestMatchers("/api/auth/Patient/files/**").permitAll()
			.requestMatchers("/api/auth/Patient/rate/**").permitAll()
					.requestMatchers("/Post/**").permitAll()
			.requestMatchers("/Comment/**").permitAll()
			.requestMatchers("/like/**").permitAll()
			.requestMatchers("/Notifications/**").permitAll()


			.requestMatchers("/api/auth/Doctor/rendezvous/**").permitAll()
			.requestMatchers("/Bill/**").permitAll()
			.requestMatchers("/domain/**").permitAll()
			.requestMatchers("/RendezVous/**").permitAll()
			.requestMatchers("/RendezVous/userRendezVous").permitAll()
			.requestMatchers("/MedicalFile/**").permitAll()
			.requestMatchers("/Cabinet/**").permitAll()
			.requestMatchers("/Messages/**").permitAll()
			.requestMatchers("/api/auth/Admin/**").permitAll()



        .anyRequest().authenticated();
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}
