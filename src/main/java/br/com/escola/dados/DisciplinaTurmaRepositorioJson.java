package br.com.escola.dados;

import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.DisciplinaTurma;
import br.com.escola.negocio.Professor;
import br.com.escola.negocio.Turma;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DisciplinaTurmaRepositorioJson implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String NOME_ARQUIVO = "disciplinas_turmas.json";
    private final ObjectMapper objectMapper;
    private final File arquivo;

    public DisciplinaTurmaRepositorioJson() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.arquivo = new File(NOME_ARQUIVO);
        
        if (!arquivo.exists() || arquivo.length() == 0) {
            try {
                if (!arquivo.exists()) {
                    arquivo.createNewFile();
                }
                if (arquivo.length() == 0) { 
                    objectMapper.writeValue(arquivo, new ArrayList<DisciplinaTurma>());
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar/inicializar arquivo JSON para DisciplinaTurma: " + e.getMessage());
            }
        }
    }

    private List<DisciplinaTurma> lerTodasDoArquivo() throws IOException {
        if (arquivo.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(arquivo, new TypeReference<List<DisciplinaTurma>>() {});
    }

    private void escreverNoArquivo(List<DisciplinaTurma> disciplinasTurmas) throws IOException {
        objectMapper.writeValue(arquivo, disciplinasTurmas);
    }

    public void salvar(DisciplinaTurma disciplinaTurma) throws IOException {
        List<DisciplinaTurma> todasDisciplinasTurmas = lerTodasDoArquivo();
        
        todasDisciplinasTurmas.removeIf(dt -> dt.getId() != null && dt.getId().equals(disciplinaTurma.getId()));
        
        todasDisciplinasTurmas.add(disciplinaTurma);
        escreverNoArquivo(todasDisciplinasTurmas);
    }
    
    public void remover(DisciplinaTurma disciplinaTurma) throws IOException {
        List<DisciplinaTurma> todasDisciplinasTurmas = lerTodasDoArquivo();
        if (todasDisciplinasTurmas.remove(disciplinaTurma)) {
            escreverNoArquivo(todasDisciplinasTurmas);
        } else {
            throw new IllegalArgumentException("Atribuição de disciplina-turma não encontrada para remoção.");
        }
    }

    public DisciplinaTurma buscar(Disciplina disciplina, Professor professor, Turma turma) throws IOException {
        return lerTodasDoArquivo().stream()
                .filter(dt -> Objects.equals(dt.getDisciplina(), disciplina) &&
                              Objects.equals(dt.getProfessor(), professor) &&
                              Objects.equals(dt.getTurma(), turma))
                .findFirst()
                .orElse(null);
    }

    public DisciplinaTurma buscarPorId(String id) throws IOException {
        return lerTodasDoArquivo().stream()
                .filter(dt -> dt.getId() != null && dt.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void atualizar(DisciplinaTurma disciplinaTurmaAtualizada) throws IOException {
        List<DisciplinaTurma> todasDisciplinasTurmas = lerTodasDoArquivo();
        boolean encontrada = false;
        for (int i = 0; i < todasDisciplinasTurmas.size(); i++) {
            DisciplinaTurma dtExistente = todasDisciplinasTurmas.get(i);
            if (dtExistente.getId() != null && dtExistente.getId().equals(disciplinaTurmaAtualizada.getId())) {
                todasDisciplinasTurmas.set(i, disciplinaTurmaAtualizada);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new IllegalArgumentException("DisciplinaTurma com ID " + disciplinaTurmaAtualizada.getId() + " não encontrada para atualização.");
        }
        escreverNoArquivo(todasDisciplinasTurmas);
    }

    public List<DisciplinaTurma> listarTodos() throws IOException {
        return lerTodasDoArquivo();
    }

    public List<DisciplinaTurma> listarPorProfessor(Professor professor) throws IOException {
        if (professor == null) {
            return new ArrayList<>();
        }
        return lerTodasDoArquivo().stream()
                .filter(dt -> Objects.equals(dt.getProfessor(), professor))
                .collect(Collectors.toList());
    }

    public List<DisciplinaTurma> listarPorTurma(Turma turma) throws IOException {
        if (turma == null) {
            return new ArrayList<>();
        }
        return lerTodasDoArquivo().stream()
                .filter(dt -> Objects.equals(dt.getTurma(), turma))
                .collect(Collectors.toList());
    }

    public List<DisciplinaTurma> listarPorDisciplina(Disciplina disciplina) throws IOException {
        if (disciplina == null) {
            return new ArrayList<>();
        }
        return lerTodasDoArquivo().stream()
                .filter(dt -> Objects.equals(dt.getDisciplina(), disciplina))
                .collect(Collectors.toList());
    }

    public void limpar() throws IOException {
        escreverNoArquivo(new ArrayList<>());
    }
}