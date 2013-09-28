
package com.github.snowdream.android.util.concurrent;

public class TaskListener<Progress, Result> {
    /**
     * Runs on the UI thread before doInBackground(Params...).
     */
    public void onStart() {
    };

    /**
     * Runs on the UI thread after publishProgress(Progress...) is invoked. The
     * specified values are the values passed to publishProgress(Progress...).
     * 
     * @param values The values indicating progress.
     */
    public void onProgressUpdate(Progress... values) {
    };

    /**
     * Runs on the UI thread after doInBackground(Params...). The specified
     * result is the value returned by doInBackground(Params...). This method
     * won't be invoked if the task was cancelled.
     * 
     * @param result The result of the operation computed by
     *            doInBackground(Params...).
     */
    public void onSuccess(Result result) {
    };

    /**
     * Applications should preferably override onCancelled(Object). This method
     * is invoked by the default implementation of onCancelled(Object). Runs on
     * the UI thread after cancel(boolean) is invoked and
     * doInBackground(Object[]) has finished.
     */
    public void onCancelled() {
    };

    public void onError(Throwable thr) {
    };

    /**
     * Runs on the UI thread after doInBackground(Params...) when the task is
     * finished or cancelled.
     */
    public void onFinish() {
    };
}
