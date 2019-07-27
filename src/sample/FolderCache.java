package sample;

import java.util.*;

public class FolderCache {
    private static HashMap<String, Integer> folderPathMap = new HashMap<>();
    private static final FolderCache instance = new FolderCache();

    private FolderCache(){}

    public static FolderCache getInstance(){
        return instance;
    }
    public static void cacheMusicFolder(String folderPath){
        if (folderPathMap.containsKey(folderPath)){
            int counter = folderPathMap.get(folderPath);
            folderPathMap.put(folderPath, counter+1);
        }else {
            folderPathMap.put(folderPath, 0);
        }
    }
    public static String getBestLocation(){
        Map.Entry<String,Integer> entry = folderPathMap.entrySet().iterator().next();
        String key = entry.getKey();
        return key;
    }
    public void sortMap(){
        List list = new LinkedList(folderPathMap.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        sortedHashMap = folderPathMap;
    }
}
