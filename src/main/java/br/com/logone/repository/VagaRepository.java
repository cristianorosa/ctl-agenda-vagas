package br.com.logone.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.logone.entity.Vaga;

public interface VagaRepository extends CrudRepository<Vaga, Long> {
	Optional<Vaga> findById(long id);
	List<Vaga> findByDataInicioAndDataFim(LocalDate dataInicio, LocalDate dataFim);
	List<Vaga> findAll();
	
	@Query( value = "SELECT * FROM TB_VAGAS WHERE DT_INICIO <= ?1 AND DT_FIM >= ?1"
		      , countQuery = "SELECT COUNT(*) FROM TB_VAGAS WHERE DT_INICIO <= ? AND DT_FIM >= ?"
		      , nativeQuery = true)
	List<Vaga> findVagasPorData(LocalDate data);
	
	List<Vaga> findAllByDataInicioLessThanEqualAndDataFimGreaterThanEqual(LocalDate dataInicio, LocalDate dataFim);
	List<Vaga> findAllByDataInicioGreaterThanEqualAndDataFimLessThanEqual(LocalDate dataInicio, LocalDate dataFim);
	List<Vaga> findAllByDataInicioLessThanEqualAndDataFimLessThanEqual(LocalDate dataInicio, LocalDate dataFim);
}
