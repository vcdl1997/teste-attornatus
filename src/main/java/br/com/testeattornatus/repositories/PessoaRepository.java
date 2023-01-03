package br.com.testeattornatus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.testeattornatus.entities.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	Page<Pessoa> findByNomeContaining(String nome, Pageable paginacao);
	
	Page<Pessoa> findByDataNascimentoContaining(String dataNascimento, Pageable paginacao);
}
