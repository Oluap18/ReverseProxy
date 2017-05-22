import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyReadMonTCP extends Thread{

    private Socket monitor;
    private Socket cliente;
    private Map<InetAddress, TabelaMonitor> tabela;
    private InetAddress ipAddress;

    public ProxyReadMonTCP(Socket m, Socket c, Map<InetAddress, TabelaMonitor> t){
        this.monitor = m;
        this.tabela = t;
        this.ipAddress = monitor.getInetAddress();
        this.cliente = c;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(monitor.getInputStream()));
            PrintWriter out = new PrintWriter(cliente.getOutputStream());
            while(true){
                String str = in.readLine();
                if(str == null)
                    break;
                out.println(str);
                out.flush();
                System.out.println("Monitor :" + ipAddress + " mandou mensagem.");
            }
    	}
        catch(Exception e){}
        TabelaMonitor t;
        synchronized(tabela){
            t = tabela.get(ipAddress);
        }
        synchronized(t){
            t.decTCPCon();
        }
    }
}
