/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.UI;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import mlanches.db.controladoras.CtrPedido;
import mlanches.db.controladoras.CtrPessoa;
import mlanches.db.controladoras.CtrProduto;
import mlanches.db.entidades.Mesa;
import mlanches.db.entidades.Pedido;
import mlanches.db.entidades.Pessoa;
import mlanches.db.entidades.Produto;
import mlanches.db.util.Conexao;
import mlanches.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class RealizarPedidoController implements Initializable
{

    public static final String FXML = "RealizarPedido.fxml";

    @FXML
    private VBox todo;
    @FXML
    private TextField txt_cliente;
    @FXML
    private TableView<Pessoa> tabela_cliente;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private TableColumn<?, ?> col_endereco;
    @FXML
    private Button bt_ok_cliente;
    @FXML
    private VBox vb_pedido;
    @FXML
    private TextField txt_produto;
    @FXML
    private Button bt_ok_produto;
    @FXML
    private TableView<Produto> tabela_produtos;
    @FXML
    private TableColumn<?, ?> col_prod_descricao;
    @FXML
    private TableColumn<?, ?> col_prod_valor;
    @FXML
    private CheckBox chk_bacon;
    @FXML
    private CheckBox chk_calabresa;
    @FXML
    private CheckBox chk_ovo;
    @FXML
    private CheckBox chk_cebola;
    @FXML
    private CheckBox chk_milho;
    @FXML
    private CheckBox chk_salsicha;
    @FXML
    private TextField txt_quantidade;
    @FXML
    private Button bt_add;
    @FXML
    private TextField txt_obsProd;
    @FXML
    private TableView<produto> tabela_pedido;
    @FXML
    private TableColumn<?, ?> col_ped_descricao;
    @FXML
    private TableColumn<?, ?> col_ped_valor;
    @FXML
    private Button bt_remove;
    @FXML
    private TextField txt_obsGeral;
    @FXML
    private VBox vb_cliente;
    @FXML
    private CheckBox chk_hamburguer;
    @FXML
    private Label lb_totalProduto;
    @FXML
    private Label lb_totalPedido;
    private Pessoa pes_selec = null;
    private int mesa;
    private Produto prod_selec = null;
    @FXML
    private AnchorPane pn_chk;
    @FXML
    private VBox vb_acrescimos;
    @FXML
    private TableColumn<?, ?> col_ped_quantidade;
    @FXML
    private ToggleGroup gp_tipoPedido;
    @FXML
    private RadioButton rb_ifood;
    @FXML
    private RadioButton rb_AiqFome;
    @FXML
    private ComboBox<Mesa> cb_mesa;
    @FXML
    private RadioButton rb_mesa;
    @FXML
    private RadioButton rb_telefone;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        MaskFieldUtil.onlyNumericValue(txt_quantidade);
        mesa = 0;
        // TODO
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        col_prod_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_prod_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        col_ped_quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        col_ped_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_ped_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        rb_mesa.setUserData("mesa");
        rb_telefone.setUserData("telefone");
        rb_ifood.setUserData("ifood");
        rb_AiqFome.setUserData("aiqfome");

        rb_mesa.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue)
            {
                cb_mesa.setDisable(false);
                vb_cliente.setDisable(true);
            } else
            {
                cb_mesa.setDisable(true);
                vb_cliente.setDisable(false);
            }
        });

        //colocando as mesas abertas no combobox
        String sql = "select m.numero,m.pes_cod from pessoa p inner join (select * from  mesa where datafechamento is null) m on p.pes_cod=m.pes_cod";
        ResultSet rsm = Conexao.get().consultar(sql);

        try
        {
            while (rsm.next())
            {
                Mesa m = new Mesa();
                m.setNum(rsm.getInt("numero"));
                m.setCliente(new CtrPessoa().get(new Pessoa(), rsm.getInt("pes_cod")));
                cb_mesa.getItems().add(m);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(RealizarPedidoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Platform.runLater(
                () ->
        {
            try
            {

                //Bacon
                ResultSet rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'bacon%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_bacon.setText(chk_bacon.getText() + " " + rs.getFloat("valor"));
                    chk_bacon.setUserData(rs.getFloat("valor"));
                }
                //Calabresa
                rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'calabresa%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_calabresa.setText(chk_calabresa.getText() + " " + rs.getFloat("valor"));
                    chk_calabresa.setUserData(rs.getFloat("valor"));
                }

                //Ovo
                rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'ovo%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_ovo.setText(chk_ovo.getText() + " " + rs.getFloat("valor"));
                    chk_ovo.setUserData(rs.getFloat("valor"));
                }
                //Cebola
                rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'cebola%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_cebola.setText(chk_cebola.getText() + " " + rs.getFloat("valor"));
                    chk_cebola.setUserData(rs.getFloat("valor"));
                }
                //Milho
                rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'milho%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_milho.setText(chk_milho.getText() + " " + rs.getFloat("valor"));
                    chk_milho.setUserData(rs.getFloat("valor"));
                }
                //Salsicha
                rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'salsicha%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_salsicha.setText(chk_salsicha.getText() + " " + rs.getFloat("valor"));
                    chk_salsicha.setUserData(rs.getFloat("valor"));
                }
                //hamburguer
                rs = Conexao.get().consultar("select valor from acrescimo t  inner join (select * from materiaprima where descricao ilike 'hamburguer%') mp ON t.mp_cod = mp.mp_cod");
                if (rs.next())
                {
                    chk_hamburguer.setText(chk_hamburguer.getText() + " " + rs.getFloat("valor"));
                    chk_hamburguer.setUserData(rs.getFloat("valor"));
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(RealizarPedidoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );

        txt_cliente.setOnKeyPressed(
                (KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                evt_procurarCliente();
        }
        );

        txt_cliente.lengthProperty()
                .addListener((event) ->
                {
                    evt_procurarCliente();
                }
                );

        evt_procurarCliente();

        txt_produto.setOnKeyPressed(
                (KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                evt_procurarProduto();
        }
        );

        txt_produto.lengthProperty()
                .addListener((event) ->
                {
                    evt_procurarProduto();
                }
                );

        pn_chk.getChildren().stream().filter((n) -> (n instanceof CheckBox)).forEachOrdered((n) ->
        {
            ((CheckBox) n).selectedProperty().addListener((observable) ->
            {
                atualizarTotalProduto();
            }
            );
        });
        evt_procurarProduto();

        txt_quantidade.textProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if (!oldValue.equals(newValue))
                        atualizarTotalProduto();
                }
                );

        tabela_pedido.getItems()
                .addListener((Observable c) ->
                {
                    float conta = 0;
                    for (produto p : (ObservableList<produto>) c)
                        conta += p.getValor();

                    final float aux = conta;
                    Platform.runLater(() ->
                    {
                        lb_totalPedido.setText("" + aux);
                    });

                }
                );
    }

    private void evt_procurarCliente()
    {
        String filtro = "";
        if (!txt_cliente.getText().trim().isEmpty())
            filtro = "nome ilike '%" + txt_cliente.getText() + "%'";
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Pessoa(), filtro, "order by nome limit 20"));
        if (ob.size() > 0)
            tabela_cliente.setItems(ob);
        else
            tabela_cliente.setItems(null);
    }

    @FXML
    private void evt_selecionarCliente(ActionEvent event)
    {
        Pessoa p;

        if (rb_mesa.isSelected() && cb_mesa.getValue() != null)
        {
            pes_selec = new CtrPessoa().get(new Pessoa(), cb_mesa.getValue().getCliente().getCod());
            mesa = cb_mesa.getValue().getNum();
            vb_cliente.setDisable(true);
            vb_pedido.setDisable(false);
        } else if ((p = tabela_cliente.getSelectionModel().getSelectedItem()) != null)
        {
            pes_selec = p;
            vb_cliente.setDisable(true);
            vb_pedido.setDisable(false);
        }
    }

    private void evt_procurarProduto()
    {
        String filtro = "tag <> 'in'";
        if (!txt_produto.getText().trim().isEmpty())
            filtro += " and descricao ilike '%" + txt_produto.getText() + "%'";
        ObservableList<Produto> ob = FXCollections.observableArrayList(new CtrProduto().get(new Produto(), filtro, "order by descricao limit 20"));
        if (ob.size() > 0)
            tabela_produtos.setItems(ob);
        else
            tabela_produtos.setItems(null);
    }

    @FXML
    private void evt_selecionarProduto(MouseEvent event)
    {
        for (Node n : pn_chk.getChildren())
            if (n instanceof CheckBox)
                ((CheckBox) n).setSelected(false);
        prod_selec = (Produto) tabela_produtos.getSelectionModel().getSelectedItem();
        txt_obsProd.setText("");
        if (prod_selec != null && prod_selec.getTag().equals("la"))
            vb_acrescimos.setDisable(false);
        else
            vb_acrescimos.setDisable(true);
        atualizarTotalProduto();
    }

    private void atualizarTotalProduto()
    {
        float conta = 0;

        float prod = prod_selec == null ? 0 : prod_selec.getValor();

        //Obtendo os checkbox prod_selecs
        float adicionais = 0;
        for (Node n : pn_chk.getChildren())
            if (n instanceof CheckBox)
                adicionais += ((CheckBox) n).isSelected() ? (float) n.getUserData() : 0;

        int quantidade = Integer.parseInt(txt_quantidade.getText().trim().equals("") ? "0" : txt_quantidade.getText().trim());
        if (quantidade <= 0)
            quantidade = 1;

        conta = (prod + adicionais) * quantidade;

        final float valor = conta;

        Platform.runLater(() ->
        {
            lb_totalProduto.setText("" + valor);
        });
    }

    @FXML
    private void evt_delProduto(ActionEvent event)
    {
        tabela_pedido.getItems().remove(tabela_pedido.getSelectionModel().getSelectedItem());

    }

    public class produto
    {

        String descricao;
        int quantidade;
        float valor;

        public String getDescricao()
        {
            return descricao;
        }

        public void setDescricao(String descricao)
        {
            this.descricao = descricao;
        }

        public int getQuantidade()
        {
            return quantidade;
        }

        public void setQuantidade(int quantidade)
        {
            this.quantidade = quantidade;
        }

        public float getValor()
        {
            return valor;
        }

        public void setValor(float valor)
        {
            this.valor = valor;
        }

    }

    @FXML
    private void evt_addProduto(ActionEvent event)
    {

        int quantidade = Integer.parseInt(txt_quantidade.getText().trim().equals("") ? "0" : txt_quantidade.getText().trim());
        if (quantidade <= 0)
            quantidade = 1;

        String descricao = prod_selec.getDescricao();
        for (Node n : pn_chk.getChildren())
            if (n instanceof CheckBox && ((CheckBox) n).isSelected())
                descricao += "+" + ((CheckBox) n).getText().substring(0, ((CheckBox) n).getText().indexOf(' '));

        if (!txt_obsProd.getText().trim().isEmpty())
            descricao += " / OBS:" + txt_obsProd.getText();
        float total = Float.parseFloat(lb_totalProduto.getText());

        produto p = new produto();
        p.setQuantidade(quantidade);
        p.setDescricao(descricao);
        p.setValor(total);

        tabela_pedido.getItems().add(p);
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {
        ObservableList<produto> obp = tabela_pedido.getItems();
        if (obp.size() > 0)
        {
            String desc = "";
            float conta = 0;
            int index = 0;
            desc += obp.get(index).getQuantidade() + "x: " + obp.get(index).getDescricao();
            conta += obp.get(index++).getValor();
            for (; index < obp.size(); index++)
            {
                desc += "\n" + obp.get(index).getQuantidade() + "x: " + obp.get(index).getDescricao();
                conta += obp.get(index).getValor();
            }

            Pedido p = new Pedido(pes_selec, mlanches.Mlanches.getInstance().Sessao, LocalDateTime.now(), null, null, desc, txt_obsGeral.getText(),
                    conta, 0, (String) gp_tipoPedido.getSelectedToggle().getUserData(), false, mesa, false);

            if (new CtrPedido().salvar(p))
                TelaPrincipalController.getInstance().setAreaTrabalho(RealizarPedidoController.FXML);

        }
    }
}
