package boardgame;

public abstract class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
    }

    protected Board getBoard() {
        return board;
    }

    // implementação dos movimentos possíveis a serem determinados em cada
    // subclasse de peça:
    public abstract boolean[][] possibleMoves();

    // hook method (template method), um método concreto que utiliza um método
    // abstrato, relacionado com uma possível instanciação em subclasse:
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    // método para verificar se a peça está travada ou pode mover-se:
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i=0; i < mat.length; i++) {
            for (int j=0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
