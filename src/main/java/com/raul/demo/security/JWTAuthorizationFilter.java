package com.raul.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//Filtro de autorização - analisa o token para saber se é válido
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	@Override		//Método q intercepta a requisição e verifica se o usuário está autorizado
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
		//Pega o valor do "Authorization" no cabeçalho
		String header = request.getHeader("Authorization");
		
		//startsWith verifica o início da str
		if (header != null && header.startsWith("Bearer ")) {
		    //header.substring(7) pega toda a str excluindo os 7 primeiros caracteres ("Bearer ")
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			
			if (auth != null) {
				//Token é válido, então libera o acesso dele
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		//Indica ao filtro q pode continuar com o processo normal da requisição
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
		//Credentials null pq as autorizações estão sendo controlandas  por perfis (3° argumento)
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}
