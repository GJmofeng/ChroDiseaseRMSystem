@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userid;
    private String password;
    private String fullname;
    private String role;
    
    // 不包含token字段，因为token是在登录时动态生成的
} 