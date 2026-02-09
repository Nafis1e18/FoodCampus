# 🚀 Railway Quick Deploy - FoodCampus

## ✅ Everything is Ready!

Your project is now configured for **Railway deployment** with **Neon PostgreSQL**.

---

## 📋 Files Created/Modified

| File | Purpose | Status |
|------|---------|--------|
| `nixpacks.toml` | Railway build config | ✅ Created |
| `application.properties` | Uses env vars for sensitive data | ✅ Updated |
| `application-prod.properties` | Production config with Neon | ✅ Updated |
| `pom.xml` | PostgreSQL driver added | ✅ Ready |
| `system.properties` | Java 17 specified | ✅ Ready |

---

## 🎯 Deploy in 3 Simple Steps

### 1️⃣ Push to GitHub
```bash
git add .
git commit -m "Railway deployment ready"
git push origin main
```

### 2️⃣ Connect Railway
- Go to https://railway.app
- Login with GitHub
- New Project → Deploy from GitHub
- Select FoodCampus repo

### 3️⃣ Add Environment Variables
In Railway dashboard, add these 3 variables:

```
MAIL_USERNAME=nirobnafis3@gmail.com
MAIL_PASSWORD=your-gmail-app-password
GEMINI_API_KEY=AIzaSyC6ONOvk0qk2lQ6lCEByBeLjEGdCQtsJ-U
```

That's it! Railway auto-deploys. ✅

---

## 🔑 Get Gmail App Password

1. Google Account → Security
2. Enable 2-Step Verification
3. Search "App Passwords"
4. Generate for "Mail"
5. Copy 16-character password
6. Use in Railway's `MAIL_PASSWORD`

---

## 📊 What Railway Does Automatically

✅ Detects Java/Maven project  
✅ Reads `nixpacks.toml` for build instructions  
✅ Builds with `mvn clean install`  
✅ Runs with production profile  
✅ Assigns a public URL  
✅ Auto-redeploys on git push  

---

## 💡 Pro Tips

- **Monitor credits**: Railway dashboard shows usage
- **View logs**: Deployments tab → Click latest deploy
- **Custom domain**: Settings → Generate Domain
- **Database**: Already using Neon (no Railway DB needed!)

---

## 🆘 Need Help?

Full guide: See `RAILWAY_DEPLOYMENT.md`

Happy deploying! 🎉

