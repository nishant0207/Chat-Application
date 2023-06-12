import java.io.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.net.*;
public class ChatClient {

	// Bellow will be used as users interface for sending messages in an interactive way
	static JFrame chatWindow=new JFrame("ChatApplication");
	static JTextArea chatArea=new JTextArea(22,40);
	static JTextField textField = new JTextField(40);
	static JLabel blankLabel = new JLabel("			");
	static JButton sendButton = new JButton("Send");
	static BufferedReader in;
	static PrintWriter out;
	static JLabel nameLabel=new JLabel("		");	
	
	// Constructor
	ChatClient()
	{
		// we will learn on some other day, there are many other layouts as-well
		chatWindow.setLayout(new FlowLayout());
		
		chatWindow.add(nameLabel);
		
		// as chat messages will keep on increasing we will need a scroller to go up and down in the chat area
		chatWindow.add(new JScrollPane(chatArea));
		chatWindow.add(blankLabel);
		chatWindow.add(textField);
		chatWindow.add(sendButton);
		
		// this will exit the chat window when we click on close button other wise even if
		// we close the chat window it will not exit the window it will just hide it from the user
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		chatWindow.setSize(475,500);
		//this will make the text visible
		// if not set true we wont be able to see the text in the chat area
		chatWindow.setVisible(true);
		
		// this will ensure the user is not able to text unless connected to the server
		// we will set it true on the server side
		textField.setEditable(false);
		chatArea.setEditable(false);
		
		// if send button is clicked the listener will be activated
		sendButton.addActionListener(new Listener());
		// if enter key is clicked the listener will be activated
		textField.addActionListener(new Listener());
		
		
	}
	
	void startChat() throws Exception
	{
		
		String ipAddress = JOptionPane.showInputDialog(chatWindow,"Enter IP Address: ","IP Address Required!!!",JOptionPane.PLAIN_MESSAGE);
		
		Socket soc=new Socket(ipAddress,9806);

		
		
		// for input output in the chat field
		in=new BufferedReader(new InputStreamReader(soc.getInputStream()));
		out=new PrintWriter(soc.getOutputStream(),true);
		
		while(true)
		{
			String str=in.readLine();
			if(str.equals("Name Required"))
			{
				String name=JOptionPane.showInputDialog(chatWindow,"Enter a Unique Name","Name Required!!!",JOptionPane.PLAIN_MESSAGE);
				out.println(name);
			}
			else if(str.equals("Name Already Exists"))
			{
				String name=JOptionPane.showInputDialog(chatWindow,"Enter Another Name","Name Already Exists",JOptionPane.WARNING_MESSAGE);
				out.println(name);
				
			}
			else if(str.startsWith("Name Accepted"))
			{
				textField.setEditable(true);
				nameLabel.setText("User Name: "+str.substring(13));
			}
			else
			{
				chatArea.append(str+"\n");
			}
			
		}
		
	}
	
	class Listener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// it will send what ever the user text
			ChatClient.out.println(ChatClient.textField.getText());
			// after the text is sent the text field will be emptied
			ChatClient.textField.setText("");
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		ChatClient client=new ChatClient();
		client.startChat();
	}
}


