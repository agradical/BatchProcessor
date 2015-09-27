package com.batch.processor;

import java.io.IOException;


public interface BatchProcessor {

	public void process() throws IOException, InterruptedException;

}
