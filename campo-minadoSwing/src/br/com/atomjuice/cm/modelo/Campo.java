package br.com.atomjuice.cm.modelo;

import java.util.ArrayList;
import java.util.List;


//TODO adicionar cobertura de pelo menos 80%
public class Campo {

	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();
	
	
	Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}
	
	
	public boolean isMarcado(){
		return marcado;
	}

	public boolean isAberto(){
		return aberto;
	}
	
	public boolean isFechado(){
		return !isAberto();
	}

	public boolean isMinado()	{
		return minado;
	}	
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	public boolean abrirCampo(){
		if(!aberto && !marcado) {
			if(minado){
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			if(expandAbrir()){
				vizinhos.forEach(v -> v.abrirCampo());
			}
			
			return true;
		}
		
		return false;
	}

	public void alternarMarcacao(){
		if(!aberto){
			marcado = !marcado;
		}
		if(marcado){
			notificarObservadores(CampoEvento.MARCAR);
		}else{
			notificarObservadores(CampoEvento.DESMARCAR);
		}
	}
	
	public int minasNaVizinhaca()	{
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	public void registrarObservador(CampoObservador observador){
		observadores.add(observador);
	}
	
	public boolean expandAbrir(){
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	private void notificarObservadores(CampoEvento evento){
		//Chama o metodo sempre que quiser notificar que o evento ocorreu
		observadores.stream().forEach(o -> o.eventoCampoOcorreu(this, evento));
	}
	
	boolean adicionarVizinho(Campo possivelVizinho){
		boolean linhaDiferente = linha != possivelVizinho.linha;
		boolean colunaDiferente = coluna != possivelVizinho.coluna;
		boolean isDiagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - possivelVizinho.linha);
		int deltaColuna = Math.abs(coluna - possivelVizinho.coluna);
		
		int totalDelta = deltaColuna + deltaLinha;
		
		if(totalDelta == 1 && !isDiagonal){
			vizinhos.add(possivelVizinho);
			return true;
		}else if(totalDelta == 2 && isDiagonal){
			vizinhos.add(possivelVizinho);
			return true;
		}else{
			return false;
		}
	}

	void minarCampo(){
		minado = true;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if(aberto){
			notificarObservadores(CampoEvento.ABRIR);
		}
	}
	
	boolean objetivoAlcancado(){
		boolean desvendado = !minado && aberto;
		boolean protegido = marcado && minado;
		return desvendado || protegido;
	}
	
	void reiniciar(){
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	
	}

}


