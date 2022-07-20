/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import rosolen.db.controladoras.CtrLancamento;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.controladoras.CtrServico;
import rosolen.db.entidades.Lancamento;
import rosolen.db.entidades.Pessoa;
import rosolen.db.entidades.Servico;
import rosolen.db.entidades.Usuario;
import rosolen.db.util.Conexao;
import rosolen.db.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author thales
 */
public class LancamentosController implements Initializable
{

    //region Variaveis
    public static final String FXML = "Lancamentos.fxml";
    private float total;
    private float desconto;
    private float credito;
    private float avaliacao;
    private float mensalidade;
    private float troco;

    private Pessoa cliente;
    private static final DecimalFormat df = new DecimalFormat("###.##");
    @FXML
    private TextField txt_pagamento;
    @FXML
    private RadioButton rb_mensalidadeNormal;
    @FXML
    private RadioButton rb_creditoNao;
    @FXML
    private RadioButton rb_creditoSim;
    @FXML
    private RadioButton rb_descontoReais;
    @FXML
    private RadioButton rb_descontoPorcentagem;
    @FXML
    private RadioButton rb_notaSim;
    @FXML
    private RadioButton rb_notaNao;
    @FXML
    private Label lb_total;
    @FXML
    private Label lb_troco;
    @FXML
    private BorderPane pn_procura;
    @FXML
    private HBox hb_procurar;
    @FXML
    private TextField txt_cod;
    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_CPF;
    @FXML
    private TextField txt_telefone;
    @FXML
    private TextField txt_email;
    @FXML
    private ImageView fotoPessoa;
    @FXML
    private TableView<Pessoa> tabelaPessoa;
    @FXML
    private TableColumn<?, ?> col_cod;
    @FXML
    private TableColumn<?, ?> col_nome;
    @FXML
    private TableColumn<?, ?> col_cpf;
    @FXML
    private TableColumn<?, ?> col_telefone;
    @FXML
    private TableColumn<?, ?> col_email;
    @FXML
    private TextField txt_procurarPessoa;
    @FXML
    private CheckBox chk_mensalidade;
    @FXML
    private TextField txt_credito;
    @FXML
    private CheckBox chk_desconto;
    @FXML
    private TextField txt_desconto;
    @FXML
    private RadioButton rb_trococreditoNao;
    @FXML
    private RadioButton rb_trococreditoSim;
    @FXML
    private ToggleGroup grupoMensalidade;
    @FXML
    private ToggleGroup grupoDesconto;
    @FXML
    private ToggleGroup grupoCpfNota;
    @FXML
    private ToggleGroup grupoCredito;
    @FXML
    private ToggleGroup grupoTrocoCredito;
    @FXML
    private AnchorPane pn_dados1;
    @FXML
    private Label lb_desconto;
    @FXML
    private RadioButton rb_mensalidadeFamiliar;
    @FXML
    private DatePicker dp_UltimaMens;
    @FXML
    private DatePicker dp_MensalidadePagar;
    @FXML
    private TextField txt_situacao;
    @FXML
    private Button btn_registrarlancamento;
    @FXML
    private ToggleGroup grupoAvaliacao;
    @FXML
    private CheckBox chk_avaliacao;
    @FXML
    private RadioButton rb_AvNormal;
    @FXML
    private RadioButton rb_AvReavaliacao;
    @FXML
    private DatePicker dp_UltimaAval;
    @FXML
    private Label lb_data1;
    @FXML
    private DatePicker dp_AvaliacaoPagar;
    @FXML
    private Button bt_familia;
    @FXML
    private HBox todo;
    @FXML
    private ComboBox<Pacote> cb_pacote;

    private ArrayList<Pessoa> alpes;
    private ArrayList<Pessoa> aldelfamilia;
    @FXML
    private Label lb_divida;

    private class Pacote
    {

        int quantidade;
        String descricao;

        public Pacote(int quantidade, String descricao)
        {
            this.quantidade = quantidade;
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

        public String getDescricao()
        {
            return descricao;
        }

        public void setDescricao(String descricao)
        {
            this.descricao = descricao;
        }

        @Override
        public String toString()
        {
            return this.getDescricao();
        }
    }

