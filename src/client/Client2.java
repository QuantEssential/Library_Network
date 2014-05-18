package client;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client2 {


	

		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			String hostname = "localhost";//args[0];
			int port = 50002;//Integer.parseInt(args[1]);
			
			try {
				
				Socket sock = new Socket(hostname,port);
				
				BufferedInputStream sin = new BufferedInputStream(sock.getInputStream());
				BufferedOutputStream sout = new BufferedOutputStream(sock.getOutputStream());
				
				BufferedReader input =new BufferedReader(new InputStreamReader(System.in));	
				
				boolean accessgranted = false;
				String localhost = "localhost";
				String uzrname = "";
				String send; boolean exit = false;
				
				while(!accessgranted) {
					System.out.println("New User? Type 'Y' to create an account, or any key to access yours: ");
					String in = input.readLine();
					
					if(in.equals("Y")) {
						send = "CREA|";
						System.out.println("Username?");
						uzrname = input.readLine();
						send+=uzrname+"|";
						System.out.println("Name?");
						send+=input.readLine()+"|";
						System.out.println("Password?");
						send+=input.readLine()+"|~";				
						
					}
					else
					{
						send = "SRCH|USR|";
						System.out.println("Username?");
						uzrname = input.readLine();
						send+=uzrname+"| |";
						System.out.println("Password?");
						send+=input.readLine()+"|~";
						
					}
				/*	packet = send.getBytes();
					DatagramPacket senddata = new DatagramPacket(packet, packet.length, addr, port);
					sock.send(senddata);

					DatagramPacket recfirstdata = new DatagramPacket(packetrec,packetrec.length);

					sock.receive(recfirstdata);
					String recstring1 = new String(recfirstdata.getData());
					System.out.println("Received: " + recstring1 );
					String[] array = recstring1.split("\\|");*/
					
					sout.write(send.getBytes());
					sout.flush();
					
					//System.out.println("Sent:"+send);
					
					byte[] serv = new byte[1024];
					
					sin.read(serv);
					String stread = new String(serv);
				//	System.out.println("Read:"+(stread));
					String[] array = stread.split("\\|");
					
					if(array[0].equals("OK"))	{
						for(int i =1;i<array.length-1;i++)	{
							System.out.println(array[i]);
						}
						System.out.println("access granted");
						accessgranted = true;
						break;
					}
					else {
						if(array.length>1)
						{
							System.out.println(array[1]);
						}
					}
					
					
				}
							
				
				
				while(!exit)
				{	
					 System.out.println("LOUT to logout and exit, UPDT for updates, SRCH to search books: ");
					 String in = input.readLine();
					 if(in.equals("X"))  {
						 exit = true;
						 break;
					 }
					 else if(in.equals("UPDT"))  {
						 String updtstr = "UPDT|C|";
						 System.out.println("IN to Checkin a book, OUT to checkout a book: ");
						 updtstr += uzrname+"|"+ input.readLine() + "|";
						 System.out.println("Book Title: ");
						 updtstr +=input.readLine() + "|~";
							
						 sout.write(updtstr.getBytes());
							sout.flush();
							
					//		System.out.println("Sent:"+updtstr);
							
							byte[] serv = new byte[1024];
							
							sin.read(serv);
							String stread = new String(serv);
							//System.out.println("Read:"+(stread));
							String[] array = stread.split("\\|");
							
					//	System.out.println("Received: " + stread );
						if(array[0].equals("OK"))	{
						//	System.out.println("Success !!");
							for(int i =1;i<array.length-1;i++)	{
								System.out.println(array[i]);
							}
						}
						else {
							System.out.println("Failed");	
						}
					 }
					 else if(in.equals("LOUT"))  {
						 String loutstr = "LOUT|USR|";
						 System.out.println("Please Enter Password: ");
						 loutstr += uzrname+"|"+ input.readLine() + "|~";
						 sout.write(loutstr.getBytes());
							sout.flush();
							
					//		System.out.println("Sent:"+loutstr);
							
							byte[] serv = new byte[loutstr.length()];
							
							sin.read(serv);
							String stread = new String(serv);
					//		System.out.println("Read:"+(stread));
							String[] array = stread.split("\\|");
							
						//System.out.println("Received: " + stread );
						if(array[0].equals("OK"))	{
							System.out.println("GoodBye");
							 exit = true;
							 break;
						}
						else {
							System.out.println("Failed");	
						}
					 }
					 else if(in.equals("SRCH"))  {
						 String srchstr = "SRCH|BOOK|" + uzrname + "|";
						 System.out.println("Type 'SINGLE' to search for one book, and 'ALL' to list all checked out books: ");
						 String inread = input.readLine();
						 if(inread.equals("SINGLE") || inread.equals("ALL"))	{
							 if(inread.equals("SINGLE")) {
								 System.out.println("Enter Book Title: ");
								srchstr +=  input.readLine()+"|"  + inread+ "|~";

							 }
							 else{
								 srchstr +=  "A" +"|" + inread + "|~";
							 }

							
							 sout.write(srchstr.getBytes());
								sout.flush();
								
							//	System.out.println("Sent:"+srchstr);
								
								byte[] serv = new byte[1024];
								
								sin.read(serv);
								String stread = new String(serv);
							//	System.out.println("Read:"+(stread));
								String[] array = stread.split("\\|");
													 
							if(array[0].equals("OK"))	{
								//System.out.println("Success !!");
								for(int i =1;i<array.length-1;i++)	{
									System.out.println(array[i]);
								}
							}
							else {
								System.out.println("Failed");	
							}
							 
							 
						 } else  {
							 System.out.println("Sorry, invalid input");
						 }
						
						 
					 }
				  }
				
				
			/*	
				System.out.println("Enter Data:");
				String user;
				while((user=input.readLine()) != null) {
					
					sout.write(user.getBytes());
					sout.flush();
					
					System.out.println("Sent:"+user);
					
					byte[] serv = new byte[user.length()];
					
					sin.read(serv);
					
					System.out.println("Read:"+(new String(serv)));
					
					System.out.println("Enter Data:");

				}*/
				
				sin.close();
				sout.close();
				input.close();
				
				sock.close();
				
				
			} catch (Exception e) {
				System.out.println("Error:"+e.getMessage());
			}

		}

	}
