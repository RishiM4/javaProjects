import requests
import datetime
import os

WEBHOOK_URL = "https://discord.com/api/webhooks/1455980619703259156/aVEop1HIP3vo9g0ej3izZz4vrMirIkOETT1ciGwjHRKaVk5pJtqrfkoQRLiGLVXKL5Oa"

now = datetime.datetime.utcnow().strftime("%Y-%m-%d %H:%M UTC")

message = f"Last updated: {now}"
import urllib.request
import json

API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjJkYTMxNmJmLThjYzQtNDE4OC04YWNjLTE0ZjViNjBiZTlmYyIsImlhdCI6MTc2NzIwNTYzMCwic3ViIjoiZGV2ZWxvcGVyLzY3MzQ0MWMxLTkwNDgtNWVmNy02ZDliLTFhZGVkMGUzMDU4YiIsInNjb3BlcyI6WyJicmF3bHN0YXJzIl0sImxpbWl0cyI6W3sidGllciI6ImRldmVsb3Blci9zaWx2ZXIiLCJ0eXBlIjoidGhyb3R0bGluZyJ9LHsiY2lkcnMiOlsiNDkuMjA3LjE1MS4yMDEiXSwidHlwZSI6ImNsaWVudCJ9XX0.eDLtKEMy9TsVHtQOnnyopP3b1FOJFnaZdo6ti6Ry6hp5NQA_8nH_CwroKD7fh9gcAl39PXnDpC_OX_xrMyNQ2g"
PLAYER_TAG = "#J8YLVC0J"         # Replace with the player tag
PLAYER_TAG_ENCODED = PLAYER_TAG.replace("#", "%23")

url = f"https://api.brawlstars.com/v1/players/{PLAYER_TAG_ENCODED}"

# Set the Authorization header with your API token
req = urllib.request.Request(
    url,
    headers={
        "Authorization": f"Bearer {API_KEY}"
    }
)

with urllib.request.urlopen(req) as response:
    data = json.loads(response.read().decode())

player_name = data["name"]
trophies = data["trophies"]
message+=f"\nCurrent Trophies:{trophies}"
requests.post(WEBHOOK_URL, json={
    "content": message
})
