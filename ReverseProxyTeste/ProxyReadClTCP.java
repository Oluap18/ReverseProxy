import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyReadClTCP extends Thread{

    private Socket socket;
    private Map<InetAddress, TabelaMonitor> tabela;
    private ProxyWriteMonTCP write;

    public ProxyReadClTCP(Socket s, Map<InetAddress, TabelaMonitor> t, ProxyWriteMonTCP w){
        this.socket = s;
        this.tabela = t;
        this.write = w;
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
        write.setMensagem(null);
    }
}