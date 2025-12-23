package com.cousinware.arete.utils.guis.jguis;

import com.cousinware.arete.client.AreteClient;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FailedHwidCheckGui implements ActionListener {


    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    public static String username;
    public static String password;
    JButton button = new JButton("Copy HWID");


    JLabel label;
    JLabel label2;

    public FailedHwidCheckGui() {
        button.addActionListener(this);

        //TODO if the user gets the screen have them send a request for their HWID to be reset if the bot sees that they are allowed a reset let it change it


        label = new JLabel("You Failed the HWID Check. Fill out a ticket on the forum or message an admin with the issue");
        label2 = new JLabel("Your HWID: " + AreteClient.currentHWID);


        panel.add(label);
        panel.add(label2);
        panel.add(button);



        frame.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/icon.png")));
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Arete Failed HWID :(");
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase(button.getActionCommand())) {

            StringSelection data = new StringSelection(AreteClient.currentHWID);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(data, data);
        }
    }
}
