/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.dominio.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@JsonPropertyOrder(value = {"name", "email", 
		"password", "phones"})
@Builder
public class CadastroDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The username. */
	@ApiModelProperty(value = "Nome do novo usuário do sistema", 
			name = "name", 
			dataType = "String", 
			required = true, 
			hidden = false, 
			example = "usuario")
	private String name;
	
	@ApiModelProperty(value = "Novo email para o usuário do sistema", 
			name = "email", 
			dataType = "String", 
			required = true, 
			hidden = false, 
			example = "email@email.com")
	private String email;

	/** The password. */
	@ApiModelProperty(value = "Nova senha para o usuário do sistema", 
			name = "password", 
			dataType = "String", 
			required = true, 
			hidden = false, 
			example = "123")
	private String password;

	@ApiModelProperty(value = "Lista de Telefones e ddds", 
			name = "email", 
			dataType = "PhoneDTO", 
			required = true, 
			hidden = false)
	private List<PhoneDTO> phones;
	
	/** The name. */
	@ApiModelProperty(value = "Descricao do nome do Perfil", 
			name = "description", 
			dataType = "String", 
			required = false, 
			hidden = true, 
			example = "ADMIN")
	private String description;

}
