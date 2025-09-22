package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Professor extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 a 100 letras")
    public String nome;

    @NotNull
    @Email(message = "O email deve ser válido")
    public String email;

    public Professor() {
    }

    public Professor(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
}
