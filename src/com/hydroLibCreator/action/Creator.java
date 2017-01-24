package com.hydroLibCreator.action;

/**
 * Created by SuperMata on 2016/12/23.
 */

import com.hydroLibCreator.exception.ApplicationException;
import com.hydroLibCreator.model.AudioLibrary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Creator {
    private Document document;
    private String destinationPath;
    private File sourceDir;
    private File destinationDir;
    private List<String> audioFileslist;

    private AudioLibrary audioLibrary;

    public Creator(AudioLibrary audioLibrary){

        this.audioLibrary = audioLibrary;
        sourceDir = new File(audioLibrary.getDirectorypath());
        if(!sourceDir.exists())
            throw new ApplicationException("No such directory exists", "please make sure you have the correct path to the library");

        destinationDir = this.newDestinationDir(sourceDir.getName());
        this.destinationPath = destinationDir.getPath();

        audioLibrary.setNewLibPath(this.destinationPath);

    }

    public void createLibrary(){

        this.setSortedAudioFileslist();
        this.copyAudioFiles();
        this.buildDrumKitXML();

    }

    private void setSortedAudioFileslist(){

        this.audioFileslist = FileUtils.listFiles(sourceDir, this.audioExtensionFilter(), null)
            .stream()
            .map(File::getName)
            .sorted()
            .collect(Collectors.toList());

        if (audioFileslist == null || audioFileslist.isEmpty())
            throw new ApplicationException("Missing Audio Files Exception", "Please make sure your directory has audio files");

    }


    private void copyAudioFiles(){

        try {

            FileUtils.copyDirectory(sourceDir, destinationDir, this.audioExtensionFilter());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void buildDrumKitXML(){

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException parserException) {
            parserException.printStackTrace();
        }

        Element drumkit_info = document.createElement("drumkit_info");
        document.appendChild(drumkit_info);

        Attr xmlns = document.createAttribute("xmlns");
        xmlns.setValue("http://www.hydrogen-music.org/drumkit");

        Attr xmlns_xsi = document.createAttribute("xmlns:xsi");
        xmlns_xsi.setValue("http://www.w3.org/2001/XMLSchema-instance");


        drumkit_info.setAttributeNode(xmlns);
        drumkit_info.setAttributeNode(xmlns_xsi);


        Node nameNode = createNameNode(document);
        drumkit_info.appendChild(nameNode);

        Node authorNode = createAuthorNode(document);
        drumkit_info.appendChild(authorNode);

        Node infoNode = createInfoNode(document);
        drumkit_info.appendChild(infoNode);

        Node licenseNode = createLicenseNode(document);
        drumkit_info.appendChild(licenseNode);

        Node instrumentListNode = createInstrumentListNode(document);
        drumkit_info.appendChild(instrumentListNode);

        // write the XML document to disk
        try {

            // create DOMSource for source XML document
            Source xmlSource = new DOMSource(document);

            // create StreamResult for transformation result
            Result result = new StreamResult(new FileOutputStream(destinationPath+"/drumkit.xml"));

            // create TransformerFactory
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // create Transformer for transformation
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // transform and deliver content to client
            transformer.transform(xmlSource, result);
        }

        // handle exception creating TransformerFactory
        catch (TransformerFactoryConfigurationError factoryError) {
            System.err.println("Error creating " + "TransformerFactory");
            factoryError.printStackTrace();
        } catch (TransformerException transformerError) {
            System.err.println("Error transforming document");
            transformerError.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private Node createNameNode(Document document){
        Element name = document.createElement("name");
        name.setTextContent(audioLibrary.getName());
        return name;
    }

    private Node createAuthorNode(Document document){
        Element author = document.createElement("author");
        author.setTextContent(audioLibrary.getAuthor());
        return author;
    }

    private Node createInfoNode(Document document){
        Element info = document.createElement("info");
        info.setTextContent(audioLibrary.getInfo());
        return info;
    }

    private Node createLicenseNode(Document document){
        Element license = document.createElement("license");
        license.setTextContent(audioLibrary.getLicense());
        return license;
    }

    private Node createLayerNode(Document document, String audioFilename){

        Element filename = document.createElement("filename");
        filename.setTextContent(audioFilename);

        Element min = document.createElement("min");
        min.setTextContent("0");

        Element max = document.createElement("max");
        max.setTextContent("1");

        Element gain = document.createElement("gain");
        gain.setTextContent("1");

        Element pitch = document.createElement("pitch");
        pitch.setTextContent("0");

        Element layer = document.createElement("layer");
        layer.appendChild(filename);
        layer.appendChild(min);
        layer.appendChild(max);
        layer.appendChild(gain);
        layer.appendChild(pitch);


        return layer;
    }

    private Node createInstrumentNode(Document document,String audioFilename,String index){

        String noExtentionName = audioFilename.substring(0,audioFilename.indexOf("."));

        Element id = document.createElement("id");
        id.setTextContent(index);

        Element name = document.createElement("name");
        name.setTextContent(noExtentionName);

        Element volume = document.createElement("volume");
        volume.setTextContent("1");

        Element isMuted = document.createElement("isMuted");
        isMuted.setTextContent("false");

        Element pan_L = document.createElement("pan_L");
        pan_L.setTextContent("1");


        Element pan_R = document.createElement("pan_R");
        pan_R.setTextContent("1");

        Element randomPitchFactor = document.createElement("randomPitchFactor");
        randomPitchFactor.setTextContent("0");

        Element gain = document.createElement("gain");
        gain.setTextContent("1");

        Element filterActive = document.createElement("filterActive");
        filterActive.setTextContent("true");

        Element filterCutoff = document.createElement("filterCutoff");
        filterCutoff.setTextContent("1");

        Element filterResonance = document.createElement("filterResonance");
        filterResonance.setTextContent("0");

        Element attack = document.createElement("Attack");
        attack.setTextContent("0");

        Element decay = document.createElement("Decay");
        decay.setTextContent("0");

        Element sustain = document.createElement("Sustain");
        sustain.setTextContent("1");

        Element release = document.createElement("Release");
        release.setTextContent("1000");


        Element muteGroup = document.createElement("muteGroup");
        muteGroup.setTextContent("-1");

        Element midiOutChannel = document.createElement("midiOutChannel");
        midiOutChannel.setTextContent("-1");

        Element midiOutNote = document.createElement("midiOutNote");
        midiOutNote.setTextContent("60");



        Element isStopNote = document.createElement("isStopNote");
        isStopNote.setTextContent("false");

        Element fx1Level = document.createElement("FX1Level");
        fx1Level.setTextContent("0");

        Element fx2Level = document.createElement("FX2Level");
        fx2Level.setTextContent("0");

        Element fx3Level = document.createElement("FX3Level");
        fx3Level.setTextContent("0");

        Element fx4Level = document.createElement("FX4Level");
        fx4Level.setTextContent("0");

        Node layer = createLayerNode(document,audioFilename);

        Element instrument = document.createElement("instrument");
        instrument.appendChild(id);
        instrument.appendChild(name);
        instrument.appendChild(volume);
        instrument.appendChild(isMuted);
        instrument.appendChild(pan_L);
        instrument.appendChild(pan_R);
        instrument.appendChild(randomPitchFactor);
        instrument.appendChild(gain);
        instrument.appendChild(filterActive);
        instrument.appendChild(filterCutoff);
        instrument.appendChild(filterResonance);
        instrument.appendChild(attack);
        instrument.appendChild(decay);
        instrument.appendChild(sustain);
        instrument.appendChild(release);
        instrument.appendChild(muteGroup);
        instrument.appendChild(midiOutChannel);
        instrument.appendChild(midiOutNote);
        instrument.appendChild(isStopNote);
        instrument.appendChild(fx1Level);
        instrument.appendChild(fx2Level);
        instrument.appendChild(fx3Level);
        instrument.appendChild(fx4Level);
        instrument.appendChild(layer);

        return instrument;
    }


    private Node createInstrumentListNode(Document document){
        Element instrumentList = document.createElement("instrumentList");

        int index = 0;
        for (String filename:audioFileslist) {

            Node instrumentNode = createInstrumentNode(document,filename,index+"");
            instrumentList.appendChild(instrumentNode);
            index++;

        }


        return instrumentList;
    }

    private IOFileFilter audioExtensionFilter() {
        List audioFileExtensions = Arrays.asList(".wav", ".flac");
        return new SuffixFileFilter(audioFileExtensions);
    }

    private File newDestinationDir(String drumKitName) {
        String destinationDirectory = System.getProperty("user.home")
            .concat("/.hydrogen/data/drumkits/")
            .concat(drumKitName)
            .concat("_HLIB");

        return new File(destinationDirectory);
    }
}