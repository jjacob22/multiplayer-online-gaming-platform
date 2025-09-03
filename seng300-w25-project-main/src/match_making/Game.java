package match_making;


public enum Game {
    Chess {
        @Override
        public String toString() {
            return "Chess";
        }
    },
    TicTacToe {
        @Override
        public String toString() {
            return "Tic-Tac-Toe";
        }
    },
    ConnectFour {
        @Override
        public String toString() {
            return "Connect Four";
        }
    }
}
