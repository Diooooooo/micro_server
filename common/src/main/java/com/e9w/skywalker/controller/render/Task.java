package com.e9w.skywalker.controller.render;

public class Task {

	private final boolean hasFinishedTask;

	public Task(boolean hasFinishedTask) {
		super();
		this.hasFinishedTask = hasFinishedTask;
	}

	public Task() {
		super();
		this.hasFinishedTask = false;
	}

	public boolean isHasFinishedTask() {
		return hasFinishedTask;
	}

}
