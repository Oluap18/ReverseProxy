import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyWriteClTCP extends Thread{

    private Socket socket;
    private String str;
    private static Object obj = new Object();
    private boolean cond;

    public ProxyWriteClTCP(Socket s){
        this.socket = s;
        cond = true;
    }

    public void setMensagem(String st){
        str = st;
        cond=false;
        synchronized(obj){
            obj.notifyAll();
        }
    }

    public void run(){
        try{
            boolean wh = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while(wh){
                cond=true;
                synchronized(obj){
                    while(cond){
                        obj.wait();
                    }
                }
                if(str==null)
                    break;
                out.println(str);
                out.flush();
            }
    	}
        catch(Exception e){}
        try{
            socket.shutdownOutput();
            socket.close();
        }
        catch(Exception e){}
    }
}