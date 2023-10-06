import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Home extends JFrame implements ActionListener {
    Container container;

//    Sign in Option
    JButton signIn = new JButton("SignIn");

//    Sign up Option
    JButton signUp = new JButton("SignUp");

//    Exit
    JButton exit = new JButton("Exit");

    Home() {
        this.setTitle("Option Page");
        this.setSize(300,200);
        this.setLocation(100,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = getContentPane();
        container.setLayout(new GridLayout(3,1));
        JPanel signInPanel = new JPanel(null);
        signIn.setBounds(50,5,200,30);
        signInPanel.add(signIn); signIn.addActionListener(this);

        JPanel signUpPanel = new JPanel(null);
        signUp.setBounds(50,10,200,30);
        signUpPanel.add(signUp); signUp.addActionListener(this);

        JPanel exitPanel = new JPanel(null);
        exit.setBounds(50,15,200,30);
        exitPanel.add(exit); exit.addActionListener(this);

        container.add(signInPanel);
        container.add(signUpPanel);
        container.add(exitPanel);
    }

    public static void main(String[] args) {
        Home home = new Home();
        home.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(signUp)) {
            SignUp signUp1 = new SignUp();
            signUp1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(signIn)) {
            SignIn signIn1 = new SignIn();
            signIn1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(exit)) System.exit(0);

    }
}
