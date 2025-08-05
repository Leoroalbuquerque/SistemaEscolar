package br.com.escola.util;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Nota;
import br.com.escola.negocio.Frequencia;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HistoricoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Aluno aluno;
    private Map<String, List<Nota>> notasPorDisciplina;
    private Map<String, List<Frequencia>> frequenciasPorDisciplina;
    
    public HistoricoEscolar(Aluno aluno, Map<String, List<Nota>> notasPorDisciplina, Map<String, List<Frequencia>> frequenciasPorDisciplina) {
        this.aluno = aluno;
        this.notasPorDisciplina = notasPorDisciplina;
        this.frequenciasPorDisciplina = frequenciasPorDisciplina;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Map<String, List<Nota>> getNotasPorDisciplina() {
        return notasPorDisciplina;
    }

    public Map<String, List<Frequencia>> getFrequenciasPorDisciplina() {
        return frequenciasPorDisciplina;
    }
}