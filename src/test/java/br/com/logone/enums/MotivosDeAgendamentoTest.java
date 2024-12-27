package br.com.logone.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MotivosDeAgendamentoTest {

	@Test
	void gettersSetterAndList() {
		String motivo = MotivosDeAgendamento.ENTREGA.getDescricao();
		Integer codigo = MotivosDeAgendamento.ENTREGA.getCodigo();
		
		List<MotivosDeAgendamento> lista = MotivosDeAgendamento.getMotivosDeAgndamento();
		
		assertEquals(1, codigo);
		assertEquals("ENTREGA", motivo);
		assertEquals(3, lista.size());
	}
	
}
