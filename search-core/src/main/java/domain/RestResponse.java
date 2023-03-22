package domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import domain.enums.ResponseCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponse<T> {
    String message;
    int code;
    T value;

    public RestResponse(ResponseCode responseCode) {
        this.message = responseCode.getMessage();
        this.code = responseCode.getCode();
    }

    public RestResponse(ResponseCode responseCode, T value) {
        this(responseCode);
        this.value = value;
    }

    public RestResponse<T> appendMessage(Object msg) {
        this.message = String.format("%s :: %s", this.message, msg);
        return this;
    }

    public static <T> RestResponse<T> success(T value) {
        return new RestResponse<>(ResponseCode.RESPONSE_OK, value);
    }
}
