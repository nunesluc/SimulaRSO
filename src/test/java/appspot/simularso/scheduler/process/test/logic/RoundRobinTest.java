package appspot.simularso.scheduler.process.test.logic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import appspot.simularso.exception.ProcessosNaoCarregadosException;
import appspot.simularso.exception.TempoQuantumException;
import appspot.simularso.model.Processo;
import appspot.simularso.scheduler.process.logic.Escalonador;
import appspot.simularso.scheduler.process.logic.impl.RoundRobin;
import appspot.simularso.scheduler.process.test.InitialCase;

public class RoundRobinTest extends InitialCase {

	@Test
	public void deveRetornarResultadoSimples() {
		final Integer[] BURSTS_SIMPLES = { 60, 20, 40, 30 };
		final Integer[] ID_COM_BURSTS_SIMPLES = { 1, 2, 3, 4, 1, 3, 4, 1 };
		final Integer[] TEMPO_RESPOSTA_COM_BURSTS_SIMPLES = { 0, 20, 40, 60 };
		final Integer[] TEMPO_ESPERA_COM_BURSTS_SIMPLES = { 90, 20, 80, 100 };
		final Integer[] TURN_AROUND_COM_BURSTS_SIMPLES = { 150, 40, 120, 130 };
		final int QUANTUM = 20;

		Escalonador roundRobin = new RoundRobin(gerarArrayListDeProcessos(BURSTS_SIMPLES.length, BURSTS_SIMPLES, null, null), QUANTUM);

		LinkedList<Processo> resultadoGrafico = roundRobin.resultadoGraficoFinal();
		Assert.assertThat(resultadoGrafico, Matchers.notNullValue());
		Assert.assertTrue(resultadoGrafico.size() == ID_COM_BURSTS_SIMPLES.length);
		for (int i = 0; i < resultadoGrafico.size(); i++) {
			Assert.assertThat(resultadoGrafico.get(i).getId(), Matchers.equalTo(ID_COM_BURSTS_SIMPLES[i]));
		}

		Set<Processo> resultado = roundRobin.resultadoFinal();
		Assert.assertThat(resultado, Matchers.notNullValue());
		Assert.assertTrue(resultado.size() == BURSTS_SIMPLES.length);
		Iterator<Processo> resultSet = resultado.iterator();
		while (resultSet.hasNext()) {
			Processo proc = resultSet.next();
			Assert.assertThat(ID_COM_BURSTS_SIMPLES, Matchers.hasItemInArray(proc.getId()));
			Assert.assertThat(TEMPO_ESPERA_COM_BURSTS_SIMPLES, Matchers.hasItemInArray(proc.getEspera()));
			Assert.assertThat(TEMPO_RESPOSTA_COM_BURSTS_SIMPLES, Matchers.hasItemInArray(proc.getResposta()));
			Assert.assertThat(TURN_AROUND_COM_BURSTS_SIMPLES, Matchers.hasItemInArray(proc.getTurnAround()));
		}

		double esperaMedia = roundRobin.tempoEsperaMedia();
		double respostaMedia = roundRobin.tempoRespostaMedia();
		double turnAroundMedio = roundRobin.tempoTurnAroundMedio();
		Assert.assertTrue(turnAroundMedio > 0.0);
		Assert.assertTrue(respostaMedia > 0.0);
		Assert.assertTrue(esperaMedia > 0.0);
		Assert.assertTrue(roundRobin.totalProcessos() == BURSTS_SIMPLES.length);
	}

