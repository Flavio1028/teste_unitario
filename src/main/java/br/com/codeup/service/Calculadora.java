package br.com.codeup.service;

import br.com.codeup.exceptions.CannotDivideZeroException;

public class Calculadora {

	public int somar(int a, int b) {
		System.out.println("Estou executando o método somar");
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public int divide(int a, int b) throws CannotDivideZeroException {
		if(b == 0) {
			throw new CannotDivideZeroException();
		}
		return a / b;
	}

	public int divide(String a, String b) {
		return Integer.valueOf(a) / Integer.valueOf(b);
	}

	public void imprime(){
		System.out.println("Passei aqui");
	}

	public static void main(String[] args) {
		new Calculadora().divide("a", "b");
	}

}
