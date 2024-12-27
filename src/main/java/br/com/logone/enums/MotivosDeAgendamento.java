package br.com.logone.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum MotivosDeAgendamento {
	ENTREGA(1, "ENTREGA"),
	CARGA(2, "CARGA"),
	MANUTENCAO(3, "MANUTENÇÃO");

	private final int codigo;
	private final String descricao;

	private MotivosDeAgendamento(int i, String descricao) {
		this.codigo = i;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static List<MotivosDeAgendamento> getMotivosDeAgndamento() {
		List<MotivosDeAgendamento> motivos = new ArrayList<>();
		Collections.addAll(motivos, values());
		return motivos;
	}
}
