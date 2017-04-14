package com.edm.utils.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.edm.utils.consts.Value;

public class Kaptchas {
	
	private static int WIDTH = 60;
	private static int HEIGHT = 30;
	private static int LENGTH = 4;
	private final static Random random = new Random();

	public static String generate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String code = code(request);
		response.setDateHeader("Expires", Value.L);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/png");
		
		render(code, response.getOutputStream(), WIDTH, HEIGHT);
		
		return code;
	}
	
	public static boolean validate(String key, String kaptcha) {
		return StringUtils.equalsIgnoreCase(key, kaptcha);
	}

	private static String code(HttpServletRequest request) {
		String code = RandomStringUtils.randomAlphanumeric(LENGTH).toUpperCase();
		code.replace('0', 'W');
		code.replace('o', 'R');
		code.replace('I', 'E');
		code.replace('1', 'T');
		return code;
	}

	private static void render(String text, OutputStream out, int width, int height) throws IOException {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();

		bi = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = bi.createGraphics();

		g.fillRect(0, 0, width, height);
		for (int i = 0; i < 8; i++) {
			g.setColor(color(150, 250));
			g.drawOval(random.nextInt(56), random.nextInt(18), 5 + random.nextInt(10), 5 + random.nextInt(10));
		}
		Font mFont = new Font("Arial", Font.ITALIC, 18);
		g.setFont(mFont);
		g.setColor(color(10, 240));
		g.drawString(text, 3, 24);
		ImageIO.write(bi, "png", out);
	}

	private static Color color(int fc, int bc) {
		fc = fc > 255 ? 255 : fc;
		bc = bc > 255 ? 255 : bc;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static void main(String[] args) throws IOException {
		String code = RandomStringUtils.randomAlphanumeric(LENGTH).toUpperCase();
		
		code = code.replace('0', 'W');
		code = code.replace('o', 'R');
		code = code.replace('I', 'E');
		code = code.replace('1', 'T');

		FileOutputStream out = new FileOutputStream("d:\\ct.jpg");
		render(code, out, 60, 30);
	}
}
