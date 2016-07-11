// @formatter:off
package com.everhomes.queue;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface DispatchableCommand extends Callable<Object>, Serializable {
}
