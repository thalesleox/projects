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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import mlanches.db.controladoras.CtrPedido;
import mlanches.db.controladoras.CtrPessoa;
import mlanches.db.controladoras.CtrRecebimento;
import mlanches.db.entidades.Mesa;
import mlanches.db.entidades.Pessoa;
import mlanches.db.entidades.Recebimento;
import mlanches.db.util.Conexao;
import mlanches.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class RealizarRecebimentoController implements Initializable
{

    public static final String FXML = "RealizarRecebimento.fxml";
    @FXML
    private RadioButton rb_pagar;
    @FXML
    private ToggleGroup gp_pagarMarcar;
    @FXML
    private RadioButton rb_marcar;
    @FXML
    private RadioButton rb_integral;
    @FXML
    private ToggleGroup gp_tipoFechamento;
    @FXML
    private RadioButton rb_parcial;
    @FXML
    private RadioButton rb_dinheiro;
    @FXML
    private RadioButton rb_cartao;
    @FXML
    private TextField txt_total;
    @FXML
    private TextField txt_recebido;
    @FXML
    private TextField txt_troco;
    @FXML
    private VBox vb_pedido;
    @FXML
    private TableColumn<?, ?> col_descricao;
    @FXML
    private TableColumn<?, ?> val_restante;
    @FXML
    private TextField txt_sum;
    @FXML
    private TextField txt_dividir;
    @FXML
    private TextField txt_valDividido;
    @FXML
    private TextField val_cobrado;
    @FXML
    private TextField txt_recebidoParcial;
    @FXML
    private TextField txt_trocoParcial;
    @FXML
    private RadioButton rb_mesa;
    @FXML
    private VBox vb_mesa;
    @FXML
    private Label lb_restante;
    @FXML
    private RadioButton rb_telefone;
    @FXML
    private ToggleGroup gp_tipoPedido;
    @FXML
    private RadioButton rb_aplicativo;
    @FXML
    private ToggleGroup gp_tipoPagamento;
    @FXML
    private ComboBox<Mesa> cb_pedido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        col_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        val_restante.setCellValueFactory(new PropertyValueFactory<>("restante"));

        MaskFieldUtil.monetaryField(txt_total);
        MaskFieldUtil.monetaryField(txt_recebido);
        MaskFieldUtil.monetaryField(txt_troco);

        txt_recebido.textProperty().addListener((observable) ->
        {
            String recebidoS = txt_recebido.getText().trim().replace(',', '.');
            Float recebidoF = Float.parseFloat(recebidoS);
            String totalS = txt_total.getText().trim().replace(',', '.');
            Float totalF = Float.parseFloat(totalS);
            String pagoS = lb_restante.getText();
            Float pagoF = Float.parseFloat(pagoS);
            if (recebidoS.contains(".") && totalS.contains("."))
                if (recebidoF + pagoF > totalF)
                {
                    String conta = ("" + (recebidoF + pagoF - totalF)).replace('.', ',');
                    if (conta.length() == 3)
                        conta += "0";
                    conta = conta.substring(0, conta.indexOf(',') + 3);
                    txt_troco.setText(conta);
                } else
                    txt_troco.setText("0");
        });
        rb_mesa.selectedProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    cb_pedido.getItems().clear();
                    if (newValue)
                    {
                        //colocando as Mesas abertas no combobox
                        String sqlMesas = "select * from Mesa where datafechamento is null";
                        ResultSet rsm = Conexao.get().consultar(sqlMesas);

                        try
                        {
                            while (rsm.next())
                            {
                                Mesa m = new Mesa();
                                m.setNum(rsm.getInt("numero"));
                                m.setCliente(new CtrPessoa().get(new Pessoa(), rsm.getInt("pes_cod")));
                                cb_pedido.getItems().add(m);
                            }
                        } catch (SQLException ex)
                        {
                            Logger.getLogger(RealizarPedidoController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        cb_pedido.getSelectionModel().select(-1);
                        txt_total.setEditable(false);
                    }
                }
                );

        rb_telefone.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue)
            {
                cb_pedido.getItems().clear();
                //colocando os pedidos em aberto no combobox
                String sqlpedido = "select * from Pedido where mesa = 0 and horaschegada is null and tipopedido='telefone' and pago is false";
                ResultSet rsp = Conexao.get().consultar(sqlpedido);

                try
                {
                    while (rsp.next())
                    {
                        Mesa m = new Mesa();
                        m.setNum(rsp.getInt("mesa"));
                        m.setCliente(new CtrPessoa().get(new Pessoa(), rsp.getInt("pes_cod")));
                        cb_pedido.getItems().add(m);
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(RealizarPedidoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_total.setText("0");
                Platform.runLater(() ->
                {
                    lb_restante.setText("0.0");
                });
                txt_total.setEditable(true);
            }
        });

        rb_aplicativo.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue)
            {
                rb_marcar.setDisable(true);
                cb_pedido.getItems().clear();
                //colocando os pedidos em aberto no combobox
                String sqlpedido = "select * from Pedido where mesa = 0 and horaschegada is null and (tipopedido = 'ifood' or tipopedido = 'aiqfome') and pago is false";
                ResultSet rsp = Conexao.get().consultar(sqlpedido);

                try
                {
                    while (rsp.next())
                    {
                        Mesa m = new Mesa();
                        m.setNum(rsp.getInt("mesa"));
                        m.setCliente(new CtrPessoa().get(new Pessoa(), rsp.getInt("pes_cod")));
                        cb_pedido.getItems().add(m);
                    }
                } catch (SQLException ex)
                {
                    Logger.getLogger(RealizarPedidoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_total.setText("0");
                Platform.runLater(() ->
                {
                    lb_restante.setText("0.0");
                });
                txt_total.setEditable(true);
            } else
                rb_marcar.setDisable(false);
        });

        cb_pedido.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null && oldValue != newValue)
            {
                txt_total.setText("0");
                txt_recebido.setText("");
                txt_troco.setText("");
                Platform.runLater(() ->
                {
                    lb_restante.setText("");
                });
                String sql = "";
                if (rb_mesa.isSelected())
                    sql = "select sum(total) total, p.mesa mesa from (select * from pedido where pes_cod = " + newValue.getCliente().getCod() + " and pago is false) p "
                            + "inner join (select * from mesa where datafechamento is null) m "
                            + "on m.pes_cod = p.pes_cod group by p.mesa";
                else if (rb_telefone.isSelected())
                    sql = "select sum(total) total from Pedido where mesa = 0 and horaschegada is null and tipopedido = 'telefone' and pago is false";
                else if (rb_aplicativo.isSelected())
                    sql = "select sum(total) total from Pedido where mesa = 0 and horaschegada is null and (tipopedido = 'ifood' or tipopedido = 'aiqfome') and pago is false";
                ResultSet rs = Conexao.get().consultar(sql);

                try
                {
                    if (rs != null && rs.next())
                    {
                        txt_total.setText("" + (rs.getFloat("total") * 10));

                        if (rb_mesa.isSelected())
                        {
                            sql = "select sum(val_pago) total from recebimento where datapagamento > "
                                    + "(select  horasrealizado from (select * from pedido where pes_cod = " + newValue.getCliente().getCod() + ") p "
                                    + "inner join (select * from mesa where datafechamento is null) m on m.pes_cod = p.pes_cod and p.mesa=m.numero limit 1) and"
                                    + " numero = " + newValue.getNum();

                            rs = Conexao.get().consultar(sql);
                        } else
                            rs = null;
                        String aux = "0";

                        if (rs != null && rs.next())
                            aux = "" + rs.getFloat("total");

                        final String f = aux;
                        Platform.runLater(() ->
                        {
                            lb_restante.setText("" + f);
                        });

                    }

                } catch (SQLException ex)
                {
                    Logger.getLogger(RealizarPedidoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {

        if (gp_tipoPedido.getSelectedToggle() != null && cb_pedido.getSelectionModel().getSelectedItem() != null)
        {
            String recebido = txt_recebido.getText().trim().replace(',', '.');
            if (recebido.equals(""))
                recebido = "0";

            String troco = txt_troco.getText().trim().replace(',', '.');
            if (troco.equals(""))
                troco = "0";

            Recebimento rec = new Recebimento(LocalDateTime.now(), Float.parseFloat(recebido), Float.parseFloat(troco), cb_pedido.getSelectionModel().getSelectedItem().getNum(),
                    rb_pagar.isSelected(), rb_marcar.isSelected(), rb_dinheiro.isSelected(), rb_cartao.isSelected());
            try
            {
                Conexao.get().getConnect().setAutoCommit(false);
            } catch (SQLException ex)
            {
                Logger.getLogger("Realizar Recebimento").log(Level.SEVERE, null, ex);
            }

            float total = 0;
            String sql;
            Mesa m = cb_pedido.getSelectionModel().getSelectedItem();
            ResultSet rs;
            if (rb_mesa.isSelected())
            {
                //obtem todos os pagamentos da mesa
                sql = "select sum(val_pago) total from recebimento where datapagamento > "
                        + "(select  horasrealizado from (select * from pedido where pes_cod = " + m.getCliente().getCod() + " and pago is false) p "
                        + "inner join (select * from mesa where datafechamento is null) m on m.pes_cod = p.pes_cod and p.mesa=m.numero limit 1) and"
                        + " numero = " + m.getNum();
                rs = Conexao.get().consultar(sql);
                try
                {
                    if (rs != null && rs.next())
                        total = rs.getFloat("total");
                } catch (SQLException ex)
                {
                    Logger.getLogger(RealizarRecebimentoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (new CtrRecebimento().salvar(rec))
                try
                {

                    //obtem o total do pedido
                    if (rb_mesa.isSelected())
                        sql = "select sum(total) total, p.mesa mesa from (select * from pedido where pes_cod = " + m.getCliente().getCod() + " and pago = 'false') p "
                                + "inner join (select * from mesa where datafechamento is null) m "
                                + "on m.pes_cod = p.pes_cod group by p.mesa";
                    else
                        sql = "select * from pedido where pes_cod = " + m.getCliente().getCod() + " and pago = 'false'";
                    rs = Conexao.get().consultar(sql);
                    try
                    {
                        if (rs != null && rs.next())
                            if (total + Float.parseFloat(recebido) - Float.parseFloat(troco) >= rs.getFloat("total"))
                            {
                                if (rb_mesa.isSelected())
                                    sql = "select p.ped_cod from (select * from pedido where pes_cod = " + m.getCliente().getCod() + " and pago = 'false') p "
                                            + "inner join (select * from mesa where datafechamento is null) m "
                                            + "on m.pes_cod = p.pes_cod";
                                else
                                    sql = "select ped_cod from pedido where pes_cod = " + m.getCliente().getCod() + " and pago is false and horaschegada is null";
                                rs = Conexao.get().consultar(sql);
                                if (rs != null)
                                    while (rs.next())
                                        new CtrPedido().pagar(rs.getInt("ped_cod"), m.getNum());
                            }

                    } catch (SQLException ex)
                    {
                        Conexao.get().getConnect().rollback();
                        Logger.getLogger(RealizarRecebimentoController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (SQLException ex)
                {
                    try
                    {
                        Conexao.get().getConnect().rollback();
                    } catch (SQLException ex1)
                    {
                        Logger.getLogger(RealizarRecebimentoController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    Logger.getLogger(RealizarRecebimentoController.class.getName()).log(Level.SEVERE, null, ex);
                }

            TelaPrincipalController.getInstance().setAreaTrabalho(FXML);
        }
        try
        {
            Conexao.get().getConnect().setAutoCommit(true);
        } catch (SQLException ex)
        {
            Logger.getLogger("Realizar Recebimento").log(Level.SEVERE, null, ex);
        }
    }
}
