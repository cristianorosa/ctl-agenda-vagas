package br.com.logone.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="TB_SOLICITANTE")
public class Solicitante implements Serializable, Comparable<Solicitante>{

	private static final long serialVersionUID = -8552056643136996024L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="DS_NOME", unique=true, nullable = false)
    private String nome;

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Solicitante solicitante = (Solicitante) o;
        return id == solicitante.id && Objects.equals(nome, solicitante.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(Solicitante o) {
    	if (this.nome != null && o != null && o.getNome() != null) {
    		return nome.compareTo(o.nome);
    	}
    	return -1;     
    }
}
