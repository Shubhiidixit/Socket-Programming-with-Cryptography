import javax. swing.*;
import java.awt.*;// color class is inside this package
import java.awt.event.*; // Actionlistener interface inside this
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

// JFrame class is inside the extended java. swing package which has the setsize function 
//sets the frame size
public class Client implements ActionListener{
    JTextField text;//declaring globbally taki it can be used outside the constructor
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    static JFrame f = new JFrame();
    Client(){
        f.setLayout(null);
        //custom layout
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94,84));
        //set coordinates
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        //panel ko frame ke upr set karna hai 
        //using the add function
        f.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        //image ko directly frame ke upar set nahi kar skte
        //ek or class ka object banana padta hai
        JLabel back = new JLabel(i3);
        back.setBounds(5,20,25,25);
        //add(back);// frame ke upar add hui hai but panel ke upar add krni hai
        p1.add(back);

        back.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                //pure project ko band krdo
                //exit kardo on mouseclick on the arrow icon
                System.exit(0);
            }
        });

        
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40,10,50,50);
        p1.add(profile);

        //jlabel ki help se frame ke upar kuchbe text likh skte hai

        JLabel name = new JLabel("Client");
        name.setBounds(110,25,100,18);
        //change text colour
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD,18));
        p1.add(name);

        //niche ke part ke lie new panel

        a1 = new JPanel();
        a1.setBounds(5,75,440,570);
        f.add(a1);

        //JTextfield ek class jisse use text pass kr skta hai text field me
        text = new JTextField();
        text.setBounds(5,655,310,40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN,16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(7,94,84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN,16));

        f.add(send);


        f.setSize(450,700);// by defaulty frame ki visibility hidden hoty hai
        //by default frame top left par dikhta hai
        //we can set the location
        f.setLocation(800, 50);
        //to remove the frame header
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        
        //set it to true
        f.setVisible(true);
        


    }
    //override the abstarct method inside the interface
    public void actionPerformed(ActionEvent ae){
        try{
            String out = text.getText();    //here adding the action to be performed on clicking the send button
        JPanel p2 = formatLabel(out);
        a1.setLayout(new BorderLayout());// this border layout places our element in top botton left right or center
        JPanel right = new JPanel(new BorderLayout());
        //messages ko line end pe bhjna hai
        right.add(p2,BorderLayout.LINE_END);
        //messages ko vertically align krna hai
        vertical.add(right);
        //do messages ke beech me kitna space
        vertical.add(Box.createVerticalStrut(15));

        a1.add(vertical,BorderLayout.PAGE_START);
        dout.writeUTF(out);
        text.setText("");//set the text field empty after the message is sent
        
        //to reload the server when the message is sent
        f.repaint();
        f.invalidate();
        f.validate();

    }catch(Exception e){
        e.printStackTrace();

    }}
    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style='width:150px'>" +out + "</html>");
        output.setFont(new Font("Tahoma", Font.PLAIN,16));
        output.setBackground (new Color(37,211,102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,15));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);
        return panel;
    }

    public static void main(String args[]){
        new Client();//anonymous object
        try{
            Socket s = new Socket("127.0.0.1",6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true){
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical,BorderLayout.PAGE_START);
                f.validate();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}