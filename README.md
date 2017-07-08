Android Utilities
=============

Library Project for utility classes that can make me(and others) more productive.
AndroidUtils requires Android 4.0+.


**Utility classes included**

* Utils - Set of general purpose utility methods.
* FileUtils - Set of general purpose utility methods for file operation.
* MediaUtils - Set of utilities to handle media resize, scaling, rotation and other relevant stuff.
* ViewUtils - Set of utilities to handle Android Views' related stuff.
* ImageUtils - Set of utilities to handle image manipulation.
* AudioUtils - Set of utilities to handle audio recording, playing and saving to disk.
* DateUtils - Set of utilities to handle date manipulation.
* StorageManager - Android SharedPreferences abstraction.
* YouTubeUtils - Set of utilities to handle YouTube related stuff.

----

Download
--------
Gradle:
```groovy
compile 'com.github.jaydeepw:android-utils:3.0.6'
```

Donations!
==========
* **Using Bitcoins**: If this project has helped you understand issues, be productive by using this library in your app or just being nice with me, you can always donate me Bitcoins at this address `3QJEmgqXsT1CFLtURYWxzmww59DdKYVwNk`

* **Using Paypal**: [Pay Jay](https://www.paypal.me/jaydeepw)


Changelog
============

## v3.0.6

Method count: 506

JAR size: 46KB

* Fix memory leak when calling methods in Utils class.

## v3.0.4

Method count: 506

JAR size: 46KB

* fix [#2](https://github.com/jaydeepw/android-utils/issues/2). Overload showAlert/showDialog methods with resId parameter
* fix [#12](https://github.com/jaydeepw/android-utils/issues/12). Use try catch and return boolean in sharePreferenceManger#clear method.
* fix [#11](https://github.com/jaydeepw/android-utils/issues/11). Add file list utils method to FileUtils class.

## v3.0.2

Method count: 11837

JAR size: 56KB

* fix [#10](https://github.com/jaydeepw/android-utils/issues/10). Remove unnecessary app_icon and strings.

## v3.0.1

* add hideKeyboard() to programmatically hide keyboard in the UI
* add setTextValues() and getNullEmptyCheckedValue() methods to help displaying text without worrying about null value check in the UI
* add method capitalizeString() to completely or partially capitalize the characters in the string
* add method toBold() to partially or completely typeface a string as bold
* add crypto helper methods getSha512Hash(String) and getSha512Hash(byte[]) to generate SHA hash
* add getExtension() for a file helper method
* add isBuildBelow() to quickly compare agains given Android API level


The MIT License
=============

**Copyright (c) 2016 Jaydeep**

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
