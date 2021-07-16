package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

public class Controller {
    public boolean isSaved,c_selected,pas_selected;
    @FXML
    CodeArea terminal=new CodeArea();
    @FXML
    MenuItem item1,item2,item3,item4,item5;
    @FXML
    public CodeArea c_code,pascal_code;
    @FXML
    SplitPane t_code;
    @FXML
    RadioMenuItem french,english;
    boolean terminal_is_hidden=false;
    private int itemnum=0;
    String pascal_path="";
    String c_path="";
    String all_projects="C:\\Users\\Amazon\\C--Projects";
    String my_project="";
    TextInputDialog createfile=new TextInputDialog();
    Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
    String lang="en";

    public void newproject(ActionEvent event) throws IOException {
        if (c_code.isVisible()) {
            Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sample.fxml")));
            Scene scene= new Scene(root,600,400);
            scene.setFill(null);
            Stage primaryStage=new Stage();
            primaryStage.setTitle("C--");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        }else {
            initprojectfolder();
        }
    }
    public void openproject(ActionEvent event) throws IOException {
        initViews();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(all_projects));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Code C","*.c"),
                new FileChooser.ExtensionFilter("text file","*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (itemnum<5) {
            switch (itemnum){
                case 0:
                    item5.setText(selectedFile.getAbsolutePath());
                    item5.setVisible(true);
                    itemnum=1;
                    break;
                case 1:
                    item4.setText(selectedFile.getAbsolutePath());
                    item4.setVisible(true);
                    itemnum=2;
                    break;
                case 2:
                    item3.setText(selectedFile.getAbsolutePath());
                    item3.setVisible(true);
                    itemnum=3;
                    break;
                case 3:
                    item2.setText(selectedFile.getAbsolutePath());
                    item2.setVisible(true);
                    itemnum=4;
                    break;
                case 4:
                    item1.setText(selectedFile.getAbsolutePath());
                    item1.setVisible(true);
                    itemnum=5;
                    break;
            }
        }else {
            String[] paths=new String[]{item1.getText(),item2.getText(),item3.getText(),item4.getText()};
            item2.setText(paths[0]);item3.setText(paths[1]);item4.setText(paths[2]);item5.setText(paths[3]);
            item1.setText(selectedFile.getAbsolutePath());
        }
        if (selectedFile!=null){
            c_code.replaceText(readFile(selectedFile));
            terminal.replaceText(terminal.getText()+"Opened: "+selectedFile.getAbsolutePath()+"\n");
            c_path=selectedFile.getAbsolutePath();
            pascal_path=selectedFile.getAbsolutePath();
            pascal_path=pascal_path.substring(0,pascal_path.length()-2)+".pas";
            File pascal=new File(pascal_path);
            if (pascal!=null) {
                pascal_code.replaceText(readFile(pascal));
            }else{
                pascal.createNewFile();
                pascal_path=pascal.getPath();
            }
            my_project=selectedFile.getName();
            my_project= my_project.substring(0,my_project.length()-2);
        }
        isSaved=false;
        terminal_is_hidden=false;

    }
    public void openrecentproject(File file) throws IOException {
        initViews();
        c_code.replaceText(readFile(file));
        terminal.replaceText(terminal.getText()+"Opened: "+file.getAbsolutePath()+"\n");
        c_path=file.getAbsolutePath();
        pascal_path=file.getAbsolutePath();
        pascal_path=pascal_path.substring(0,pascal_path.length()-2)+".pas";
        File pascal=new File(pascal_path);
        pascal_code.replaceText(readFile(pascal));
        my_project=file.getName();
        my_project= my_project.substring(0,my_project.length()-2);
        isSaved=false;
        t_code.setDividerPositions(0.8);
    }
    public void item1_clicked(ActionEvent event) throws IOException {
        File file=new File(item1.getText());
        openrecentproject(file);
    }
    public void item2_clicked(ActionEvent event) throws IOException {
        File file=new File(item2.getText());
        openrecentproject(file);
    }
    public void item3_clicked(ActionEvent event) throws IOException {
        File file=new File(item3.getText());
        openrecentproject(file);
    }
    public void item4_clicked(ActionEvent event) throws IOException {
        File file=new File(item4.getText());
        openrecentproject(file);
    }
    public void item5_clicked(ActionEvent event) throws IOException {
        File file=new File(item5.getText());
        openrecentproject(file);
    }
    public void closeapp(ActionEvent event){
        saveproject(event);
        c_code.replaceText("");
        pascal_code.replaceText("");
        terminal.replaceText("");
        c_code.setVisible(false);
        pascal_code.setVisible(false);
        terminal.setVisible(false);
        pascal_path="";
        c_path="";
        my_project="";
    }
    public void saveproject(ActionEvent event){
        File c_file= new File(all_projects+"\\"+my_project+"\\"+my_project+".c");
        File pas_file=new File(all_projects+"\\"+my_project+"\\"+my_project+".pas");
        pascal_path=pas_file.getAbsolutePath();
        c_path=c_file.getAbsolutePath();
        filesaver(c_file,c_code.getText());
        filesaver(pas_file,pascal_code.getText());
        isSaved=true;
    }
    public void save_as(ActionEvent event){
        createfile.setTitle("Name your project");
        createfile.getDialogPane().setContentText("Name:");
        createfile.setHeaderText(null);
        createfile.getDialogPane().setPrefWidth(400);
        Optional<String> res=createfile.showAndWait();
        TextField input= createfile.getEditor();
        String s=input.getText();
        if (s!=""){
            File file=new File("C:\\Users\\Amazon\\"+s);
            file.mkdir();
            FileChooser fileChooser=new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\Amazon\\"+s));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Code C","*.c"),
                    new FileChooser.ExtensionFilter("text file","*.txt"));
            File selectedFile = fileChooser.showSaveDialog(null);
            filesaver(selectedFile,c_code.getText());
            fileChooser=new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\Amazon\\"+s));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Code Pascal","*.pas"),
                    new FileChooser.ExtensionFilter("text file","*.txt"));
            selectedFile = fileChooser.showSaveDialog(null);
            filesaver(selectedFile,pascal_code.getText());
        }
        isSaved=true;
    }
    public void Exit(ActionEvent event){
        alert.setTitle("Project isn't saved");
        String s = "You didn't save the last changes!" +
                "Do you want to save them ?";
        alert.setContentText(s);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)){
            saveproject(event);
            Platform.exit();
            System.exit(0);
        }else {
            Platform.exit();
            System.exit(0);
        };

    }

    public void initprojectfolder() throws IOException {
        createfile.setTitle("Name your project");
        createfile.getDialogPane().setContentText("Name:");
        createfile.setHeaderText(null);
        createfile.getDialogPane().setPrefWidth(400);
        Optional<String> res=createfile.showAndWait();
        TextField input= createfile.getEditor();
        if (input.getText()!=""){
            my_project=input.getText();
            File file=new File(all_projects+"\\"+my_project);
            file.mkdir();
            File c_file= new File(all_projects+"\\"+my_project+"\\"+my_project+".c");
            File pas_file=new File(all_projects+"\\"+my_project+"\\"+my_project+".pas");
            c_file.createNewFile();
            c_path=c_file.getPath();
            pas_file.createNewFile();
            pascal_path=pas_file.getPath();
            terminal.replaceText(terminal.getText()+"your project has been created by the name: "+my_project+"\nIn: "
                    +all_projects+my_project+"\n\n");
            initViews();
        }
    }
    public void initViews(){
        t_code.setDividerPositions(0.8);
        c_code.setVisible(true);
        pascal_code.setVisible(true);
        terminal.setVisible(true);
        c_code.setParagraphGraphicFactory(LineNumberFactory.get(c_code));
        pascal_code.setParagraphGraphicFactory(LineNumberFactory.get(pascal_code));
        c_code.replaceText("");
        pascal_code.replaceText("");
        terminal_is_hidden=false;

    }
    private void filesaver(File file,String s) {
        PrintWriter printWriter= null;
        try {
            printWriter = new PrintWriter(file);
            printWriter.write(s);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String readFile(File selected) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(selected));
        String text;
        while ((text = bufferedReader.readLine()) != null) {
            stringBuffer.append( text);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
    public void build_pascal(ActionEvent event){
            saveproject(event);
            terminal.replaceText(terminal.getText()+"\n");
            cmd_running(pascal_path,"C:\\FPC\\3.2.2\\bin\\i386-Win32\\fpc.exe ");
            terminal.deleteText(terminal.getText().length()-78,terminal.getText().length());
            terminal.appendText("---------------------------------------------------------\n");
    }
    public void cmd_running(String s,String s1){
        try {
            t_code.setDividerPositions(0.8);
            terminal_is_hidden=false;
            String commend = s1 + s;
            run_cmd(commend);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        terminal.replaceText(terminal.getText()+"---------------------------------------------------------\n");
    }
    public void build_c(ActionEvent event){
        File file=new File(c_path);
        filesaver(file,c_code.getText());
        terminal.appendText("compiling...\n");
        cmd_running(c_path,"C:\\MinGW\\bin\\gcc.exe ");


    }
    public void redoIsClicked(ActionEvent event){
        if (c_selected) {
            if (c_code.isRedoAvailable()) {
                c_code.redo();
            }
        }
        if (pas_selected) {
            if (pascal_code.isRedoAvailable()) {
                pascal_code.redo();
            }
        }
        isSaved=false;
    }
    public void undoIsClicked(ActionEvent event){
        if (c_selected) {
            if (c_code.isUndoAvailable()) {
                c_code.undo();
            }
        }
        if (pas_selected) {
            if (pascal_code.isUndoAvailable()) {
                pascal_code.undo();
            }
        }
        isSaved=false;
    }
    public void SelectAll(ActionEvent event){
        if (c_selected) {
            if (c_code.getText().length() != 0)
                c_code.selectAll();
        }
        if (pas_selected){
            if (pascal_code.getText().length()!=0)
                pascal_code.selectAll();
        }
        isSaved=false;
    }
    public void unselectall(ActionEvent event){
            if (c_selected) {
                if (c_code.getText().length() != 0)
                    c_code.replaceText(c_code.getText());
            }
            if (pas_selected) {
                if (pascal_code.getText().length() != 0)
                    pascal_code.replaceText(pascal_code.getText());
            }


        isSaved=false;
    }
    public void paste(ActionEvent event){
        if (c_selected)
            c_code.paste();
        if (pas_selected)
            pascal_code.paste();
        isSaved=false;
    }
    public void copy(ActionEvent event){
        if (c_selected)
            c_code.copy();
        if (pas_selected)
            pascal_code.copy();

        isSaved=false;
    }
    public void cut(ActionEvent event){
        if (c_selected)
            c_code.cut();
        if (pas_selected)
            pascal_code.cut();
        isSaved=false;
    }
    public void delete(ActionEvent event){
        if (c_selected)
            c_code.deleteText(c_code.getSelection().getStart(),c_code.getSelection().getEnd());
        if (pas_selected)
            pascal_code.deleteText(pascal_code.getSelection().getStart(),pascal_code.getSelection().getEnd());
        isSaved=false;
    }
    public  void build_c_shortcut(MouseEvent event){
        ActionEvent event1 = new ActionEvent();
        build_c(event1);

    }
    public  void build_pas_shortcut(MouseEvent event){
        ActionEvent event1 = new ActionEvent();
        build_pascal(event1);
    }
    public void translate_shortcut(MouseEvent event) throws IOException, InterruptedException {
        ActionEvent event1= new ActionEvent();
        translat(event1);
    }
    public void hide_t(MouseEvent event){
        if (!terminal_is_hidden){
            t_code.setDividerPositions(1);
            terminal_is_hidden=true;}
        else{
            t_code.setDividerPositions(0.6);
            terminal_is_hidden=false;
        }
    }
    public void c_is_selected(){
        c_selected=true;
        pas_selected=false;
    }
    public void pas_is_selected(){
        c_selected=false;
        pas_selected=true;
    }
    public void changed(){
        isSaved=false;
    }
    public void translat(ActionEvent event) throws IOException, InterruptedException {
        translate_cmd(c_path,pascal_path,lang);
    }
    public void translate_cmd(String s1,String s2, String lang) throws IOException, InterruptedException {
        String commend = "./comp "+s1+" "+ s2+" "+lang;
        run_cmd(commend);
        File file=new File(pascal_path);
        pascal_code.replaceText(readFile(file));
    }

    public void run_cmd(String commend) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(commend);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        BufferedReader bufer = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String line;

            while ((line = bufer.readLine()) != null) {
                terminal.appendText( line + "\n");
            }
            while ((line = buf.readLine()) != null) {
                terminal.appendText( line + "\n");
            }
        if (bufer.readLine()==null){
            terminal.appendText("complited no errors\n");
        }
        terminal.requestFollowCaret();

    }
    public void change_lang_to_english(ActionEvent event){
        lang="en";
        french.setSelected(false);
        english.setSelected(true);
    }
    public void change_lang_to_french(ActionEvent event){
        english.setSelected(false);
        french.setSelected(true);
        lang="fr";
    }
}