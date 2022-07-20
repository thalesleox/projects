/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import rosolen.db.controladoras.CtrServico;
import rosolen.db.util.Conexao;
import rosolen.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class ValoresController implements Initializable
{

    public static final String FXML = "Valores.fxml";

    private static final DecimalFormat df = new DecimalFormat("###.00");
    @FXML
    private TextField txt_avaliacao;
    @FXML
    private TextField txt_reavaliacao;
    @FXML
    private TextField txt_mensNormal;
    @FXML
    private TextField txt_mensFamiliar;
    @FXML
    private AnchorPane todo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        // TODO
        MaskFieldUtil.monetaryField(txt_avaliacao);
        MaskFieldUtil.monetaryField(txt_reavaliacao);
        MaskFieldUtil.monetaryField(txt_mensNormal);
        MaskFieldUtil.monetaryField(txt_mensFamiliar);

        CtrServico ser = new CtrServico();
        txt_avaliacao.setText(df.format(ser.get(3).getValor()));
        txt_reavaliacao.setText(df.format(ser.get(4).getValor()));
        txt_mensNormal.setText(df.format(ser.get(1).getValor()));
        txt_mensFamiliar.setText(df.format(ser.get(2).getValor()));
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {
        String sql = "update servico set valor = $1 where ser_cod = 3".replace("$1", "" + Float.parseFloat(txt_avaliacao.getText().replace(",", ".")));
        Conexao.get().manipular(sql);
        sql = "update servico set valor = $1 where ser_cod = 4".replace("$1", "" + Float.parseFloat(txt_reavaliacao.getText().replace(",", ".")));
        Conexao.get().manipular(sql);
        sql = "update servico set valor = $1 where ser_cod = 1".replace("$1", "" + Float.parseFloat(txt_mensNormal.getText().replace(",", ".")));
        Conexao.get().manipular(sql);
        sql = "update servico set valor = $1 where ser_cod = 2".replace("$1", "" + Float.parseFloat(txt_mensFamiliar.getText().replace(",", ".")));
        Conexao.get().manipular(sql);

        todo.getChildren().clear();
    }
}
