import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyReadClTCP extends Thread{

    private Socket cliente;
    private Socket monitor;
    private Map<InetAddress, TabelaMonitor> tabela;

    public ProxyReadClTCP(Socket c, Socket m, Map<InetAddress, TabelaMonitor> t){
        this.cliente = c;
        this.tabela = t;
        this.monitor = m;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter(monitor.getOutputStream());
            while(true){
                String str = in.readLine();
                if(str == null)
                    break;
                out.println(str + "\r");
                out.flush();
            }
    	}
        catch(Exception e){}
    }
}