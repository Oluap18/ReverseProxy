import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyReadMonTCP extends Thread{

    private Socket socket;
    private Map<InetAddress, TabelaMonitor> tabela;
    private ProxyWriteClTCP write;
    private InetAddress ipAddress;

    public ProxyReadMonTCP(Socket s, Map<InetAddress, TabelaMonitor> t, ProxyWriteClTCP w){
        this.socket = s;
        this.tabela = t;
        this.write = w;
        this.ipAddress = socket.getInetAddress();
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true){
                String str = in.readLine();
                if(str == null)
                    break;
                write.setMensagem(str);
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
        write.setMensagem(null);
    }
}
