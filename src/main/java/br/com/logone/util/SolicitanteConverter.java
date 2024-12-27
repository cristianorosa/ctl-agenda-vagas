package br.com.logone.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import br.com.logone.entity.Solicitante;
import br.com.logone.service.SolicitanteService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
@FacesConverter(value = "solicitanteConverter", managed = true)
@PropertySource("classpath:messages.properties")
public class SolicitanteConverter implements Converter<Object> {
	
		
	@Value("${converter.exception}")
	private String converterException;
	
	@Value("${converter.exception.msg}")
	private String converterExceptionMsg;
	

    @Inject
    private SolicitanteService solicitanteService;

    @Override
    public Solicitante getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equalsIgnoreCase("&nbsp;") && value.trim().length() > 0 ) {
            try {
            	Optional<Solicitante> ret = solicitanteService.findById(Long.parseLong(value));            	
            	if (ret.isPresent()) {
            		return ret.get();
            	}
            	return null;
            }
            catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, converterException, converterExceptionMsg));
            }
        }
        else {
            return null;
        }
    }

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Solicitante sol) {
			return String.valueOf(sol.getId());
		}		
        return null;
	}
}