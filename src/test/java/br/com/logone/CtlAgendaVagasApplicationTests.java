package br.com.logone;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CtlAgendaVagasApplicationTests {

	@Test
	void main() {
		assertThrows(IllegalArgumentException.class, () -> CtlAgendaVagasApplication.main(new String[0]));
	}
}
