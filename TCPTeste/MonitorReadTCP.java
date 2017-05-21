import java.io.*;
import java.net.*;
import java.util.*;

public class MonitorReadTCP extends Thread{

	private Socket socket;
	private MonitorWriteTCP write;

	public MonitorReadTCP(Socket monitor, MonitorWriteTCP w){
		this.socket=monitor;
		this.write = w;
	}

	public void run(){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str;
			while(true){
				str = in.readLine();
				if(str==null)
					break;
				//System.out.println("Recebi MonitorRead " + socket.getInetAddress());
				System.out.println(str);
				write.setMensagem("Recebi a tua mensagem: " + str + socket.getInetAddress());
				//System.out.println("Escrevi MonitorRead " + socket.getInetAddress());
			}
		}
		catch(Exception e){}
		write.setMensagem(null);
		System.out.println("A conexão fechou. Monitor Read");
	}
}	
