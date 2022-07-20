package rosolen.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class AcessoCEP
{

    public String consultaCep(String cep, String formato)
    {
        String s = "";
        try
        {

            URL url = new URL("http://apps.widenet.com.br/busca-cep/api/cep/" + cep + "." + formato);

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null)
                s += line;
            in.close();

        } catch (MalformedURLException e)
        {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("I/O Error: " + e.getMessage());
        }

        return s;
    }
}
