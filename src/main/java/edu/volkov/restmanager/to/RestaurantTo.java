package edu.volkov.restmanager.to;

import edu.volkov.restmanager.HasIdAndName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantTo extends BaseTo implements Serializable, HasIdAndName {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml(whitelistType = NONE)
    private String name;

    @NotBlank
    @Size(min = 5, max = 200)
    @SafeHtml(whitelistType = NONE)
    private String address;

    @Pattern(regexp = "^(\\+\\d{1}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Must be in format: +7 (777) 777-7777")
    @Size(max = 20)
    @SafeHtml(whitelistType = NONE)
    private String phone;

    @NotNull
    private boolean enabled;

    public RestaurantTo(Integer id, String name, String address, String phone, Boolean enabled) {
        super(id);
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
