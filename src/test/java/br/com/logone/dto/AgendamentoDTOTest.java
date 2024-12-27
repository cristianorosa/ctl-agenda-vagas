package br.com.logone.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AgendamentoDTOTest {
	
	@Test
	void gettersSetters() {
		AgendamentoDTO dto = new AgendamentoDTO();
		dto.setNome("Teste");
		dto.setPercentual(25D);
		dto.setQtdVagas(25);
		dto.setTotalAgendamentos(4);
		
		assertEquals("Teste", dto.getNome());
		assertEquals(25D, dto.getPercentual());
		assertEquals(25, dto.getQtdVagas());
		assertEquals(4, dto.getTotalAgendamentos());
	}

}
