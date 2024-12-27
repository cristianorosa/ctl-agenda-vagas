package br.com.logone.service;

import java.util.List;

import br.com.logone.entity.Vaga;
import br.com.logone.exception.BusinessException;

public interface VagaService {

	Vaga insert(Vaga vaga) throws BusinessException;
	void deleteById(Long id) throws BusinessException;
	List<Vaga> getAll();

}
