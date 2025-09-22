package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Disciplina extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 a 100 letras")
    public String nome;

    @NotNull
    @Size(min = 2, max = 200, message = "A descrição deve ter entre 2 a 200 letras")
    public String descricao;

    @Min(value = 30, message = "A quantidade mínima de carga horária é 30 horas")
    @Max(value = 100, message = "A quantidade máxima de carga horária é 100 horas")
    public int cargaHoraria;

    @ManyToOne
    public Professor professor;

    public Disciplina() {
    }

    public Disciplina(String nome, String descricao, int cargaHoraria, Professor professor) {
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.professor = professor;
    }
}
