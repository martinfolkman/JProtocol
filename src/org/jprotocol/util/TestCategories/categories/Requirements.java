package org.jprotocol.util.TestCategories.categories;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Requirements {
	String[] value();
}