/*
	public static void main(String[] args)
	{
		int len = 128;
		int port = 50068; 
		String end = "~";
		try {
			BufferedReader input =new BufferedReader(new InputStreamReader(System.in));	
			DatagramSocket sockudp = new DatagramSocket();
			sockudp.setSoTimeout(1000000);
			System.out.println("Client Socket Created");
			byte[] packet = new byte[len];
			
			boolean accessgranted = false;
			byte[] packetrec = new byte[len];
			String localhost = "localhost";
			String uzrname = "";
			String send; boolean exit = false;
			InetAddress addr =  InetAddress.getByName(localhost);//"10.0.0.11");// InetAddress.getByName("localhost");
		while(!accessgranted) {
			System.out.println("New User? Type 'Y' to create an account, or any key to access yours: ");
			String in = input.readLine();
			
			if(in.equals("Y")) {
				send = "CREA|";
				System.out.println("Username?");
				uzrname = input.readLine();
				send+=uzrname+"|";
				System.out.println("Name?");
				send+=input.readLine()+"|";
				System.out.println("Password?");
				send+=input.readLine()+"|~";				
				
			}
			else
			{
				send = "SRCH|USR|";
				System.out.println("Username?");
				uzrname = input.readLine();
				send+=uzrname+"| |";
				System.out.println("Password?");
				send+=input.readLine()+"|~";
				
			}
			packet = send.getBytes();
			DatagramPacket senddata = new DatagramPacket(packet, packet.length, addr, port);
			sockudp.send(senddata);

			DatagramPacket recfirstdata = new DatagramPacket(packetrec,packetrec.length);

			sockudp.receive(recfirstdata);
			String recstring1 = new String(recfirstdata.getData());
			System.out.println("Received: " + recstring1 );
			String[] array = recstring1.split("\\|");
			if(array[0].equals("OK"))	{
				System.out.println("access granted");
				accessgranted = true;
				break;
			}
			
			
		}
		
	while(!exit)
	{	
		 System.out.println("UPDT for updates, SRCH to search books, X to finish: ");
		 String in = input.readLine();
		 if(in.equals("X"))  {
			 exit = true;
			 break;
		 }
		 else if(in.equals("UPDT"))  {
			 String updtstr = "UPDT|C|";
			 System.out.println("IN to Checkin a book, OUT to checkout a book: ");
			 updtstr += uzrname+"|"+ input.readLine() + "|";
			 System.out.println("Book Title: ");
			 updtstr +=input.readLine() + "|~";
				
			 byte[] packet2 = updtstr.getBytes();
			DatagramPacket senddata2 = new DatagramPacket(packet2, packet2.length, addr, port);
			sockudp.send(senddata2);
			
			
			byte[] pack = new byte[len];
			DatagramPacket recdata = new DatagramPacket(pack, pack.length);
;

			sockudp.receive(recdata);
		    String recstring = new String(recdata.getData());

			System.out.println("Received: " + recstring );
			String[] array = recstring.split("\\|");
			if(array[0].equals("OK"))	{
				System.out.println("Success !!");
			}
			else {
				System.out.println("Failed");	
			}
		 }
		 else if(in.equals("SRCH"))  {
			 String srchstr = "SRCH|BOOK|" + uzrname + "|";
			 System.out.println("Type 'SINGLE' to search for one book, and 'ALL' to list all checked out books: ");
			 String inread = input.readLine();
			 if(inread.equals("SINGLE") || inread.equals("ALL"))	{
				 if(inread.equals("SINGLE")) {
					 System.out.println("Enter Book Title: ");
					srchstr +=  input.readLine()+"|"  + inread+ "|~";

				 }
				 else{
					 srchstr +=  "A" +"|" + inread + "|~";
				 }

				 byte[] packet3 = srchstr.getBytes();
				DatagramPacket senddata3 = new DatagramPacket(packet3, packet3.length, addr, port);
				sockudp.send(senddata3);
				
				
				byte[] pack2 = new byte[len];
				DatagramPacket recdata2 = new DatagramPacket(pack2, pack2.length);
	;

				sockudp.receive(recdata2);
			    String recstring = new String(recdata2.getData());

				System.out.println("Received: " + recstring );
				String[] array = recstring.split("\\|");
				if(array[0].equals("OK"))	{
					System.out.println("Success !!");
				}
				else {
					System.out.println("Failed");	
				}
				 
				 
			 } else  {
				 System.out.println("Sorry, invalid input");
			 }
			
			 
		 }
	  }
			// send += "|" + input.readLine()
//send = "";
		//	send = "UPDT|COUT|dan|OUT|A|~";
	//		send = "UPDT|CIN|dan|IN|E|~";
		//	send = "SRCH|BOOK|dan|E|SINGLE|~";
	
	while(true)	{
				byte[] pack = new byte[len];
				DatagramPacket recdata = new DatagramPacket(pack, pack.length);
;

				sockudp.receive(recdata);
			    String recstring = new String(recdata.getData());
			    if(recstring.contains(end))	{
					  System.out.println("Received End : " + recstring );
					 // break;
					}
				System.out.println("Received : " + recstring );
				
			}
		
		
			
			//sockudp.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}*/