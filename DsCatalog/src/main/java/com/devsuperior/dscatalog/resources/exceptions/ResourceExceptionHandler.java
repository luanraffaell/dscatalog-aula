package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Resource not found");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> handleDatabase(DataBaseException ex, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(HttpStatus.BAD_REQUEST.value());
		err.setError("Database exception");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException ex, HttpServletRequest request){
		ValidationError err = new ValidationError();
		err.setTimeStamp(Instant.now());
		err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		err.setError("Database exception");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
		
		ex.getBindingResult().getFieldErrors().forEach(x -> err.addError(x.getField(), x.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}
}
