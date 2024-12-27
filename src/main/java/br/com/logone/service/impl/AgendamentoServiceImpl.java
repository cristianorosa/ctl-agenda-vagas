package br.com.logone.service.impl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import br.com.logone.dto.AgendamentoDTO;
import br.com.logone.entity.Agendamento;
import br.com.logone.entity.Solicitante;
import br.com.logone.entity.Vaga;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.AgendamentoRepository;
import br.com.logone.repository.SolicitanteRepository;
import br.com.logone.repository.VagaRepository;
import br.com.logone.service.AgendamentoService;

@Service
@PropertySource("classpath:messages.properties")
public class AgendamentoServiceImpl implements AgendamentoService {
	
	@Value("${validation.invalid.vaga.indisponivel}")
	private String msgVagaIndisponivelParaData;
	
	@Value("${validation.invalid.agendamento.data.retroativa}")
	private String msgDataRetroativa;
	
	@Value("${validation.invalid.vaga.indisponivel.solicitante}")
	private String msgVagaIndisponivelParaSolicitante;
	
	@Value("${validation.invalid.data.consulta}")
	private String msgAmbasDatasPreechidas;
	
	private AgendamentoRepository repo;
	
	private VagaRepository repoVaga;
	
	private SolicitanteRepository repoSolicitante;
	
	public AgendamentoServiceImpl() {}
		
	@Autowired
	public AgendamentoServiceImpl(AgendamentoRepository repo, VagaRepository repoVaga, SolicitanteRepository repoSolicitante) {
		this.repo = repo;
		this.repoVaga = repoVaga;
		this.repoSolicitante = repoSolicitante;
	}

	@Override
	public List<Agendamento> getAll() {
		return repo.findAll(); 
	}

	@Override
	public Agendamento insert(Agendamento agendamento) throws BusinessException {
   	    verificaSeDataTemDataRetroativa(agendamento);
		verificarVagasDisponivel(agendamento);
		
		return repo.save(agendamento);
	}

	@Override
	public void deleteById(Long id) {
		Optional<Agendamento> agenda = repo.findById(id);
		if (agenda.isPresent()) {
			repo.delete(agenda.get());
		}
	}
	
	private void verificaSeDataTemDataRetroativa(Agendamento agendamento) throws BusinessException {
		if (agendamento.getDataAgendamento()!= null && agendamento.getDataAgendamento().isBefore(LocalDate.now())) {
			throw new BusinessException(msgDataRetroativa);
		}
	}
	
	private void verificarVagasDisponivel(Agendamento agendamento) throws BusinessException {
		List<Vaga> vagas = repoVaga.findAllByDataInicioLessThanEqualAndDataFimGreaterThanEqual(agendamento.getDataAgendamento(), agendamento.getDataAgendamento());
		
		Integer qtdVagas = vagas.stream()
								.mapToInt(Vaga::getQuantidade) 
								.sum();
		
		Optional<Vaga> maiorDataVaga = vagas.stream()
	            .max((p1, p2) -> p1.getDataFim().compareTo(p2.getDataFim()));
		
		Optional<Vaga> menorDataVaga = vagas.stream()
	            .min((p1, p2) -> p1.getDataFim().compareTo(p2.getDataFim()));
		
		List<Agendamento> agendas = new ArrayList<>();
		
		vagas.forEach(o -> {
			List<Agendamento> ag = repo.findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqual(o.getDataInicio(), o.getDataFim());
			ag.forEach( i -> {
				if (!agendas.contains(i)) {
					agendas.add(i);
				}
			});
		});	
		
		Integer qtdAgenda = agendas.size();
		
		if ((qtdVagas-qtdAgenda) <= 0 || maiorDataVaga.isEmpty() || menorDataVaga.isEmpty()) {
			throw new BusinessException(msgVagaIndisponivelParaData);
		}
		
		Integer qtdAgendaSolicitante = repo.findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqualAndSolicitante(menorDataVaga.get().getDataInicio(), maiorDataVaga.get().getDataFim(), agendamento.getSolicitante()).size();
		if ((qtdVagas/4) <= qtdAgendaSolicitante) {
			throw new BusinessException(msgVagaIndisponivelParaSolicitante);
		}
	}

	@Override
	public List<AgendamentoDTO> consultar(String nome, LocalDate dataInicio, LocalDate dataFim) throws BusinessException {
		List<AgendamentoDTO> ret = new ArrayList<>();
		Solicitante solicitante = null;
		List<Agendamento> ag = new ArrayList<>();
		
		if (nome != null ) {
			solicitante = repoSolicitante.findByNome(nome);			
		}
		
		if (dataInicio == null || dataFim == null) {
			throw new BusinessException(msgAmbasDatasPreechidas);		
		} else if (solicitante != null) {
			ag.addAll(repo.findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqualAndSolicitante(dataInicio, dataFim, solicitante));
		} else {
			ag.addAll(repo.findAllByDataAgendamentoGreaterThanEqualAndDataAgendamentoLessThanEqual(dataInicio, dataFim));
		}
		
		List<Vaga> vagas = repoVaga.findAllByDataInicioLessThanEqualAndDataFimGreaterThanEqual(dataInicio, dataFim);
		
		Integer qtdVagas = vagas.stream()
				.mapToInt(Vaga::getQuantidade) 
				.sum();
		
		Map<Solicitante, Long> somaPorNome = ag.stream()
	            .collect(Collectors.groupingBy(
	            	Agendamento::getSolicitante,        
	                Collectors.counting()  // Somar as quantidades
	            ));
		
		somaPorNome.forEach((chave, valor) -> {
			AgendamentoDTO f = new AgendamentoDTO();
			f.setNome(chave.getNome());
			f.setQtdVagas(qtdVagas);
			f.setTotalAgendamentos(valor.intValue());

			if (qtdVagas > 0) {
				DecimalFormat formato = new DecimalFormat("#,##"); 
				Double percent = (valor.doubleValue()/qtdVagas)*100;
				f.setPercentual(Double.valueOf(formato.format(percent)));
				ret.add(f);
			}
			
		});
		return ret;
	}
}
