package br.com.logone.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgendamentoDTO implements Serializable{

	private static final long serialVersionUID = -1753683735419699065L;
	private String nome;
	private Integer totalAgendamentos;
	private Integer qtdVagas;
	private Double percentual;

}