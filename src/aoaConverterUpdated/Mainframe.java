package aoaConverterUpdated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Mainframe extends JFrame implements ActionListener {
	private JPanel rightPanel;
	private JPanel totalsPanel;
	private JLabel x1Label;
	private JLabel x2Label;
	private JButton clearButton;
	private JPanel controlsPanel;
	private JButton convertButton;
	private JSpinner howMany;
	private JTextField costCode;
	private JTextField priceField;
	private JTextArea output;
	private int newCodes = 0;

	DecimalFormat df = new DecimalFormat("#.00");

	public Mainframe() {
		super("AOA cost code converter");

		rightPanel = new JPanel();
		totalsPanel = new JPanel();
		controlsPanel = new JPanel();
		Color backColor = totalsPanel.getBackground();

		output = new JTextArea();
		output.setEditable(false);
		output.setBackground(backColor);
		output.setFont(output.getFont().deriveFont(16.0f));

		setControlsPanel();
		setTotalsPanel();

		convertButton.addActionListener(this);
		clearButton.addActionListener(this);

		setLayout(new BorderLayout());
		rightPanel.setLayout(new BorderLayout());
		totalsPanel.setLayout(new FlowLayout());

		rightPanel.add(output, BorderLayout.CENTER);
		add(controlsPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.CENTER);
		rightPanel.add(totalsPanel, BorderLayout.SOUTH);

		costCode.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					convertButton.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		priceField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					convertButton.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		setSize(650, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void setTotalsPanel() {
		x1Label = new JLabel("0");
		x2Label = new JLabel("0");
		clearButton = new JButton("Clear");
		totalsPanel.setLayout(new FlowLayout());
		totalsPanel.add(new JLabel("TOTALS: "));
		totalsPanel.add(new JLabel("Cost to AOA: $"));
		totalsPanel.add(x1Label);
		totalsPanel.add(new JLabel("   Cost to customer: $"));
		totalsPanel.add(x2Label);
		totalsPanel.add(new JLabel("    "));
		totalsPanel.add(clearButton);
	}

	private void setControlsPanel() {
		convertButton = new JButton("Convert");
		howMany = new JSpinner();
		costCode = new JTextField(4);
		priceField = new JTextField(4);
		howMany.setValue(1);

		Dimension dim1 = controlsPanel.getPreferredSize();
		dim1.width = 200;
		controlsPanel.setPreferredSize(dim1);
		controlsPanel.setLayout(new FlowLayout());
		controlsPanel.add(new JLabel("             "));
		controlsPanel.add(new JLabel("To convert cost codes to prices:"));
		controlsPanel.add(howMany);
		controlsPanel.add(new JLabel(" Parts at cost code "));
		controlsPanel.add(costCode);
		controlsPanel.add(new JLabel("                    "));
		controlsPanel.add(new JLabel("To convert a price to cost code:"));
		controlsPanel.add(new JLabel("Enter price: "));
		controlsPanel.add(priceField);
		controlsPanel.add(new JLabel("    "));
		controlsPanel.add(new JLabel(
				"                                               "));
		controlsPanel.add(convertButton);

	}

	public void pressedKey(KeyEvent e){
		
	}
	
	public void actionPerformed(ActionEvent e) {

		JButton clicked = (JButton) e.getSource();
		
		//processes a click of the convert button
		if (clicked == convertButton) {
			String codeIn = costCode.getText();
			String priceIn = priceField.getText();
			int multiplier = (int) howMany.getValue();
			double priceX1 = Double.parseDouble(x1Label.getText());
			double priceX2Total = Double.parseDouble(x2Label.getText());
			double priceX2 = 0;

			//Draws the table headers if the user has input a cost code
			if ((codeIn.length() >= 1) && (priceIn.length() < 1)) {
				if (newCodes == 0) {
					output.setText("");
					output.append("Quantity\tCost Code\t   Price\tPrice X2\n");
					newCodes = 1;
				}
				//converts the cost code to a price and displays the cost info in the table
				double newCode = Double.parseDouble(convert(codeIn).toString()) / 100;
				double price = newCode * multiplier;
				if ((newCode * 2) < 5) {
					priceX2 = 5 * multiplier;
				} else {
					priceX2 = (newCode * 2) * multiplier;
				}
				output.append(multiplier + "\t" + codeIn + "\t   " + "$"
						+ df.format(price) + "\t" + "$" + df.format(priceX2)
						+ "\n");
				String newPriceX1 = (df.format(priceX1 += price));
				String newPriceX2 = df.format((priceX2Total += priceX2));
				x1Label.setText(newPriceX1);
				x2Label.setText(newPriceX2);
				costCode.setText("") ;
				costCode.setCaretPosition(0);
			}
			
			//Posts the output if the user has input a price to convert
			if (priceIn.length() >= 1 && codeIn.length() < 1) {
				output.setText("");
				x1Label.setText("0");
				x2Label.setText("0");
				newCodes = 0;
				output.append("\n    " + priceIn + " = " + convert(priceIn).toString());
				priceField.setText("");
				priceField.setCaretPosition(0);
			}
			
			//clears the table for a new set of entries
		} else if (clicked == clearButton) {
			output.setText("");
			x1Label.setText("0");
			newCodes = 0;
			x2Label.setText("0");
		}
	}

	//Handles the conversion of letters to numbers & numbers to letters by looking up the input char in the test strings and adding the corresponding char to the return string
	public static StringBuilder convert(String input) {
		String test = "A0 B1 C2 D3 E4 F5 G6 H7 I8 J9 a0 b1 c2 d3 e4 f5 g6 h7 i8 j9";
		String test2 = "0A 1B 2C 3D 4E 5F 6G 7H 8I 9J";
		StringBuilder sb = new StringBuilder("");

		while (1 == 1) {
			int testlength = test.length();
			int test2length = test2.length();
			int length = input.length();

			for (int i = 0; i < length; i++) {
				char ch = input.charAt(i);
				if (Character.isLetter(ch)) {
					for (int j = 0; j < testlength; j++) {
						char te = test.charAt(j);
						if (ch == te)
							sb.append(test.charAt(j + 1));
					}

					if (i == length - 1) {
						String cost = sb.toString();
						double price = (Double.parseDouble(cost)) / 100;
						return sb;
					}
				} else if (Character.isDigit(ch)) {
					for (int j = 0; j < test2length; j++) {
						char te = test2.charAt(j);
						if (ch == te)
							sb.append(test2.charAt(j + 1));
					}
					if (i == length - 1) {
						return sb;
					}
				}
			}
		}
	}
}
