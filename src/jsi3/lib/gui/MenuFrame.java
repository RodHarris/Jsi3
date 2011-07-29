package jsi3.lib.gui;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


public abstract class MenuFrame extends JFrame
{
	private JMenuBar menuBar;
	
	private ArrayList<MenuWrapper> menusWrappers;
	
	private boolean menu_added;
	
	
	public MenuFrame()
	{
		menuBar = new JMenuBar();
		
		menusWrappers = new ArrayList<MenuWrapper>();
	}
	
	
	public abstract void menuItemSelected( String item );
	
	
	public boolean addMenuItem( String item )
	{
		return addMenuItem( item, null );
	}
	
	
	public boolean addMenuItem( String item, String accelerator )
	{
		if( ! menu_added )
		{
			setJMenuBar( menuBar );
			
			menu_added = true;
		}
		
		String[] tokens = item.split( "[/]" );
		
		int numTokens = tokens.length;
		
		int level = 0;
		
		int lastLevel = numTokens - 1;
		
		String path = "";
		
		JMenu menu = null;
		
		JMenu parentMenu = null;
		
		//System.err.println( "lastLevel = " + lastLevel );
		
		for( String token : tokens )
		{
			//System.err.println( "level = " + level );
			
			if( level != lastLevel )
			{
				path += token + "/";
				
				menu = getMenu( path );
				
				if( menu == null )
				{
					MenuWrapper mw = new MenuWrapper();
					
					menu = new JMenu( token );
					
					menu.setDelay( 0 );
					
					mw.menu = menu;
					
					mw.path = path;
					
					menusWrappers.add( mw );
					
					if( parentMenu == null )
					{
						menuBar.add( menu );
					}
					else
					{
						parentMenu.add( menu );
					}
				}
				
				parentMenu = menu;
			}
			else
			{
				if( parentMenu != null )
				{
					if( token.equals( "<SEPARATOR>" ) )
					{
						parentMenu.addSeparator();
					}
					else
					{
						MenuItemAction mia = new MenuItemAction( token, path + token );
						
						if( accelerator != null ) mia.setAccelerator( KeyStroke.getKeyStroke( accelerator ) );
						
						parentMenu.add( mia );
					}
				}
			}
			
			level ++;
		}
		
		return false;
	}
	
	
	private JMenu getMenu( String path )
	{
		for( MenuWrapper mw : menusWrappers )
		{
			if( mw.path.equals( path ) )
			{
				return mw.menu;
			}
		}
		
		return null;
	}
	
	
	private static class MenuWrapper
	{
		protected JMenu menu;
		
		protected String path;
	}
	
	
	//private class MenuItemAction extends AbstractAction
	private class MenuItemAction extends JMenuItem implements ActionListener
	{
		private String path;
		
		public MenuItemAction( String name, String path )
		{
			super( name );
			
			this.path = path;
			
			addActionListener( this );
		}
		
		public void actionPerformed( ActionEvent e )
		{
			menuItemSelected( path );
		}
	}
}
