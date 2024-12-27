package br.com.logone.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import br.com.logone.entity.Agendamento;
import br.com.logone.entity.Vaga;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.AgendamentoRepository;
import br.com.logone.repository.VagaRepository;
import br.com.logone.service.VagaService;

@Service
@PropertySource("classpath:messages.properties")
public class VagaServiceImpl implements VagaService {

	@Value("${validation.invalid.vaga.data.retroativa}")
	private String msgDataRetroativa;
	
	@Value("${validation.invalid.vaga.data.intervalo}")
	private String msgDataFimMenorQueDataInicio;
	
	@Value("${validation.invalid.vaga.update.conflita.agenda}")
	private String msgUpdateConflitaVagaAgendada;
	
	private VagaRepository repo;
	
	private AgendamentoRepository repoAgendamento;
	
	public VagaServiceImpl() {}

	@Autowired
	public VagaServiceImpl(VagaRepository repo, AgendamentoRepository repoAgendamento) {
		this.repo = repo;
		this.repoAgendamento = repoAgendamento;
	}

	@Override
	public Vaga insert(Vaga vaga) throws BusinessException {
		verificaSeDataTemDataRetroativa(vaga);
		verificaSeDataFinalHeMaiorQueDataInicial(vaga);
		return repo.save(vaga);
	}

	@Override
	public void deleteById(Long id) throws BusinessException {
		Optional<Vaga> exist = Optional.empty();
		if (id != null ) {
			exist = repo.findById(id);
		} 
		
		if (exist.isPresent()) {
			verificaSeExisteAgendamentoConflitante(exist.get());
			repo.delete(exist.get());
		}
	}

	private void verificaSeExisteAgendamentoConflitante(Vaga vaga) throws BusinessException {
		List<Agendamento> agendas = repoAgendamento.findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqual(vaga.getDataInicio(), vaga.getDataFim());
		if (!agendas.isEmpty()) {
			throw new BusinessException(msgUpdateConflitaVagaAgendada);
		}		
	}

	private void verificaSeDataFinalHeMaiorQueDataInicial(Vaga vaga) throws BusinessException {
		if (vaga.getDataInicio() != null && vaga.getDataFim() != null && vaga.getDataInicio().isAfter(vaga.getDataFim())) {
			throw new BusinessException(msgDataFimMenorQueDataInicio);
		}
	}

	private void verificaSeDataTemDataRetroativa(Vaga vaga) throws BusinessException {
		if (vaga.getDataInicio()!= null && vaga.getDataInicio().isBefore(LocalDate.now())) {
			throw new BusinessException(msgDataRetroativa);
		}
		if (vaga.getDataFim()!= null && vaga.getDataFim().isBefore(LocalDate.now())) {
			throw new BusinessException(msgDataRetroativa);
		}
	}

	@Override
	public List<Vaga> getAll() {
		return repo.findAll();
	}
}
