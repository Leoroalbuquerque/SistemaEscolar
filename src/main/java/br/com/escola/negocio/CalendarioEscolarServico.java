package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CalendarioEscolarServico {

    private IRepositorio<CalendarioEscolar, Integer> repositorioCalendarios;
    private AvaliacaoServico avaliacaoServico;

    public CalendarioEscolarServico(IRepositorio<CalendarioEscolar, Integer> repositorioCalendarios,
                                    AvaliacaoServico avaliacaoServico) {
        this.repositorioCalendarios = repositorioCalendarios;
        this.avaliacaoServico = avaliacaoServico;
    }

    public void adicionarCalendario(CalendarioEscolar calendario) throws DadoInvalidoException {
        if (calendario == null) {
            throw new DadoInvalidoException("Calendário escolar não pode ser nulo.");
        }
        if (calendario.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Ano letivo inválido para o calendário.");
        }
        if (repositorioCalendarios.buscarPorId(calendario.getAnoLetivo()).isPresent()) {
            throw new DadoInvalidoException("Calendário para o ano letivo " + calendario.getAnoLetivo() + " já existe.");
        }
        repositorioCalendarios.salvar(calendario);
    }

    public CalendarioEscolar buscarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (anoLetivo <= 0) {
            throw new DadoInvalidoException("Ano letivo inválido para busca.");
        }
        return repositorioCalendarios.buscarPorId(anoLetivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Calendário para o ano letivo " + anoLetivo + " não encontrado."));
    }

    public List<CalendarioEscolar> listarTodosCalendarios() {
        return repositorioCalendarios.listarTodos();
    }

    public void atualizarCalendario(CalendarioEscolar calendario) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (calendario == null || calendario.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Calendário escolar e seu ano letivo não podem ser nulos/inválidos para atualização.");
        }
        if (repositorioCalendarios.buscarPorId(calendario.getAnoLetivo()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Calendário para o ano letivo " + calendario.getAnoLetivo() + " não encontrado para atualização.");
        }
        repositorioCalendarios.atualizar(calendario);
    }

    public boolean deletarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (anoLetivo <= 0) {
            throw new DadoInvalidoException("Ano letivo inválido para exclusão.");
        }
        if (repositorioCalendarios.buscarPorId(anoLetivo).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Calendário para o ano letivo " + anoLetivo + " não encontrado para exclusão.");
        }
        return repositorioCalendarios.deletar(anoLetivo);
    }

    public void adicionarDataLetiva(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException {
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

    public void removerDataLetiva(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (data == null) {
            throw new DadoInvalidoException("Data letiva não pode ser nula para remoção.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        calendario.removerDataLetiva(data);
        atualizarCalendario(calendario);
    }

    public void adicionarFeriado(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException {
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

    public void removerFeriado(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (data == null) {
            throw new DadoInvalidoException("Feriado não pode ser nulo para remoção.");
        }
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        calendario.removerFeriado(data);
        atualizarCalendario(calendario);
    }

    public void registrarAvaliacaoNoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        Avaliacao avaliacao = avaliacaoServico.buscarAvaliacaoPorId(idAvaliacao);
        calendario.registrarAvaliacao(avaliacao);
        atualizarCalendario(calendario);
    }

    public void removerAvaliacaoDoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        CalendarioEscolar calendario = buscarCalendario(anoLetivo);
        Avaliacao avaliacao = avaliacaoServico.buscarAvaliacaoPorId(idAvaliacao);
        calendario.removerAvaliacao(avaliacao);
        atualizarCalendario(calendario);
    }

    public void limparRepositorio() {
        repositorioCalendarios.limpar();
    }
}