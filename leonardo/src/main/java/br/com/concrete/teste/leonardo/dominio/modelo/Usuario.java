/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.dominio.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Class Usuario.
 */
@Entity
@Table(name = "tb_user", schema = "leonardo")
@Data
@EqualsAndHashCode(of = { "id"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/** The uuid. */
	@Column(name = "ds_uuid", unique = true)
	private String uuid;
	
	/** The name. */
	@Column(name = "ds_name", unique = true)
	private String name;
	
	/** The email. */
	@Column(name = "ds_email", unique = true)
	private String email;
	
	/** The password. */
	@Column(name = "ds_password")
	private String password;
	
	/** The created. */
	@Column(name="dt_created")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss SSS")
	private LocalDate created;
	
	/** The modified. */
	@Column(name="dt_modified")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss SSS")
	private LocalDate modified;
	
	/** The last login. */
	@Column(name="dt_last_login")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss SSS")
	private LocalDate lastLogin;
	
	/** The token. */
	@Column(name = "ds_token")
	private String token;
	
	/** The account non expired. */
	@Column(name = "account_non_expired")
	private Boolean accountNonExpired;
	
	/** The account non locked. */
	@Column(name = "account_non_locked")
	private Boolean accountNonLocked;
	
	/** The credentials non expired. */
	@Column(name = "credentials_non_expired")
	private Boolean credentialsNonExpired;
	
	/** The enabled. */
	@Column(name = "enabled")
	private Boolean enabled;
	
	/** The perfis. */
	@OneToMany(mappedBy="usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Perfil> perfis;
	
	/** The phones. */
	@OneToMany(mappedBy="usuario", cascade = CascadeType.ALL)
	private List<Phone> phones;
	
	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		for (Perfil permission : this.perfis) {
			roles.add(permission.getDescription());
		}
		return roles;
	}

	/**
	 * Gets the authorities.
	 *
	 * @return the authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	@Override
	public String getUsername() {
		return this.name;
	}

	/**
	 * Checks if is account non expired.
	 *
	 * @return true, if is account non expired
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	/**
	 * Checks if is account non locked.
	 *
	 * @return true, if is account non locked
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	/**
	 * Checks if is credentials non expired.
	 *
	 * @return true, if is credentials non expired
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Pre persist.
	 */
	@PrePersist
	public void prePersist() {
		created = LocalDate.now();
		lastLogin = LocalDate.now();
		accountNonExpired = true;
		accountNonLocked = true;
		credentialsNonExpired = true;
		enabled = true;
	}
	
	/**
	 * Pre update.
	 */
	@PreUpdate
	public void preUpdate() {
		modified = LocalDate.now();
	}
}
