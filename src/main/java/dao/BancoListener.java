package dao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Inicializa o banco uma única vez quando a aplicação sobe. */
@WebListener
public class BancoListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        CriarBanco.criarTabela();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
