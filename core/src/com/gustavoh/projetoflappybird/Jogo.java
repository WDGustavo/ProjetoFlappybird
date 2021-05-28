package com.gustavoh.projetoflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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
    Texture gameOver;

	BitmapFont textoPontuacao;
    BitmapFont textoReiniciar;
    BitmapFont textoMelhorPontuacao;

    Sound somVoando;
    Sound somColisao;
    Sound somPontuacao;

    private float larguraDispositivo;
    private float alturaDispositivo;
    private float variacao = 0;
    private float posicaoInicialVerticalPassaro = 0;
    private float posicaoInicialCanoHorizontal;
	private float posicaoInicialCanoVertical;
    private float espacoCano;
    private float posicaoHorzontalPassaro = 0;
    private int pontos = 0;
	private int gravidade = 0;
	private int pontuacaoMaxima;
    private Random numeroRandom;
	private boolean passouCano = false;
    private int estadoJogo = 0;
	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	Preferences preferencias;
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
        espacoCano = 250;
        //*
        //cria as variaveis para fazer a pontuação
        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(10);
        //cria as variaveis para fazer a mensagem de reiniciar aparecer
        textoReiniciar = new BitmapFont();
        textoReiniciar.setColor(Color.RED);
        textoReiniciar.getData().setScale(3);
        //cria as variaveis para fazer a mensagem de melhor pontuação aparecer
        textoMelhorPontuacao = new BitmapFont();
        textoMelhorPontuacao.setColor(Color.ORANGE);
        textoMelhorPontuacao.getData().setScale(3);

        //cria as variaveis para fazer os colider
        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoBaixo = new Rectangle();
        retanguloCanoCima = new Rectangle();
        //cria as variaveis para pegar os sounds
        somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
        somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
        somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
        //variavel para setar as preferencias
        preferencias = Gdx.app.getPreferences("flappyBird");
        //variavel para a pontuação maxima
        pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima",0);
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
        //pega a imagem do GameOver
        gameOver = new Texture("game_over.png");

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
		circuloPassaro.set(50 + passaros[0].getWidth() / 2,
                posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2,
                passaros[0].getWidth() / 2);
        //seta o colider no canobaixo
		retanguloCanoBaixo.set(posicaoInicialCanoHorizontal,
                alturaDispositivo /2 -canoBaixo.getHeight() -espacoCano/2 +posicaoInicialCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight());
        //seta o colider no canotopo
		retanguloCanoCima.set(posicaoInicialCanoHorizontal,
                alturaDispositivo /2 + espacoCano/2 +posicaoInicialCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight());
		//*identifica se o passaro e o cano bateram
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro,retanguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro,retanguloCanoBaixo);
        //*
        //se bateu mostra que bateu no logcat
		if(bateuCanoBaixo || bateuCanoCima){
			if(estadoJogo ==1){
			    //toca o som ao colidir
			    somColisao.play();
			    //muda o estado do jogo para 2
                estadoJogo = 2;
            }

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
    			//toca o som de pontuação
    			somPontuacao.play();
			}
		}
        //faz a velocidade da troca de animação
        variacao += Gdx.graphics.getDeltaTime() * 10;

        //faz a troca de animação
        if (variacao > 3) {
            variacao = 0;
        }

	}

	private void desenhaTextura() {

		batch.begin();
		//coloca o background no app e configura seu tamanho e posição
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

		//coloca o passaro no app e configura sua posição e movimentação
		batch.draw(passaros[(int) variacao], 50 + posicaoHorzontalPassaro, posicaoInicialVerticalPassaro);

		//coloca os canos na tela e configura a altura e largura
		batch.draw(canoBaixo, posicaoInicialCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoCano / 2 + posicaoInicialCanoVertical);
		batch.draw(canoTopo, posicaoInicialCanoHorizontal, alturaDispositivo / 2 + espacoCano/2 + posicaoInicialCanoVertical);

		//coloca a pontuação no centro da tela
		textoPontuacao.draw(batch, String.valueOf(pontos),larguraDispositivo/2,alturaDispositivo-100);
        //caso o estado do jogo seja 2 mostra o gameover e as mensagens para tocar na tela e reiniciar e sua melhor pontuação
		if(estadoJogo ==2) {
		    //coloca a imagem de gameover na tela
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth()/2, alturaDispositivo / 2);
            //coloca na tela a mensagem avisando que pode tocar na tela para reiniciar
            textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR!",
                    larguraDispositivo / 2 -400, alturaDispositivo / 2 - gameOver.getHeight()/2);
            //coloca na tela a mensagem mostrando a melhor pontuação
            textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUAÇÃO É:"+ pontuacaoMaxima+" PONTOS",
                    larguraDispositivo / 2-400, alturaDispositivo / 2-gameOver.getHeight()*2);
        }

        //fecha o que foi colocado para mostrar na tela
		batch.end();

	}

    private void verificaEstadoJogo() {
        //identifica se tocou na tela ou não
        boolean toqueTela = Gdx.input.justTouched();
        //inicio do jogo onde o passaro começa parado e o jogo so começa se ele tocar na tela
        if(estadoJogo == 0){
            //se tocar na tela perde 15 de gravidade
            if (toqueTela) {
                gravidade = -15;
                //muda o estado do jogo para 1
                estadoJogo =1;
                //toca o som de asa
                somVoando.play();
            }
        }
        //estado onde o jogo esta rodando
        else if(estadoJogo ==1){
            //se tocar na tela perde 10 de gravidade
            if (toqueTela) {
                gravidade = -15;
                //toca o som de asa
                somVoando.play();
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

            //Passaro
            //caso a posição for maior que o e seja feito o toque na tela ele pula, isso é feito para o passaro não travar quando chegar na posição 0
            if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
            }

            //faz a gravidade funcionar
            gravidade++;

        }
        //estado do gameover
        else if(estadoJogo ==2){
            //se a pontuação for maior que a pontuação maxima
            if (pontos > pontuacaoMaxima) {
                //pontuação maxima salva o ponto mais alto
                pontuacaoMaxima = pontos;
                //grava a pontuação maxima
                preferencias.putInteger("pontuacaoMaxima",pontuacaoMaxima);
            }
            //jogo o passaro para traz
            posicaoHorzontalPassaro -=Gdx.graphics.getDeltaTime()*500;
            //ao tocar na tela
            if(toqueTela){
                //estado do jogo vai para 0
                estadoJogo =0;
                //zera os pontos
                pontos = 0;
                //zera a gravidade
                gravidade = 0;
                //*sera a posição do passaro e coloca ele no meio da tela
                posicaoHorzontalPassaro=0;
                posicaoInicialVerticalPassaro=alturaDispositivo/2;
                //*
                //coloca o cano no começo da tela
                posicaoInicialCanoHorizontal = larguraDispositivo;

            }

        }

    }

    @Override
    public void dispose() {

    }
}
