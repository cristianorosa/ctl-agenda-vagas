package br.com.logone.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@Entity(name="TB_VAGAS")
public class Vaga implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="DT_INICIO", nullable = false)
    private LocalDate  dataInicio;
	
	@Column(name="DT_FIM", nullable = false)
    private LocalDate dataFim;
	
	@Column(name="QT_VAGAS", nullable = false)
	private Integer quantidade;
}
