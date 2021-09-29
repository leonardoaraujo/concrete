/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.util.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class InvalidJwtAuthenticationException.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJwtAuthenticationException extends AuthenticationException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new invalid jwt authentication exception.
	 *
	 * @param exception the exception
	 */
	public InvalidJwtAuthenticationException(String exception) {
		super(exception);
	}
	
}
