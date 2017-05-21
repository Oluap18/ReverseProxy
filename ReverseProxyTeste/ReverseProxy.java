import java.io.*;
import java.net.*;
import java.util.*;

public class ReverseProxy{

	public static InetAddress getMonitor(HashMap<InetAddress,TabelaMonitor> tabela){
        InetAddress ip = null;
        Double status=-1.0;
        Collection<TabelaMonitor> col = tabela.values();
        for(TabelaMonitor t : col){
            if(status==-1){
                status = t.getStatus();
                ip = t.getIpMonitor();
            }
            else{
                if(status>t.getStatus()){
                    status = t.getStatus();
                    ip = t.getIpMonitor();
                }
            }
        }
        return ip;
    }

	public static void main(String[] args) throws Exception{
		HashMap<InetAddress,TabelaMonitor> tabela = new HashMap<InetAddress,TabelaMonitor>();
		HashMap<InetAddress, ProxyUpdateIP> updates = new HashMap<InetAddress, ProxyUpdateIP>();
		ProxyReadUDP pR = new ProxyReadUDP(tabela, updates);
		pR.start();
		ServerSocket srv = new ServerSocket(80);
		while(true){
			Socket cliente = srv.accept();
			InetAddress ip = getMonitor(tabela);
			while(ip==null){
				synchronized(tabela){
					tabela.wait();
				}
				ip = getMonitor(tabela);
			}
			TabelaMonitor t = tabela.get(ip);
			t.incTCPCon();
			Socket monitor = new Socket(ip, 80);
			ProxyReadClTCP readCl = new ProxyReadClTCP(cliente, monitor, tabela);
			ProxyReadMonTCP readMon = new ProxyReadMonTCP(monitor, cliente, tabela);
			readMon.start();
			readCl.start();
		}
	}

}