/*******************************************************************************
 * Copyright (C) 2013 Snowdream Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: * www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.github.snowdream.android.util.concurrent;

import android.annotation.TargetApi;
import android.os.Build;

import com.github.snowdream.android.util.Log;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;

/**
 * 1.inherit a class from
 * com.github.snowdream.android.util.concurrent.AsyncTask,explicit inherit the
 * construction from the super class.
 * <p/>
 * <pre>
 *  * inherit a class from com.github.snowdream.android.util.concurrent.AsyncTask
 * public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
 *  public DownloadFilesTask(TaskListener<Integer, Long> listener) {
 *        * explicit inherit the construction from the super class.
 *       super(listener);
 *  }
 *
 *  *
 *  * TODO
 *  * if error occurs,carry it out.
 *  *
 *  * if (listener != null) {
 *  *    listener.onError(new Throwable());
 *  * }
 *  *
 * protected Long doInBackground(URL... urls) {
 *      int count = urls.length;
 *      long totalSize = 0;
 *      for (int i = 0; i < count; i++) {
 *          totalSize += 10;
 *          publishProgress((int) ((i / (float) count) * 100));
 *           *  Escape early if cancel() is called
 *          if (isCancelled()) break;
 *          try {
 *             Thread.sleep(1000);
 *         } catch (InterruptedException e) {
 *             e.printStackTrace();
 *         }
 *      }
 *      return totalSize;
 *  }
 * }
 * </pre>
 * <p/>
 * 2.define a TaskListener.please pay attention to the generic parameter Integer
 * and Long,is the same as above.
 * <p/>
 * <pre>
 * private TaskListener&lt;Integer, Long&gt; listener = new TaskListener&lt;Integer, Long&gt;() {
 *
 *     &#064;Override
 *     public void onStart() {
 *         super.onStart();
 *         Log.i(&quot;onStart()&quot;);
 *     }
 *
 *     &#064;Override
 *     public void onProgressUpdate(Integer... values) {
 *         super.onProgressUpdate(values);
 *         Log.i(&quot;onProgressUpdate(values)&quot; + values[0]);
 *     }
 *
 *     &#064;Override
 *     public void onSuccess(Long result) {
 *         super.onSuccess(result);
 *         Log.i(&quot;onSuccess(result)&quot; + result);
 *     }
 *
 *     &#064;Override
 *     public void onCancelled() {
 *         super.onCancelled();
 *         Log.i(&quot;onCancelled()&quot;);
 *     }
 *
 *     &#064;Override
 *     public void onError(Throwable thr) {
 *         super.onError(thr);
 *         Log.i(&quot;onError()&quot;);
 *     }
 *
 *     &#064;Override
 *     public void onFinish() {
 *         super.onFinish();
 *         Log.i(&quot;onFinish()&quot;);
 *     }
 *
 * };
 * </pre>
 * <p/>
 * 3.construct a AsyncTask,and carry it out.
 * <p/>
 * <pre>
 * URL url = null;
 * try {
 *     url = new URL("http: * www.baidu.com/");
 * } catch (MalformedURLException e) {
 *      *  TODO Auto-generated catch block
 *     e.printStackTrace();
 * }
 *
 * new DownloadFilesTask(listener).execute(url,url,url);
 * </pre>
 *
 * @author snowdream <yanghui1986527@gmail.com>
 * @version v1.0
 * @date 2013年9月8日
 */
public abstract class AsyncTask<Params, Progress, Result> extends
        android.os.AsyncTask<Params, Progress, Result> {
    protected TaskListener<Progress, Result> listener = null;

    public AsyncTask() {
        super();
    }

    public AsyncTask(TaskListener<Progress, Result> listener) {
        super();
        this.listener = listener;
    }


    public TaskListener<Progress, Result> getListener() {
        return listener;
    }

    public void setListener(TaskListener<Progress, Result> listener) {
        this.listener = listener;
    }

    /**
     * TODO <BR/>
     * if error occurs,carry it out.<BR/>
     * <p/>
     * <pre>
     * if (listener != null) {
     *     listener.onError(new Throwable());
     * }
     * </pre>
     */
    @Override
    protected abstract Result doInBackground(Params... params);

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (listener != null) {
            listener.onCancelled();
            listener.onFinish();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCancelled(Result result) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            super.onCancelled(result);
        } else {
            super.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (listener != null) {
            if (result != null) {
                listener.onSuccess(result);
            }

            listener.onFinish();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onStart();
        }
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (listener != null) {
            listener.onProgressUpdate(values);
        }
    }

    /**
     * set the default Executor
     *
     * @param executor
     */
    public static void setDefaultExecutor(Executor executor) {
        Class<?> c = null;
        Field field = null;
        try {
            c = Class.forName("android.os.AsyncTask");
            field = c.getDeclaredField("sDefaultExecutor");
            field.setAccessible(true);
            field.set(null, executor);
            field.setAccessible(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("IllegalArgumentException", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("ClassNotFoundException", e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.e("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("IllegalAccessException", e);
        }
    }

    /**
     * get the default Executor
     *
     * @return the default Executor
     */
    public static Executor getDefaultExecutor() {
        Executor exec = null;

        Class<?> c = null;
        Field field = null;
        try {
            c = Class.forName("android.os.AsyncTask");
            field = c.getDeclaredField("sDefaultExecutor");
            field.setAccessible(true);
            exec = (Executor) field.get(null);
            field.setAccessible(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("IllegalArgumentException", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("ClassNotFoundException", e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.e("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("IllegalAccessException", e);
        }

        return exec;
    }
}