    //endregion
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
        desconto = 0f;
        total = 0f;

        MaskFieldUtil.cpfField(txt_CPF);
        MaskFieldUtil.foneField(txt_telefone);
        MaskFieldUtil.onlyAlfaNumericValue(txt_procurarPessoa);
        MaskFieldUtil.onlyDigitsValue(txt_desconto);
        MaskFieldUtil.onlyDigitsValue(txt_pagamento);

        //Pessoa
        col_cod.setCellValueFactory(new PropertyValueFactory<>("cod"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        cb_pacote.getItems().add(new Pacote(1, "1 - Mes"));
        for (int i = 2; i <= 12; i++)
            cb_pacote.getItems().add(new Pacote(i, i + " - Meses"));
        cb_pacote.getSelectionModel().select(0);

        ImageView iv = new ImageView(rosolen.Rosolen.getInstance().getImagem("imagens/teamwork.png"));
        iv.setPreserveRatio(true);
        iv.setFitHeight(32);
        iv.setFitHeight(32);
        bt_familia.setGraphic(iv);
        alpes = new ArrayList();

        bt_familia.setOnAction((event)
                ->
        {
            alpes = new ArrayList();
            AnchorPane pane = new AnchorPane();
            pane.setStyle("-fx-border-color : orange;-fx-background-color : #000000;");
            VBox vb_todo = new VBox();
            vb_todo.setSpacing(2);
            vb_todo.setPadding(new Insets(2, 2, 2, 2));
            vb_todo.setId("todo");
            pane.getChildren().add(vb_todo);

            String sql = "select * from pessoa where not pes_cod = $1 and familia = $2";
            sql = sql.replace("$1", "" + cliente.getCod());
            sql = sql.replace("$2", "" + cliente.getFamilia());

            ArrayList<Pessoa> alp = new CtrPessoa().get(new Pessoa(), sql);

            if (alp != null && alp.size() > 0)
                for (Pessoa p : alp)
                {
                    CheckBox cb = new CheckBox(p.getNome());
                    cb.setTextFill(Paint.valueOf("white"));
                    cb.setSelected(false);
                    cb.setUserData(p);
                    cb.setPadding(new Insets(2, 2, 2, 2));
                    cb.selectedProperty().addListener((observable, oldValue, newValue)
                            ->
                    {
                        if (newValue)
                            alpes.add((Pessoa) cb.getUserData());
                        else
                            alpes.remove((Pessoa) cb.getUserData());
                    });

                    vb_todo.getChildren().add(cb);
                }
            else
            {
                Label lb = new Label("NÃO HÁ INTEGRANTES NA FAMÍLIA!");
                lb.setTextFill(Paint.valueOf("white"));
                lb.setPadding(new Insets(2, 2, 2, 2));
                vb_todo.getChildren().add(lb);
            }
            HBox hbbotao = new HBox();
            hbbotao.setAlignment(Pos.CENTER_RIGHT);
            hbbotao.setId("hbbotao");
            hbbotao.setSpacing(2);
            hbbotao.setPadding(new Insets(2, 2, 2, 2));
            vb_todo.getChildren().add(hbbotao);
            Button confirmar = new Button("Confirmar");
            confirmar.setOnAction((event1)
                    ->
            {
                validarDados();
                ((Stage) confirmar.getScene().getWindow()).close();
            });
            hbbotao.getChildren().add(confirmar);

            Button cancelar = new Button("cancelar");
            cancelar.setOnAction((event1)
                    ->
            {
                alpes = new ArrayList();
                validarDados();
                ((Stage) cancelar.getScene().getWindow()).close();
            });
            hbbotao.getChildren().add(cancelar);

            Scene secondScene = new Scene(pane);
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initStyle(StageStyle.UNDECORATED);
            newWindow.getIcons().add(((Stage) todo.getScene().getWindow()).getIcons().get(0));
            newWindow.setTitle("FAMILIARES");
            newWindow.setScene(secondScene);
            newWindow.toFront();
            newWindow.setResizable(false);
            newWindow.sizeToScene();
            newWindow.centerOnScreen();
            newWindow.showAndWait();
        });

        grupoAvaliacao.selectedToggleProperty().addListener((observable)
                ->
        {
            validarDados();
        });

        chk_avaliacao.selectedProperty().addListener((observable, oldValue, newValue)
                ->
        {
            grupoAvaliacao.getToggles().forEach((t)
                    ->
            {
                ((Node) t).setDisable(!newValue);
            });
            dp_AvaliacaoPagar.setDisable(!newValue);
            validarDados();
        });

        chk_mensalidade.selectedProperty().addListener((observable, oldValue, newValue)
                ->
        {
            grupoMensalidade.getToggles().forEach((t)
                    ->
            {
                ((Node) t).setDisable(!newValue);
            });
            cb_pacote.setDisable(!newValue);
            dp_MensalidadePagar.setDisable(!newValue);
            validarDados();
        });

        grupoMensalidade.selectedToggleProperty().addListener((observable)
                ->
        {
            validarDados();
        });

        cb_pacote.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                ->
        {
            System.out.println(newValue);
            validarDados();
        });

        chk_desconto.selectedProperty().addListener((observable, oldValue, newValue)
                ->
        {
            grupoDesconto.getToggles().forEach((t)
                    ->
            {
                ((Node) t).setDisable(!newValue);
            });
            txt_desconto.setDisable(!newValue);
            validarDados();
        });

        grupoDesconto.selectedToggleProperty().addListener((observable)
                ->
        {
            txt_desconto.setText("0");
            validarDados();
        });

        txt_desconto.focusedProperty().addListener((observable, oldValue, newValue)
                ->
        {
            if (!newValue)
                validarDados();
        });

        grupoCredito.selectedToggleProperty().addListener((observable)
                ->
        {
            validarDados();
        });

        txt_pagamento.focusedProperty().addListener((observable, oldValue, newValue)
                ->
        {
            if (!newValue)
                validarDados();
        });

        txt_desconto.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                validarDados();
        });

