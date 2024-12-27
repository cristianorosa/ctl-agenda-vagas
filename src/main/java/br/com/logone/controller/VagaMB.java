package br.com.logone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import br.com.logone.entity.Vaga;
import br.com.logone.exception.BusinessException;
import br.com.logone.service.VagaService;
import br.com.logone.service.impl.VagaServiceImpl;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named(value = "vagaMB")
@RequestScoped
@PropertySource("classpath:messages.properties")
public class VagaMB {
	@Value("${cadastro.registro.inserido.sucesso}")
	private String sucessInsert;

	@Value("${cadastro.registro.excluido.sucesso}")
	private String sucessDelete;

	private static final Logger LOGGER = LoggerFactory.getLogger(VagaMB.class);

	private Vaga vaga = new Vaga();

	private VagaService service;

	private FacesContext context;

	public VagaMB() {
		this.service = new VagaServiceImpl();
		this.context = FacesContext.getCurrentInstance();
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}
	
	public void setService(VagaService service) {
		this.service = service;
	}

	public List<Vaga> getVagas() {
		return service.getAll();
	}

	public Vaga getVaga() {
		return vaga;
	}
	
	public void salvar() {
		try {
			vaga.setId(null);
			service.insert(vaga);
			vaga = new Vaga();
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", sucessInsert));
		} catch (BusinessException e) {
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			LOGGER.error(e.getMessage());
		}
	}

	public void excluir(Vaga vaga) {
		try {
			service.deleteById(vaga.getId());
			this.vaga = new Vaga();
			feedBackUser(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", sucessDelete));
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
}
