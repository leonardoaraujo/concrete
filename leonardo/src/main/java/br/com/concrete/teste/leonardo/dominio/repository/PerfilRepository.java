package br.com.concrete.teste.leonardo.dominio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.concrete.teste.leonardo.dominio.modelo.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{

	
	@Query("SELECT p FROM Perfil p WHERE p.id =:id")
	public List<Perfil> buscaPerfilPorIdUsuario(@Param("id") Long id);
	
}