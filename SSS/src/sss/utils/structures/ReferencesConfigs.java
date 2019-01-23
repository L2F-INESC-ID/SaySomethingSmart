package sss.utils.structures;

/**
 * @author Vania
 * @date 24/10/2016
 */
public class ReferencesConfigs {

    private String folderPath;
    private String interactionsFileName;
    private String linesFileName;
    private int inputSize;

    public ReferencesConfigs(String folderPath, String interactionsFileName, String linesFileName, int inputSize) {

        this.folderPath = folderPath;
        this.interactionsFileName = interactionsFileName;
        this.linesFileName = linesFileName;
        this.inputSize = inputSize;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getInteractionsFileName() {
        return interactionsFileName;
    }

    public String getLinesFileName() {
        return linesFileName;
    }

    public int getInputSize() {
        return inputSize;
    }
}
