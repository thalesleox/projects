/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class AlunosController implements Initializable
{

    public static final String FXML = "Alunos.fxml";
    @FXML
    private AnchorPane pn_situacao;
    @FXML
    private AnchorPane pn_anivesariantes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            // TODO
            pn_situacao.getChildren().add(rosolen.Rosolen.getInstance().carregarFXML(PendentesController.FXML));
        } catch (Exception ex)
        {
            Logger.getLogger(AlunosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            pn_anivesariantes.getChildren().add(rosolen.Rosolen.getInstance().carregarFXML(AniversariantesController.FXML));
        } catch (Exception ex)
        {
            Logger.getLogger(AlunosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
