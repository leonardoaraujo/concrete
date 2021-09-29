/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.util.excecao;

/**
 *  Class NegocioExcecao.
 */
public class NegocioExcecao extends RuntimeException {

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor negocio excecao.
	 *
	 * @param mensagem the mensagem
	 */
	public NegocioExcecao(String mensagem) {
		super(mensagem);
	}
}
