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
				write.setMensagem("Recebi a tua mensagem: " + str + socket.getInetAddress());
			}
		}
		catch(Exception e){}
		write.setMensagem(null);
	}
}	
