package com.github.ambry.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class ByteBufferInputStream extends InputStream {
  private ByteBuffer byteBuffer;
  private int mark;
  private int readLimit;

  public ByteBufferInputStream(ByteBuffer byteBuffer) {
    this.byteBuffer = byteBuffer;
    this.mark = -1;
    this.readLimit = -1;
  }

  /**
   * Reads 'size' amount of bytes from the stream into the buffer.
   * @param stream The stream from which bytes need to be read. If the underlying stream is SocketInputStream, it needs
   *               to be blocking
   * @param size The size that needs to be read from the stream
   * @throws IOException
   */
  public ByteBufferInputStream(InputStream stream, int size)
      throws IOException {
    this.byteBuffer = ByteBuffer.allocate(size);
    int read = 0;
    ReadableByteChannel readableByteChannel = Channels.newChannel(stream);
    while (read < size) {
      int sizeRead = readableByteChannel.read(byteBuffer);
      if (sizeRead == 0 || sizeRead == -1) {
        throw new IOException("Total size read " + read + " is less than the size to be read " + size);
      }
      read += sizeRead;
    }
    byteBuffer.flip();
    this.mark = -1;
    this.readLimit = -1;
  }

  @Override
  public int read()
      throws IOException {
    if (!byteBuffer.hasRemaining()) {
      return -1;
    }
    return byteBuffer.get() & 0xFF;
  }

  @Override
  public int read(byte[] bytes, int offset, int length)
      throws IOException {
    int count = Math.min(byteBuffer.remaining(), length);
    if (count == 0) {
      return -1;
    }
    byteBuffer.get(bytes, offset, count);
    return count;
  }

  @Override
  public int available()
      throws IOException {
    return byteBuffer.remaining();
  }

  @Override
  public synchronized void reset()
      throws IOException {
    if (readLimit == -1 || mark == -1) {
      throw new IOException("Mark not set before reset invoked.");
    }
    if (byteBuffer.position() - mark > readLimit) {
      throw new IOException("Read limit exceeded before reset invoked.");
    }
    byteBuffer.reset();
  }

  @Override
  public synchronized void mark(int readLimit) {
    this.mark = byteBuffer.position();
    this.readLimit = readLimit;
    byteBuffer.mark();
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  public ByteBufferInputStream duplicate() {
    return new ByteBufferInputStream(byteBuffer.duplicate());
  }
}

