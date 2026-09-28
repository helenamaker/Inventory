package controller;

import dao.CriarBanco;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // ATUALIZAR / CRIAR BANCO
        // =====================================================

        try {

            CriarBanco.criarTabela();

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "BANCO VERIFICADO PELO HOME!"
            );

            System.out.println(
                    "================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO INICIALIZAR BANCO:"
            );

            e.printStackTrace();
        }

        // =====================================================
        // VERIFICAR LOGIN
        // =====================================================

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");

            return;
        }

        // =====================================================
        // IR PARA PÁGINA INICIAL
        // =====================================================

        response.sendRedirect("index.html");
    }
}