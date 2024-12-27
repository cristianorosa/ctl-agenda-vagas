package br.com.logone.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

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

import br.com.logone.entity.Solicitante;
import br.com.logone.entity.Vaga;
import br.com.logone.exception.BusinessException;
import br.com.logone.repository.SolicitanteRepository;
import br.com.logone.service.SolicitanteService;
import jakarta.faces.context.FacesContext;
import nl.altindag.console.ConsoleCaptor;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:messages.properties")
class SolicitanteMBTest {
	
	@Value("${validation.invalid.solicitante.exists}")
	private String msgSolicitanteExits;

	@Mock
	SolicitanteService service;
	
	@Mock
	Vaga vaga;
	
	@Autowired
	SolicitanteRepository repo;
	
	SolicitanteMB solicitanteMB = new SolicitanteMB();
	
	ConsoleCaptor consoleCaptor = new ConsoleCaptor();
	
	@BeforeEach
	void setUp() {
		FacesContext context = mock(FacesContext.class);
		solicitanteMB.setContext(context);
		solicitanteMB.setService(service);
	}
	
	@AfterEach
	void tearDown() {
		consoleCaptor.close();
	}
	
	@Test
	void salvar() throws BusinessException {
		given(service.insert(Mockito.any(Solicitante.class))).willReturn(new Solicitante(1L, "Teste 123"));
		
		assertDoesNotThrow(() -> {
			solicitanteMB.salvar();
		});
	}
	
	@Test
	void salvarError() throws BusinessException {
		doThrow(new BusinessException(msgSolicitanteExits)).when(service).insert(Mockito.any(Solicitante.class));
		solicitanteMB.salvar();
		
		assertTrue(consoleCaptor.getStandardOutput().toString().contains(msgSolicitanteExits));
	}
	
	@Test
	void excluir() {
		Solicitante sol = repo.save(new Solicitante(null, "Teste 123"));
	    
	    assertDoesNotThrow(() -> {
	    	solicitanteMB.excluir(sol);
		});
	}
	
	@Test
	void excluirError() throws BusinessException {
	    doThrow(new BusinessException(msgSolicitanteExits)).when(service).deleteById(Mockito.anyLong());
	    Solicitante sol = repo.save(new Solicitante(null, "Test 1237"));
	    solicitanteMB.excluir(sol);

	    assertTrue(consoleCaptor.getStandardOutput().toString().contains(msgSolicitanteExits));
	}
	
	@Test
	void getSolicitantes() {		
		BDDMockito.given(service.getAll()).willReturn(List.of(new Solicitante()));
		List<Solicitante> sol = solicitanteMB.getSolicitantes();
		
		assertEquals(1, sol.size());
	}
	
	@Test
	void getVaga() {
		Solicitante sol = solicitanteMB.getSolicitante();
		assertEquals(null, sol.getId());
	}
	
	
}
