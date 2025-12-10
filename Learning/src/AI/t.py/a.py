import easyocr, pyautogui,time
from ollama import Client
client = Client()

reader = easyocr.Reader(['en'])
def get_response(input) :
    model = "llama3.2"
    
    response = client.generate(
        model=model,
        prompt=input
    )
    return response['response']
def read_text():
    img = pyautogui.screenshot()
    ##img.save("screen.png")
    result = reader.readtext("screen.png", detail=0)
    return "\n".join(result)


time.sleep(5)
test=read_text()
print(test)
print(get_response("Extract the main questions from this text:" +test))

