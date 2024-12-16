/*
Programming Quest - Assignment 6 (8)
 21/ENG/003
 21/ENG/151
 21/ENG/149*/


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Network extends JFrame {

    private JPanel panelMain;
    private JTextField txtCommand;
    private JButton btnRun;
    private JButton btnStop;
    private JTextArea output;
    private JTextField textField1;
    //private JScrollPane scrollPane = new JScrollPane(output);
    //We tried to add a scrollbar (not enough time)

    private volatile Process process;
    private volatile boolean stopFlag;

    public Network() {
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlag = false;
                String command = txtCommand.getText();
                executeCommand(command);
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlag = true;
                stopCommand();
            }
        });


    }

    private void executeCommand(String command) {
        try {
            textField1.setText("Output of command: " + command + "\n");

            // Start a new thread for command execution
            Thread commandThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        process = new ProcessBuilder("cmd", "/c", command).start();

                        // Read the output of the command
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while (!stopFlag && (line = reader.readLine()) != null) {
                            output.append(line + "\n");
                        }

                        // Wait for the command to complete
                        int exitCode = process.waitFor();
                        output.append("Exit Code: " + exitCode + "\n");

                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            commandThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopCommand() {
        if (process != null) {
            process.destroy();
            JOptionPane.showMessageDialog(btnStop, "Command is stopped");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Network ntw = new Network();
                ntw.setContentPane(ntw.panelMain);
                ntw.setTitle("Networking Testing Tool");
                ntw.setBounds(100, 200, 600, 500);
                ntw.setVisible(true);
                ntw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
