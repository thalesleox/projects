/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.UI;

import compilador.Entidade.Dicionario;
import compilador.Entidade.palavraReservada;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 *
 * @author Aluno
 */
public class PrincipalController implements Initializable
{

    public static String FXML = "Principal.fxml";
    @FXML
    private CodeArea codeArea;
    Dicionario dic;
    ArrayList<TokenLexema> listaTL;
    @FXML
    private TextArea txt_saida;

    class TokenLexema
    {

        private String token;
        private String lexema;
        private int pos;

        public TokenLexema(String token, String lexema, int pos)
        {
            this.token = token;
            this.lexema = lexema;
            this.pos = pos;
        }

        public String getToken()
        {
            return token;
        }

        public void setToken(String token)
        {
            this.token = token;
        }

        public String getLexema()
        {
            return lexema;
        }

        public void setLexemas(String lexemas)
        {
            this.lexema = lexemas;
        }

        public int getPos()
        {
            return pos;
        }

        public void setPos(int pos)
        {
            this.pos = pos;
        }

        @Override
        public String toString()
        {
            return "TokenLexema{" + "token=" + token + ", lexema=" + lexema + ", pos=" + pos + '}';
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        dic = new Dicionario();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        listaTL = new ArrayList<>();
    }

    @FXML
    private void novoCodigo(ActionEvent event)
    {
        codeArea.clear();
        listaTL.clear();
    }

