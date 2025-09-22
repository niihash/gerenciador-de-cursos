package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Aluno extends PanacheEntity {

    public enum Sexo {
        MASCULINO,FEMININO
    }

    @NotNull
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 a 100 letras")
    public String nome;

    @NotNull
    @Email(message = "O email deve ser válido")
    public String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    public Sexo sexo;

    @ManyToMany
    @JoinTable(
            name = "aluno_curso",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    public Set<Curso> cursos = new HashSet<>();

    public Aluno() {
    }

    public Aluno(String nome, String email, Sexo sexo) {
        this.nome = nome;
        this.email = email;
        this.sexo = sexo;
    }
}
