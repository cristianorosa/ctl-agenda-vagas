package br.com.logone.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.logone.entity.Vaga;

@SpringBootTest
@ActiveProfiles("test")
class VagaRepositoryTest {
	
	static final LocalDate DATE_INICIO = LocalDate.now();
	static final LocalDate DATE_FIM = LocalDate.now();
	static final Integer QTD_VAGAS = 35;

	private Vaga vaga;
		
	@Autowired
	VagaRepository repo;
	
	@BeforeEach
	public void setUp() {
		this.vaga = repo.save(new Vaga(null, DATE_INICIO, DATE_FIM, QTD_VAGAS));
	} 
	
	@AfterEach 
    void tearDown() {
		repo.deleteAll();
    }
	
	@Test
	void saveVaga() {
		Vaga newVaga = repo.save(new Vaga(null, DATE_INICIO, DATE_FIM, QTD_VAGAS));		
		
		assertNotNull(newVaga);
		assertNotNull(newVaga.getId());
		assertEquals(DATE_INICIO, newVaga.getDataInicio());
		assertEquals(DATE_FIM, newVaga.getDataFim());
		assertEquals(QTD_VAGAS, newVaga.getQuantidade());
	}
	
	@Test
	void findByVagaId() {
		Optional<Vaga> wasFound = repo.findById(vaga.getId());	
		
		assertNotNull(wasFound.get());
	}
}
