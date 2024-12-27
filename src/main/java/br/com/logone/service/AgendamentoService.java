package br.com.logone.service;

import java.time.LocalDate;
import java.util.List;

import br.com.logone.dto.AgendamentoDTO;
import br.com.logone.entity.Agendamento;
import br.com.logone.exception.BusinessException;

public interface AgendamentoService {

	List<Agendamento> getAll();
	Agendamento insert(Agendamento agendamento) throws BusinessException;
	void deleteById(Long id);
	List<AgendamentoDTO> consultar(String nome, LocalDate dataInicio, LocalDate dataFim) throws BusinessException;

}

