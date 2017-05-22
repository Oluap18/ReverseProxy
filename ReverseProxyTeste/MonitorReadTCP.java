import java.io.*;
import java.net.*;
import java.util.*;

public class MonitorReadTCP extends Thread{

	private Socket socket;

	public MonitorReadTCP(Socket monitor){
		this.socket=monitor;
	}

	public void run(){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			String str;
			while(true){
				str = in.readLine();
				if(str==null)
					break;
				System.out.println(str);
				out.println(str.toUpperCase());
				out.flush();
			}
		}
		catch(Exception e){}
	}
}	
