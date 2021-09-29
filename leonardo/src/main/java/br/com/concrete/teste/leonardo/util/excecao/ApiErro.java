/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.util.excecao;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErro {

	/** O estado http da mensagem de erro. */
	private HttpStatus estadoHttp;
	
	/** A mensagem de erro. */
	private String mensagem;
	
	/** A mensagem de erro. */
	private String erro;

	public ApiErro(HttpStatus estadoHttp, String mensagem) {
		super();
		this.estadoHttp = estadoHttp;
		this.mensagem = mensagem;
	}
	
}
