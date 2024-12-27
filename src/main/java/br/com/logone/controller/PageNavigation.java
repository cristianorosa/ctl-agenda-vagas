package br.com.logone.controller;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@SessionScoped
@Named(value = "pageNavigation")
@Getter
@Setter
public class PageNavigation implements Serializable {

	private static final long serialVersionUID = -6257415832805571124L;

	private String currentPage = "index";

	public String navigate(String pagina) {
		this.currentPage = pagina;
		return pagina;
	}
}
