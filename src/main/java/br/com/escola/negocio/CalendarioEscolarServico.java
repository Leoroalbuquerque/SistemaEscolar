package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.util.EventoCalendario;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CalendarioEscolarServico {

    private IRepositorio<CalendarioEscolar, Integer> repositorioCalendarios;
    private AvaliacaoServico avaliacaoServico;

    public CalendarioEscolarServico(IRepositorio<CalendarioEscolar, Integer> repositorioCalendarios,
                                     AvaliacaoServico avaliacaoServico) {
        this.repositorioCalendarios = repositorioCalendarios;
        this.avaliacaoServico = avaliacaoServico;
    }

    public void adicionarCalendario(CalendarioEscolar calendario) throws DadoInvalidoException, IOException {
        if (calendario == null) {
            throw new DadoInvalidoException("Calendário escolar não pode ser nulo.");
        }
        if (calendario.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Ano letivo inválido para o calendário.");
        }
        try {
            repositorioCalendarios.buscarPorId(calendario.getAnoLetivo());
            throw new DadoInvalidoException("Calendário para o ano letivo " + calendario.getAnoLetivo() + " já existe.");
        } catch (EntidadeNaoEncontradaException e) {
            repositorioCalendarios.salvar(calendario);
        }
    }

    public CalendarioEscolar buscarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (anoLetivo <= 0) {
            throw new DadoInvalidoException("Ano letivo inválido para busca.");
        }
        return repositorioCalendarios.buscarPorId(anoLetivo);
    }

    public List<CalendarioEscolar> listarTodosCalendarios() throws IOException {
        return repositorioCalendarios.listarTodos();
    }

    public void atualizarCalendario(CalendarioEscolar calendario) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (calendario == null || calendario.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Calendário escolar e seu ano letivo não podem ser nulos/inválidos para atualização.");
        }
        repositorioCalendarios.atualizar(calendario);
    }

    public void deletarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (anoLetivo <= 0) {
            throw new DadoInvalidoException("Ano letivo inválido para exclusão.");
        }
        repositorioCalendarios.deletar(anoLetivo);
    }

    public void adicionarDataLetiva(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (data == null) {
            throw new DadoInvalidoException("Data letiva não pode ser nula.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        if (calendario.getFeriados().contains(data)) {
            throw new DadoInvalidoException("Data " + data + " já está registrada como feriado e não pode ser letiva.");
        }
        calendario.adicionarDataLetiva(data);
        atualizarCalendario(calendario);
    }

    public void removerDataLetiva(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (data == null) {
            throw new DadoInvalidoException("Data letiva não pode ser nula para remoção.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        calendario.removerDataLetiva(data);
        atualizarCalendario(calendario);
    }

    public void adicionarFeriado(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (data == null) {
            throw new DadoInvalidoException("Feriado não pode ser nulo.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        if (calendario.getDatasLetivas().contains(data)) {
            throw new DadoInvalidoException("Data " + data + " já está registrada como data letiva e não pode ser feriado.");
        }
        calendario.adicionarFeriado(data);
        atualizarCalendario(calendario);
    }

    public void removerFeriado(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (data == null) {
            throw new DadoInvalidoException("Feriado não pode ser nulo para remoção.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        calendario.removerFeriado(data);
        atualizarCalendario(calendario);
    }

    public void registrarAvaliacaoNoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        Avaliacao avaliacao = avaliacaoServico.buscarAvaliacaoPorId(idAvaliacao);
        calendario.registrarAvaliacao(avaliacao);
        atualizarCalendario(calendario);
    }

    public void removerAvaliacaoDoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        Avaliacao avaliacao = avaliacaoServico.buscarAvaliacaoPorId(idAvaliacao);
        calendario.removerAvaliacao(avaliacao);
        atualizarCalendario(calendario);
    }
    
    public void adicionarEventoNoCalendario(int anoLetivo, EventoCalendario evento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (evento == null || evento.getData() == null || evento.getDescricao() == null || evento.getDescricao().trim().isEmpty()) {
            throw new DadoInvalidoException("O evento e seus atributos (data e descrição) não podem ser nulos/vazios.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        
        calendario.adicionarEvento(evento); 
        
        atualizarCalendario(calendario);
    }
    
    public void removerEventoDoCalendario(int anoLetivo, String idEvento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (idEvento == null || idEvento.trim().isEmpty()) {
            throw new DadoInvalidoException("O ID do evento não pode ser nulo ou vazio para remoção.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        
        calendario.removerEventoPorId(idEvento);
        
        atualizarCalendario(calendario);
    }

    public void limparRepositorio() throws IOException {
        repositorioCalendarios.limpar();
    }
}