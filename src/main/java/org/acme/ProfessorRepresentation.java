package org.acme;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ProfessorRepresentation {
    public long id;
    public String nome;
    public String email;
    public Map<String, String> _links;

    public ProfessorRepresentation() {
    }

    public static ProfessorRepresentation from(Professor professor, UriInfo uriInfo){
        ProfessorRepresentation rep = new ProfessorRepresentation();
        rep.id = professor.id;
        rep.nome = professor.nome;
        rep.email = professor.email;

        rep._links = new HashMap<>();
        URI baseUri = uriInfo.getBaseUri();

        rep._links.put("self", baseUri + "professores/" + professor.id);
        rep._links.put("all", baseUri + "professores");
        rep._links.put("delete", baseUri + "preofessores/" + professor.id);
        rep._links.put("update", baseUri + "professores/" + professor.id);
        rep._links.put("search", baseUri + "professores/search");

        return rep;
    }
}
