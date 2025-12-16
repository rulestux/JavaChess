package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0; i < board.getRows(); i++) {
            for (int j=0; j < board.getColumns(); j++) {
                // downcasting:
                mat[i][j] = (ChessPiece)board.piece(i, j);
            }
        }
        return mat;
    }

    // implementação de alvos ou destinos possíveis:
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    // implementação de movimento:
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        // impedir movimento caso o jogador coloque seu rei em cheque,
        // testando em testCheck:
        if (testCheck(currentPlayer)) {
            // se true, desfazer o movimento:
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can not put yourself in check.");
        }

        // ternário para verificar se o oponente foi colocado em cheque:
        check = (testCheck(opponent(currentPlayer))) ? true : false;

        // teste cheque-mate:
        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        else {
            nextTurn();
        }

        return (ChessPiece)capturedPiece;
    }

/*===========================================================================*/
// MAKEMOVE

    // movimento das peças de uma origem para um destino, incluindo substituição
    // de peças capturadas nas casas:
    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece)board.removePiece(source);
        p.increaseMoveCount();

        // peça capturada:
        Piece capturedPiece = board.removePiece(target);

        // conclusão do movimento:
        board.placePiece(p, target);

        // caso haja uma captura, atualizar lista de peças no tabuleiro
        // e de peças capturadas:
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // Roque pequeno: validar se a peça em movimento é do tipo King e se
        // o movimento foi excepcional de duas casas para a direita:
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            // instância das posições de origem e destino da Torre:
            Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
            Position targetR = new Position(source.getRow(), source.getColumn() + 1);
            // movendo a Torre:
            ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
            board.placePiece(rook, targetR);
            rook.increaseMoveCount();
        }
        // Roque grande: validar se a peça em movimento é do tipo King e se
        // o movimento foi excepcional de duas casas para a esquerda:
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            // instância das posições de origem e destino da Torre:
            Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
            Position targetR = new Position(source.getRow(), source.getColumn() - 1);
            // movendo a Torre:
            ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
            board.placePiece(rook, targetR);
            rook.increaseMoveCount();
        }

        return capturedPiece;
    }

/*===========================================================================*/
// UNDO MOVE

    // desfazer movimento, caso o movimento solicitado resulte em cheque:
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece)board.removePiece(target);
        p.decreaseMoveCount();

        // conclusão:
        board.placePiece(p, source);

        // testar se alguma peça foi capturada e reintegrá-la:
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // Roque pequeno:
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
            Position targetR = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetR);
            board.placePiece(rook, sourceR);
            rook.decreaseMoveCount();
        }
        // Roque grande:
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
            Position targetR = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetR);
            board.placePiece(rook, sourceR);
            rook.decreaseMoveCount();
        }
    }

/*===========================================================================*/

    // validação da posição de origem:
    private void validateSourcePosition(Position position) {
        // se existe peça na posição (casa) escolhida:
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position.");
        }
        // se o jogador atual é diferente do jogador da vez:
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours.");
        }
        // se a peça pode ser movida ou está travada:
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
    }

    // validação da posição de destino:
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can not move to target position.");
        }
    }

    // método de troca de jogada, chamado em performChessMove:
    private void nextTurn() {
        // incrementa turno:
        turn++;
        // troca de jogador com condiconal ternária: se o currentPlayer for
        // igual a branco, então vira preto, ':' do contrário, branco:
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // método para identificar o oponente pela cor:
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // método para rastrear o rei de cada cor, filtrando com expressão lambda:
    private ChessPiece king(Color color) {
        // listar todas as peças da cor do parâmetro do método:
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() ==  color).collect(Collectors.toList());
        for (Piece p : list) {
            // com o foreach, checar se a peça é rei com 'instanceof King' e
            // retorná-lo:
            if (p instanceof King) {
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("There is no " + color + " King on the board.");
    }

    // método para testar se algum rei está em cheque, chamado em
    // performChessMove:
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        // listar todas as peças oponentes à cor do parâmetro do método:
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)). collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            // com o foreach, checar se algum destino possível de cada peça
            // adversária leva à posição do rei:
            boolean[][] mat = p.possibleMoves();
            // se alguma posição na matriz de alvos possíveis da peça em
            // verificação coincidir com a posição do rei, retornar true:
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        // caso o foreach se esgote sem a coincidência acima, retornar false:
        return false;
    }

    // método para identificar o cheque-mate:
    private boolean testCheckMate(Color color) {
        // testar se não está em cheque:
        if (!testCheck(color)) {
            return false;
        }
        // listar todas as peças na cor do parâmetro do método:
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() ==  color).collect(Collectors.toList());
        // foreach para percorrer cada uma das as peças da lista acima e...
        for (Piece p : list) {
            // checar movimentos possíveis da peça em verificação:
            boolean[][] mat = p.possibleMoves();
            for (int i=0; i < board.getRows(); i++) {
                for (int j=0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        // esse movimento da peça em verificação desfaz o
                        // cheque? realizar movimento simulado para teste
                        // da posição em verificação:
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        // verificar de está em cheque:
                        boolean testCheck = testCheck(color);
                        // desfazer movimento:
                        undoMove(source, target, capturedPiece);
                        // se não estiver em cheque com o movimento em teste
                        // retornar falso para testCheckMate:
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // método para controlar as peças no tabuleiro, recebendo a posição no
    // formato das posições do tabuleiro do xadrez, em vez de posições de
    // matriz, com o método 'toPosition()':
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        // atualizar lista de peças no tabuleiro:
        piecesOnTheBoard.add(piece);
    }

/*===========================================================================*/
// INITIAL SETUP

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
    }
}
