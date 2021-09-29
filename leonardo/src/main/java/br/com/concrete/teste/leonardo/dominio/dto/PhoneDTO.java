/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.dominio.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "number")
@JsonPropertyOrder(value = {"number", "ddd"})
@Builder
public class PhoneDTO implements Serializable {

/** The Constant serialVersionUID. */
private static final long serialVersionUID = 1L;
	
	/** The number. */
	@ApiModelProperty(value = "Número de telefone do Usuário", 
			name = "number", 
			dataType = "Integer", 
			required = true, 
			hidden = false, 
			example = "123456789")
	private Long number;
	
	/** The ddd. */
	@ApiModelProperty(value = "DDD do telefone do Usuário", 
			name = "ddd", 
			dataType = "Integer", 
			required = true, 
			hidden = false, 
			example = "61")
	private Integer ddd;

}
