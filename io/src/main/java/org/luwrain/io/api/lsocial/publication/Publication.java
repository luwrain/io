/*
 * Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.luwrain.io.api.lsocial.publication;

import java.util.*;
import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Publication
{
    static public final int
	TYPE_PAPER = 0,
	TYPE_BOOK = 1,
	TYPE_THESIS = 2,
	TYPE_GRADUATION_WORK = 3,
    TYPE_COURSE_WORK = 4;

    private long id;
    private int type;
    private String name, title, authors, subject;
    private Map<String, String> info;
    private long creationTimestamp, modificationTimestamp;
    private List<Section> sects;
    }
