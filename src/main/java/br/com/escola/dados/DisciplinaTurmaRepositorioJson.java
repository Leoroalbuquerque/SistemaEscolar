package br.com.escola.dados;

import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.DisciplinaTurma;
import br.com.escola.negocio.Professor;
import br.com.escola.negocio.Turma;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
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
import java.util.UUID;
import java.util.stream.Collectors;

public class DisciplinaTurmaRepositorioJson implements IRepositorio<DisciplinaTurma, String>, Serializable {
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

    @Override
    public void salvar(DisciplinaTurma disciplinaTurma) throws IOException {
        if (disciplinaTurma.getId() == null || disciplinaTurma.getId().isEmpty()) {
            disciplinaTurma.setId(UUID.randomUUID().toString());
        }
        List<DisciplinaTurma> todasDisciplinasTurmas = lerTodasDoArquivo();
        todasDisciplinasTurmas.add(disciplinaTurma);
        escreverNoArquivo(todasDisciplinasTurmas);
    }
    
    @Override
    public void deletar(String id) throws IOException, EntidadeNaoEncontradaException {
        List<DisciplinaTurma> todasDisciplinasTurmas = lerTodasDoArquivo();
        boolean removido = todasDisciplinasTurmas.removeIf(dt -> dt.getId() != null && dt.getId().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Atribuição de disciplina-turma com ID " + id + " não encontrada para remoção.");
        }
        escreverNoArquivo(todasDisciplinasTurmas);
    }

    @Override
    public DisciplinaTurma buscarPorId(String id) throws IOException, EntidadeNaoEncontradaException {
        return lerTodasDoArquivo().stream()
                .filter(dt -> dt.getId() != null && dt.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Atribuição de disciplina-turma com ID " + id + " não encontrada."));
    }

    public DisciplinaTurma buscarAssociacao(String codigoDisciplina, String codigoTurma, String cpfProfessor) throws IOException, EntidadeNaoEncontradaException {
        return lerTodasDoArquivo().stream()
                .filter(dt -> Objects.equals(dt.getDisciplina().getCodigo(), codigoDisciplina) &&
                                Objects.equals(dt.getTurma().getCodigo(), codigoTurma) &&
                                (cpfProfessor == null ? dt.getProfessor() == null : 
                                 Objects.equals(dt.getProfessor() != null ? dt.getProfessor().getCpf() : null, cpfProfessor)))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Atribuição de disciplina-turma não encontrada para a disciplina, turma e professor especificados."));
    }

    public DisciplinaTurma buscarAssociacaoPorDisciplinaETurma(String codigoDisciplina, String codigoTurma) throws IOException, EntidadeNaoEncontradaException {
        return lerTodasDoArquivo().stream()
                .filter(dt -> Objects.equals(dt.getDisciplina().getCodigo(), codigoDisciplina) &&
                                Objects.equals(dt.getTurma().getCodigo(), codigoTurma))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Associação de disciplina e turma não encontrada para a disciplina e turma especificadas."));
    }

    @Override
    public void atualizar(DisciplinaTurma disciplinaTurmaAtualizada) throws IOException, EntidadeNaoEncontradaException {
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
            throw new EntidadeNaoEncontradaException("DisciplinaTurma com ID " + disciplinaTurmaAtualizada.getId() + " não encontrada para atualização.");
        }
        escreverNoArquivo(todasDisciplinasTurmas);
    }

    @Override
    public List<DisciplinaTurma> listarTodos() throws IOException {
        return lerTodasDoArquivo();
    }

    public List<DisciplinaTurma> listarPorProfessor(String cpfProfessor) throws IOException {
        if (cpfProfessor == null || cpfProfessor.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return lerTodasDoArquivo().stream()
                .filter(dt -> dt.getProfessor() != null && dt.getProfessor().getCpf().equals(cpfProfessor))
                .collect(Collectors.toList());
    }

    public List<DisciplinaTurma> listarPorTurma(String codigoTurma) throws IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return lerTodasDoArquivo().stream()
                .filter(dt -> dt.getTurma() != null && dt.getTurma().getCodigo().equals(codigoTurma))
                .collect(Collectors.toList());
    }

    public List<DisciplinaTurma> listarPorDisciplina(String codigoDisciplina) throws IOException {
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return lerTodasDoArquivo().stream()
                .filter(dt -> dt.getDisciplina() != null && dt.getDisciplina().getCodigo().equals(codigoDisciplina))
                .collect(Collectors.toList());
    }

    public void limpar() throws IOException {
        escreverNoArquivo(new ArrayList<>());
    }
}