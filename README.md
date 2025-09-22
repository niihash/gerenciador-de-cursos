# API de Gerenciamento de Curso

API desenvolvida para a disciplina de Web Services utilizando Quarkus, fornecendo funcionalidades simples para gerenciamento de alunos, cursos, disciplinas e professores em um ambiente acadêmico.

## Tecnologias Utilizadas

- **Quarkus** - Framework Java

## 📋 Funcionalidades

### Recursos Disponíveis

- **👨‍🎓 Alunos**: Cadastro, consulta, atualização e exclusão de alunos
- **📚 Cursos**: Gerenciamento completo de cursos
- **📖 Disciplinas**: Administração de disciplinas
- **👨‍🏫 Professores**: Controle de dados dos professores
- **🔗 Matrículas**: Sistema de matrícula de alunos em cursos

## 🔗 Endpoints Principais

### Alunos (`/alunos`)
- `GET /alunos` - Lista todos os alunos
- `POST /alunos` - Cria um novo aluno
- `GET /alunos/{id}` - Retorna informações de um aluno específico
- `PUT /alunos/{id}` - Atualiza dados de um aluno
- `DELETE /alunos/{id}` - Exclui um aluno
- `GET /alunos/search` - Pesquisa alunos com filtros
- `GET /alunos/{id}/cursos` - Lista cursos de um aluno
- `POST /alunos/{id}/cursos/{cursoId}` - Matricula aluno em curso
- `DELETE /alunos/{id}/cursos/{cursoId}` - Remove matrícula

### Cursos (`/cursos`)
- `GET /cursos` - Lista todos os cursos
- `POST /cursos` - Cria um novo curso
- `GET /cursos/{id}` - Retorna informações de um curso específico
- `PUT /cursos/{id}` - Atualiza dados de um curso
- `DELETE /cursos/{id}` - Exclui um curso
- `GET /cursos/search` - Pesquisa cursos com filtros
- `GET /cursos/{id}/alunos` - Lista alunos de um curso
- `POST /cursos/{id}/alunos/{alunoId}` - Matricula aluno no curso
- `DELETE /cursos/{id}/alunos/{alunoId}` - Remove matrícula

### Disciplinas (`/disciplinas`)
- `GET /disciplinas` - Lista todas as disciplinas
- `POST /disciplinas` - Cria uma nova disciplina
- `GET /disciplinas/{id}` - Retorna informações de uma disciplina específica
- `PUT /disciplinas/{id}` - Atualiza dados de uma disciplina
- `DELETE /disciplinas/{id}` - Exclui uma disciplina
- `GET /disciplinas/search` - Pesquisa disciplinas com filtros

### Professores (`/professores`)
- `GET /professores` - Lista todos os professores
- `POST /professores` - Cria um novo professor
- `GET /professores/{id}` - Retorna informações de um professor específico
- `PUT /professores/{id}` - Atualiza dados de um professor
- `DELETE /professores/{id}` - Exclui um professor
- `GET /professores/search` - Pesquisa professores com filtros

## 📚 Documentação da API

Após executar a aplicação, acesse:

- **Swagger UI**: https://gerenciador-de-cursos.onrender.com/q/swagger-ui/

## 🔧 Desenvolvimento

### Principais Entidades
- **Aluno**: Representa estudantes matriculados
- **Curso**: Representa os cursos oferecidos
- **Disciplina**: Representa as disciplinas acadêmicas
- **Professor**: Representa os docentes

## 📄 Licença

Este projeto não possui licença.