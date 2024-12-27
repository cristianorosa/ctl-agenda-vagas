package br.com.logone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import br.com.logone.entity.Solicitante;
import br.com.logone.exception.BusinessException;
import br.com.logone.service.SolicitanteService;
import br.com.logone.service.impl.SolicitanteServiceImpl;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named(value = "solicitanteMB")
@RequestScoped
@PropertySource("classpath:messages.properties")
public class SolicitanteMB {
	
	@Value("${cadastro.registro.inserido.sucesso}")
	private String sucessInsert;

	@Value("${cadastro.registro.excluido.sucesso}")
	private String sucessDelete;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolicitanteMB.class);
	
	private Solicitante solicitante = new Solicitante();
	
	private SolicitanteService service;	
	
	private FacesContext context;
	
	public SolicitanteMB() {
		this.service = new SolicitanteServiceImpl();
		this.context = FacesContext.getCurrentInstance();
	}
	
	public void setContext(FacesContext context) {
		this.context = context;
	}
	
	public void setService(SolicitanteService service) {
		this.service = service;
	}
	
	public List<Solicitante> getSolicitantes() {
		return service.getAll();		
	}
	
	public Solicitante getSolicitante() {
		return solicitante;
	}
	
	public void salvar() {
		solicitante.setId(null);
		try {
			service.insert(solicitante);
			solicitante = new Solicitante();
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", sucessInsert));
		} catch (BusinessException e) {
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			LOGGER.error(e.getMessage());
		}
	}
	
	private void feedBackUser(FacesMessage msg) {
		if (context != null) {
			context.addMessage(null, msg);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void excluir(Solicitante solicitante) {
		try {
			service.deleteById(solicitante.getId());
			this.solicitante = new Solicitante();
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", sucessDelete));
		} catch (BusinessException e) {
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			LOGGER.error(e.getMessage());
		}
	}
}
