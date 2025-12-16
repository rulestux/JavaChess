package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

	// códigos de cores extraídos de:
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // acréscimos meus:
    public static final String ANSI_BLINK = "\u001B[5m";
    public static final String ANSI_BOLD = "\u001B[1m";

	// método para limpar a tela com código extraído de:
    // https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

    // método para ler a posição informada pelo jogador:
    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        }
        catch (RuntimeException e) {
            throw new InputMismatchException("Error reading position. Valid values are from a1 to h8.");
        }
    }

    // impressão da jogada, da cor do jogador corrente e de peças capturadas:
    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        // peças capturadas:
        printCapturedPieces(captured);
        System.out.println();

        // barra de jogada (turno):
        System.out.println(ANSI_BOLD + ANSI_CYAN_BACKGROUND + ANSI_WHITE + "     TURN: " + chessMatch.getTurn() + "       " + ANSI_RESET);

        // testar se não há cheque-mate para seguir com a próxima jogada:
        if (!chessMatch.getCheckMate()) {

            // jogador corrente:
            System.out.print("Player: ");
            // cor do jogador corrente:
            if (chessMatch.getCurrentPlayer() == Color.WHITE) {
                System.out.print(ANSI_BLINK + ANSI_WHITE + "🨅 " + ANSI_BOLD + "WHITE" + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_BLINK + ANSI_CYAN + "🨅 " + ANSI_BOLD + "BLACK" + ANSI_RESET);
            }
            System.out.println();

            // notificação de cheque:
            if (chessMatch.getCheck()) {
                System.out.println(ANSI_BOLD + ANSI_BLINK + ANSI_YELLOW + "  󰀦  C H E C K 󰀦 " + ANSI_RESET);
            }

        }
        // fim de jogo e cheque-mate:
        else {
            System.out.println();
            System.out.println(ANSI_BOLD + ANSI_RED_BACKGROUND + ANSI_WHITE + "·                 ·" + ANSI_RESET);
            System.out.println(ANSI_BOLD + ANSI_RED_BACKGROUND + ANSI_WHITE + "    CHECKMATE!     " + ANSI_RESET);
            System.out.println(ANSI_BOLD + ANSI_RED_BACKGROUND + ANSI_WHITE + "·                 ·" + ANSI_RESET);
            System.out.println();
            System.out.println(ANSI_BOLD + ANSI_BLINK + ANSI_RED + "    " + chessMatch.getCurrentPlayer().toString().toUpperCase() + " WON!" + ANSI_RESET);
            System.out.println();
        }
    }

    // impressão do tabuleiro na tela:
    public static void printBoard(ChessPiece[][] pieces) {
        // barra de título:
        System.out.println(ANSI_BOLD + ANSI_YELLOW_BACKGROUND + ANSI_WHITE + "    JAVA CHESS     " + ANSI_RESET);
        // espaço acima do tabuleiro:
        System.out.println();

        for (int i=0; i < pieces.length; i++) {
            // numeração das linhas com espaço antes, para afastar da margem
            // do terminal:
            System.out.print(" " + (8 - i) + " ");
            // peças ou casas nas linhas:
            for (int j=0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], false);
            }
            // quebra para próxima linha:
            System.out.println();
        }
        System.out.println("   a b c d e f g h");

        // espaço abaixo do tabuleiro:
        System.out.println();
    }

    // impressão do tabuleiro com destaque dos movimentos possíveis para
    // uma peça escolhida:
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        // barra de título:
        System.out.println(ANSI_BOLD + ANSI_YELLOW_BACKGROUND + ANSI_WHITE + "    JAVA CHESS     " + ANSI_RESET);
        // espaço acima do tabuleiro:
        System.out.println();

        for (int i=0; i < pieces.length; i++) {
            // numeração das linhas com espaço antes, para afastar da margem
            // do terminal:
            System.out.print(" " + (8 - i) + " ");
            // peças ou casas nas linhas:
            for (int j=0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            // quebra para próxima linha:
            System.out.println();
        }
        System.out.println("   a b c d e f g h");

        // espaço abaixo do tabuleiro:
        System.out.println();
    }

    // impressão de casas e peças:
    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print(ANSI_YELLOW + "■" + ANSI_RESET);
        }
        else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_CYAN + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    // impressão das peças capturadas:
    private static void printCapturedPieces(List<ChessPiece> captured) {
        // expressão lambda para filtrar da lista todos os elementos com o
        // predicado Color.WHITE ou Color.BLACK:
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());
        // imprimindo listas:
        System.out.println(ANSI_BOLD + ANSI_WHITE + ANSI_BLUE_BACKGROUND + "     CAPTURED      " + ANSI_RESET);
        System.out.println(ANSI_WHITE + Arrays.toString(white.toArray()) + ANSI_RESET);
        System.out.println(ANSI_CYAN + Arrays.toString(black.toArray()) + ANSI_RESET);
    }
}
