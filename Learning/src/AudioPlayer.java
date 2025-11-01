package src;

import javax.sound.sampled.*;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class AudioPlayer { 
	static JFrame frame = new JFrame("Audio Player");
	static JPanel panel = new JPanel();
	static JCheckBox shuffle = new JCheckBox("Shuffle");
	static JCheckBox weighted = new JCheckBox("Weighted");
	static Clip clip;
	static String fileOrigin = "C:\\Users\\Rishi (New)\\javaProjects\\Audio\\";
	static String order = "set";
	static Boolean isWeighted = false;
	static int audioNumber = 0;
	static int previousAudio[] = {-1,-1,-1};
	private static void playClip(String filePath){
		try {
            File audioFile = new File(filePath);

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip = AudioSystem.getClip();

            clip.open(audioStream);

            clip.start();



        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	private static void updateFrame(){
		frame.setBounds(100,0,400,600);
		frame.add(panel);
		frame.requestFocusInWindow();
		frame.add(shuffle);
		frame.add(weighted);
		frame.setLayout(null);
		shuffle.setBounds(0,0,100,20);
		weighted.setBounds(0,40,100,20);
		weighted.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isWeighted) {
					isWeighted = false;
				}
				else if(!isWeighted){
					isWeighted = true;
				}
				System.out.println(isWeighted);
			}
			
		});
		shuffle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (order.equals("set")) {
					order= "shuffle";
				}
				else if(order.equals("shuffle")){
					order = "set";
				}
				System.out.println(order);
			}
			
		});
		panel.setBackground(Color.GRAY);
		frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	private static void chooseNext(){
		if (order.equals("set")) {
			
			Path filePath = Paths.get(fileOrigin+"audioData.txt");
        	try {
				List<String> lines = Files.readAllLines(filePath);
				if (audioNumber<lines.size()) {
					playClip(fileOrigin+lines.get(audioNumber));
					
				}
				else{
					audioNumber = 0;
					playClip(fileOrigin+lines.get(audioNumber));
				}
			} catch (IOException e) {
				
			}
			audioNumber++;
		}
		else if (order.equals("shuffle")){
			Path filePath = Paths.get(fileOrigin+"audioData.txt");
        	try {
				List<String> lines = Files.readAllLines(filePath);
				
				
				
				Random random = new Random();
				audioNumber = random.nextInt(lines.size()-1);
				while (audioNumber==previousAudio[0]||audioNumber==previousAudio[1]||audioNumber==previousAudio[2]) {
					audioNumber = random.nextInt(lines.size()-1);
					
				}
	
				playClip(fileOrigin+lines.get(audioNumber));
				previousAudio[2] = previousAudio[1];
       			previousAudio[1] = previousAudio[0];
        		previousAudio[0] = audioNumber;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    public static void main(String[] args) {
		updateFrame();
		
        playClip(fileOrigin+"SlowItDown.wav");
		
		while (true) {
			try {
				Thread.sleep(100);
			} 
			catch (Exception e) {}
			if (!clip.isActive()) {
				chooseNext();
				System.out.println(audioNumber);
			}
		}
    }
}
