import java.io.*;
import java.util.*;
import java.net.*;

public class ChatServer {
	
	static ArrayList<String> userNames=new ArrayList<String>();
	static ArrayList<PrintWriter> printWriters=new ArrayList<PrintWriter>();
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Waiting For Clients");
		ServerSocket ss=new ServerSocket(9806);
		while(true)
		{
			Socket soc=ss.accept();
			System.out.println("Connection Established");
			ConversationHandler handler=new ConversationHandler(soc);
			handler.start();
		}
	}
}

// Thread Class for handling multiple clients

class ConversationHandler extends Thread
{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	FileWriter fileWriter;
	BufferedWriter bufferedWriter;
	PrintWriter pw;
	
	public ConversationHandler(Socket socket) throws IOException
	{
		this.socket = socket;
		
		// Open the file for appending messages
        String filePath = "/Users/nishantdalal/Desktop/ChatServer-Logs.txt";
        fileWriter = new FileWriter(filePath, true);
        bufferedWriter = new BufferedWriter(fileWriter);
        pw = new PrintWriter(bufferedWriter, true);
        
	}
	
	public void run()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			
			int count=0;
			while(true)
			{
				if(count>0)
				{
					out.println("Name Already Exists");
				}
				else
				{
					out.println("Name Required");
				}
				
				name = in.readLine();
				
				if(name == null)
				{
					return;
				}
				
				// if name is unique it will be added to the array list of user names
				if(!ChatServer.userNames.contains(name)) {
					ChatServer.userNames.add(name);
					break;
				}
				count++;
			}
			out.println("Name Accepted"+name);
			ChatServer.printWriters.add(out);
			
			while(true)
			{
				String message=in.readLine();
				if(message == null)
				{
					return;
				}
				
				// Save the message to the file
                pw.println(name + ": " + message);
				
				for(PrintWriter writer: ChatServer.printWriters)
				{
					writer.println(name + ": "+message);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			try {
                if (pw != null) {
                    pw.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}
}


