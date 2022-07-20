/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.Entidade;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aluno
 */
public class Dicionario
{

    private static ArrayList<palavraReservada> lista;

    public Dicionario()
    {
        lista = retornaLista();
    }

    public static ArrayList<palavraReservada> getLista()
    {
        return retornaLista();
    }

    private static ArrayList<palavraReservada> retornaLista()
    {
        if (lista == null)
        {
            lista = new ArrayList<>();

            lista.add(nova("tk_main", Arrays.asList("main")));
            lista.add(nova("tk_pontoVirgula", Arrays.asList(";")));
            lista.add(nova("tk_abreParentese", Arrays.asList("(")));
            lista.add(nova("tk_fechaParentese", Arrays.asList(")")));
            lista.add(nova("tk_abreChave", Arrays.asList("{")));
            lista.add(nova("tk_fechaChave", Arrays.asList("}")));
            lista.add(nova("tk_abreColchete", Arrays.asList("[")));
            lista.add(nova("tk_fechaColchete", Arrays.asList("]")));
            lista.add(nova("tk_atribuicao", Arrays.asList("=")));
            lista.add(nova("tk_if", Arrays.asList("if")));
            lista.add(nova("tk_else", Arrays.asList("else")));
            lista.add(nova("tk_while", Arrays.asList("while")));
            lista.add(nova("tk_switch", Arrays.asList("switch")));
            lista.add(nova("tk_case", Arrays.asList("case")));
            lista.add(nova("tk_break", Arrays.asList("break")));
            lista.add(nova("tk_default", Arrays.asList("default")));
            lista.add(nova("tk_virgula", Arrays.asList(",")));
            lista.add(nova("tk_operadorRelacional", Arrays.asList(">", "<", "==", ">=", "<=", "!=")));
            lista.add(nova("tk_operadorNegacao", Arrays.asList("!")));
            lista.add(nova("tk_eComercial", Arrays.asList("&")));
            lista.add(nova("tk_pipe", Arrays.asList("|")));
            lista.add(nova("tk_operadorLogico", Arrays.asList("&&", "||")));
            lista.add(nova("tk_operadorMenos", Arrays.asList("-")));
            lista.add(nova("tk_operadorMatematico", Arrays.asList("+", "*", "/", "^", "%")));
            lista.add(nova("tk_tipoVariavel", Arrays.asList("int", "float", "char", "String"), Color.BLUE));

        }
        return lista;
    }

    private static palavraReservada nova(String token, List<String> lexemas, Color cor)
    {
        return new palavraReservada(token, lexemas, cor);
    }

    private static palavraReservada nova(String token, List<String> lexemas)
    {
        return new palavraReservada(token, lexemas, Color.BLACK);
    }

    public palavraReservada getPalavra(String str)
    {
        palavraReservada pres = null;
        if (!str.isEmpty())
            for (int i = 0; i < lista.size() && pres == null; i++)
                for (int j = 0; j < lista.get(i).getLexemas().size() && pres == null; j++)
                    if (str.equals(lista.get(i).getLexemas().get(j)))
                        pres = lista.get(i);

        return pres;
    }

    public boolean fazParte(String str)
    {
        boolean achou = false;
        if (!str.isEmpty())
            for (int i = 0; i < lista.size() && !achou; i++)
                for (int j = 0; j < lista.get(i).getLexemas().size() && !achou; j++)
                    if (str.equals(lista.get(i).getLexemas().get(j)))
                        achou = true;

        return achou;
    }
}
