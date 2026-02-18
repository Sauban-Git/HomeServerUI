##############################
# RETROFIT
##############################

# Keep Retrofit interfaces (HTTP annotations)
-keep interface * {
    @retrofit2.http.* <methods>;
}

# Keep generic type signatures (VERY IMPORTANT)
-keepattributes Signature
-keepattributes Exceptions

##############################
# GSON
##############################

# Keep Gson annotated fields
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep all model/data classes in your app
-keep class com.sauban.securemessenger.** { *; }

##############################
# OKHTTP
##############################

-dontwarn okhttp3.**
-dontwarn okio.**

##############################
# KOTLIN
##############################

-keep class kotlin.Metadata { *; }

##############################
# PARCELABLE
##############################

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

##############################
# ENUM SAFETY
##############################

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