        txt_pagamento.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                validarDados();

        });
        txt_procurarPessoa.setOnKeyPressed((KeyEvent ke)
                ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
                evt_procurarPessoa(new ActionEvent());
        });

        limpaTela();
        validarDados();
    }

    private void limpaTela()
    {
        txt_cod.setText("");
        txt_nome.setText("");
        txt_CPF.setText("");
        txt_telefone.setText("");
        txt_email.setText("");
        txt_credito.setText("0");
        txt_procurarPessoa.setText("");
        txt_desconto.setText("0");
        txt_pagamento.setText("0");
        txt_situacao.setText("");

        fotoPessoa.setImage(null);

        tabelaPessoa.getItems().clear();

        aldelfamilia = new ArrayList();

        chk_mensalidade.setSelected(false);
        rb_mensalidadeNormal.setSelected(true);
        cb_pacote.getSelectionModel().select(0);
        dp_MensalidadePagar.setDisable(true);
        chk_avaliacao.setSelected(false);
        rb_AvNormal.setSelected(true);
        dp_AvaliacaoPagar.setDisable(true);
        chk_desconto.setSelected(false);
        rb_descontoReais.setSelected(true);
        rb_creditoNao.setSelected(true);
        rb_notaSim.setSelected(true);
        rb_trococreditoNao.setSelected(true);
        btn_registrarlancamento.setDisable(true);
        rb_AvNormal.setSelected(true);
        pn_dados1.setDisable(true);

        Platform.runLater(()
                ->
        {
            lb_desconto.setText("");
            lb_total.setText("0.00");
            lb_troco.setText("0.00");
        });
    }

    private void validarDados()
    {
        total = 0f;
        desconto = 0f;
        avaliacao = 0f;
        mensalidade = 0f;
        credito = 0f;

        if (cliente != null)
        {
            if (chk_avaliacao.isSelected())
                if (rb_AvNormal.isSelected())
                    total += avaliacao = new CtrServico().get(3).getValor();
                else if (rb_AvReavaliacao.isSelected())
                    total += avaliacao = new CtrServico().get(4).getValor();

            if (chk_mensalidade.isSelected())
            {
                Servico serSelecionado;
                if (rb_mensalidadeNormal.isSelected())
                    serSelecionado = new CtrServico().get(1);
                else
                    serSelecionado = new CtrServico().get(2);

                mensalidade = serSelecionado.getValor();

                total += mensalidade * cb_pacote.getValue().getQuantidade();
            }

            total = (total * (1 + alpes.size()));

            if (chk_desconto.isSelected())
            {
                float valor = 0f;
                if (txt_desconto.getText().trim().isEmpty())
                    txt_desconto.setText("0.0");
                else
                {
                    valor = Float.parseFloat(txt_desconto.getText().replace(",", "."));
                    if (valor < 0)
                    {
                        valor *= -1f;
                        txt_desconto.setText(String.valueOf(valor));
                    }
                }

                if (valor > 0f)
                {
                    if (rb_descontoReais.isSelected())
                    {
                        if (valor > total)
                            valor = total;

                        desconto = valor / total;
                    } else
                    {
                        if (valor > 100)
                            valor = 100f;

                        desconto = valor / 100f;
                    }
                    txt_desconto.setText(String.valueOf(valor));
                }
            }

            if (rb_creditoSim.isSelected())
                credito = cliente.getCredito();

            float valor = 0f;
            if (txt_pagamento.getText().trim().isEmpty())
                txt_pagamento.setText("0.0");
            else
            {
                valor = Float.parseFloat(txt_pagamento.getText().replace(",", "."));
                if (valor < 0)
                    valor *= -1f;
                txt_pagamento.setText(String.valueOf(valor));
            }

            float aux = (total - credito - (total * desconto));
            boolean desabilitar = true;
            if (chk_avaliacao.isSelected() || chk_mensalidade.isSelected())
                desabilitar = false;
            btn_registrarlancamento.setDisable(desabilitar);

            String stotal = String.valueOf(df.format(aux));
            troco = valor - aux;

            Platform.runLater(()
                    ->
            {
                if (desconto > 0)
                    lb_desconto.setText("-" + df.format(total * desconto) + "R$");
                else
                    lb_desconto.setText("");
                lb_total.setText(stotal);
                if (troco >= 0)
                {
                    lb_troco.setText(df.format(troco));
                    lb_divida.setText(df.format(0f));
                } else
                {
                    lb_troco.setText(df.format(0f));
                    lb_divida.setText(df.format(troco));
                }
            });
        }
    }

    @FXML
    private void evt_procurarPessoa(ActionEvent event)
    {
        String filtro = "";
        if (!txt_procurarPessoa.getText().trim().isEmpty())
            filtro = "lower(nome) like '%" + txt_procurarPessoa.getText() + "%'" + " or " + "cpf like '%" + txt_procurarPessoa.getText() + "%'";
        ObservableList<Pessoa> ob = FXCollections.observableArrayList(new CtrPessoa().get(new Usuario(), filtro, "order by nome limit 20"));
        if (ob.size() > 0)
            tabelaPessoa.setItems(ob);
        else
            tabelaPessoa.setItems(null);
    }

    @FXML
    private void evt_selecionarPessoa(MouseEvent event)
    {
        cliente = tabelaPessoa.getSelectionModel().getSelectedItem();
        if (cliente != null)
        {
            aldelfamilia = new ArrayList();
            txt_cod.setText("" + cliente.getCod());
            txt_nome.setText(cliente.getNome());
            txt_CPF.setText(cliente.getCpf());
            txt_telefone.setText(cliente.getTelefone());
            txt_email.setText(cliente.getEmail());
            txt_situacao.setText(cliente.getSituacao().getDescricao().toUpperCase());

            String sql = "select * from pessoa where not pes_cod=$1 and familia=$2";
            sql = sql.replace("$1", "" + cliente.getCod());
            sql = sql.replace("$2", "" + cliente.getFamilia());
            ArrayList<Pessoa> alp = new CtrPessoa().get(new Pessoa(), sql);
            if (alp != null && alp.size() > 0)
                rb_mensalidadeFamiliar.setSelected(true);
            else
                rb_mensalidadeNormal.setSelected(true);

            switch (cliente.getSituacao().getCod())
            {
                case 1:
                    txt_situacao.setStyle("-fx-text-fill: green;");
                    break;

                case 2:
                    txt_situacao.setStyle("-fx-text-fill: red;");
                    break;

                case 3:
                    txt_situacao.setStyle("-fx-text-fill: black;");
                    break;

                case 4:
                    txt_situacao.setStyle("-fx-text-fill: orange;");
                    break;

                default:
                    txt_situacao.setStyle("-fx-text-fill: black;");
                    break;
            }

            dp_UltimaAval.setValue(new CtrLancamento().ultimaAvaliacao(cliente.getCod()));
            dp_AvaliacaoPagar.setValue(LocalDate.now());

            dp_UltimaMens.setValue(new CtrLancamento().ultimaMensalidade(cliente.getCod()));
            dp_UltimaMens.setStyle(cliente.getSituacao().getCod() == 2 ? "-fx-text-fill: red;" : "-fx-text-fill: black;");
            if (dp_UltimaMens.getValue().getYear() != 1900)
                dp_MensalidadePagar.setValue(dp_UltimaMens.getValue().plusMonths(1));
            else
                dp_MensalidadePagar.setValue(LocalDate.now());

            txt_credito.setText("" + cliente.getCredito());
            fotoPessoa.setImage(cliente.getImg() != null ? SwingFXUtils.toFXImage(cliente.getImg(), null) : null);
            pn_dados1.setDisable(false);
        }
    }

    @FXML
    private void evt_registrarLancamento(ActionEvent event)
    {
        boolean valido = false;
        LocalDate ldultima = dp_UltimaMens.getValue();
        LocalDate ldpagar = dp_MensalidadePagar.getValue();
        aldelfamilia = new ArrayList();

        String sql = "select * from pessoa where not pes_cod=$1 and familia=$2 and sit_cod=2";
        sql = sql.replace("$1", "" + cliente.getCod());
        sql = sql.replace("$2", "" + cliente.getFamilia());
        ArrayList<Pessoa> alp = new CtrPessoa().get(new Pessoa(), sql);
        if (alp != null && alp.size() > 0)
            for (Pessoa p : alp)
            {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setContentText("O integrante familiar '$1' está pendente, deseja retira-lo do grupo familiar?".replace("$1", p.getNome()));
                al.getButtonTypes().clear();
                al.getButtonTypes().add(new ButtonType("Sim", ButtonBar.ButtonData.YES));
                al.getButtonTypes().add(new ButtonType("Nao", ButtonBar.ButtonData.NO));
                al.showAndWait();
                if (al.getResult().getButtonData() == ButtonBar.ButtonData.YES)
                {
                    if (alpes != null && alpes.size() > 0)
                    {
                        int i = 0;
                        for (; i < alpes.size() && !alpes.get(i).getNome().equals(p.getNome()); i++);
                        if (i < alpes.size())
                            alpes.remove(i);
                    }
                    aldelfamilia.add(p);
                    if ((alp.size() - aldelfamilia.size()) > 0)
                        rb_mensalidadeFamiliar.setSelected(true);
                    else
                        rb_mensalidadeNormal.setSelected(true);
                }
            }

        if (aldelfamilia.size() > 0)
            delFamilia(new CtrPessoa());
        else
        {

            if (ldultima.getYear() != 1900)
                if (ldultima.getYear() < ldpagar.getYear())
                    if (ldultima.getYear() + 1 == ldpagar.getYear() && ldultima.plusMonths(1).getMonthValue() == ldpagar.getMonthValue())
                        valido = true;
                    else
                    {
                        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                        al.setContentText("Há mensalidades a ser pagas ainda, deseja realmente anulá-las?");
                        al.getButtonTypes().clear();
                        al.getButtonTypes().add(new ButtonType("Sim", ButtonBar.ButtonData.YES));
                        al.getButtonTypes().add(new ButtonType("Nao", ButtonBar.ButtonData.NO));
                        al.showAndWait();
                        if (al.getResult().getButtonData() == ButtonBar.ButtonData.YES)
                            valido = true;
                    }
                else if (ldultima.getYear() == ldpagar.getYear())
                    if (ldultima.getMonthValue() == ldpagar.getMonthValue())
                        new Alert(Alert.AlertType.ERROR, "Essa mensalidade já foi paga!", ButtonType.OK).showAndWait();
                    else if (ldultima.getMonthValue() + 1 == ldpagar.getMonthValue())
                        valido = true;
                    else
                    {
                        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                        al.setContentText("Há mensalidades a ser pagas ainda, deseja realmente anulá-las?");
                        al.getButtonTypes().clear();
                        al.getButtonTypes().add(new ButtonType("Sim", ButtonBar.ButtonData.YES));
                        al.getButtonTypes().add(new ButtonType("Nao", ButtonBar.ButtonData.NO));
                        al.showAndWait();
                        if (al.getResult().getButtonData() == ButtonBar.ButtonData.YES)
                            valido = true;
                    }
                else
                    new Alert(Alert.AlertType.ERROR, "Ano da mensalidade a pagar nao pode ser menor que o ano da ultima mensalidade paga!", ButtonType.OK).showAndWait();
            else
                valido = true;

            if (valido)
            {

                float auxCredito = 0f;
                if (rb_creditoSim.isSelected())
                {
                    if (rb_trococreditoSim.isSelected())
                        auxCredito = troco;
                } else
                {
                    auxCredito = cliente.getCredito();
                    if (rb_trococreditoSim.isSelected())
                        auxCredito += troco;
                }

                if (troco < 0)
                {
                    Alert al = new Alert(Alert.AlertType.WARNING);
                    al.setContentText("Valor pago menor que o montante, deseja aplicar como divida?");
                    al.getButtonTypes().clear();
                    al.getButtonTypes().add(new ButtonType("Sim", ButtonBar.ButtonData.YES));
                    al.getButtonTypes().add(new ButtonType("Nao", ButtonBar.ButtonData.NO));
                    al.showAndWait();
                    if (al.getResult().getButtonData() == ButtonBar.ButtonData.YES)
                        auxCredito += troco;
                }

                sql = "update pessoa set credito = $1 where pes_cod = $2";
                sql = sql.replace("$1", "" + auxCredito);
                sql = sql.replace("$2", "" + cliente.getCod());
                Conexao.get().manipular(sql);

                float parcelas = cb_pacote.getValue().getQuantidade();

                ArrayList<Lancamento> all = new ArrayList();

                CtrServico ser = new CtrServico();
                LocalDateTime dataPagamentoAva = dp_MensalidadePagar.getValue().atStartOfDay();
                float valAval = avaliacao - (avaliacao * desconto);
                if (chk_avaliacao.isSelected())
                {
                    int cod = 0;

                    if (rb_AvNormal.isSelected())
                        cod = 3;
                    else if (rb_AvReavaliacao.isSelected())
                        cod = 4;

                    all.add(new Lancamento(cliente, rosolen.Rosolen.getInstance().getSessao(),
                            Timestamp.valueOf(dataPagamentoAva), Timestamp.valueOf(LocalDateTime.now()), true, false, ser.get(cod).getDescricao(), valAval));
                    for (Pessoa auxp : alpes)
                        all.add(new Lancamento(auxp, rosolen.Rosolen.getInstance().getSessao(),
                                Timestamp.valueOf(dataPagamentoAva), Timestamp.valueOf(LocalDateTime.now()), true, false, ser.get(cod).getDescricao(), valAval));
                }

                LocalDateTime dataPagamentoMens = dp_MensalidadePagar.getValue().atStartOfDay();
                float valParc = mensalidade - (mensalidade * desconto);
                if (chk_mensalidade.isSelected())
                {
                    Lancamento lan;

                    int cod = 0;
                    if (rb_mensalidadeNormal.isSelected())
                        cod = 1;
                    else if (rb_mensalidadeFamiliar.isSelected())
                        cod = 2;

                    if (parcelas == 1)
                    {
                        all.add(new Lancamento(cliente, rosolen.Rosolen.getInstance().getSessao(),
                                Timestamp.valueOf(dataPagamentoMens), Timestamp.valueOf(LocalDateTime.now()), false, true,
                                ser.get(cod).getDescricao(), valParc));
                        for (Pessoa auxp : alpes)
                            all.add(new Lancamento(auxp, rosolen.Rosolen.getInstance().getSessao(),
                                    Timestamp.valueOf(dataPagamentoMens), Timestamp.valueOf(LocalDateTime.now()), false, true,
                                    ser.get(cod).getDescricao(), valParc));
                    } else
                    {

                        String combo = "Pacote " + cb_pacote.getValue().getDescricao() + " / Mes";

                        LocalDateTime ldtaux = dataPagamentoMens;
                        for (int i = 0; i < parcelas; i++, ldtaux = ldtaux.plusMonths(1))
                        {
                            all.add(new Lancamento(cliente, rosolen.Rosolen.getInstance().getSessao(),
                                    Timestamp.valueOf(ldtaux), Timestamp.valueOf(LocalDateTime.now()), false, true,
                                    combo + " " + (i + 1) + " / " + ser.get(cod).getDescricao(), valParc));
                            for (Pessoa auxp : alpes)
                                all.add(new Lancamento(auxp, rosolen.Rosolen.getInstance().getSessao(),
                                        Timestamp.valueOf(ldtaux), Timestamp.valueOf(LocalDateTime.now()), false, true,
                                        combo + " " + (i + 1) + " / " + ser.get(cod).getDescricao(), valParc));
                        }
                    }
                }

                if (all.size() > 0)
                {
                    CtrLancamento ctrlan = new CtrLancamento();
                    for (Lancamento l : all)
                        ctrlan.salvar(l);

                    if (alpes == null)
                        alpes = new ArrayList();
                    alpes.add(cliente);

                    for (Pessoa pe : alpes)
                    {
                        sql = "update pessoa set sit_cod=# where pes_cod=" + pe.getCod();
                        LocalDate ldUltima = new CtrLancamento().ultimaMensalidade(pe.getCod());

                        if (ldUltima.getYear() > LocalDate.now().getYear())
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
                            sql = sql.replace("#", "3");

                        Conexao.get().manipular(sql);
                    }
                    /*if (Conexao.get().manipular(sql))
                    {
                        cliente = new CtrPessoa().get(cliente);
                        if (cliente.getSituacao().getCod() == 1)
                        {
                            all.clear();
                            all = ctrlan.getAll("pes_cod = " + cliente.getCod() + " and mensalidade = true", "order by datamens desc");
                            if (all.size() > 11)
                            {
                                valido = true;
                                int i;
                                for (i = 0; i < all.size() - 1 && valido; i++)
                                {
                                    int aux = all.get(i).getDataMens().toLocalDateTime().getMonthValue();
                                    if (aux == 1)
                                    {
                                        if (all.get(i + 1).getDataMens().toLocalDateTime().getMonthValue() != 12)
                                            valido = false;
                                    } else if (aux - 1 != all.get(i + 1).getDataMens().toLocalDateTime().getMonthValue())
                                        valido = false;
                                }

                                if (valido || i >= 11)
                                {
                                    sql = "update pessoa set sit_cod=4, mesvip = " + (i + 1) + " where pes_cod=" + cliente.getCod();
                                    Conexao.get().manipular(sql);
                                }
                            }
                        }
                    }*/
                }
            }
            rosolen.Rosolen.getInstance().atualizarSituacao();
            limpaTela();
        }
    }

    private void delFamilia(CtrPessoa ctr)
    {
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
}
