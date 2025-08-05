package br.com.escola.dados;

import br.com.escola.negocio.CalendarioEscolar;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CalendarioEscolarRepositorioJson implements IRepositorio<CalendarioEscolar, Integer> {

    private static final String NOME_ARQUIVO = "calendarios.json";
    private final File arquivo;
    private final ObjectMapper objectMapper;
    private List<CalendarioEscolar> calendarios;

    public CalendarioEscolarRepositorioJson() {
        this.arquivo = new File(NOME_ARQUIVO);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.calendarios = carregarCalendarios();
    }

    private List<CalendarioEscolar> carregarCalendarios() {
        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(arquivo, new TypeReference<List<CalendarioEscolar>>() {});
        } catch (IOException e) {
            System.err.println("Erro ao carregar calendários do arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarCalendarios() throws IOException {
        try {
            objectMapper.writeValue(arquivo, calendarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar calendários no arquivo JSON: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(CalendarioEscolar calendario) throws IOException, DadoInvalidoException {
        if (calendario == null) {
            throw new DadoInvalidoException("Calendário não pode ser nulo.");
        }

        try {
            buscarPorId(calendario.getAnoLetivo());
            throw new DadoInvalidoException("Calendário para o ano letivo " + calendario.getAnoLetivo() + " já existe.");
        } catch (EntidadeNaoEncontradaException e) {
            calendarios.add(calendario);
            salvarCalendarios();
        }
    }

    @Override
    public CalendarioEscolar buscarPorId(Integer anoLetivo) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (anoLetivo == null) {
            throw new DadoInvalidoException("Ano letivo para busca não pode ser nulo.");
        }
        return calendarios.stream()
                .filter(c -> Objects.equals(c.getAnoLetivo(), anoLetivo))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Calendário para o ano letivo " + anoLetivo + " não encontrado."));
    }

    @Override
    public List<CalendarioEscolar> listarTodos() throws IOException {
        return new ArrayList<>(calendarios);
    }

    @Override
    public void atualizar(CalendarioEscolar calendario) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (calendario == null) {
            throw new DadoInvalidoException("Calendário não pode ser nulo para atualização.");
        }
        
        boolean encontrada = false;
        for (int i = 0; i < calendarios.size(); i++) {
            if (Objects.equals(calendarios.get(i).getAnoLetivo(), calendario.getAnoLetivo())) {
                calendarios.set(i, calendario);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Calendário para o ano letivo " + calendario.getAnoLetivo() + " não encontrado para atualização.");
        }
        salvarCalendarios();
    }

    @Override
    public void deletar(Integer anoLetivo) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (anoLetivo == null) {
            throw new DadoInvalidoException("Ano letivo para exclusão não pode ser nulo.");
        }
        boolean removido = calendarios.removeIf(c -> Objects.equals(c.getAnoLetivo(), anoLetivo));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Calendário para o ano letivo " + anoLetivo + " não encontrado para exclusão.");
        }
        salvarCalendarios();
    }

    @Override
    public void limpar() throws IOException {
        this.calendarios.clear();
        salvarCalendarios();
    }
}