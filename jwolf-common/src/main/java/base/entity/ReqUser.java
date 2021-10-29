package base.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Getter
@Setter
@AllArgsConstructor
public class ReqUser {
    private String userId;
    private String accessType;
    private String isAdmin; //1管理员 1非管理员


}