    @FXML
    private void abrirCodigo(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        File f = fc.showOpenDialog(null);
        if (f != null)
            try
            {
                byte[] encoded = Files.readAllBytes(Paths.get(f.getPath()));
                codeArea.clear();
                codeArea.appendText(new String(encoded, StandardCharsets.UTF_8));
            } catch (FileNotFoundException ex)
            {
                saida("Arquivo nao existe!");
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex)
            {
                saida("Erro ao abrir o arquivo: " + ex);
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void salvarCodigo(ActionEvent event)
    {
    }

    @FXML
    private void evt_gerarTabela(ActionEvent event)
    {
        if (!codeArea.getText().trim().isEmpty())
        {

            analisadorLexico(codeArea.getText());

            txt_saida.clear();
            txt_saida.setText("Tokens\t\t\t\t\tLexemas");
            listaTL.forEach((tl) ->
            {
                txt_saida.appendText("\n" + tl.getToken() + "\t\t\t\t\t" + tl.getLexema());
            });

        }
    }

    private void analisadorLexico(String codigo)
    {
        listaTL.clear();
        String palavraAtual = "";
        int i;
        for (i = 0; i < codigo.length(); palavraAtual = "")
            if (codigo.charAt(i) != ' ' && codigo.charAt(i) != '\t' && codigo.charAt(i) != '\n')
                if (Character.isLetter(codigo.charAt(i))) // automato para detectar palavraReservada/id
                {
                    //verifica se esta lendo somente letra
                    while (i < codigo.length() && Character.isLetter(codigo.charAt(i)))
                        palavraAtual += codigo.charAt(i++);

                    // leu algo diferente de letra
                    if (i < codigo.length())
                    {
                        //verifica se tem continuacao com numero ou underline
                        while (i < codigo.length() && (Character.isLetterOrDigit(codigo.charAt(i)) || codigo.charAt(i) == '_'))
                            palavraAtual += codigo.charAt(i++);

                        //saiu porque leu algum caractere nao identificado
                        if (i != codigo.length() && !Character.isLetterOrDigit(codigo.charAt(i)) && codigo.charAt(i) != '_')
                        {
                            //se for palavra reservada atribui o token dela, senao, id
                            palavraReservada pr = dic.getPalavra(palavraAtual);
                            listaTL.add(new TokenLexema(pr != null ? pr.getToken() : "tk_id", palavraAtual, i - palavraAtual.length()));
                        }
                    } else//chegou no fim
                    {
                        palavraReservada pr = dic.getPalavra(palavraAtual);
                        listaTL.add(new TokenLexema(pr != null ? pr.getToken() : "tk_id", palavraAtual, i - palavraAtual.length()));
                    }

                } else if (codigo.charAt(i) == '-' || Character.isDigit(codigo.charAt(i)))// automato identificador de numeros ou sinal isolado de menos
                {
                    if (codigo.charAt(i) == '-')
                        palavraAtual += codigo.charAt(i++);
                    if (i < codigo.length() && Character.isDigit(codigo.charAt(i)))
                    {
                        while (i < codigo.length() && Character.isDigit(codigo.charAt(i)))
                            palavraAtual += codigo.charAt(i++);

                        if (i < codigo.length())
                            if (codigo.charAt(i) == '.')
                            {
                                palavraAtual += codigo.charAt(i++);
                                if (Character.isDigit(codigo.charAt(i)))
                                {
                                    while (i < codigo.length() && Character.isDigit(codigo.charAt(i)))
                                        palavraAtual += codigo.charAt(i++);
                                    listaTL.add(new TokenLexema("tk_numeroFloat", palavraAtual, i - palavraAtual.length()));
                                } else
                                {
                                    //erro lexico
                                    String novaPalavra = palavraAtual.substring(0, palavraAtual.length() - 1);
                                    saida("ERRO LEXICO: numero " + palavraAtual + codigo.charAt(i) + " desconhecido, será atribuido " + novaPalavra + " para o ID.");
                                    listaTL.add(new TokenLexema("tk_numeroInt", novaPalavra, i - palavraAtual.length()));
                                }
                            } else
                                listaTL.add(new TokenLexema("tk_numeroInt", palavraAtual, i - palavraAtual.length()));
                        else
                            listaTL.add(new TokenLexema("tk_numeroInt", palavraAtual, i - palavraAtual.length()));
                    } else if (i == codigo.length())
                        if (Character.isDigit(codigo.charAt(i - 1)))
                            listaTL.add(new TokenLexema("tk_numeroInt", palavraAtual, i - palavraAtual.length()));
                        else
                            listaTL.add(new TokenLexema("tk_operadorMenos", palavraAtual, i - palavraAtual.length()));
                    else
                        listaTL.add(new TokenLexema("tk_operadorMenos", palavraAtual, i - palavraAtual.length()));
                } else if (codigo.charAt(i) == '\'') // automato identificador de char
                    if (i + 2 < codigo.length() && codigo.charAt(i + 2) == '\'')
                    {
                        palavraAtual += codigo.charAt(i++);
                        palavraAtual += codigo.charAt(i++);
                        palavraAtual += codigo.charAt(i++);
                        listaTL.add(new TokenLexema("tk_valorChar", palavraAtual, i - palavraAtual.length()));
                    } else
                        i++;
                else if (codigo.charAt(i) == '\"') // automato identificador de string
                {
                    palavraAtual += codigo.charAt(i++);
                    int j = i;
                    String auxPA = palavraAtual;
                    while (j < codigo.length() && codigo.charAt(j) != '\"')
                        auxPA += codigo.charAt(j++);
                    if (j < codigo.length()) // achou "
                    {
                        auxPA += codigo.charAt(j++);
                        i = j;
                        listaTL.add(new TokenLexema("tk_valorString", auxPA, i - auxPA.length()));
                    } else
                        listaTL.add(new TokenLexema("tk_AbreAspas", palavraAtual, i - palavraAtual.length()));

                } else if (codigo.charAt(i) == '/') // automato identificador de comentario // ou /* */
                {
                    palavraAtual += codigo.charAt(i++);
                    if (i < codigo.length())
                    {
                        char c = codigo.charAt(i);
                        if (c == '/' || c == '*')
                        {
                            palavraAtual += codigo.charAt(i++);
                            switch (c)
                            {
                                case '/':
                                    while (i < codigo.length() && codigo.charAt(i) != '\t')
                                        palavraAtual += codigo.charAt(i++);
                                    listaTL.add(new TokenLexema("tk_comentarioInline", palavraAtual, i - palavraAtual.length()));
                                    break;

                                case '*':
                                    boolean continuar = true;
                                    do
                                    {
                                        while (i < codigo.length() && codigo.charAt(i) != '*')
                                            palavraAtual += codigo.charAt(i++);
                                        if (i < codigo.length()) // achou *
                                        {
                                            palavraAtual += codigo.charAt(i++); // adiciona *
                                            if (i == codigo.length())
                                            {
                                                listaTL.add(new TokenLexema("tk_comentarioBloco", palavraAtual, i - palavraAtual.length()));
                                                continuar = false;
                                            } else if (codigo.charAt(i) == '/')
                                            {
                                                palavraAtual += codigo.charAt(i++);
                                                listaTL.add(new TokenLexema("tk_comentarioBloco", palavraAtual, i - palavraAtual.length()));
                                                continuar = false;
                                            }
                                        } else
                                        {
                                            listaTL.add(new TokenLexema("tk_comentarioBloco", palavraAtual, i - palavraAtual.length()));
                                            continuar = false;
                                        }
                                    } while (continuar);

                                    break;
                            }
                        } else
                        {
                            palavraReservada pr = dic.getPalavra(palavraAtual);
                            listaTL.add(new TokenLexema(pr != null ? pr.getToken() : "tk_barra", palavraAtual, i - palavraAtual.length()));
                        }
                    } else
                    {
                        palavraReservada pr = dic.getPalavra(palavraAtual);
                        listaTL.add(new TokenLexema(pr != null ? pr.getToken() : "tk_barra", palavraAtual, i - palavraAtual.length()));
                    }
                } else // automato identificandor do dicionario da linguagem
                {
                    palavraReservada pr = dic.getPalavra("" + codigo.charAt(i));
                    if (pr != null)
                    {
                        String lexema = "" + codigo.charAt(i++);
                        if (i < codigo.length())
                        {
                            palavraReservada pr2 = dic.getPalavra("" + codigo.charAt(i - 1) + codigo.charAt(i));
                            if (pr2 != null)
                            {
                                pr = pr2;
                                lexema = "" + codigo.charAt(i - 1) + codigo.charAt(i++);
                            }
                        }
                        listaTL.add(new TokenLexema(pr.getToken(), lexema, i - palavraAtual.length()));
                    } else //erro lexico
                        i++;
                }
            else
                i++;
        listaTL.add(new TokenLexema("tk_FIM", "", i));
    }

    @FXML
    private void evt_verificarSintaxe(ActionEvent event)
    {
        txt_saida.clear();
        if (!codeArea.getText().trim().isEmpty())
        {
            analisadorLexico(codeArea.getText());
            analisadorSintatico(0);
        }
    }

    private int fun_main(int pos)
    {
        TokenLexema tl = listaTL.get(pos);
        if (!tl.getToken().equals("tk_FIM"))
        {
            if (tl.getToken().equals("tk_abreParentese"))
                tl = listaTL.get(++pos);
            else
                saida("Função main: ERRO - era esperado '('");

            if (!tl.getToken().equals("tk_FIM"))
            {
                if (tl.getToken().equals("tk_fechaParentese"))
                    tl = listaTL.get(++pos);
                else
                    saida("Função main: ERRO - era esperado ')'");

                if (!tl.getToken().equals("tk_FIM"))
                {
                    if (!tl.getToken().equals("tk_abreChave"))
                        saida("Função main: ERRO - era esperado '{'");
                    do
                    {
                        pos = analisadorSintatico(++pos);
                        tl = listaTL.get(pos);
                    } while (!tl.getToken().equals("tk_FIM") && !tl.getToken().equals("tk_fechaChave"));
                    if (!tl.getToken().equals("tk_FIM"))
                    {
                        if (!tl.getToken().equals("tk_fechaChave"))
                            saida("Função main: ERRO - era esperado '}'");
                    } else
                        saida("Função main: ERRO - fim inesperado");
                } else
                    saida("Função main: ERRO - fim inesperado");
            } else
                saida("Função main: ERRO - fim inesperado");
        } else
            saida("Função main: ERRO - fim inesperado");
        return pos;
    }

    private int fun_valor(int pos)
    {
        TokenLexema tl = listaTL.get(pos);
        if (!tl.getToken().equals("tk_FIM"))
            if (tl.getToken().equals("tk_abreParentese"))
            {
                pos = fun_valor(++pos);
                tl = listaTL.get(pos);
                if (!tl.getToken().equals("tk_FIM"))
                    if (tl.getToken().equals("tk_fechaParentese"))
                        pos++;
                    else
                        saida("Declaração de valor: ERRO - faltou fecha Parentese ')'.");
                else
                    saida("Declaração de valor: ERRO - sintaxe incorreta.");
            } else if (tl.getToken().contains("tk_numero") || tl.getToken().contains("tk_valor") || tl.getToken().equals("tk_id"))
            {
                tl = listaTL.get(++pos);
                if (tl.getToken().equals("tk_operadorMatematico") || tl.getToken().equals("tk_operadorMenos"))
                    pos = fun_valor(++pos);
            } else
            {
                pos++;
                saida("Declaração de valor: ERRO - valor incorreto.");
            }
        else
            saida("Declaração de valor: ERRO - sintaxe incorreta.");

        return pos;
    }

    private int fun_condicao(int pos)
    {
        TokenLexema tl = listaTL.get(pos);
        if (!tl.getToken().equals("tk_FIM"))
        {
            switch (tl.getToken())
            {
                case "tk_operadorNegacao":
                    pos = fun_condicao(++pos);
                    break;
                case "tk_abreParentese":
                    pos = fun_condicao(++pos);
                    tl = listaTL.get(pos);
                    if (!tl.getToken().equals("tk_FIM"))
                        if (tl.getToken().equals("tk_fechaParentese"))
                            pos++;
                        else
                            saida("Condição: ERRO - faltou fecha Parentese ')'.");
                    else
                        saida("Condição: ERRO - sintaxe incorreta.");
                    break;
                default:
                    if (tl.getToken().contains("tk_numero") || tl.getToken().contains("tk_valor"))
                    {
                        pos = fun_valor(pos);
                        tl = listaTL.get(pos);
                        if (!tl.getToken().equals("tk_FIM"))
                            if (tl.getToken().equals("tk_operadorRelacional"))
                                pos = fun_valor(++pos);
                            else
                                saida("Condição: ERRO - esperado um operador relacional.");
                    } else
                        saida("Condição: ERRO - sintaxe incorreta, caractere '" + tl.getLexema() + "' nao esperado.");
                    break;
            }

            tl = listaTL.get(pos);
            if (!tl.getToken().equals("tk_FIM"))
            {
                if (tl.getToken().equals("tk_operadorLogico"))
                    pos = fun_condicao(++pos);
            } else
                saida("Condição: ERRO - sintaxe incorreta.");
        } else
            saida("Condição: ERRO - sintaxe incorreta.");

        return pos;
    }

    private int fun_tipo(int pos)
    {
        boolean continuar = false;
        TokenLexema tl;
        do
        {
            tl = listaTL.get(pos);
            if (!tl.getToken().equals("tk_FIM"))
            {
                if (!tl.getToken().equals("tk_id")) // tem id
                    saida("Declaração de tipo: sem ID");
                else
                    tl = listaTL.get(++pos);
                if (!tl.getToken().equals("tk_FIM"))
                    if (tl.getToken().equals("tk_atribuicao"))
                    {
                        pos = fun_valor(++pos);
                        tl = listaTL.get(pos);
                        if (!tl.getToken().equals("tk_FIM"))
                            switch (tl.getToken())
                            {
                                case "tk_virgula":
                                    pos++;
                                    continuar = true;
                                    break;
                                case "tk_pontoVirgula":
                                    continuar = false;
                                    break;
                                case "tk_id":
                                    saida("Declaração de tipo: ERRO - sintaxe incorreta, era esperado ','");
                                    fun_tipo(pos);
                                    break;
                                default:
                                    break;
                            }
                        else
                            saida("Declaração de tipo: ERRO - sintaxe incorreta, fim inesperado");
                    } else if (tl.getToken().equals("tk_virgula"))
                    {
                        pos++;
                        continuar = true;
                    } else if (tl.getToken().equals("tk_pontoVirgula"))
                    {
                        pos++;
                        continuar = false;
                    } else if (tl.getToken().equals("tk_id"))
                    {
                        saida("Declaração de tipo: ERRO - sintaxe incorreta, era esperado ','");
                        fun_tipo(pos);
                    } else
                        saida("Declaração de tipo: ERRO - sintaxe incorreta, era esperado '=' ou ',' ou ';'");
                else
                    saida("Declaração de tipo: ERRO - sintaxe incorreta, fim inesperado");
            } else
                saida("Declaração de tipo: ERRO - sintaxe incorreta, fim inesperado");
        } while (continuar);

        return pos;
    }

    private int fun_if(int pos)
    {
        TokenLexema tl = listaTL.get(pos);
        if (!tl.getToken().equals("tk_FIM"))
        {
            if (tl.getToken().equals("tk_abreParentese"))
                tl = listaTL.get(++pos);
            else
                saida("Estrutura IF: ERRO - sintaxe incorreta, era esperado '('.");

            if (!tl.getToken().equals("tk_FIM"))
            {
                pos = fun_condicao(pos);
                tl = listaTL.get(pos);
                if (!tl.getToken().equals("tk_FIM"))
                {

                    if (tl.getToken().equals("tk_fechaParentese"))
                        tl = listaTL.get(++pos);
                    else
                        saida("Estrutura IF: ERRO - faltou fecha Parentese ')'.");
                    if (!tl.getToken().equals("tk_FIM"))
                    {
                        if (tl.getToken().equals("tk_abreChave"))
                            pos++;
                        else
                            saida("Estrutura IF: ERRO - sintaxe incorreta, era esperado '{'.");
                        tl = listaTL.get(pos);
                        if (!tl.getToken().equals("tk_FIM"))
                        {
                            if (tl.getToken().equals("tk_fechaChave"))
                                tl = listaTL.get(++pos);
                            else
                                saida("Estrutura IF: ERRO - sintaxe incorreta, era esperado '}'.");
                            if (!tl.getToken().equals("tk_FIM"))
                                if (tl.getToken().equals("tk_else"))
                                {
                                    tl = listaTL.get(++pos);
                                    if (!tl.getToken().equals("tk_FIM"))
                                    {
                                        if (tl.getToken().equals("tk_abreChave"))
                                            pos++;
                                        else
                                            saida("Estrutura ELSE: ERRO - sintaxe incorreta, era esperado '{'.");

                                        pos = analisadorSintatico(pos);
                                        tl = listaTL.get(pos);
                                        if (!tl.getToken().equals("tk_FIM"))
                                            if (!tl.getToken().equals("tk_fechaChave"))
                                                saida("Estrutura ELSE: ERRO - sintaxe incorreta, era esperado '}'.");
                                            else
                                                pos++;
                                    }
                                }//posso nao ter else
                        } else
                            saida("Estrutura IF: ERRO - fim inesperado.");
                    } else
                        saida("Estrutura IF: ERRO - fim inesperado.");
                } else
                    saida("Estrutura IF: ERRO - fim inesperado.");
            } else
                saida("Estrutura IF: ERRO - fim inesperado.");
        } else
            saida("Estrutura IF: ERRO - fim inesperado.");

        return pos;
    }

    private int fun_while(int pos)
    {
        TokenLexema tl = listaTL.get(pos);
        if (!tl.getToken().equals("tk_FIM"))
        {
            if (tl.getToken().equals("tk_abreParentese"))
                tl = listaTL.get(++pos);
            else
                saida("Estrutura While: ERRO - sintaxe incorreta, era esperado '('.");
            if (!tl.getToken().equals("tk_FIM"))
            {
                pos = fun_condicao(pos);
                tl = listaTL.get(pos);
                if (!tl.getToken().equals("tk_FIM"))
                {
                    if (tl.getToken().equals("tk_fechaParentese"))
                        tl = listaTL.get(++pos);
                    else
                        saida("Estrutura While: ERRO - sintaxe incorreta, era esperado ')'.");

                    if (!tl.getToken().equals("tk_FIM"))
                    {
                        if (tl.getToken().equals("tk_abreChave"))
                            pos++;
                        else
                            saida("Estrutura While: ERRO - sintaxe incorreta, era esperado '{'.");
                        pos = analisadorSintatico(pos);
                        tl = listaTL.get(pos);
                        if (!tl.getToken().equals("tk_FIM"))
                            if (tl.getToken().equals("tk_fechaChave"))
                                pos++;
                            else
                                saida("Estrutura While: ERRO - sintaxe incorreta, era esperado '}'.");
                        else
                            saida("Função While: ERRO - fim inesperado");
                    }
                }
            } else
                saida("Função While: ERRO - fim inesperado");
        } else
            saida("Função While: ERRO - fim inesperado");

        return pos;
    }

    private int fun_var(int pos)
    {
        TokenLexema tl = listaTL.get(pos);
        if (!tl.getToken().equals("tk_FIM"))
        {
            if (tl.getToken().equals("tk_atribuicao"))
                pos++;
            else
                saida("Atribuição de variável: ERRO - era esperado '='");
            pos = fun_valor(pos);
            tl = listaTL.get(pos);
            if (!tl.getToken().equals("tk_FIM"))
            {
                if (!tl.getToken().equals("tk_pontoVirgula"))
                    saida("Atribuição de variável: ERRO - era esperado ';'");
                pos++;
            } else
                saida("Atribuição de variável: ERRO - Fim do código inesperado");
        }
        return pos;
    }

    private int analisadorSintatico(int pos)
    {
        TokenLexema tx = listaTL.get(pos);

        switch (tx.getToken())
        {
            case "tk_tipoVariavel":
                pos = fun_tipo(++pos);
                break;
            case "tk_main":
                pos = fun_main(++pos);
                break;
            case "tk_if":
                pos = fun_if(++pos);
                break;
            case "tk_while":
                pos = fun_while(++pos);
                break;
            case "tk_id":
                pos = fun_var(++pos);
                break;
            case "tk_comentarioInline":
                pos++;
                break;
            case "tk_comentarioBloco":
                pos++;
                break;
        }
        return pos;
    }

    private void saida(String erro)
    {
        txt_saida.appendText("\n" + erro);
    }
}
