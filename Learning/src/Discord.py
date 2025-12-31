import requests
import datetime
import os

WEBHOOK_URL = "https://discord.com/api/webhooks/1455980619703259156/aVEop1HIP3vo9g0ej3izZz4vrMirIkOETT1ciGwjHRKaVk5pJtqrfkoQRLiGLVXKL5Oa"

now = datetime.datetime.utcnow().strftime("%Y-%m-%d %H:%M UTC")

message = f"🧪 Test update\nTime: {now}"

requests.post(WEBHOOK_URL, json={
    "content": message
})
