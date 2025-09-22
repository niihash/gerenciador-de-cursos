package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/cursos")
public class CursoResource {

    @Context
    UriInfo uriInfo;

    private CursoRepresentation rep(Curso c){
        return CursoRepresentation.from(c, uriInfo);
    }

    private List<CursoRepresentation> repList(List<Curso> cursos){
        return cursos.stream().map(this::rep).collect(Collectors.toList());
    }

    @GET
    @Operation(
            summary = "Retorna todos os cursos",
            description = "Retorna uma lista de cursos por padrão no formato JSON"
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class, type = SchemaType.ARRAY)
            )
    )
    public Response getAll(){
        return Response.ok(repList(Curso.listAll())).build();
    }

    @GET
    @Operation(
            summary = "Retorna as informações de um curso",
            description = "Retorna as informações de um curso no formato JSON"
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Path("{id}")
    public Response getById(
            @Parameter(description = "Id do curso a ser pesquisado", required = true)
            @PathParam("id") long id
    ) {
        Curso entity = Curso.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        return Response.ok(rep(entity)).build();
    }

    @GET
    @Operation(
            summary = "Retorna as informações de uma pesquisa",
            description = "Retorna as informações de uma pesquisa por nome ou descrição no formato JSON"
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SearchCursoResponse.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Path("/search")
    public Response search(
            @Parameter(description = "Query de busca por nome ou descricao")
            @QueryParam("q") String q,
            @Parameter(description = "Campo de ordenação da lista de retorno")
            @QueryParam("sort") @DefaultValue("id") String sort,
            @Parameter(description = "Direção da ordenação ascendente/descendente")
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        Set<String> allowed = Set.of("id", "nome", "descricao", "cargaHoraria", "professor");
        if (!allowed.contains(sort)) {
            sort = "id";
        }

        Sort sortObj = Sort.by(
                sort,
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.Descending : Sort.Direction.Ascending
        );

        int effectivePage = page <= 1 ? 0 : page - 1;

        PanacheQuery<Curso> query = (q == null || q.isBlank())
                ? Curso.findAll(sortObj)
                : Curso.find("lower(nome) like ?1 or lower(descricao) like ?1",
                sortObj,
                "%" + q.toLowerCase() + "%");

        long totalElements = query.count();
        long totalPages = (long) Math.ceil((double) totalElements / size);

        List<Curso> cursos = query.page(effectivePage, size).list();

        SearchCursoResponse response = SearchCursoResponse.from(
                cursos, uriInfo, q, sort, direction, page, size, totalElements, totalPages
        );

        return Response.ok(response).build();
    }

    @POST
    @Operation(
            summary = "Cria um novo curso",
            description = "Cria uma novo curso com as informações fornecidas no formato JSON"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    @APIResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Transactional
    public Response insert(@Valid Curso curso){

        Curso.persist(curso);
        return Response.status(201).entity(rep(curso)).build();
    }

    @DELETE
    @Operation(
            summary = "Exclui um curso",
            description = "Exclui um curso pelo id fornecido"
    )
    @APIResponse(
            responseCode = "204",
            description = "Deletada"
    )
    @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Transactional
    @Path("{id}")
    public Response delete(
            @PathParam("id") long id
    ) {
        Curso entity = Curso.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        Curso.deleteById(id);
        return Response.noContent().build();
    }

    @PUT
    @Operation(
            summary = "Atualiza um curso",
            description = "Atualiza um curso pelo id fornecido e com as informações no formato JSON"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Transactional
    @Path("{id}")
    public Response update(
            @PathParam("id") long id,
            @Valid Curso newCurso
    ) {
        Curso entity = Curso.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }

        entity.nome = newCurso.nome;
        entity.descricao = newCurso.descricao;
        entity.cargaHoraria = newCurso.cargaHoraria;
        entity.professor = newCurso.professor;

        return Response.status(200).entity(rep(entity)).build();
    }

    // Matricula a partir daqui

    @POST
    @Operation(
            summary = "Matricula um aluno no curso",
            description = "Matricula um novo aluno no curso com os ids fornecidos"
    )
    @RequestBody(
            required = false
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = Curso.class)
//            )
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Conflict",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Path("{id}/alunos/{alunoId}")
    @Transactional
    public Response matricular(
            @PathParam("id") long cursoId,
            @PathParam(("alunoId")) long alunoId
    ){
        Curso curso = Curso.findById(cursoId);
        if (curso == null) {
            return Response.status(404).entity("Curso não encontrado").build();
        }

        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            return Response.status(404).entity("Aluno não encontrado").build();
        }

        if(curso.alunos.contains(aluno)){
            return Response.status(409).entity("Aluno já está matriculado neste curso").build();
        }

        aluno.cursos.add(curso);

        // Atualiza as informações de curso para a resposta
        curso = Curso.findById(cursoId);

        return Response.status(200).entity(rep(curso)).build();
    }

    @GET
    @Operation(
            summary = "Retorna um curso e todos os seus alunos",
            description = "Retorna um curso e todos os seus alunos no formato JSON"
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Path("{id}/alunos")
    public Response listarAlunos(
            @PathParam("id") long cursoId
    ){
        Curso curso = Curso.findById(cursoId);
        if (curso == null) {
            return Response.status(404).build();
        }

        return Response.ok(rep(curso)).build();
    }

    @DELETE
    @Operation(
            summary = "Deleta a matricula de um aluno no curso",
            description = "Deleta a matricula de um aluno no curso com os ids fornecidos"
    )
    @RequestBody(
            required = false
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = Curso.class)
//            )
    )
    @APIResponse(
            responseCode = "204",
            description = "Deletado"
    )
    @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Path("{id}/alunos/{alunoId}")
    @Transactional
    public Response desmatricular(
            @PathParam("id") long cursoId,
            @PathParam("alunoId") long alunoId
    ) {

        Curso curso = Curso.findById(cursoId);
        if (curso == null) {
            return Response.status(404).entity("Curso não encontrado").build();
        }

        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            return Response.status(404).entity("Aluno não encontrado").build();
        }

        if(!curso.alunos.contains(aluno)){
            return Response.status(404).entity("Aluno não está matriculado neste curso").build();
        }

        aluno.cursos.remove(curso);
        return Response.noContent().build();
    }
}
