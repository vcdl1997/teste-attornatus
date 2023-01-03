package br.com.testeattornatus.controllers.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.testeattornatus.entities.Pessoa;

public class PessoaForm {
	
	private static final SimpleDateFormat FORMAT_DATA = new SimpleDateFormat("yyyy-MM-dd");
	
	@NotNull(message = "O campo Nome não pode ser vazio!")
	private String nome;
	
	@NotNull(message = "O campo Data de Nascimento não pode ser vazio!")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "O campo de Data de Nascimento de estar no formato YYYY-MM-DD")
	private String dataNascimento;

	public PessoaForm(
		String nome,
		String dataNascimento
	) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}
	
	public Pessoa transform() throws ParseException {
		Date dataNasc = FORMAT_DATA.parse(dataNascimento);
		return new Pessoa(nome, dataNasc);
	}
}
