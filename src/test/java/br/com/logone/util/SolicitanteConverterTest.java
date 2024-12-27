package br.com.logone.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import br.com.logone.entity.Solicitante;
import br.com.logone.repository.SolicitanteRepository;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.FacesListener;
import jakarta.faces.render.Renderer;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:messages.properties")
class SolicitanteConverterTest {
	
	@Value("${converter.exception}")
	private String converterException;
	
	@Autowired
	SolicitanteRepository repo;
	
	@Autowired
	SolicitanteConverter converter;
	
	@Autowired
	FacesContext context;
	
	Solicitante solic;
	
	@BeforeEach
	void setUp() {
		solic = repo.save(new Solicitante(null, "Teste 789"));
	}
	
	@AfterEach
	void tearDown() {
		repo.deleteAll();
	}
	
	@Test
	void getAsObject() {
		Solicitante solicitante = converter.getAsObject(context, getComponent(), solic.getId().toString());
		
		assertNotNull(solicitante);
	}	
	
	@Test
	void getAsObjectNull() {
		Solicitante solicitante = converter.getAsObject(context, getComponent(), null);
		
		assertNull(solicitante);
	}	
	
	
	@Test
	void getAsObjectIsNoPresent() {
		Solicitante solicitante = converter.getAsObject(context, getComponent(), "9");
		
		assertNull(solicitante);
	}	
	
	@Test
	void getAsObjectException() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			converter.getAsObject(context, getComponent(), "A");
		});

		assertEquals(ConverterException.class, throwable.getClass());
		assertEquals(converterException, throwable.getMessage());
	}	
	
	@Test
	void getAsString() {
		String solicitante = converter.getAsString(context, getComponent(), solic);
		
		assertNotNull(solicitante);
	}	
	
	@Test
	void getAsStringNull() {
		String solicitante = converter.getAsString(context, getComponent(), null);
		
		assertNull(solicitante);
	}	
	
	private UIComponent getComponent() {
		return new UIComponent() {
			
			@Override
			public void setTransient(boolean newTransientValue) {
				// Auto-generated method stub
			}
			
			@Override
			public Object saveState(FacesContext context) {
				return null;
			}
			
			@Override
			public void restoreState(FacesContext context, Object state) {
				//  Auto-generated method stub
			}
			
			@Override
			public boolean isTransient() {
				return false;
			}
			
			@Override
			public void setRendererType(String rendererType) {
				//  Auto-generated method stub
			}
			
			@Override
			public void setRendered(boolean rendered) {
				//  Auto-generated method stub
			}
			
			@Override
			public void setParent(UIComponent parent) {
				//  Auto-generated method stub
			}
			
			@Override
			public void setId(String id) {
				//  Auto-generated method stub
			}
			
			@Override
			protected void removeFacesListener(FacesListener listener) {
				//  Auto-generated method stub
			}
			
			@Override
			public void queueEvent(FacesEvent event) {
				//  Auto-generated method stub
			}
			
			@Override
			public void processValidators(FacesContext context) {
				//  Auto-generated method stub
			}
			
			@Override
			public void processUpdates(FacesContext context) {
				//  Auto-generated method stub
			}
			
			@Override
			public Object processSaveState(FacesContext context) {
				return null;
			}
			
			@Override
			public void processRestoreState(FacesContext context, Object state) {
				//  Auto-generated method stub
			}
			
			@Override
			public void processDecodes(FacesContext context) {
				//  Auto-generated method stub
			}
			
			@Override
			public boolean isRendered() {
				return false;
			}
			
			@Override
			public boolean getRendersChildren() {
				return false;
			}
			
			@Override
			public String getRendererType() {
				return null;
			}
			
			@Override
			protected Renderer<?> getRenderer(FacesContext context) {
				return null;
			}
			
			@Override
			public UIComponent getParent() {
				return null;
			}
			
			@Override
			public String getId() {
				return null;
			}
			
			@Override
			public String getFamily() {
				return null;
			}
			
			@Override
			public Iterator<UIComponent> getFacetsAndChildren() {
				return null;
			}
			
			@Override
			public Map<String, UIComponent> getFacets() {
				return null;
			}
			
			@Override
			public UIComponent getFacet(String name) {
				return null;
			}
			
			@Override
			protected FacesListener[] getFacesListeners(@SuppressWarnings("rawtypes") Class clazz) {
				return null;
			}
			
			@Override
			protected FacesContext getFacesContext() {
				return null;
			}
			
			@Override
			public String getClientId(FacesContext context) {
				return null;
			}
			
			@Override
			public List<UIComponent> getChildren() {
				return null;
			}
			
			@Override
			public int getChildCount() {
				return 0;
			}
			
			@Override
			public Map<String, Object> getAttributes() {
				return null;
			}
			
			@Override
			public UIComponent findComponent(String expr) {
				return null;
			}
			
			@Override
			public void encodeEnd(FacesContext context) throws IOException {
				//  Auto-generated method stub
			}
			
			@Override
			public void encodeChildren(FacesContext context) throws IOException {
				//  Auto-generated method stub
			}
			
			@Override
			public void encodeBegin(FacesContext context) throws IOException {
				//  Auto-generated method stub
			}
			
			@Override
			public void decode(FacesContext context) {
				//  Auto-generated method stub
			}
			
			@Override
			public void broadcast(FacesEvent event) throws AbortProcessingException {
				//  Auto-generated method stub
			}
			
			@Override
			protected void addFacesListener(FacesListener listener) {
				//  Auto-generated method stub
			}
		};
	}

}
