package br.com.concrete.teste.leonardo.dominio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.concrete.teste.leonardo.dominio.modelo.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>{

	@Query("SELECT p FROM Phone p JOIN p.usuario u WHERE u.id =:id")
	public List<Phone> buscaPhonePorIdUsuario(@Param("id") Long id);
}