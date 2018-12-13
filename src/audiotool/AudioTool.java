/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotool;

import audiotool.analysis.FFT;
import audiotool.io.MP3Decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 *
 * @author MSi-Gaming
 */
public class AudioTool {

    private static List<BandRateObject> BAND_RATE;

    public AudioTool() {
        try {
            BAND_RATE = getListBandRate();
        } catch (IOException ex) {
            Logger.getLogger(AudioTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<BandRateObject> getListBandRate() throws IOException {
        URL url = getClass().getResource("bandRate.dat");
//        File file = new File(url.getPath());
        File file = new File("bandRate.dat");
        List<BandRateObject> results = new ArrayList<>();
        List<String> resources;
        resources = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        String[] values;
        for (String value : resources) {
            values = value.split(" ");
            if (values.length < 2) {
                continue;
            }
            results.add(new BandRateObject(values[0], convertStringToDouble(values[1])));
        }
        return results;
    }

    public static double convertStringToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    public interface OnAudioProcessListener {

        void onSuccess(String file);

        void onFail(String msg);
    }

    private OnAudioProcessListener onAudioProcessListener;

    private Thread mMainThread;
    private List<File> files;

    public void processAudioFiles(List<File> files, OnAudioProcessListener listener) {
        if (listener == null) {
            return;
        }
        this.files = files;
        onAudioProcessListener = listener;
        mMainThread = new Thread(mainRunable);
        mMainThread.start();
    }

    private Runnable mainRunable = new Runnable() {
        @Override
        public void run() {
            for (File file : files) {
                runProcessFile(file);
            }
            mMainThread.stop();
        }
    };

    private void runProcessFile(File path) {
        long timeShift = System.currentTimeMillis();
        try {
            MP3Decoder decoder = new MP3Decoder(path.getPath());
            FFT fft = new FFT(1024, decoder.getSampleRate());
            onAudioProcessListener.onSuccess(path.getName() + " Dịch thành công: " + processAudio(decoder, fft, path));
            System.out.println("Time: " + (System.currentTimeMillis() - timeShift));
        } catch (Exception ex) {
            System.err.println("Ex: " + ex.toString());
            onAudioProcessListener.onFail(path.getName() + " Lỗi: " + ex.toString());
        }
    }

    private String processAudio(MP3Decoder decoder, FFT fft, File file) throws FileNotFoundException, UnsupportedEncodingException {
        float[] samples = new float[1024];
        float[] realPart = new float[1024 / 2 + 1];
        float[] imaPart = new float[1024 / 2 + 1];
        double[] magnitudeFFTData = new double[1024 / 2 + 1];

        List<Float> frequencyList = new ArrayList<>();
        int count = 0;
        while (decoder.readSamples(samples) > 0) {
            fft.forward(samples);
            count++;
            System.arraycopy(fft.getImaginaryPart(), 0, imaPart, 0, imaPart.length);
            System.arraycopy(fft.getRealPart(), 0, realPart, 0, realPart.length);

            for (int i = 0; i < imaPart.length; i++) {
                float real = realPart[i];
                float ima = imaPart[i];
                magnitudeFFTData[i] = Math.sqrt((real * real) + (ima * ima));
            }

            int maxIndex = 0;
            double maxValue = magnitudeFFTData[0];
            for (int i = 1; i < magnitudeFFTData.length; i++) {
                if (maxValue > magnitudeFFTData[i]) {
                    continue;
                }
                maxValue = magnitudeFFTData[i];
                maxIndex = i;
            }
            float freq = maxIndex * decoder.getSampleRate() / 1024.0F;
            frequencyList.add(freq);
        }

        count -= 1;
        frequencyList.remove(0);

        double timeTheshold = decoder.getDuration() * 1.0F / count;
        System.out.println("Count: " + count);
        System.out.println("TimeThreshold: " + timeTheshold);

        int startTime = 0;
        String currentLine = "";
        int noiseCount = 0;

        String lrcPath = file.getPath().replace(".mp3", ".lrc");
        PrintWriter writer = new PrintWriter(lrcPath, "UTF-8");

        for (int i = 0; i < count - 1; i++) {
            BandRateObject currentBand = musicalNote(frequencyList.get(i));
            System.out.println("band: " + currentBand);
            if (((currentBand != musicalNote(isNextValue(i + 1, frequencyList))) || i == count - 2) && noiseCount > 1) {
                currentLine = "[" + getTimeFromMillis(startTime) + "-" + getTimeFromMillis((long) (timeTheshold * i)) + "]" + currentBand.getName();
                startTime = (int) (timeTheshold * i);
                System.out.println(currentLine);
                writer.println(currentLine);
                noiseCount = 0;
            } else {
                noiseCount++;
            }
        }
        writer.close();
        return lrcPath;
    }

    private float isNextValue(int index, List<Float> floats) {
        return valueBetween(0, floats.size() - 1, index) ? floats.get(index) : -1;
    }

    private BandRateObject musicalNote(float freq) {
        for (BandRateObject object : BAND_RATE) {
            if (valueBetween(minValueBand(object), maxValueBand(object), freq)) {
                return object;
            }
        }
        return new BandRateObject("NONE", 0);
    }

    private double minValueBand(BandRateObject bandRateObject) {
        int index = BAND_RATE.indexOf(bandRateObject);
        return index < 1 ? (bandRateObject.getValue() - valueLevel())
                : (bandRateObject.getValue() + BAND_RATE.get(index - 1).getValue()) / 2;
    }

    private double maxValueBand(BandRateObject bandRateObject) {
        int index = BAND_RATE.indexOf(bandRateObject);
        return index >= (BAND_RATE.size() - 1) ? (bandRateObject.getValue() + valueLevel())
                : (bandRateObject.getValue() + BAND_RATE.get(index + 1).getValue()) / 2;
    }

    private double valueLevel() {
        return Math.pow(2, 1.0f / 12.0f);
    }

    private boolean valueBetween(double min, double max, double value) {
        return value <= max && value >= min;
    }

    public String getTimeFromMillis(long time) {
        int h, m, s;
        h = m = s = 0;
        s = (int) (time / 1000);
        if (s >= 60) {
            m = s / 60;
            s = s % 60;
        }

        if (m >= 60) {
            h = m / 60;
            m = m % 60;
        }
        if (h != 0) {
            return String.format("%02d:%02d:%02d.%03d", h, m, s, time % 1000);
        }

        return String.format("00:%02d:%02d.%03d", m, s, time % 1000);
    }

    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    private List<Note> processMIDI(File file) throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(file);
        System.out.println("Resolution: " + sequence.getResolution() + ", MicrosecondLength: " + sequence.getMicrosecondLength());
        List<Note> notes = new ArrayList<>();
        int trackNumber = 0;
        float millisecondPerTick = sequence.getMicrosecondLength();
        long maxTick = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            int noteSize = 0;
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                Note noteProcess = new Note();
                String tickieTime = "@" + event.getTick() + " ";
                noteProcess.setStickTime(event.getTick());
                maxTick = maxTick >= event.getTick() ? maxTick : event.getTick();
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    tickieTime += "Channel: " + sm.getChannel() + " ";
                    switch (sm.getCommand()) {
                        case NOTE_ON: {
                            noteSize++;
                            int key = sm.getData1();
                            int octave = (key / 12) - 1;
                            int note = key % 12;
                            String noteName = NOTE_NAMES[note];
                            int velocity = sm.getData2();
                            noteProcess.setName(noteName);
                            noteProcess.setKey(key);
                            noteProcess.setNoteState(NOTE_ON);
                            noteProcess.setOctave(octave);
                            noteProcess.setVelocity(velocity);
                            notes.add(noteProcess);
                            System.out.println(tickieTime + "Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                            break;
                        }
                        case NOTE_OFF: {
                            int key = sm.getData1();
                            int octave = (key / 12) - 1;
                            int note = key % 12;
                            String noteName = NOTE_NAMES[note];
                            int velocity = sm.getData2();
                            noteProcess.setName(noteName);
                            noteProcess.setKey(key);
                            noteProcess.setNoteState(NOTE_OFF);
                            noteProcess.setOctave(octave);
                            noteProcess.setVelocity(velocity);
                            notes.add(noteProcess);
                            System.out.println(tickieTime + "Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                            break;
                        }
                        default:
                            System.out.println("Command:" + sm.getCommand());
                            break;
                    }
                } else if (message instanceof MetaMessage) {
                    MetaMessage metaMessage = (MetaMessage) message;
                    switch (metaMessage.getType()) {
                        case 81:
                            byte[] data = metaMessage.getData();
                            int tempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
                            int bpm = 60000000 / tempo;
                            break;
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }
            System.out.println("Notes: " + noteSize);
            System.out.println();
        }
        String lrcPath = file.getPath().replace(".mid", ".lrc");
        PrintWriter writer = new PrintWriter(lrcPath, "UTF-8");

        notes = sortListNote(notes);
        List<NoteFull> noteFulls = mergeNote(notes);
        noteFulls = clearNotes(noteFulls);
        float timePerTick = millisecondPerTick / maxTick;
        for (NoteFull note : noteFulls) {
            note.getStartNote().setStickTime((long) (timePerTick * note.getStartTime()) / 1000);
            note.getEndNote().setStickTime((long) (timePerTick * note.getEndTime()) / 1000);
            writer.println(makeSquareSymbol(getTimeFromStamp(note.getStartTime()) + "-"
                    + getTimeFromStamp(note.getEndTime()))
                    + (note.getStartNote().getName()) + " " + note.getStartNote().getKey());
        }
        writer.close();

//        makeLyricFromMid(file, noteFulls, LYRIC_TEST);
        return notes;
    }

//    private List<NoteFull> removeNotes(List<NoteFull> noteFulls) {
//        List<NoteFull> result = new ArrayList<>();
//        noteFulls.stream().filter((note) -> (note.getEndTime() - note.getStartTime() > 0)).forEachOrdered((note) -> {
//            result.add(note);
//        });
//        return result;
//    }
    public void processFilesMIDI(List<File> files) {
        try {
            for (File file : files) {
                processMIDI(file);
            }
        } catch (InvalidMidiDataException | IOException midiDataException) {
            System.err.println(midiDataException.toString());
        }
    }

    public List<Note> sortListNote(List<Note> notes) {
        for (int i = 0; i < notes.size(); i++) {
            notes = findNoteOff(notes.get(i), notes);
        }
        return notes;
    }

    public List<NoteFull> mergeNote(List<Note> notes) {
        List<NoteFull> resultFulls = new ArrayList<>();
        for (int i = 0; i < notes.size(); i += 2) {
            resultFulls.add(new NoteFull(notes.get(i), notes.get(i + 1)));
        }
        return resultFulls;
    }

    public List<NoteFull> clearNotes(List<NoteFull> notes) {
        List<NoteFull> result = new ArrayList<>(notes);
        for (int i = 1; i < notes.size(); i++) {
            if (notes.get(i).getStartNote().getStickTime() < notes.get(i - 1).getEndNote().getStickTime()) {
                notes.get(i - 1).getEndNote().setStickTime(notes.get(i).getStartNote().getStickTime());
            }
        }
        return result;
    }

    private List<Note> findNoteOff(Note note, List<Note> notes) {
        if (note.getNoteState() == NOTE_OFF) {
            return notes;
        }
        int index = notes.indexOf(note);
        for (int i = index + 1; i < notes.size(); i++) {
            if (note.getKey() == (notes.get(i).getKey()) && notes.get(i).getNoteState() == NOTE_OFF) {
                Note tmpSwap = notes.get(i);
                notes.remove(i);
                notes.add(index + 1, tmpSwap);
                return notes;
            }
        }
        return notes;
    }

    private String getTimeFromStamp(long time) {
        float second = time * 1.0F / 1000;
        int minutes = (int) (second / 60);
        second = second % 60;
        String secondText = String.format("%02.03f", second);
        return String.format("%02d:" + (second < 10 ? "0" + secondText : secondText), minutes);
    }

    private void makeLyricFromMid(File file, List<NoteFull> notes, String content) throws FileNotFoundException, UnsupportedEncodingException {
        if (content == null || content.length() == 0) {
            return;
        }
        String lrcPath = file.getPath().replace(".mid", "-lyric.lrc");
        PrintWriter writer = new PrintWriter(lrcPath, "UTF-8");
        String[] contents = content.trim().split("\n");
        int currentCursor = 0;
        for (String line : contents) {
            if (line.trim().equals("")) {
                continue;
            }
            String[] words = makeStringValidate(line.trim()).split(" ");
            String lineLyric = processLineLyric(currentCursor, words, notes);
            writer.println(lineLyric);
            currentCursor += words.length;
        }
        writer.close();
    }

    private String processLineLyric(int currentCursor, String[] words, List<NoteFull> notes) {
        String result = makeSquareSymbol(getTimeFromStamp(notes.get(currentCursor).getStartTime()))
                + words[0] + " "
                + makeNotSquareSymbol(getTimeFromStamp(notes.get(currentCursor).getEndTime()));
        System.out.println(currentCursor + " " + words[0]);
        for (int i = 1; i < words.length; i++) {
            currentCursor++;
            if (currentCursor >= notes.size()) {
                continue;
            }
            System.out.println(currentCursor + " " + words[i]);
            result += words[i] + " " + makeNotSquareSymbol(getTimeFromStamp(notes.get(currentCursor).getEndTime()));
        }
        return result;
    }

    private String makeSquareSymbol(String content) {
        return "[" + content + "]";
    }

    private String makeNotSquareSymbol(String content) {
        return "<" + content + ">";
    }

    private String makeStringValidate(String content) {
        String oldContent;
        do {
            oldContent = content;
            content = content.replace("  ", " ");
        } while (oldContent.length() != content.length());
        return content;
    }

    private static String LYRIC_TEST = "Mưa trôi cả bầu Trời nắng, \n"
            + "trượt theo những nỗi buồn \n"
            + "Thấm ướt lệ sầu môi đắng\n"
            + "vì đánh mất hy vọng \n"
            + "Lần đầu gặp nhau dưới mưa, \n"
            + "trái tim rộn ràng bởi ánh nhìn \n"
            + "Tình cảm dầm mưa thấm lâu, \n"
            + "em nào ngờ... \n"
            + "\n"
            + "Mình hợp nhau đến như vậy\n"
            + "thế nhưng... không phải là yêu! \n"
            + "Và em muốn hỏi anh rằng \n"
            + "chúng ta là thế nào?\n"
            + "Rồi... lặng người đến vô tận,\n"
            + "trách sao được sự tàn nhẫn \n"
            + "Anh trót vô tình.. \n"
            + "thương em như là em gái... \n"
            + "\n"
            + "Đừng lo lắng về \n"
            + "em khi mà em... \n"
            + "vẫn còn yêu anh \n"
            + "Càng xa lánh, càng trống vắng... \n"
            + "tim cứ đau và nhớ lắm... \n"
            + "Đành phải buông hết tất cả thôi, \n"
            + "nụ cười mỉm sau bờ môi \n"
            + "Ấm áp dịu dàng vai anh,\n"
            + "em đã bao lần yên giấc... \n"
            + "\n"
            + "Nhìn trên cao \n"
            + "khoảng Trời yêu \n"
            + "mà em lỡ dành cho anh, \n"
            + "Giờ mây đen quyện thành bão, \n"
            + "giông tố đang dần kéo đến \n"
            + "Chồi non háo hức đang đợi mưa, \n"
            + "rất giống em ngày xưa \n"
            + "Mưa trôi để lại ngây thơ, \n"
            + "trong giấc mơ buốt lạnh \n"
            + "\n"
            + "Mình hợp nhau đến như vậy\n"
            + "thế nhưng... không phải là yêu! \n"
            + "Và em muốn hỏi anh rằng \n"
            + " chúng ta là thế nào?\n"
            + " Rồi... lặng người đến vô tận,\n"
            + "trách sao được sự tàn nhẫn \n"
            + "Anh trót vô tình.. \n"
            + "thương em như là em gái... \n"
            + "\n"
            + "Đừng lo lắng \n"
            + "về em khi mà em... \n"
            + "vẫn còn yêu anh \n"
            + "Càng xa lánh, càng trống vắng... \n"
            + "tim cứ đau và nhớ lắm... \n"
            + "Đành phải buông hết tất cả thôi, \n"
            + "nụ cười mỉm sau bờ môi \n"
            + "Ấm áp dịu dàng vai anh, \n"
            + "em đã bao lần yên giấc... \n"
            + "\n"
            + "Nhìn trên cao \n"
            + "khoảng Trời yêu \n"
            + "mà em lỡ dành cho anh, \n"
            + "Giờ mây đen quyện thành bão,\n"
            + "giông tố đang dần kéo đến \n"
            + "Chồi non háo hức đang đợi mưa, \n"
            + "rất giống em ngày xưa \n"
            + "Mưa trôi để lại ngây thơ,\n"
            + "trong giấc mơ buốt lạnh \n"
            + "\n"
            + "Đừng lo lắng về em khi mà em... \n"
            + "vẫn còn yêu anh \n"
            + "Càng xa lánh, càng trống vắng... \n"
            + "tim cứ đau và nhớ lắm... \n"
            + "Đành phải buông hết tất cả thôi, \n"
            + "nụ cười mỉm sau bờ môi\n"
            + "Ấm áp dịu dàng vai anh, \n"
            + "em đã bao lần yên giấc... \n"
            + "\n"
            + "Nhìn trên cao khoảng Trời yêu\n"
            + "mà em lỡ dành cho anh, \n"
            + "Giờ mây đen quyện thành bão, \n"
            + "giông tố đang dần kéo đến \n"
            + "Chồi non háo hức đang đợi mưa, \n"
            + "rất giống em ngày xưa \n"
            + "Mưa trôi để lại ngây thơ, \n"
            + "trong giấc mơ buốt lạnh";
}
