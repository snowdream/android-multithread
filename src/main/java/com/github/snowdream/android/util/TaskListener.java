package com.github.snowdream.android.util;

public class TaskListener<Result> {
	public void onStart() {
	};

	public void onSuccess(Result result) {
	};

	public void onError(Throwable thr) {
	};

	public void onFinish() {
	};
}
