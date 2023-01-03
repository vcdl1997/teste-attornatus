package br.com.testeattornatus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.testeattornatus.entities.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	Page<Endereco> findByPessoaId(Long idPessoa, Pageable paginacao);
	
	@Modifying
	@Query("update Endereco e set e.principal = false where e.pessoa.id = ?1")
	void updateRemoveEnderecoPrincipal(Long id);
	
	@Modifying
	@Query("update Endereco e set e.principal = true where e.id = ?1")
	void updateEnderecoPrincipal(Long id);
}
