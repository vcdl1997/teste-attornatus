package br.com.testeattornatus.controllers.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.testeattornatus.controllers.handlers.exceptions.RecordNotFoundException;
import br.com.testeattornatus.entities.ExceptionResponse;

@ControllerAdvice
public class AplicationExceptionHandler {
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse>  handleServerError(
      Exception ex, 
      WebRequest request
    ) {
		ex.printStackTrace();
		
		ExceptionResponse response = new ExceptionResponse(
			"Erro interno no Servidor, tente novamente em alguns minutos;", 
			request.getDescription(false), 
			LocalDateTime.now()
		);
        
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRecordNotFoundException(
      Exception ex, 
      WebRequest request
    ) {
		
		ExceptionResponse response = new ExceptionResponse(
			ex.getMessage(), 
			request.getDescription(false), 
			LocalDateTime.now()
		);
        
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ExceptionResponse>> handle(
		MethodArgumentNotValidException exception, 
		WebRequest request
	) {
		
		List<ExceptionResponse> lista = new ArrayList<>();
		
		exception.getBindingResult().getFieldErrors().forEach(x -> {
			String mensagem = messageSource.getMessage(x, LocaleContextHolder.getLocale());
			
			lista.add(
				new ExceptionResponse(
					mensagem, 
					request.getDescription(false), 
					LocalDateTime.now()
				)
			);
		});
		
		return new ResponseEntity<>(lista, HttpStatus.BAD_REQUEST);
	}
}
