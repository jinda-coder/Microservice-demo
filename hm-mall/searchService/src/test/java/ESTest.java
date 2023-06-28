import com.alibaba.fastjson.JSON;
import com.hmall.api.ItemAPI;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import com.hmall.seach.SearchApplication;
import com.hmall.seach.pojo.ItemDoc;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class ESTest {
    @Autowired
    RestHighLevelClient client;
    @Autowired
    ItemAPI itemAPI;
//    @Before
//    public void setUp() throws Exception {
//        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://localhost:9200")));
//
//    }
    @Test
    public void bulk() throws IOException {
        Integer page = 1;
        Integer size = 1000;
        //1.创建请求对象
        while(true) {
            BulkRequest request = new BulkRequest("item");
            PageDTO<Item> allByPage = itemAPI.findAllByPage(page, size);
            List<Item> list = allByPage.getList();
            list.forEach(item -> {
                ItemDoc itemDoc = new ItemDoc();
                BeanUtils.copyProperties(item, itemDoc);
                IndexRequest request1 = new IndexRequest("item");
                request1.id(itemDoc.getId() + "").source(JSON.toJSONString(itemDoc), XContentType.JSON);
                request.add(request1);

            });
            BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
            System.out.println("导入成功");
            if (list.size() < 500){
                System.out.println("导入完毕程序退出");
                break;
            }
            page++;
        }
    }
}
