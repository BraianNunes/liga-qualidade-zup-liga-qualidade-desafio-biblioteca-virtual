package br.com.zup.edu.ligaqualidade.desafiobiblioteca.modifique;

import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosDevolucao;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosEmprestimo;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.EmprestimoConcedido;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Solucao {

	/**
	 * Você precisa implementar o código para executar o fluxo
	 * o completo de empréstimo e devoluções a partir dos dados
	 * que chegam como argumento. 
	 * 
	 * Caso você queira pode adicionar coisas nas classes que já existem,
	 * mas não pode alterar nada.
	 */
	
	/**
	 * 
	 * @param livros dados necessários dos livros
	 * @param exemplares tipos de exemplares para cada livro
	 * @param usuarios tipos de usuarios
	 * @param emprestimos informações de pedidos de empréstimos
	 * @param devolucoes informações de devoluções, caso exista. 
	 * @param dataParaSerConsideradaNaExpiracao aqui é a data que deve ser utilizada para verificar expiração
	 * @return
	 */
	public static Set<EmprestimoConcedido> executa(Set<DadosLivro> livros,
			Set<DadosExemplar> exemplares,
			Set<DadosUsuario> usuarios, Set<DadosEmprestimo> emprestimos,
			Set<DadosDevolucao> devolucoes, LocalDate dataParaSerConsideradaNaExpiracao) {

		Set<EmprestimoConcedido> emprestimoConcedidos = new HashSet<>();

		for (DadosEmprestimo emprestimo : emprestimos) {
			DadosExemplar dadosExemplar = obterExemplar(exemplares, emprestimo.idLivro);
			LocalDate tempoEmprestimo = LocalDate.now().plusDays(emprestimo.tempo);

			// Restrição 6 - Só pesquisadores podem pedir empréstimos de exemplares restritos
			if (emprestimo.tipoExemplar == TipoExemplar.RESTRITO) {
				DadosUsuario dadosUsuario = obterUsuario(usuarios, emprestimo.idUsuario);

				if (dadosUsuario.padrao == TipoUsuario.PADRAO) {
					continue;
				}
			}

			// Restrição 2 - Todo pedido de empréstimo tem o limite de 60 dias a partir do momento do pedido
			if (emprestimo.tempo > 60) {
				continue;
			}

			emprestimoConcedidos.add(
							new EmprestimoConcedido(
											emprestimo.idUsuario,
											dadosExemplar.idExemplar,
											tempoEmprestimo
											)
			);
		}

		return emprestimoConcedidos;
	}

	private static DadosExemplar obterExemplar(Set<DadosExemplar> exemplares, int idLivro) {
		return exemplares.stream()
						.filter(e -> e.idLivro == idLivro)
						.findFirst().get();
	}

	private static DadosUsuario obterUsuario(Set<DadosUsuario> dadosUsuarios, int idUsuario) {
		return dadosUsuarios.stream()
						.filter(e -> e.idUsuario == idUsuario)
						.findFirst().get();
	}

}
