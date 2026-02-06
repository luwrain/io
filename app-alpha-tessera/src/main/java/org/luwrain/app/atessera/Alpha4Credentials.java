// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import java.util.concurrent.*;
import io.grpc.*;

public class Alpha4Credentials extends CallCredentials
{
    static final Metadata.Key<String> AUTHORIZATION_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    
  private final String accessToken;

  public Alpha4Credentials(String accessToken)
    {
    this.accessToken = accessToken;
  }

  @Override public void applyRequestMetadata(RequestInfo requestInfo,
					     Executor executor,
      MetadataApplier metadataApplier)
    {
    executor.execute(new Runnable() {
      @Override public void run()
	    {
        try {
          Metadata headers = new Metadata();
          headers.put(AUTHORIZATION_KEY, accessToken);
          metadataApplier.apply(headers);
        }
	catch (Throwable e)
	{
          metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
        }
      }
    });
  }
}
