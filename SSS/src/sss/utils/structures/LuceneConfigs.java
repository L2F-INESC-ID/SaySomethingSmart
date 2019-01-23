package sss.utils.structures;

/**
 * @author Vania
 * @date 24/10/2016
 */
public class LuceneConfigs {

    private String dbPath;
    private String indexPath;
    private boolean usePreviouslyCreatedIndex;
    private int hitsPerQuery;

    public LuceneConfigs(String dbPath, String indexPath, boolean usePreviouslyCreatedIndex, int hitsPerQuery) {

        this.dbPath = dbPath;
        this.indexPath = indexPath;
        this.usePreviouslyCreatedIndex = usePreviouslyCreatedIndex;
        this.hitsPerQuery = hitsPerQuery;
    }

    public String getDbPath() {
        return dbPath;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public boolean isUsePreviouslyCreatedIndex() {
        return usePreviouslyCreatedIndex;
    }

    public int getHitsPerQuery() {
        return hitsPerQuery;
    }
}
