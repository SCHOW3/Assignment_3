package Index;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SearchUI {

	
	public static void launchUI()
	{
		
		//Window Setup
		final JFrame searchWindow = new JFrame("Basic Search Engine");
		searchWindow.setVisible(true);
		searchWindow.setSize(500,200);
		searchWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Panel Setup
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(null);
		searchPanel.setBackground(Color.black);
		searchWindow.add(searchPanel);
		
		
		//TextField Setup
		final JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);
		searchField.setBounds(150, 55, 200, 30);
		
		
		//Button Setup
		JButton goButton = new JButton("Go");
		searchPanel.add(goButton);
		goButton.setBounds(200,100,100,25);
		
		
		ActionListener on_select = new ActionListener() {
			
	         public void actionPerformed(ActionEvent e) {
	        	 //This is where the code for the back end search part goes
	        	 String searchPhrase = searchField.getText();
	        	 //this is where you will want to put the method for taking 'searchPhrase'
	        	 //Initialize the backend search class
	        	 // replace {} with YOURCLASS.METHOD()
	        	 ArrayList<String> topfiveURLs;
				try {
					topfiveURLs = Searcher.search(searchPhrase);
						        	 
		             searchWindow.dispose();
		             
		             
		             final JFrame results_window = new JFrame("Results Window");
		             results_window.setVisible(true);
		             results_window.setSize(500,200);
		             results_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		             
		             JPanel results_panel = new JPanel();
	
		             results_window.add(results_panel);
		             
		             JEditorPane results_jpane = new JEditorPane();
		             results_jpane.setEditable(false);
		             results_jpane.setContentType("text/html");
		             /*
		              * this is for testing purposes only
		             
		             String[] testarray = {"<a href=\"https://www.google.com/\"> Google </a><br>",
		            		 "<a href=\"http://www.cnn.com/\"> CNN </a><br>",
		            		 "<a href=\"http://abcnews.go.com/\"> ABCNews </a><br>",
		            		 "<a href=\"https://www.reddit.com/\"> Reddit </a><br>",
		            		 "<a href=\"https://www.facebook.com/\"> Facebook </a><br>"
		            		 };
		             */
		             String paneText = "";
		             int url_count = 1;
		             if(topfiveURLs == null){
		            	 paneText = "Results Not Found";
		             }
		             else{
		             for(String links : topfiveURLs)
		             {
		            	 paneText += "<a href=\""+ links +"\">"+ url_count +": "+ links +" </a><br>";
		            	 url_count += 1;
		             }}
		             results_jpane.setText(paneText);
		             
		             
		             results_jpane.addHyperlinkListener(new HyperlinkListener(){
		            	 public void hyperlinkUpdate(HyperlinkEvent event) {
		            	        if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            	        	if(Desktop.isDesktopSupported()) {
		            	        	    try {
											Desktop.getDesktop().browse(event.getURL().toURI());
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (URISyntaxException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		            	        	}
		            	        }
		            	    }
		            	 
		             });
		             
		             
		             JScrollPane results_sp = new JScrollPane(results_jpane);
		             results_sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		             results_sp.setPreferredSize(new Dimension(250,145));
		             results_sp.setMinimumSize(new Dimension(10, 10));
		             
		             results_panel.add(results_sp);
		             
		             JButton results_close = new JButton("Close");
		             results_panel.add(results_close);
		             results_close.addActionListener(new ActionListener(){
		            	 
		            	 public void actionPerformed(ActionEvent pressed){
		            		 results_window.dispose();
		            	 }
		            	 
		             });
	             
	             
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   
	          }
	       };
		
	    goButton.addActionListener(on_select);
	    searchField.addActionListener(on_select);
	    
		//Label Setup
		
		JLabel titleLabel = new JLabel("<html><b><font size = '5' color = 'white'>Basic Search Engine</font></b></html>", JLabel.CENTER);
		searchPanel.add(titleLabel);
		titleLabel.setBounds(150,20,200,30);
		
	}
	

	
	public static void main(String[] args)
	{
		
//		SearchUI testUI = new SearchUI();
//		testUI.launchUI();
		launchUI();
		
	}
	
}
