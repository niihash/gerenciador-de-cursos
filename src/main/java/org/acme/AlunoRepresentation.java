package org.acme;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlunoRepresentation {

    public long id;
    public String nome;
    public String email;
    public Aluno.Sexo sexo;
    public Map<String, String> _links;

    public List<Map<String, Object>> cursos;

    public AlunoRepresentation() {
    }

    public static AlunoRepresentation from(Aluno aluno, UriInfo uriInfo) {
        AlunoRepresentation rep = new AlunoRepresentation();
        rep.id = aluno.id;
        rep.nome = aluno.nome;
        rep.email = aluno.email;
        rep.sexo = aluno.sexo;

        rep._links = new HashMap<>();
        URI baseUri = uriInfo.getBaseUri();

        rep._links.put("self", baseUri + "alunos/" + aluno.id);
        rep._links.put("all", baseUri + "alunos");
        rep._links.put("delete", baseUri + "alunos/" + aluno.id);
        rep._links.put("update", baseUri + "alunos/" + aluno.id);
        rep._links.put("search", baseUri + "alunos/search");

        rep.cursos = aluno.cursos.stream()
                .map(curso -> {
                    Map<String, Object> cursoMap = new HashMap<>();
                    cursoMap.put("id", curso.id);
                    cursoMap.put("nome", curso.nome);
                    cursoMap.put("descricao", curso.descricao);
                    cursoMap.put("cargaHoraria", curso.cargaHoraria);

                    cursoMap.put("_link", baseUri + "cursos/" + curso.id);

                    return cursoMap;
                }).collect(Collectors.toList());

        return rep;
    }

}
