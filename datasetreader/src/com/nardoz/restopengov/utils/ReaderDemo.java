import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import java.util.List;
import static org.elasticsearch.node.NodeBuilder.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ReaderDemo {

    public static void main(String[] args) {

        // Read the specified datasets
        DatasetReader dataset = new DatasetReader();

        List<String> events    = dataset.read("data/bafici-2010/bafici10-events.csv").getJSONList();
        List<String> countries = dataset.read("data/bafici-2010/bafici10-countries.csv").getJSONList();

        // Elasticsearch magic
        Node node = nodeBuilder().client(true).node();
        Client client = node.client();

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for(int i = 0; i < events.size(); i++) {
            bulkRequest.add(client.prepareIndex("bafici-2010", "events", String.valueOf(i)).setSource(events.get(i)));
        }

        for(int i = 0; i < countries.size(); i++) {
            bulkRequest.add(client.prepareIndex("bafici-2010", "countries", String.valueOf(i)).setSource(countries.get(i)));
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if(bulkResponse.hasFailures()) {
            System.out.println(bulkResponse.buildFailureMessage());
        }

        node.close();

    }

}