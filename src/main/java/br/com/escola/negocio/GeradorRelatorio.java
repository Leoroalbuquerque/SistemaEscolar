package br.com.escola.negocio;

import br.com.escola.util.PDF;
import br.com.escola.util.Periodo;

public class GeradorRelatorio {
    public PDF gerarRelatorioAlunosPorTurma(Turma turma) { return new PDF(); }
    public PDF gerarRelatorioFrequenciaPorAluno(Aluno aluno, Periodo periodo) { return new PDF(); }
    public PDF gerarBoletim(Aluno aluno, Periodo periodo) { return new PDF(); }
    public PDF gerarHistorico(Aluno aluno) { return new PDF(); }
    public PDF gerarDeclaracaoMatricula(Aluno aluno, int anoLetivo) { return new PDF(); }
    public PDF gerarRelatorioDesempenhoPorDisciplina(Disciplina disciplina, Periodo periodo) { return new PDF(); }
    public PDF gerarRelatorioBaixoDesempenho(Periodo periodo, Disciplina disciplina) { return new PDF(); }
    public PDF gerarRelatorioOcorrencias(Aluno aluno, Turma turma) { return new PDF(); }
    public PDF gerarRelatorioAprovacaoReprovacao(Turma turma, int anoLetivo) { return new PDF(); }
    public PDF gerarDiarioClasse(Turma turma, Disciplina disciplina) { return new PDF(); }
}