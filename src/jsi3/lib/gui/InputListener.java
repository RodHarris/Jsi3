package jsi3.lib.gui;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public interface InputListener
{
	public static int UP = KeyEvent.VK_UP;

	public static int DOWN = KeyEvent.VK_DOWN;

	public static int LEFT = KeyEvent.VK_LEFT;

	public static int RIGHT = KeyEvent.VK_RIGHT;

	public static int F1 = KeyEvent.VK_F1;

	public static int F2 = KeyEvent.VK_F2;

	public static int F3 = KeyEvent.VK_F3;

	public static int F4 = KeyEvent.VK_F4;

	public static int F5 = KeyEvent.VK_F5;

	public static int F6 = KeyEvent.VK_F6;

	public static int F7 = KeyEvent.VK_F7;

	public static int F8 = KeyEvent.VK_F8;

	public static int F9 = KeyEvent.VK_F9;

	public static int F10 = KeyEvent.VK_F10;

	public static int F11 = KeyEvent.VK_F11;

	public static int F12 = KeyEvent.VK_F12;

	public static int ESC = KeyEvent.VK_ESCAPE;

	public static int SPACE = KeyEvent.VK_SPACE;

	public static int LEFT_MOUSE = 0;

	public static int RIGHT_MOUSE = 2;

	public void key_typed( char key, int code, KeyEvent e );

	public void key_pressed( char key, int code, KeyEvent e );

	public void key_released( char key, int code, KeyEvent e );

	public void mouse_clicked( MouseEvent e );

	public void mouse_pressed( MouseEvent e );

	public void mouse_released( MouseEvent e );

	public void mouse_entered( MouseEvent e );

	public void mouse_exited( MouseEvent e );

	public void mouse_dragged( MouseEvent e );

	public void mouse_moved( MouseEvent e );

	public void mouse_wheel_moved( MouseWheelEvent e );
}
