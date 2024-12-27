package br.com.logone.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import br.com.logone.enums.MotivosDeAgendamento;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.AgendamentoRepository;
import br.com.logone.repository.SolicitanteRepository;
import br.com.logone.service.AgendamentoService;
import br.com.logone.service.SolicitanteService;
import jakarta.faces.context.FacesContext;
import nl.altindag.console.ConsoleCaptor;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:messages.properties")
class AgendamentoMBTest {
	
	@Value("${validation.invalid.solicitante.exists}")
	private String msgSolicitanteExits;

	@Mock
	AgendamentoService service;
	
	@Mock
	SolicitanteService serviceSolicitante;
	
	@Mock
	Agendamento agendamento;
	
	@Autowired
	AgendamentoRepository repo;
	
	@Autowired
	SolicitanteRepository repoSolicitante;
	
	AgendamentoMB agendamentoMB = new AgendamentoMB();
	
	ConsoleCaptor consoleCaptor = new ConsoleCaptor();
	
	@BeforeEach
	void setUp() {
		FacesContext context = mock(FacesContext.class);
		agendamentoMB.setContext(context);
		agendamentoMB.setService(service);
		agendamentoMB.setServiceSolicitante(serviceSolicitante);
	}
	
	@AfterEach
	void tearDown() {
		consoleCaptor.close();
	}
	
	@Test
	void salvar() throws BusinessException {
		given(service.insert(Mockito.any(Agendamento.class))).willReturn(new Agendamento(null, LocalDate.now(), MotivosDeAgendamento.MANUTENCAO, new Solicitante(1L, "Teste 123")));
		
		assertDoesNotThrow(() -> {
			agendamentoMB.salvar();
		});
	}
	
	@Test
	void getterSetters() {
		agendamentoMB.setDataInicio(LocalDate.now().plusDays(30));
		agendamentoMB.setDataFim(LocalDate.now().plusDays(60));
		List<MotivosDeAgendamento> motivos = agendamentoMB.getMotivos();
		List<Agendamento> agendas = agendamentoMB.getAgendamentos();
		Agendamento agenda = agendamentoMB.getAgendamento();
		
		assertDoesNotThrow(() -> {
			agendamentoMB.salvar();
		});
		
		assertNotNull(agenda);
		assertTrue(motivos.size() > 0);
		assertTrue(agendas.size() >= 0);
		assertEquals(LocalDate.now().plusDays(30), agendamentoMB.getDataInicio());
		assertEquals(LocalDate.now().plusDays(60), agendamentoMB.getDataFim());
	}
	
	@Test
	void salvarError() throws BusinessException {
		doThrow(new BusinessException(msgSolicitanteExits)).when(service).insert(Mockito.any(Agendamento.class));
		agendamentoMB.salvar();
		
		assertTrue(consoleCaptor.getStandardOutput().toString().contains(msgSolicitanteExits));
	}
	
	@Test
	void excluir() {
		Solicitante sol = repoSolicitante.save(new Solicitante(null, "Teste 1238"));
		Agendamento ag = repo.save(new Agendamento(null, LocalDate.now(), MotivosDeAgendamento.MANUTENCAO, sol));
	    
	    assertDoesNotThrow(() -> {
	    	agendamentoMB.excluir(ag);
		});
	}
	
	
	@Test
	void getSolicitantes() {		
		BDDMockito.given(serviceSolicitante.getAll()).willReturn(List.of(new Solicitante()));
		List<Solicitante> sol = agendamentoMB.getSolicitantes();
		
		assertEquals(1, sol.size());
	}
}
