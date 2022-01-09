package com.raul.demo.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Classe utilitária para trabalhar com recursos da URL
public class URLResource {

	/*Encode é converter uma str q pode conter espaço em branco ou char especial em str com 
	characteres básicos. Este método faz a decodificação*/
	public static String decodeParam(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			return "";
		}
	}	
	
	//Pega uma str e converte para List<Integer>. Recebe uma URL com "1,2,3,4" e retorna [1,2,3,4]
	public static List<Integer> decodeIntList(String str) {
		/*Converte o vetor str.split(",") em lista. A partir da lista, chama o stream().map e converte
		 * cada elemento String em um inteiro*/
		return Arrays.asList(str.split(","))
					.stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}
