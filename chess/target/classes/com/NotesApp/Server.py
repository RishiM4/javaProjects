from fastapi import FastAPI
import uvicorn
from sentence_transformers import SentenceTransformer
import yake
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer, util


model = SentenceTransformer('all-MiniLM-L6-v2')
emb1 = None
text = ""
app = FastAPI()
model = SentenceTransformer("all-MiniLM-L6-v2")


class EmbedRequest(BaseModel):
    text: str


@app.post("/getKeyword")
def embed(req: EmbedRequest):
    highscore = 0
    text = req.text
    word = ""
    emb1 = req.text
    emb1 = model.encode(text, convert_to_tensor=True)
    kw_extractor = yake.KeywordExtractor(
        lan="en",      # Language
        n=2,           # 1-word keywords (set n=2 or 3 for multi-word)
        dedupLim=0.9,  # Remove near-duplicates
        top=3,        # Number of keywords to return
        features=None
    )

    keywords = kw_extractor.extract_keywords(text)
    for kw, score in keywords:
        print(f"{kw}: {score}")
        emb2 = model.encode(kw, convert_to_tensor=True)

        similarity = util.cos_sim(emb1, emb2).item()
        if similarity > highscore :
            highscore = similarity
            word = kw

    kw_extractor = yake.KeywordExtractor(
        lan="en",      # Language
        n=1,           # 1-word keywords (set n=2 or 3 for multi-word)
        dedupLim=0.5,  # Remove near-duplicates
        top=2,        # Number of keywords to return
        features=None
    )

    keywords = kw_extractor.extract_keywords(text)




    for kw, score in keywords:
        print(f"{kw}: {score}")
        emb2 = model.encode(kw, convert_to_tensor=True)

        similarity = util.cos_sim(emb1, emb2).item()
        if similarity > highscore :
            highscore = similarity
            word = kw

    print(f"Highscore{highscore}: {word}")

    return {f"highscore: {highscore}: at word {word}"}


@app.post("/test")
def embed(req: EmbedRequest):
    
        



    return {"recieved": req.text}
if __name__ == "__main__":
    uvicorn.run("Server:app", host="0.0.0.0", port=8000, reload=False)
