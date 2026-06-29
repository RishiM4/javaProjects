import pyautogui
import getpixelcolor
k=0
while True:
    pyautogui.scroll(300)
    k+=1
    #print(f"{x},{y}")
    color = getpixelcolor.pixel(938,308)
    if (color[0] > 230 and color[1] < 75 and color[2]<100) :
        if(getpixelcolor.pixel(1562,308) != (151, 113, 74)) :
            pyautogui.moveTo(1562,308)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(1)
    color = getpixelcolor.pixel(938,370)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,370) != (151, 113, 74)) :
            pyautogui.moveTo(1562,370)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(2)
    color = getpixelcolor.pixel(938,428)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,428) != (151, 113, 74)) :
            pyautogui.moveTo(1562,428)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(3)
    color = getpixelcolor.pixel(938,489)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,489) != (151, 113, 74)) :
            pyautogui.moveTo(1562,489)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(4)
    color = getpixelcolor.pixel(938,551)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,551) != (151, 113, 74)) :
            pyautogui.moveTo(1562,551)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(5)
    color = getpixelcolor.pixel(938,610)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,610) != (151, 113, 74)) :
            pyautogui.moveTo(1562,610)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(6)
    color = getpixelcolor.pixel(938,671)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,671) != (151, 113, 74)) :
            pyautogui.moveTo(1562,671)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(7)
    color = getpixelcolor.pixel(938,730)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,730) != (151, 113, 74)) :
            pyautogui.moveTo(1562,730)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(color)
    color = getpixelcolor.pixel(938,787)
    if (color[0] > 230 and color[1] < 75 and color[2]<100):
        if(getpixelcolor.pixel(1562,787) != (151, 113, 74)) :
            pyautogui.moveTo(1562,787)
            pyautogui.mouseDown()
            pyautogui.mouseUp()
        print(9)
    if k == 5 :
        pyautogui.moveTo(1185,869)
        pyautogui.mouseDown()
        pyautogui.mouseUp()
        pyautogui.moveTo(1534,869)
        pyautogui.mouseDown()
        pyautogui.mouseUp()
        k=0
    color = getpixelcolor.pixel(441,869)
    if  color == (211, 191, 143) or color ==(255, 241, 210) :
        pyautogui.moveTo(441,869)
        pyautogui.mouseDown()
        pyautogui.mouseUp()
        
    #1 - 938,308 
    #2 - 938,367    
    #3 - 938,428    
    #4 - 938,489
    #5 - 938,551
    #6 - 938,610
    #7 - 938,671
    #8 - 938,730
    #9 - 938,787
    #1562, BUTTON
    #441,876 refresh
    #1185,869 boost 
    #1534,864 collect