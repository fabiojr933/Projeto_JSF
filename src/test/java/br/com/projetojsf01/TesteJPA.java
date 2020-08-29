package br.com.projetojsf01;

import javax.persistence.Persistence;

public class TesteJPA {
	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("projetojsf01");
	}
}
