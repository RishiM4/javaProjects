import numpy as np
import pygame
import random

# Grid dimensions
BOARD_WIDTH = 10
BOARD_HEIGHT = 20
CELL_SIZE = 30

# Tetromino shapes
TETROMINOES = {
    'I': [[1, 1, 1, 1]],
    'O': [[1, 1],
          [1, 1]],
    'T': [[0, 1, 0],
          [1, 1, 1]],
    'S': [[0, 1, 1],
          [1, 1, 0]],
    'Z': [[1, 1, 0],
          [0, 1, 1]],
    'J': [[1, 0, 0],
          [1, 1, 1]],
    'L': [[0, 0, 1],
          [1, 1, 1]],
}

PIECES = list(TETROMINOES.keys())

# Colors for each tetromino (RGB)
COLORS = {
    'I': (0, 255, 255),
    'O': (255, 255, 0),
    'T': (128, 0, 128),
    'S': (0, 255, 0),
    'Z': (255, 0, 0),
    'J': (0, 0, 255),
    'L': (255, 165, 0),
    'X': (50, 50, 50),  # Locked blocks color (gray)
}

class Tetris:
    def __init__(self):
        self.init_gui()
        self.reset()
        self.fall_counter = 0
        self.fall_speed = 10  # Number of steps before automatic piece falls by 1

    def reset(self):
        self.board = np.zeros((BOARD_HEIGHT, BOARD_WIDTH), dtype=int)
        self.score = 0
        self.done = False
        self.current_piece = None
        self.piece_type = None
        self.piece_position = [0, 0]
        self.next_piece()
        self.fall_counter = 0
        self.render()
        return self.get_state()

    def next_piece(self):
        self.piece_type = random.choice(PIECES)
        self.current_piece = np.array(TETROMINOES[self.piece_type])
        # Spawn above visible board (-1 row)
        self.piece_position = [-1, BOARD_WIDTH // 2 - len(self.current_piece[0]) // 2]
        if self.check_collision(self.current_piece, self.piece_position):
            # Game over if spawn collides
            self.done = True

    def rotate_piece(self):
        rotated = np.rot90(self.current_piece, -1)
        if not self.check_collision(rotated, self.piece_position):
            self.current_piece = rotated

    def move(self, dx):
        new_pos = [self.piece_position[0], self.piece_position[1] + dx]
        if not self.check_collision(self.current_piece, new_pos):
            self.piece_position = new_pos

    def soft_drop(self):
        new_pos = [self.piece_position[0] + 1, self.piece_position[1]]
        if not self.check_collision(self.current_piece, new_pos):
            self.piece_position = new_pos
            return True
        else:
            # Can't move down: lock piece and spawn new one
            self.lock_piece()
            return False

    def drop_piece(self):
        while self.soft_drop():
            pass

    def lock_piece(self):
        for i in range(self.current_piece.shape[0]):
            for j in range(self.current_piece.shape[1]):
                if self.current_piece[i][j]:
                    x = self.piece_position[0] + i
                    y = self.piece_position[1] + j
                    if 0 <= x < BOARD_HEIGHT and 0 <= y < BOARD_WIDTH:
                        self.board[x][y] = 1
        lines_cleared = self.clear_lines()
        self.next_piece()
        if self.done:
            return
        self.score += lines_cleared * 60  # Reward 10 points per line

    def clear_lines(self):
        full_rows = [i for i in range(BOARD_HEIGHT) if all(self.board[i])]
        num_cleared = len(full_rows)
        if num_cleared > 0:
            self.board = np.delete(self.board, full_rows, axis=0)
            new_rows = np.zeros((num_cleared, BOARD_WIDTH), dtype=int)
            self.board = np.vstack((new_rows, self.board))
        return num_cleared
    def count_holes(self):
        holes = 0
        for col in range(BOARD_WIDTH):
            block_found = False
            for row in range(BOARD_HEIGHT):
                if self.board[row][col] == 1:
                    block_found = True
                elif self.board[row][col] == 0 and block_found:
                    # Empty cell below a block
                    holes += 1
        return holes
    def check_collision(self, piece, pos):
        for i in range(piece.shape[0]):
            for j in range(piece.shape[1]):
                if piece[i][j]:
                    x = pos[0] + i
                    y = pos[1] + j
                    if y < 0 or y >= BOARD_WIDTH:
                        return True
                    if x >= BOARD_HEIGHT:
                        return True
                    if x >= 0 and self.board[x][y]:
                        return True
        return False

    def step(self, action):
        if self.done:
            return self.get_state(), -25, True  # Penalty for losing

        # Apply action
        if action == 0:
            self.move(-1)
        elif action == 1:
            self.move(1)
        elif action == 2:
            self.rotate_piece()
        elif action == 3:
            self.drop_piece()

        self.fall_counter += 1
        reward = 0.1  # small reward for staying alive

        if self.fall_counter >= self.fall_speed:
            self.fall_counter = 0
            moved_down = self.soft_drop()
            if not moved_down:
                # Piece locked
                if self.done:
                    reward = -25  # Game over penalty
                else:
                    reward +=10
                    holes = self.count_holes()
                    reward -= holes * 5
            else:
                reward += 0  # no extra reward just falling

        return self.get_state(), reward, self.done
    def get_state(self):
        # Board + current piece projected on top
        state = self.board.copy()
        for i in range(self.current_piece.shape[0]):
            for j in range(self.current_piece.shape[1]):
                if self.current_piece[i][j]:
                    x = self.piece_position[0] + i
                    y = self.piece_position[1] + j
                    if 0 <= x < BOARD_HEIGHT and 0 <= y < BOARD_WIDTH:
                        state[x][y] = 2  # Different number for current piece
        return state.flatten()

    # ===== GUI PART =====

    def init_gui(self):
        pygame.init()
        self.screen = pygame.display.set_mode((BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE))
        pygame.display.set_caption("Tetris AI")

    def render(self):
        self.screen.fill((0, 0, 0))  # Black background

        # Draw locked blocks
        for i in range(BOARD_HEIGHT):
            for j in range(BOARD_WIDTH):
                if self.board[i][j]:
                    pygame.draw.rect(self.screen, COLORS['X'],
                                     (j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE))
                    pygame.draw.rect(self.screen, (30,30,30),
                                     (j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE), 1)  # Border

        # Draw current falling piece
        for i in range(self.current_piece.shape[0]):
            for j in range(self.current_piece.shape[1]):
                if self.current_piece[i][j]:
                    x = self.piece_position[1] + j
                    y = self.piece_position[0] + i
                    if 0 <= y < BOARD_HEIGHT and 0 <= x < BOARD_WIDTH:
                        pygame.draw.rect(self.screen, COLORS[self.piece_type],
                                         (x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE))
                        pygame.draw.rect(self.screen, (255,255,255),
                                         (x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE), 1)  # White border

        pygame.display.flip()
