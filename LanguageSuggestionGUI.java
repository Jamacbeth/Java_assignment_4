import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LanguageSuggestionGUI {
    private static Map<String, String> templateFiles = new HashMap<>();
    private static Map<String, Color> textColors = new HashMap<>();

    static {
        // Map project type to template file
        templateFiles.put("web", "templates/JavaScriptWeb.txt");
        templateFiles.put("mobile", "templates/JavaMobile.txt");
        templateFiles.put("game", "templates/CSharpGame.txt");
        templateFiles.put("data", "templates/PythonData.txt");

        // Map project type to text color
        textColors.put("web", new Color(135, 206, 250)); // light sky blue
        textColors.put("mobile", Color.YELLOW);
        textColors.put("game", Color.RED);
        textColors.put("data", Color.GREEN);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LanguageSuggestionGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Language Suggestion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        // Main panel with dark background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setLayout(new BorderLayout(10, 10));
        frame.setContentPane(panel);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(panel.getBackground());
        buttonsPanel.setLayout(new FlowLayout());

        String[] projectTypes = {"web", "mobile", "game", "data"};
        JTextArea templateArea = new JTextArea();
        templateArea.setEditable(false);
        templateArea.setLineWrap(true);
        templateArea.setWrapStyleWord(true);
        templateArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        templateArea.setBackground(new Color(30, 30, 30));
        templateArea.setForeground(Color.WHITE); // default

        JScrollPane scrollPane = new JScrollPane(templateArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        for (String type : projectTypes) {
            JButton btn = new JButton(type.toUpperCase());
            btn.setFocusPainted(false);
            // Keep the button colors as they currently are
            if (type.equals("web")) btn.setBackground(new Color(70, 130, 180)); // steel blue
            else if (type.equals("mobile")) btn.setBackground(Color.YELLOW);
            else if (type.equals("game")) btn.setBackground(Color.RED);
            else if (type.equals("data")) btn.setBackground(Color.GREEN);
            btn.addActionListener(e -> loadTemplate(type, templateArea));
            buttonsPanel.add(btn);
        }

        // Copy button
        JButton copyBtn = new JButton("COPY TO CLIPBOARD");
        copyBtn.setBackground(Color.LIGHT_GRAY);
        copyBtn.setFocusPainted(false);
        copyBtn.addActionListener(e -> {
            String text = templateArea.getText();
            if (!text.isEmpty()) {
                Toolkit.getDefaultToolkit()
                       .getSystemClipboard()
                       .setContents(new java.awt.datatransfer.StringSelection(text), null);
                JOptionPane.showMessageDialog(frame, "Template copied to clipboard!");
            } else {
                JOptionPane.showMessageDialog(frame, "No template to copy.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonsPanel.add(copyBtn);

        panel.add(buttonsPanel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private static void loadTemplate(String type, JTextArea templateArea) {
        templateArea.setText(""); // clear previous text
        templateArea.setForeground(textColors.get(type)); // change text color
        String filePath = templateFiles.get(type);
        File file = new File(filePath);
        if (!file.exists()) {
            templateArea.setText("Error: template not found: " + filePath);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Project type: ").append(type).append("\n");
        sb.append("Suggested language: ").append(suggestLanguage(type)).append("\n\n");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            templateArea.setText(sb.toString());
            templateArea.setCaretPosition(0); // scroll to top
        } catch (IOException e) {
            templateArea.setText("Error reading template: " + e.getMessage());
        }
    }

    private static String suggestLanguage(String type) {
        switch (type) {
            case "web": return "JavaScript";
            case "mobile": return "Java";
            case "game": return "C#";
            case "data": return "Python";
            default: return "Unknown";
        }
    }
}
