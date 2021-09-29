/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.util.excecao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class ApiManipuladorExcecao.
 * Manipulacao geral de todas as Excecoes da API.
 */
@ControllerAdvice
public class ApiManipuladorExcecao extends ResponseEntityExceptionHandler {


	/**
	 * Manipulador negocio.
	 * Para ser lancado de forma personalizada
	 * @param ex the ex
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@ExceptionHandler(NegocioExcecao.class)
	public ResponseEntity<Object> manipuladorNegocio(NegocioExcecao ex, WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		HttpStatus estadoHttp = HttpStatus.BAD_REQUEST;
		ApiErro apiErro = new ApiErro();
		apiErro.setEstadoHttp(estadoHttp);
		apiErro.setMensagem(ex.getMessage());
		return handleExceptionInternal(ex, apiErro, new HttpHeaders(), apiErro.getEstadoHttp(), requisicao);
	}



	/**
	 * Manipulador Para todos as excecoes nao tratadas e para os erros 
	 * do servidor http 500.
	 *
	 * @param ex the ex
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	// 500
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> manipuladorTodos(final Exception ex, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		logger.error("erro: ", ex);
		final ApiErro apiErro = new ApiErro(HttpStatus.INTERNAL_SERVER_ERROR, 
				ex.getLocalizedMessage());
		return handleExceptionInternal(ex, apiErro, new HttpHeaders(), apiErro.getEstadoHttp(), requisicao);
	}

	/**
	 * Manipulador de conflito da API.
	 *
	 * @param ex the ex
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	// 409
	@ExceptionHandler({ InvalidDataAccessApiUsageException.class })
	public ResponseEntity<Object> handleConflict(final InvalidDataAccessApiUsageException ex,
			final WebRequest requisicao) {
		logger.error("Código 409", ex);
		final String corpoResposta = "Não pode fazer uso dos dados da API!";
		return handleExceptionInternal(ex, corpoResposta, new HttpHeaders(), HttpStatus.CONFLICT, requisicao);
	}

	/**
	 * Manipulador de conflitos, banco de dados e DTO`s.
	 *
	 * @param ex the ex
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@ExceptionHandler({ DataAccessException.class })
	protected ResponseEntity<Object> manipuladorConflito(final DataAccessException ex, final WebRequest requisicao) {
		logger.error("Código 409", ex);
		final String corpoResposta = "Não pode fazer uso dos dados (DTException):" + ex.getLocalizedMessage();
		return handleExceptionInternal(ex, corpoResposta, new HttpHeaders(), HttpStatus.CONFLICT, requisicao);
	}

	/**
	 * Manipulador tipo de argumento imcompativel.
	 * Suporta Media JSON tudo q for diferente ou mal formatado 
	 * retorna excecao.
	 * @param ex the ex
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> manipuladorTipoArgumentoImcompativel(final MethodArgumentTypeMismatchException ex,
			final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final String erro = ex.getName() + " Deveria ser do tipo: " 
		+ ex.getRequiredType().getName();
		
		final ApiErro apiErro = new ApiErro(HttpStatus.BAD_REQUEST, 
				ex.getLocalizedMessage(), erro);
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Metodo com argumento invalido 
	 * se a requisicao e GET so aceitara GET
	 * ou retornara a excecao
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final List<String> erros = new ArrayList<>();
		for (final FieldError erro : ex.getBindingResult().getFieldErrors()) {
			erros.add(erro.getField() + ": " + erro.getDefaultMessage());
		}
		for (final ObjectError erro : ex.getBindingResult().getGlobalErrors()) {
			erros.add(erro.getObjectName() + ": " + erro.getDefaultMessage());
		}
		final ApiErro apiErro = new ApiErro(HttpStatus.BAD_REQUEST, 
				ex.getLocalizedMessage());
		return handleExceptionInternal(ex, apiErro, cabecalho, apiErro.getEstadoHttp(), requisicao);
	}

	/**
	 * Manipulador para mensagem que nao pode ser lida, JSON com campos invalidos,
	 * tipos etc.
	 *
	 * @param ex the ex
	 * @param headers the headers
	 * @param status the status
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		HttpStatus estadoHttp = HttpStatus.BAD_REQUEST;
		ApiErro apiErro = new ApiErro();
		apiErro.setEstadoHttp(estadoHttp);
		apiErro.setMensagem("Json Inválido (1 ou Mais campos do JSON estão inválidos): " + ex.getLocalizedMessage());

		return handleExceptionInternal(ex, apiErro, new HttpHeaders(), 
				apiErro.getEstadoHttp(), requisicao);
	}

	/**
	 * Manipulador das Excecoes Bind, se o campo e valor estarem
	 * do tipo correto passa se nao retorna essa excecao
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders cabecalho, HttpStatus estadoHttp,
			WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final List<String> erros = new ArrayList<>();
		for (final FieldError erro : ex.getBindingResult().getFieldErrors()) {
			erros.add(erro.getField() + ": " + erro.getDefaultMessage());
		}
		for (final ObjectError erro : ex.getBindingResult().getGlobalErrors()) {
			erros.add(erro.getObjectName() + ": " + erro.getDefaultMessage());
		}
		final ApiErro apiErro = new ApiErro(HttpStatus.BAD_REQUEST, 
				ex.getLocalizedMessage());
		return handleExceptionInternal(ex, apiErro, new HttpHeaders(), apiErro.getEstadoHttp(), requisicao);
	}

	/**
	 * Manipulador para tipos nao compatives, caracteres invalidos.
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders cabecalho,
			final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final String erro = ex.getValue() + " Valor para " + ex.getPropertyName() + " Deve ser do tipo "
				+ ex.getRequiredType();

		final ApiErro apiErro = new ApiErro(HttpStatus.BAD_REQUEST, 
				ex.getLocalizedMessage(), erro);
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Manipulador de excecao cao esteja faltando uma parte da requisicao.
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estado http
	 * @param requisicao the requisicao
	 * @return the response entity
	 */
	@Override
	public ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final String erro = ex.getRequestPartName() + " Parte esta faltando";
		final ApiErro apiErro = new ApiErro(HttpStatus.BAD_REQUEST,
				ex.getLocalizedMessage(), erro);
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Manipulador para parametros do Servlet que estejam faltando.
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final String erro = ex.getParameterName() + " Parâmetro esta faltando";
		final ApiErro apiErro = new ApiErro(HttpStatus.BAD_REQUEST, 
				ex.getLocalizedMessage(), erro);
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Manipulador para excecao nao encontrada,
	 * utilizado para manipuladores de erros do Spring que sao interfaces.
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final String erro = "Sem manipulador " + ex.getHttpMethod() + " " + ex.getRequestURL();
		final ApiErro apiErro = new ApiErro(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), erro);
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Metodo http nao suportado.
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	// 405
	@Override
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append("Método Não suportado por essa requisição método suportado e: ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
		final ApiErro apiErro = new ApiErro(HttpStatus.METHOD_NOT_ALLOWED, 
				ex.getLocalizedMessage(), builder.toString());
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Manipulador tipo de media nao suportado http media type not supported.
	 *
	 * @param ex the ex
	 * @param cabecalho the cabecalho
	 * @param estadoHttp the estadoHttp
	 * @param requisicao the requisicao
	 * @return response entity
	 */
	// 415
	@Override
	public ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
		logger.error(ex.getClass().getName());
		final StringBuilder construtor = new StringBuilder();
		construtor.append(ex.getContentType());
		construtor.append("Tipo de media nao suportado.O tipo suportado e: ");
		ex.getSupportedMediaTypes().forEach(t -> construtor.append(t + " "));

		final ApiErro apiErro = new ApiErro(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(),
				construtor.substring(0, construtor.length() - 1));
		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
	}

	/**
	 * Manipulador para tipo de media nao aceitavel.
	 *
	 * @param ex the ex
	 * @param headers the headers
	 * @param status the status
	 * @param request the request
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error(ex.getClass().getName());
		HttpStatus estadoHttp = HttpStatus.NOT_ACCEPTABLE;
		ApiErro apiErro = new ApiErro();
		apiErro.setEstadoHttp(estadoHttp);
		apiErro.setMensagem("Tipo de media não aceito" + ex.getMessage());
		return handleExceptionInternal(ex, new HttpHeaders(), headers, status, request);
	}

	/**
	 * Manipulador para missingpathVariable,
	 * indica se tem erros na parametrizacao dos controladores (API).
	 *
	 * @param ex the ex
	 * @param headers the headers
	 * @param status the status
	 * @param request the request
	 * @return response entity
	 */
	@Override
	public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		logger.error(ex.getClass().getName());
		HttpStatus estadoHttp = HttpStatus.INTERNAL_SERVER_ERROR;
		ApiErro apiErro = new ApiErro();
		apiErro.setEstadoHttp(estadoHttp);
		apiErro.setMensagem("Erro: Váriavel no caminho está errada: " + ex.getMessage());
		return handleExceptionInternal(ex, new HttpHeaders(), headers, status, request);
	}

	/**
	 * Manipulador para conversao nao suportada.
	 *
	 * @param ex the ex
	 * @param headers the headers
	 * @param status the status
	 * @param request the request
	 * @return the response entity
	 */
	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error(ex.getClass().getName());
		HttpStatus estadoHttp = HttpStatus.INTERNAL_SERVER_ERROR;
		ApiErro apiErro = new ApiErro();
		apiErro.setEstadoHttp(estadoHttp);
		apiErro.setMensagem("Erro: Conversão Não Suportada : " + ex.getMessage());
		return handleExceptionInternal(ex, new HttpHeaders(), headers, status, request);
	}

	/**
	 * Manipulador para Mensagem HTTp com erro que nao podem ser escritas
	 *
	 * @param ex the ex
	 * @param headers the headers
	 * @param status the status
	 * @param request the request
	 * @return response entity
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error(ex.getClass().getName());
		HttpStatus estadoHttp = HttpStatus.INTERNAL_SERVER_ERROR;
		ApiErro apiErro = new ApiErro();
		apiErro.setEstadoHttp(estadoHttp);
		apiErro.setMensagem("Erro: Mensagem não pode ser escrita : " + ex.getMessage());
		return handleExceptionInternal(ex, new HttpHeaders(), headers, status, request);
	}
}
