package br.com.escola.dados;

import br.com.escola.negocio.CalendarioEscolar;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return objectMapper.readValue(arquivo, objectMapper.getTypeFactory().constructCollectionType(List.class, CalendarioEscolar.class));
        } catch (IOException e) {
            System.err.println("Erro ao carregar calendários do arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarCalendarios() {
        try {
            objectMapper.writeValue(arquivo, calendarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar calendários no arquivo JSON: " + e.getMessage());
        }
    }

    @Override
    public void salvar(CalendarioEscolar calendario) {
        if (buscarPorId(calendario.getAnoLetivo()).isEmpty()) {
            calendarios.add(calendario);
            salvarCalendarios();
        } else {
            System.out.println("Calendário para o ano letivo " + calendario.getAnoLetivo() + " já existe e não foi salvo.");
        }
    }

    @Override
    public Optional<CalendarioEscolar> buscarPorId(Integer anoLetivo) {
        return calendarios.stream()
                .filter(c -> c.getAnoLetivo() == anoLetivo)
                .findFirst();
    }

    @Override
    public List<CalendarioEscolar> listarTodos() {
        return new ArrayList<>(calendarios);
    }

    @Override
    public void atualizar(CalendarioEscolar calendario) {
        Optional<CalendarioEscolar> existingCalendarioOpt = buscarPorId(calendario.getAnoLetivo());
        if (existingCalendarioOpt.isPresent()) {
            CalendarioEscolar existingCalendario = existingCalendarioOpt.get();
            int index = calendarios.indexOf(existingCalendario);
            if (index != -1) {
                calendarios.set(index, calendario);
                salvarCalendarios();
            }
        } else {
            System.out.println("Calendário para o ano letivo " + calendario.getAnoLetivo() + " não encontrado para atualização.");
        }
    }

    @Override
    public boolean deletar(Integer anoLetivo) {
        boolean removido = calendarios.removeIf(c -> c.getAnoLetivo() == anoLetivo);
        if (removido) {
            salvarCalendarios();
        }
        return removido;
    }

    @Override
    public void limpar() {
        this.calendarios.clear();
        salvarCalendarios();
    }
}