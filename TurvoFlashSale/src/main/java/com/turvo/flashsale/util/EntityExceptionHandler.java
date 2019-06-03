package com.turvo.flashsale.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

	@ControllerAdvice
	@RestController
	public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

	
	
		@ExceptionHandler(value = { Exception.class, IllegalStateException.class })
	    protected ResponseEntity<Object> handleConflict(
	    		Exception ex, WebRequest request) {
			List<String> responseMsgs = new ArrayList<String>();
			Throwable[] supressedEx = ex.getSuppressed();
			ex.printStackTrace();
			for(Throwable th : supressedEx) {
				responseMsgs.add(th.getMessage());
			}
	        return handleExceptionInternal(ex, responseMsgs, 
	          new HttpHeaders(), HttpStatus.CONFLICT, request);
	    }
}