	@Test
	public void deveRetornarResultadoMedio() {
		final Integer[] BURSTS_MEDIO = { 53, 17, 68, 24 };
		final Integer[] ID_COM_BURSTS_MEDIO = { 1, 2, 3, 4, 1, 3, 4, 1, 3, 3 };
		final Integer[] TEMPO_RESPOSTA_COM_BURSTS_MEDIO = { 0, 20, 37, 57 };
		final Integer[] TEMPO_ESPERA_COM_BURSTS_MEDIO = { 81, 20, 94, 97 };
		final Integer[] TURN_AROUND_COM_BURSTS_MEDIO = { 134, 37, 162, 121 };
		final int QUANTUM = 20;

		Escalonador roundRobin = new RoundRobin(gerarArrayListDeProcessos(BURSTS_MEDIO.length, BURSTS_MEDIO, null, null), QUANTUM);

		LinkedList<Processo> resultadoGrafico = roundRobin.resultadoGraficoFinal();
		Assert.assertThat(resultadoGrafico, Matchers.notNullValue());
		Assert.assertThat(resultadoGrafico.size(), Matchers.is(ID_COM_BURSTS_MEDIO.length));
		for (int i = 0; i < resultadoGrafico.size(); i++) {
			Assert.assertThat(resultadoGrafico.get(i).getId(), Matchers.equalTo(ID_COM_BURSTS_MEDIO[i]));
		}

		Set<Processo> resultado = roundRobin.resultadoFinal();
		Assert.assertThat(resultado, Matchers.notNullValue());
		Assert.assertThat(resultado.size(), Matchers.is(BURSTS_MEDIO.length));
		Iterator<Processo> resultSet = resultado.iterator();
		while (resultSet.hasNext()) {
			Processo proc = resultSet.next();
			Assert.assertThat(ID_COM_BURSTS_MEDIO, Matchers.hasItemInArray(proc.getId()));
			Assert.assertThat(TEMPO_ESPERA_COM_BURSTS_MEDIO, Matchers.hasItemInArray(proc.getEspera()));
			Assert.assertThat(TEMPO_RESPOSTA_COM_BURSTS_MEDIO, Matchers.hasItemInArray(proc.getResposta()));
			Assert.assertThat(TURN_AROUND_COM_BURSTS_MEDIO, Matchers.hasItemInArray(proc.getTurnAround()));
		}

		double esperaMedia = roundRobin.tempoEsperaMedia();
		double respostaMedia = roundRobin.tempoRespostaMedia();
		double turnAroundMedio = roundRobin.tempoTurnAroundMedio();
		Assert.assertTrue(turnAroundMedio > 0.0);
		Assert.assertTrue(respostaMedia > 0.0);
		Assert.assertTrue(esperaMedia > 0.0);
		Assert.assertTrue(roundRobin.totalProcessos() == BURSTS_MEDIO.length);
	}

	@Test
	public void deveEscalonarComDoisACemProcessos() {
		final int QUANTUM_VALIDO = 20;
		for (int i = 2; i <= 100; i++) {
			Escalonador roundRobin = new RoundRobin(gerarListaDeProcessos(i, VALIDO), QUANTUM_VALIDO);
			Set<Processo> resultado = roundRobin.resultadoFinal();
			Assert.assertThat(resultado, Matchers.notNullValue());
		}
	}

	@Test
	public void deveRetornarResultadoFinalOrdernadoPorProcessoId() {
		final int TOTAL = 10;
		final int QUANTUM_VALIDO = 50;
		Escalonador roundRobin = new RoundRobin(gerarListaDeProcessos(TOTAL, VALIDO), QUANTUM_VALIDO);
		Set<Processo> resultado = roundRobin.resultadoFinal();
		Iterator<Processo> result = resultado.iterator();
		int id = 1;
		while (result.hasNext()) {
			Integer procId = result.next().getId();
			Assert.assertTrue(id == procId);
			id++;
		}
	}

	@Test(expected = TempoQuantumException.class)
	public void naoDeveEscalonarProcessosComTempoQuantumNegativo() {
		final int QUANTUM_INVALIDO = 0;
		new RoundRobin(gerarListaDeProcessos(3, VALIDO), QUANTUM_INVALIDO);
	}

	@Test(expected = ProcessosNaoCarregadosException.class)
	public void naoDeveEscalonarProcessosComBurstNegativo() {
		final int QUANTUM_VALIDO = 20;
		new RoundRobin(gerarListaDeProcessos(3, INVALIDO), QUANTUM_VALIDO);
	}

	@Test(expected = ProcessosNaoCarregadosException.class)
	public void naoDeveEscalonarSemAntesCarregarOsProcessos() {
		final int QUANTUM_VALIDO = 20;
		new RoundRobin(null, QUANTUM_VALIDO);
	}
}