package br.com.escola.util;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Nota;
import br.com.escola.negocio.Frequencia;
import br.com.escola.negocio.Disciplina;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoletimEscolar {
    private Aluno aluno;
    private Map<Disciplina, List<Nota>> notasPorDisciplina;
    private Map<Disciplina, List<Frequencia>> frequenciasPorDisciplina;
    
    public BoletimEscolar(Aluno aluno, List<Nota> notas, List<Frequencia> frequencias) {
        this.aluno = aluno;
        this.notasPorDisciplina = notas.stream()
                .collect(Collectors.groupingBy(Nota::getDisciplina));
        this.frequenciasPorDisciplina = frequencias.stream()
                .collect(Collectors.groupingBy(Frequencia::getDisciplina));
    }
    
    public Aluno getAluno() {
        return aluno;
    }
    
    public Map<Disciplina, List<Nota>> getNotasPorDisciplina() {
        return notasPorDisciplina;
    }
    
    public Map<Disciplina, List<Frequencia>> getFrequenciasPorDisciplina() {
        return frequenciasPorDisciplina;
    }
}