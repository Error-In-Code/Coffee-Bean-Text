import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EditorGUI extends JFrame{
    JPanel panel = new JPanel();
    JEditorPane editorPane = new JEditorPane();
    JMenuBar menuBar = new JMenuBar();
    JMenuItem saveButton = new JMenuItem("Save");
    JTextField filePathField = new JTextField();
    JMenuItem loadButton = new JMenuItem("Load");
    public EditorGUI(String fileData){
        panel.setLayout(new BorderLayout());
        setSize(500, 500);
        add(panel);
        panel.add(editorPane, BorderLayout.CENTER);
        panel.add(menuBar, BorderLayout.NORTH);
        panel.add(filePathField, BorderLayout.SOUTH);
        menuBar.add(loadButton);
        menuBar.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileHandler.saveToFile(editorPane.getText());
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileHandler.openFile(filePathField.getText());
                loadFile(FileHandler.getFileData());
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        editorPane.setVisible(false);

        setVisible(true);
    }

    public void loadFile(String fileData){
        editorPane.setVisible(true);
        editorPane.removeAll();
        editorPane.setText(fileData);
    }
}
