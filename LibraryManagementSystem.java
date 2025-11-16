import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

    // Book class to represent book details
    class Book {
        private String title;
        private String author;
        private boolean issued;

        public Book(String title, String author) {
            this.title = title;
            this.author = author;
            this.issued = false;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isIssued() {
            return issued;
        }

        public void issue() {
            issued = true;
        }

        public void returned() {
            issued = false;
        }
    }

    // Main Library Management System class with GUI
    public class LibraryManagementSystem extends JFrame {
        private ArrayList<Book> books = new ArrayList<>();
        private JTextArea displayArea;

        public LibraryManagementSystem() {
            // Set up frame attributes
            setTitle("Library Management System");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Create main panel and layout
            JPanel panel = new JPanel(new BorderLayout());
            getContentPane().add(panel);

            // Display area for book details
            displayArea = new JTextArea();
            displayArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(displayArea);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Control buttons panel
            JPanel buttonPanel = new JPanel();

            JButton addButton = new JButton("Add Book");
            addButton.addActionListener(e -> addBookDialog());

            JButton viewButton = new JButton("View Books");
            viewButton.addActionListener(e -> viewBooks());

            JButton issueButton = new JButton("Issue Book");
            issueButton.addActionListener(e -> issueBookDialog());

            JButton returnButton = new JButton("Return Book");
            returnButton.addActionListener(e -> returnBookDialog());

            buttonPanel.add(addButton);
            buttonPanel.add(viewButton);
            buttonPanel.add(issueButton);
            buttonPanel.add(returnButton);

            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // Dialog to add book
        private void addBookDialog() {
            JTextField titleField = new JTextField(10);
            JTextField authorField = new JTextField(10);

            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Title:"));
            inputPanel.add(titleField);
            inputPanel.add(new JLabel("Author:"));
            inputPanel.add(authorField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Add Book", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();

                if (title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Title and Author cannot be empty.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                books.add(new Book(title, author));
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            }
        }

        // Display all books
        private void viewBooks() {
            StringBuilder sb = new StringBuilder();
            if (books.isEmpty()) {
                sb.append("No books available in the library.\n");
            } else {
                for (int i = 0; i < books.size(); i++) {
                    Book b = books.get(i);
                    sb.append((i + 1)).append(". ")
                            .append(b.getTitle()).append(" by ")
                            .append(b.getAuthor())
                            .append(b.isIssued() ? " (Issued)" : " (Available)")
                            .append("\n");
                }
            }
            displayArea.setText(sb.toString());
        }

        // Dialog to issue a book
        private void issueBookDialog() {
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books to issue.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] bookTitles = books.stream()
                    .map(b -> b.getTitle() + (b.isIssued() ? " (Issued)" : ""))
                    .toArray(String[]::new);

            String bookToIssue = (String) JOptionPane.showInputDialog(this,
                    "Select a book to issue:",
                    "Issue Book",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    bookTitles,
                    bookTitles[0]);

            if (bookToIssue != null) {
                int index = -1;
                for (int i = 0; i < bookTitles.length; i++) {
                    if (bookTitles[i].equals(bookToIssue)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    if (!books.get(index).isIssued()) {
                        books.get(index).issue();
                        JOptionPane.showMessageDialog(this, "Book issued successfully.");
                        viewBooks();
                    } else {
                        JOptionPane.showMessageDialog(this, "Book is already issued.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        // Dialog to return book
        private void returnBookDialog() {
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books to return.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] issuedBooks = books.stream()
                    .filter(Book::isIssued)
                    .map(Book::getTitle)
                    .toArray(String[]::new);

            if (issuedBooks.length == 0) {
                JOptionPane.showMessageDialog(this, "No books are currently issued.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String bookToReturn = (String) JOptionPane.showInputDialog(this,
                    "Select a book to return:",
                    "Return Book",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    issuedBooks,
                    issuedBooks[0]);

            if (bookToReturn != null) {
                for (Book b : books) {
                    if (b.getTitle().equals(bookToReturn) && b.isIssued()) {
                        b.returned();
                        JOptionPane.showMessageDialog(this, "Book returned successfully.");
                        viewBooks();
                        break;
                    }
                }
            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                LibraryManagementSystem lms = new LibraryManagementSystem();
                lms.setVisible(true);
            });
        }
    }
