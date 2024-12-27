package br.com.logone.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import br.com.logone.service.VagaService;
import jakarta.faces.context.FacesContext;
import nl.altindag.console.ConsoleCaptor;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:messages.properties")
class VagaMBTest {
	
	@Value("${validation.invalid.vaga.update.conflita.agenda}")
	private String msgUpdateConflitaVagaAgendada;
	
	@Value("${validation.invalid.vaga.data.intervalo}")
	private String msgDataFimMenorQueDataInicio;

	@Mock
	VagaService service;
	
	@Mock
	Vaga vaga;
	
	@Autowired
	VagaRepository repo;
	
	@Autowired
	SolicitanteRepository repoSolicitante;

	@Autowired
	AgendamentoRepository repoAgendamento;
	
	VagaMB vagaMB = new VagaMB();
	
	ConsoleCaptor consoleCaptor = new ConsoleCaptor();
	
	@BeforeEach
	void setUp() {
		FacesContext context = mock(FacesContext.class);
		vagaMB.setContext(context);
	    vagaMB.setService(service);
	}
	
	@AfterEach
	void tearDown() {
		consoleCaptor.close();
	}
	
	@Test
	void salvar() throws BusinessException {
		given(service.insert(Mockito.any(Vaga.class))).willReturn(new Vaga(1L, LocalDate.now().plusDays(23), LocalDate.now().plusDays(64), 32));
		given(vaga.getDataInicio()).willReturn(LocalDate.now().plusDays(23));
		
		assertDoesNotThrow(() -> {
			vagaMB.salvar();
		});
	}
	
	@Test
	void salvarError() throws BusinessException {
		doThrow(new BusinessException(msgDataFimMenorQueDataInicio)).when(service).insert(Mockito.any(Vaga.class));
		vagaMB.salvar();
		
		assertTrue(consoleCaptor.getStandardOutput().toString().contains(msgDataFimMenorQueDataInicio));
	}
	
	@Test
	void excluir() {
	    Vaga vg = repo.save(new Vaga(null, LocalDate.now().plusDays(23), LocalDate.now().plusDays(64), 32));
	    
	    assertDoesNotThrow(() -> {
			vagaMB.excluir(vg);
		});
	}
	
	@Test
	void excluirError() throws BusinessException {
	    doThrow(new BusinessException(msgUpdateConflitaVagaAgendada)).when(service).deleteById(Mockito.anyLong());

	    Solicitante sol = repoSolicitante.save(new Solicitante(null, "Test 123"));
	    Vaga vg = repo.save(new Vaga(null, LocalDate.now().plusDays(23), LocalDate.now().plusDays(64), 32));
	    repoAgendamento.save(new Agendamento(null, LocalDate.now().plusDays(25), MotivosDeAgendamento.MANUTENCAO, sol));	
	    
	    vagaMB.excluir(vg);

	    assertTrue(consoleCaptor.getStandardOutput().toString().contains(msgUpdateConflitaVagaAgendada));
	}
	
	@Test
	void getVagas() {		
		BDDMockito.given(service.getAll()).willReturn(List.of(new Vaga()));
		List<Vaga> vagas = vagaMB.getVagas();
		
		assertEquals(1, vagas.size());
	}
	
	@Test
	void getVaga() {
		Vaga vag = vagaMB.getVaga();
		assertEquals(null, vag.getId());
	}
}
