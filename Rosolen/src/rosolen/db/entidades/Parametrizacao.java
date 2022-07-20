/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosolen.db.entidades;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author Aluno
 */
public class Parametrizacao
{

    private String razaosoc;
    private Endereco endereco;
    private String telefone;
    private BufferedImage logo;

    public Parametrizacao(String razaosoc, Endereco endereco, String telefone, Image logo)
    {
        this.razaosoc = razaosoc;
        this.endereco = endereco;
        this.telefone = telefone;
        setLogo(logo);
    }

    public Parametrizacao(String razaosoc, Endereco endereco, String telefone, BufferedImage logo)
    {
        this.razaosoc = razaosoc;
        this.endereco = endereco;
        this.telefone = telefone;
        this.logo = logo;
    }

    public String getRazaosoc()
    {
        return razaosoc;
    }

    public void setRazaosoc(String razaosoc)
    {
        this.razaosoc = razaosoc;
    }

    public Endereco getEndereco()
    {
        return endereco;
    }

    public void setEndereco(Endereco endereco)
    {
        this.endereco = endereco;
    }

    public Endereco getCidade()
    {
        return this.endereco;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }

    public BufferedImage getBufferedImageLogo()
    {
        return logo;
    }

    public void setLogo(BufferedImage logo)
    {
        this.logo = logo;
    }

    public Image getImageLogo()
    {
        return SwingFXUtils.toFXImage(this.logo, null);
    }

    public void setLogo(Image logo)
    {
        this.logo = SwingFXUtils.fromFXImage(logo, null);
    }
}
