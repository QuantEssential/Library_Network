package datalayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Database {

	 //store these objects in a collection
	//use a hashmap
	public static ArrayList<Book> coutlist =new ArrayList<Book>();
	public static HashMap<String, Book> bookmap  = new HashMap<String, Book>();
	public static HashMap<String, User> usermap = new HashMap<String, User>();
	
	public static class Book	{
		boolean checkedout;
		String title;
		String author;
		String year;
		String username;//username of the user who checked out this book
		User user; //user who checked out this book
		
		Book()	{
		}
		Book(String t, String a, String y)	{
			title = t;
			checkedout = false;
			author = a;
			year = y;
		
		}
	};
	
	public static class User	{
		int limit;
		String username;
		String password;
		String name;
		int checknum;
		ArrayList<Book> checkedout;
		boolean active;
		
		User()	{
		}
		User(String u,String p,String n)	{
			active = false;
			username = u;
			password = p;
			name = n;
			checknum = 0;
			checkedout = new ArrayList<Book>();
			limit = 10;
		}
		
		
		
	};
	
	public static class CheckedOut	{
		Book book;
		User user;
	};
	public static String cin(User u, Book b)	{
		

		if(u.checknum==0){
			return "ERROR|DID NOT CHECK THIS OUT";
		}
			
		for(Book boo : u.checkedout)	{
			
			String	test1 = b.title;
			String test2 = boo.title;
			if(test1.equals(test2))	{
				System.out.println("FOUND" + b.title + " checkIN");
				u.checkedout.remove(boo);
				return  "OK|"+u.username+"|"+"IN"+"|"+b.title+"|"+b.author+"|"+b.year;
			}
		}
		return "ERROR|DID NOT CHECK THIS OUT";
	}
	
	public static String cout(User u, String btitle)	{
		if(u.checknum<u.limit)
		{
			Book b =  bookmap.get(btitle.toLowerCase());
			if(b==null)	{
				System.out.print("null object");
				return "ERROR|NULL";
			}
			for(Book boo : u.checkedout)	{
				
				String	test1 = b.title;
				String test2 = boo.title;
				if(test1.equals(test2))	{
					System.out.println("duplicate checkout");
					return  "ERROR|duplicate checkout";
				}
			}
			coutlist.add(b);
			u.checknum++;
			u.checkedout.add(b);
			b.username = u.username;
			b.checkedout = true;
			b.user = u;
			String R = ("OK|"+u.username+"|"+"OUT"+"|"+b.title+"|"+b.author+"|"+b.year);
			return R;
		}	else	{
			System.out.println("Limit Reached");
			return  "ERROR|Limit Reached";

		}
	}


	public static String loutprocess(String[] s) {
		String r = "";
		//Book B = bookmap.get(s[3]);
		User U = usermap.get(s[2]);
		
		if(usermap.containsValue(U))	{
			if(s[1].equals("USR") && U.active)	{
				if(U.password.equals(s[3]))	{
					U.active = false;
					r = "OK|"+U.username+"|"+U.password+"|"+U.name;
					return r;
				} 
				else {
				  return	"ERROR|Invalid Password Username Combo";
				}

			} else if(!U.active)	{
				return "ERROR|USER IS NOT ACTIVE";
			}
		}else {
			   return "ERROR|USER DOESNT EXIST";  
		}	
		return "ERROR|FUNCTION ERROR";
	}
	
	public static String srchprocess(String[] s) {
		String r = "";
		Book B = bookmap.get(s[3].toLowerCase());
		User U = usermap.get(s[2]);
	if(s[1].equals("USR") ){
		if(usermap.containsValue(U))	{
			if(!U.active)	{
				if(U.password.equals(s[4]))	{
					U.active = true;
					r = "OK|"+U.username+"|"+U.password+"|"+U.name;
					return r;
				} 
				else {
				  return	"ERROR|Invalid Password Username Combo";
				}

			} 
			else if(U.active)	{
				return "ERROR|USER ALREADY ACTIVE";
			}
		}else {
			   return "ERROR|USER DOESNT EXIST";  
		}	
		
	}
	else  if(s[1].equals("BOOK"))	{
		  if(s[4].equals("SINGLE"))	{
			  if(bookmap.containsValue(B))	{
					r = "OK|"+U.username+"|"+B.title+"|"+B.author+"|"+B.year;
					return r;
				} else {
					return "ERROR|BOOK TITLE DOESNT EXIST";
				}	
		  } else if(s[4].equals("ALL"))	{
			  if(U.checkedout.isEmpty()) {
				  return "OK|"+"|"+"NONE"+"|"+U.username;//+"|~"; 
				  	
			  }
			  else {
				  r = "OK";
				  for(Book ball : U.checkedout) {
					  r+="|" + ball.title+"|"+ball.author+"|"+ball.year;
				  }
				//  r+="|~";
				  return r;
			  }
			  
			  
		  }
		
	  }
	
		return r;
	}
	
	public static String updtprocess(String[] s) {
		String retOK = "";
		String reterror = "";
		Integer if1IN = 0;
		String uname = s[1];
		Book B = bookmap.get(s[4].toLowerCase());
		User U = usermap.get(s[2]);
		if(usermap.containsValue(U))	{
			
			
		}
		else {
			reterror += "|USER DOESNT EXIST";

		}
		if(s[3].equals("IN")) {
			if1IN = 1;
		} else if(s[3].equals("OUT"))	{
			if1IN = 2;
		} else {
			return "ERROR|INVALID IN/OUT ARG";
		}


		if(bookmap.containsValue(B))	{
		} else {
			return "ERROR|BOOK TITLE DOESNT EXIST";
		}
		
		if(if1IN == 1) // USER WANTS TO CHECK IN A BOOK
		{
			retOK = cin(U, B);
		} else if(if1IN==2) //USER WANTS TO CHECKOUT 
		{
			if(bookmap.containsValue(B)&&usermap.containsValue(U)) {
				retOK = cout(U, B.title);
			
		   } else {
				return "ERROR|BOOK TITLE DOESNT EXIST";			}
		}
		else {
		return "ERROR|INVALID IN/OUT ARG";
		}
		
		return retOK;/*
		if(reterror.equals("ERROR")) // NO ERRORS FOUND, still initial state
		{
			return retOK;
		}
		System.out.print("OK MESSAGES: " + retOK);//return successful ops
		return reterror; *///otherwise error,
	}
	
	public static String creaprocess(String[] s) {
		String r = "";
		User U = usermap.get(s[1]);

		if(usermap.containsValue(U))	{
			return  "ERROR|USERNAME ALREADY EXISTS START OVER";
			
		}
		else {
			User u = new User(s[1], s[3], s[2]);
			u.active = true;
			usermap.put(s[1], u);
			r = "OK|" +s[1] + "|" + s[2] + "|" + s[3];  
			return r;
		}
		}
	

	public static void Initialize()
		{
		
			//Library.Initialize();
		
		
		    int port = 50003; 
			int len = 128;
		    try {
				DatagramSocket sockudp = new DatagramSocket(port);
				sockudp.setSoTimeout(10000000);
				System.out.println("Database UDP Server Socket Created");
				//byte[] sendpack = new byte[len];
				
				while(true)	{	
					byte[] packetrec = new byte[len];
					
					DatagramPacket recdata = new DatagramPacket(packetrec,packetrec.length);
				sockudp.receive(recdata);
				InetAddress IPaddr = recdata.getAddress();
				port = recdata.getPort();
				String received = new String(recdata.getData());
				System.out.println("Received: " + received);
				//String send = received.toUpperCase();
				String[] array = received.split("\\|");
				String ret = "";
				
				
				
				
				
				
				if(array[0].equals("SRCH")) {
					if(array.length == 6){
						ret = srchprocess(array);
					} else
					{
						ret = "ERROR|INVALID ARG NUMBER";
					}
				}else if(array[0].equals("LOUT")) {
					if(array.length == 5){
						ret = loutprocess(array);
					} else
					{
						ret = "ERROR|INVALID ARG NUMBER";
					}
				} else if(array[0].equals("UPDT")){
					if(array.length == 6){
					
							ret = updtprocess(array);
					} else
					{
						ret = "ERROR|INVALID ARG NUMBER";
					}
					
				} else if(array[0].equals("CREA")) {
					if(array.length == 5){
						ret = creaprocess(array);
					} else
					{
						ret = "ERROR|INVALID ARG NUMBER";
					}
				}
				//for(String s : array)	{
				ret+="|~";
					byte[] sendpack = new byte[len];
					System.out.println("Sending: " + ret);

					sendpack = ret.getBytes();
					DatagramPacket senddata = new DatagramPacket(sendpack, sendpack.length, IPaddr, port);
					sockudp.send(senddata);
				//}

				/*String end = "~";
				byte[] sendendpack = new byte[len];
				sendendpack = end.getBytes();
				DatagramPacket sendenddata = new DatagramPacket(sendendpack, sendendpack.length, IPaddr, port);
				sockudp.send(sendenddata);
				*/
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

	public static void main(String[] args)
		{
		//coutlist = new ArrayList<Book>();
	//	bookmap = new HashMap<String, Book>();
	//	usermap = new HashMap<String, User>();
/*		User dan = new User("dan", "eng", "daniel");
		User john = new User("john", "jjj", "johndoe");
		User q = new User("qzqzq", "qqq", "James");
		User uz = new User("uzr", "use", "Jamie");

		
		Book ender = new Book("E", "Card", "1991");
		Book h = new Book("Zoo", "Smith", "1990");
		Book A = new Book("A", "Adams", "1999");
		Book B = new Book("B", "Brad", "1997");
		Book C = new Book("C", "EEN570", "1993");
		Book D = new Book("D", "Card", "1995");
	
		bookmap.put("A", A);
		bookmap.put("B", B);
		bookmap.put("C", C);
		bookmap.put("D", D);
		bookmap.put("E", ender);
		if(!usermap.containsKey("dan"))	{
			System.out.println("no vallueeee");	
		} else	{
			User U = usermap.get("dan");
			if(usermap.containsValue(U))	{
				
			}
	*/
			//cout(U, ender.title)
		Book ender = new Book("enders game","Orson Scott Card", "1991");
		bookmap.put("enders game", ender);
		User dan = new User("dan", "eng", "daniel");
		usermap.put("dan", dan);

		cout(dan, ender.title);
			
			Scanner read = null;
			
			try {
				read = new Scanner(new FileReader("Book2.txt"));

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			while(read.hasNextLine()) {
				
				String title = read.nextLine();
				String author = read.nextLine();
				String lang = read.nextLine();
				String year = read.nextLine();
				
				Book newbook = new Book(title.toLowerCase(), author, year);
				bookmap.put(title.toLowerCase(), newbook);
				
			}
			
			
			
			
		  Initialize();		

			
			
//		if(map.containsValue(U))	{	System.out.println("Correct");	}
		
		
	}
	
}