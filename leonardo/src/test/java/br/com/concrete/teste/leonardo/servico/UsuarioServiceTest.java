package br.com.concrete.teste.leonardo.servico;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.concrete.teste.leonardo.api.UsuarioApi;
import br.com.concrete.teste.leonardo.dominio.dto.CadastroDTO;
import br.com.concrete.teste.leonardo.dominio.dto.PhoneDTO;
import br.com.concrete.teste.leonardo.dominio.modelo.Perfil;
import br.com.concrete.teste.leonardo.dominio.modelo.Phone;
import br.com.concrete.teste.leonardo.dominio.modelo.Usuario;
import br.com.concrete.teste.leonardo.dominio.repository.PerfilRepository;
import br.com.concrete.teste.leonardo.dominio.repository.PhoneRepository;
import br.com.concrete.teste.leonardo.dominio.repository.UsuarioRepository;
import br.com.concrete.teste.leonardo.util.config.jwt.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@InjectMocks
	private UsuarioApi usuarioApi;
	
	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtTokenProvider tokenProvider;
	
	@Mock
	private UsuarioService usuarioService;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private PhoneRepository phoneRepository;
	
	@Mock
	private PerfilRepository perfilRepository;

	@Test
	void deveCriarUsuario() {

		PhoneDTO phoneDTO = PhoneDTO.builder()
				.number(10310313L)
				.ddd(99)
				.build();
		
		List<PhoneDTO> phonesDTO = new ArrayList<>();
		phonesDTO.add(phoneDTO);
		
		Perfil perfil = new Perfil();
		perfil.setDescription("ADMIN");
		List<Perfil> perfis = new ArrayList<>();
		perfis.add(perfil);
		

		CadastroDTO cadastro = CadastroDTO.builder()
				.name("testeUsuario")
				.email("email222@email.com")
				.password("123")
				.phones(phonesDTO)
				.build();
		
//		usuarioApi.incluir(cadastro);
//		usuarioService.criaUsuario(cadastro);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String resultado = bCryptPasswordEncoder.encode(cadastro.getPassword());
		
		List<Phone> phones = new ArrayList<>();
		Phone phone = Phone.builder()
				.number(10310313L)
				.ddd(99)
				.usuario(null)
				.build();
		phones.add(phone);
		Usuario usuario = Usuario.builder()
				.name(cadastro.getName())
				.email(cadastro.getEmail())
				.password(resultado)
				.phones(phones)
				.perfis(perfis)
				.build();


		usuarioRepository.save(usuario);
		usuarioService.findByUsuario(usuario.getName());
		List<Usuario> user = usuarioRepository
				.findAll();
		
		assertThat(user.size() > 0);
	}
}
