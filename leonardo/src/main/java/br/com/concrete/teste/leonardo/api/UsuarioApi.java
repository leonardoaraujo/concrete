/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.api;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.concrete.teste.leonardo.dominio.dto.CadastroDTO;
import br.com.concrete.teste.leonardo.dominio.dto.CredencialAcessoDTO;
import br.com.concrete.teste.leonardo.dominio.modelo.Phone;
import br.com.concrete.teste.leonardo.dominio.modelo.Usuario;
import br.com.concrete.teste.leonardo.servico.UsuarioService;
import br.com.concrete.teste.leonardo.util.config.jwt.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class AuthApi.
 */
@Api(tags = "Auth") 
@RestController
@RequestMapping("/auth")
public class UsuarioApi {
	
	/** The authentication manager. */
	@Autowired
	private AuthenticationManager authenticationManager;

	/** The token provider. */
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	/** The repository. */
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Signin.
	 *
	 * @param data the data
	 * @return the response entity
	 */
	@ApiOperation(value = "Authenticates a user and returns a token", tags = "Auth")
	@PostMapping(value = "/signin", produces = { "application/json" }, 
			consumes = { "application/json" })
	public ResponseEntity<Object> signin(@RequestBody CredencialAcessoDTO data) {
		try {
			String name = data.getEmail();
			String password = data.getPassword();	
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(name, password));
			
			Usuario user = usuarioService.findByUsuario(name);
			String token = "";
			if (user != null) {
				token = tokenProvider.createToken(name, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Usuário: " + name + " não encontrado!");
			}
			List<Phone> phones = usuarioService.buscaPerfilPorIdUsuario(user.getId());
			
			Map<Object, Object> model = new HashMap<>();
			model.put("name", name);
			model.put("password", password);
			
			model.put("email", user.getEmail());
			model.put("phones", "");
			model.put("number", phones.get(0).getNumber());
			model.put("ddd", phones.get(0).getDdd());

			model.put("created", user.getCreated());
			model.put("modified", user.getModified());
			model.put("lastLogin", user.getLastLogin());
			
			model.put("token", token);
			return ok(model);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Usuário e/ou senha inválido(s)!");
		}
	}
	
	@ApiOperation(value = "Inclui novo usuário", tags = "Auth")
	@PostMapping(consumes = { "application/json" })
	public ResponseEntity<String> incluir(@RequestBody CadastroDTO cadastro) {
		usuarioService.criaUsuario(cadastro);
		return ResponseEntity.ok("Usuário criado com Sucesso!");
	}
}
