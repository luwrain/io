/*
 * Copyright 2024-2025 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.io.api.lsocial.account;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public class CaptureInfoQuery extends Query<RegisterQuery, CaptureInfoResponse>
{
    static public final String
	ADDR = "/v1/account/capture/get/",
	ARG_CAPTURE_ID = "capture_id";

    public CaptureInfoQuery(String endpoint) { super(Type.GET, endpoint, ADDR, CaptureInfoResponse.class); }
    public CaptureInfoQuery captureId(String value) { args.put(ARG_CAPTURE_ID, requireNonNull(value, "value can't be null"));  return this; }
}
