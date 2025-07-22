package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.util.List;
import java.util.stream.Collectors;

public class FuncionarioServico {

    private final IRepositorio<Funcionario, String> funcionarioRepositorio;

    public FuncionarioServico(IRepositorio<Funcionario, String> funcionarioRepositorio) {
        this.funcionarioRepositorio = funcionarioRepositorio;
    }

    public void adicionarFuncionario(Funcionario funcionario) throws DadoInvalidoException {
        if (funcionario == null) {
            throw new DadoInvalidoException("Funcionário não pode ser nulo.");
        }
        if (funcionario.getMatriculaFuncional() == null || funcionario.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional do funcionário é obrigatória.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do funcionário é obrigatório.");
        }
        if (!funcionario.getMatriculaFuncional().matches("[A-Z]{3}\\d{4}")) {
            throw new DadoInvalidoException("Formato da matrícula funcional inválido. Esperado XXX9999 (ex: SEC1234).");
        }
        if (funcionarioRepositorio.buscarPorId(funcionario.getMatriculaFuncional()).isPresent()) {
            throw new DadoInvalidoException("Já existe um funcionário cadastrado com a matrícula: " + funcionario.getMatriculaFuncional());
        }
        funcionarioRepositorio.salvar(funcionario);
    }

    public Funcionario buscarFuncionario(String matriculaFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (matriculaFuncional == null || matriculaFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para busca não pode ser nula ou vazia.");
        }
        return funcionarioRepositorio.buscarPorId(matriculaFuncional)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário com matrícula " + matriculaFuncional + " não encontrado."));
    }

    public void atualizarFuncionario(Funcionario funcionario) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (funcionario == null) {
            throw new DadoInvalidoException("Funcionário não pode ser nulo para atualização.");
        }
        if (funcionario.getMatriculaFuncional() == null || funcionario.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional do funcionário é obrigatória para atualização.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do funcionário é obrigatório para atualização.");
        }
        if (!funcionario.getMatriculaFuncional().matches("[A-Z]{3}\\d{4}")) {
            throw new DadoInvalidoException("Formato da matrícula funcional inválido. Esperado XXX9999 (ex: SEC1234).");
        }
        if (funcionarioRepositorio.buscarPorId(funcionario.getMatriculaFuncional()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Funcionário com matrícula " + funcionario.getMatriculaFuncional() + " não encontrado para atualização.");
        }
        funcionarioRepositorio.atualizar(funcionario);
    }

    public boolean deletarFuncionario(String matriculaFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (matriculaFuncional == null || matriculaFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para deleção não pode ser nula ou vazia.");
        }
        return funcionarioRepositorio.deletar(matriculaFuncional);
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioRepositorio.listarTodos();
    }

    public List<Funcionario> buscarFuncionariosPorCargo(String cargo) throws DadoInvalidoException {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new DadoInvalidoException("Cargo para busca não pode ser nulo ou vazio.");
        }
        return funcionarioRepositorio.listarTodos().stream()
                .filter(f -> f.getCargo() != null && f.getCargo().equalsIgnoreCase(cargo.trim()))
                .collect(Collectors.toList());
    }
}