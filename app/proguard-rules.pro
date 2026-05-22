# [SECURITY] Aggressive Obfuscation Rules for Nivar

# Optimization passes
-optimizationpasses 5
-allowaccessmodification

# Keep essential Android classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# [SECURITY] Obfuscate internal logic
-repackageclasses 'com.nivar.app.a'
-overloadaggressively

# [SECURITY] Remove Log statements in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# [SECURITY] Keep security-related annotations
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Prevent shrinking of Compose-related code
-keepclassmembers class androidx.compose.runtime.Recomposer {
    public *** runRecomposeAndApplyChanges(...);
}

# Retrofit/OkHttp/Gson obfuscation preservation
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepclassmembers class com.nivar.app.data.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keep class okhttp3.** { *; }

# ISRO/Ministry Data model preservation (if using Gson reflection)
-keepclassmembers class com.nivar.app.ui.screens.MinistryContact { *; }
-keepclassmembers class com.nivar.app.ui.screens.Official { *; }
-keepclassmembers class com.nivar.app.ui.screens.SubOrganization { *; }
