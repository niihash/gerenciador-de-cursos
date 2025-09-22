package org.acme;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CursoRepresentation {

    public long id;
    public String nome;
    public String descricao;
    public int cargaHoraria;
    public Map<String, String> _links;

    public List<Map<String, Object>> alunos;

    public Professor professor;

    public CursoRepresentation() {
    }

    public static CursoRepresentation from(Curso curso, UriInfo uriInfo) {
        CursoRepresentation rep = new CursoRepresentation();
        rep.id = curso.id;
        rep.nome = curso.nome;
        rep.descricao = curso.descricao;
        rep.cargaHoraria = curso.cargaHoraria;

        rep.professor = curso.professor;

        rep._links = new HashMap<>();
        URI baseUri = uriInfo.getBaseUri();

        rep._links.put("self", baseUri + "cursos/" + curso.id);
        rep._links.put("all", baseUri + "cursos");
        rep._links.put("delete", baseUri + "cursos/" + curso.id);
        rep._links.put("update", baseUri + "cursos/" + curso.id);
        rep._links.put("search", baseUri + "cursos/search");

        rep.alunos = curso.alunos.stream()
                .map(aluno -> {
                    Map<String, Object> alunoMap = new HashMap<>();
                    alunoMap.put("id", aluno.id);
                    alunoMap.put("nome", aluno.nome);
                    alunoMap.put("email", aluno.email);
                    alunoMap.put("sexo", aluno.sexo);
                    alunoMap.put("_link", baseUri + "alunos/" + aluno.id);
                    return alunoMap;
                }).collect(Collectors.toList());

        return rep;
    }
}
