import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SearchFrame extends JFrame {
    JPanel westPnl;
    JButton fileBtn;
    JTextArea fileArea;
    JScrollPane fileScroll;

    JPanel eastPnl;
    JTextField filterField;
    JTextArea filteredArea;
    JScrollPane filteredScroll;

    JPanel southPnl;
    JButton quitBtn;

    public SearchFrame() {
        createSouthPanel();
        createWestPanel();
        createEastPanel();

        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        setTitle("Text Search");
        setSize(780, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createWestPanel() {
        westPnl = new JPanel(new BorderLayout());
        fileBtn = new JButton("Open File");
        fileArea = new JTextArea(26, 30);
        fileScroll = new JScrollPane(fileArea);

        fileBtn.addActionListener((ActionEvent ae) -> fileHandler());

        westPnl.add(fileBtn, BorderLayout.NORTH);
        westPnl.add(fileScroll, BorderLayout.CENTER);
        add(westPnl, BorderLayout.WEST);
    }

    private void createEastPanel() {
        eastPnl = new JPanel(new BorderLayout());
        filterField = new JTextField(20);
        filteredArea = new JTextArea(26, 30);
        filteredScroll = new JScrollPane(filteredArea);

        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterHandler();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterHandler();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterHandler();
            }
        });

        eastPnl.add(filterField, BorderLayout.NORTH);
        eastPnl.add(filteredScroll, BorderLayout.CENTER);
        add(eastPnl, BorderLayout.EAST);
    }

    private void createSouthPanel() {
        southPnl = new JPanel();
        quitBtn = new JButton("Quit");

        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));

        southPnl.add(quitBtn);
        add(southPnl, BorderLayout.SOUTH);
    }

    private void fileHandler() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text files (.txt)", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            try {
                Stream<String> fileLines = Files.lines(Paths.get(chooser.getSelectedFile().getAbsolutePath()));

                fileArea.setText("");
                fileLines.forEach(line -> fileArea.append(line + "\n"));
                filterHandler();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void filterHandler() {
        String fileText = fileArea.getText();
        String filter = filterField.getText();
        if (fileText == null || fileText == "" || filter == null || filter == "") {
            filteredArea.setText("");
            return;
        }

        try {
            Stream<String> filteredLines = fileText.lines().filter(line -> line.contains(filter));

            filteredArea.setText("");
            filteredLines.forEach(line -> filteredArea.append(line + "\n"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
