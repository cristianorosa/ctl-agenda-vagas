package br.com.logone.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.logone.entity.Agendamento;
import br.com.logone.entity.Solicitante;

public interface AgendamentoRepository extends CrudRepository<Agendamento, Long> {
	List<Agendamento> findBySolicitante(Solicitante solicitante);
	List<Agendamento> findAll();
	Agendamento findById(long id);
	List<Agendamento> findAllByDataAgendamento(LocalDate data);
	List<Agendamento> findAllByDataAgendamentoAndSolicitante(LocalDate dataInicio,
			Solicitante solicitante);
	List<Agendamento> findAllBySolicitante(Solicitante solicitante);
	List<Agendamento> findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoGreaterThanEqualAndSolicitante(
			LocalDate dataInicio, LocalDate dataFim, Solicitante solicitante);
	List<Agendamento> findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoGreaterThanEqual(
			LocalDate dataInicio, LocalDate dataFim);
	List<Agendamento> findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqualAndSolicitante(
			LocalDate dataInicio, LocalDate dataFim, Solicitante solicitante);
	List<Agendamento> findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqual(LocalDate dataInicio,
			LocalDate dataFim);
}
