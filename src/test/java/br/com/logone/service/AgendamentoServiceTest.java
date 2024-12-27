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
import org.springframework.test.context.ActiveProfiles;

import br.com.logone.dto.AgendamentoDTO;
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
class AgendamentoServiceTest {
	
	@Value("${validation.invalid.solicitante.nao.pode.ser.excluido}")
	private String msgSolicitanteNaoPodeSerExcluidoJaPossuiAgenda;
	
	@Value("${validation.invalid.agendamento.data.retroativa}")
	private String msgDataRetroativa;
	
	@Value("${validation.invalid.vaga.indisponivel}")
	private String msgVagaIndisponivelParaData;
	
	@Value("${validation.invalid.vaga.indisponivel.solicitante}")
	private String msgVagaIndisponivelParaSolicitante;
	
	@Value("${validation.invalid.data.consulta}")
	private String msgAmbasDatasPreechidas;
	
	static final LocalDate HOJE = LocalDate.now();

	@Autowired
	SolicitanteRepository repoSolicitante;

	@Autowired
	VagaRepository repoVaga;
	
	@Autowired
	AgendamentoRepository repo;
	
	@Autowired
	AgendamentoService service;
	
	private Solicitante solicitante;
		
	@BeforeEach
	public void setUp() {
		repoVaga.save(new Vaga(null, HOJE, HOJE.plusDays(15), 20));
		solicitante = repoSolicitante.save(new Solicitante(null, "Teste123"));
	}
	
	@AfterEach
    void tearDown() {
		repo.deleteAll();
		repoVaga.deleteAll();
		repoSolicitante.deleteAll();
    }
	
	
	@Test
	void insert() throws BusinessException {
		Agendamento agenda = service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));

		assertNotNull(agenda);
		assertNotNull(agenda.getId());
		assertEquals("Teste123", agenda.getSolicitante().getNome()); 
	}
	
	@Test
	void insertDataRetroativa() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Agendamento(null, HOJE.minusDays(5), MotivosDeAgendamento.MANUTENCAO, solicitante));
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgDataRetroativa, throwable.getMessage());
	}
	
	@Test
	void insertSemVagaDisponivel() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Agendamento(null, HOJE.plusDays(100), MotivosDeAgendamento.MANUTENCAO, solicitante));			
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgVagaIndisponivelParaData, throwable.getMessage());
	}
	
	@Test
	void insertVagaIndisponivelParaSolicitante() {
		repoVaga.save(new Vaga(null, HOJE.plusDays(150), HOJE.plusDays(170), 3));
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.insert(new Agendamento(null, HOJE.plusDays(170), MotivosDeAgendamento.MANUTENCAO, solicitante));			
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgVagaIndisponivelParaSolicitante, throwable.getMessage());
	}
	
	@Test
	void deleteById() throws BusinessException {		
		Agendamento agenda = service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
		
		assertDoesNotThrow(() -> {
			service.deleteById(agenda.getId());
		});
	}	
	
	@Test
	void findAll() throws BusinessException {		
		repo.deleteAll();
		service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
		service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
		
		List<Agendamento> agenda  = service.getAll();
		
		assertEquals(2, agenda.size()); 
	}	
	
	@Test
	void consultaPorDataSolicitante() throws BusinessException {		
		repo.deleteAll();
		service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
		service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
				
		List<AgendamentoDTO> agenda  = service.consultar(solicitante.getNome(), HOJE, HOJE);
		
		assertEquals(2, agenda.get(0).getTotalAgendamentos()); 
	}	
	
	@Test
	void consultaPorData() throws BusinessException {		
		repo.deleteAll();
		service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
		service.insert(new Agendamento(null, HOJE, MotivosDeAgendamento.MANUTENCAO, solicitante));
				
		List<AgendamentoDTO> agenda  = service.consultar(null, HOJE, HOJE);
		
		assertEquals(2, agenda.get(0).getTotalAgendamentos()); 
	}	
	
	@Test
	void consultaSemDataInicial() {		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.consultar(null, HOJE, null);		
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgAmbasDatasPreechidas, throwable.getMessage());
	}	
	
	@Test
	void consultaSemDataFinal() {		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			service.consultar(null, null, HOJE);		
		});

		assertEquals(BusinessException.class, throwable.getClass());
		assertEquals(msgAmbasDatasPreechidas, throwable.getMessage());
	}	
}
