import java.io.*;
import java.net.*;
import java.util.*;

public class MonitorTCP extends Thread{

	public MonitorTCP(){
   }

	public void run(){
		try{
			ServerSocket srv = new ServerSocket(80);
			while(true){
				Socket socket = srv.accept();
				MonitorReadTCP read = new MonitorReadTCP(socket);
				read.start();
			}
		}
		catch(Exception e){}
	}

}