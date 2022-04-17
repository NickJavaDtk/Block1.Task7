package Study.streamIO.task3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    static int i = 0;
    public static void main(String[] args) {
        GameProgress archer = new GameProgress(12, 13, 17, 24.2);
        GameProgress warrior = new GameProgress(22, 9, 17, 7.1);
        GameProgress wizard = new GameProgress(15, 18, 17, 17.6);
        String path = "D:/Users/GitTest/Study/Block1/streamIO/Games/savegames/save";
        String pathZip = "D:/Users/GitTest/Study/Block1/streamIO/Games/savegames/save.zip";
        String pathDirectory = "D:/Users/GitTest/Study/Block1/streamIO/Games/savegames/";
        List<GameProgress> listGames = new ArrayList<>( );
        listGames.add(archer);
        listGames.add(warrior);
        listGames.add(wizard);

        for (GameProgress listGame : listGames) {
            saveGame(listGame, Main.getGenerationString(path));
        }

        addSlowInitialization(pathZip, pathDirectory);
       // deleteSaveFiles(pathDirectory);


        openZip(pathZip, pathDirectory);
      for( GameProgress game : openProgress(pathDirectory)) {
          System.out.println(game );
        }

    }

    public static void openZip(String pathZip, String pathDirectory) {
        try (FileInputStream fis = new FileInputStream(pathZip);
             ZipInputStream zip = new ZipInputStream(fis)) {
            ZipEntry entry;
            String tmpName;
            while ((entry = zip.getNextEntry()) != null) {
                tmpName = pathDirectory + entry.getName();
                FileOutputStream fos = new FileOutputStream(tmpName);
                for (int i = zip.read( ); i != -1; i = zip.read()) {
                    fos.write(i);
                }
                fos.flush();
                zip.closeEntry();
                fos.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<GameProgress> openProgress(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<GameProgress> gameList = new ArrayList<>(  );
        for (File fileList : files) {
            String tmpfileList = String.valueOf(fileList);
            if (tmpfileList.endsWith(".zip")) {
                continue;
                //fileList.delete();
            }
            //String tmpPath = String;
            try (FileInputStream fis = new FileInputStream(fileList);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                 GameProgress gameProgress = (GameProgress) ois.readObject();
                 gameList.add(gameProgress);


            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
      return gameList;
    }

    public static void saveGame(GameProgress gameProgress, String path) {
        try (FileOutputStream fos = new FileOutputStream(path, true);
             ObjectOutputStream ous = new ObjectOutputStream(fos)) {
            ous.writeObject(gameProgress);
        } catch (IOException e) {
            e.printStackTrace( );
        }

    }

    public static void zipFiles(String pathDirectory, ZipOutputStream zios) {
        File listFile = new File(pathDirectory);
        List<String> fileList = Arrays.asList(listFile.list( ));
        for (String s : fileList) {
            if (!s.endsWith(".zip")) {
                String sTmp = pathDirectory + s;
                try (FileInputStream fis = new FileInputStream(sTmp)) {
                    ZipEntry zipEntry = new ZipEntry(s);
                    zios.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fis.available( )];
                    fis.read(buffer);
                    zios.write(buffer);
                    zios.closeEntry( );
                } catch (FileNotFoundException e) {
                    e.printStackTrace( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }

        }
    }

    public static String getGenerationString(String getPath) {
        char ch = getPath.charAt(getPath.length( ) - 1);
        if (Character.isDigit(ch)) {
            int c = 1 + i;
            i++;
            return getPath + c + ".dat";
        } else {
            int c = 1 + i;
            i++;
            return getPath + c + ".dat";
        }

    }

    public static void addSlowInitialization(String pathZip, String pathDirectory) {
        try (FileOutputStream fos = new FileOutputStream(pathZip);
             ZipOutputStream zios = new ZipOutputStream(fos)) {
            zipFiles(pathDirectory, zios);
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public static void deleteSaveFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles( );
        for (File fileList : files) {
            String tmp = String.valueOf(fileList);
            if (!tmp.endsWith(".zip")) {
                fileList.deleteOnExit( );
            }
        }
    }
}
