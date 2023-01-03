package br.com.testeattornatus.controllers.form;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import br.com.testeattornatus.controllers.handlers.exceptions.RecordNotFoundException;
import br.com.testeattornatus.entities.Endereco;
import br.com.testeattornatus.entities.Pessoa;
import br.com.testeattornatus.repositories.PessoaRepository;

public class EnderecoForm {
	
	@NotNull(message = "O campo Logradouro não pode ser vazio!")
	private String logradouro;
	
	@NotNull(message = "O campo CEP não pode ser vazio!")
	@Pattern(regexp = "[0-9]+", message = "O campo CEP deve ser composto apenas por números!")
	private String cep;
	
	@Pattern(regexp = "[0-9]+", message = "O campo número deve ser composto apenas por números!")
	private String numero;
	
	@NotNull(message = "O campo Cidade não pode ser vazio!")
	private String cidade;
	
	@NotNull(message = "O campo Pessoa não pode ser vazio!")
	@Pattern(regexp = "^[0-9]*$", message = "O campo Pessoa deve ser do tipo númerico!")
	@Range(min=1, message = "O valor minimo para o campo Pessoa é 1")
	private String id_pessoa;


	public EnderecoForm(
		String logradouro,
		String cep, 
		String numero,
		String cidade,
		String id_pessoa
	) {
		this.logradouro = logradouro;
		this.cep = cep;
		this.numero = numero;
		this.cidade = cidade;
		this.id_pessoa = id_pessoa;
	}
	
	public String getLogradouro() {
		return logradouro;
	}

	public String getCep() {
		return cep;
	}

	public String getNumero() {
		return numero;
	}

	public String getCidade() {
		return cidade;
	}

	public String getId_pessoa() {
		return id_pessoa;
	}

	public Endereco transform(PessoaRepository pessoaRepository) {
		
		Optional<Pessoa> buscaPessoa = pessoaRepository.findById(Long.parseLong(id_pessoa));
		
		if(buscaPessoa.isEmpty()) throw new RecordNotFoundException("Pessoa não encontrada!");
		
		Pessoa pessoa = buscaPessoa.get();
		
		return new Endereco(logradouro, Long.parseLong(cep), Long.parseLong(numero), cidade, pessoa);
	}
}
