package br.com.logone.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VagaTest {
	
	@Test
	void getterSetters() {
		Vaga vaga = new Vaga();
		vaga.setId(1L);
		vaga.setDataInicio(LocalDate.now());
		vaga.setDataFim(LocalDate.now().plusDays(25));
		vaga.setQuantidade(25);
		
		assertEquals(1L, vaga.getId());
		assertEquals(LocalDate.now(), vaga.getDataInicio());
		assertEquals(LocalDate.now().plusDays(25), vaga.getDataFim());
		assertEquals(25, vaga.getQuantidade());
	}

}
