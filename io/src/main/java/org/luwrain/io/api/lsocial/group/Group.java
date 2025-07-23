
package org.luwrain.io.api.lsocial.group;

import lombok.*;

@Data
@NoArgsConstructor
public final class Group
{
    private String name, org, department;
    private boolean owner, teacher, student;
}
