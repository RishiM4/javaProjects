import chess
import pygame as t
board = chess.Board()
print(board)

def main():
    print("Hello, World!")
    for move in board.legal_moves:
        print(move)
    
if __name__ == "__main__":
    main()