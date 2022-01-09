package com.raul.demo.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.raul.demo.domain.Cliente;
import com.raul.demo.dto.ClienteDTO;
import com.raul.demo.repositories.ClienteRepository;
import com.raul.demo.resources.exception.FieldMessage;

//O DTO utilizado para atualização de cliente é o ClienteDTO
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;			//Obj padrão do Java para web 
	
	@Autowired
	private ClienteRepository repository;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		//HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE pega o map de variáveis de URI de uma requisição
		@SuppressWarnings("unchecked")	 //Remove a msg amarela
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		//Pega o parâmetro ID da URI da requição. Este ID é o do cliente q será atualizado
		Integer uriId = Integer.parseInt(map.get("id"));	
		
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente aux = repository.findByEmail(objDto.getEmail());
		
		//Verifica se o ID do cliente buscado pelo email é diferente do ID q está usando para atualizar
		if (aux != null && !aux.getId().equals(uriId)) {
			//Tentou atualizar um cliente contendo email q já tinha em outro cliente no BD
			list.add(new FieldMessage("email", "Email já existente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}

