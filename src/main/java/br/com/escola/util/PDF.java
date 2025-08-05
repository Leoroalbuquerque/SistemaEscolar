package br.com.escola.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDF {

    public static void gerarPDFSimples(String caminhoArquivo, String titulo, String conteudo) throws IOException {
        try (PDDocument document = new PDDocument()) {
            RelatorioPDF relatorio = new RelatorioPDF(document);
            relatorio.adicionarTitulo(titulo);
            relatorio.adicionarParagrafo(conteudo);
            relatorio.salvarEFechar(caminhoArquivo);
        }
    }
}