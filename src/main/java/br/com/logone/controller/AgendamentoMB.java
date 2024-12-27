package br.com.logone.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import br.com.logone.dto.AgendamentoDTO;
import br.com.logone.entity.Agendamento;
import br.com.logone.entity.Solicitante;
import br.com.logone.enums.MotivosDeAgendamento;
import br.com.logone.exception.BusinessException;
import br.com.logone.service.AgendamentoService;
import br.com.logone.service.SolicitanteService;
import br.com.logone.service.impl.AgendamentoServiceImpl;
import br.com.logone.service.impl.SolicitanteServiceImpl;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named(value = "agendamentoMB")
@RequestScoped
@PropertySource("classpath:messages.properties")
public class AgendamentoMB {
	@Value("${cadastro.registro.inserido.sucesso}")
	private String sucessInsert;

	@Value("${cadastro.registro.excluido.sucesso}")
	private String sucessDelete;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgendamentoMB.class);
	
	private Agendamento agendamento = new Agendamento();
	private Solicitante solicitante = new Solicitante();
	private List<Solicitante> solicitantes; 
	
	// campos para a busca
	private Solicitante solicitanteConsulta = new Solicitante();
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private List<AgendamentoDTO> resultadoBusca = new ArrayList<>();
	
	private AgendamentoService service;
	
	private SolicitanteService serviceSolicitante;
	
	private FacesContext context;
	
	public AgendamentoMB() {
		this.service = new AgendamentoServiceImpl();
		this.serviceSolicitante = new SolicitanteServiceImpl();
		this.context = FacesContext.getCurrentInstance();
	}
	
	public void setContext(FacesContext context) {
		this.context = context;
	}
	
	public void setService(AgendamentoService service) {
		this.service = service;
	}
	
	public void setServiceSolicitante(SolicitanteService service) {
		this.serviceSolicitante = service;
	}
	
	public List<Solicitante> getSolicitantes() {
		solicitantes = serviceSolicitante.getAll();
		return solicitantes;
	}
	
	public List<Agendamento> getAgendamentos() {
		return service.getAll();		
	}
	
	public List<MotivosDeAgendamento> getMotivos() {
		return MotivosDeAgendamento.getMotivosDeAgndamento();
	}
	
	public void consultar() {
		String nome = solicitanteConsulta != null ? solicitanteConsulta.getNome() : null;
		try {
			resultadoBusca = service.consultar(nome, dataInicio, dataFim);
		} catch (BusinessException e) {
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			LOGGER.error(e.getMessage());
		}
	}
	
	public void salvar() {
		agendamento.setId(null);
		try {
			service.insert(agendamento);
			agendamento = new Agendamento();
			
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", sucessInsert));
		} catch (BusinessException e) {
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			LOGGER.error(e.getMessage());
		}
	}
	
	public void excluir(Agendamento agendamento) {
		service.deleteById(agendamento.getId());
		this.agendamento = new Agendamento();
		feedBackUser(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", sucessDelete));
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public Solicitante getSolicitante() {
		return solicitante;
	}

	public Solicitante getSolicitanteConsulta() {
		return solicitanteConsulta;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public List<AgendamentoDTO> getResultadoBusca() {
		return resultadoBusca;
	}

	public void setResultadoBusca(List<AgendamentoDTO> resultadoBusca) {
		this.resultadoBusca = resultadoBusca;
	}

	public void setSolicitantes(List<Solicitante> solicitantes) {
		this.solicitantes = solicitantes;
	}
	
	private void feedBackUser(FacesMessage msg) {
		if (context != null) {
			context.addMessage(null, msg);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void setSolicitanteConsulta(Solicitante solicitanteConsulta) {
		this.solicitanteConsulta = solicitanteConsulta;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}
}
