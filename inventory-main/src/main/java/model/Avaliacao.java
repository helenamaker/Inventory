package model;

public class Avaliacao {

    private int id;
    private Usuario usuario;
    private Jogo jogo;
    private double nota;
    private String comentario;
    private String data;
    private boolean favorito;
    private boolean recomendado;

    public Avaliacao() {
    }

    public Avaliacao(int id, Usuario usuario, Jogo jogo, double nota,
            String comentario, String data,
            boolean favorito, boolean recomendado) {

        this.id = id;
        this.usuario = usuario;
        this.jogo = jogo;
        this.nota = nota;
        this.comentario = comentario;
        this.data = data;
        this.favorito = favorito;
        this.recomendado = recomendado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public void setRecomendado(boolean recomendado) {
        this.recomendado = recomendado;
    }

    @Override
    public String toString() {
        return usuario.getNome() + " avaliou " + jogo.getTitulo();
    }
}