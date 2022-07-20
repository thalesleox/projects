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
public class VipsController implements Initializable
{

    public static final String FXML = "Vips.fxml";
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private TableColumn<?, ?> col_datacadastro;
    @FXML
    private ImageView foto;
    @FXML
    private TableView<vips> tb_vips;
    @FXML
    private TableColumn<?, ?> col_mesvip;

    public class vips
    {

        private String nome;
        private String telefone;
        private String datacadastro;
        private String mesvip;
        private BufferedImage foto;

        public vips(String nome, String telefone, String datacadastro, String mesvip, BufferedImage foto)
        {
            this.nome = nome;
            this.telefone = telefone;
            this.datacadastro = datacadastro;
            this.mesvip = mesvip;
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

        public String getDatacadastro()
        {
            return datacadastro;
        }

        public void setDatacadastro(String datacadastro)
        {
            this.datacadastro = datacadastro;
        }

        public String getMesvip()
        {
            return mesvip;
        }

        public void setMesvip(String mesvip)
        {
            this.mesvip = mesvip;
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_datacadastro.setCellValueFactory(new PropertyValueFactory<>("datacadastro"));
        col_mesvip.setCellValueFactory(new PropertyValueFactory<>("mesvip"));

        String sql = "select nome,telefone,datacadastro,mesvip,foto from pessoa p where sit_cod = 4 order by mesvip desc";

        ResultSet rs = Conexao.get().consultar(sql);
        ObservableList<vips> ob = FXCollections.observableArrayList();
        try
        {
            CtrPessoa ctrpes = new CtrPessoa();
            while (rs.next())
                ob.add(new vips(rs.getString("nome"), rs.getString("telefone"), rs.getString("datacadastro"), rs.getString("mesvip"),
                        ctrpes.obterImagem(rs.getBytes("foto"))));
        } catch (SQLException ex)
        {
            Logger.getLogger(PendentesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ob.size() > 0)
            tb_vips.setItems(ob);
    }

    @FXML
    private void evt_selecionar(MouseEvent event)
    {
        vips in = (vips) tb_vips.getSelectionModel().getSelectedItem();
        if (in != null)
            foto.setImage(in.getFoto() != null ? SwingFXUtils.toFXImage(in.getFoto(), null) : null);
    }

}
