package controller;

import dao.CriarBanco;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            );

    // =====================================================
    // INICIAR
    // =====================================================

    @Override
    public void init()
            throws ServletException {

        System.out.println(
                "================================="
        );

        System.out.println(
                "USUARIOSERVLET INICIADO"
        );

        System.out.println(
                "================================="
        );

        CriarBanco.criarTabela();
    }

    // =====================================================
    // POST - CADASTRO
    // =====================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(
                "================================="
        );

        System.out.println(
                "SERVLET CADASTRO FOI CHAMADO"
        );

        System.out.println(
                "================================="
        );

        request.setCharacterEncoding(
                "UTF-8"
        );

        // =================================================
        // DADOS DO FORMULÁRIO
        // =================================================

        String nome =
                limpar(
                        request.getParameter("nome")
                );

        String username =
                limpar(
                        request.getParameter("username")
                );

        String email =
                limpar(
                        request.getParameter("email")
                ).toLowerCase();

        String senha =
                request.getParameter("senha");

        String dataNascimento =
                limpar(
                        request.getParameter(
                                "dataNascimento"
                        )
                );

        String pais =
                limpar(
                        request.getParameter("pais")
                );

        String plataforma =
                limpar(
                        request.getParameter(
                                "plataforma"
                        )
                );

        String bio =
                limpar(
                        request.getParameter("bio")
                );

        System.out.println(
                "Nome: " + nome
        );

        System.out.println(
                "Username: " + username
        );

        System.out.println(
                "Email: " + email
        );

        // =================================================
        // VALIDAÇÕES
        // =================================================

        if (nome.isEmpty()
                || username.isEmpty()
                || email.isEmpty()
                || senha == null
                || senha.trim().isEmpty()) {

            mostrarErro(
                    response,
                    "Preencha todos os campos obrigatórios."
            );

            return;
        }

        if (!emailValido(email)) {

            mostrarErro(
                    response,
                    "Digite um e-mail válido."
            );

            return;
        }

        if (senha.length() < 6) {

            mostrarErro(
                    response,
                    "A senha deve ter pelo menos 6 caracteres."
            );

            return;
        }

        // =================================================
        // DAO
        // =================================================

        UsuarioDAO usuarioDAO =
                new UsuarioDAO();

        // =================================================
        // VERIFICAR E-MAIL JÁ CADASTRADO
        // =================================================

        Usuario usuarioExistente =
                usuarioDAO.buscarPorEmail(
                        email
                );

        if (usuarioExistente != null) {

            mostrarErro(
                    response,
                    "Este e-mail já está cadastrado."
            );

            return;
        }

        // =================================================
        // VERIFICAR USERNAME
        // =================================================

        Usuario usernameExistente =
                usuarioDAO.buscarPorUsername(
                        username
                );

        if (usernameExistente != null) {

            mostrarErro(
                    response,
                    "Este nome de usuário já está em uso."
            );

            return;
        }

        // =================================================
        // FOTO
        // =================================================

        Part arquivoFoto =
                request.getPart("foto");

        String nomeFoto = null;

        if (arquivoFoto != null &&
                arquivoFoto.getSize() > 0) {

            String nomeOriginal =
                    arquivoFoto.getSubmittedFileName();

            if (nomeOriginal == null ||
                    nomeOriginal.trim().isEmpty()) {

                nomeOriginal =
                        "foto.jpg";
            }

            nomeOriginal =
                    new File(nomeOriginal)
                            .getName();

            String nomeMinusculo =
                    nomeOriginal.toLowerCase();

            // =================================================
            // VALIDAR EXTENSÃO
            // =================================================

            if (!nomeMinusculo.endsWith(".jpg")
                    && !nomeMinusculo.endsWith(".jpeg")
                    && !nomeMinusculo.endsWith(".png")
                    && !nomeMinusculo.endsWith(".webp")) {

                mostrarErro(
                        response,
                        "Formato de foto inválido. "
                        + "Use JPG, JPEG, PNG ou WEBP."
                );

                return;
            }

            nomeFoto =
                    System.currentTimeMillis()
                    + "_"
                    + nomeOriginal;

            // =================================================
            // PASTA DE UPLOAD
            // =================================================

            String uploadsPath =
                    System.getenv("UPLOADS_PATH");

            String caminhoBase;

            if (uploadsPath != null &&
                    !uploadsPath.trim().isEmpty()) {

                caminhoBase =
                        uploadsPath
                        + File.separator
                        + "perfil";

            } else {

                String sistema =
                        System.getProperty(
                                "os.name"
                        ).toLowerCase();

                if (sistema.contains("win")) {

                    caminhoBase =
                            "C:\\GameBoxdUploads\\data\\perfil";

                } else {

                    caminhoBase =
                            "/app/data/perfil";
                }
            }

            File pasta =
                    new File(caminhoBase);

            if (!pasta.exists()) {

                if (!pasta.mkdirs()) {

                    mostrarErro(
                            response,
                            "Não foi possível criar "
                            + "a pasta da foto."
                    );

                    return;
                }
            }

            File arquivo =
                    new File(
                            pasta,
                            nomeFoto
                    );

            // =================================================
            // SALVAR FOTO
            // =================================================

            arquivoFoto.write(
                    arquivo.getAbsolutePath()
            );

            System.out.println(
                    "Foto salva em:"
            );

            System.out.println(
                    arquivo.getAbsolutePath()
            );
        }

        // =================================================
        // CRIAR USUÁRIO
        // =================================================

        Usuario usuario =
                new Usuario();

        usuario.setNome(nome);

        usuario.setUsername(username);

        usuario.setEmail(email);

        usuario.setSenha(senha);

        usuario.setFoto(nomeFoto);

        usuario.setBio(bio);

        usuario.setDataNascimento(
                dataNascimento
        );

        usuario.setPais(pais);

        usuario.setPlataformaFavorita(
                plataforma
        );

        // =================================================
        // GERAR CÓDIGO
        // =================================================

        String codigo =
                gerarCodigo();

        // =================================================
        // EXPIRA EM 10 MINUTOS
        // =================================================

        String expiraEm =
                LocalDateTime
                        .now()
                        .plusMinutes(10)
                        .format(
                                FORMATO_DATA
                        );

        System.out.println(
                "Código de verificação gerado."
        );

        // =================================================
        // SALVAR CADASTRO PENDENTE
        // =================================================

        boolean salvo =
                usuarioDAO.salvarCadastroPendente(
                        usuario,
                        codigo,
                        expiraEm
                );

        if (!salvo) {

            mostrarErro(
                    response,
                    "Não foi possível iniciar o cadastro."
            );

            return;
        }

        // =================================================
        // ENVIAR E-MAIL
        // =================================================

        try {

            enviarCodigoEmail(
                    email,
                    nome,
                    codigo
            );

            System.out.println(
                    "E-MAIL DE VERIFICAÇÃO ENVIADO!"
            );

            mostrarTelaVerificacao(
                    request,
                    response,
                    email
            );

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO ENVIAR E-MAIL:"
            );

            e.printStackTrace();

            mostrarErro(
                    response,
                    "Não foi possível enviar o "
                    + "e-mail de verificação."
            );
        }
    }

    // =====================================================
    // GERAR CÓDIGO
    // =====================================================

    private String gerarCodigo() {

        int numero =
                100000
                + RANDOM.nextInt(900000);

        return String.valueOf(numero);
    }

    // =====================================================
    // ENVIAR E-MAIL
    // =====================================================

    private void enviarCodigoEmail(
            String emailDestino,
            String nome,
            String codigo)
            throws Exception {

        // =================================================
        // VARIÁVEIS
        // =================================================

        String host =
                System.getenv("MAIL_HOST");

        String port =
                System.getenv("MAIL_PORT");

        String usuario =
                System.getenv("MAIL_USER");

        String senha =
                System.getenv("MAIL_PASSWORD");

        if (host == null ||
                host.trim().isEmpty()
                ||
                port == null ||
                port.trim().isEmpty()
                ||
                usuario == null ||
                usuario.trim().isEmpty()
                ||
                senha == null ||
                senha.trim().isEmpty()) {

            throw new Exception(
                    "As variáveis MAIL_HOST, MAIL_PORT, "
                    + "MAIL_USER e MAIL_PASSWORD "
                    + "não estão configuradas."
            );
        }

        // =================================================
        // SMTP
        // =================================================

        Properties props =
                new Properties();

        props.put(
                "mail.smtp.auth",
                "true"
        );

        props.put(
                "mail.smtp.starttls.enable",
                "true"
        );

        props.put(
                "mail.smtp.starttls.required",
                "true"
        );

        props.put(
                "mail.smtp.host",
                host
        );

        props.put(
                "mail.smtp.port",
                port
        );

        // =================================================
        // SESSÃO
        // =================================================

        Session session =
                Session.getInstance(
                        props,
                        new Authenticator() {

                            @Override
                            protected PasswordAuthentication
                            getPasswordAuthentication() {

                                return new PasswordAuthentication(
                                        usuario,
                                        senha
                                );
                            }
                        }
                );

        // =================================================
        // MENSAGEM
        // =================================================

        Message mensagem =
                new MimeMessage(session);

        mensagem.setFrom(
                new InternetAddress(
                        usuario,
                        "Inventory"
                )
        );

        mensagem.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(
                        emailDestino
                )
        );

        mensagem.setSubject(
                "Código de verificação - Inventory"
        );

        String texto =
                "Olá, "
                + nome
                + "!\n\n"
                + "Seu código de verificação "
                + "do Inventory é:\n\n"
                + codigo
                + "\n\n"
                + "Esse código é válido por "
                + "10 minutos.\n\n"
                + "Se você não solicitou esse "
                + "cadastro, ignore este e-mail.\n\n"
                + "Inventory";

        // =================================================
        // CORREÇÃO PRINCIPAL
        // =================================================

        mensagem.setText(
                texto
        );

        // =================================================
        // ENVIAR
        // =================================================

        Transport.send(
                mensagem
        );
    }

    // =====================================================
    // TELA DE VERIFICAÇÃO
    // =====================================================

    private void mostrarTelaVerificacao(
            HttpServletRequest request,
            HttpServletResponse response,
            String email)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        String emailSeguro =
                escaparHTML(email);

        String contexto =
                request.getContextPath();

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<!DOCTYPE html>"
        );

        html.append(
                "<html lang='pt-BR'>"
        );

        html.append(
                "<head>"
        );

        html.append(
                "<meta charset='UTF-8'>"
        );

        html.append(
                "<meta name='viewport' "
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
        );

        html.append(
                "<title>Verificar e-mail - Inventory</title>"
        );

        html.append(
                "<link href='https://fonts.googleapis.com/css2?"
                + "family=Rajdhani:wght@400;500;600;700&display=swap' "
                + "rel='stylesheet'>"
        );

        html.append(
                "<style>"
                + "body{"
                + "margin:0;"
                + "min-height:100vh;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "font-family:'Rajdhani',sans-serif;"
                + "background:"
                + "linear-gradient("
                + "135deg,#0d0714,#1d0b2d,#0d0714"
                + ");"
                + "color:white;"
                + "}"

                + ".box{"
                + "width:420px;"
                + "max-width:90%;"
                + "background:#181020;"
                + "border:1px solid #4b2370;"
                + "border-radius:18px;"
                + "padding:35px;"
                + "text-align:center;"
                + "box-shadow:"
                + "0 20px 60px rgba(0,0,0,.45);"
                + "}"

                + "h1{"
                + "font-size:30px;"
                + "margin-bottom:10px;"
                + "color:#c084fc;"
                + "font-family:'Rajdhani',sans-serif;"
                + "}"

                + "p{"
                + "color:#aaa;"
                + "line-height:1.5;"
                + "}"

                + ".email{"
                + "color:#c084fc;"
                + "font-weight:bold;"
                + "word-break:break-word;"
                + "}"

                + "input{"
                + "width:100%;"
                + "box-sizing:border-box;"
                + "padding:15px;"
                + "margin-top:18px;"
                + "background:#0f0a14;"
                + "border:1px solid #4b315a;"
                + "border-radius:9px;"
                + "color:white;"
                + "font-size:24px;"
                + "text-align:center;"
                + "letter-spacing:8px;"
                + "}"

                + "button{"
                + "width:100%;"
                + "padding:14px;"
                + "margin-top:18px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#a855f7"
                + ");"
                + "border:none;"
                + "border-radius:9px;"
                + "color:white;"
                + "font-size:16px;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"

                + ".voltar{"
                + "display:block;"
                + "margin-top:18px;"
                + "color:#aaa;"
                + "text-decoration:none;"
                + "}"

                + "</style>"
        );

        html.append(
                "</head>"
        );

        html.append(
                "<body>"
        );

        html.append(
                "<div class='box'>"
        );

        html.append(
                "<h1>Verificar e-mail</h1>"
        );

        html.append(
                "<p>"
                + "Enviamos um código de 6 dígitos "
                + "para:"
                + "</p>"
        );

        html.append(
                "<p class='email'>"
                + emailSeguro
                + "</p>"
        );

        html.append(
                "<form method='POST' "
                + "action='"
                + contexto
                + "/verificar-email'>"
        );

        html.append(
                "<input "
                + "type='hidden' "
                + "name='email' "
                + "value='"
                + emailSeguro
                + "'>"
        );

        html.append(
                "<input "
                + "type='text' "
                + "name='codigo' "
                + "maxlength='6' "
                + "pattern='[0-9]{6}' "
                + "inputmode='numeric' "
                + "placeholder='000000' "
                + "required>"
        );

        html.append(
                "<button type='submit'>"
                + "Verificar e-mail"
                + "</button>"
        );

        html.append(
                "</form>"
        );

        html.append(
                "<a class='voltar' "
                + "href='cadastro.html'>"
                + "Voltar"
                + "</a>"
        );

        html.append(
                "</div>"
        );

        html.append(
                "</body>"
        );

        html.append(
                "</html>"
        );

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // ERRO
    // =====================================================

    private void mostrarErro(
            HttpServletResponse response,
            String mensagem)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        response.getWriter().println(
                "<!DOCTYPE html>"
                + "<html lang='pt-BR'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<title>Cadastro - Inventory</title>"
                + "<link href='https://fonts.googleapis.com/css2?"
                + "family=Rajdhani:wght@400;500;600;700&display=swap' "
                + "rel='stylesheet'>"
                + "<style>"
                + "body{"
                + "background:#100814;"
                + "color:white;"
                + "font-family:'Rajdhani',sans-serif;"
                + "display:flex;"
                + "justify-content:center;"
                + "align-items:center;"
                + "min-height:100vh;"
                + "}"
                + ".box{"
                + "background:#1d1226;"
                + "padding:35px;"
                + "border-radius:15px;"
                + "text-align:center;"
                + "max-width:500px;"
                + "}"
                + "h2{"
                + "color:#c084fc;"
                + "font-family:'Rajdhani',sans-serif;"
                + "}"
                + "p{"
                + "color:#aaa;"
                + "line-height:1.5;"
                + "}"
                + "a{"
                + "color:#a855f7;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='box'>"
                + "<h2>Não foi possível continuar</h2>"
                + "<p>"
                + escaparHTML(mensagem)
                + "</p>"
                + "<a href='cadastro.html'>"
                + "Voltar para cadastro"
                + "</a>"
                + "</div>"
                + "</body>"
                + "</html>"
        );
    }

    // =====================================================
    // LIMPAR
    // =====================================================

    private String limpar(
            String texto) {

        if (texto == null) {

            return "";
        }

        return texto.trim();
    }

    // =====================================================
    // VALIDAR E-MAIL
    // =====================================================

    private boolean emailValido(
            String email) {

        return email != null
                && email.matches(
                        "^[A-Za-z0-9+_.-]+@"
                        + "[A-Za-z0-9.-]+$"
                );
    }

    // =====================================================
    // ESCAPAR HTML
    // =====================================================

    private String escaparHTML(
            String texto) {

        if (texto == null) {

            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}