-keep public class * {
   public * ;
 }

 -dontoptimize
 -dontpreverify

 -keep class com.cousinware.arete.mixins.* { *; }
 -keep,allowobfuscation @org.spongepowered.asm.mixin.Mixin class *
 -keep @org.spongepowered.asm.mixin.Mixin class *
  -keepclassmembers class * {
  @org.spongepowered.asm.mixin.Mixin *;
  }

 -keep class org.spongepowered.asm.mixin.injection.Inject { *; }
 -keep class org.spongepowered.asm.mixin.injection.ModifyVariable { *; }
 -keep class org.spongepowered.asm.mixin.injection.ModifyConstant { *; }
 -keep class org.spongepowered.asm.mixin.injection.Shadow { *; }
 -keep class org.spongepowered.asm.mixin.injection.Overwrite { *; }
 -keep class org.spongepowered.asm.mixin.injection.Unique { *; }
 -keep class org.spongepowered.asm.mixin.injection.SoftImplement { *; }
 -keep class org.spongepowered.asm.mixin.MixinEnvironment { *; }

 -ignorewarnings