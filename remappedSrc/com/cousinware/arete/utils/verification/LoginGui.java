package com.cousinware.arete.utils.verification;


import com.cousinware.arete.managers.ThreadManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGui implements ActionListener {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    public static String username;
    public static String password;
    JButton button = new JButton("Login");


    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JLabel label;
    JLabel label2;

    public LoginGui() {

        button.addActionListener(this);





        label = new JLabel("Username");
        label2 = new JLabel("Password");

        panel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));
        panel.setLayout(new GridLayout(3, 2));
        panel.add(label);
        panel.add(label2);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(button);


        frame.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/icon.png")));



        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Arete login");
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase(button.getActionCommand())) {
            username = usernameField.getText();
            password = passwordField.getText();
            ThreadManager.loginThread.interrupt();
            frame.dispose();
        }
    }


}
