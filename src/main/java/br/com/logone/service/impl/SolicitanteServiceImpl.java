package br.com.logone.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.logone.entity.Solicitante;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.SolicitanteRepository;
import br.com.logone.service.SolicitanteService;

@Service
@PropertySource("classpath:messages.properties")
public class SolicitanteServiceImpl implements SolicitanteService {
	
	@Value("${validation.invalid.solicitante.exists}")
	private String msgSolicitanteExits;
	
	@Value("${validation.invalid.solicitante.nao.pode.ser.excluido}")
	private String msgSolicitanteNaoPodeSerExcluidoJaPossuiAgenda;
	
	private SolicitanteRepository repo;
	
	public SolicitanteServiceImpl() {}

	@Autowired
	public SolicitanteServiceImpl(SolicitanteRepository repo) {
		this.repo = repo;
	}

	@Override
	public Solicitante save(Solicitante solicitante) {
		return repo.save(solicitante);
	}

	@Override
	public List<Solicitante> getAll() {
		return repo.findAll();
	}

	@Override
	public void deleteById(Long id) throws BusinessException {
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(msgSolicitanteNaoPodeSerExcluidoJaPossuiAgenda);
		}
	}

	@Override
	public Solicitante insert(Solicitante solicitante) throws BusinessException {
		try { 
			return repo.save(solicitante);
		} catch (RuntimeException e) {
			throw new BusinessException(msgSolicitanteExits);
		}
	}

	@Override
	public Optional<Solicitante> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Optional<Solicitante> findByNome(String nome) {
		if (nome != null) {
			Solicitante ret = repo.findByNome(nome);
			if (ret != null) {
				return Optional.of(ret);
			}
		}
		return Optional.empty();
	}
}
