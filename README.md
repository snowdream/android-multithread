#android-multithread

##Introduction
android lib - multithread    
add a listener to the AsyncTask.

##System requirements
Android 1.5+

##Dependencies
android-log [https://github.com/snowdream/android-log](https://github.com/snowdream/android-log)

##Download
Download [the latest jar][1] or grab via Maven:

```xml
<dependency>
  <groupId>com.github.snowdream.android.util.concurrent</groupId>
  <artifactId>multithread</artifactId>
  <version>1.0</version>
</dependency>
```

or Gradle:
```groovy
    compile 'com.github.snowdream.android.util.concurrent:multithread:1.0'
```

##Usage
1.inherit a class from com.github.snowdream.android.util.concurrent.AsyncTask,explicit inherit the construction from the super class.

```java
//inherit a class from com.github.snowdream.android.util.concurrent.AsyncTask
public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
 public DownloadFilesTask(TaskListener<Integer, Long> listener) {
      //explicit inherit the construction from the super class.
      super(listener);
 }
       
/**
 * TODO 
 * if error occurs,carry it out.
 * 
 * if (listener != null) {
 *    listener.onError(new Throwable());
 * }
 */
protected Long doInBackground(URL... urls) {
     int count = urls.length;
     long totalSize = 0;
     for (int i = 0; i < count; i++) {
         totalSize += 10;
         publishProgress((int) ((i / (float) count) * 100));
         // Escape early if cancel() is called
         if (isCancelled()) break;
         try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     }
     return totalSize;
 }
}
```

2.define a TaskListener.please pay attention to the generic parameter Integer and Long,is the same as above.
```java
private TaskListener<Integer, Long> listener = new TaskListener<Integer, Long>(){

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart()");
    }

    @Override
    public void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.i("onProgressUpdate(values)" + values[0] );
    }

    @Override
    public void onSuccess(Long result) {
        super.onSuccess(result);
        Log.i("onSuccess(result)" + result);
    }

    @Override
    public void onCancelled() {
        super.onCancelled();
        Log.i("onCancelled()");
    }

    @Override
    public void onError(Throwable thr) {
        super.onError(thr);
        Log.i("onError()");
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Log.i("onFinish()");
    }
	    
};
```

3.construct a AsyncTask,and carry it out.   
```java
URL url = null;
try {
    url = new URL("http://www.baidu.com/");
} catch (MalformedURLException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
        
new DownloadFilesTask(listener).execute(url,url,url);
```

##License
```
 Copyright (C) 2013 Snowdream Mobile
  
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
  
 http://www.apache.org/licenses/LICENSE-2.0
  
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```

[1]:https://oss.sonatype.org/content/groups/public/com/github/snowdream/android/util/concurrent/multithread/1.0/multithread-1.0.jar
