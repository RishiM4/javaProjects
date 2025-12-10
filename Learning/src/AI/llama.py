from ollama import Client

client = Client()
def get_response(input, questionType) :
    model = "llama3.2"
    if questionType != "open" :
        model="slower model"
    response = client.generate(
        model=model,
        prompt=input
    )
    return response['response']
print(get_response("Hi","open"))