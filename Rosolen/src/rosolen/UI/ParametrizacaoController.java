/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rosolen.db.controladoras.CtrParametrizacao;
import rosolen.db.entidades.Endereco;
import rosolen.db.entidades.Parametrizacao;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class ParametrizacaoController implements Initializable
{

    public static final String FXML = "Parametrizacao.fxml";

    @FXML
    private TextField txt_telefone;
    @FXML
    private TextArea txt_razaosoc;
    @FXML
    private Button btn_confirmar;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_carregar;
    @FXML
    private ImageView img_logo;
    @FXML
    private TextField txt_cep;
    @FXML
    private TextField txt_numero;
    CtrParametrizacao ctrpar;
    @FXML
    private TextField txt_tipolog;
    @FXML
    private TextField txt_logradouro;
    @FXML
    private TextField txt_bairro;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ctrpar = new CtrParametrizacao();
    }

    private Parametrizacao infoTela()
    {
        return new Parametrizacao(txt_razaosoc.getText(), new Endereco(0, txt_cep.getText(), txt_tipolog.getText(), txt_logradouro.getText(),
                txt_bairro.getText(), Integer.parseInt(txt_numero.getText())),
                txt_telefone.getText(), img_logo.getImage());
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {
        if (!txt_razaosoc.getText().trim().isEmpty() && !txt_cep.getText().trim().isEmpty()
                && !txt_numero.getText().trim().isEmpty() && !txt_telefone.getText().trim().isEmpty() && !txt_tipolog.getText().trim().isEmpty()
                && !txt_logradouro.getText().trim().isEmpty() && !txt_bairro.getText().trim().isEmpty()
                && img_logo.getImage() != null)
            if (!ctrpar.salvar(infoTela()))
                System.out.println("Erro ao salvar!");
            else
            {
                Stage stage = (Stage) btn_cancelar.getScene().getWindow();
                stage.close();
            }
        else
        {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Atenção");
            al.setContentText("Campos preenchidos inválidos!");
            al.showAndWait();
        }
    }

    @FXML
    private void evt_cancelar(ActionEvent event)
    {
        Stage stage = (Stage) btn_cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void evt_carregarlogo(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        File arqsel = fc.showOpenDialog(null);
        img_logo.setImage(arqsel != null ? new Image(arqsel.toURI().toString()) : null);
    }

}
