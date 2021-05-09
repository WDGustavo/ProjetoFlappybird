package com.gustavoh.projetoflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	//*criação das variaveis
	SpriteBatch batch;
	Texture[] passaros;
	Texture fundo;

	private float larguraDispositivo;
	private float alturaDispositivo;
	private float variacao= 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro= 0;
	private int movimentaY = 0;
	private int movimentaX = 0;
	//*
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//pega a imagem o background
		fundo = new Texture("fundo.png");
		//tamanho da lista
		passaros = new Texture[3];
		//pega a imagem do passaro para fazer a troca de sprites para a animação
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		//*configuração de largura, altura e coloca o passo no meio da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo/2;
		//*
	}

	@Override
	public void render () {
		batch.begin();
		//faz a troca de animação
		if(variacao >3){
			variacao = 0;
		}
		//identifica se tocou na tela ou não
		boolean toqueTela = Gdx.input.justTouched();
		//se tocar na tela perde 25 de gravidade
		if(Gdx.input.justTouched()){
			gravidade = -25;
		}
		//caso a posição for maior que o e seja feito o toque na tela ele pula, isso é feito para o passaro não travar quando chegar na posição 0
		if(posicaoInicialVerticalPassaro>0||toqueTela){
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}
		//coloca o background no app e configura seu tamanho e posição
		batch.draw(fundo,0, 0,larguraDispositivo,alturaDispositivo);
		//coloca o passaro no app e configura sua posição e movimentação
		batch.draw(passaros[(int) variacao],30,	posicaoInicialVerticalPassaro);
		//faz a velocidade da troca de animação
		variacao += Gdx.graphics.getDeltaTime() *10;

		//faz a gravidade funcionar
		gravidade++;
		//faz a movimentação dos eixos
		movimentaY++;
		movimentaX++;
		//fecha o que foi colocado para mostrar na tela
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
