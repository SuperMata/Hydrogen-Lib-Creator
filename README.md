# Hydrogen-Lib-Creator
Java application for creating Hydrogen Drum kit library
## What you need to know.
I have provided both the source code and a bundled executable file with the jre.
The application was built on java 1.8 if you wish to build it yourself use the source and any java IDE you like.
## How it works
The application takes a folder with audio files; creates a copy of the same folder; creates a <b>drumkit.xml</b> file inside the new folder and adds audio entries of the audio files in the drumkit.xml file. You can also add editional details of the libray these include the name and licence information.

The new folder with the drumkit.xml will be named in the format < <b> Foldername__HLIB </b> > i.e suffixed with < <b>_HLIB</b> >.
Once it's done you will need to copy the folder to where your Hydrogen install stores libraries.Should be in this format:<b> /home/username/.hydrogen/data/drumkits/ </b>

<b>Note:</b> You will need to restart Hydrogen if you have it running at the time of the above otherwise start Hydrogen and go to user libraries. Your new library should be loaded.

##What's missing
<ul>
<li>Would like to ad the ability to copy the library automatically to the .hydrogen folder once it's been created.</li>
<li>Currently there should only be audio files in the folder to be converted otherwise the application wont work as it should.That's something to fix.
</li>
</ul>
Hope you find this useful :)
