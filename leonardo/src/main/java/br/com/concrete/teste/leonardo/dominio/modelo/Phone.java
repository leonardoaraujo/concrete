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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Class Phone.
 */
@Entity
@Table(name = "tb_phone", schema = "leonardo")
@Data
@EqualsAndHashCode(of = { "id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/** The number. */
	@Column(name = "nr_number", unique = true)
	private Long number;
	
	/** The ddd. */
	@Column(name = "nr_ddd", unique = true)
	private Integer ddd;
	
	/** O usuario do sistema. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id",  insertable = false, updatable = false)
	private Usuario usuario;
}
