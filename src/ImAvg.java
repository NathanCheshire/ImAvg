import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import static java.awt.Font.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.SwingConstants.*;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

@SuppressWarnings("all")
public class ImAvg implements MouseMotionListener, ActionListener, MouseListener, WindowListener {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private LinkedList AwaitingImages = new LinkedList<File>();

    private int ScreenY = (int)this.screenSize.getHeight();
    private int ScreenX = (int)this.screenSize.getWidth();

    private ImageIcon scaledDown = new ImageIcon(new ImageIcon("ImAvgIcon.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

    private JTextField DirectoryField;
    private BufferedImage saveImage;
    private JTextArea OutputArea;
    private String storeLocation;
    private JButton ChooseFiles;
    private JButton minimize;
    private JLabel ImagesNum;
    private JButton Start;
    private JButton close;
    private JButton Clear;
    private JFrame MainFrame;

    private int RestoreX;
    private int RestoreY;
    private int xMouse;
    private int yMouse;
    private int height = 0;
    private int width = 0;

    public static void main(String[] useless) {
        UIManager.put("ToolTip.background", new Color(39, 40, 34));
        UIManager.put("ToolTip.border", Color.black);
        UIManager.put("ToolTip.font", new Font("Tahoma", BOLD, 10));
        UIManager.put("ToolTip.foreground", new Color(85, 85, 255));
        new ImAvg();
    }

    private ImAvg() {
        try {
            MainFrame = new JFrame();

            MainFrame.setUndecorated(true);

            MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            MainFrame.setBounds(0, 0, 432, 520);

            MainFrame.setTitle("IA");

            MainFrame.addWindowListener(this);

            MainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/ImAvgIcon.png")));

            JPanel parentPanel = new JPanel();

            MainFrame.setContentPane(parentPanel);

            parentPanel.setLayout(null);

            JLabel parentLabel = new JLabel("");

            parentLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/Background.png"))));

            parentLabel.setBounds(0, 0, 432, 520);

            JLabel consoleDragLabel = new JLabel();

            consoleDragLabel.setBounds(0, 0, 432, 15);

            consoleDragLabel.setToolTipText("Drag label");

            consoleDragLabel.setOpaque(true);

            consoleDragLabel.setBackground(new Color(26, 32, 51));

            consoleDragLabel.addMouseMotionListener(this);

            Font weatherFontSmall = new Font("Segoe UI Black", BOLD, 10);

            consoleDragLabel.setFont(weatherFontSmall);

            Color vanila = new Color(252, 251, 227);

            consoleDragLabel.setForeground(vanila);

            parentLabel.add(consoleDragLabel);

            minimize = new JButton("");

            minimize.setToolTipText("Minimize");

            minimize.addActionListener(this);

            minimize.addMouseListener(this);

            minimize.setBounds(402, 2, 11, 10);

            minimize.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/Minimize1.png"))));

            consoleDragLabel.add(minimize);

            minimize.setFocusPainted(false);

            minimize.setOpaque(false);

            minimize.setContentAreaFilled(false);

            minimize.setBorderPainted(false);

            close = new JButton("");

            close.setToolTipText("Close");

            close.addActionListener(this);

            close.addMouseListener(this);

            close.setBounds(416, 2, 13, 10);

            close.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/Close1.png"))));

            consoleDragLabel.add(close);

            close.setFocusPainted(false);

            close.setOpaque(false);

            close.setContentAreaFilled(false);

            close.setBorderPainted(false);

            JLabel CreditsLabel = new JLabel("ImAvg");

            CreditsLabel.setForeground(vanila);

            CreditsLabel.setFont(new Font("Segoe UI Black", BOLD, 20));

            CreditsLabel.setBounds(188, 5, 250, 50);

            parentLabel.add(CreditsLabel);

            JLabel DescriptionLabel = new JLabel("An image processing tool by Nathan Cheshire");

            DescriptionLabel.setForeground(vanila);

            DescriptionLabel.setFont(weatherFontSmall);

            DescriptionLabel.setBounds(108, 25, 250, 50);

            parentLabel.add(DescriptionLabel, 0);

            DirectoryField = new JTextField(40);

            Color navy = new Color(26, 32, 51);
            DirectoryField.setBorder(BorderFactory.createLineBorder(navy, 1));

            DirectoryField.addActionListener(this);

            DirectoryField.setToolTipText("Select a folder");

            Font buttonFont = new Font("Segoe UI Black", BOLD, 7);

            DirectoryField.setFont(buttonFont);

            DirectoryField.setSelectionColor(new Color(204, 153, 0));

            DirectoryField.setBackground(new Color(75, 75, 75));

            DirectoryField.setForeground(vanila);

            DirectoryField.setCaretColor(vanila);

            DirectoryField.setBounds(72, 75, 125, 22);

            parentLabel.add(DirectoryField);

            ChooseFiles = new JButton("Select images");

            ChooseFiles.setFocusPainted(false);

            ChooseFiles.setBackground(new Color(70, 70, 70));

            ChooseFiles.setForeground(vanila);

            ChooseFiles.setFont(buttonFont);

            ChooseFiles.addActionListener(this);

            ChooseFiles.setToolTipText("Images to process");

            ChooseFiles.setBounds(251, 75, 125, 22);

            ChooseFiles.setBorder(BorderFactory.createLineBorder(navy, 1));

            parentLabel.add(ChooseFiles);

            JLabel OR = new JLabel("OR");

            OR.setForeground(vanila);

            OR.setFont(weatherFontSmall);

            OR.setBounds(216, 72, 25, 25);

            parentLabel.add(OR);

            JLabel imagesFound = new JLabel("Images found: ");

            imagesFound.setForeground(vanila);

            imagesFound.setFont(weatherFontSmall);

            imagesFound.setBounds(181, 110, 100, 100);

            parentLabel.add(imagesFound);

            ImagesNum = new JLabel("[No images]", CENTER);

            ImagesNum.setForeground(vanila);

            ImagesNum.setFont(weatherFontSmall);

            ImagesNum.setBounds(171, 125, 100, 100);

            parentLabel.add(ImagesNum);

            Start = new JButton("Start Processing");

            Start.setBorder(BorderFactory.createLineBorder(navy, 1));

            Start.setFocusPainted(false);

            Start.setBackground(new Color(70, 70, 70));

            Start.setForeground(vanila);

            Start.setFont(buttonFont);

            Start.addActionListener(this);

            Start.setToolTipText("Take the average");

            Start.setBounds(144, 200, 150, 22);

            parentLabel.add(Start, 0);

            OutputArea = new JTextArea();

            OutputArea.setBorder(BorderFactory.createLineBorder(navy, 1));

            OutputArea.setToolTipText("Selected images");

            OutputArea.setBounds(72, 250, 300, 164);

            OutputArea.setEditable(false);

            OutputArea.setAutoscrolls(true);

            OutputArea.setLineWrap(true);

            OutputArea.setWrapStyleWord(true);

            OutputArea.setFocusable(true);

            OutputArea.setSelectionColor(new Color(204, 153, 0));

            OutputArea.setOpaque(true);

            OutputArea.setForeground(vanila);

            OutputArea.setFont(new Font("Segoe UI Black", BOLD, 7));

            OutputArea.setBackground(new Color(70, 70, 70));

            JScrollPane outputScroll = new JScrollPane(OutputArea, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

            outputScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

            outputScroll.getViewport().setBorder(null);

            outputScroll.getViewport().setOpaque(false);

            outputScroll.setOpaque(false);

            outputScroll.setBorder(BorderFactory.createEmptyBorder());

            outputScroll.setBounds(72, 250, 300, 164);

            parentLabel.add(outputScroll, 0);

            Clear = new JButton("Clear");

            Clear.setBorder(BorderFactory.createLineBorder(navy, 1));

            Clear.setFocusPainted(false);

            Clear.setBackground(new Color(70, 70, 70));

            Clear.setForeground(vanila);

            Clear.setFont(buttonFont);

            Clear.addActionListener(this);

            Clear.setToolTipText("clear selection");

            Clear.setBounds(159, 430, 125, 22);

            parentLabel.add(Clear, 0);

            NProg nprog = new NProg();

            nprog.setBounds(159, 470, 125, 22);

            parentLabel.add(nprog, CENTER);

            parentPanel.add(parentLabel);

            MainFrame.repaint();

            MainFrame.setLocationRelativeTo(null);

            MainFrame.setVisible(true);

            MainFrame.setAlwaysOnTop(true);

            MainFrame.setAlwaysOnTop(false);

            DirectoryField.requestFocus();
        }

        catch (Exception e) {
            windowsFeel();

            JOptionPane.showMessageDialog(null, "Please note that there are no employees.\n" +
                    "You will need to add at least one employee before you can use the program.","No employees were found", JOptionPane.INFORMATION_MESSAGE,scaledDown);

            swingFeel();
        }
    }

    private void swingFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            JFrame bodgeFrame = new JFrame();

            SwingUtilities.updateComponentTreeUI(bodgeFrame);
        }

        catch (Exception ex) {
            windowsFeel();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            JOptionPane.showMessageDialog(null,"An unfortunate error occured.\nMaybe Nathan is an idiot?\n" +
                    "We won't know unless you email him the following error at: NathanJavaDevelopment@gmail.com\nError: " + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE,scaledDown);

            swingFeel();
        }
    }

    private void windowsFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            JFrame bodgeFrame = new JFrame();

            SwingUtilities.updateComponentTreeUI(bodgeFrame);
        }

