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

package org.luwrain.io.api.lsocial.presentation;

import java.util.*;
import lombok.*;
import org.luwrain.io.api.lsocial.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Presentation
{
    private long id;
    private String name, shortRef, title, subtitle, authors, subject, date;
    private long creationTimestamp, modificationTimestamp;

    private List<Frame> frames;
    }
