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

import br.com.testeattornatus.controllers.form.EnderecoForm;
import br.com.testeattornatus.controllers.handlers.exceptions.RecordNotFoundException;
import br.com.testeattornatus.entities.Endereco;
import br.com.testeattornatus.repositories.EnderecoRepository;
import br.com.testeattornatus.repositories.PessoaRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces="application/json")
	@Cacheable(value = "listaDeEnderecos")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
	            value = "Pagina a ser carregada", defaultValue = "0"),
	    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
	            value = "Quantidade de registros", defaultValue = "10"),
	    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
	            value = "Ordenacao dos registros")
	})
	public ResponseEntity<Page<Endereco>> findAll(
		@RequestParam(required = true) Long idPessoa, 
		@PageableDefault(
			sort = "id", 
			direction = Direction.ASC, 
			page = 0, 
			size = 10
		) @ApiIgnore Pageable paginacao
	){
		Page<Endereco> lista = enderecoRepository.findByPessoaId(idPessoa, paginacao);
		
		return ResponseEntity.ok().body(lista);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Endereco> getById(@PathVariable Long id){
		Optional<Endereco> busca = enderecoRepository.findById(id);
		
		if(busca.isEmpty()) throw new RecordNotFoundException("Endereço não encontrado!");
		
		return ResponseEntity.ok().body(busca.get());
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	@Transactional
	@CacheEvict(value = "listaDeEnderecos", allEntries = true)
	public ResponseEntity<Endereco> insert(
		@RequestBody @Valid EnderecoForm form,
		UriComponentsBuilder uriBuilder
	) throws ParseException{
		
		Endereco endereco = form.transform(pessoaRepository);
		
		endereco = enderecoRepository.save(endereco);
		
		URI uri = uriBuilder.path("/enderecos/{id}").buildAndExpand(endereco.getId()).toUri();
		
		return ResponseEntity.created(uri).body(endereco);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces="application/json")
	@Transactional
	@CacheEvict(value = "listaDeEnderecos", allEntries = true)
	public ResponseEntity<Endereco> updateEnderecoPrincipal(@PathVariable Long id){	
		Optional<Endereco> busca = enderecoRepository.findById(id);
		
		if(busca.isEmpty()) throw new RecordNotFoundException("Endereço não encontrado!");
		
		Endereco endereco = busca.get();
		
		enderecoRepository.updateRemoveEnderecoPrincipal(endereco.getPessoa().getId());
		enderecoRepository.updateEnderecoPrincipal(id);
		
		return ResponseEntity.ok().build();
	}
}
