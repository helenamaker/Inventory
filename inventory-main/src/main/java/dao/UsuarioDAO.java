package dao;

import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class UsuarioDAO {

    // =====================================================
    // CADASTRAR USUARIO
    // =====================================================

    public boolean cadastrar(Usuario usuario) {

        String sql =
                "INSERT INTO usuario "
                + "(nome, username, email, senha, foto, bio, "
                + "data_nascimento, pais, plataforma_favorita) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            System.out.println("==============================");
            System.out.println("TENTANDO CADASTRAR USUARIO");
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Username: " + usuario.getUsername());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("==============================");

            conexao = Conexao.conectar();

            if (conexao == null) {

                System.out.println(
                        "ERRO: CONEXAO COM BANCO NULL!"
                );

                return false;
            }

            stmt = conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    usuario.getNome()
            );

            stmt.setString(
                    2,
                    usuario.getUsername()
            );

            stmt.setString(
                    3,
                    usuario.getEmail()
            );

            stmt.setString(
                    4,
                    usuario.getSenha()
            );

            stmt.setString(
                    5,
                    usuario.getFoto()
            );

            stmt.setString(
                    6,
                    usuario.getBio()
            );

            stmt.setString(
                    7,
                    usuario.getDataNascimento()
            );

            stmt.setString(
                    8,
                    usuario.getPais()
            );

            stmt.setString(
                    9,
                    usuario.getPlataformaFavorita()
            );

            int resultado =
                    stmt.executeUpdate();

            System.out.println("==============================");
            System.out.println(
                    "USUARIO INSERIDO NO BANCO!"
            );
            System.out.println(
                    "RESULTADO: " + resultado
            );
            System.out.println("==============================");

            return resultado > 0;

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO REAL AO CADASTRAR");
            System.out.println("==============================");

            e.printStackTrace();

            System.out.println("==============================");

            return false;

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public Usuario login(
            String email,
            String senha) {

        String sql =
                "SELECT * FROM usuario "
                + "WHERE email = ? "
                + "AND senha = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return null;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            stmt.setString(
                    2,
                    senha
            );

            resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                // ---------------------------------------------------
                // Contas criadas via "Entrar com Google" recebem a
                // senha-sentinela "GOOGLE_LOGIN" (ver
                // GoogleCallbackServlet.criarUsuarioGoogle). Essas
                // contas não têm senha própria e NUNCA podem ser
                // autenticadas por este formulário, senão qualquer
                // pessoa poderia entrar digitando literalmente
                // "GOOGLE_LOGIN" como senha.
                // ---------------------------------------------------

                String senhaSalva =
                        resultado.getString("senha");

                if ("GOOGLE_LOGIN".equals(senhaSalva)) {
                    return null;
                }

                return criarUsuario(
                        resultado
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "=============================="
            );

            System.out.println(
                    "ERRO NO LOGIN:"
            );

            e.printStackTrace();

            System.out.println(
                    "=============================="
            );

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public Usuario buscarPorId(
            int id) {

        String sql =
                "SELECT * "
                + "FROM usuario "
                + "WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return null;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    id
            );

            resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                return criarUsuario(
                        resultado
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR USUARIO:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR USERNAME
    // =====================================================

    public Usuario buscarPorUsername(
            String username) {

        String sql =
                "SELECT * "
                + "FROM usuario "
                + "WHERE username = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return null;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    username
            );

            resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                return criarUsuario(
                        resultado
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR USERNAME:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR EMAIL
    // =====================================================

    public Usuario buscarPorEmail(
            String email) {

        String sql =
                "SELECT * "
                + "FROM usuario "
                + "WHERE email = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return null;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                return criarUsuario(
                        resultado
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR EMAIL:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // =====================================================
    // BUSCAR USERNAME PARCIAL
    // =====================================================

    public ArrayList<Usuario>
            buscarPorUsernameParcial(
                    String username) {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT * "
                + "FROM usuario "
                + "WHERE username LIKE ? "
                + "ORDER BY username";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return usuarios;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    "%" + username + "%"
            );

            resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                usuarios.add(
                        criarUsuario(
                                resultado
                        )
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR USERNAMES:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return usuarios;
    }

    // =====================================================
    // BUSCAR POR NOME
    // =====================================================

    public ArrayList<Usuario>
            buscarPorNome(
                    String nome) {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT * "
                + "FROM usuario "
                + "WHERE nome LIKE ? "
                + "ORDER BY nome";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return usuarios;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    "%" + nome + "%"
            );

            resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                usuarios.add(
                        criarUsuario(
                                resultado
                        )
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR NOMES:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return usuarios;
    }

    // =====================================================
    // LISTAR USUARIOS
    // =====================================================

    public ArrayList<Usuario> listar() {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT * FROM usuario";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return usuarios;
            }

            stmt =
                    conexao.prepareStatement(sql);

            resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                usuarios.add(
                        criarUsuario(
                                resultado
                        )
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO LISTAR USUARIOS:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return usuarios;
    }

    // =====================================================
    // ATUALIZAR USUARIO
    // =====================================================

    public boolean atualizar(
            Usuario usuario) {

        String sql =
                "UPDATE usuario SET "
                + "nome = ?, "
                + "username = ?, "
                + "email = ?, "
                + "senha = ?, "
                + "foto = ?, "
                + "bio = ?, "
                + "data_nascimento = ?, "
                + "pais = ?, "
                + "plataforma_favorita = ? "
                + "WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    usuario.getNome()
            );

            stmt.setString(
                    2,
                    usuario.getUsername()
            );

            stmt.setString(
                    3,
                    usuario.getEmail()
            );

            stmt.setString(
                    4,
                    usuario.getSenha()
            );

            stmt.setString(
                    5,
                    usuario.getFoto()
            );

            stmt.setString(
                    6,
                    usuario.getBio()
            );

            stmt.setString(
                    7,
                    usuario.getDataNascimento()
            );

            stmt.setString(
                    8,
                    usuario.getPais()
            );

            stmt.setString(
                    9,
                    usuario.getPlataformaFavorita()
            );

            stmt.setInt(
                    10,
                    usuario.getId()
            );

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO ATUALIZAR USUARIO:"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // EXCLUIR USUARIO
    // =====================================================

    public boolean excluir(
            int id) {

        String sql =
                "DELETE FROM usuario "
                + "WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    id
            );

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO EXCLUIR USUARIO:"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // SALVAR CADASTRO PENDENTE
    // =====================================================

    public boolean salvarCadastroPendente(
            Usuario usuario,
            String codigo,
            String expiraEm) {

        String sql =
                "INSERT INTO cadastro_pendente "
                + "(nome, username, email, senha, foto, bio, "
                + "data_nascimento, pais, plataforma_favorita, "
                + "codigo, expira_em) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(email) DO UPDATE SET "
                + "nome = excluded.nome, "
                + "username = excluded.username, "
                + "senha = excluded.senha, "
                + "foto = excluded.foto, "
                + "bio = excluded.bio, "
                + "data_nascimento = excluded.data_nascimento, "
                + "pais = excluded.pais, "
                + "plataforma_favorita = excluded.plataforma_favorita, "
                + "codigo = excluded.codigo, "
                + "expira_em = excluded.expira_em";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getUsername());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getFoto());
            stmt.setString(6, usuario.getBio());
            stmt.setString(7, usuario.getDataNascimento());
            stmt.setString(8, usuario.getPais());
            stmt.setString(9, usuario.getPlataformaFavorita());
            stmt.setString(10, codigo);
            stmt.setString(11, expiraEm);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO SALVAR CADASTRO PENDENTE:"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // VERIFICAR CADASTRO PENDENTE
    // =====================================================

    public boolean existeCadastroPendente(
            String email) {

        String sql =
                "SELECT id "
                + "FROM cadastro_pendente "
                + "WHERE email = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            rs =
                    stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // CONFIRMAR EMAIL
    // =====================================================

    public Usuario confirmarEmail(
            String email,
            String codigo) {

        String sql =
                "SELECT * "
                + "FROM cadastro_pendente "
                + "WHERE email = ? "
                + "AND codigo = ? "
                + "AND expira_em > CURRENT_TIMESTAMP";

        Connection conexao = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtInserir = null;
        PreparedStatement stmtApagar = null;
        ResultSet rs = null;
        ResultSet chaves = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return null;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            stmt.setString(
                    2,
                    codigo
            );

            rs =
                    stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Usuario usuario =
                    new Usuario();

            usuario.setNome(
                    rs.getString("nome")
            );

            usuario.setUsername(
                    rs.getString("username")
            );

            usuario.setEmail(
                    rs.getString("email")
            );

            usuario.setSenha(
                    rs.getString("senha")
            );

            usuario.setFoto(
                    rs.getString("foto")
            );

            usuario.setBio(
                    rs.getString("bio")
            );

            usuario.setDataNascimento(
                    rs.getString("data_nascimento")
            );

            usuario.setPais(
                    rs.getString("pais")
            );

            usuario.setPlataformaFavorita(
                    rs.getString("plataforma_favorita")
            );

            String inserir =
                    "INSERT INTO usuario "
                    + "(nome, username, email, senha, foto, bio, "
                    + "data_nascimento, pais, plataforma_favorita) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmtInserir =
                    conexao.prepareStatement(
                            inserir,
                            java.sql.Statement.RETURN_GENERATED_KEYS
                    );

            stmtInserir.setString(1, usuario.getNome());
            stmtInserir.setString(2, usuario.getUsername());
            stmtInserir.setString(3, usuario.getEmail());
            stmtInserir.setString(4, usuario.getSenha());
            stmtInserir.setString(5, usuario.getFoto());
            stmtInserir.setString(6, usuario.getBio());
            stmtInserir.setString(7, usuario.getDataNascimento());
            stmtInserir.setString(8, usuario.getPais());
            stmtInserir.setString(9, usuario.getPlataformaFavorita());

            stmtInserir.executeUpdate();

            chaves =
                    stmtInserir.getGeneratedKeys();

            if (chaves.next()) {

                usuario.setId(
                        chaves.getInt(1)
                );
            }

            String apagar =
                    "DELETE FROM cadastro_pendente "
                    + "WHERE email = ?";

            stmtApagar =
                    conexao.prepareStatement(
                            apagar
                    );

            stmtApagar.setString(
                    1,
                    email
            );

            stmtApagar.executeUpdate();

            return usuario;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO CONFIRMAR EMAIL:"
            );

            e.printStackTrace();

            return null;

        } finally {

            try {

                if (chaves != null) {
                    chaves.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmtApagar != null) {
                    stmtApagar.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmtInserir != null) {
                    stmtInserir.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // SEGUIR USUARIO
    // =====================================================

    public boolean seguir(
            int idSeguidor,
            int idSeguido) {

        if (idSeguidor == idSeguido) {
            return false;
        }

        String sql =
                "INSERT OR IGNORE INTO seguidor "
                + "(id_seguidor, id_seguido) "
                + "VALUES (?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idSeguidor
            );

            stmt.setInt(
                    2,
                    idSeguido
            );

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO SEGUIR USUARIO:"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // DEIXAR DE SEGUIR
    // =====================================================

    public boolean deixarDeSeguir(
            int idSeguidor,
            int idSeguido) {

        String sql =
                "DELETE FROM seguidor "
                + "WHERE id_seguidor = ? "
                + "AND id_seguido = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idSeguidor
            );

            stmt.setInt(
                    2,
                    idSeguido
            );

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO DEIXAR DE SEGUIR:"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // VERIFICAR SE SEGUE
    // =====================================================

    public boolean seguindo(
            int idSeguidor,
            int idSeguido) {

        String sql =
                "SELECT id "
                + "FROM seguidor "
                + "WHERE id_seguidor = ? "
                + "AND id_seguido = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idSeguidor
            );

            stmt.setInt(
                    2,
                    idSeguido
            );

            rs =
                    stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO VERIFICAR SEGUIMENTO:"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // CONTAR SEGUIDORES
    // =====================================================

    public int contarSeguidores(
            int idUsuario) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM seguidor "
                + "WHERE id_seguido = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return 0;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO CONTAR SEGUIDORES:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    // =====================================================
    // CONTAR QUANTOS SEGUE
    // =====================================================

    public int contarSeguindo(
            int idUsuario) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM seguidor "
                + "WHERE id_seguidor = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return 0;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO CONTAR SEGUINDO:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    // =====================================================
    // LISTAR SEGUIDORES
    // =====================================================

    public ArrayList<Usuario>
            listarSeguidores(
                    int idUsuario) {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT u.* "
                + "FROM usuario u "
                + "INNER JOIN seguidor s "
                + "ON u.id = s.id_seguidor "
                + "WHERE s.id_seguido = ? "
                + "ORDER BY u.username";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return usuarios;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                usuarios.add(
                        criarUsuario(rs)
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO LISTAR SEGUIDORES:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return usuarios;
    }

    // =====================================================
    // LISTAR QUEM O USUARIO SEGUE
    // =====================================================

    public ArrayList<Usuario>
            listarSeguindo(
                    int idUsuario) {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT u.* "
                + "FROM usuario u "
                + "INNER JOIN seguidor s "
                + "ON u.id = s.id_seguido "
                + "WHERE s.id_seguidor = ? "
                + "ORDER BY u.username";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return usuarios;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                usuarios.add(
                        criarUsuario(rs)
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO LISTAR SEGUINDO:"
            );

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return usuarios;
    }

    // =====================================================
    // CRIAR OBJETO USUARIO
    // =====================================================

    private Usuario criarUsuario(
            ResultSet resultado)
            throws Exception {

        Usuario usuario =
                new Usuario();

        usuario.setId(
                resultado.getInt("id")
        );

        usuario.setNome(
                resultado.getString("nome")
        );

        usuario.setUsername(
                resultado.getString("username")
        );

        usuario.setEmail(
                resultado.getString("email")
        );

        usuario.setSenha(
                resultado.getString("senha")
        );

        usuario.setFoto(
                resultado.getString("foto")
        );

        usuario.setBio(
                resultado.getString("bio")
        );

        usuario.setDataNascimento(
                resultado.getString(
                        "data_nascimento"
                )
        );

        usuario.setPais(
                resultado.getString("pais")
        );

        usuario.setPlataformaFavorita(
                resultado.getString(
                        "plataforma_favorita"
                )
        );

        return usuario;
    }
}