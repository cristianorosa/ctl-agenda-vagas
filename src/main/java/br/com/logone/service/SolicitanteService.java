package br.com.logone.service;

import java.util.List;
import java.util.Optional;

import br.com.logone.entity.Solicitante;
import br.com.logone.exception.BusinessException;

public interface SolicitanteService {
	
	Solicitante save(Solicitante solicitante);
	List<Solicitante> getAll();
	void deleteById(Long id) throws BusinessException;
	Solicitante insert(Solicitante solicitante) throws BusinessException;
	Optional<Solicitante> findById(Long id);
	Optional<Solicitante> findByNome(String nome);

}
