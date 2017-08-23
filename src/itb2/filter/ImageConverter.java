package itb2.filter;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import itb2.image.Image;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ImageConverter {
	
	Class<? extends Image> value();
	
}
