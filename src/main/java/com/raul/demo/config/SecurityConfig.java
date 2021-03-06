package com.raul.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.raul.demo.security.JWTAuthenticationFilter;
import com.raul.demo.security.JWTAuthorizationFilter;
import com.raul.demo.security.JWTUtil;

@EnableWebSecurity			//@EnableWebSecurity já tem a anotação @Configuration
/*@EnableGlobalMethodSecurity permite colocar anotações de pré-autorização nos endpoints. Pode 
colocar autorização para perfis específicos*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*É injetado uma interface, mas o Spring é inteligente o suficiente para buscar a implementação 
	 * dela (UserDetailsServiceImpl)*/
	@Autowired			
	private UserDetailsService userDetailsService;
	
	@Autowired		//Environment é uma interface q representa em qual ambiente o sistema está sendo executado
    private Environment environment;			//Para acessar os profiles do projeto
	
	@Autowired
	private JWTUtil jwtUtil;
	
	//Caminhos q ficarão liberados por padrão
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
	//Caminhos público apenas para leitura de dados
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**",
			"/estados/**",
	};

	//Caminhos q o usuário pode fazer o post sem precisar estar logado
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes/**",
			"/auth/forgot/**"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Pega os profiles ativos e verifica se existe o profile "test"
		if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            //Libera o acesso para o BD H2. Sem esse comando n é possível acessá-lo
			http.headers().frameOptions().disable();
        }
		
		/*http.cors() ativa o @Bean do corsConfigurationSource 
		csrf().disable() desabilita a proteção de ataque csrf pq como o sistema é STATELESS,
		n é preciso se preocupar com esse tipo de ataque q é baseado no armazenamento de autenticação
		em seção. Como n será armazenado seção, então isso n é uma preocupação*/
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			/*Só permite o método POST para quem estiver na lista. Permite todos os caminhos que 
			estiverem no vetor*/
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			//Para todo resto é exigido autenticação
			.anyRequest().authenticated();
		//Filtro de autenticação. authenticationManager() traz o resultado de sucesso ou de erro
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		//Filtro de autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		//Garante que o back end não irá criar seção de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	/*Configuração do mecanismo de autenticação. Método para indicar qm é o userDetailsService e qual
	  é o algoritmo de encodificação da senha userDetailsService indica qm é capaz de buscar o 
	  usuário por email*/
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	//Configuração caso queira q requisições de múltiplas fontes (heroku, etc) sejam feitas ao back end 
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		//Lista de métodos liberados nas requisições (liberados para o Cors)
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean //Faz o encode de uma senha. Esse componente pode ser injetado em qualquer classe
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();			
	}
}
