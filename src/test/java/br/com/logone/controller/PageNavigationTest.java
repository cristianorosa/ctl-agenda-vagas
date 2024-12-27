package br.com.logone.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PageNavigationTest {

	@Test
	void getterSetter() {
		PageNavigation pn = new PageNavigation();
		pn.setCurrentPage("5");
		
		assertEquals("5", pn.getCurrentPage());
	}
	
	@Test
	void navigate() {
		PageNavigation pn = new PageNavigation();
		pn.setCurrentPage("5");
		
		assertEquals("5", pn.getCurrentPage());
		assertEquals("home", pn.navigate("home"));
	}
}
