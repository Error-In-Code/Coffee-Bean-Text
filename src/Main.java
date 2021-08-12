public class Main {
    public Main(){
        EditorGUI gui = new EditorGUI(FileHandler.getFileData());
    }

    public static void main(String[] args){
        new Main();
    }
}
