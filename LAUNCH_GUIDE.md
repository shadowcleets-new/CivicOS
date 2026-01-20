# CivicOS Launch Guide (Troubleshooting Edition)

## ❌ Issue: "No Module" in Run Configuration
If you see `<no module>` in the dropdown, it means **Gradle Sync Failed**.

### Solution:
1.  **Check the Build Tab**: Look at the bottom of Android Studio. Click the `Build` or `Sync` tab to see the actual error message.
2.  **Force Re-Sync**:
    - Go to **File > Sync Project with Gradle Files**.
    - Or look for the **Elephant Icon** (🐘) with a refresh arrow in the top right toolbar.
3.  **Check SDK Location**:
    - Ensure your `local.properties` file points to your Android SDK.
    - If missing, Android Studio usually prompts to create it.
    
### Folder Structure Check
Make sure you opened `CivicOS/android_native` and NOT `CivicOS/android_native/app`.

## 1. Backend
1.  Navigate to `CivicOS/backend`.
2.  Run `docker-compose up -d`.
3.  Run `uvicorn app.main:app --reload`.

## 2. iOS Native
1.  Open **Xcode** on your Mac.
2.  Open the `ios_native` folder.
