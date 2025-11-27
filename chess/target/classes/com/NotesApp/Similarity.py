from sentence_transformers import SentenceTransformer
from sklearn.cluster import KMeans
import nltk
from nltk.tokenize import sent_tokenize

nltk.download('punkt')

model = SentenceTransformer("all-MiniLM-L6-v2")

def get_semantic_topics(text, num_topics=3):
    # Split into sentences
    sentences = sent_tokenize(text)
    
    # Embed
    embeddings = model.encode(sentences)

    # Cluster
    kmeans = KMeans(n_clusters=num_topics, random_state=42)
    labels = kmeans.fit_predict(embeddings)

    # Group sentences by cluster
    clusters = {i: [] for i in range(num_topics)}
    for sentence, label in zip(sentences, labels):
        clusters[label].append(sentence)

    # Create topic labels based on cluster content
    topics = {}
    for label, sents in clusters.items():
        joined = " ".join(sents)
        # extract simple top words
        words = [w.lower() for w in joined.split() if w.isalpha()]
        top_words = list(set(words))[:4]
        topics[f"Topic {label+1}"] = ", ".join(top_words)

    return topics

text = """
Urban areas are becoming new ecosystems that support diverse wildlife.
Animals such as foxes and pigeons adapt easily to city life.
Cities struggle with waste management due to wildlife activity.
Understanding animal behavior helps design better environmental policies.
"""

print(get_semantic_topics(text))
