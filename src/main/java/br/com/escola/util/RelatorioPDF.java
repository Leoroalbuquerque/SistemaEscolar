package br.com.escola.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RelatorioPDF {

    private final PDDocument document;
    private PDPage page;
    private PDPageContentStream contentStream;
    private float margem = 50;
    private float yPosicao;
    private float larguraPagina;
    private boolean isFirstPage;

    public RelatorioPDF(PDDocument document) {
        this.document = document;
        this.isFirstPage = true;
        criarNovaPagina();
    }

    private void criarNovaPagina() {
        if (contentStream != null) {
            try {
                contentStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        larguraPagina = page.getMediaBox().getWidth() - 2 * margem;
        yPosicao = page.getMediaBox().getHeight() - margem;
        try {
            contentStream = new PDPageContentStream(document, page);
            isFirstPage = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adicionarTitulo(String texto) throws IOException {
        adicionarTexto(texto, PDType1Font.HELVETICA_BOLD, 18, yPosicao);
        yPosicao -= 30;
    }

    public void adicionarSubtitulo(String texto) throws IOException {
        adicionarTexto(texto, PDType1Font.HELVETICA, 14, yPosicao);
        yPosicao -= 20;
    }

    public void adicionarParagrafo(String texto) throws IOException {
        adicionarTextoComQuebra(texto, PDType1Font.HELVETICA, 12, 15);
        yPosicao -= 20;
    }
    
    public void pularLinha() {
        yPosicao -= 15;
    }

    private void adicionarTexto(String texto, PDFont fonte, int tamanho, float y) throws IOException {
        contentStream.setFont(fonte, tamanho);
        contentStream.beginText();
        contentStream.newLineAtOffset(margem, y);
        contentStream.showText(texto);
        contentStream.endText();
    }

    private void adicionarTextoComQuebra(String texto, PDFont fonte, int tamanho, float alturaLinha) throws IOException {
        contentStream.setFont(fonte, tamanho);
        List<String> linhas = wrapText(texto, fonte, tamanho);

        contentStream.beginText();
        contentStream.newLineAtOffset(margem, yPosicao);

        for (String linha : linhas) {
            contentStream.showText(linha);
            yPosicao -= alturaLinha;
            contentStream.newLineAtOffset(0, -alturaLinha);

            if (yPosicao < margem) {
                contentStream.endText();
                criarNovaPagina();
                contentStream.setFont(fonte, tamanho);
                contentStream.beginText();
                contentStream.newLineAtOffset(margem, yPosicao);
            }
        }
        contentStream.endText();
    }

    private List<String> wrapText(String texto, PDFont fonte, int tamanho) throws IOException {
        return new TextWrapper(texto, fonte, tamanho, larguraPagina).wrap();
    }

    public void adicionarTabela(String[] cabecalho, List<String[]> dados) throws IOException {
        float alturaLinha = 20;
        float larguraTabela = larguraPagina;
        int numColunas = cabecalho.length;
        float larguraColuna = larguraTabela / numColunas;
        float textX = margem + 5;
        float y = yPosicao - 15;

        adicionarCabecalhoTabela(cabecalho, larguraColuna, y);
        y -= alturaLinha;

        for (String[] linha : dados) {
            y = adicionarLinhaTabela(linha, larguraColuna, y, alturaLinha);
            if (y < margem) {
                criarNovaPagina();
                y = yPosicao - alturaLinha;
            }
        }
        yPosicao = y;
    }
    
    private void adicionarCabecalhoTabela(String[] cabecalho, float larguraColuna, float y) throws IOException {
        float textX = margem + 5;
        contentStream.setLineWidth(1f);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        
        contentStream.beginText();
        contentStream.newLineAtOffset(textX, y);
        for(int i = 0; i < cabecalho.length; i++) {
            contentStream.showText(cabecalho[i]);
            contentStream.newLineAtOffset(larguraColuna, 0);
        }
        contentStream.endText();
        
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(margem, y - 5);
        contentStream.lineTo(margem + (larguraColuna * cabecalho.length), y - 5);
        contentStream.stroke();
    }

    private float adicionarLinhaTabela(String[] linha, float larguraColuna, float y, float alturaLinha) throws IOException {
        float textX = margem + 5;
        contentStream.setFont(PDType1Font.HELVETICA, 10);

        contentStream.beginText();
        contentStream.newLineAtOffset(textX, y);
        for(int i = 0; i < linha.length; i++) {
            contentStream.showText(linha[i]);
            contentStream.newLineAtOffset(larguraColuna, 0);
        }
        contentStream.endText();
        
        contentStream.setLineWidth(0.2f);
        contentStream.moveTo(margem, y - alturaLinha);
        contentStream.lineTo(margem + (larguraColuna * linha.length), y - alturaLinha);
        contentStream.stroke();
        
        return y - alturaLinha;
    }
    
    public void salvarEFechar(String caminhoArquivo) throws IOException {
        if (contentStream != null) {
            contentStream.close();
        }
        document.save(new File(caminhoArquivo));
        document.close();
    }
}