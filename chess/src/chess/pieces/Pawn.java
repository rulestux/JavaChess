package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    // associação com ChessMatch para o En-Passant:
    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "🨅";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        if (getColor() == Color.WHITE) {
            // movimento padrão do peão branco:
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // primeiro movimento do peão branco:
            p.setValues(position.getRow() - 2, position.getColumn());
            // verificar se a primeira de duas casas também está livre:
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // captura diagonal à direita:
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            // verificar se existe peça oponente na posição:
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // captura diagonal à esquerda:
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            // verificar se existe peça oponente na posição:
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // En-Passant à direita:
            // testar se existe peça adversária na linha 5 do tabuleiro e se
            // é um peão vulnerável:
            if (position.getRow() == 3) {
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    // efetuar movimento:
                    mat[right.getRow() - 1][right.getColumn()] = true;
                }
            }
            // En-Passant à esquerda:
            // testar se existe peça adversária na linha 5 do tabuleiro e se
            // é um peão vulnerável:
            if (position.getRow() == 3) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    // efetuar movimento:
                    mat[left.getRow() - 1][left.getColumn()] = true;
                }
            }
        }
        // movimentos do peão preto:
        else {
            // movimento padrão do peão preto:
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // primeiro movimento do peão preto:
            p.setValues(position.getRow() + 2, position.getColumn());
            // verificar se a primeira de duas casas também está livre:
            Position p2 = new Position(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // captura diagonal à direita:
            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            // verificar se existe peça oponente na posição:
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // captura diagonal à esquerda:
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            // verificar se existe peça oponente na posição:
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // En-Passant à direita:
            // testar se existe peça adversária na linha 4 do tabuleiro e se
            // é um peão vulnerável:
            if (position.getRow() == 4) {
                Position right = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    // efetuar movimento:
                    mat[right.getRow() + 1][right.getColumn()] = true;
                }
            }
            // En-Passant à esquerda:
            // testar se existe peça adversária na linha 4 do tabuleiro e se
            // é um peão vulnerável:
            if (position.getRow() == 4) {
                Position left = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    // efetuar movimento:
                    mat[left.getRow() + 1][left.getColumn()] = true;
                }
            }
        }

        return mat;
    }
}
