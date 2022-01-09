package com.raul.demo.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.raul.demo.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	/*Preenche o pagamento com a data de vencimento (1 semana depois do instante)
	Em uma situação real, esse código deveria ser trocado por uma chamada de web service q gera
	boleto*/
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagamento, Date instanteDoPedido) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(instanteDoPedido);
		calendar.add(Calendar.DAY_OF_MONTH, 7);			//Acrescenta 7 dias
		pagamento.setDataVencimento(calendar.getTime());
	}
}
