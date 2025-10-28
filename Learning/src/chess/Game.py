import pygame
import sys
import chess 
pygame.init()
board = chess.Board("rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP2PPP/RNBQKBNR w KQkq e6 0 3")
WIDTH, HEIGHT = 900, 700   
ROWS, COLS = 8, 8          
SQUARE_SIZE = 75
offset = 50

WHITE = (255, 255, 255)
HIGHLIGHT_WHITE = (247, 246, 132)
GREEN = (105,146,62)
HIGHLIGHT_GREEN = (186, 202, 68)
GRAY = (30, 30, 30)

x = 0
y = 0
font = pygame.font.SysFont("arial", 32)
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Chess | By Rishi & Advik")
currentPiece = None
currentCell = None
running = True
captures = []
while running:
    screen.fill(GRAY)
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.MOUSEBUTTONDOWN:
            if event.button == 1:  
                x, y = event.pos
                if (x > 50 and x < 650) and (y > 50 and y < 650) :
                    col = (x - 50) // 75
                    row = (y - 50) // 75
                    row = 7 - row
                    currentPiece = str(board.piece_at((row * 8) + col))
                    currentCell = row * 8 + col
                    if currentPiece == "None" :
                        currentCell = None
                    for move in board.generate_legal_captures() :
                        if board.is_capture(move) :
                            captures.append(move.to_square)
                    
        elif event.type == pygame.MOUSEBUTTONUP:
            if event.button == 1: 
                currentPiece = None
                currentCell = None

    for row in range(ROWS):
        for col in range(COLS):
            cell = (7-row) * 8 + col
            color = WHITE 
            if (row + col) % 2 != 0 :
                color = GREEN
            piece = str(board.piece_at(((7-row)*8)+col))
            if(cell == currentCell and color == WHITE) :
                color = HIGHLIGHT_WHITE
                surface = pygame.Surface((75, 75), pygame.SRCALPHA)

                
            elif(cell == currentCell and color == GREEN) :
                color = HIGHLIGHT_GREEN
                surface = pygame.Surface((75, 75), pygame.SRCALPHA)

            else :
                if(piece == 'k') :
                    surface = pygame.image.load('ChessPieces/black_king.png').convert_alpha()
                elif (piece == 'q') :
                    surface = pygame.image.load('ChessPieces/black_queen.png').convert_alpha()
                elif (piece == 'r') :
                    surface = pygame.image.load('ChessPieces/black_rook.png').convert_alpha()
                elif (piece == 'b') :
                    surface = pygame.image.load('ChessPieces/black_bishop.png').convert_alpha()
                elif (piece == 'n') :
                    surface = pygame.image.load('ChessPieces/black_knight.png').convert_alpha()
                elif (piece == 'p') :
                    surface = pygame.image.load('ChessPieces/black_pawn.png').convert_alpha()
                elif(piece == 'K') :
                    surface = pygame.image.load('ChessPieces/white_king.png').convert_alpha()
                elif (piece == 'Q') :
                    surface = pygame.image.load('ChessPieces/white_queen.png').convert_alpha()
                elif (piece == 'R') :
                    surface = pygame.image.load('ChessPieces/white_rook.png').convert_alpha()
                elif (piece == 'B') :
                    surface = pygame.image.load('ChessPieces/white_bishop.png').convert_alpha()
                elif (piece == 'N') :
                    surface = pygame.image.load('ChessPieces/white_knight.png').convert_alpha()
                elif (piece == 'P') :
                    surface = pygame.image.load('ChessPieces/white_pawn.png').convert_alpha()
                else :
                    surface = pygame.Surface((75, 75), pygame.SRCALPHA)
                
            rect = (col * SQUARE_SIZE + offset, row * SQUARE_SIZE + offset, SQUARE_SIZE, SQUARE_SIZE)
            scaled_img = pygame.transform.smoothscale(surface, (75, 75))
            pygame.draw.rect(screen, color, rect)
            screen.blit(scaled_img, (col*SQUARE_SIZE + offset, row*SQUARE_SIZE + offset))
            if cell in captures :

                ring_surface = pygame.Surface((75, 75), pygame.SRCALPHA)

                center = ((75 // 2), (75 // 2))
                radius = 75 // 2 - 6

                pygame.draw.circle(ring_surface, (0, 0, 0, 120), center, radius, 6)

                screen.blit(ring_surface, (4 * 75 + offset, 4 * 75 + offset))

    if currentPiece != None :
        if currentPiece == 'k' :
            piece = pygame.image.load('ChessPieces/black_king.png').convert_alpha()
        elif currentPiece == 'q' :
            piece = pygame.image.load('ChessPieces/black_queen.png').convert_alpha()
        elif currentPiece == 'r' :
            piece = pygame.image.load('ChessPieces/black_rook.png').convert_alpha()
        elif currentPiece == 'b' :
            piece = pygame.image.load('ChessPieces/black_bishop.png').convert_alpha()
        elif currentPiece == 'n' :
            piece = pygame.image.load('ChessPieces/black_knight.png').convert_alpha()
        elif currentPiece == 'p' :
            piece = pygame.image.load('ChessPieces/black_pawn.png').convert_alpha()
        elif currentPiece == 'K' :
            piece = pygame.image.load('ChessPieces/white_king.png').convert_alpha()
        elif currentPiece == 'Q' :
            piece = pygame.image.load('ChessPieces/white_queen.png').convert_alpha()
        elif currentPiece == 'R' :
            piece = pygame.image.load('ChessPieces/white_rook.png').convert_alpha()
        elif currentPiece == 'B' :
            piece = pygame.image.load('ChessPieces/white_bishop.png').convert_alpha()
        elif currentPiece == 'N' :
            piece = pygame.image.load('ChessPieces/white_knight.png').convert_alpha()
        elif currentPiece == 'P' :
            piece = pygame.image.load('ChessPieces/white_pawn.png').convert_alpha()
        else :
            piece = pygame.Surface((75, 75), pygame.SRCALPHA)
        piece = pygame.transform.smoothscale(piece, (75, 75))
        x, y = pygame.mouse.get_pos()
        screen.blit(piece, piece.get_rect(center=(x, y)))

    
        
    pygame.display.flip()

pygame.quit()
sys.exit()
