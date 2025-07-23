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
public final class Frame
{
    static public final int
	TYPE_MARKDOWN = 0,
	TYPE_LATEX = 1,
	TYPE_EQUATION = 2,
	TYPE_TABLE = 3,
	TYPE_METAPOST = 4,
	TYPE_GNUPLOT = 5,
	TYPE_PLANTUML = 6,
	TYPE_LISTING = 7,
	TYPE_GRAPHVIZ_DOT = 8,
	TYPE_GRAPHVIZ_NEATO = 9,
	TYPE_GRAPHVIZ_TWOPI = 10,
	TYPE_GRAPHVIZ_CIRCO = 11;

    private String title, subtitle, label, listingLang;
    private List<String> source;
}
