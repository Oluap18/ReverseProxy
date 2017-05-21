import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyWriteMonTCP extends Thread{

    private Socket socket;
    private String str;
    private static Object obj = new Object();
    private boolean cond=true;

    public ProxyWriteMonTCP(Socket s){
        this.socket = s;
    }

    public void setMensagem(String st){
        str = st;
        cond=false;
        //System.out.println("Mensagem Escrita no setMensagem WriteMon " + cond + socket.getInetAddress());
        synchronized(obj){
            obj.notifyAll();
        }
    }

    public void run(){
        try{
            boolean wh = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while(wh){
                //System.out.println("Inicio do while write monitor " + cond + socket.getInetAddress());
                synchronized(obj){
                    while(cond){
                        //System.out.println("Wait write monitor " + cond + socket.getInetAddress() + str);
                        obj.wait();
                    }
                    //System.out.println("Acordei WriteMon " + socket.getInetAddress());
                }
                if(str==null)
                    break;
                out.println(str);
                out.flush();
                cond=true;
                //System.out.println("Escrevi WriteMon " + socket.getInetAddress());
            }
    	}
        catch(Exception e){}
        System.out.println("Sai do write monitor.");
        try{
            socket.shutdownOutput();
            socket.close();
        }
        catch(Exception e){}
    }
}