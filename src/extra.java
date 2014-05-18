import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class extra {

	public static void main(String[] args)
		{
		    int port = 50003 ; 
			int len = 128;
		    try {
				DatagramSocket sockudp = new DatagramSocket(port);
				sockudp.setSoTimeout(50000);
				System.out.println("Socket Created");
				byte[] sendpack = new byte[len];
				byte[] packetrec = new byte[len];
			while(true)	{	
				
				DatagramPacket recdata = new DatagramPacket(packetrec,packetrec.length);
				sockudp.receive(recdata);
				InetAddress IPaddr = recdata.getAddress();
				port = recdata.getPort();
				String received = new String(recdata.getData());
				System.out.println("Received: " + received);
				String send = received.toUpperCase();
				sendpack = send.getBytes();
			
				DatagramPacket senddata = new DatagramPacket(sendpack, sendpack.length, IPaddr, port);
		
				sockudp.send(senddata);
			   } 
		  }
			catch(SocketTimeoutException e) {
				System.out.println("No Response");
			}
			catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		
	}
	
}
