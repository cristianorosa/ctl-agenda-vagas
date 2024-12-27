package br.com.logone.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import br.com.logone.entity.Vaga;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.AgendamentoRepository;
import br.com.logone.repository.SolicitanteRepository;
import br.com.logone.repository.VagaRepository;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:messages.properties")
class VagaServiceTest {

	static final LocalDate DATE_INICIO = LocalDate.now();
	static final LocalDate DATE_FIM = LocalDate.now();
	static final Integer QTD_VAGAS = 35;
	
	@Value("${validation.invalid.vaga.data.retroativa}")
	private String msgDataRetroativa;
	
	@Value("${validation.invalid.vaga.data.intervalo}")
	private String msgDataFimMenorQueDataInicio;
	
	@Value("${validation.invalid.vaga.update.conflita.agenda}")
	private String msgUpdateConflitaVagaAgendada;

	private Vaga vaga;

	@Autowired
	VagaRepository repo;
	
	@Autowired
	SolicitanteRepository repoSolicitante;

	@Autowired
	VagaService service;
	
	@Autowired
	AgendamentoRepository repoAgendamento;

	@BeforeEach
	public void setUp() {
		vaga = repo.save(getNewVaga());
	}

	@AfterEach
	void tearDown() {
		repo.deleteAll();
	}

	@Test
	void save() throws BusinessException {
		Vaga savedVaga = service.insert(new Vaga(null, DATE_INICIO.plusDays(23), DATE_INICIO.plusDays(64), QTD_VAGAS));

		assertNotNull(savedVaga);
		assertNotNull(savedVaga.getId());
		assertEquals(DATE_INICIO, vaga.getDataInicio()); 
		assertEquals(DATE_FIM, vaga.getDataFim());
		assertEquals(QTD_VAGAS, vaga.getQuantidade());
	}

	@Test
	void saveNull() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Vaga());
		});

		assertEquals(DataIntegrityViolationException.class, throwable.getClass());
	}

	@Test
	void saveRetroactiveDataInicio() {		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Vaga(null, LocalDate.now().minusDays(23), LocalDate.now().minusDays(64), QTD_VAGAS));
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgDataRetroativa, throwable.getMessage());

	}
	
	@Test
	void saveRetroactiveDataFim() {		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Vaga(null, LocalDate.now(), LocalDate.now().minusDays(64), QTD_VAGAS));
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgDataRetroativa, throwable.getMessage());

	}
	
	@Test
	void findAll() throws BusinessException {	
		repo.deleteAll();
		service.insert(new Vaga(null, DATE_INICIO.plusDays(23), DATE_INICIO.plusDays(64), QTD_VAGAS));
		service.insert(new Vaga(null, DATE_INICIO.plusDays(23), DATE_INICIO.plusDays(64), QTD_VAGAS));
		
		List<Vaga> vagas = service.getAll();

		assertNotNull(vagas);
		assertEquals(2, vagas.size()); 
	}
	
	@Test
	void saveDataFimLessThanDataInicio() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Vaga(null, DATE_INICIO.plusDays(23), DATE_FIM.plusDays(3), QTD_VAGAS));
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgDataFimMenorQueDataInicio, throwable.getMessage());

	}
	
	@Test
	void deleteById() {
		repoAgendamento.deleteAll();
		assertDoesNotThrow(() -> {
			service.deleteById(vaga.getId());
		});
	}

	private Vaga getNewVaga() {
		return new Vaga(null, DATE_INICIO, DATE_FIM, QTD_VAGAS);
	}
}
