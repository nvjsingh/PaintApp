package Paint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.io.Serializable;

class ReadWritePackage implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<IShape> shapes;
    public ArrayList<GroupShape> grps;
    public ArrayList<Activity> undo;
    public ArrayList<Activity> redo;
}

public class FileRW {
    public void WritePack(ReadWritePackage pack) {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Please choose a directory: ");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (!fileChooser.getCurrentDirectory().isDirectory()) {
                return;
            }
        }
        try {
            String filename = fileChooser.getCurrentDirectory().getCanonicalPath() + "\\" + fileChooser.getSelectedFile().getName();
            FileOutputStream fStream = new FileOutputStream(filename);
            ObjectOutputStream oOutStream = new ObjectOutputStream(fStream);
            oOutStream.writeObject(pack);
            oOutStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ReadWritePackage ReadPack() {
        ReadWritePackage pack = null;
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Please choose a file: ");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (!fileChooser.getCurrentDirectory().isDirectory()) {
                return null;
            }
        }
        try {
            String filename = fileChooser.getCurrentDirectory().getCanonicalPath() + "\\" + fileChooser.getSelectedFile().getName();
            FileInputStream fStream = new FileInputStream(filename);
            ObjectInputStream oInStream = new ObjectInputStream(fStream);
            pack = (ReadWritePackage) oInStream.readObject();
            oInStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pack;
    }
}

class FileLogger {
    private BufferedWriter buffWriter = null;
    public void OpenLog() {
        try {
            buffWriter = new BufferedWriter(new FileWriter("SketchLog.txt", false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void Log(String str) {
        try {
            buffWriter.write(str +"\n");
            buffWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void CloseFile() {
        try {
            if (null != buffWriter) {
                buffWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}