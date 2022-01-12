package com.raul.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raul.demo.dto.CredenciaisDTO;

/*Filtro é um cara q intercepta a requisição, executa algo antes e depois. Se der certo, ele devolve
a requisição para continuar executando normalmente. No caso do filtro de autenticação, qnd o 
usuário for tentar fazer o login, ao fazer a requisição, o filtro de autenticação vai interceptar
a requisição e verificar se usuário e senha estão corretos. Para o filtro ser um filtro de 
autenticação é preciso estender UsernamePasswordAuthenticationFilter. Ao fazer isso, o Spring 
Secury sabe q esse filtro precisará interceptar a requisição de login*/
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;	//Classe do SS
    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    	setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
	
	@Override		//Tenta autenticar
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

		try {
			//Operação q pega os dados q vieram na requisição e converte para o tipo da classe especificado
			CredenciaisDTO creds = new ObjectMapper().readValue(req.getInputStream(), CredenciaisDTO.class);
	        //Esse token n é do JWT, é do Spring Security. Unicialmente a lista passada é vazia
			UsernamePasswordAuthenticationToken authToken = 
	        		new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
	        //authenticate verifica se o usuário e senha são válidos. Isso é feito com base no UserDatailService
	        Authentication auth = authenticationManager.authenticate(authToken);
	        return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override			//O que faz se a autenticação for um sucesso
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
	
		//getPrincipal retorna o usuário do SS. getUsername pega o email da pessoa q fez o login
		String username = ((UserSS) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        //Acrescenta o token como cabeçalho da resposta
        response.addHeader("Authorization", "Bearer " + token);	 
        //Expõe o header Authorization e libera a leitura do cabeçalho (cabeçalho personalizado)
        response.addHeader("access-control-expose-headers", "Authorization");
	}
	
	//Classe para personalizar caso a autenticação falhe
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }
}
