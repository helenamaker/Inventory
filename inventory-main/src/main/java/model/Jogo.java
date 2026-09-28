package model;

public class Jogo {

    private int id;
    private String titulo;
    private String descricao;
    private String genero;
    private String desenvolvedora;
    private String distribuidora;
    private String plataforma;
    private int anoLancamento;
    private String capa;
    private String trailer;
    private double notaMedia;

    public Jogo() {
    }

    public Jogo(int id, String titulo, String descricao, String genero,
            String desenvolvedora, String distribuidora,
            String plataforma, int anoLancamento,
            String capa, String trailer, double notaMedia) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.genero = genero;
        this.desenvolvedora = desenvolvedora;
        this.distribuidora = distribuidora;
        this.plataforma = plataforma;
        this.anoLancamento = anoLancamento;
        this.capa = capa;
        this.trailer = trailer;
        this.notaMedia = notaMedia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDesenvolvedora() {
        return desenvolvedora;
    }

    public void setDesenvolvedora(String desenvolvedora) {
        this.desenvolvedora = desenvolvedora;
    }

    public String getDistribuidora() {
        return distribuidora;
    }

    public void setDistribuidora(String distribuidora) {
        this.distribuidora = distribuidora;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(double notaMedia) {
        this.notaMedia = notaMedia;
    }

    @Override
    public String toString() {
        return titulo;
    }
}