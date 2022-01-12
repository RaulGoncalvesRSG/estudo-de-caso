package com.raul.demo.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

//Filtro para interceptar todas requisições
@Component
public class HeaderExposureFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	//Toda vez q houver requisição, o filtro irá dizer q pode ler o header location
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) response;
		//Caso o cabeçalho location n seja exposto de forma explícita, o Angular n consegue ler ele
		res.addHeader("access-control-expose-headers", "location");
		chain.doFilter(request, response);				//Faz a requisição continuar normalmente
	}

	@Override
	public void destroy() {
	}
}
