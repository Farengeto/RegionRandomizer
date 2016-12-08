import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class RegionGenerator implements Observer{
	private JFrame frame;
	private RegionMain generator;
	private File map;
	private JButton run;
	private JButton fileSelect;
	private JTextArea name;
	private JTextArea points;
	private JTextArea first;
	private JTextArea second;
	private long start;
	private JFrame preview;
	
	public RegionGenerator(){
		frame = new JFrame("Region Generator");
		frame.setLayout(new BorderLayout());
		
		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(5,2));
		name = new JTextArea();
		points = new JTextArea();
		first = new JTextArea();
		second = new JTextArea();
		menu.add(new JLabel("Name of Map:"));
		menu.add(name);
		menu.add(new JLabel("Map File:"));
		fileSelect = new JButton("Select File");
		fileSelect.addActionListener(new FileSelect(this,frame));
		menu.add(fileSelect);
		menu.add(new JLabel("Number of Points:"));
		menu.add(points);
		menu.add(new JLabel("Number of First-level Divisions:"));
		menu.add(first);
		menu.add(new JLabel("Number of Second-level Divisions:"));
		menu.add(second);
		frame.add(menu,BorderLayout.CENTER);
		
		run = new JButton("Run");
		run.addActionListener(new RunListener(this));
		frame.add(run,BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		new RegionGenerator();
	}
	
	public void runGenerator(){
		run.setEnabled(false);
		run.setText("Running...");
		String n = name.getText();
		try{
			int p = Integer.parseInt(points.getText());
			int l1 = Integer.parseInt(first.getText());
			int l2 = Integer.parseInt(second.getText());
			int processors = Runtime.getRuntime().availableProcessors();
			generator = new RegionMain(n,map,map,map,p,l1,l2);
			generator.addObserver(this);
			preview = new JFrame("Preview");
			JScrollPane scroll = new JScrollPane(new RegionPanel(generator));
			scroll.setPreferredSize(new Dimension(1000,1000));
			preview.add(scroll);
			preview.pack();
			preview.setVisible(true);
			Thread t = new Thread("Map maker"){
				public void run(){
					start = System.currentTimeMillis();
					generator.makeMapThreaded2(processors);
				}
			};
			t.start();
			//generator.makeMapThreaded2(processors);
		}catch(NumberFormatException e){
			run.setText("Error: Input not a number");
			run.setEnabled(true);
		}
		catch(IllegalArgumentException e){
			run.setText("Error: No file selected");
			run.setEnabled(true);
		}
		catch(IOException e){
			run.setText("Error: Invalid file");
			run.setEnabled(true);
		}
	}
	
	public void setFile(File f){
		map = f;
		fileSelect.setText(f.getAbsolutePath());
	}
	
	public void update(Observable gen,Object o){
		double percent = (double)o;
		if(percent < 0){
			run.setText("Run");
			run.setEnabled(true);
			preview = null;
		}
		else if(percent <= 100){
			long r = (long)((System.currentTimeMillis() - start)/1000.0*(100-percent)/percent);
			run.setText("Running: " + percent + "% done (" + (r/60) + "m" + (r%60) + "s)");
			preview.repaint();
		}
	}
}
