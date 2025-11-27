import yake
from sentence_transformers import SentenceTransformer, util

text = """
Artificial intelligence is rapidly transforming industries around the world.
Companies are using machine learning models to automate tasks, analyze data,
and improve decision-making. However, there are concerns about ethics, 
privacy, and job displacement.
"""
model = SentenceTransformer('all-MiniLM-L6-v2')


emb1 = model.encode(text, convert_to_tensor=True)
highscore = 0
word = ""
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
