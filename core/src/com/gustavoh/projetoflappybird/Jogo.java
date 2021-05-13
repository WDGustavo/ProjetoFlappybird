package com.gustavoh.projetoflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Jogo extends ApplicationAdapter {
	//*criação das variaveis
	SpriteBatch batch;
	Texture[] passaros;
	Texture fundo;
	Texture canoBaixo;
	Texture canoTopo;

	private float larguraDispositivo;
	private float alturaDispositivo;
	private float variacao= 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro= 0;
	private float posicaoInicialCano;
	private float espacoCano;
	private int movimentaY = 0;
	private int movimentaX = 0;

	//variavel criada para um teste para fazer os canos aparecerem em posições diferentes sem usar as outras imagens(sera retirado quando usar troca de imagens)
	private float alturaCanoRandom;
	private Random numeroRandom;
	//*
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//cria numero randomico(sera retirado quando usar troca de imagens)
		numeroRandom = new Random();
		pegarImagens();
		configuraPosicaoLarguraeAltura();

	}

	private void configuraPosicaoLarguraeAltura() {
		//*configuração de largura, altura e coloca o passo no meio da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		//posicionamento dos canos na tela
		posicaoInicialVerticalPassaro = alturaDispositivo/2;
		posicaoInicialCano = larguraDispositivo;
		espacoCano = 150;
		//*
	}

	private void pegarImagens() {
		//pega a imagem o background
		fundo = new Texture("fundo.png");
		//tamanho da lista
		passaros = new Texture[3];
		//pega a imagem do passaro para fazer a troca de sprites para a animação
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//pega a imagem do cano
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");

		//*lista criado para pooder randomizar a posição dos canos (ainda estou tentando fazer funcionar)
		//canoBaixo = new Texture[2];
		//canoBaixo[0] = new Texture("cano_baixo.png");
		//canoBaixo[1] = new Texture("cano_baixo_maior.png");

		//canoTopo = new Texture[2];
		//canoTopo[0] = new Texture("cano_topo_maior.png");
		//canoTopo[1] = new Texture("cano_topo_maior.png");
		//*
	}

	@Override
	public void render () {
		batch.begin();
		//coloca o background no app e configura seu tamanho e posição
		batch.draw(fundo,0, 0,larguraDispositivo,alturaDispositivo);
		configuracaoPassaro();
		configuracaoCanos();
		constantes();

		//fecha o que foi colocado para mostrar na tela
		batch.end();
	}

	private void constantes() {
		//faz a gravidade funcionar
		gravidade++;
		//faz a movimentação dos eixos
		movimentaY++;
		movimentaX++;
	}

	private void configuracaoPassaro() {
		//faz a troca de animação
		if(variacao >3){
			variacao = 0;
		}
		//identifica se tocou na tela ou não
		boolean toqueTela = Gdx.input.justTouched();
		//se tocar na tela perde 25 de gravidade
		if(Gdx.input.justTouched()){
			gravidade = -15;
		}
		//caso a posição for maior que o e seja feito o toque na tela ele pula, isso é feito para o passaro não travar quando chegar na posição 0
		if(posicaoInicialVerticalPassaro>0||toqueTela){
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}
		//coloca o passaro no app e configura sua posição e movimentação
		batch.draw(passaros[(int) variacao],30,	posicaoInicialVerticalPassaro);
		//faz a velocidade da troca de animação
		variacao += Gdx.graphics.getDeltaTime() *10;
	}

	private void configuracaoCanos() {
		//faz um "loop" para sempre aparecer um obstaculo
		if(posicaoInicialCano <-canoTopo.getWidth()){
			posicaoInicialCano = larguraDispositivo;
			//faz a troca de posição dos canos (sera retirado quando usar troca de imagens)
			alturaCanoRandom = numeroRandom.nextInt(400)-200;
		}
		//coloca os canos na tela e configura a altura e largura
		batch.draw(canoBaixo,posicaoInicialCano,alturaDispositivo/2 - canoBaixo.getHeight() - espacoCano/2 + alturaCanoRandom);
		batch.draw(canoTopo,posicaoInicialCano,alturaDispositivo/2 + espacoCano + alturaCanoRandom);
		//movimentação do cano na tela
		posicaoInicialCano -= Gdx.graphics.getDeltaTime()*200;
	}

	@Override
	public void dispose () {

	}
}
