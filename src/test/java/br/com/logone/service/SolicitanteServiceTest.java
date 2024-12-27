package br.com.logone.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import br.com.logone.entity.Agendamento;
import br.com.logone.entity.Solicitante;
import br.com.logone.entity.Vaga;
import br.com.logone.enums.MotivosDeAgendamento;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.AgendamentoRepository;
import br.com.logone.repository.SolicitanteRepository;
import br.com.logone.repository.VagaRepository;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:messages.properties")
class SolicitanteServiceTest {
	
	@Value("${validation.invalid.solicitante.nao.pode.ser.excluido}")
	private String msgSolicitanteNaoPodeSerExcluidoJaPossuiAgenda;

	@Autowired
	SolicitanteRepository repo;
	
	@Autowired
	AgendamentoRepository repoAgenda;
	
	@Autowired
	VagaRepository repoVaga;

	@Autowired
	SolicitanteService service;
	
	@Test
	void insert() throws BusinessException {
		deleteAll();
		Solicitante savedSolicitante = service.insert(new Solicitante(null, "Teste"));

		assertNotNull(savedSolicitante);
		assertNotNull(savedSolicitante.getId());
		assertEquals("Teste", savedSolicitante.getNome()); 
	}
	
	private void deleteAll() {
		repoAgenda.deleteAll();
		repoVaga.deleteAll();
		repo.deleteAll();
	}
	
	@Test
	void save() {
		deleteAll();
		Solicitante savedSolicitante = service.save(new Solicitante(null, "Teste"));

		assertNotNull(savedSolicitante);
		assertNotNull(savedSolicitante.getId());
		assertEquals("Teste", savedSolicitante.getNome()); 
	}
	
	@Test
	void findById() {
		deleteAll();
		Solicitante savedSolicitante = service.save(new Solicitante(null, "Teste"));
		Optional<Solicitante> saved = service.findById(savedSolicitante.getId());

		assertNotNull(saved.get());
		assertNotNull(saved.get().getId());
		assertEquals("Teste", saved.get().getNome()); 
	}
	
	@Test
	void findByNomeNotFound() {
		deleteAll();
		Optional<Solicitante> saved = service.findByNome("Test9");

		assertTrue(saved.isEmpty());
	}
	
	@Test
	void findByNome() {
		deleteAll();
		Solicitante savedSolicitante = service.save(new Solicitante(null, "Teste"));
		Optional<Solicitante> saved = service.findByNome(savedSolicitante.getNome());

		assertNotNull(saved.get());
		assertNotNull(saved.get().getId());
		assertEquals("Teste", saved.get().getNome()); 
	}

	@Test
	void saveNull() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Solicitante());
		});

		assertEquals(BusinessException.class, throwable.getClass());
	}
		
	@Test
	void findAll() throws BusinessException {	
		repo.deleteAll();
		service.insert(new Solicitante(null, "Teste 123"));
		service.insert(new Solicitante(null, "Teste 456"));
		
		List<Solicitante> solicitante = (List<Solicitante>) service.getAll();

		assertNotNull(solicitante);
		assertEquals(2, solicitante.size()); 
	}
			
	@Test
	void deleteByIdError() throws BusinessException {
		repoVaga.save(new Vaga(null, LocalDate.now(), LocalDate.now().plusDays(15), 4));
		Solicitante sol = service.insert(new Solicitante(null, "Teste999"));
		repoAgenda.save(new Agendamento(null, LocalDate.now(), MotivosDeAgendamento.MANUTENCAO, sol));
		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.deleteById(sol.getId());
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgSolicitanteNaoPodeSerExcluidoJaPossuiAgenda, throwable.getMessage());
	}
	
	@Test
	void deleteById() throws BusinessException {		
		Solicitante sol = service.insert(new Solicitante(null, "Teste 123"));
		assertDoesNotThrow(() -> {
			service.deleteById(sol.getId());
		});
	}	
	
}
