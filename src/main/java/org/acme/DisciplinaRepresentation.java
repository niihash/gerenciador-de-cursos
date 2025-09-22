package org.acme;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DisciplinaRepresentation {

    public long id;
    public String nome;
    public String descricao;
    public int cargaHoraria;
    public Map<String, String> _links;

    public Professor professor;

    public DisciplinaRepresentation() {
    }

    public static DisciplinaRepresentation from(Disciplina disciplina, UriInfo uriInfo) {
        DisciplinaRepresentation rep = new DisciplinaRepresentation();
        rep.id = disciplina.id;
        rep.nome = disciplina.nome;
        rep.descricao = disciplina.descricao;
        rep.cargaHoraria = disciplina.cargaHoraria;

        rep.professor = disciplina.professor;

        rep._links = new HashMap<>();
        URI baseUri = uriInfo.getBaseUri();

        rep._links.put("self", baseUri + "disciplinas/" + disciplina.id);
        rep._links.put("all", baseUri + "disciplinas");
        rep._links.put("delete", baseUri + "disciplinas/" + disciplina.id);
        rep._links.put("update", baseUri + "disciplinas/" + disciplina.id);
        rep._links.put("search", baseUri + "disciplinas/search");

        return rep;
    }
}
