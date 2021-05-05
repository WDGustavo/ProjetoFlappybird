package com.gustavoh.projetoflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	//*criação das variaveis
	SpriteBatch batch;
	Texture passaro;
	Texture fundo;

	private float larguraDispositivo;
	private float alturaDispositivo;

	private int movimentaY = 0;
	private int movimentaX = 0;
	//*
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//pega a imagem o background
		fundo = new Texture("fundo.png");
		//pega a imagem do passaro
		passaro = new Texture("passaro1.png");

		//*configuração de largura e altura
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		//*
	}

	@Override
	public void render () {
		batch.begin();
		//coloca o background no app e configura seu tamanho e posição
		batch.draw(fundo,0, 0,larguraDispositivo,alturaDispositivo);
		//coloca o passaro no app e configura sua posição e movimentação
		batch.draw(passaro,movimentaX,movimentaY);

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
