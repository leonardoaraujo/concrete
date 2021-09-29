/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.dominio.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Class Perfil.
 */
@Entity
@Table(name = "tb_perfil", schema = "leonardo")
@Data
@EqualsAndHashCode(of = { "id"})
public class Perfil implements GrantedAuthority, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/** The description. */
	@Column(name = "ds_description")
	private String description;
	
	/** O usuario do sistema. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id",  insertable = false, updatable = false)
	private Usuario usuario;

	/**
	 * Gets the authority.
	 *
	 * @return the authority
	 */
	@Override
	public String getAuthority() {
		return this.description;
	}


}
