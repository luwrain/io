
package org.luwrain.io.api.lsocial.account;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
public final class Account
{
    private String nickName, fullName, bio, city, organization, department, subdepartment, position, speciality, degree, rank;
    private List<String> interests;
    private long lastSeen;
}
