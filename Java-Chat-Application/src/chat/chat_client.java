package chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.net.InetAddress;
import javax.swing.JOptionPane;

public class chat_client extends javax.swing.JFrame {

    private MessageStorage messageStorage = new MessageStorage();
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String serverIP;
    private Socket connection;
    private int port = 6789;
    final static String secretKey = "secrete";
    EncryDecry encyrDecry = new EncryDecry();
    
    
    public chat_client(String s) {
        
        initComponents();
        
        this.setTitle("Client");
        this.setVisible(true);
        status.setVisible(true);
        serverIP = s;
    }

    private void whileChatting() {
        jTextField1.setEditable(true);
        do {
            try {
                message = (String) input.readObject();
                chatArea.append("\n" + message);
    
                // Store the message in the custom message storage
                messageStorage.addMessage(message);
    
                // Print the list of messages to the console
                printMessagesToConsole();
    
            } catch (ClassNotFoundException classNotFoundException) {
                // Handle the exception
            } catch (IOException ioException) {
                ioException.printStackTrace();
                // Handle the IO exception, e.g., notify the user
            }
        } while (!message.equals("Client - END"));
    }
    
    // Method to print the list of messages to the console
    private void printMessagesToConsole() {
        for (int i = 0; i < messageStorage.getSize(); i++) {
            System.out.println("Message " + (i + 1) + ": " + messageStorage.getMessage(i));
        }
    }
    
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        status = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 255, 204));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(102, 255, 204));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(null);

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 90, 480, 250);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1);
        jTextField1.setBounds(10, 350, 400, 40);

        jButton1.setBackground(new java.awt.Color(102, 102, 255));
        jButton1.setFont(new java.awt.Font("Arial Black", 1, 12)); 
        jButton1.setText("Send");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(410, 350, 80, 40);

        status.setForeground(new java.awt.Color(255, 255, 255));
        status.setText("...");
        jPanel1.add(status);
        status.setBounds(10, 60, 300, 40);

        jLabel2.setFont(new java.awt.Font("Myriad Pro", 1, 48));
        jLabel2.setForeground(new java.awt.Color(51, 0, 51));
        jLabel2.setText("Client");
        jLabel2.setToolTipText("");
        //jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 10, 190, 60);

        jLabel1.setBackground(new java.awt.Color(153, 255, 204));
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 35, 460, 410);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(500, 427));
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        
        sendMessage(jTextField1.getText());
	jTextField1.setText("");
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        
        sendMessage(jTextField1.getText());
	jTextField1.setText("");
    }
    public void startRunning()
    {
       try
       {
            status.setText("Attempting Connection ...");
            try
            {
                connection = new Socket(InetAddress.getByName(serverIP),port);
            }catch(IOException ioEception)
            {
                    JOptionPane.showMessageDialog(null,"Server Might Be Down!","Warning",JOptionPane.WARNING_MESSAGE);
            }
            status.setText("Connected");
            //to: " + connection.getInetAddress().getHostName());


            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());

            whileChatting();
       }
       catch(IOException ioException)
       {
            ioException.printStackTrace();
       }
    }
    
    
    
    private void sendMessage(String message) {
        try {
            // Get the current time
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
            // Format the time as a string
            String timeString = currentTime.format(formatter);
    
            // Create the complete message with the timestamp
            String completeMessage = timeString + " - ME(Client) - " + message;
    
            chatArea.append("\n" + completeMessage);
    
            String encryptedmsg = encyrDecry.encrypt(message, secretKey);
            System.out.println("(enc):" + encryptedmsg);
            EncryDecry encyrDecry = new EncryDecry();
            message = encyrDecry.decrypt(encryptedmsg, secretKey);
            System.out.println("Client(decrypt) - " + message);
            output.writeObject("                                                                       "+timeString + " - "+ "Client(decrypt) - "   + message);
            output.flush();
        } catch (IOException ioException) {
            chatArea.append("\n Unable to Send Message");
        }
    }
    
  
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel status;
    
}
