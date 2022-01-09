package com.raul.demo.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.raul.demo.domain.Cliente;
import com.raul.demo.domain.enums.TipoCliente;
import com.raul.demo.dto.ClienteNewDTO;
import com.raul.demo.repositories.ClienteRepository;
import com.raul.demo.resources.exception.FieldMessage;
import com.raul.demo.services.validation.utils.BR;

//Especifica no ConstraintValidator: nome da anotação e o tipo da classe q vai aceitar a anotação
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override	 
	/*Lógica de validação. Retorna true se o obj for válido. Esse resultado será recebido no @Valid da camada Resource */
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		//Se o TipoCliente for PESSOAFISICA: faz validação do CPF
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		//Se o TipoCliente for PESSOAJURIDICA: faz validação do CNPJ
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}

	/*	Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}*/
		
		//Para cada obj na lista FieldMessage, add um erro correspondente na lista de erros do framework
		//Essa lista de erros do framework é tratada e mostrada na resposta na classe ExceptionHandler
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();			
	}
}

