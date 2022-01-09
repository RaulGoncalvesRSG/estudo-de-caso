package com.raul.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.raul.demo.domain.Categoria;
import com.raul.demo.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	/*Pesquisa um nome de produto que estiver na lista de determinadas categorias. O métodod
	 * "findDistinctByNomeContainingAndCategoriasIn" já faz a consulta correta sem criar uma query. 
	 * Neste caso, n preciaria do @Param no argumento. OBS: a query sobrepõe o nome do método*/
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome, 
							@Param("categorias") List<Categoria> categorias, Pageable pageRequest);
}
