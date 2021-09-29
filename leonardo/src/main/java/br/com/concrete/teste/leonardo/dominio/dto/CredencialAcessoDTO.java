/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.dominio.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
@JsonPropertyOrder(value = {"name", "password"})
public class CredencialAcessoDTO implements Serializable {

/** The Constant serialVersionUID. */
private static final long serialVersionUID = 1L;
	
	/** The username. */
	private String email;
	
	/** The password. */
	private String password;
	
}
