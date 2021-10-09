package br.com.atomjuice.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
	
	private final int qtdLinhas;
	private final int qtdColunas;
	private int qtdMinas;

	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	
	public Tabuleiro(int qtdLinhas, int qtdColunas, int qtdMinas) {
		super();
		this.qtdLinhas = qtdLinhas;
		this.qtdColunas = qtdColunas;
		this.qtdMinas = qtdMinas;
		
		gerarCampos();
		associarOsVizinhos();
		generateMinas();
	}
	
	
	public int getQtdLinhas() {
		return qtdLinhas;
	}

	public int getQtdColunas() {
		return qtdColunas;
	}
	
	public void abrir(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrirCampo());				
	}
	
	public void alterarMarcacao(int linha, int coluna){
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.alternarMarcacao());;
	}
	
	public boolean objetivoAlcancado(){
		return campos.stream().allMatch(c->c.objetivoAlcancado());
	}
	
	public void reiniciar(){
		campos.stream().forEach(c-> c.reiniciar());
		generateMinas();
	}
	
	@Override
	public void eventoCampoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR){
			mostrarMinas();
			notificarObservadores(false);
		}else if(objetivoAlcancado()){
			notificarObservadores(true);
		}
	}
	
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	public void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}
	
	public void paraCadaCampo(Consumer<Campo> funcao){
		campos.forEach(funcao);
	}

	private void gerarCampos() {
		for (int linhas = 0; linhas < qtdLinhas;  linhas++) {
			for (int colunas = 0; colunas < qtdColunas; colunas++) {
				Campo campo = new Campo(linhas, colunas);
				campo.registrarObservador(this);
				campos.add(campo);
			
			}
		}
	}

	private void associarOsVizinhos() {
		for(Campo c1: campos) {
			for(Campo c2: campos){
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void generateMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {
			//Cast tem prioridade sobre a multiplicacao, como o Math.random() gera um valor entre 0 e 1, o cast transforma esse valor para 0. Por isso realizamos a multiplicacao primeiro
			int aleatorio = (int) (Math.random() * campos.size());
			
			campos.get(aleatorio).minarCampo();		
			minasArmadas = campos.stream().filter(minado).count();
		
		} while (minasArmadas < qtdMinas);
	}
	
	private void mostrarMinas() {
		campos.stream().filter(c -> c.isMinado()).filter(c -> !c.isMarcado()).forEach(c -> c.setAberto(true));
	}
	
}
