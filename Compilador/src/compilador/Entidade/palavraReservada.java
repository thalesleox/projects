/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.Entidade;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author Aluno
 */
public class palavraReservada
{

    private String token;
    private List<String> lexemas;
    private Color cor;

    public palavraReservada(String token, List<String> lexemas, Color cor)
    {
        this.token = token;
        this.lexemas = lexemas;
        this.cor = cor;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public List<String> getLexemas()
    {
        return lexemas;
    }

    public void setLexemas(List<String> lexemas)
    {
        this.lexemas = lexemas;
    }

    public Color getCor()
    {
        return cor;
    }

    public void setCor(Color cor)
    {
        this.cor = cor;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
