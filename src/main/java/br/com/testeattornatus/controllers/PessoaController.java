package br.com.testeattornatus.controllers;

import java.net.URI;
import java.text.ParseException;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.testeattornatus.controllers.form.PessoaForm;
import br.com.testeattornatus.controllers.handlers.exceptions.RecordNotFoundException;
import br.com.testeattornatus.entities.Pessoa;
import br.com.testeattornatus.repositories.PessoaRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces="application/json")
	@Cacheable(value = "listaDePessoas")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
	            value = "Pagina a ser carregada", defaultValue = "0"),
	    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
	            value = "Quantidade de registros", defaultValue = "10"),
	    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
	            value = "Ordenacao dos registros")
	})
	public ResponseEntity<Page<Pessoa>> findAll(
		@RequestParam(required = false) String nome, 
		@RequestParam(required = false) String dataNascimento,
		@PageableDefault(
			sort = "id", 
			direction = Direction.ASC, 
			page = 0, 
			size = 10
		) @ApiIgnore Pageable paginacao
	){
		Page<Pessoa> lista = null;
		
		if(nome != null) lista = pessoaRepository.findByNomeContaining(nome, paginacao);
		else if (dataNascimento != null) lista = pessoaRepository.findByDataNascimentoContaining(dataNascimento, paginacao);
		else lista = pessoaRepository.findAll(paginacao);
		
		return ResponseEntity.ok().body(lista);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Pessoa> getById(@PathVariable Long id){
		Optional<Pessoa> busca = pessoaRepository.findById(id);
		
		if(busca.isEmpty()) throw new RecordNotFoundException("Pessoa não encontrada!");
		
		return ResponseEntity.ok().body(busca.get());
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	@Transactional
	@CacheEvict(value = "listaDePessoas", allEntries = true)
	public ResponseEntity<Pessoa> insert(
		@RequestBody @Valid PessoaForm form,
		UriComponentsBuilder uriBuilder
	) throws ParseException{
		
		Pessoa pessoa = form.transform();
		
		pessoa = pessoaRepository.save(pessoa);
		
		URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(pessoa.getId()).toUri();
		
		return ResponseEntity.created(uri).body(pessoa);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@Transactional
	@CacheEvict(value = "listaDePessoas", allEntries = true)
	public ResponseEntity<Pessoa> update(
		@PathVariable Long id, 
		@RequestBody @Valid PessoaForm form
	) throws ParseException{
		
		Optional<Pessoa> busca = pessoaRepository.findById(id);
		
		if(busca.isEmpty()) throw new RecordNotFoundException("Pessoa não encontrada!");
		
		Pessoa pessoaBuscada = busca.get();
		Pessoa pessoaForm = form.transform();
		
		pessoaBuscada.setNome(pessoaForm.getNome());
		pessoaBuscada.setDataNascimento(pessoaForm.getDataNascimento());
		
		pessoaBuscada = pessoaRepository.save(pessoaBuscada);
		
		return ResponseEntity.ok().body(pessoaBuscada);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	@Transactional
	@CacheEvict(value = "listaDePessoas", allEntries = true)
	public ResponseEntity<?> delete(@PathVariable Long id) throws ParseException{
		
		Optional<Pessoa> busca = pessoaRepository.findById(id);
		
		if(busca.isEmpty()) throw new RecordNotFoundException("Pessoa não encontrada!");
		
		pessoaRepository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
}
