import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.io.*;

public class StudentHubGUI extends JFrame {
	//Helper makes a panel with padding
	private JPanel paddedPanel(LayoutManager layout) {
	    JPanel p = new JPanel(layout);
	    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    p.setOpaque(false); 
	    return p;
	}

	//Helper makes a consistently-styled button
	private JButton makeButton(String text) {
	    JButton b = new JButton(text);
	    b.setFont(new Font("Courier New", Font.BOLD, 13));
	    b.setFocusPainted(false);
	    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    b.setPreferredSize(new Dimension(250, 34));
	    return b;
	}

    private StudentHub hub = new StudentHub();

    private JTextArea outputArea = new JTextArea(15, 40);
    private JButton getBillBtn;
    private JButton totalProfitBtn;
    private JButton sortNameBtn;
    private JButton sortBillBtn;
    private JButton printHubBtn;
    private JButton saveButton;
    private JButton loadButton;

    

        public StudentHubGUI() {
            super("Student Hub Tester");

            // icon + background (same idea as your JFrames class)
            ImageIcon image = new ImageIcon("flowerIcon.jpg");
            ImageIcon icon2 = new ImageIcon("pinky2.png");
            setIconImage(image.getImage());
            getContentPane().setBackground(new Color(0xFFD1DC));

            setLayout(new BorderLayout(10, 10));

            // Output area
            outputArea.setEditable(false);
            outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            JScrollPane scroll = new JScrollPane(outputArea);
            scroll.setBorder(BorderFactory.createTitledBorder("Output"));

            // ===== NORTH (title + top buttons) =====
            JPanel north = paddedPanel(new BorderLayout());
            JLabel title = new JLabel("~Student Hub~", SwingConstants.CENTER);
            title.setFont(new Font("Courier New", Font.BOLD, 20));
            north.add(title, BorderLayout.NORTH);

            JPanel topButtons = paddedPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            JButton addStudentBtn = makeButton("Add Student");
            JButton buildHubDataBtn = makeButton("Build Hub Data");
            topButtons.add(addStudentBtn);
            topButtons.add(buildHubDataBtn);

            north.add(topButtons, BorderLayout.SOUTH);

            // ===== CENTER =====
            JPanel center = paddedPanel(new GridLayout(0, 2, 10, 10)); 

            getBillBtn = makeButton("Get Student Bill");
            totalProfitBtn = makeButton("Calculate Total Profit");
            sortNameBtn = makeButton("Sort by Name");
            sortBillBtn = makeButton("Sort by Bill Amount");
            printHubBtn = makeButton("Print Hub Data");
            saveButton = makeButton("Save");
            loadButton = makeButton("Load");

            center.setBorder(BorderFactory.createTitledBorder("Actions"));
            center.add(getBillBtn);
            center.add(totalProfitBtn);
            center.add(sortNameBtn);
            center.add(sortBillBtn);
            center.add(printHubBtn);
            center.add(saveButton);
            center.add(loadButton);

            //Disable buttons until data exists
            setHubActionsEnabled(false);

            //Add to frame
            add(north, BorderLayout.NORTH);
            add(center, BorderLayout.CENTER);
            add(scroll, BorderLayout.SOUTH);

            setSize(700, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null); // centers window
            setVisible(true);
        


            
            
        // =====BUTTON ACTIONS=====
        
        
        //Add student button
        addStudentBtn.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog(this, "Enter student name:");
                if (name == null || name.isBlank()) return;

                //Area options
                String[] areaOptions = {"Cafe", "Lounge" , "Study Room"};
                String area = (String) JOptionPane.showInputDialog(
                		this,
                		"Select area:",
                		"Study Area",
                		JOptionPane.QUESTION_MESSAGE, 
                		null, //add icon
                		areaOptions,
                		areaOptions[0]
                		);
                if (area == null || area.isBlank()) return;

                //Time spent
                String timeStr = JOptionPane.showInputDialog(this, "Enter minutes spent:");
                int time = Integer.parseInt(timeStr);

                //Create the student
                Student newStudent = new Student(name, area, time);

                //Prompt for billable items (comma separated)
                String[] orderOptions = {
                		"CAKE", "COOKIE" , "BROWNIE" , "PASTRY",
                		"ESPRESSO" , "LATTE" , "MOCHA" , "AMERICANO"
                };
                
                JList<String> orderListUI = new JList<>(orderOptions);
                
                //Allow for selecting more than one item
                orderListUI.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                
                //Put list inside a scroll pane
                JScrollPane orderScroll = new JScrollPane(orderListUI);
                orderScroll.setPreferredSize(new Dimension(200,120));
                
                //Create panel that has instructions 
                JPanel orderPanel = new JPanel(new BorderLayout());
                orderPanel.add(
                		new JLabel("Hold CTRL (or ⌘ on Mac) to select multiple orders!"),
                		BorderLayout.NORTH
                		);
                
               orderPanel.add(orderScroll, BorderLayout.CENTER);
               
               //Show dialog
                int result = JOptionPane.showConfirmDialog(
                		this,
                		orderPanel,
                		"Select Cafe Orders",
                		JOptionPane.OK_CANCEL_OPTION,
                		JOptionPane.PLAIN_MESSAGE
                );
                
                if (result != JOptionPane.OK_OPTION) return;
                
                
                //Convert item to billable objects
                ArrayList<Billable> orderList = new ArrayList<>();

                List<String> selectedOrders = orderListUI.getSelectedValuesList();

                for (String orderName : selectedOrders) {
                    try {
                        orderList.add(new CafeOrder<DessertType>(DessertType.valueOf(orderName)));
                    } 
                    catch (IllegalArgumentException ex1) {
                        
                    try {
                           orderList.add(new CafeOrder<CoffeeType>(CoffeeType.valueOf(orderName)));
                    } 
                    catch (IllegalArgumentException ex2) {
                            
                    }
                 }
                }

                //Add billable orders to student
                newStudent.addOrders(orderList);

                //Add student to hub queue
                hub.buildQueue(newStudent);

                outputArea.append("\nAdded Student: " + name + "\n");
                outputArea.append("Area: " + area + "\n");
                outputArea.append("Time: " + time + " mins\n");
                outputArea.append("Orders Added: " + orderList.size() + "\n\n");
                outputArea.append("Added At: " + newStudent.getAddTimeFormatted() + "\n");

            } catch (Exception ex) {
                outputArea.append("Invalid input. Student not created.\n");
            }
        });

        
        
       //Build hub data button
       buildHubDataBtn.addActionListener(e -> {
           hub.buildHubData();
           setHubActionsEnabled(true);
           outputArea.append("Hub Data Built.\n");
        });

       
       
       //Get bill button
        getBillBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter student name:");
            try {
                outputArea.append("Bill for " + name + ": $" + hub.getStudentBill(name) + "\n");
            } catch (UnKnownStudentException ex) {
                outputArea.append("Student not found.\n");
            }
        });

        
        
        //Total profit button
        totalProfitBtn.addActionListener(e -> {
            outputArea.append("Total Hub Profit: $" + hub.calculateTotalHubProfit() + "\n");
        });

        
        //Sort by name button
        sortNameBtn.addActionListener(e -> {
            hub.sortHubDatabyStudentName();
            outputArea.append("Sorted hub data by student name.\n");
        });

        
        //Sort by bill button
        sortBillBtn.addActionListener(e -> {
            hub.sortHubDatabyStudentBill();
            outputArea.append("Sorted hub data by bill amount.\n");
        });

        
        //Print hub data button
        printHubBtn.addActionListener(e -> {
        	 outputArea.append("\n--- HUB DATA ---\n");
        	for(int i = 0; i < hub.getHubData().size(); i++)
        	{
        		for(int j = 0; j < hub.getHubData().get(i).size(); j++)
        		{
        			outputArea.append(hub.getHubData().get(i).get(j).toString() + "\n");
        		}
        	}
            
        });

        
      //Save button
        saveButton.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showSaveDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = chooser.getSelectedFile().getAbsolutePath();
                    hub.saveHubDataToFile(filePath);
                    outputArea.append("Saved successfully.\n");
                }

            } catch (IOException ex) {
                outputArea.append("Save failed: " + ex.getMessage() + "\n");
            }
        });

        
        //Load button
        loadButton.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = chooser.getSelectedFile().getAbsolutePath();
                    hub.loadHubDataFromFile(filePath);
                    outputArea.append("Loaded successfully.\n");
                    setHubActionsEnabled(true);
                }

            } catch (IOException ex) {
                outputArea.append("Load failed: " + ex.getMessage() + "\n");
            }
        });
        
        
        //Window settings
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
 
    }
        
    //Enable specific buttons once students are added
        private void setHubActionsEnabled(boolean enabled) {
        	getBillBtn.setEnabled(enabled);
        	totalProfitBtn.setEnabled(enabled);
        	sortNameBtn.setEnabled(enabled);
        	sortBillBtn.setEnabled(enabled);
        	printHubBtn.setEnabled(enabled);
        }
        
        
        
  
    public static void main(String[] args) throws UnKnownStudentException{
    	
    	StudentHubGUI test = new StudentHubGUI();
    	
    }

}
