package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 */

/**
 * @author een570
 *
 */


public class TCPEchoSA {
	
	
	
	TCPEchoSA(int port) throws Exception {
		doEcho(port);
	}
	
	private static void doEcho(int port) throws Exception {
		
		int len = 128;
		int udpport = 50003; 
		DatagramSocket sockudp = new DatagramSocket();
		sockudp.setSoTimeout(1000000);
		System.out.println("TCP Server Socket Created");
		System.out.println("UDP Client Socket Created");

		InetAddress addr =  InetAddress.getByName("localhost");


		// Selector
		
		Selector theSelector = Selector.open(); //SelectorProvider.provider().openSelector();
		
		// Server socket channel
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false); // make non-blocking
		
		// bind to local address
		InetSocketAddress isa = new InetSocketAddress(port);
		ssc.socket().bind(isa);
	
		
		//System.out.println("Starting on "+port);
		
		// register the server socket with the selector
		SelectionKey acceptKey = ssc.register(theSelector, SelectionKey.OP_ACCEPT);
		
		int keysAdded = 0;
		
		// while loop
		while ((keysAdded = theSelector.select())>0) {
			
		//	System.out.println("Select returned");
			
			// get the ready keys
			Set<SelectionKey> readyK = theSelector.selectedKeys();
			// get an iterator on the set of keys
			Iterator<SelectionKey> i = readyK.iterator();
			
			// process the ready keys
			while (i.hasNext()) {
				SelectionKey sk = (SelectionKey)i.next(); // get the next key
				i.remove(); // remove from the ready set
				
				// check if it is the serversocket
				if (sk.isAcceptable()) {
					System.out.println("Process accept");
					
					// Get the ServerSocketChannel
					ServerSocketChannel nssc = (ServerSocketChannel)sk.channel();
					
					// accept the connection
					SocketChannel sc = nssc.accept();
					
					// Configure the SocketChannel as non blocking
					sc.configureBlocking(false);
					
					// register the SocketChannel with the selector
					sc.register(theSelector, SelectionKey.OP_READ);
					
				} else if (sk.isReadable()) { // if a readable key
					System.out.println("Process read");
					
					// get the SocketChannel
					SocketChannel sc = (SocketChannel)sk.channel();
					
					// Create a ByteBuffer for the data
					ByteBuffer dst = ByteBuffer.allocate(1000);
					
					// read the data from the channel
					int numread = sc.read(dst);
					if (numread>0) {
						//System.out.println("Read:"+dst.toString());
						System.out.println("Data Read from TCP:"+new String(dst.array()));
						String stread = new String(dst.array());
						String[] array = stread.split("\\|");

/*
						if(array[0].equals("SRCH")) {
							if(array.length == 6){
							//	ret = srchprocess(array);
							} else
							{
							//	ret = "ERROR|INVALID ARG NUMBER";
							}
						} else if(array[0].equals("UPDT")){
							if(array.length == 6){
								byte[] packetrec = new byte[len];
								byte[] packet = new byte[len];
								packet = stread.getBytes();
								DatagramPacket senddata = new DatagramPacket(packet, packet.length, addr, udpport);
								sockudp.send(senddata);

								DatagramPacket recfirstdata = new DatagramPacket(packetrec,packetrec.length);

								sockudp.receive(recfirstdata);
								String recstring1 = new String(recfirstdata.getData());
								System.out.println("Received: " + recstring1 );
								String[] UDParray = recstring1.split("\\|");
								dst.flip();
								System.out.println("Read:"+recstring1.toString());
								// write the buffer to the ScoketChannel
								sc.write(dst);
							} else
							{
							//	ret = "ERROR|INVALID ARG NUMBER";
							}
							
						} else*/ 
						if(array[0].equals("CREA")||array[0].equals("UPDT")||array[0].equals("SRCH")||array[0].equals("LOUT")) {
							if(array.length != 5 && array[0].equals("CREA")){
							//   error message	
							} else if(array.length != 6 && ( array[0].equals("UPDT") || array[0].equals("SRCH") ) ){
								//error
							} else {
								/*
								byte[] packetrec = new byte[len];
								byte[] packet = new byte[len];
								packet = stread.getBytes();
								DatagramPacket senddata = new DatagramPacket(packet, packet.length, addr, udpport);
								
								sockudp.send(senddata);

								DatagramPacket recfirstdata = new DatagramPacket(packetrec,packetrec.length);

								sockudp.receive(recfirstdata);
								String recstring1 = new String(recfirstdata.getData());
								System.out.println("Received from data Layer: " + recstring1 );
								String[] UDParray = recstring1.split("\\|");
								
								*/
								
								
								
								
								
								dst.clear();
								String retmsg = "";
								//if(UDParray[0].equals("OK")) {
									/* retmsg = "OK";
									for(int j =1;j<UDParray.length;j++)	{
										retmsg+= "|" + UDParray[j];
									}
									retmsg+="|~";*/
									//retmsg = recstring1;
								//}
								try {
									dst.put(retmsg.getBytes());
								} catch(BufferOverflowException e)	{
									e.printStackTrace();
								}
					/*			ByteBuffer buf = ByteBuffer.allocate(100);

								// Create a character ByteBuffer
								CharBuffer cbuf = buf.asCharBuffer();
								// Write a string
								cbuf.put(recstring1);
								// Convert character ByteBuffer to a string.
								// Uses characters between current position and limit so flip it first
								cbuf.flip();

								//dst.put(recstring1);
								System.out.println("Sending to Client: "+recstring1.toString());
								// write the buffer to the ScoketChannel
						
								 */
								dst.flip();
								sc.write(dst);
							} 

						} else	{
							//error message
						}
						
						
						
						// flip the buffer to prepare it for writing
						// resets the position to 0 and the limit to
						// the previous position
						// only needed as we are reusing the ByteBuffer
						/*dst.flip();
						System.out.println("Read:"+dst.toString());
					
						// write the buffer to the ScoketChannel
						sc.write(dst);*/
					} else { // close
						System.out.println("Close client socket");
						
						// close the SocketChannel
						sc.close();
						
						// unregister the key from the set monitored by the selector
						// places on the canceled key list
						sk.cancel();
						
					}
				}
			}
		}
		
		System.out.println("Done");
		ssc.close();
		acceptKey.cancel();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int port = 50002; // Integer.parseInt(args[0]);
		
		try {
			TCPEchoSA t = new TCPEchoSA(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
