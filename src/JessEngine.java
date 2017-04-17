import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;

import jess.JessException;
import jess.Rete;
import jess.Value;
import jess.awt.ActionListener;
import jess.awt.TextAreaWriter;
import jess.awt.TextReader;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;


public class JessEngine implements org.nlogo.api.Reporter{

	private Rete engine = null;
	private JessConsole console;
	public JessEngine() {
		super();
		if (engine == null) {
			engine = new Rete();

			try {
				engine.reset();
				console = new JessConsole(engine);
				console.setVisible(true);
			} catch (JessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public Syntax getSyntax() {
//		return Syntax.reporterSyntax(new int [] {Syntax.StringType()},Syntax.StringType());
		return SyntaxJ.reporterSyntax(new int [] {Syntax.StringType()},Syntax.WildcardType());
	}


	public Object report(Argument[] arg0, Context arg1)
			throws ExtensionException, LogoException {
		// TODO Auto-generated method stub
		String command = arg0[0].getString();
//		String result = "";
		Value result = null;

		if (command != null)
			try {
//				result = engine.eval(command).toString();
				result = engine.eval(command);
				console.setVisible(true);
			} catch (JessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return result.toString();
	}



	class JessConsolePanel extends Panel implements Serializable  {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;



		// Members used for presenting output
		private TextAreaWriter m_taw;

		// Members used for getting input
		private TextField m_tf;

		// TextReader is in the jess package
		private TextReader m_in;

		private Rete r;

		private ArrayList<String> commandBuffer;
		private int commandIndex;


		private  String getStackTrace(final Throwable throwable) {
		     final StringWriter sw = new StringWriter();
		     final PrintWriter pw = new PrintWriter(sw, true);
		     throwable.printStackTrace(pw);
		     String s = sw.getBuffer().toString();
		     return s.substring(0, s.indexOf("at jess")-1)+"\r\n";
		}

		public JessConsolePanel(Rete rete)
		{
			this.r = rete;
			// Set up the GUI elements
			TextArea ta = new TextArea(10, 40);
			m_tf = new TextField(50);
			ta.setEditable(false);
			Panel p = new Panel();
			p.setLayout(new BorderLayout());

			// arrange buttons   
			Button bClear = new Button("Clear Window");   

			// Set up I/O streams
			m_taw = new TextAreaWriter(ta);
			m_in = new TextReader(false);
			commandBuffer = new ArrayList<String>();
			commandIndex = 0;

			// Assemble the GUI
			setLayout(new BorderLayout());
			add("Center", ta);
			p.add("Center", m_tf);
			p.add("East", bClear);
			add("South", p);

			m_tf.addKeyListener(new KeyListener(){

				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						m_tf.setText(commandBuffer.get(commandIndex));
						if (commandIndex > 0) commandIndex--;

					}
					else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (commandIndex < commandBuffer.size()-1) commandIndex++;
						m_tf.setText(commandBuffer.get(commandIndex));


					}

				}

				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub

				}

				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub

				}

			});

			try {
				m_tf.addActionListener(new ActionListener("", rete) {
					public void actionPerformed(ActionEvent ae)
					{
						//synchronized (ta)
					Value result;					 
						{
							try
							{
								String command = m_tf.getText();
								m_taw.write("Jess> " + command + "\r\n");
								commandBuffer.add(command);
								commandIndex = commandBuffer.size()-1;
								m_taw.write(r.eval(command).toString()+"\r\n");
								m_taw.flush();
							
							}
							catch (Exception e)
							{
								try {
									m_taw.write(getStackTrace(e));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								m_taw.flush();
								e.printStackTrace();
							}
						}

						m_in.appendText(m_tf.getText() + "\n");
						m_tf.setText("");
					}
				});
			} catch (JessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				bClear.addActionListener(new ActionListener("hy", rete) {
					public void actionPerformed(ActionEvent ae)
					{
						m_taw.clear();
						m_tf.setText("");
					}
				});
			} catch (JessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PrintWriter pw = new PrintWriter(m_taw, true);
			r.addInputRouter("t", m_in, true);
			r.addOutputRouter("t", pw);
			r.addInputRouter("WSTDIN", m_in, true);
			r.addOutputRouter("WSTDOUT", m_taw);
			r.addOutputRouter("WSTDERR", m_taw);
		}

		/**
		 * Move focus to the input area. Helps to call this whenever a button is clicked, etc.
		 */

		final public void setFocus() { m_tf.requestFocus(); }


	}

	public class JessConsole extends Frame implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JessConsolePanel m_panel;
		Rete m_rete;

		/**
		 * Create a Console. This constructor creates a new Rete object, which you can
		/**
		 * Create a Console, using a prexisting Rete object.
		 * @param title The title for the Frame.
		 * @param r A prexisting Rete object.
		 */

		public JessConsole(Rete rete) {

			m_rete = rete;
			m_panel = new JessConsolePanel(rete);

			add("Center", m_panel);

			validate();
			setSize(500, 300);
			this.setVisible(true);
		}

	}

}

