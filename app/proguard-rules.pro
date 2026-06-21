-keep class com.vidsaver.app.data.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn kotlinx.serialization.**
-keep,includedescriptorclasses class com.vidsaver.app.**$$serializer { *; }
-keepclassmembers class com.vidsaver.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.vidsaver.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}