        catch (Exception ex) {
            windowsFeel();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            JOptionPane.showMessageDialog(null,"An unfortunate error occured.\nMaybe Nathan is an idiot?\n" +
                    "We won't know unless you email him the following error at: NathanJavaDevelopment@gmail.com\nError: " + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE,scaledDown);

            swingFeel();
        }
    }

    private void CloseAnimation(JFrame frame) {
        try {
            if (frame != null && frame.isVisible()) {
                Point point = frame.getLocationOnScreen();

                int x = (int)point.getX();
                int y = (int)point.getY();

                for(int i = y ; i >= 0 - frame.getHeight() ; i -= 30) {
                    Thread.sleep(1L);
                    frame.setLocation(x, i);
                }
            }
        }

        catch (Exception ex) {
            windowsFeel();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            JOptionPane.showMessageDialog(null,"An unfortunate error occured.\nMaybe Nathan is an idiot?\n" +
                    "We won't know unless you email him the following error at: NathanJavaDevelopment@gmail.com\nError: " + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE,scaledDown);

            swingFeel();
        }
    }

    private void MinimizeAnimation(JFrame frame) {
        Point point = frame.getLocationOnScreen();

        int x = (int)point.getX();
        int y = (int)point.getY();

        try {
            for(int i = y ; i <= frame.getHeight() ; i += 30) {
                Thread.sleep(1L);
                frame.setLocation(x, i);
            }
        }

        catch (Exception ex) {
            windowsFeel();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            JOptionPane.showMessageDialog(null,"An unfortunate error occured.\nMaybe Nathan is an idiot?\n" +
                    "We won't know unless you email him the following error at: NathanJavaDevelopment@gmail.com\nError: " + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE,scaledDown);

            swingFeel();
        }
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getXOnScreen();
        int y = e.getYOnScreen();

        if (this.MainFrame != null && this.MainFrame.isFocused()) {
            this.MainFrame.setLocation(x - this.xMouse, y - this.yMouse);
        }
    }

    public void mouseMoved(MouseEvent e) {
        this.xMouse = e.getX();
        this.yMouse = e.getY();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        Object control = e.getSource();

        if (control == this.minimize) {
            this.minimize.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/Minimize2.png"))));
        }

        else if (control == this.close) {
            this.close.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/Close2.png"))));
        }
    }

    public void mouseExited(MouseEvent e) {
        Object control = e.getSource();

        if (control == this.minimize) {
            this.minimize.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/Minimize1.png"))));
        }

        else if (control == this.close) {
            this.close.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/Close1.png"))));
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == close) {
            CloseAnimation(MainFrame);

            System.exit(0);
        } 
        
        else if (source == minimize) {
            RestoreX = MainFrame.getX();
            RestoreY = MainFrame.getY();

            MinimizeAnimation(MainFrame);

            MainFrame.setState(1);
        }

        else if (source == DirectoryField) {
            String Dir = DirectoryField.getText();

            if ((Dir != null) && !Dir.equals("") && (Dir.length() != 0)) {
                if (!(new File(Dir)).exists()) {
                    windowsFeel();

                    JOptionPane.showMessageDialog(null, "Sorry, " + System.getProperty("user.name") + "," + " but this is an invalid directory.\nPlease ensure that you have entered the " + "path correctly.", "", JOptionPane.ERROR_MESSAGE, scaledDown);

                    swingFeel();
                }

                else {
                    storeLocation = Dir;

                    LocateFiles(new File(Dir));
                }
            }

            else {
                windowsFeel();

                JOptionPane.showMessageDialog(null,"Try entering a valid directory", "No directory specified", JOptionPane.INFORMATION_MESSAGE, scaledDown);

                swingFeel();
            }
        }

        else if (source == ChooseFiles) {

            File[] files = GetFiles();

            if (files != null && files.length > 0) {
                Collections.addAll(AwaitingImages, files);

                storeLocation = ((File) AwaitingImages.get(0)).getAbsolutePath().substring(0,((File) AwaitingImages.get(0)).getAbsolutePath().lastIndexOf(File.separator));

                ImagesNum.setText(String.valueOf(files.length));

                StringBuilder build = new StringBuilder();

                for(File file: files) {
                    build.append(file.getAbsoluteFile()).append("\n");
                }

                OutputArea.setText("");

                OutputArea.setText(build.toString());
            }
        }

        else if (source == Start) {
            if (AwaitingImages != null) {
                if (AwaitingImages.size() > 0) {
                    AverageImages();
                }

                else {
                    windowsFeel();

                    JOptionPane.showMessageDialog(null,
                            "Sorry, " + System.getProperty("user.name") + ", but there are no files waiting to be averaged."
                                ,"", JOptionPane.ERROR_MESSAGE,scaledDown);

                    swingFeel();
                }
            }
        }

        else if (source == Clear) {
            OutputArea.setText("");

            ImagesNum.setText("[No images]");

            DirectoryField.setText("");

            AwaitingImages = null;

            width = 0;

            height = 0;

            saveImage = null;
        }
    }

    public void windowDeiconified(WindowEvent e) {
        Object Source = e.getSource();

        if (Source == this.MainFrame) {
            this.MainFrame.setLocation(this.RestoreX, this.RestoreY);

            this.MainFrame.setVisible(true);
        }
    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowOpened(WindowEvent e) {

    }

    private void LocateFiles(File start) {
        if (start.isFile()) {
            windowsFeel();

            JOptionPane.showMessageDialog(null,
                    "Sorry, " + System.getProperty("user.name") + ", but that" + " is a file in itself." +
                            "\nI'm afraid that I can only accept a directory.", "", JOptionPane.ERROR_MESSAGE,scaledDown);

            swingFeel();
        }

        else {
            File[] AllFiles = start.listFiles();

            LinkedList<File> Images = new LinkedList();

            File[] files = AllFiles;

            assert AllFiles != null;

            int len = AllFiles.length;

            for(int i = 0 ; i < len ; ++i) {
                File file = files[i];

                if ((file.getName().endsWith(".png") || file.getName().endsWith(".jpg") ||
                     file.getName().endsWith(".gif") || file.getName().endsWith(".GIF") ||
                     file.getName().endsWith(".PNG") || file.getName().endsWith(".JPG")) && !file.getName().startsWith("imAvg")) {
                    Images.add(file.getAbsoluteFile());
                }
            }

            if (Images.size() > 0) {
                ImagesNum.setText(String.valueOf(Images.size()));

                AwaitingImages = Images;

                String build = "";

                File file;

                for(Iterator iter = AwaitingImages.iterator(); iter.hasNext(); build = build + file.getAbsoluteFile() + "\n") {
                    file = (File )iter.next();
                }

                OutputArea.setText("");

                OutputArea.setText(build);

                Start.requestFocus();
            }

            else {
                windowsFeel();

                JOptionPane.showMessageDialog(null,
                        "Sorry, " + System.getProperty("user.name") + ", but there are no supported files in this directory."
                        ,"", JOptionPane.ERROR_MESSAGE,scaledDown);

                swingFeel();
            }
        }
    }

    private File[] GetFiles() {
        windowsFeel();

        try {
            JFileChooser Chooser = new JFileChooser();

            Chooser.setDialogTitle("Select any number of png, jpg, or gif files");

            Chooser.setMultiSelectionEnabled(true);

            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg", "PNG file", "png", "GIF file", "gif");

            Chooser.setFileFilter(filter);

            int choice;

            Chooser.setPreferredSize(new Dimension(this.ScreenX - this.ScreenX / 4, this.ScreenY - this.ScreenY / 4));

            choice = Chooser.showOpenDialog(this.MainFrame);

            if (choice == 0) {

                return Chooser.getSelectedFiles();
            }
        }

        catch (Exception ex) {
            windowsFeel();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            JOptionPane.showMessageDialog(null,"An unfortunate error occured.\nMaybe Nathan is an idiot?\n" +
                    "We won't know unless you email him the following error at: NathanJavaDevelopment@gmail.com\nError: " + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE,scaledDown);

            swingFeel();
        }

        return null;
    }

    private void AverageImages() {
        if (AwaitingImages.size() > 1 && ExtensionCheck()) {

            try {
                width = 0;
                height = 0;

                for(int i = 0 ; i < AwaitingImages.size() ; ++i) {
                    BufferedImage currentImage = ImageIO.read((File)AwaitingImages.get(i));

                    if (currentImage.getWidth() > width) {
                        width = currentImage.getWidth();
                    }

                    if (currentImage.getHeight() > height) {
                        height = currentImage.getHeight();
                    }
                }

                saveImage = new BufferedImage(width, height, TYPE_INT_ARGB);

                Graphics2D graph = saveImage.createGraphics();

                graph.setPaint (new Color(255,255,255,0));

                graph.fillRect ( 0, 0, width, height);

                ComputeAverage();

                File outFile = new File(storeLocation + "\\Average_Image_" + ((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L) + ".png");

                ImageIO.write(saveImage, "png", outFile);
            }

            catch (Exception ex) {
                windowsFeel();

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);

                JOptionPane.showMessageDialog(null,"An unfortunate error occured.\nMaybe Nathan is an idiot?\n" +
                        "We won't know unless you email him the following error at: " +
                        "NathanJavaDevelopment@gmail.com\nError: " + sw.toString(), "Error", JOptionPane.ERROR_MESSAGE,scaledDown);

                swingFeel();
            }
        }

        OutputArea.setText("");

        DirectoryField.setText("");

        AwaitingImages = null;

        width = 0;

        height = 0;

        saveImage = null;

        ImagesNum.setText("[No images]");
    }

    private boolean ExtensionCheck() {
        Iterator iter = this.AwaitingImages.iterator();

        File file;

        do {
            if (!iter.hasNext()) {
                return true;
            }

            file = (File) iter.next();
        }

        while(file.getName().endsWith(".png") || file.getName().endsWith(".PNG") ||
              file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG") ||
              file.getName().endsWith(".JPEG") || file.getName().endsWith(".jpeg") ||
              file.getName().endsWith(".gif") || file.getName().endsWith(".GIF"));

        return false;
    }

    private void ComputeAverage () {
        try {
            int[][] pixels = new int[height][width];

            for (int i = 0 ; i < AwaitingImages.size() ; i++) {
                BufferedImage bufferImage = ImageIO.read((File) AwaitingImages.get(i));

                int[][] currentPixels = convertTo2DWithoutUsingGetRGB(bufferImage);

                int currentHeight = bufferImage.getHeight();
                int currentWidth = bufferImage.getWidth();
                int currentXOffset = (width - currentWidth) / 2;
                int currentYOffset = (height - currentHeight) / 2;

                for (int y = 0 ; y < currentPixels.length ; y++) {
                    for (int x = 0 ; x < currentPixels[0].length ; x++) {
                        pixels[y + currentYOffset][x + currentXOffset] += currentPixels[y][x];
                        pixels[y + currentYOffset][x + currentXOffset] /= 2;
                        //note this divide by 2 does not work as it does not result in the proper color
                        //so come back and fix this sometime
                    }
                }
            }

            for (int y = 0 ; y < pixels.length ; y++) {
                for (int x = 0 ; x < pixels[0].length ; x++) {
                    saveImage.setRGB(x,y,pixels[y][x]);
                }
            }
        }

        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage() + "\nStack trace:\n\n");
            e.printStackTrace();
        }
    }

    private int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;

                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;

                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public class NProg extends JPanel {

        private MediaProgressBar pb;
        private int value = 0;
        private int delta = 25;

        NProg() {
            setBackground(new Color(26, 32, 51));
            setLayout(new GridBagLayout());
            pb = new MediaProgressBar();
            add(pb);

            Timer timer = new Timer(500, e -> {
                if (value + delta > 100) {
                    delta *= -1;
                    value = 100;
                } else if (value + delta < 0) {
                    delta *= -1;
                    value = 0;
                }
                value += delta;
                pb.setValue(value);
            });

            timer.start();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, 100);
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x,y,width,height);
        }

        @Override
        public void setSize(int width, int height) {
            super.setSize(width, height);
        }

        @Override
        public void setToolTipText(String toolTipText) {
            super.setToolTipText(toolTipText);
        }
    }

    public class MediaProgressBar extends JProgressBar {

        MediaProgressBar() {
            setUI(new MediaProgressBarUI());
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(125, 22);
        }
    }

    public class MediaProgressBarUI extends BasicProgressBarUI {

        private Handler handler;

        private double renderProgress = 0;
        private double targetProgress = 0;
        private double progressDelta = 0.009;

        private Timer repaintTimer;
        private Timer paintTimer;

        MediaProgressBarUI() {
            repaintTimer = new Timer(30, e -> progressBar.repaint());

            repaintTimer.setRepeats(false);
            repaintTimer.setCoalesce(true);

            paintTimer = new Timer(40, e -> {
                if (progressDelta < 0) {
                    if (renderProgress + progressDelta < targetProgress) {
                        ((Timer) e.getSource()).stop();
                        renderProgress = targetProgress + progressDelta;
                    }
                }

                else {
                    if (renderProgress + progressDelta > targetProgress) {
                        ((Timer) e.getSource()).stop();

                        renderProgress = targetProgress - progressDelta;
                    }
                }

                renderProgress += progressDelta;

                requestRepaint();
            });
        }

        void requestRepaint() {
            repaintTimer.restart();
        }

        @Override
        protected void installDefaults() {
            super.installDefaults();

            progressBar.setOpaque(false);
            progressBar.setBorder(null);
        }

        void setRenderProgress(double value) {
            if (value != targetProgress) {
                paintTimer.stop();

                targetProgress = value;

                if (targetProgress < renderProgress && progressDelta > 0) {
                    progressDelta *= -1;
                }

                else if (targetProgress > renderProgress && progressDelta < 0) {
                    progressDelta *= -1;
                }

                paintTimer.start();
            }
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int iStrokWidth = 3;
            g2d.setStroke(new BasicStroke(iStrokWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(c.getBackground());
            g2d.setBackground(c.getBackground());

            int width = c.getWidth();
            int height = c.getHeight();

            RoundRectangle2D outline = new RoundRectangle2D.Double(iStrokWidth / 2, iStrokWidth / 2,
                    width - iStrokWidth, height - iStrokWidth, height, height);

            g2d.draw(outline);

            int iInnerHeight = height - (iStrokWidth * 4);
            int iInnerWidth = width - (iStrokWidth * 4);

            iInnerWidth = (int) Math.round(iInnerWidth * renderProgress);

            int x = iStrokWidth * 2;
            int y = iStrokWidth * 2;

            Point2D start = new Point2D.Double(x, y);
            Point2D end = new Point2D.Double(x, y + iInnerHeight);

            float[] dist = {0.0f, 0.25f, 1.0f};
            Color[] colors = {c.getBackground(), c.getBackground().brighter(), c.getBackground().darker()};
            LinearGradientPaint p = new LinearGradientPaint(start, end, dist, colors);

            g2d.setPaint(p);

            RoundRectangle2D fill = new RoundRectangle2D.Double(iStrokWidth * 2, iStrokWidth * 2,
                    iInnerWidth, iInnerHeight, iInnerHeight, iInnerHeight);

            g2d.fill(fill);

            g2d.dispose();
        }

        @Override
        protected void installListeners() {
            super.installListeners();
            progressBar.addChangeListener(getChangeHandler());
        }

        ChangeListener getChangeHandler() {
            return getHandler();
        }

        Handler getHandler() {
            if (handler == null) {
                handler = new Handler();
            }

            return handler;
        }

        protected class Handler implements ChangeListener {

            @Override
            public void stateChanged(ChangeEvent e) {
                BoundedRangeModel model = progressBar.getModel();

                int newRange = model.getMaximum() - model.getMinimum();

                double dProgress = model.getValue() / (double) newRange;

                if (dProgress < 0) {
                    dProgress = 0;
                }

                else if (dProgress > 1) {
                    dProgress = 1;
                }

                setRenderProgress(dProgress);
            }
        }
    }
}