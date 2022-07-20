/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.UI;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import rosolen.db.controladoras.CtrLancamento;
import rosolen.db.controladoras.CtrPessoa;
import rosolen.db.controladoras.CtrServico;
import rosolen.db.entidades.Lancamento;
import rosolen.db.entidades.Pessoa;
import rosolen.db.entidades.Usuario;

/**
 * FXML Controller class
 *
 * @author thale
 */
public class HistoricoController implements Initializable
{

    public static final String FXML = "Historico.fxml";
    @FXML
    private TableColumn<?, ?> col_data;
    @FXML
    private TableColumn<?, ?> col_descricao;
    @FXML
    private TableColumn<?, ?> col_valor;
    @FXML
    private TableColumn<?, ?> col_funcionario;
    @FXML
    private TableView<historico> tabelaHistorico;
    @FXML
    private BorderPane pn_historico;
    @FXML
    private TableColumn<?, ?> col_dataPagamento;

    public class historico
    {

        private String data;
        private String dataPagamento;
        private String descricao;
        private String valor;
        private String funcionario;

        public historico(String data, String dataPagamento, String descricao, String valor, String funcionario)
        {
            this.data = data;
            this.dataPagamento = dataPagamento;
            this.descricao = descricao;
            this.valor = valor;
            this.funcionario = funcionario;
        }

        public String getData()
        {
            return data;
        }

        public void setData(String data)
        {
            this.data = data;
        }

        public String getDataPagamento()
        {
            return dataPagamento;
        }

        public void setDataPagamento(String dataPagamento)
        {
            this.dataPagamento = dataPagamento;
        }

        public String getDescricao()
        {
            return descricao;
        }

        public void setDescricao(String descricao)
        {
            this.descricao = descricao;
        }

        public String getValor()
        {
            return valor;
        }

        public void setValor(String valor)
        {
            this.valor = valor;
        }

        public String getFuncionario()
        {
            return funcionario;
        }

        public void setFuncionario(String funcionario)
        {
            this.funcionario = funcionario;
        }

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
        //TABELA HISTORICO
        col_data.setCellValueFactory(new PropertyValueFactory<>("data"));
        col_dataPagamento.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        col_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        col_funcionario.setCellValueFactory(new PropertyValueFactory<>("funcionario"));

        Platform.runLater(() ->
        {
            Usuario p = (Usuario) pn_historico.getScene().getUserData();
            if (p != null)
            {
                ObservableList<historico> obh = FXCollections.observableArrayList();

                pn_historico.getScene().setCursor(Cursor.WAIT);
                //traz todos lançamentos da familia
                String sql = "select * from lancamento where pes_cod in (select pes_cod from pessoa where familia = $1) order by datamens desc";
                sql = sql.replace("$1", "" + p.getFamilia());
                ArrayList<Lancamento> allan = new CtrLancamento().getAll(sql);
                CtrServico ser = new CtrServico();
                //Familia possui lancamentos
                if (allan.size() > 0)
                    allan.forEach((Lancamento lan) ->
                    {
                        if (lan.getCliente().getCod() == p.getCod())
                        {
                            ArrayList<Lancamento> lanIguais;
                            //Filtra mensalidade na mesma data
                            lanIguais = new ArrayList(allan.stream().filter((aux) -> (aux.isMensalidade() == lan.isMensalidade()
                                    && aux.getDataMens().toLocalDateTime().getYear() == lan.getDataMens().toLocalDateTime().getYear()
                                    && aux.getDataMens().toLocalDateTime().getMonthValue() == lan.getDataMens().toLocalDateTime().getMonthValue()
                                    && aux.getDataPagamento().toLocalDateTime().getDayOfMonth() == lan.getDataPagamento().toLocalDateTime().getDayOfMonth())
                            ).collect(Collectors.toList()));

                            lanIguais.sort((t1, t2) -> t1.getCliente().getNome().compareTo(t2.getCliente().getNome()));

                            int i = 0;
                            float total;
                            String fam;

                            //Obtem os nomes e os valores
                            fam = lanIguais.get(i).getDescricao() + "$1" + lanIguais.get(i).getCliente().getNome().split(" ")[0];
                            total = lanIguais.get(i).getTotal();

                            if (lanIguais.size() > 1)
                            {
                                fam = fam.replace("$1", " (Junto): ");
                                for (i = 1; i < lanIguais.size(); i++)
                                {
                                    fam += "-" + lanIguais.get(i).getCliente().getNome().split(" ")[0];
                                    total += lanIguais.get(i).getTotal();
                                }
                            } else
                            {
                                fam = fam.replace("$1", " (Separado): ");
                                total = lanIguais.get(i).getTotal();
                            }

                            Lancamento lanEdit = lan;
                            lanEdit.setDescricao(fam);
                            lanEdit.setTotal(total);
                            obh.add(new historico(lanEdit.getDataMens().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    lanEdit.getDataPagamento().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    lanEdit.getDescricao(), "" + lanEdit.getTotal(), new CtrPessoa().get(new Pessoa(), lanEdit.getAcademia().getCod()).getNome()));
                        }
                    });

                pn_historico.getScene().setCursor(Cursor.DEFAULT);
                tabelaHistorico.setItems(obh.size() > 0 ? obh : null);
            }
        });

    }

}
