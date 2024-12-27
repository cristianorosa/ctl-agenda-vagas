package br.com.logone.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SolicitanteTest {
	
	@Test
	void getterSetters() {
		Solicitante solicitante = new Solicitante();
		solicitante.setId(1L);
		solicitante.setNome("Teste");
		boolean test1 = solicitante.equals(solicitante);
		boolean test2 = solicitante.equals(null);
		boolean test3 = solicitante.equals(new Solicitante(2L, ""));
		
		assertTrue(test1);
		assertFalse(test2);
		assertFalse(test3);
		assertEquals(0, solicitante.compareTo(solicitante));		
		assertEquals(80699859, solicitante.hashCode());
		assertEquals("Teste", solicitante.toString());
		assertEquals(1L, solicitante.getId());
		assertEquals("Teste", solicitante.getNome());
	}

}
