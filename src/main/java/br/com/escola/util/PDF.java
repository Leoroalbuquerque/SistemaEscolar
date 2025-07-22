package br.com.escola.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PDF {

    public static void gerarPDFSimples(String caminhoArquivo, String titulo, String conteudo) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText(titulo);
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);

                String[] linhasConteudo = conteudo.split("\n");
                
                for (String linha : linhasConteudo) {
                    contentStream.showText(linha);
                    contentStream.newLineAtOffset(0, -15);
                }
                contentStream.endText();
            }
            document.save(caminhoArquivo);
        }
    }
}