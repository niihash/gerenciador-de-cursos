package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Curso extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 a 100 letras")
    public String nome;

    @NotNull
    @Size(min = 2, max = 200, message = "A descrição deve ter entre 2 a 200 letras")
    public String descricao;

    @Min(value = 2400, message = "A quantidade mínima de carga horária é 2400 horas")
    @Max(value = 7200, message = "A quantidade máxima de carga horária é 7200 horas")
    public int cargaHoraria;

    @OneToOne
    @JoinColumn(name = "coordenador_id", unique = true)
    public Professor professor;

    @ManyToMany(mappedBy = "cursos")
    public Set<Aluno> alunos = new HashSet<>();

    public Curso() {
    }

    public Curso(String nome, String descricao, int cargaHoraria, Professor professor) {
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.professor = professor;
    }
}
