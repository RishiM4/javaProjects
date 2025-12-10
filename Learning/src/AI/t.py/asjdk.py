import pyautogui
import pytesseract
import time
# Ignore top 90px         (URL bar)
# Ignore bottom 40px      (taskbar)
# Capture the rest
REGION = (0, 100, 1920, 1080 - 150 - 40)

def read_screen_text():
    img = pyautogui.screenshot(region=REGION)
    img.save("screedn.png")
time.sleep(3)
read_screen_text()
