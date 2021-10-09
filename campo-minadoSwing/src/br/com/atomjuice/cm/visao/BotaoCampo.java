package br.com.atomjuice.cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.atomjuice.cm.modelo.Campo;
import br.com.atomjuice.cm.modelo.CampoEvento;
import br.com.atomjuice.cm.modelo.CampoObservador;


@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener{
	
	private final Color BG_PADRAO = new Color(184,184,184);
	private final Color BG_MARCADO = new Color(8,179,247);
	private final Color BG_EXPLODIR = new Color(189,66,68);
	private final Color TEXTO_VERDE = new Color(0,100,0);

	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		campo.registrarObservador(this);
		addMouseListener(this);
	}
	
	@Override
	public void eventoCampoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
		case ABRIR: 
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
		}
		SwingUtilities.invokeLater(()-> { repaint(); validate();});
		
	}
	
	//Interface dos Eventos do Mouse 
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1){
			campo.abrirCampo();
		}else {
			campo.alternarMarcacao();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){
		if(campo.isAberto()){
			return;
		}
		setBackground(Color.DARK_GRAY);
	}

	@Override
	public void mouseExited(MouseEvent e){
		setBackground(BG_PADRAO);
	}

	//Estilos do Eventos do Botão
	
	private void aplicarEstiloPadrao() {
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		setText("");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("💣");
	}

	private void aplicarEstiloMarcar() {
		if(campo.isAberto()){
			return;
		}
		setBackground(BG_MARCADO);
		setForeground(Color.BLACK);
		setText("🏁");
	
		
	}

	private void aplicarEstiloAbrir() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		if(campo.isMinado()){
			setBackground(BG_EXPLODIR);
			setForeground(Color.BLACK);
			setText("💣");
			return;
		}
		
		setBackground(BG_PADRAO);
		
		switch (campo.minasNaVizinhaca()) {
		case 1:
			setForeground(TEXTO_VERDE); 
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK); 
		}
		
		String valor = !campo.expandAbrir() ? campo.minasNaVizinhaca() + "" : "";
		setText(valor);
	}

	
}
