/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.servico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.concrete.teste.leonardo.dominio.dto.CadastroDTO;
import br.com.concrete.teste.leonardo.dominio.modelo.Perfil;
import br.com.concrete.teste.leonardo.dominio.modelo.Phone;
import br.com.concrete.teste.leonardo.dominio.modelo.Usuario;
import br.com.concrete.teste.leonardo.dominio.repository.PhoneRepository;
import br.com.concrete.teste.leonardo.dominio.repository.UsuarioRepository;

/**
 * The Class UserServices.
 */
@Service
public class UsuarioService implements UserDetailsService {
	
	/** The user repository. */
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PhoneRepository phoneRepository;

	/**
	 * Load user by username.
	 *
	 * @param username the username
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = usuarioRepository.buscaPorNomeUsuario(username);
		if (user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Usuário: " + username + " Não encontrado");
		}
	}
	
	public Usuario findByUsuario(String name) {
		return usuarioRepository.buscaPorNomeUsuario(name);
	}
	
	public void criaUsuario(CadastroDTO cadastro) {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String resultado = bCryptPasswordEncoder.encode(cadastro.getPassword());
		Phone phone = Phone.builder()
				.number(cadastro.getPhones().get(0).getNumber())
				.ddd(cadastro.getPhones().get(0).getDdd())
				.build();
		
		List<Phone> phones = new ArrayList<>();
		phones.add(phone);
		
		Perfil perfil = new Perfil();
		perfil.setDescription("ADMIN");
		List<Perfil> perfis = new ArrayList<>();
		perfis.add(perfil);
		
		Usuario usuario = Usuario.builder()
				.name(cadastro.getName())
				.email(cadastro.getEmail())
				.password(resultado)
				.phones(phones)
				.perfis(perfis)
				.build();
		
		usuarioRepository.save(usuario);
		
	}
	
	public List<Phone> buscaPerfilPorIdUsuario(Long id) {
		return phoneRepository.buscaPhonePorIdUsuario(id);
	}
		
}