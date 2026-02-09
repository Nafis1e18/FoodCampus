# ✅ Railway Deployment Guide - FoodCampus

## Why Railway is Better for Your Project

- ✅ **Auto-detects Java/Maven** - No Docker needed
- ✅ **$5 free credits/month** (~500 hours runtime)
- ✅ **Easy environment variables** - Simple UI
- ✅ **Fast deployments** - Usually faster than Render
- ✅ **Great for Spring Boot** - Built-in support

---

## What Was Changed for Railway

### Files Created:
1. **`nixpacks.toml`** - Railway build configuration (tells Railway how to build your Java app)
2. **This guide!**

### Database Already Configured:
- ✅ Neon PostgreSQL connection in `application.properties`
- ✅ Neon PostgreSQL connection in `application-prod.properties`

---

## Deploy to Railway - Step by Step

### Step 1: Push Your Code to GitHub

```bash
cd C:\Users\MN\Documents\FoodCampus
git add .
git commit -m "Add Railway deployment configuration"
git push origin main
```

> If you haven't initialized git yet:
```bash
git init
git add .
git commit -m "Initial commit with Railway support"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/FoodCampus.git
git push -u origin main
```

---

### Step 2: Create Railway Account

1. Go to **https://railway.app**
2. Click **Login** → **Login with GitHub**
3. Authorize Railway to access your GitHub

---

### Step 3: Deploy Your Project

1. Click **New Project**
2. Select **Deploy from GitHub repo**
3. Choose your `FoodCampus` repository
4. Railway will:
   - ✅ Detect `pom.xml`
   - ✅ Use `nixpacks.toml` configuration
   - ✅ Build your Spring Boot app
   - ✅ Start automatically

---

### Step 4: Add Environment Variables

In your Railway project dashboard:

1. Click on your service
2. Go to **Variables** tab
3. Add these variables:

| Variable Name | Value | Notes |
|---------------|-------|-------|
| `MAIL_USERNAME` | `nirobnafis3@gmail.com` | Your Gmail |
| `MAIL_PASSWORD` | `your-app-password` | Gmail App Password (not your regular password) |
| `GEMINI_API_KEY` | `AIzaSyC6ONOvk0qk2lQ6lCEByBeLjEGdCQtsJ-U` | Your Gemini API key |
| `PORT` | `8080` | Railway provides this automatically, but set it explicitly |

4. Click **Deploy** (or it auto-deploys on save)

---

### Step 5: Get Your URL

1. Go to **Settings** tab
2. Scroll to **Environment** section
3. Click **Generate Domain**
4. You'll get a URL like: `https://foodcampus-production.up.railway.app`

---

## Important Notes

### ⚠️ Gmail App Password Setup

Your current password `1234` won't work. Generate an App Password:

1. Go to **Google Account** → **Security**
2. Enable **2-Step Verification** (if not already enabled)
3. Search for **App Passwords**
4. Select **Mail** and **Other (Custom name)**
5. Name it "FoodCampus Railway"
6. Copy the 16-character password
7. Use this in Railway's `MAIL_PASSWORD` variable

### 💾 File Uploads (Images)

Railway's free tier has **ephemeral storage**:
- Product images, profile pictures will be **deleted on redeploy**
- **Solution**: Use **Cloudinary** for image storage
  - Free tier: 25GB storage, 25GB bandwidth/month
  - Sign up at: https://cloudinary.com

### 🔋 Railway Free Tier Limits

- **$5 credits/month** (resets monthly)
- ~500 hours of runtime
- No sleep/cold starts (unlike Render!)
- After credits exhausted, service stops until next month

### 📊 Monitor Usage

Check your usage in Railway dashboard:
- **Overview** tab shows credit usage
- **Metrics** tab shows CPU/Memory/Network

---

## Database Information

Your app is already configured to use **Neon PostgreSQL**:

- **Host**: `ep-little-bush-a1tyf2pp-pooler.ap-southeast-1.aws.neon.tech`
- **Database**: `neondb`
- **User**: `neondb_owner`
- **Connection**: Already in your properties files ✅

**No additional database setup needed!**

---

## Testing Locally Before Deploy

Test with Neon database locally:

```bash
.\mvnw.cmd spring-boot:run
```

Your app will connect to Neon PostgreSQL.

---

## Deployment Timeline

1. **Push to GitHub** - 1 minute
2. **Connect to Railway** - 2 minutes
3. **First build** - 3-5 minutes
4. **Add environment variables** - 1 minute
5. **Generate domain** - 30 seconds

**Total: ~10 minutes to go live!** 🚀

---

## Troubleshooting

### Build Fails
- Check Railway build logs
- Verify `nixpacks.toml` is in root directory
- Ensure `pom.xml` is correct

### App Doesn't Start
- Check **Deployments** → **Logs** in Railway
- Verify all environment variables are set
- Check that PORT is set to 8080

### Database Connection Error
- Verify Neon database is active
- Check connection string in `application.properties`
- Ensure SSL mode is set (`sslmode=require`)

---

## Next Steps After Deployment

1. **Test all features** on your Railway URL
2. **Set up Cloudinary** for permanent image storage
3. **Monitor credit usage** in Railway dashboard
4. **Consider upgrading** if you need more credits ($5/month for hobby plan)

---

## Comparison: Railway vs Render

| Feature | Railway | Render |
|---------|---------|--------|
| Java Support | ✅ Native | ⚠️ Docker only |
| Free Tier | $5 credits/month | 750 hours/month |
| Cold Starts | ❌ No | ✅ Yes (15 min) |
| Setup Time | Faster | Slower |
| Database | External (Neon) | Free 90 days |
| Best For | Your project! | Frontend/Node apps |

---

## Ready to Deploy! ✅

Everything is configured and ready:
- ✅ Neon PostgreSQL configured
- ✅ Railway build file created
- ✅ Environment variables documented
- ✅ Deployment steps clear

**Go deploy your FoodCampus app on Railway!** 🎉

