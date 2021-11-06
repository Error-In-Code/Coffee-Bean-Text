import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorGUI extends JFrame{

    // Create basic GUI elements
    private final JPanel panel = new JPanel();
    private final JEditorPane editorPane = new JEditorPane();
    private final JMenuBar menuBar = new JMenuBar();

    // File menu creation
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem saveButton = new JMenuItem("Save");
    private final JMenuItem loadButton = new JMenuItem("Open");
    private final JMenuItem saveAsButton = new JMenuItem("Save As...");
    private final JMenuItem newButton = new JMenuItem("New");

    // Edit menu creation
    private final JMenuItem editMenu = new JMenu("Edit");
    private final JMenuItem fontButton = new JMenuItem("Change Font");

    private Font[] fonts;
    private String[] fontNames;

    final JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text file", "txt");
    JDialog newFileDialog = new JDialog();
    JPanel newFilePanel = new JPanel();
    JTextField newFileField = new JTextField();
    JButton createNewFile = new JButton("Create");
    JLabel nameFileLabel = new JLabel("Enter the name of your file:");

    JDialog fontsDialog = new JDialog();
    JPanel fontsPanel = new JPanel();
    JTextField fontSizeField = new JTextField();
    JScrollPane fontScroll = new JScrollPane();
    JList fontList;

    private String newFilePath;

    public EditorGUI(){
        // Load Fonts
        fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        // Load Icon
        Image icon = Toolkit.getDefaultToolkit().getImage("src/TextEditorIcon.png");

        // Load GUI
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
        menuBar.add(editMenu);
        editMenu.add(fontButton);
        setTitle("Text Editor");
        setIconImage(icon);

        // Set up dialogs
        newFileDialog.setSize(300, 200);
        newFileDialog.add(newFilePanel);
        newFileDialog.setName("Create File");

        newFilePanel.setLayout(null);

        newFilePanel.add(nameFileLabel);
        newFilePanel.add(createNewFile);
        newFilePanel.add(newFileField);


        newFileField.setBounds(50, 50, 200, 25);
        createNewFile.setBounds(50, 100, 100, 25);
        nameFileLabel.setBounds(50, 10, 300, 25);

        fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontList = new JList(fontNames);


        fontsDialog.setSize(400, 300);
        fontsDialog.add(fontsPanel);
        fontsDialog.setName("Set Font");

        fontsPanel.setLayout(null);

        fontsPanel.add(fontSizeField);
        fontsPanel.add(fontScroll);
        fontScroll.setViewportView(fontList);

        fontSizeField.setBounds(50, 20, 50, 25);
        fontScroll.setBounds(50, 80, 200, 100);

        editorPane.setFont(fonts[0].deriveFont(18f));

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
//                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
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
//                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setFileFilter(textFileFilter);

                int response = fileChooser.showDialog(EditorGUI.this, "Save");
                if(response == JFileChooser.APPROVE_OPTION){
//                    newFilePath = fileChooser.getSelectedFile().toString();
//                    newFileDialog.setVisible(true);
                    if(fileChooser.getSelectedFile().getName().endsWith(".txt")){
                        FileHandler.openFile(fileChooser.getSelectedFile());
                        FileHandler.saveToFile(editorPane.getText());
                    }else{
                        FileHandler.openFile(fileChooser.getSelectedFile() + ".txt");
                        FileHandler.saveToFile(editorPane.getText());
                    }

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
                saveButton.setVisible(true);
            }
        });

        fontButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontsDialog.setVisible(true);
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
