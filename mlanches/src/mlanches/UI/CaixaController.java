/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlanches.UI;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import mlanches.db.controladoras.CtrCaixa;
import mlanches.db.entidades.Caixa;
import mlanches.db.util.Conexao;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class CaixaController implements Initializable
{

    public static final String FXML = "Caixa.fxml";
    @FXML
    private RadioButton rb_Fechado;
    @FXML
    private ToggleGroup tg_statusCaixa;
    @FXML
    private RadioButton rb_Aberto;
    @FXML
    private TextField txt_saldoInicial;
    @FXML
    private TextField txt_saldoFinal;
    String StatusCaixa;
    Caixa cx;
    @FXML
    private AnchorPane todo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        cx = new CtrCaixa().get("select * from caixa order by cx_cod desc limit 1");

        if (cx.getStatus().equals("aberto"))
            rb_Aberto.setSelected(true);
        else
            rb_Fechado.setSelected(true);
        StatusCaixa = cx.getStatus();
        txt_saldoInicial.setText(Float.toString(cx.getSaldoInicial()));
        txt_saldoFinal.setText(Float.toString(cx.getSaldoFinal()));
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {
        if (rb_Aberto.isSelected() && StatusCaixa.equals("fechado")
                || rb_Fechado.isSelected() && StatusCaixa.equals("aberto")) // trocou o status
        {
            if (rb_Aberto.isSelected()) // Abriu
            {
                cx.setStatus("aberto");
                cx.setSaldoInicial(cx.getSaldoFinal());
            } else // Fechou
                cx.setStatus("fechado");

            cx.setData(LocalDateTime.now());
            if (new CtrCaixa().salvar(cx))
            {
                todo.setDisable(true);
                TelaPrincipalController.getInstance().atualizarStatus(cx);
            } else
                new Alert(Alert.AlertType.ERROR, "Não foi possível atualizar o status do caixa, contate o suporte!\n\n"
                        + Conexao.get().getMensagemErro(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void evt_cancelar(ActionEvent event)
    {
        if (StatusCaixa.equals("aberto"))
            rb_Aberto.setSelected(true);
        else
            rb_Fechado.setSelected(true);
    }

}
