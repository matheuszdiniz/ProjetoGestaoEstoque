import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ControleEstoque extends JFrame {
    private static final String DB_URL = "jdbc:sqlite:estoque.db";
    private Database database;
    private JTable table;
    private DefaultTableModel tableModel;

    public ControleEstoque() {
        try {
            database = new Database(DB_URL);
            configurarInterface();
            listarProdutos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private void configurarInterface() {
        setTitle("Controle de Estoque");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new Object[]{"Nome", "Quantidade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton adicionarButton = new JButton("Adicionar Produto");
        JButton alterarButton = new JButton("Alterar Produto");
        JButton excluirButton = new JButton("Excluir Produto");
        JButton listarButton = new JButton("Listar Produtos");

        JPanel panel = new JPanel();
        panel.add(adicionarButton);
        panel.add(alterarButton);
        panel.add(excluirButton);
        panel.add(listarButton);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarProduto();
            }
        });

        alterarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarProduto();
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProdutos();
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    private void adicionarProduto() {
        String nome = JOptionPane.showInputDialog(this, "Nome do produto:");
        if (nome == null || nome.isEmpty()) return;

        String quantidadeStr = JOptionPane.showInputDialog(this, "Quantidade:");
        if (quantidadeStr == null || quantidadeStr.isEmpty()) return;

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            return;
        }

        try {
            database.addProduto(new Produto(nome, quantidade));
            listarProdutos();
            JOptionPane.showMessageDialog(this, "Produto adicionado.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar produto: " + e.getMessage());
        }
    }

    private void alterarProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para alterar.");
            return;
        }

        String nome = tableModel.getValueAt(selectedRow, 0).toString();
        String quantidadeStr = JOptionPane.showInputDialog(this, "Nova quantidade:");
        if (quantidadeStr == null || quantidadeStr.isEmpty()) return;

        int novaQuantidade;
        try {
            novaQuantidade = Integer.parseInt(quantidadeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            return;
        }

        try {
            database.updateProduto(new Produto(nome, novaQuantidade));
            listarProdutos();
            JOptionPane.showMessageDialog(this, "Produto alterado.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar produto: " + e.getMessage());
        }
    }

    private void excluirProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
            return;
        }

        String nome = tableModel.getValueAt(selectedRow, 0).toString();

        try {
            database.deleteProduto(nome);
            listarProdutos();
            JOptionPane.showMessageDialog(this, "Produto excluído.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + e.getMessage());
        }
    }

    private void listarProdutos() {
        tableModel.setRowCount(0); // Limpa a tabela
        try {
            List<Produto> produtos = database.getAllProdutos();
            for (Produto produto : produtos) {
                tableModel.addRow(new Object[]{produto.getNome(), produto.getQuantidade()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar produtos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControleEstoque controle = new ControleEstoque();
            controle.setVisible(true);
        });
    }
}
try {
    Class.forName("org.sqlite.JDBC");
} catch (ClassNotFoundException e) {
    e.printStackTrace();
}
