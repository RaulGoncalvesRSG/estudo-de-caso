package com.raul.demo.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.raul.demo.services.exceptions.FileException;

//Serviço responsável por fonercer funcionalidades de imagem 
@Service
public class ImageService {

	//Obtém uma uma imagem JPG a partir do arquivo MultipartFile
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		//Pega a extensão do arquivo MultipartFile. FilenameUtils é da dependência commons-io
		String extesion = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		
		if (!"png".equals(extesion) && !"jpg".equals(extesion)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}
		
		try {
			/*Pega o BufferedImage a partir do MultipartFile. BufferedImage é um tipo de img do Java, 
			ele é no formato JPG*/
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			
			//Se a img for PNG, converte ela em JPG
			if ("png".equals(extesion)) {			
				img = pngToJpg(img);
			}
			return img;				//Retorna a img em JPG
		} 
		catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	//Converte uma imagem com formato PNG para JPG
	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		//Color.WHITE pq alguns PNG possuem o fundo transparente, então dessa forma preenche com branco
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}
	
	/*Retorna um InputStream (obj q encapsula a leitura) a partir de um BufferedImage 
	 * O método q faz upload para o S3 recebe um InputStream, então é preciso obter um InputStream
	 * a partir de BufferedImage */
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} 
		catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}
	
	//Deixa a imagem recortada com formato quadrado
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		//Descobre qual é o valor mínimo, se é a largura ou a altura da imagem
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		//recorta ("cropa") a img
		return Scalr.crop(
			sourceImg, 
			//Metade da largura - metade do mínimo 		//Metade da altura - metade do mínimo 
			(sourceImg.getWidth()/2) - (min/2), (sourceImg.getHeight()/2) - (min/2),
			//Informa qnt quer recortar na largura e na altura
			min, min);		
	}
	
	//Redimensiona a img: recebe uma img e o tamanho q deseja q ela seja recortada
	public BufferedImage resize(BufferedImage sourceImg, int size) {
		//ULTRA_QUALITY para evitar a perca da qualidade ao fazer o redimensionamento
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
