# Keep Retrofit interfaces (HTTP annotations)
-keep interface * {
    @retrofit2.http.* <methods>;
}

# Keep generic type signatures (VERY IMPORTANT)
-keepattributes Signature
-keepattributes Exceptions

# Keep Gson annotated fields
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class com.sauban.securemessenger.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**

-keep class kotlin.Metadata { *; }

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

