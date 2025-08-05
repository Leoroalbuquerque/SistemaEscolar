package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ResponsavelServico {

    private final IRepositorio<Responsavel, String> responsavelRepositorio;

    public ResponsavelServico(IRepositorio<Responsavel, String> responsavelRepositorio) {
        this.responsavelRepositorio = responsavelRepositorio;
    }

    public void adicionarResponsavel(Responsavel responsavel) throws DadoInvalidoException, IOException, EntidadeNaoEncontradaException {
        if (responsavel == null) {
            throw new DadoInvalidoException("Responsável não pode ser nulo.");
        }
        if (responsavel.getCpfResponsavel() == null || responsavel.getCpfResponsavel().trim().isEmpty()) {
            throw new DadoInvalidoException("CPF do responsável é obrigatório.");
        }
        if (responsavel.getNome() == null || responsavel.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do responsável é obrigatório.");
        }
        if (!responsavel.getCpfResponsavel().matches("\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new DadoInvalidoException("Formato de CPF do responsável inválido. Esperado 11 dígitos ou 999.999.999-99.");
        }
        
        try {
            responsavelRepositorio.buscarPorId(responsavel.getCpfResponsavel());
            throw new DadoInvalidoException("Já existe um responsável cadastrado com o CPF: " + responsavel.getCpfResponsavel());
        } catch (EntidadeNaoEncontradaException e) {
            responsavelRepositorio.salvar(responsavel);
        }
    }

    public Responsavel buscarResponsavel(String cpfResponsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (cpfResponsavel == null || cpfResponsavel.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para busca não pode ser nulo ou vazio.");
        }
        return responsavelRepositorio.buscarPorId(cpfResponsavel);
    }

    public void atualizarResponsavel(Responsavel responsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (responsavel == null) {
            throw new DadoInvalidoException("Responsável não pode ser nulo para atualização.");
        }
        if (responsavel.getCpfResponsavel() == null || responsavel.getCpfResponsavel().trim().isEmpty()) {
            throw new DadoInvalidoException("CPF do responsável é obrigatório para atualização.");
        }
        if (responsavel.getNome() == null || responsavel.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do responsável é obrigatório para atualização.");
        }
        if (!responsavel.getCpfResponsavel().matches("\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new DadoInvalidoException("Formato de CPF do responsável inválido. Esperado 11 dígitos ou 999.999.999-99.");
        }
        
        responsavelRepositorio.buscarPorId(responsavel.getCpfResponsavel());
        responsavelRepositorio.atualizar(responsavel);
    }

    public void deletarResponsavel(String cpfResponsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (cpfResponsavel == null || cpfResponsavel.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para deleção não pode ser nulo ou vazio.");
        }
        responsavelRepositorio.buscarPorId(cpfResponsavel);
        responsavelRepositorio.deletar(cpfResponsavel);
    }

    public List<Responsavel> listarTodosResponsaveis() throws IOException {
        return responsavelRepositorio.listarTodos();
    }

    public List<Responsavel> buscarResponsaveisPrincipais() throws IOException {
        return responsavelRepositorio.listarTodos().stream()
                .filter(Responsavel::isPrincipal)
                .collect(Collectors.toList());
    }
}