import pygame
pygame.init()

screen = pygame.display.set_mode((1000, 500))
pygame.display.set_caption("Chess | By Rishi & Advik")
currentPiece = None
currentCell = None
moveCells = []
running = True
captures = []
moves = []
while running:
    print("Hi!")
