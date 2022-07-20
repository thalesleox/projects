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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.util.Conexao;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class AniversariantesController implements Initializable
{

    public static final String FXML = "Aniversariantes.fxml";
    @FXML
    private TableView<Aniversariante> tb_aniversariantes;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private ImageView foto;
    @FXML
    private TableColumn<?, ?> col_idade;
    @FXML
    private TableColumn<?, ?> col_data;

    public class Aniversariante
    {

        private String nome;
        private String telefone;
        private String data;
        private String Idade;
        private BufferedImage foto;

        public Aniversariante(String nome, String telefone, LocalDate datanascimento, BufferedImage foto)
        {
            this.nome = nome;
            this.telefone = telefone;
            this.data = datanascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int aux = LocalDate.now().getYear() - datanascimento.getYear();
            this.Idade = aux + " -> " + (aux + 1);
            this.foto = foto;
        }

        public String getNome()
        {
            return nome;
        }

        public void setNome(String nome)
        {
            this.nome = nome;
        }

        public String getTelefone()
        {
            return telefone;
        }

        public void setTelefone(String telefone)
        {
            this.telefone = telefone;
        }

        public String getIdade()
        {
            return Idade;
        }

        public void setIdade(String Idade)
        {
            this.Idade = Idade;
        }

        public String getData()
        {
            return data;
        }

        public void setData(String data)
        {
            this.data = data;
        }

        public BufferedImage getFoto()
        {
            return foto;
        }

        public void setFoto(BufferedImage foto)
        {
            this.foto = foto;
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
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_data.setCellValueFactory(new PropertyValueFactory<>("data"));
        col_idade.setCellValueFactory(new PropertyValueFactory<>("idade"));

        String sql = "select * from pessoa p "
                + "where extract(month from p.datanascimento) = extract(month from current_date) "
                + "order by extract(day from p.datanascimento)";

        ResultSet rs = Conexao.get().consultar(sql);
        ObservableList<Aniversariante> ob = FXCollections.observableArrayList();
        try
        {
            CtrPessoa ctrpes = new CtrPessoa();
            while (rs.next())
                ob.add(new Aniversariante(rs.getString("nome"), rs.getString("telefone"), rs.getTimestamp("datanascimento").toLocalDateTime().toLocalDate(),
                        ctrpes.obterImagem(rs.getBytes("foto"))));
        } catch (SQLException ex)
        {
            Logger.getLogger(PendentesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ob.size() > 0)
            tb_aniversariantes.setItems(ob);
    }

    @FXML
    private void evt_selecionar(MouseEvent event)
    {
        Aniversariante in = (Aniversariante) tb_aniversariantes.getSelectionModel().getSelectedItem();
        if (in != null)
            foto.setImage(in.getFoto() != null ? SwingFXUtils.toFXImage(in.getFoto(), null) : null);
    }

}
