import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
@SuppressWarnings("serial")
public class Screen extends JFrame {
    private transient MyHashTable<Country, DLList<MyImage>> countryTable;
    private transient Country currentCountry;
    // dllist of images for each country, allows for images to be flipped through
    private transient DLList<MyImage> currentImages;
   private static final long serialVersionUID = 1L; // Add this field to suppress the warning
    private JPanel mainPanel, detailPanel;
    private JTextField abbrevField, nameField, urlField, landmarkField;
    private JTextArea displayArea;
    private JLabel imageLabel, landmarkLabel;
  
    private int currentImageIndex = 0;

    public Screen() {
        countryTable = new MyHashTable<>();
        setupInitialData();
        
        setTitle("Country Image Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setupMainPanel();
        setupDetailPanel();
        
        add(mainPanel);
        setVisible(true);
    }

    private void setupInitialData() {
        // Initialize with 3 countries, each with 2 images
        Country country1 = new Country("United States Of America", "us");
        Country country2 = new Country("Japan", "jp");
        Country country3 = new Country("France", "fr");
        
        DLList<MyImage> images1 = new DLList<>();
        images1.add(new MyImage("New York City", "https://w7.pngwing.com/pngs/524/107/png-transparent-statue-of-liberty-new-york-new-york-city-cities-skylines-news-building-city-skyscraper-thumbnail.png"));
        images1.add(new MyImage("Zion National Park", "https://images.squarespace-cdn.com/content/v1/5bb674d7a0cd273dc676b5e2/1565725607801-9F574387F95ZHITP6QH1/image-asset.jpeg"));
        
        DLList<MyImage> images2 = new DLList<>();
        images2.add(new MyImage("Tokyo Tower", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgjvtdP9DSS0Jjl8K96ckd7tssegbGJLBseQ&s"));
        images2.add(new MyImage("Mt. Fuji", "https://static.wixstatic.com/media/327546_8da296e6f2704a0ca6b894142559c866~mv2.png/v1/fill/w_524,h_294,al_c,q_85,usm_0.66_1.00_0.01,enc_auto/327546_8da296e6f2704a0ca6b894142559c866~mv2.png"));
        
        DLList<MyImage> images3 = new DLList<>();
        images3.add(new MyImage("Eiffel Tower", "https://res.cloudinary.com/eurocamp/image/upload/v1727432367/24_Winter_Paris_u0sxwe.png"));
        images3.add(new MyImage("Louvre Museum", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSfLmTYQz4SDwfb0IoyfyYT8F2r25H5gVjSiA&s"));
        
        countryTable.put(country1, images1);
        countryTable.put(country2, images2);
        countryTable.put(country3, images3);
    }

    private void setupMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        updateCountryListDisplay();
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        
        abbrevField = new JTextField();
        nameField = new JTextField();
        
        JButton viewCountryButton = new JButton("View Country");
        viewCountryButton.addActionListener(e -> viewCountry());
        
        JButton addCountryButton = new JButton("Add Country");
        addCountryButton.addActionListener(e -> addCountry());
        
        JButton deleteCountryButton = new JButton("Delete Country");
        deleteCountryButton.addActionListener(e -> deleteCountry());
        
        inputPanel.add(new JLabel("Country Abbreviation:"));
        inputPanel.add(abbrevField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel("Country Name: "));
        
        inputPanel.add(nameField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(viewCountryButton);
        inputPanel.add(addCountryButton);
        inputPanel.add(deleteCountryButton);
       
        
        mainPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void setupDetailPanel() {
        detailPanel = new JPanel(new BorderLayout());
        
        imageLabel = new JLabel("", JLabel.CENTER);
        landmarkLabel = new JLabel("", JLabel.CENTER);
        
        JPanel controlPanel = new JPanel();
        
        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> showPreviousImage());
        
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> showNextImage());
        
        JButton deleteImageButton = new JButton("Delete Image");
        deleteImageButton.addActionListener(e -> deleteCurrentImage());
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToMainView());
        
        urlField = new JTextField();
        landmarkField = new JTextField();
        
        JButton addImageButton = new JButton("Add Image");
        addImageButton.addActionListener(e -> addImage());
        
        controlPanel.add(prevButton);
        controlPanel.add(nextButton);
        controlPanel.add(deleteImageButton);
        controlPanel.add(backButton);
        
        JPanel addImagePanel = new JPanel(new GridLayout(3, 2));
        addImagePanel.add(new JLabel("Image URL:"));
        addImagePanel.add(urlField);
        addImagePanel.add(new JLabel("Landmark Name:"));
        addImagePanel.add(landmarkField);
        addImagePanel.add(addImageButton);
        
        detailPanel.add(controlPanel, BorderLayout.NORTH);
        detailPanel.add(imageLabel, BorderLayout.CENTER);
        detailPanel.add(landmarkLabel, BorderLayout.SOUTH);
        detailPanel.add(addImagePanel, BorderLayout.SOUTH);
    }

    private void updateCountryListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Country country : countryTable.keySet()) {
            DLList<MyImage> images = countryTable.get(country);
            sb.append(country.getName()).append(" - ").append(country.getAbbreviation()).append(" - ").append(images.size()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void viewCountry() {
        String abbrev = abbrevField.getText();
        currentCountry = getCountryByAbbreviation(abbrev);
        if (currentCountry == null) {
            JOptionPane.showMessageDialog(this, "Country not found.");
            return;
        }
        currentImages = countryTable.get(currentCountry);
        currentImageIndex = 0;
        displayImage();
        setContentPane(detailPanel);
        revalidate();
    }

    private void displayImage() {
        if (currentImages == null || currentImages.size() == 0) {
            imageLabel.setText("No Images Available");
            landmarkLabel.setText("");
            return;
        }
        
        
        MyImage image = currentImages.get(currentImageIndex);
        String url = image.getUrl();
                try {
                        URI uri = URI.create(url); // Create a URI from the URL string
                        URL imageUrl = uri.toURL(); // Convert the URI to a URL
                        ImageIcon icon = new ImageIcon(imageUrl);
                        
                        if (icon.getIconWidth() > 0) { // Check if image loaded
                            // Resize the image
                            Image scaledImage = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
                            JLabel label = new JLabel(new ImageIcon(scaledImage)); // Use scaled image
                            
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                            
                        } else {
                            System.err.println("Image not found or invalid URL: " + url);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load image from URL: " + url);
                        e.printStackTrace();
                    }
                
    
                // Show images in a dialog
        imageLabel.setText("<html><a href='" + "Landmark: " + image.getLandmarkName() + "'>" +  "Landmark: " + image.getLandmarkName() + "</a></html>");
        landmarkLabel.setText("<html><a href='" + "Landmark: " + image.getLandmarkName() + "</a></html>");
    }

    private void showPreviousImage() {
        if (currentImages != null && currentImageIndex > 0) {
            currentImageIndex--;
            displayImage();
        }
    }

    private void showNextImage() {
        if (currentImages != null && currentImageIndex < currentImages.size() - 1) {
            currentImageIndex++;
            displayImage();
        }
    }

    private void deleteCurrentImage() {
        if (currentImages != null && currentImages.size() > 0) {
            currentImages.remove(currentImageIndex);
            if (currentImageIndex >= currentImages.size()) {
                currentImageIndex = currentImages.size() - 1;
            }
            displayImage();
        }
    }

    private void addImage() {
        String url = urlField.getText();
        String landmark = landmarkField.getText();
        if (url.isEmpty() || landmark.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid URL and landmark name.");
            return;
        }
        currentImages.add(new MyImage(landmark, url));
        displayImage();
    }

    private void goBackToMainView() {
        updateCountryListDisplay();
        setContentPane(mainPanel);
        revalidate();
    }

    private void addCountry() {
        String name = nameField.getText();
        String abbrev = abbrevField.getText();
        if (name.isEmpty() || abbrev.isEmpty() || abbrev.length() != 2) {
            JOptionPane.showMessageDialog(this, "Please enter a valid country name and 2-letter abbreviation.");
            return;
        }
        Country newCountry = new Country(name, abbrev.toLowerCase());
        countryTable.put(newCountry, new DLList<>());
        updateCountryListDisplay();
    }

    private void deleteCountry() {
        String abbrev = abbrevField.getText();
        Country country = getCountryByAbbreviation(abbrev);
        if (country != null) {
            countryTable.remove(country);
            updateCountryListDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Country not found.");
        }
    }

    private Country getCountryByAbbreviation(String abbrev) {
        for (Country country : countryTable.keySet()) {
            if (country.getAbbreviation().equals(abbrev.toLowerCase())) {
                return country;
            }
        }
        return null;
    }

}
