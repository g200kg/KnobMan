# KnobMan

This is the code source of JKnobMan.

The original source code was lost after the release of JKnobMan 1.3.3, this is
a recovery based on decompilation. It is fairly complete, but may lack comments
and source documentation.

---

Copyright (c) 2012-2020 [g200kg]

![Knobs Image](res/Samples/BlueDot_V.png)

## What is this?

**JKnobMan** is a 'Knob' graphic image design tool.\
The image strip has continuous frames stitched vertical, stitched horizontal or
individual files. Though **JKnobMan** is mainly designed for VST-plugin GUI
development, it may be useful if you need image-strips like this.

**JKnobMan** is distribuited under the [MIT] license.\
Donations are appreciated if you would like.

<http://www.g200kg.com/en/software/knobman.html> (English)\
<http://www.g200kg.com/jp/software/knobman.html> (Japanese)

Runnable on Java environment (Win/Mac/Linux)\
At first, confirm the Java Runtime Environment (JRE) is installed.
JRE7 or later is recommended.\
<http://www.java.com/>

---

## How to use

The detail of usage is here

<http://www.g200kg.com/en/docs/jknobman/>

---

## History

- 2016/10/05 1.3.3
      - Bugfix : Exe version cannot run with Java 8 update 101 (Windows).
    - Bugfix : Gif animation export image is corrupted with Java 8 update 101.
    - BugFix : Layer preview behavior with frame mask is not normal.
    - Frame Bit-Mask is now evenly assigned to frame length regardless of preview/render mode.
    - Language Ukrainian configuration file is added (may not be perfect).
- 2013/11/07 1.3.2
    - Bugfix : Texture setting may be lost after save/load in some case.
- 2013/10/11 1.3.1
    - .knob-file format is changed for reducing size
    - (backword compatible: note that the file saved with 1.3.1 is loadable only for 1.3.1 or later)
    - Thumbnails for KnobBrowser size is changed to reflect the oversampling setting
    - Rotation CenterX/CenterY display on layer preview
    - Improved UI response if large output image size
- 2013/10/05 1.3.0
    - Fix : Overwrite prompt may appear twice in some case
    - Thumbnails on texture select dropdown
    - Some more textures are added
    - Transparency is enabled on texture when alpha-PNG is used as texture file
    - Improved interpolation for texture zooming
    - Added magnifier with color pipette tool
    - Exe warpping tool is changed to Launch4j
- 2013/09/26 1.2.9
    - Color value editing with hex string
    - Added some Textures (Thanks to [AZ])
    - Use previous extension when export if no extension is specified
    - SVG import/export for shape editor
    - Frame mask mode 'Maskbits' is added
    - Enable value editing from keyboard in AnimCurve editor
- 2013/09/18 1.2.8
    - Editing is immediately reflect to 'Test' floating icon
    - Remember individually the folders for knob-files and image-files on file dialog
    - Adding status bar
    - Delete 'Export Done' dialog, that may cause hung up in some case.
    - Fix : The 'Recent Files' cannot work normal on Japanese kanji char folder-name
- 2013/05/02 1.2.7
    - Fix : illegal behavior on complex DynamicText e.g. '(99:1),<C>(1:99)' (enbugged in 1.2.6)
- 2013/04/23 1.2.6
    - Fix : Dynamix text freezing in some case
- 2013/03/05 Ver.1.2.5
    - Expand parameter max-value of offset/angle
    - Foldable effect parameters
- 2012/12/26 Ver.1.2.4
    - BugFix: Japanese setting is forced on .dmg version
    - BugFix: Specific RGB color value cannot be set
    - BugFix: Center offset when odd pixel size knob
    - configuration for Dutch (nl.ini) is added. (Thanks to Sjoerd Bijleveld)
- 2012/07/06 Ver.1.2.3
    - Number of frames max limitation is expanded to 1000
- 2012/05/11 Ver.1.2.2
    - BugFix: Anti-aliasing is not work properly when Zoom< about 20%
    - BugFix: Export result become not completed image in some cases.
    - BugFix: Crush on some illegal dynamic text syntax in the 'Text' primitive
    - BugFix: Cannot launch normally from .knob association (exe version)
    - Help menu is added
- 2012/05/04 Ver 1.21
    - Image primitive without 'auto-fit' behavior is adjusted to KnobMan
    - improved image primitive / oversampling decimation
    - fixed launch error in some environment ( japanese windows).
    - dmg packaged version is prepared for mac
    - and many minor bug fixes / expanded localizable parameters
- 2012/04/25 Ver 1.20
    - full localizable GUI parts
    - direct file drop to the 'Image' primitve's filename field
    - number of animation curve and curve points are expanded
    - improved oversampling decimation algo generating better results
    - other minor bug-fixes
- 2012/04/22 Ver.1.10
    - performance improved
    - support Undo / Redo
    - export to Animation (AnimGIF / APNG)
    - multi-language suport
    - menu, mnemonic, shortcuts support & customaizable
    - texture-image direct drop support
    - switchable look&feel / language from file-config
    - Texture folder should be placed to same folder to JKnobMan.jar
    - image files can be dropped directly to 'Texture-combobox' from explorer / finder.
    - menu mnemonic / shortcuts are custmizable.
- 2012/04/16 Ver.1.00
    - First Release.Based on KnobMan (windows-native) 1.50

[g200kg]: https://www.g200kg.com/
[MIT]:    LICENSE
[AZ]:     https://bji.yukihotaru.com/
