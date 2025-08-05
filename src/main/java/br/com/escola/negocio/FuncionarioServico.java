package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FuncionarioServico {

    private final IRepositorio<Funcionario, String> funcionarioRepositorio;

    public FuncionarioServico(IRepositorio<Funcionario, String> funcionarioRepositorio) {
        this.funcionarioRepositorio = funcionarioRepositorio;
    }

    public void adicionarFuncionario(Funcionario funcionario) throws DadoInvalidoException, IOException {
        if (funcionario == null) {
            throw new DadoInvalidoException("Funcionário não pode ser nulo.");
        }
        if (funcionario.getMatriculaFuncional() == null || funcionario.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional do funcionário é obrigatória.");
        }

        String matriculaPadronizada = funcionario.getMatriculaFuncional().trim().toUpperCase();
        funcionario.setMatriculaFuncional(matriculaPadronizada);

        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do funcionário é obrigatório.");
        }
        if (!matriculaPadronizada.matches("[A-Z]{3}\\d{4}")) {
            throw new DadoInvalidoException("Formato da matrícula funcional inválido. Esperado XXX9999 (ex: SEC1234).");
        }

        try {
            funcionarioRepositorio.buscarPorId(funcionario.getMatriculaFuncional());
            throw new DadoInvalidoException("Já existe um funcionário cadastrado com a matrícula: " + funcionario.getMatriculaFuncional());
        } catch (EntidadeNaoEncontradaException e) {
            funcionarioRepositorio.salvar(funcionario);
        }
    }

    public Funcionario buscarFuncionario(String matriculaFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matriculaFuncional == null || matriculaFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para busca não pode ser nula ou vazia.");
        }
        String matriculaPadronizada = matriculaFuncional.trim().toUpperCase();
        return funcionarioRepositorio.buscarPorId(matriculaPadronizada);
    }

    public void atualizarFuncionario(Funcionario funcionario) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (funcionario == null) {
            throw new DadoInvalidoException("Funcionário não pode ser nulo para atualização.");
        }
        if (funcionario.getMatriculaFuncional() == null || funcionario.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional do funcionário é obrigatória para atualização.");
        }
        String matriculaPadronizada = funcionario.getMatriculaFuncional().trim().toUpperCase();
        funcionario.setMatriculaFuncional(matriculaPadronizada);

        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do funcionário é obrigatório para atualização.");
        }
        if (!matriculaPadronizada.matches("[A-Z]{3}\\d{4}")) {
            throw new DadoInvalidoException("Formato da matrícula funcional inválido. Esperado XXX9999 (ex: SEC1234).");
        }
        funcionarioRepositorio.atualizar(funcionario);
    }

    public void deletarFuncionario(String matriculaFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matriculaFuncional == null || matriculaFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para deleção não pode ser nula ou vazia.");
        }
        String matriculaPadronizada = matriculaFuncional.trim().toUpperCase();
        funcionarioRepositorio.deletar(matriculaPadronizada);
    }

    public List<Funcionario> listarTodosFuncionarios() throws IOException {
        return funcionarioRepositorio.listarTodos();
    }

    public List<Funcionario> buscarFuncionariosPorCargo(String cargo) throws DadoInvalidoException, IOException {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new DadoInvalidoException("Cargo para busca não pode ser nulo ou vazio.");
        }
        return funcionarioRepositorio.listarTodos().stream()
                .filter(f -> f.getCargo() != null && f.getCargo().equalsIgnoreCase(cargo.trim()))
                .collect(Collectors.toList());
    }
}