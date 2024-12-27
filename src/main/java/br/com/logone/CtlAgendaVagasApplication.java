package br.com.logone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.logone"}) 
public class CtlAgendaVagasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CtlAgendaVagasApplication.class, args);
	}
}
