package jp.yucchi.filechannelperformancetest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import static java.nio.file.StandardOpenOption.READ;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yucchi
 */
public class FileChannelPerformanceTest {

    private static File file;

    private static final boolean DEBUG = false;

    private static final int REPEAT = 50;
    private static long start;
    private static long time;

    public static void main(String[] args) {

        System.out.println("テスト用一時ファイル作成開始");

        // 効率の悪いファイル作成処理
        preparation();
        
        // ウォーミングアップ、処理速度計測処理
        for (int i = 0; i < REPEAT; i++) {
            fileInputStream();
        }

        start = System.nanoTime();
        fileInputStream();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  FileInputStream の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            bufferedInputStream();
        }

        start = System.nanoTime();
        bufferedInputStream();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  BufferedInputStream の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            fileChannelOpen();
        }

        start = System.nanoTime();
        fileChannelOpen();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  fileChannelOpen の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            fileChannelOpenDirectBuffer();
        }

        start = System.nanoTime();
        fileChannelOpenDirectBuffer();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  fileChannelOpenDirectBuffer の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            fileChannelByteBuffer();
        }

        start = System.nanoTime();
        fileChannelByteBuffer();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  fileChannelByteBuffer の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            fileChannelDirectByteBuffer();
        }

        start = System.nanoTime();
        fileChannelDirectByteBuffer();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  FileChannelDirectByteBuffer の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            fileChannelMappedByteBuffer();
        }

        start = System.nanoTime();
        fileChannelMappedByteBuffer();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  FileChannelMappedByteBuffer の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            fileChannelOpenMappedByteBuffer();
        }

        start = System.nanoTime();
        fileChannelOpenMappedByteBuffer();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  FileChannelOpenMappedByteBuffer の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());
        
        for (int i = 0; i < REPEAT; i++) {
            seekableByteChannel();
        }
        
        start = System.nanoTime();
        seekableByteChannel();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  SeekableByteChannel の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());

        for (int i = 0; i < REPEAT; i++) {
            seekableByteChannelDirectBuffer();
        }
        
        start = System.nanoTime();
        seekableByteChannelDirectBuffer();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  SeekableByteChannelDirectBuffer の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());
        
        for (int i = 0; i < REPEAT; i++) {
            filesReadAllBytes();
        }
        
        start = System.nanoTime();
        filesReadAllBytes();
        time = System.nanoTime() - start;
        System.out.println(System.lineSeparator() + "(○･ω･)ﾉ-------------  FilesReadAllBytes の読み込み時間は、"
                + (int) (time * 1e-9) / 3_600 + "時間"
                + (int) ((time * 1e-9) / 60) % 60 + "分"
                + (int) (time * 1e-9 % 60) + "秒"
                + Double.toString((time * 1e-9 % 60) % 1).substring(2) + "  ------------ )" + System.lineSeparator());
        
        // お掃除
        file.deleteOnExit();

        System.out.println("テスト終了！");

    }

    private static void preparation() {

        try {
            file = File.createTempFile("temp", ".txt");
        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("テスト用一時ファイルを " + Paths.get(file.getAbsolutePath()) + " に作成します。");

        String content = "JAVA + YOU, DOWNLOAD TODAY!  JAVA + YOU, DOWNLOAD TODAY!  JAVA + YOU, DOWNLOAD TODAY!" + System.lineSeparator();

        try (FileChannel fileChannel = new FileOutputStream(file.getAbsolutePath()).getChannel();
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, false)) {

            long currentPosition = 0L;
            byte[] bytes = content.getBytes();

            while (file.length() < 512 * 1_024 * 1024) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                currentPosition += fileChannel.write(byteBuffer, currentPosition);
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("テスト用一時ファイルのサイズは、" + file.length() + " バイトです。" + System.lineSeparator());

    }

    private static void fileInputStream() {

        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {

            final int SIZE = 8 * 1_024;
            byte[] byteArray = new byte[SIZE];
            int readPosition;
            long sum = 0L;

            while ((readPosition = fis.read(byteArray, 0, SIZE)) != -1) {
                for (int i = 0; i < readPosition; i++) {
                    sum += byteArray[i];
                }
            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void bufferedInputStream() {

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()))) {

            final int SIZE = 8 * 1_024;
            byte[] byteArray = new byte[SIZE];
            int readPosition;
            long sum = 0L;

            while ((readPosition = bis.read(byteArray, 0, SIZE)) != -1) {
                for (int i = 0; i < readPosition; i++) {
                    sum += byteArray[i];
                }
            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void fileChannelOpen() {

        try (FileChannel fileChannel = FileChannel.open(Paths.get(file.getAbsolutePath()), StandardOpenOption.READ);
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, true)) {

            final int CAPACITY = 1_000 * 1_024;

            ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);

            int readPosition;
            long sum = 0L;

            while ((readPosition = fileChannel.read(byteBuffer)) != -1) {

                if (readPosition == 0) {
                    continue;
                }

                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.get();
                }

                byteBuffer.clear();

            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void fileChannelOpenDirectBuffer() {

        try (FileChannel fileChannel = FileChannel.open(Paths.get(file.getAbsolutePath()), StandardOpenOption.READ);
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, true)) {

            final int CAPACITY = 1_000 * 1_024;

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(CAPACITY);

            int readPosition;
            long sum = 0L;

            while ((readPosition = fileChannel.read(byteBuffer)) != -1) {

                if (readPosition == 0) {
                    continue;
                }

                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.get();
                }

                byteBuffer.clear();

            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void fileChannelByteBuffer() {

        try (FileChannel fileChannel = new FileInputStream(file.getAbsolutePath()).getChannel();
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, true)) {

            final int CAPACITY = 1_000 * 1_024;

            ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);

            int readPosition;
            long sum = 0L;

            while ((readPosition = fileChannel.read(byteBuffer)) != -1) {

                if (readPosition == 0) {
                    continue;
                }

                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.get();
                }

                byteBuffer.clear();

            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void fileChannelDirectByteBuffer() {

        try (FileChannel fileChannel = new FileInputStream(file.getAbsolutePath()).getChannel();
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, true)) {

            final int CAPACITY = 1_000 * 1_024;

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(CAPACITY);

            int readPosition;
            long sum = 0L;

            while ((readPosition = fileChannel.read(byteBuffer)) != -1) {

                if (readPosition == 0) {
                    continue;
                }

                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.get();
                }

                byteBuffer.clear();

            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void fileChannelMappedByteBuffer() {

        try (FileChannel fileChannel = new FileInputStream(file.getAbsolutePath()).getChannel();
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, true)) {

            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

            long sum = 0L;

            while (mappedByteBuffer.hasRemaining()) {
                sum += mappedByteBuffer.get();
            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void fileChannelOpenMappedByteBuffer() {

        try (FileChannel fileChannel = FileChannel.open(Paths.get(file.getAbsolutePath()), StandardOpenOption.READ);
                FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, true)) {

            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

            long sum = 0L;

            while (mappedByteBuffer.hasRemaining()) {
                sum += mappedByteBuffer.get();
            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void seekableByteChannel() {
        // FileChannel にあえてキャストしないでみた。
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(Paths.get(file.getAbsolutePath()), EnumSet.of(READ))) {

            final int CAPACITY = 1_000 * 1_024;

            ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);

            int readPosition;
            long sum = 0L;

            while ((readPosition = seekableByteChannel.read(byteBuffer)) != -1) {

                if (readPosition == 0) {
                    continue;
                }

                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.get();
                }

                byteBuffer.clear();

            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void seekableByteChannelDirectBuffer() {
        // FileChannel にあえてキャストしないでみた。
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(Paths.get(file.getAbsolutePath()), EnumSet.of(READ))) {

            final int CAPACITY = 1_000 * 1_024;

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(CAPACITY);

            int readPosition;
            long sum = 0L;

            while ((readPosition = seekableByteChannel.read(byteBuffer)) != -1) {

                if (readPosition == 0) {
                    continue;
                }

                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.get();
                }

                byteBuffer.clear();

            }

            if (DEBUG) {
                System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
            }

        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static void filesReadAllBytes() {

        long sum = 0L;

        byte[] byteArray = null;

        try {
            byteArray = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException ex) {
            Logger.getLogger(FileChannelPerformanceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < byteArray.length; i++) {
            sum += byteArray[i];
        }

        if (DEBUG) {
            System.out.println("バイト配列の要素の値の合計は、" + sum + " です。" + System.lineSeparator());
        }

    }

}
