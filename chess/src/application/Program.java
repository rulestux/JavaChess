package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured =  new ArrayList<>();

        while (!chessMatch.getCheckMate()) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                UI.printMatch(chessMatch, captured);

                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                // exibindo movimentos possíveis:
                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);
                UI.printMatch(chessMatch, captured);

                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                // movimento com captura de peças:
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
                // testando se existe peça capturada e incluindo-a na lista captured:
                if (capturedPiece != null) {
                    captured.add(capturedPiece);
                }

                // testando peça promovida na jogada, para solicitar a escolha:
                if (chessMatch.getPromoted() != null) {
                    System.out.print("Enter piece for promotion (B/K/Q/R): ");
                    String type = sc.nextLine().toUpperCase();
                    while (!type.equals("B") && !type.equals("K") && !type.equals("Q") && !type.equals("R")) {
                        System.out.print("Invalid value! Enter piece for promotion (B/K/Q/R): ");
                        type = sc.nextLine().toUpperCase();
                    }
                    // chamar método de promoção com a peça escolhida:
                    chessMatch.replacePromotedPiece(type);
                }
            }
            catch (ChessException e) {
                System.out.println(e.getMessage());
                // aguardar o 'enter' do usuário com 'sc.nextLine()':
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printBoard(chessMatch.getPieces());
        UI.printMatch(chessMatch, captured);
    }
}
