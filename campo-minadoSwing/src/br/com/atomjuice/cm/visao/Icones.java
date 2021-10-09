package br.com.atomjuice.cm.visao;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Icones extends JButton{	

	private JLabel imag;

	public Icones() { criaImag(); }

	private void criaImag() { ImageIcon imagem = new ImageIcon(getClass().getResource("laMarcado.jpg"));

	final JLabel label = new JLabel(imagem); this.setImag(label); }

	public JLabel getImag() { return imag; }

	public void setImag(JLabel imag) { this.imag = imag; }
	
}

