package br.com.logone.entity;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.logone.enums.MotivosDeAgendamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="TB_AGENDAMENTO")
public class Agendamento implements Serializable{

	private static final long serialVersionUID = -8584959871748880214L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="DT_AGENDAMENTO")
	@Temporal(TemporalType.DATE)
    private LocalDate dataAgendamento;
	
	@Column(name = "TP_MOTIVO", columnDefinition = "NUMBER(2)")
	@Enumerated(EnumType.ORDINAL)
	private MotivosDeAgendamento motivo; 
	
	@ManyToOne
	@JoinColumn(name = "CD_SOLICITANTE", columnDefinition = "NUMBER(9)")
	private Solicitante solicitante;
	
}
