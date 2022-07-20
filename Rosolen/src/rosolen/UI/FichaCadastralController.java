/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFrame;
import rosolen.db.controladoras.CtrCategoria;
import rosolen.db.controladoras.CtrEndereco;
import rosolen.db.controladoras.CtrLancamento;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.controladoras.CtrServico;
import rosolen.db.controladoras.CtrSituacao;
import rosolen.db.entidades.Endereco;
import rosolen.db.entidades.Lancamento;
import rosolen.db.entidades.Pessoa;
import rosolen.db.entidades.Servico;
import rosolen.db.entidades.Usuario;
import rosolen.db.entidades.tipoLancamento;
import rosolen.db.util.Conexao;
import rosolen.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class FichaCadastralController implements Initializable
{

    public static final String FXML = "FichaCadastral.fxml";
    public Usuario selecionado;

    @FXML
    private HBox hb_botoes;
    @FXML
    private Button bt_novo;
    @FXML
    private Button bt_alterar;
    @FXML
    private Button bt_apagar;
    @FXML
    private Button bt_confirmar;
    @FXML
    private Button bt_cancelar;
    @FXML
    private AnchorPane pn_dados;
    @FXML
    private TextField txt_codigo;
    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_cpf;
    @FXML
    private TextField txt_telefone;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_cep;
    @FXML
    private TextField txt_tipologradouro;
    @FXML
    private TextField txt_logradouro;
    @FXML
    private TextField txt_bairro;
    @FXML
    private TextField txt_numero;
    @FXML
    private ImageView foto;
    @FXML
    private BorderPane pn_procura;
    @FXML
    private TableView<Pessoa> tabela;
    @FXML
    private TableColumn<?, ?> col_cod;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_cpf;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private TableColumn<?, ?> col_cep;
    @FXML
    private TableColumn<?, ?> col_email;
    @FXML
    private HBox hb_procurar;
    @FXML
    private TextField txt_procurar;
    @FXML
    private Button bt_procurar;
    @FXML
    private ToggleGroup Sexo;
    @FXML
    private RadioButton rb_homem;
    @FXML
    private RadioButton rb_mulher;
    @FXML
    private Label lb_resultado;
    @FXML
    private VBox pn_fichacadastral;
    @FXML
    private DatePicker dp_datacadastro;
    @FXML
    private DatePicker dp_datanascimento;

    private int status;
    Webcam webcam;
    JFrame window;

    @FXML
    private TextField txt_credito;
    @FXML
    private Button bt_ligar;
    @FXML
    private Button bt_capturar;
    @FXML
    private Button bt_verificar;
    @FXML
    private Button bt_historico;

    private ArrayList<tipoLancamento> auxLanc;
    @FXML
    private Button bt_familia;

    public ArrayList<Pessoa> alfamilia;
    public ArrayList<Pessoa> aldelfamilia;

    private static FichaCadastralController instance;
    @FXML
    private TextField txt_situacao;

    public FichaCadastralController()
    {
        instance = this;
    }

    public static FichaCadastralController getInstance()
    {
        return instance;
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
        col_cod.setCellValueFactory(new PropertyValueFactory<>("cod"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_cep.setCellValueFactory(new PropertyValueFactory<>("end"));

        MaskFieldUtil.cpfField(this.txt_cpf);
        MaskFieldUtil.onlyAlfaNumericValue(this.txt_nome);
        MaskFieldUtil.foneField(this.txt_telefone);
        MaskFieldUtil.onlyDigitsValue(txt_numero);

        ImageView iv = new ImageView(rosolen.Rosolen.getInstance().getImagem("imagens/teamwork.png"));
        iv.setPreserveRatio(true);
        iv.setFitHeight(32);
        iv.setFitHeight(32);
        bt_familia.setGraphic(iv);

        Sexo.getToggles().get(0).setUserData("H");
        Sexo.getToggles().get(1).setUserData("M");

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        lb_resultado.setTextFill(Paint.valueOf("red"));

        txt_procurar.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                evt_procurar(new ActionEvent());
        });

        txt_cep.setOnKeyPressed((KeyEvent ke)
                ->
        {
            new Thread(() ->
            {
                pn_fichacadastral.getScene().setCursor(Cursor.WAIT);
            }).start();

            if (ke.getCode().equals(KeyCode.ENTER))
            {
                Endereco end = new CtrEndereco().getCepWS(txt_cep.getText());
                if (end == null)
                    Platform.runLater(() ->
                    {
                        lb_resultado.setText("CEP invalido!");
                    });
                else
                {
                    Platform.runLater(() ->
                    {
                        lb_resultado.setText("");
                    });
                    txt_tipologradouro.setText(end.getTipolog());
                    txt_logradouro.setText(end.getLogradouro());
                    txt_bairro.setText(end.getBairro());
                }
            }
            new Thread(() ->
            {
                pn_fichacadastral.getScene().setCursor(Cursor.DEFAULT);
            }).start();
        });

        dp_datacadastro.setOnAction((event) ->
        {
            if (dp_datacadastro.getValue().getYear() < 2000)
                dp_datacadastro.setValue(LocalDate.of(2000, status, status));
            else if (dp_datacadastro.getValue().getYear() > LocalDate.now().getYear())
                dp_datacadastro.setValue(LocalDate.of(LocalDate.now().getYear(), status, status));

            if (status == 1)
            {

                VBox vb_todo = new VBox();
                vb_todo.setSpacing(2);
                vb_todo.setPadding(new Insets(2, 2, 2, 2));
                vb_todo.setId("todo");

                VBox vbAvaliacao = new VBox();
                vbAvaliacao.setSpacing(2);
                vbAvaliacao.setPadding(new Insets(2, 2, 2, 2));
                vb_todo.getChildren().add(vbAvaliacao);

                Label lbAvaliacao = new Label("Primeira Avaliação:");
                vbAvaliacao.getChildren().add(lbAvaliacao);

                DatePicker dpAvaliacao = new DatePicker();
                dpAvaliacao.setValue(dp_datacadastro.getValue());
                vbAvaliacao.getChildren().add(dpAvaliacao);

                int anoStart = dp_datacadastro.getValue().getYear();
                int anoAUX = anoStart;
                for (int anoAtual = LocalDate.now().getYear(); anoAUX <= anoAtual; anoAUX++)
                {
                    VBox vbauxAno = new VBox();
                    vbauxAno.setId("vb" + anoAUX);
                    vbauxAno.setSpacing(2);
                    vbauxAno.setPadding(new Insets(2, 2, 2, 2));
                    vbauxAno.setStyle("-fx-border-color:black");
                    vbauxAno.setUserData(anoAUX);
                    vb_todo.getChildren().add(vbauxAno);

                    Label lbano = new Label(String.valueOf(anoAUX));
                    lbano.setStyle("-fx-font-weight: bold;-fx-font-size: 18px");
                    lbano.setId("lb" + String.valueOf(anoAUX));
                    vbauxAno.getChildren().add(lbano);

                    HBox hbReavaliacao = new HBox();
                    hbReavaliacao.setId("hbReavalicao" + anoAUX);
                    hbReavaliacao.setSpacing(2);
                    hbReavaliacao.setPadding(new Insets(2, 2, 2, 2));
                    hbReavaliacao.setAlignment(Pos.CENTER_LEFT);
                    hbReavaliacao.setUserData(0);
                    vbauxAno.getChildren().add(hbReavaliacao);

                    Label lbReavaliacao = new Label("Reavaliação:");
                    hbReavaliacao.getChildren().add(lbReavaliacao);

                    TextField tfReavaliacao = new TextField();
                    tfReavaliacao.setMaxWidth(40);
                    hbReavaliacao.getChildren().add(tfReavaliacao);

                    HBox hbDatePicker = new HBox();
                    hbDatePicker.setId("hbReavalicao" + anoAUX);
                    hbDatePicker.setSpacing(2);
                    hbDatePicker.setPadding(new Insets(2, 2, 2, 2));
                    hbDatePicker.setUserData(1);
                    vbauxAno.getChildren().add(hbDatePicker);
                    tfReavaliacao.setOnKeyPressed((KeyEvent ke) ->
                    {
                        if (ke.getCode().equals(KeyCode.ENTER) && !tfReavaliacao.getText().isEmpty() && Integer.valueOf(tfReavaliacao.getText().trim()) > 0)
                        {
                            hbDatePicker.getChildren().clear();
                            int value = Integer.valueOf(tfReavaliacao.getText().trim());
                            for (int i = 0; i < value; i++)
                            {
                                DatePicker dp = new DatePicker();
                                dp.setMaxWidth(125);
                                hbDatePicker.getChildren().add(dp);
                                hbDatePicker.getScene().getWindow().sizeToScene();
                                hbDatePicker.getScene().getWindow().centerOnScreen();
                            }
                        }
                    });

                    HBox hbauxMes = new HBox();
                    hbauxMes.setId("hbMes" + anoAUX);
                    hbauxMes.setSpacing(2);
                    hbauxMes.setPadding(new Insets(2, 2, 2, 2));
                    hbauxMes.setStyle("-fx-border-color:orange");
                    hbauxMes.setUserData(2);
                    int maxmes = anoAUX == anoAtual ? LocalDate.now().getMonthValue() : 12;
                    for (int i = (anoStart == anoAUX ? dp_datacadastro.getValue().getMonthValue() : 1); i <= maxmes; i++)
                    {
                        VBox vbaux = new VBox();
                        vbaux.setAlignment(Pos.CENTER);
                        vbaux.setSpacing(2);
                        vbaux.setPadding(new Insets(2, 2, 2, 2));
                        vbaux.setStyle("-fx-border-color:lightgrey");
                        hbauxMes.getChildren().add(vbaux);

                        CheckBox cb = new CheckBox(String.valueOf(i));
                        cb.setAllowIndeterminate(false);
                        cb.setUserData(i);
                        cb.setSelected(true);
                        cb.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                        cb.setUserData(i);
                        vbaux.getChildren().add(cb);

                        ComboBox cbmes = new ComboBox();
                        cbmes.setPadding(new Insets(2, 2, 2, 2));
                        int maxdias = LocalDate.of(anoAUX, i, 1).getMonth().minLength();

                        if (anoAUX == anoAtual)
                            if (i == LocalDate.now().getMonthValue())
                                maxdias = LocalDate.now().getDayOfMonth();

                        for (int j = 0; j < maxdias; j++)
                            cbmes.getItems().add(j + 1);
                        if (dpAvaliacao.getValue().getDayOfMonth() >= maxdias)
                            cbmes.getSelectionModel().selectLast();
                        else
                            cbmes.getSelectionModel().select(dpAvaliacao.getValue().getDayOfMonth() - 1);
                        vbaux.getChildren().add(cbmes);

                        HBox hbTipoMens = new HBox();
                        hbTipoMens.setSpacing(2);
                        hbTipoMens.setPadding(new Insets(2, 2, 2, 2));
                        RadioButton rb1 = new RadioButton("N");
                        rb1.setSelected(true);
                        rb1.setUserData(1);
                        RadioButton rb2 = new RadioButton("F");
                        rb2.setUserData(2);
                        hbTipoMens.getChildren().add(rb1);
                        hbTipoMens.getChildren().add(rb2);
                        ToggleGroup tg = new ToggleGroup();
                        tg.getToggles().add(rb1);
                        tg.getToggles().add(rb2);
                        vbaux.getChildren().add(hbTipoMens);
                    }
                    vbauxAno.getChildren().add(hbauxMes);
                }

                HBox hbbotao = new HBox();
                hbbotao.setAlignment(Pos.CENTER_RIGHT);
                hbbotao.setId("hbbotao");
                hbbotao.setSpacing(2);
                hbbotao.setPadding(new Insets(2, 2, 2, 2));

                auxLanc = new ArrayList();
                Button bt1 = new Button("Confirmar");
                bt1.setOnAction((ActionEvent event1) ->
                {
                    if (((DatePicker) ((VBox) (((VBox) vb_todo).getChildren().get(0))).getChildren().get(1)).getValue() != null)
                        auxLanc.add(new tipoLancamento(3, Timestamp.valueOf(((DatePicker) ((VBox) (((VBox) vb_todo).getChildren().get(0))).getChildren().get(1)).getValue().atStartOfDay())));
                    for (Node vb : vb_todo.getChildren())
                        if (vb.getUserData() != null)
                        {
                            int ano = (int) vb.getUserData();
                            for (Node nd : ((VBox) vb).getChildren())
                                if (nd instanceof HBox)
                                {
                                    int num = (int) ((HBox) nd).getUserData();
                                    if (num == 1)
                                    {
                                        for (Node dp : ((HBox) nd).getChildren())
                                            if (((DatePicker) dp).getValue() != null)
                                                auxLanc.add(new tipoLancamento(4, Timestamp.valueOf(((DatePicker) dp).getValue().atStartOfDay())));
                                    } else if (num == 2)
                                        for (Node vbm : ((HBox) nd).getChildren())
                                            if (((CheckBox) ((VBox) vbm).getChildren().get(0)).isSelected())
                                            {
                                                RadioButton rb1 = (RadioButton) ((HBox) ((VBox) vbm).getChildren().get(2)).getChildren().get(0);
                                                if (!rb1.isSelected())
                                                    rb1 = ((RadioButton) ((HBox) ((VBox) vbm).getChildren().get(2)).getChildren().get(1));
                                                int mes = (int) ((CheckBox) ((VBox) vbm).getChildren().get(0)).getUserData();
                                                int dia = (int) ((ComboBox) ((VBox) vbm).getChildren().get(1)).getValue();
                                                LocalDateTime ldt = LocalDateTime.of(ano, mes, dia, 0, 0);
                                                auxLanc.add(new tipoLancamento((int) rb1.getUserData(), Timestamp.valueOf(ldt)));
                                            }
                                }
                        }
                    //auxLanc.sort((o1, o2) -> o1.getData().compareTo(o2.getData()));
                    ((Stage) bt1.getScene().getWindow()).close();
                });

                AnchorPane pane = new AnchorPane();
                pane.setStyle("-fx-border-color : black;");
                hbbotao.getChildren().add(bt1);
                vb_todo.getChildren().add(hbbotao);
                pane.getChildren().add(vb_todo);

                Scene secondScene = new Scene(pane);
                rosolen.Rosolen.getInstance().addCSS(secondScene);

                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.initStyle(StageStyle.UNDECORATED);
                newWindow.getIcons().add(((Stage) dp_datacadastro.getScene().getWindow()).getIcons().get(0));
                newWindow.setTitle("MENSALIDADES");
                newWindow.setScene(secondScene);
                newWindow.toFront();
                newWindow.setResizable(false);
                newWindow.sizeToScene();
                newWindow.centerOnScreen();
                newWindow.showAndWait();
            }
        });

        bt_familia.setOnAction((event) ->
        {
            try
            {
                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.getIcons().add(((Stage) dp_datacadastro.getScene().getWindow()).getIcons().get(0));
                newWindow.setTitle("Grupo Familiar");
                Scene sc = new Scene(rosolen.Rosolen.getInstance().carregarFXML(FamiliarController.FXML));
                if (selecionado != null)
                    sc.setUserData(selecionado);
                newWindow.setScene(sc);
                newWindow.toFront();
                newWindow.setResizable(false);
                newWindow.sizeToScene();
                newWindow.centerOnScreen();
                newWindow.showAndWait();
            } catch (Exception ex)
            {
                Logger.getLogger(FichaCadastralController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        limpaTela();
    }

    public Usuario getInfoTela()
    {
        CtrPessoa ctrusu = new CtrPessoa();
        Usuario u;
        try
        {
            u = (Usuario) ctrusu.novoUsuario(status == 1 ? 0 : Integer.parseInt(txt_codigo.getText()), txt_nome.getText().toUpperCase(), txt_cpf.getText().replaceAll("[^A-Za-z0-9]", ""),
                    txt_telefone.getText().replaceAll("[^A-Za-z0-9 ]", ""), txt_email.getText(),
                    new Endereco(status == 1 ? 0 : Integer.parseInt(txt_codigo.getText()), txt_cep.getText().toUpperCase(), txt_tipologradouro.getText().toUpperCase(),
                            txt_logradouro.getText().toUpperCase(), txt_bairro.getText().toUpperCase(), txt_numero.getText().trim().isEmpty() ? 0 : Integer.parseInt(txt_numero.getText())),
                    foto.getImage() != null ? SwingFXUtils.fromFXImage(foto.getImage(), null) : null, ((String) Sexo.getSelectedToggle().getUserData()).charAt(0),
                    new CtrSituacao().get(1),
                    new Timestamp(Date.from(dp_datacadastro.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime()),
                    new Timestamp(Date.from(dp_datanascimento.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime()),
                    Float.parseFloat(txt_credito.getText().replace(",", ".")), 0, status == 1 ? 0 : Integer.parseInt(txt_codigo.getText()),
                    "", new CtrCategoria().get(3));

        } catch (NumberFormatException ex)
        {
            System.out.println(ex.getMessage());
            u = null;
        }
        return u;
    }

    public void limpaTela()
    {
        status = -1;
        txt_codigo.setDisable(true);

        bt_confirmar.setDisable(true);
        bt_cancelar.setDisable(true);
        bt_novo.setDisable(false);
        bt_alterar.setDisable(false);
        bt_apagar.setDisable(false);
        bt_verificar.setDisable(false);
        pn_dados.setDisable(true);
        bt_historico.setVisible(false);
        bt_ligar.setDisable(false);
        bt_capturar.setDisable(false);
        hb_procurar.setDisable(false);
        pn_procura.setDisable(true);
        rb_homem.setSelected(true);
        tabela.setDisable(false);
        txt_credito.setDisable(true);
        bt_familia.setDisable(false);
        txt_situacao.setDisable(false);
        TelaPrincipalController.getInstance().getMenu().setDisable(false);

        txt_codigo.setText("");
        txt_nome.setText("");
        txt_cpf.setText("");
        txt_telefone.setText("");
        txt_email.setText("");
        txt_cep.setText("");
        txt_tipologradouro.setText("");
        txt_logradouro.setText("");
        txt_bairro.setText("");
        txt_situacao.setText("");
        txt_numero.setText("");
        txt_procurar.setText("");
        txt_credito.setText("0");

        selecionado = null;
        tabela.setItems(null);
        foto.setImage(null);

        auxLanc = null;
        alfamilia = new ArrayList();
        aldelfamilia = new ArrayList();

        dp_datacadastro.setValue(LocalDate.now());
        dp_datanascimento.setValue(LocalDate.now());
        Platform.runLater(()
                ->
        {
            lb_resultado.setText("");
        });

        txt_situacao.setStyle("-fx-text-fill: white;"
                + "-fx-border-color: transparent transparent orange transparent;"
                + "-fx-font-weight: normal");
    }

    public void setInfoTela(Pessoa p)
    {
        if (p != null)
        {
            txt_codigo.setText(String.valueOf(p.getCod()));
            txt_nome.setText(p.getNome().toUpperCase());
            txt_cpf.setText(p.getCpf());
            txt_telefone.setText(p.getTelefone());
            txt_email.setText(p.getEmail());
            txt_credito.setText(String.valueOf(p.getCredito()));
            txt_cep.setText(p.getEnd().getCep());
            txt_tipologradouro.setText(p.getEnd().getTipolog());
            txt_logradouro.setText(p.getEnd().getLogradouro());
            txt_bairro.setText(p.getEnd().getBairro());
            txt_numero.setText("" + p.getEnd().getNumero());
            foto.setImage(p.getImg() != null ? SwingFXUtils.toFXImage(p.getImg(), null) : null);
            if (p.getSexo() == 'H')
                rb_homem.setSelected(true);
            else if (p.getSexo() == 'M')
                rb_mulher.setSelected(true);
            txt_situacao.setText(p.getSituacao().getDescricao());
            switch (p.getSituacao().getCod())
            {
                case 1:
                    txt_situacao.setStyle("-fx-text-fill: green;"
                            + "-fx-border-color: green;"
                            + "-fx-font-weight: bold;"
                            + "-fx-opacity: 1;");
                    break;

                case 2:
                    txt_situacao.setStyle("-fx-text-fill: red;"
                            + "-fx-border-color: red;"
                            + "-fx-font-weight: bold;"
                            + "-fx-opacity: 1;");
                    txt_situacao.setText(txt_situacao.getText() + " - " + new CtrLancamento().ultimaMensalidade(p.getCod()).plusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM")));
                    break;

                case 3:
                    txt_situacao.setStyle("-fx-text-fill: white;"
                            + "-fx-border-color: white;"
                            + "-fx-font-weight: normal;"
                            + "-fx-opacity: 1;");
                    break;

                case 4:
                    txt_situacao.setStyle("-fx-text-fill: yellow;"
                            + "-fx-border-color: yellow;"
                            + "-fx-font-weight: bold italic;"
                            + "-fx-opacity: 1;");
                    break;

                default:
                    txt_situacao.setStyle("-fx-text-fill: black;");
                    break;
            }
            dp_datacadastro.setValue(p.getDatacadastro().toLocalDateTime().toLocalDate());
            dp_datanascimento.setValue(p.getDatanascimento().toLocalDateTime().toLocalDate());
        }
    }

    @FXML
    private void evt_novo(ActionEvent event)
    {
        txt_codigo.setDisable(true);
        pn_dados.setDisable(false);
        hb_procurar.setDisable(true);
        tabela.setDisable(true);
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        bt_historico.setDisable(true);
        txt_credito.setDisable(false);
        txt_situacao.setDisable(true);
        TelaPrincipalController.getInstance().getMenu().setDisable(true);
        status = 1;
    }

    @FXML
    private void evt_alterar(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(true);
        bt_historico.setDisable(true);
        pn_procura.setDisable(false);
        tabela.setDisable(false);
        TelaPrincipalController.getInstance().getMenu().setDisable(true);
        status = 2;
    }

    @FXML
    private void evt_apagar(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_confirmar.setDisable(false);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(true);
        bt_historico.setDisable(true);
        bt_ligar.setDisable(true);
        bt_capturar.setDisable(true);
        pn_procura.setDisable(false);
        tabela.setDisable(false);
        TelaPrincipalController.getInstance().getMenu().setDisable(true);
        status = 3;
    }

    @FXML
    private void evt_verificar(ActionEvent event)
    {
        bt_novo.setDisable(true);
        bt_alterar.setDisable(true);
        bt_apagar.setDisable(true);
        bt_verificar.setDisable(true);
        bt_historico.setVisible(true);
        bt_confirmar.setDisable(true);
        bt_cancelar.setDisable(false);
        pn_dados.setDisable(true);
        bt_historico.setDisable(false);
        pn_procura.setDisable(false);
        tabela.setDisable(false);
        status = 0;
    }

    @FXML
    private void evt_confirmar(ActionEvent event)
    {

        boolean valida = true;

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Informações não preenchidas!");

        if (txt_nome.getText().trim().isEmpty())
        {
            valida = false;
            alerta.setHeaderText("Erro no NOME!");
            alerta.setContentText("O Nome precisa estar preenchido!");
            alerta.showAndWait();
            txt_nome.setStyle("-fx-border-color: transparent transparent red transparent;");
            txt_nome.requestFocus();
        }

        if (txt_email.getText().trim().isEmpty())
        {
            valida = false;
            alerta.setHeaderText("Erro no EMAIL!");
            alerta.setContentText("O e-mail precisa estar preenchido!");
            alerta.showAndWait();
            txt_email.setStyle("-fx-border-color: transparent transparent red transparent;");
            txt_email.requestFocus();
        }

        if (!txt_codigo.getText().trim().equals("0"))
        {
            if (valida)
            {

                CtrPessoa ctr = new CtrPessoa();
                switch (status)
                {
                    case 1:
                        String nome = txt_nome.getText();
                        ArrayList<Pessoa> alpes = new CtrPessoa().get(new Pessoa(), "select * from pessoa where nome ilike '%" + nome + "%'");

                        if (alpes != null && alpes.size() > 0)
                        {
                            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                            al.setContentText("Ja existe alguem com o nome '$1', deseja adicionar assim mesmo?".replace("$1", alpes.get(0).getNome()));
                            al.getButtonTypes().clear();
                            al.getButtonTypes().add(new ButtonType("Sim", ButtonBar.ButtonData.YES));
                            al.getButtonTypes().add(new ButtonType("Nao", ButtonBar.ButtonData.NO));
                            al.showAndWait();
                            if (al.getResult().getButtonData() == ButtonBar.ButtonData.YES)
                                valida = true;
                        }
                        if (valida)
                            if (valida = ctr.salvar(getInfoTela()))
                            {
                                addFamilia(ctr.getAtual());
                                delFamilia();
                                updateEnd(ctr.getAtual().getFamilia());

                                if (auxLanc != null && auxLanc.size() > 0)
                                {
                                    CtrLancamento ctrlan = new CtrLancamento();

                                    CtrServico ctrser = new CtrServico();
                                    Servico ser;
                                    for (tipoLancamento tl : auxLanc)
                                    {
                                        ser = ctrser.get(tl.getCod());
                                        ctrlan.salvar(new Lancamento(ctr.getAtual().getCod(), ctr.getAtual(), rosolen.Rosolen.getInstance().getSessao(), tl.getData(), tl.getData(),
                                                (tl.getCod() == 3 || tl.getCod() == 4), (tl.getCod() == 1 || tl.getCod() == 2), ser.getDescricao(), ser.getValor()));
                                    }

                                    String sql = "update pessoa set sit_cod=# where pes_cod=" + ctr.getAtual().getCod();
                                    LocalDate dataUltima = new CtrLancamento().ultimaMensalidade(ctr.getAtual().getCod());
                                    dataUltima = dataUltima.withDayOfMonth(1);

                                    LocalDate dataAtual = LocalDate.now().withDayOfMonth(1);
                                    if (dataUltima.equals(dataAtual) || dataUltima.isAfter(dataAtual))
                                        sql = sql.replace("#", "1");
                                    else if (dataUltima.equals(dataAtual.minusMonths(1)) || dataUltima.equals(dataAtual.minusMonths(2))
                                            || dataUltima.equals(dataAtual.minusMonths(3)))
                                        sql = sql.replace("#", "2");
                                    else
                                        sql = sql.replace("#", "3");

                                    /*if (ldUltima.getYear() > LocalDate.now().getYear())
                                        sql = sql.replace("#", "1");
                                    else if (ldUltima.getYear() == LocalDate.now().getYear()) // mesmo ano
                                    {
                                        if (ldUltima.getMonthValue() >= LocalDate.now().getMonthValue()) // mes atual ou adiantado - normal

                                            sql = sql.replace("#", "1");
                                        else if (ldUltima.plusMonths(1).getMonthValue() == LocalDate.now().getMonthValue()
                                                || ldUltima.plusMonths(2).getMonthValue() == LocalDate.now().getMonthValue()) // ate 2 meses atrasado - pendente

                                            sql = sql.replace("#", "2");
                                        else if (ldUltima.plusMonths(2).getMonthValue() < LocalDate.now().getMonthValue()) // mais de 2 meses atrasado - inativo

                                            sql = sql.replace("#", "3");
                                    } else if (ldUltima.getYear() + 1 == LocalDate.now().getYear()) //ano anterior
                                    {
                                        if (ldUltima.plusMonths(1).getMonthValue() == LocalDate.now().getMonthValue()
                                                || ldUltima.plusMonths(2).getMonthValue() == LocalDate.now().getMonthValue()) // ate 2 meses atrasado - pendente

                                            sql = sql.replace("#", "2");
                                        else if (ldUltima.plusMonths(2).getMonthValue() < LocalDate.now().getMonthValue()) // mais de 2 meses atrasado - inativo

                                            sql = sql.replace("#", "3");
                                    } else
                                        sql = sql.replace("#", "3");*/
                                    Conexao.get().manipular(sql);
                                }
                            }
                        break;
                    case 2:
                        valida = ctr.alterar(getInfoTela());
                        if (valida)
                        {
                            addFamilia(ctr.getAtual());
                            delFamilia();
                            updateEnd(ctr.getAtual().getFamilia());
                        }
                        break;
                    case 3:
                        valida = ctr.apagar(getInfoTela());
                        break;
                }

                if (valida)
                    limpaTela();
                else
                {
                    alerta.alertTypeProperty().set(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro na Gravação");
                    alerta.setHeaderText("Houve um erro ao registrar no sistema!\nContate o suporte técnico.");
                    alerta.setContentText(Conexao.get().getMensagemErro());
                    alerta.showAndWait();
                }
            }
        } else
        {
            alerta.alertTypeProperty().set(Alert.AlertType.ERROR);
            alerta.setTitle("Erro na Gravação");
            alerta.setHeaderText("Não é possivel modificar e/ou apagar o administrador.");
            alerta.setContentText(Conexao.get().getMensagemErro());
            alerta.showAndWait();
        }
    }

    private void addFamilia(Pessoa pes)
    {
        CtrPessoa ctr = new CtrPessoa();
        if (alfamilia != null && alfamilia.size() > 0)
            for (Pessoa fam : alfamilia)
            {
                fam.setFamilia(pes.getCod());
                ctr.alterar(fam);
            }
    }

    private void updateEnd(int famCod)
    {
        String sql = "select * from endereco "
                + "where pes_cod in (select pes_cod from pessoa where familia = $1) ".replace("$1", "" + famCod)
                + "and not coalesce(TRIM(logradouro), '') = ''";

        Endereco end = null;
        ArrayList<Endereco> alend = new CtrEndereco().get(sql);
        if (alend != null && alend.size() > 0)
            end = alend.get(0);

        if (end != null)
        {
            CtrPessoa ctr = new CtrPessoa();
            ArrayList<Pessoa> alp = ctr.get(new Pessoa(), "select * from pessoa where familia=" + famCod);
            for (Pessoa fam : alp)
                if (fam.getEnd().getLogradouro().trim().isEmpty())
                {
                    fam.setEnd(end);
                    fam.getEnd().setCod(fam.getCod());
                    ctr.alterar(fam);
                }
        }
    }

    private void delFamilia()
    {
        CtrPessoa ctr = new CtrPessoa();
        if (aldelfamilia != null && aldelfamilia.size() > 0)
        {
            int i = 0;
            //Busca se existe alguem "chefe" sendo excluido (pes_cod = familia)
            while (i < aldelfamilia.size() && aldelfamilia.get(i).getCod() != aldelfamilia.get(i).getFamilia())
                i++;

            if (i < aldelfamilia.size())
            {
                String sql = "select * from pessoa where not pes_cod=$1 and familia=$1";
                sql = sql.replace("$1", "" + aldelfamilia.get(i).getCod());
                ArrayList<Pessoa> alp = new CtrPessoa().get(new Pessoa(), sql);
                if (alp.size() > 0)
                {
                    int cod = alp.get(0).getCod();
                    for (Pessoa p : alp)
                    {
                        p.setFamilia(cod);
                        ctr.alterar(p);
                    }
                }
            } else
                for (Pessoa p : aldelfamilia)
                {
                    p.setFamilia(p.getCod());
                    ctr.alterar(p);
                }
        }
    }

    @FXML
    private void evt_cancelar(ActionEvent event)
    {
        limpaTela();
    }

    @FXML
    private void evt_procurar(ActionEvent event)
    {

        String filtro = "";
        if (!txt_procurar.getText().trim().isEmpty())
            filtro = "lower(nome) ilike '%" + txt_procurar.getText() + "%'" + " or " + "cpf like '%" + txt_procurar.getText() + "%'";
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Usuario(), filtro, "order by nome limit 20"));
        if (ob.size() > 0)
            tabela.setItems(ob);
        else
            tabela.setItems(null);
    }

    @FXML
    private void evt_ligar(ActionEvent event)
    {

        WebcamPanel panel = new WebcamPanel(webcam);
        window = new JFrame("WEBCAM");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setVisible(true);
        window.setAlwaysOnTop(true);
        window.pack();
        window.requestFocus();

    }

    @FXML
    private void evt_capturar(ActionEvent event) throws IOException
    {
        if (window != null)
        {
            foto.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
            window.dispose();
            webcam.close();
            window = null;
        }
    }

    @FXML
    private void evt_selecionar(MouseEvent event)
    {
        selecionado = (Usuario) tabela.getSelectionModel().getSelectedItem();
        if (selecionado != null)
        {
            pn_dados.setDisable(false);
            setInfoTela(selecionado);
        }
    }

    @FXML
    private void evt_historico(ActionEvent event) throws Exception
    {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Historico: " + selecionado.getNome());
        Scene sc = new Scene(rosolen.Rosolen.getInstance().carregarFXML(HistoricoController.FXML));
        sc.setUserData(selecionado);
        stage.setScene(sc);
        stage.toFront();
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
