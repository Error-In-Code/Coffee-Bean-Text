import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;

public class EditorGUI extends JFrame{
    private JPanel panel = new JPanel();
    private JEditorPane editorPane = new JEditorPane();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem saveButton = new JMenuItem("Save");
    private JMenuItem loadButton = new JMenuItem("Open");
    private JMenuItem saveAsButton = new JMenuItem("Save As...");
    private JMenuItem newButton = new JMenuItem("New");

    final JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text file", "txt");
    JDialog newFileDialog = new JDialog();
    JPanel diaPanel = new JPanel();
    JTextField newFileField = new JTextField();
    JButton createNewFile = new JButton("Create");
    JLabel nameFileLabel = new JLabel("Enter the name of your file:");

    private String newFilePath;

    public EditorGUI(){
        Image icon = Toolkit.getDefaultToolkit().getImage("src/TexEditorIcon.png");
        panel.setLayout(new BorderLayout());
        setSize(500, 500);
        add(panel);
        panel.add(editorPane, BorderLayout.CENTER);
        panel.add(menuBar, BorderLayout.NORTH);
        menuBar.add(fileMenu);
        fileMenu.add(loadButton);
        fileMenu.add(saveButton);
        fileMenu.add(saveAsButton);
        fileMenu.add(newButton);
        setTitle("Text Editor");
        setIconImage(icon);

        newFileDialog.setSize(300, 200);
        newFileDialog.add(diaPanel);
        newFileDialog.setName("Create File");

        diaPanel.setLayout(null);

        diaPanel.add(nameFileLabel);
        diaPanel.add(createNewFile);
        diaPanel.add(newFileField);


        newFileField.setBounds(50, 50, 200, 25);
        createNewFile.setBounds(50, 100, 100, 25);
        nameFileLabel.setBounds(50, 10, 300, 25);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileHandler.saveToFile(editorPane.getText());
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(textFileFilter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int response = fileChooser.showDialog(EditorGUI.this, "Open");
                if(response == JFileChooser.APPROVE_OPTION){
                    FileHandler.openFile(fileChooser.getSelectedFile());
                    loadFile(FileHandler.getFileData());
                }

            }
        });

        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int response = fileChooser.showDialog(EditorGUI.this, "Create");
                if(response == JFileChooser.APPROVE_OPTION){
                    newFilePath = fileChooser.getSelectedFile().toString();
                    newFileDialog.setVisible(true);
                }
            }
        });

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButton.setVisible(false);
                loadFile("");
            }
        });

        createNewFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileHandler.openFile(newFilePath + "\\" + newFileField.getText() + ".txt");
                FileHandler.saveToFile(editorPane.getText());
                newFileDialog.setVisible(false);
                newFileField.setText("");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        editorPane.setVisible(false);
        newFileDialog.setVisible(false);

        setVisible(true);
    }

    public void loadFile(String fileData){
        editorPane.setVisible(true);
        editorPane.removeAll();
        editorPane.setText(fileData);
    }
}
