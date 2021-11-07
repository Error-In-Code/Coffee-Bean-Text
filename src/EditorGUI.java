import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

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

    private final JFileChooser fileChooser = new JFileChooser();
    private FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text file", "txt");
    private JDialog newFileDialog = new JDialog();
    private JPanel newFilePanel = new JPanel();
    private JTextField newFileField = new JTextField();
    private JButton createNewFile = new JButton("Create");
    private JLabel nameFileLabel = new JLabel("Enter the name of your file:");

    private JDialog fontsDialog = new JDialog();
    private JPanel fontsPanel = new JPanel();
    private JTextField fontSizeField = new JTextField();
    private JScrollPane fontScroll = new JScrollPane();
    private JList fontList;
    private JLabel sizeLabel = new JLabel("Set Font Size");

    private int selectFont = 1;
    private float fontSize = 18f;

    private String newFilePath;

    public EditorGUI(){
        // Load Fonts
        fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        fontNames = new String[fonts.length];
        for(int i = 0; i < fonts.length; i++){
            fontNames[i] = fonts[i].getName();
        }

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


        fontList = new JList(fontNames);


        fontsDialog.setSize(400, 300);
        fontsDialog.add(fontsPanel);
        fontsDialog.setTitle("Set Font");

        fontsPanel.setLayout(null);

        fontsPanel.add(fontSizeField);
        fontsPanel.add(fontScroll);
        fontsPanel.add(sizeLabel);
        fontScroll.setViewportView(fontList);

        fontSizeField.setBounds(25, 20, 50, 25);
        fontScroll.setBounds(25, 70, 325, 150);
        sizeLabel.setBounds(80, 20, 150, 25);

        updateFont();

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

        fontList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectFont = fontList.getSelectedIndex();
                updateFont();
            }
        });

        fontSizeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontSize = Float.parseFloat(fontSizeField.getText());
                updateFont();
            }
        });

        fontSizeField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                char chr = ke.getKeyChar();

                if(!Character.isDigit(chr)){
                    ke.consume();
                }
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

    private void updateFont(){
        editorPane.setFont(fonts[selectFont].deriveFont(fontSize));
    }
}
