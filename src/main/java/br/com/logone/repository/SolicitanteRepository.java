package br.com.logone.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.logone.entity.Solicitante;

public interface SolicitanteRepository extends CrudRepository<Solicitante, Long> {
	Solicitante findByNome(String nome);
	Solicitante findById(long id);
	List<Solicitante> findAll();
}
