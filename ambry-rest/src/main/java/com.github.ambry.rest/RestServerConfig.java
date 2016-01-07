package com.github.ambry.rest;

import com.github.ambry.config.Config;
import com.github.ambry.config.Default;
import com.github.ambry.config.VerifiableProperties;


/**
 * Configuration parameters required by {@link RestServer} and Rest infrastructure ({@link RestRequestHandler},
 * {@link RestResponseHandler}).
 * <p/>
 * Receives the in-memory representation of a properties file and extracts parameters that are specifically
 * required for {@link RestServer} and presents them for retrieval through defined APIs.
 */
class RestServerConfig {
  /**
   * The {@link BlobStorageServiceFactory} that needs to be used by the {@link RestServer}
   * for bootstrapping the {@link BlobStorageService}.
   */
  @Config("rest.server.blob.storage.service.factory")
  public final String restServerBlobStorageServiceFactory;

  /**
   * The {@link NioServerFactory} that needs to be used by the {@link RestServer} for
   * bootstrapping the {@link NioServer}.
   */
  @Config("rest.server.nio.server.factory")
  @Default("com.github.ambry.rest.NettyServerFactory")
  public final String restServerNioServerFactory;

  /**
   * The number of scaling units in {@link RestRequestHandler} that will handle requests.
   */
  @Config("rest.server.request.handler.scaling.unit.count")
  @Default("5")
  public final int restServerRequestHandlerScalingUnitCount;

  /**
   * The {@link RestRequestHandlerFactory} that needs to be used by the {@link RestServer}
   * for bootstrapping the {@link RestRequestHandler}.
   */
  @Config("rest.server.request.handler.factory")
  @Default("com.github.ambry.rest.AsyncRequestResponseHandlerFactory")
  public final String restServerRequestHandlerFactory;

  /**
   * The number of scaling units in {@link RestResponseHandler} handle responses.
   */
  @Config("rest.server.response.handler.scaling.unit.count")
  @Default("5")
  public final int restServerResponseHandlerScalingUnitCount;

  /**
   * The {@link RestResponseHandlerFactory} that needs to be used by the {@link RestServer}
   * for bootstrapping the {@link RestResponseHandler}.
   */
  @Config("rest.server.response.handler.factory")
  @Default("com.github.ambry.rest.AsyncRequestResponseHandlerFactory")
  public final String restServerResponseHandlerFactory;

  /**
   * The {@link com.github.ambry.router.RouterFactory} that needs to be used by the {@link RestServer}
   * for bootstrapping the {@link com.github.ambry.router.Router}.
   */
  @Config("rest.server.router.factory")
  @Default("com.github.ambry.router.CoordinatorBackedRouterFactory")
  public final String restServerRouterFactory;

  public RestServerConfig(VerifiableProperties verifiableProperties) {
    restServerBlobStorageServiceFactory = verifiableProperties.getString("rest.server.blob.storage.service.factory");
    restServerNioServerFactory =
        verifiableProperties.getString("rest.server.nio.server.factory", "com.github.ambry.rest.NettyServerFactory");
    restServerRequestHandlerScalingUnitCount =
        verifiableProperties.getIntInRange("rest.server.request.handler.scaling.unit.count", 5, 1, Integer.MAX_VALUE);
    restServerRequestHandlerFactory = verifiableProperties
        .getString("rest.server.request.handler.factory", "com.github.ambry.rest.AsyncRequestResponseHandlerFactory");
    restServerResponseHandlerScalingUnitCount =
        verifiableProperties.getIntInRange("rest.server.response.handler.scaling.unit.count", 5, 1, Integer.MAX_VALUE);
    restServerResponseHandlerFactory = verifiableProperties
        .getString("rest.server.response.handler.factory", "com.github.ambry.rest.AsyncRequestResponseHandlerFactory");
    restServerRouterFactory = verifiableProperties
        .getString("rest.server.router.factory", "com.github.ambry.router.CoordinatorBackedRouterFactory");
  }
}
