package br.com.testeattornatus.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExceptionResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private String message;
	private String details;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime timestamp;
	
	public ExceptionResponse(String message, String details, LocalDateTime timestamp) {
		this.message = message;
		this.details = details;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
}
