/*
Copyright (C) 2014 Andrew Shay shayConcepts

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software") for free and 
non commercial purposes, to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, 
sublicense, copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright 
notice and this permission notice shall be included in all copies and substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR 
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package application;

//Download progressbar code from user "par" at http://stackoverflow.com/questions/2263062/how-to-monitor-progress-jprogressbar-with-filechannels-transferfrom-method


import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadUpdate {
	public static FileOutputStream fos;
    public static void runUpdate(String path, String downloadLink) {
		System.out.println(path);	
    	System.out.println(downloadLink);
    		new Downloader(path, downloadLink);
    }

    private interface RBCWrapperDelegate {
        // will get the progress as a percentage if known, otherwise -1.0 to
        // indicate indeterminate progress.
        public void rbcProgressCallback( RBCWrapper rbc, double progress );
    }

    private static final class Downloader implements RBCWrapperDelegate {
        public Downloader(String localPath, String remoteURL ) {
            ReadableByteChannel     rbc;
            URL                     url;

            try {
                url = new URL( remoteURL );
                rbc = new RBCWrapper( Channels.newChannel( url.openStream() ), contentLength( url ), this );
                fos = new FileOutputStream("update.exe");
                fos.getChannel().transferFrom( rbc, 0, Long.MAX_VALUE );
                fos.close();
                Desktop.getDesktop().open(new File(Methods.getProgramPath() + "update.exe"));
                System.exit(0);
            } catch ( Exception e ) {
				Methods.loadPopup("Download Update Error 1", PopupController.ERROR, Update.getPopX(), Update.getPopY());
            }
        }

        public void rbcProgressCallback( RBCWrapper rbc, double progress ) {
            UpdateController.updateProgress(progress);
        }

        private int contentLength( URL url ) {
            HttpURLConnection           connection;
            int                         contentLength = -1;

            try {
                HttpURLConnection.setFollowRedirects( true );

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod( "HEAD" );

                contentLength = connection.getContentLength();
            } catch ( Exception e ) { 
				Methods.loadPopup("Download Update Error 3", PopupController.ERROR, Update.getPopX(), Update.getPopY());
            }

            return contentLength;
        }
    }

    private static final class RBCWrapper implements ReadableByteChannel {
        private RBCWrapperDelegate              delegate;
        private long                            expectedSize;
        private ReadableByteChannel             rbc;
        private long                            readSoFar;

        RBCWrapper( ReadableByteChannel rbc, long expectedSize, RBCWrapperDelegate delegate ) {
            this.delegate = delegate;
            this.expectedSize = expectedSize;
            this.rbc = rbc;
        }

        public void close() throws IOException { rbc.close(); }
        @SuppressWarnings("unused")
		public long getReadSoFar() { return readSoFar; }
        public boolean isOpen() { return rbc.isOpen(); }

        public int read( ByteBuffer bb ) throws IOException {
            int                     n;
            double                  progress;

            if ( ( n = rbc.read( bb ) ) > 0 ) {
                readSoFar += n;
                progress = expectedSize > 0 ? (double) readSoFar / (double) expectedSize * 100.0 : -1.0;
                delegate.rbcProgressCallback( this, progress );
            }

            return n;
        }
    }
}
