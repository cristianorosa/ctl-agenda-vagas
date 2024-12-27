package br.com.logone;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CtlAgendaVagasApplicationTests {
	
	@Test
	void contextLoads() { 
		// document why this method is empty
	}

	@Test
	void main() {
		assertThrows(IllegalArgumentException.class, () -> CtlAgendaVagasApplication.main(null));
	}
}
