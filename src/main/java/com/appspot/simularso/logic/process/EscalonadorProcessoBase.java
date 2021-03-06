package com.appspot.simularso.logic.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.appspot.simularso.model.Processo;
import com.appspot.simularso.model.ProcessoDTO;
import com.appspot.simularso.model.ProcessoVO;

public abstract class EscalonadorProcessoBase {

	private List<ProcessoVO> resultado;
	private List<ProcessoDTO> resultadoGrafico;
	private int tempoTotal;
	private int totalProcessos;
	private int tempoQuantum;
	private int tempoContexto;
	protected ArrayList<Processo> processos;

	public EscalonadorProcessoBase(ArrayList<Processo> processos, int tempoQuantum, int tempoContexto) {
		this.processos = processos;
		this.totalProcessos = processos.size();
		this.tempoContexto = tempoContexto;
		this.tempoQuantum = tempoQuantum;
		this.resultado = new LinkedList<ProcessoVO>();
		this.resultadoGrafico = new LinkedList<ProcessoDTO>();
	}

	protected void utilizarBurstOrder(boolean burstOrder) {
		for (Processo processo : processos) {
			processo.setBurstOrder(burstOrder);
		}
	}

	protected int tempoContexto() {
		return tempoContexto;
	}

	protected int tempoQuantum() {
		return tempoQuantum;
	}

	protected int tempoTotal() {
		return tempoTotal;
	}

	protected void atualizarTempoTotal(int burst) {
		this.tempoTotal += burst;
	}

	protected double calcularEsperaMedia() {
		int acumulado = 0;
		for (ProcessoVO proc : resultado) {
			acumulado += proc.getEspera();
		}
		return (acumulado / resultado.size());
	}

	protected double calcularRespostaMedia() {
		int acumulado = 0;
		for (ProcessoVO proc : resultado) {
			acumulado += proc.getResposta();
		}
		return (acumulado / resultado.size());
	}

	protected double calcularTurnAroundMedio() {
		int acumulado = 0;
		for (ProcessoVO proc : resultado) {
			acumulado += proc.getTurnAround();
		}
		return (acumulado / resultado.size());
	}

	protected int totalProcessosExecutados() {
		return totalProcessos;
	}

	protected int totalDeProcessos() {
		return processos.size();
	}

	protected void ordernarProcessos() {
		Collections.sort(processos);
	}

	protected Processo clonarProcesso(int index) {
		if (index >= processos.size()) {
			return null;
		}
		return processos.get(index).clone();
	}

	protected void atualizarProcesso(int index, Processo processo) {
		if (processo.isFirstRun()) {
			processo.setFirstRun(false);
		}
		processos.set(index, processo);
	}

	protected void atualizarProcesso(Processo processo) {
		if (processo.isFirstRun()) {
			processo.setFirstRun(false);
		}
		int index = 0;
		for (int i = 0; i < processos.size(); i++) {
			if (processo.getBurstTotal() > processos.get(i).getBurstTotal()) {
				index = i;
			}
		}
		processos.add(++index, processo);
	}

	protected void removerProcesso(int index) {
		processos.remove(index);
	}

	protected int recuperarIndex(Processo processo) {
		return processos.indexOf(processo);
	}

	protected void adicionarResultadoFinal(Processo processo) {
		int id = processo.getId();
		int burst = processo.getBurst();
		int espera = processo.getEspera();
		int resposta = processo.getResposta();
		int turnAround = processo.getTurnAround();
		String cor = processo.getCor();
		resultado.add(new ProcessoVO(id, burst, espera, resposta, turnAround, cor));
	}

	protected void adicionarResultadoGrafico(Processo processo) {
		int id = processo.getId();
		long x = tempoTotal;
		long y = id;
		long w = processo.getBurstAtual();
		long h = 1;
		String cor = processo.getCor();
		resultadoGrafico.add(new ProcessoDTO(id, x, y, w, h, cor));
	}

	protected List<ProcessoVO> resultado() {
		Collections.sort(resultado);
		return resultado;
	}

	protected List<ProcessoDTO> resultadoGrafico() {
		return resultadoGrafico;
	}

}