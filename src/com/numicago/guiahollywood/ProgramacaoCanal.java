/*
* * Figueir� dos Vinhos 
 * Data: 2010-10-14
 * Autor: Nuno Gon�alves
 */
package com.numicago.guiahollywood;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.EnumCountryCanal;

/**
 * Classe que vai buscar toda a informa��o ao conte�do do site, e retorna uma lista de instancias de Filme.
 * Todas as caracteristicas que possam ser retiradas dos filmes s�o feitas aqui. Exceptua-se a informa��o de 
 * Imagens grandes, e descri��o, porque isso s� pode ser feito com novo acesso � internet atrav�s do link do filme.
 * @author Para
 *
 */
public class ProgramacaoCanal {
	private HtmlHelper hh;
	private List<DayMovie> listaFilmesDoDia;
	
	public ProgramacaoCanal() {}

	/**
	 * Construtor que cria uma lista de filmes. 
	 * @param site
	 */
	public ProgramacaoCanal(final String site, final int canalEmExibicao) {
		try {
			listaFilmesDoDia = new ArrayList<DayMovie>();
			hh = new HtmlHelper(new URL(site)) { 
				//M�todo respons�vel por carregar toda a informa��o do site para as instancias de Filme.			
				public void getContents() {
					Log.d("ProgramacaoCanal", site);
					if(canalEmExibicao == EnumCountryCanal.PORTUGAL.getId())
						listaFilmesDoDia = ProgramacaoPortugal.getProgramacaoPortugal(rootNode, site);
					else
						listaFilmesDoDia = ProgramacaoEspanha.getProgramacaoEspanha(rootNode, site);
				}
			};
			hh.getContents();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retornar a programa��o do dia
	 * @return
	 */
	public List<DayMovie> getProgramacaoDoDia() {
		return listaFilmesDoDia;
	}
	
	public static void setEndTimes(List<DayMovie> movieList, int ano, int mes, int dia) {
		for (int i = 0; i < movieList.size(); i++) {
			NumiCal end = new NumiCal(ano, mes, dia);
			//if index is the last one add two hours to the start date.
			if (i == movieList.size() - 1) {
				end.setCalendar(movieList.get(i).getDay().getStart());
				end.addHours(2);
				movieList.get(i).getDay().setEnd(end);
				
			} else { //else put the end time of the current movie = next movie start time
				end.setTime(movieList.get(i+1).getDay().getStart().getTime());
				movieList.get(i).getDay().setEnd(end);
			}
		}
	}
}
