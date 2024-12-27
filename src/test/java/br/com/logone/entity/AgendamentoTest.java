package br.com.logone.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.logone.enums.MotivosDeAgendamento;

@SpringBootTest
@ActiveProfiles("test")
class AgendamentoTest {
	
	@Test
	void getterSetters() {
		Agendamento agenda = new Agendamento();
		Solicitante solicitante = new Solicitante(1L, "teste");
		agenda.setId(1L);
		agenda.setDataAgendamento(LocalDate.now());
		agenda.setMotivo(MotivosDeAgendamento.MANUTENCAO);
		agenda.setSolicitante(solicitante);
		
		assertEquals(1L, agenda.getId());
		assertEquals(LocalDate.now(), agenda.getDataAgendamento());
		assertEquals(MotivosDeAgendamento.MANUTENCAO, agenda.getMotivo());
		assertEquals(solicitante, agenda.getSolicitante());
	}
}
