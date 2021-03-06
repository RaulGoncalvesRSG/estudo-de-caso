package com.raul.demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raul.demo.domain.Cliente;
import com.raul.demo.domain.ItemPedido;
import com.raul.demo.domain.PagamentoComBoleto;
import com.raul.demo.domain.Pedido;
import com.raul.demo.domain.enums.EstadoPagamento;
import com.raul.demo.repositories.ItemPedidoRepository;
import com.raul.demo.repositories.PagamentoRepository;
import com.raul.demo.repositories.PedidoRepository;
import com.raul.demo.security.UserSS;
import com.raul.demo.services.email.EmailService;
import com.raul.demo.services.exceptions.AuthorizationException;
import com.raul.demo.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! ID: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public List<Pedido> buscarTodos() {
		return repository.findAll();
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setInstante(new Date());			//Momento em q o pedido está sendo realizado
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		//Pedido que está sendo feito tem o estado incial PENDENTE 
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);			//O Pagamento precisa conhecer o seu pedido
		
		//Se o pagamento for do tipo PagamentoComBoleto
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagamento= (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagamento, obj.getInstante());
		}
		
		obj = repository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());	//O repository é capaz de salvar uma lista
		emailService.sendOrderConfirmationEmail(obj);
		
		return obj;
	}
	
	//Busca os pedidos somente do usuário logado
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();		//Pega o usuário logado
		
		if (user == null) {				//Usuário n autenticado
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.findById(user.getId());	//Pedidos apenas do usuário logado
		return repository.findByCliente(cliente, pageRequest);
	}
}
