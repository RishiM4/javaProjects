import torch
from tetris import Tetris
from dqn import Agent
import time
import pygame

def train(num_episodes=500):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    env = Tetris()
    state_size = BOARD_HEIGHT * BOARD_WIDTH  # Flattened state size from tetris.py
    action_size = 4  # left, right, rotate, hard drop

    agent = Agent(state_size, action_size, device)

    for episode in range(1, num_episodes + 1):
        state = env.reset()
        total_reward = 0
        done = False

        while not done:
            # Handle Pygame events to keep window responsive
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    return

            action = agent.act(state)
            next_state, reward, done = env.step(action)
            agent.remember(state, action, reward, next_state, done)
            agent.replay()

            state = next_state
            total_reward += reward

            env.render()
           # time.sleep(0.01)  # Slow down for visualization

        print(f"Episode {episode} finished with reward {total_reward}, epsilon {agent.epsilon:.3f}")

    pygame.quit()

if __name__ == "__main__":
    # Import constants from tetris for BOARD_HEIGHT and BOARD_WIDTH
    from tetris import BOARD_HEIGHT, BOARD_WIDTH
    train()
