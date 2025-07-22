package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResponsavelServico {

    private final IRepositorio<Responsavel, String> responsavelRepositorio;

    public ResponsavelServico(IRepositorio<Responsavel, String> responsavelRepositorio) {
        this.responsavelRepositorio = responsavelRepositorio;
    }

    public void adicionarResponsavel(Responsavel responsavel) throws DadoInvalidoException {
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
        
        if (responsavelRepositorio.buscarPorId(responsavel.getCpfResponsavel()).isPresent()) {
            throw new DadoInvalidoException("Já existe um responsável cadastrado com o CPF: " + responsavel.getCpfResponsavel());
        }

        responsavelRepositorio.salvar(responsavel);
    }

    public Responsavel buscarResponsavel(String cpfResponsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (cpfResponsavel == null || cpfResponsavel.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para busca não pode ser nulo ou vazio.");
        }
        return responsavelRepositorio.buscarPorId(cpfResponsavel)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsável com CPF " + cpfResponsavel + " não encontrado."));
    }

    public void atualizarResponsavel(Responsavel responsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException {
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
        if (responsavelRepositorio.buscarPorId(responsavel.getCpfResponsavel()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + responsavel.getCpfResponsavel() + " não encontrado para atualização.");
        }

        responsavelRepositorio.atualizar(responsavel);
    }

    public boolean deletarResponsavel(String cpfResponsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (cpfResponsavel == null || cpfResponsavel.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para deleção não pode ser nulo ou vazio.");
        }
        if (responsavelRepositorio.buscarPorId(cpfResponsavel).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + cpfResponsavel + " não encontrado para deleção.");
        }
        return responsavelRepositorio.deletar(cpfResponsavel);
    }

    public List<Responsavel> listarTodosResponsaveis() {
        return responsavelRepositorio.listarTodos();
    }

    public List<Responsavel> buscarResponsaveisPrincipais() {
        return responsavelRepositorio.listarTodos().stream()
                .filter(Responsavel::isPrincipal)
                .collect(Collectors.toList());
    }
}