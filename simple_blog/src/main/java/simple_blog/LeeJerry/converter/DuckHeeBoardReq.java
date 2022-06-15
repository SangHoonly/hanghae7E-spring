package simple_blog.LeeJerry.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import simple_blog.LeeJerry.dto.BoardReqTemplate;
import simple_blog.LeeJerry.exception.AbstractException;

@Getter
@Setter
public class DuckHeeBoardReq extends BoardReqTemplate {

    private MultipartFile files;

    private String data;

    public DuckHeeBoardReq(MultipartFile files, String data) {
        super();
        this.files = files;
        this.data = data;

        transfer();
    }


    @Override
    public void transfer() {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(this.data);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        super.setTitle(jsonObject.get("title").getAsString());
        super.setBody( jsonObject.get("body").getAsString());
    }

    public boolean validate() throws AbstractException {
        return super.getTitle() != null && super.getBody() != null && this.files != null;
    }
}
