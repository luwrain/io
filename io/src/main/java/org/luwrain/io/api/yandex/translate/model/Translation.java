// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.yandex.translate.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Translation
{
    private String text, detectedLanguageCode;
}

