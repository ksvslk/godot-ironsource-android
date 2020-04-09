# Godot IronSource Android plugin

Godot Android plugin for IronSource SDK.

## Description

* Ad units supported: **BANNER, REWARDED_VIDEO, INTERSTITIAL**
* Ad networks (currently added): **AdMob, AdColony, AppLovin**

## Getting Started

### Dependencies

* Godot 3.2.1
* Android Build template

### Installing

1. Set up [custom build for Android](https://docs.godotengine.org/en/3.2/getting_started/workflow/export/android_custom_build.html). 
  *After that you should have ```android/build``` directory in your Godot project.*
2. Open the ```build``` directory in your favourite text editor.
3. Open ```AndroidManifest.xml```
  * **Insert** attribute ```tools:replace="label"``` inside ```<application>``` tag:
  
    ![AndroidManifest.xml](https://i.imgur.com/pHRCFJZ.png)
    
4. Open gradle.properties 
  * **Insert** ```android.useAndroidX=true``` and ```android.enableJetifier=true``` to the end:
  
    ![gradle.properties](https://i.imgur.com/EYimYpM.png)
    
5. Clone or download this plugin and place it inside ```android``` folder like so:

    ![godot-ironsource-android](https://i.imgur.com/17cQHRW.png)
    
6. *(Optional)* If using **AdMob**, then **insert** your APP ID to ```AndroidManifest.conf``` inside plugin folder
    
    ![AndroidManifest.conf](https://i.imgur.com/33yy2FR.png)
    
7. In Godot under ```Project Settings``` **add** the plugin to ```Modules```

    ![Modules](https://i.imgur.com/nlPJadn.png)


### Executing program

* How to run the program
* Step-by-step bullets
```
code blocks for commands
```

## Help

Any advise for common problems or issues.
```
command to run if program contains helper info
```

## License

This project is licensed under the [NAME HERE] License - see the LICENSE.md file for details

## Acknowledgments

Inspiration, code snippets, etc.
* [alexzheng](https://github.com/alexzheng/admob_for_godot)
* [Shin-NiL](https://github.com/Shin-NiL/godot-admob)
* [DomPizzie](https://gist.github.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc)
