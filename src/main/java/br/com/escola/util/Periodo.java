package br.com.escola.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Periodo {
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Periodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas.");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início.");
        }
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public long getDuracaoEmDias() {
        return ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
    }

    public boolean contemData(LocalDate data) {
        if (data == null) {
            return false;
        }
        return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
    }

    public boolean sobrepoe(Periodo outroPeriodo) {
        if (outroPeriodo == null) {
            return false;
        }
        return !this.dataInicio.isAfter(outroPeriodo.dataFim) && !this.dataFim.isBefore(outroPeriodo.dataInicio);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periodo periodo = (Periodo) o;
        return Objects.equals(dataInicio, periodo.dataInicio) &&
                Objects.equals(dataFim, periodo.dataFim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataInicio, dataFim);
    }

    @Override
    public String toString() {
        return "Periodo{" +
                "dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                '}';
    }
}