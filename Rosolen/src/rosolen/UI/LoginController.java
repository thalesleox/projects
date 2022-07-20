/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.entidades.Usuario;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class LoginController implements Initializable
{

    public static final String FXML = "Login.fxml";

    Stage stg;
    @FXML
    private ImageView iv_logo;
    @FXML
    private TextField tb_usuario;
    @FXML
    private PasswordField pf_senha;
    @FXML
    private Button btEntrar;
    @FXML
    private AnchorPane Todo;

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
        pf_senha.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                Entrar(new ActionEvent());
        });
    }

    @FXML
    private void Entrar(ActionEvent event)
    {

        Usuario u = new CtrPessoa().Entrar(tb_usuario.getText(), pf_senha.getText());
        rosolen.Rosolen.getInstance().Logar(true, u);
    }
}
