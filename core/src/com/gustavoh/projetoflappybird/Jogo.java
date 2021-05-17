package com.gustavoh.projetoflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Jogo extends ApplicationAdapter {
    //*criação das variaveis
    SpriteBatch batch;
    Texture[] passaros;
    Texture fundo;
    Texture canoBaixo;
    Texture canoTopo;
	BitmapFont textoPontuacao;

    private float larguraDispositivo;
    private float alturaDispositivo;
    private float variacao = 0;
    private float posicaoInicialVerticalPassaro = 0;
    private float posicaoInicialCanoHorizontal;
	private float posicaoInicialCanoVertical;
    private float espacoCano;
    private int pontos = 0;
	private int gravidade = 0;

	private boolean passouCano = false;

	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;
    private Random numeroRandom;
    //*

    @Override
    public void create() {


        pegarImagens();
        inicializaObjetos();

    }

    private void inicializaObjetos() {
		batch = new SpriteBatch();
		//cria numero randomico
		numeroRandom = new Random();
        //*configuração de largura, altura e coloca o passo no meio da tela
        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        //posicionamento dos canos na tela
        posicaoInicialVerticalPassaro = alturaDispositivo / 2;
        posicaoInicialCanoHorizontal = larguraDispositivo;
        espacoCano = 150;
        //*
        //cria as variaveis para fazer a pontuação
        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(10);

        //cria as variaveis para fazer os colider
        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoBaixo = new Rectangle();
        retanguloCanoCima = new Rectangle();
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

    }

    @Override
    public void render() {

        desenhaTextura();
        verificaEstadoJogo();
        detectarColisao();
        validaPontos();

    }
	private void detectarColisao() {
        //Seta o colider no passaro
		circuloPassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2,passaros[0].getWidth() / 2);
        //seta o colider no canobaixo
		retanguloCanoBaixo.set(posicaoInicialCanoHorizontal,alturaDispositivo /2 -canoBaixo.getHeight() -espacoCano/2 +posicaoInicialCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());
        //seta o colider no canotopo
		retanguloCanoCima.set(posicaoInicialCanoHorizontal,alturaDispositivo /2 -canoTopo.getHeight() -espacoCano/2 +posicaoInicialCanoVertical, canoTopo.getWidth(), canoTopo.getHeight());
		//*identifica se o passaro e o cano bateram
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro,retanguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro,retanguloCanoBaixo);
        //*
        //se bateu mostra que bateu no logcat
		if(bateuCanoBaixo || bateuCanoCima){
			Gdx.app.log("Log", "Bateu");
		}
	}

	private void validaPontos() {
        //se o passaro passar no meio dos canos faça
    	if(posicaoInicialCanoHorizontal <50 - passaros[0].getWidth()){
    	    //se for true
    		if(!passouCano){
    		    //da ponto
    			pontos++;
    			passouCano = true;
			}
		}

	}

	private void desenhaTextura() {

		batch.begin();
		//coloca o background no app e configura seu tamanho e posição
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

		//coloca o passaro no app e configura sua posição e movimentação
		batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);

		//coloca os canos na tela e configura a altura e largura
		batch.draw(canoBaixo, posicaoInicialCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoCano / 2 + posicaoInicialCanoVertical);
		batch.draw(canoTopo, posicaoInicialCanoHorizontal, alturaDispositivo / 2 + espacoCano + posicaoInicialCanoVertical);
        //coloca a pontuação no centro da tela
		textoPontuacao.draw(batch, String.valueOf(pontos),larguraDispositivo/2,alturaDispositivo-100);
		//fecha o que foi colocado para mostrar na tela
		batch.end();

	}

    private void verificaEstadoJogo() {
        //Passaro
        //identifica se tocou na tela ou não
        boolean toqueTela = Gdx.input.justTouched();
        //se tocar na tela perde 25 de gravidade
        if (Gdx.input.justTouched()) {
            gravidade = -15;
        }
        //caso a posição for maior que o e seja feito o toque na tela ele pula, isso é feito para o passaro não travar quando chegar na posição 0
        if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
        }

        //faz a velocidade da troca de animação
        variacao += Gdx.graphics.getDeltaTime() * 10;

        //faz a troca de animação
        if (variacao > 3) {
            variacao = 0;
        }

        //Canos
		//faz um "loop" para sempre aparecer um obstaculo
		if (posicaoInicialCanoHorizontal < -canoTopo.getWidth()) {
            posicaoInicialCanoHorizontal = larguraDispositivo;
			//faz a troca de posição dos canos (sera retirado quando usar troca de imagens)
			posicaoInicialCanoVertical = numeroRandom.nextInt(400) - 200;
			passouCano = false;
		}

		//movimentação do cano na tela
        posicaoInicialCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;

		//faz a gravidade funcionar
		gravidade++;
    }

    @Override
    public void dispose() {

    }
}
