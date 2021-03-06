package com.raul.demo.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ClienteInsertValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

/*Para fazer uma validação personalizada é preciso criar 2 arquivos: um para ser a anotação personalizada
e o outro é o Validator para essa anotação*/

/*Anotação costumizada para validação. ClienteInsert é a anotação e ClienteInsertValidator é a classe
q implementa o Validator*/
public @interface ClienteInsert {
	String message() default "Erro de validação";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
