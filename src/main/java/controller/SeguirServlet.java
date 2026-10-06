package controller;

import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/seguir")
public class SeguirServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        try {

            Usuario logado =
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

            int idSeguidor =
                    logado.getId();

            int idSeguido =
                    Integer.parseInt(
                            request.getParameter(
                                    "idUsuario"
                            )
                    );

            String acao =
                    request.getParameter("acao");

            UsuarioDAO dao =
                    new UsuarioDAO();

            if (idSeguidor == idSeguido) {

                response.sendRedirect(
                        "perfil"
                );

                return;
            }

            if ("deixar".equals(acao)) {

                dao.deixarDeSeguir(
                        idSeguidor,
                        idSeguido
                );

            } else {

                dao.seguir(
                        idSeguidor,
                        idSeguido
                );
            }

            response.sendRedirect(
                    "perfil-usuario?id="
                    + idSeguido
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "buscar-usuarios"
            );
        }
    }
}