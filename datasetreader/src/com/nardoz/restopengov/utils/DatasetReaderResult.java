import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatasetReaderResult {

    private Gson gson = new Gson();
    private List<Map<String, String>> dataset = new ArrayList<Map<String, String>>();

    public List<String> getJSONList() {

        List<String> result = new ArrayList<String>();

        for(Map<String, String> item : dataset) {
            result.add(gson.toJson(item));
        }

        return result;
    }

    public void add(Map<String, String> obj) {
        dataset.add(obj);
    }
}
