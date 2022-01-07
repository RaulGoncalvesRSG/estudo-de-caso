package com.raul.demo.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.raul.demo.domain.Categoria;
import com.raul.demo.domain.Cidade;
import com.raul.demo.domain.Cliente;
import com.raul.demo.domain.Endereco;
import com.raul.demo.domain.Estado;
import com.raul.demo.domain.Pagamento;
import com.raul.demo.domain.PagamentoComBoleto;
import com.raul.demo.domain.PagamentoComCartao;
import com.raul.demo.domain.Pedido;
import com.raul.demo.domain.Produto;
import com.raul.demo.domain.enums.EstadoPagamento;
import com.raul.demo.domain.enums.TipoCliente;
import com.raul.demo.repositories.CategoriaRepository;
import com.raul.demo.repositories.CidadeRepository;
import com.raul.demo.repositories.ClienteRepository;
import com.raul.demo.repositories.EnderecoRepository;
import com.raul.demo.repositories.EstadoRepository;
import com.raul.demo.repositories.PagamentoRepository;
import com.raul.demo.repositories.PedidoRepository;
import com.raul.demo.repositories.ProdutoRepository;

@Configuration		//Define a classe como uma classe de configuração
public class TestConfig implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
	
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c1, c3));
		
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "12345678", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("12345", "12346"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Aptos 303", "Jardim", "1232435", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "9797312", cli1, c2);
	
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		
		SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sfd.parse("30/09/2020 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sfd.parse("10/10/2020 10:32"), cli1, e2);
		
		//ID do pagamento é o msmo ID do pedido
		Pagamento pagamento1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagamento1);
		
		//Pag 2 ainda n teve pagamento
		Pagamento pagamento2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sfd.parse("20/10/2020 00:00"), null);
		ped2.setPagamento(pagamento2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagamento1, pagamento2));
	}
}
