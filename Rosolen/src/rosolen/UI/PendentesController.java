/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.util.Conexao;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class PendentesController implements Initializable
{

    public static final String FXML = "Pendentes.fxml";

    @FXML
    private TableView<Inadimplente> tb_inadimplentes;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_cpf;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private TableColumn<?, ?> col_email;
    @FXML
    private TableColumn<?, ?> col_datamens;
    @FXML
    private TextField txt_nome;
    @FXML
    private ImageView foto;
    @FXML
    private RadioButton rbMes;
    @FXML
    private RadioButton rbGeral;
    @FXML
    private ToggleGroup tipoPendentes;
    @FXML
    private ToggleGroup tipoAlunos;

    public class Inadimplente
    {

        private String nome;
        private String cpf;
        private String telefone;
        private String email;
        private BufferedImage foto;
        private String datamens;

        public Inadimplente(String nome, String cpf, String telefone, String email, BufferedImage foto, String datamens)
        {
            this.nome = nome;
            this.cpf = cpf;
            this.telefone = telefone;
            this.email = email;
            this.foto = foto;
            this.datamens = datamens;
        }

        public String getNome()
        {
            return nome;
        }

        public String getCpf()
        {
            return cpf;
        }

        public String getTelefone()
        {
            return telefone;
        }

        public String getEmail()
        {
            return email;
        }

        public BufferedImage getFoto()
        {
            return foto;
        }

        public String getDatamens()
        {
            return datamens;
        }

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_datamens.setCellValueFactory(new PropertyValueFactory<>("datamens"));

        txt_nome.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                buscarPessoa(txt_nome.getText());
        });

        rbMes.setOnAction((event) ->
        {
            buscarPessoa("select nome,cpf,telefone,email,foto,datamens from pessoa pes "
                    + "inner join (select pes_cod, max(datamens) as datamens from lancamento where mensalidade = true group by pes_cod) lan "
                    + "on pes.pes_cod = lan.pes_cod where pes.sit_cod = 2 "
                    + "and nome ilike '%" + txt_nome.getText() + "%' "
                    + "and extract(month from lan.datamens) = extract(month from (current_date - interval '1 month')) "
                    + "and extract(day from lan.datamens) <= extract(day from current_date)"
                    + "order by datamens");
        });

        rbGeral.setOnAction((event) ->
        {
            buscarPessoa("select nome,cpf,telefone,email,foto,datamens from pessoa pes "
                    + "inner join (select pes_cod, max(datamens) as datamens from lancamento where mensalidade = true group by pes_cod) lan "
                    + "on pes.pes_cod = lan.pes_cod where pes.sit_cod = 2 and nome ilike '%" + txt_nome.getText() + "%' order by datamens");
        });
    }

    @FXML
    private void evt_selecionar(MouseEvent event)
    {
        Inadimplente in = (Inadimplente) tb_inadimplentes.getSelectionModel().getSelectedItem();
        if (in != null)
            foto.setImage(in.getFoto() != null ? SwingFXUtils.toFXImage(in.getFoto(), null) : null);
    }

    private void buscarPessoa(String sql)
    {
        ResultSet rs = Conexao.get().consultar(sql);
        ObservableList<Inadimplente> ob = FXCollections.observableArrayList();
        try
        {
            CtrPessoa ctrpes = new CtrPessoa();
            while (rs.next())
                ob.add(new Inadimplente(rs.getString("nome"), rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"),
                        ctrpes.obterImagem(rs.getBytes("foto")), rs.getTimestamp("datamens").toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        } catch (SQLException ex)
        {
            Logger.getLogger(PendentesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ob.size() > 0)
            tb_inadimplentes.setItems(ob);
    }
}
